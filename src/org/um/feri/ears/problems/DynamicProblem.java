package org.um.feri.ears.problems;

import org.um.feri.ears.problems.dynamic.cec2009.ChangeType;
import org.um.feri.ears.problems.dynamic.cec2009.Matrix;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class DynamicProblem extends DoubleProblem {

    protected int numberOfPeaksOrFunctions;
    /**
     * Minimum/Maximum height of all peaks (local optima) in RotationDBG (CompositionDBG).
     */
    protected double minHeight, maxHeight;
    private final double chaoticConstant;
    /**
     * Peak height in RotationDBG, height of global optima in CompositionDBG.
     */
    protected double[] peakHeights;
    protected ChangeType changeType;
    /**
     * Definite period for values repeating.
     */
    protected int periodicity;
    /**
     * True if the number of dimensions has changed, false otherwise.
     */
    protected boolean dimensionChanging;
    protected final int minDimensions, maxDimensions;
    /**
     * True if the direction should be changed, false otherwise.
     */
    private boolean isDimensionIncreasing;
    /**
     * Number of evaluations between two successive changes.
     */
    private final int changeFrequency;
    private final int heightSeverity;
    /**
     * Positions of local or global optima (local optima in RotationDBG, global optima of basic function in CompositionDBG).
     */
    protected double[][] peakPositions;
    /**
     * Save the initial positions.
     */
    protected double[][] initialPeakPositions;
    /**
     * Save the planes rotated during one periodicity.
     */
    protected int[][][] rotationPlanes;
    /**
     * Weight value of each basic function in CompositionDBG, peak width in RotationDBG.
     */
    protected double[] weight;
    /**
     * Deviation severity from the trajectory of recurrent change.
     */
    protected float recurrentNoisySeverity;
    /**
     * Solution space.
     */
    protected final double gLowerLimit, gUpperLimit;
    /**
     * Global optima value.
     */
    protected double globalOptima;
    /**
     * Orthogonal rotation matrices for each function.
     */
    protected Matrix[] rotationMatrix;

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
        this.changeType = changeType;
        peakHeights = new double[numberOfPeaksOrFunctions];
        initPeakHeights();
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
            for (int j = 0; j < numberOfDimensions; j++) {
                peakPositions[i][j] = gLowerLimit + (gUpperLimit - gLowerLimit) * Util.nextDouble();
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

    private void initPeakHeights() {
        if (changeType == ChangeType.CHAOTIC) {
            for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                peakHeights[i] = minHeight + (maxHeight - minHeight) * Util.nextGaussian();
            }
        } else {
            Arrays.fill(peakHeights, 50.0);
        }
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

    /**
     * Return a value calculated by the logistics function.
     */
    protected double getChaoticValue(final double x, final double min, final double max) {
        if (min > max) {
            return -1.0;
        }
        double chaoticValue;
        chaoticValue = (x - min) / (max - min);
        chaoticValue = chaoticConstant * chaoticValue * (1 - chaoticValue);
        return min + chaoticValue * (max - min);
    }

    /**
     * Return a value in recurrent with noisy dynamism environment.
     */
    protected double sinValueNoisy(final int x, final double min, final double max, final double amplitude, final double angle, final double noisySeverity) {
        double y = min + amplitude * (Math.sin(2 * Math.PI * (x + angle) / periodicity) + 1) / 2.;
        double noisy = noisySeverity * Util.nextGaussian();
        double t = y + noisy;
        return (t > min && t < max) ? t : (t - noisy);
    }

    /**
     * Dimension changes (linear increase or decrease).
     */
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

        final double ALPHA = 0.04;
        final double MAX_ALPHA = 0.1;

        switch (changeType) {
            case SMALL_STEP:
                step = -1 + 2 * Util.nextDouble();
                step = ALPHA * step * (max - min);
                break;
            case U_RANDOM:
                step = Util.nextGaussian();
                break;
            case LARGE_STEP:
                step = -1 + 2 * Util.nextDouble();
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
            if ((changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY) && changeCounter >= periodicity) {
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
                    System.arraycopy(m.getData()[0], 0, peakPositions[i], 0, numberOfDimensions);
                    setFeasible(peakPositions[i]);
                }
            }
        }
    }

    /**
     * Generate a set of random numbers from 0-|array| without repeat.
     */
    public void initializeRandomArray(int[] array, int dim) {
        int[] temp = new int[dim];
        for (int i = 0; i < dim; i++) {
            temp[i] = i;
        }
        int d = dim;
        for (int i = 0; i < dim; i++) {
            int t = (int) (d * Util.nextDouble());
            array[i] = temp[t];
            for (int k = t; k < d - 1; k++) {
                temp[k] = temp[k + 1];
            }
            d--;
        }
    }
}
