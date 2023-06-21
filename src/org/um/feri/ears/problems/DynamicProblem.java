package org.um.feri.ears.problems;

import org.um.feri.ears.problems.dynamic.cec2009.ChangeType;
import org.um.feri.ears.problems.dynamic.cec2009.Matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public abstract class DynamicProblem extends DoubleProblem {

    protected int numberOfPeaksOrFunctions;
    protected double minHeight, maxHeight;    // minimum/maximum height of all peaks (local optima) in RotationDBG (CompositionDBG)
    private final double chaoticConstant;
    protected double[] peakHeights;    // peak height in RotationDBG, height of global optima in CompositionDBG
    protected ChangeType changeType;
    protected int periodicity;    // definite period for values repeating
    protected boolean dimensionChanging;  // true if the number of dimensions has changed, false otherwise
    private final int minDimensions, maxDimensions;
    private boolean isDimensionIncreasing;  // true if the direction should be changed, false otherwise
    private final int changeFrequency;    // number of evaluations between two successive changes
    private final int heightSeverity;
    protected double[][] peakPositions;  // positions of local or global optima (local optima in RotationDBG, global optima of basic function in CompositionDBG)
    protected double[][] initialPeakPositions;   // save the initial positions
    protected int[][][] rotationPlanes;    // save the planes rotated during one periodicity
    protected double[] weight;   // weight value of each basic function in CompositionDBG, peak width in RotationDBG
    protected float recurrentNoisySeverity; // deviation severity from the trajectory of recurrent change
    protected final double gLowerLimit, gUpperLimit;  // solution space // TODO: spada to samo v DynamicCompositionProblem?
    protected double globalOptima;  // global optima value
    protected Matrix[] rotationMatrix;  // orthogonal rotation matrices for each function

    public DynamicProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints,
                          int numberOfPeaksOrFunctions, double minHeight, double maxHeight, double chaoticConstant, ChangeType changeType,
                          int periodicity, boolean dimensionChanging, int minDimensions, int maxDimensions, int changeFrequency,
                          int heightSeverity, double gLowerLimit, double gUpperLimit) {

        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints);

        if ((changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY) && periodicity != 12) {
            throw new IllegalArgumentException("Periodicity must be 12 if changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY");
        }
        if ((changeType != ChangeType.RECURRENT && changeType != ChangeType.RECURRENT_NOISY) && periodicity != 0) {
            throw new IllegalArgumentException("Periodicity must be 0 if changeType != ChangeType.RECURRENT && changeType != ChangeType.RECURRENT_NOISY");
        }

        this.numberOfPeaksOrFunctions = numberOfPeaksOrFunctions;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.chaoticConstant = chaoticConstant;
        peakHeights = new double[numberOfPeaksOrFunctions];
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            if (changeType == ChangeType.CHAOTIC) {
                peakHeights[i] = minHeight + (maxHeight - minHeight) * new Random().nextDouble();    // TODO: use appropriate random
            } else {
                peakHeights[i] = 50;
            }
        }
        this.changeType = changeType;
        this.periodicity = periodicity;
        this.dimensionChanging = dimensionChanging;
        this.isDimensionIncreasing = true;
        this.minDimensions = minDimensions;
        this.maxDimensions = maxDimensions;
        this.changeFrequency = changeFrequency;
        this.heightSeverity = heightSeverity;
        peakPositions = new double[numberOfPeaksOrFunctions][];
        initialPeakPositions = new double[numberOfPeaksOrFunctions][];
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            peakPositions[i] = new double[maxDimensions];
            initialPeakPositions[i] = new double[maxDimensions];
        }
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            for (int j = 0; j < maxDimensions; j++) {
                peakPositions[i][j] = gLowerLimit + (gUpperLimit - gLowerLimit) * new Random().nextDouble();    // TODO: use appropriate random
                initialPeakPositions[i][j] = peakPositions[i][j];
            }
        }
        weight = new double[numberOfPeaksOrFunctions];
        this.gLowerLimit = gLowerLimit;
        this.gUpperLimit = gUpperLimit;
        rotationMatrix = new Matrix[numberOfPeaksOrFunctions];
        rotationPlanes = new int[periodicity][][];
        for (int i = 0; i < periodicity; i++) {
            rotationPlanes[i] = new int[numberOfPeaksOrFunctions][];
            for (int j = 0; j < numberOfPeaksOrFunctions; j++) {
                rotationPlanes[i][j] = new int[maxDimensions];
            }
        }

        lowerLimit = new ArrayList<>(Collections.nCopies(maxDimensions, gLowerLimit));
        upperLimit = new ArrayList<>(Collections.nCopies(maxDimensions, gUpperLimit));

        if (changeType == ChangeType.RECURRENT_NOISY) {
            recurrentNoisySeverity = 0.8f;
        }

        decisionSpaceOptima = new double[numberOfGlobalOptima][maxDimensions];  // globalOptimaPosition
    }

    public int getMinDimensions() {
        return minDimensions;
    }

    public int getMaxDimensions() {
        return maxDimensions;
    }

    public boolean isDimensionIncreasing() {
        return isDimensionIncreasing;
    }

    public boolean isDimensionChanging() {
        return dimensionChanging;
    }

    public int getChangeFrequency() {
        return changeFrequency;
    }

    public void setWeight(double weight) {
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            this.weight[i] = weight;
        }
    }

    // return a value calculated by the logistics function
    protected double getChaoticValue(final double x, final double min, final double max) {
        if (min > max) {
            return -1.0;
        }
        double chaoticValue;
        chaoticValue = (x - min) / (max - min);
        chaoticValue = chaoticConstant * chaoticValue * (1 - chaoticValue);
        return min + chaoticValue * (max - min);
    }

    // return a value in recurrent with noisy dynamism environment
    protected double sinValueNoisy(final int x, final double min, final double max, final double amplitude, final double angle, final double noisySeverity) {
        double y = min + amplitude * (Math.sin(2 * Math.PI * (x + angle) / periodicity) + 1) / 2.;
        double noisy = noisySeverity * new Random().nextGaussian();    // TODO: use appropriate random
        double t = y + noisy;
        return (t > min && t < max) ? t : (t - noisy);
    }

    // dimension changes (linear increase or decrease).
    public void changeDimension(int changeCounter) {
        if (!dimensionChanging) {
            return;
        }

        int newDimension = numberOfDimensions;
        if (numberOfDimensions == minDimensions) {
            isDimensionIncreasing = true;
        }
        if (numberOfDimensions == maxDimensions) {
            isDimensionIncreasing = false;
        }

        if (isDimensionIncreasing) {
            newDimension++;
        } else {
            newDimension--;
        }

        if (newDimension < numberOfDimensions) {
            decreaseDimension(newDimension, changeCounter);
        } else {
            increaseDimension(newDimension, changeCounter);
        }
    }

    protected abstract void calculateGlobalOptima();

    protected abstract void increaseDimension(int newDimension, int changeCounter);

    protected abstract void decreaseDimension(int newDimension, int changeCounter);

    public abstract void performChange(int changeCounter);

    public double standardChange(final double min, final double max) {
        double step = 0.0;
        int sign;

        final double ALPHA = 0.04;    // TODO: where to put this variable?  // TODO: ALPHA = 0.02 v članku, v originalni kodi za CEC'09 pa 0.04
        final double MAX_ALPHA = 0.1;   // TODO: where to put this variable?

        switch (changeType) {
            case SMALL_STEP:
                step = -1 + 2 * new Random().nextDouble();    // TODO: use appropriate random
                step = ALPHA * step * (max - min);
                break;
            case U_RANDOM:
                step = new Random().nextGaussian();    // TODO: use appropriate random
                break;
            case LARGE_STEP:
                step = -1 + 2 * new Random().nextDouble();    // TODO: use appropriate random
                if (step > 0) {
                    sign = 1;
                } else if (step < 0) {
                    sign = -1;
                } else {
                    sign = 0;
                }
                step = (ALPHA * sign + (MAX_ALPHA - ALPHA) * step) * (max - min);
                break;
            case RECURRENT:
            case CHAOTIC:
            case RECURRENT_NOISY:
                break;
        }
        return step;
    }

    public void heightStandardChange() {
        double step;
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            step = heightSeverity * standardChange(minHeight, maxHeight);
            peakHeights[i] = peakHeights[i] + step;
            if (peakHeights[i] > maxHeight || peakHeights[i] < minHeight) {
                peakHeights[i] = peakHeights[i] - step;
            }
        }
    }

    // TODO: glede na originalno kodo parameter 'angle' ni potreben, lahko je lokalna spremenljivka v metodi
    public void positionStandardChange(double angle, int changeCounter) {
        // for each basic function of dimension n(even number) , R = R(l1, l2) * R(l3, l4) * .... * R(li - 1, li), 0 <= li <= n
        if (changeType == ChangeType.CHAOTIC) {
            for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                for (int j = 0; j < numberOfDimensions; j++) {
                    peakPositions[i][j] = getChaoticValue(peakPositions[i][j], gLowerLimit, gUpperLimit);
                }
            }
            return;
        }
        int[] d = new int[numberOfDimensions];
        Matrix I = new Matrix(numberOfDimensions);
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            if ((changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY)
                    && changeCounter >= periodicity) {
                System.arraycopy(rotationPlanes[changeCounter % periodicity][i], 0, d, 0, numberOfDimensions);
            } else {
                initializeRandomArray(d, numberOfDimensions);
                if ((changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY) && changeCounter % periodicity == 0) {
                    System.arraycopy(d, 0, rotationPlanes[changeCounter][i], 0, numberOfDimensions);
                }

                if ((changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY) && changeCounter % periodicity == 0) {
                    System.arraycopy(initialPeakPositions[i], 0, peakPositions[i], 0, numberOfDimensions);
                }

                I.identity();
                for (int j = 0; j + 1 < numberOfDimensions; j += 2) {
                    if (changeType == ChangeType.SMALL_STEP || changeType == ChangeType.LARGE_STEP || changeType == ChangeType.U_RANDOM) {
                        angle = standardChange(-Math.PI, Math.PI);
                    }
                    I.setRotation(d[j], d[j + 1], angle);
                    if (j == 0) {
                        rotationMatrix[i] = I;
                    } else {
                        rotationMatrix[i] = rotationMatrix[i].multiply(I);
                    }
                    Matrix m = new Matrix(numberOfDimensions, 1);
                    m.setData(peakPositions[i], numberOfDimensions);
                    m = m.multiply(rotationMatrix[i]);
                    // System.arraycopy(m.getData()[0], 0, genes, 0, dimension); // TODO: preveri kaj so genes, še jih ne uporabljaš
                    // correction(); // TODO: tukaj bo verjetno klic metode makeFeasible
                    // System.arraycopy(genes, 0, position[i], 0, dimension); // TODO
                }
            }
        }
    }

    // TODO: nisem prepričani, če je to pravo mesto za to metodo
    // generate a set of random numbers from 0-|a| without repeat
    public void initializeRandomArray(int[] array, int dim) {
        int[] temp = new int[dim];
        for (int i = 0; i < dim; i++) {
            temp[i] = i;
        }
        int d = dim;
        for (int i = 0; i < dim; i++) {
            int t = (int) (d * new Random().nextDouble());  // TODO: use appropriate random
            array[i] = temp[t];
            for (int k = t; k < d - 1; k++) {
                temp[k] = temp[k + 1];
            }
            d--;
        }
    }
}
