package org.um.feri.ears.problems;

import org.um.feri.ears.problems.dynamic.cec2009.BasicFunction;
import org.um.feri.ears.problems.dynamic.cec2009.ChangeType;
import org.um.feri.ears.problems.dynamic.cec2009.Matrix;
import org.um.feri.ears.problems.unconstrained.*;
import org.um.feri.ears.util.Util;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class DynamicCompositionProblem extends DynamicProblem {

    /**
     * Which basic function is used to compose the composition function.
     */
    private DoubleProblem[] basicFunctions;
    /**
     * Severity of converge range for each function.
     */
    private double[] convergeSeverity;
    /**
     * Severity of stretching original function, greater than 1 for stretch, less than 1 for compress the original function.
     */
    private double[] stretchSeverity;
    /**
     * The constant number for normalizing all basic functions with similar height.
     */
    private final double heightNormalizeSeverity;

    private final BasicFunction basicFunction;  // TODO: It should be removed when the array with all basic functions is passed to the class's constructor.

    public DynamicCompositionProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives,
                                     int numberOfConstraints, int numberOfPeaksOrFunctions, double minHeight, double maxHeight,
                                     double chaoticConstant, ChangeType changeType, int periodicity, boolean dimensionChanging,
                                     int minDimension, int maxDimension, int changeFrequency, int heightSeverity,
                                     double heightNormalizeSeverity, double gLowerLimit, double gUpperLimit, BasicFunction basicFunction) {

        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints,
                numberOfPeaksOrFunctions, minHeight, maxHeight, chaoticConstant, changeType, periodicity,
                dimensionChanging, minDimension, maxDimension, changeFrequency, heightSeverity, gLowerLimit, gUpperLimit);

        objectiveMaximizationFlags[0] = false;

        this.heightNormalizeSeverity = heightNormalizeSeverity;

        this.basicFunction = basicFunction;
        basicFunctions = new DoubleProblem[numberOfPeaksOrFunctions];
        initBasicFunctions(basicFunction);

        convergeSeverity = new double[numberOfPeaksOrFunctions];
        Arrays.fill(convergeSeverity, 1.0);
        stretchSeverity = new double[numberOfPeaksOrFunctions];
        initStretchSeverity();
        setRotationMatrix();
        calculateGlobalOptima();
    }

    // TODO: Create an array outside of the class and pass it to the constructor. Currently, the class is fixed
    // TODO: to the CEC 2009 Dynamic Optimization Competition Benchmark Generator scenarios.
    private void initBasicFunctions(BasicFunction basicFunction) {
        switch (basicFunction) {
            case SPHERE:
                Arrays.fill(basicFunctions, new Sphere(numberOfDimensions));
                break;
            case RASTRIGIN:
                Arrays.fill(basicFunctions, new Rastrigin(numberOfDimensions));
                break;
            case GRIEWANK:
                Arrays.fill(basicFunctions, new Griewank(numberOfDimensions));
                break;
            case ACKLEY:
                Arrays.fill(basicFunctions, new Ackley1(numberOfDimensions));
                break;
            case MIXED:
                // if (basicFunctions.length != 10) throw new IllegalStateException("The number of dimension should be 10 for CEC2009DOBenchmark.");
                basicFunctions[0] = basicFunctions[1] = new Sphere(numberOfDimensions);
                basicFunctions[2] = basicFunctions[3] = new Rastrigin(numberOfDimensions);
                basicFunctions[4] = basicFunctions[5] = new Weierstrass(numberOfDimensions);
                basicFunctions[6] = basicFunctions[7] = new Griewank(numberOfDimensions);
                basicFunctions[8] = basicFunctions[9] = new Ackley1(numberOfDimensions);
                break;
            default:
                throw new IllegalArgumentException("Unknown BasicFunction value passed in constructor.");
        }
    }

    private void initStretchSeverity() {
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            stretchSeverity[i] = convergeSeverity[i] * (gUpperLimit - gLowerLimit) / (basicFunctions[i].getUpperLimit(0) - basicFunctions[i].getLowerLimit(0));
        }
    }

    @Override
    public void increaseDimension(int newDimension, int changeCounter) {
        numberOfDimensions = newDimension;
        int newDimensionIndex = newDimension - 1;

        initBasicFunctions(basicFunction);

        if (changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY) {
            for (int i = 0; i < periodicity; i++) {
                if (changeCounter <= i) {
                    break;
                }
                for (int j = 0; j < numberOfPeaksOrFunctions; j++) {
                    System.arraycopy(rotationPlanes[i][j], 0, rotationPlanes[i][j], 0, numberOfDimensions);
                }
            }
        }

        setRotationMatrix();

        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            peakPositions[i][newDimensionIndex] = gLowerLimit + (gUpperLimit - gLowerLimit) * Util.nextDouble();
            initialPeakPositions[i][newDimensionIndex] = peakPositions[i][newDimensionIndex];
        }

        if (changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY) {
            for (int i = 0; i < periodicity; i++) {
                if (changeCounter <= i) {
                    break;
                }
                for (int j = 0; j < numberOfPeaksOrFunctions; j++) {
                    rotationPlanes[i][j][newDimensionIndex] = newDimensionIndex;
                }
            }
        }


        calculateGlobalOptima();
    }

    @Override
    protected void calculateGlobalOptima() {
        globalOptima = Arrays.stream(peakHeights).min().orElseThrow(NoSuchElementException::new);   // Global.extremum(height, numPeakOrFun, Compare.MIN);
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            if (peakHeights[i] == globalOptima) {
                System.arraycopy(peakPositions[i], 0, decisionSpaceOptima[0], 0, numberOfDimensions);
            }
        }
    }

    @Override
    public void decreaseDimension(int newDimension, int changeCounter) {
        numberOfDimensions = newDimension;

        initBasicFunctions(basicFunction);

        if (changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY) {
            for (int i = 0; i < periodicity; i++) {
                if (changeCounter <= i) {
                    break;
                }
                for (int j = 0; j < numberOfPeaksOrFunctions; j++) {
                    for (int m = 0, k = 0; k < newDimension; k++, m++) {
                        if (rotationPlanes[i][j][m] == newDimension) {
                            k--;
                        } else {
                            rotationPlanes[i][j][k] = rotationPlanes[i][j][m];
                        }
                    }
                }
            }
        }

        setRotationMatrix();

        calculateGlobalOptima();
    }

    /**
     * Randomly generate rotation matrix for each basic function.
     * For each basic function of dimension n(even number), R=R(l1,l2)*R(l3,l4)*....*R(ln-1,ln), 0<=li<=n
     */
    public void setRotationMatrix() {
        //
        Matrix I = new Matrix(numberOfDimensions, numberOfDimensions);

        int[] d = new int[numberOfDimensions];
        initializeRandomArray(d, numberOfDimensions);
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            for (int j = 0; j + 1 < numberOfDimensions; j += 2) {
                double angle = 2 * Math.PI * Util.nextDouble();   // random angle for rotation plane of d[j]-d[j+1] from d[j]th axis to d[j+1]th axis
                I.setRotation(d[j], d[j + 1], angle);
                if (j == 0) {
                    rotationMatrix[i] = I;
                } else {
                    rotationMatrix[i] = rotationMatrix[i].multiply(I);
                }
                I.identity();
            }
        }
    }

    @Override
    public void performChange(int changeCounter) {
        switch (changeType) {
            case SMALL_STEP:
                heightStandardChange();
                positionStandardChange(0, changeCounter);
                calculateGlobalOptima();
                break;
            case LARGE_STEP:
                heightStandardChange();
                positionStandardChange(0, changeCounter);
                calculateGlobalOptima();
                break;
            case U_RANDOM:
                // change the global minimum value of each function
                heightStandardChange();
                // change the position of global optimum of each function randomly
                positionStandardChange(0, changeCounter);
                calculateGlobalOptima();
                break;
            case RECURRENT: {
                double initialAngle;
                double heightRange = maxHeight - minHeight;

                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    initialAngle = (double) periodicity * i / numberOfPeaksOrFunctions;
                    peakHeights[i] = minHeight + heightRange * (Math.sin(2 * Math.PI * (changeCounter + initialAngle) / periodicity) + 1) / 2.;
                }
                initialAngle = Math.PI * (Math.sin(2 * Math.PI * changeCounter / periodicity) + 1) / 12.;
                positionStandardChange(initialAngle, changeCounter);
                calculateGlobalOptima();
                break;
            }
            case CHAOTIC:
                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    peakHeights[i] = getChaoticValue(peakHeights[i], minHeight, maxHeight);
                }
                positionStandardChange(0, changeCounter);
                calculateGlobalOptima();
                break;
            case RECURRENT_NOISY: {
                double initialAngle;
                double heightRange = maxHeight - minHeight;

                double noisy;
                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    initialAngle = (double) periodicity * i / numberOfPeaksOrFunctions;
                    peakHeights[i] = sinValueNoisy(changeCounter, minHeight, maxHeight, heightRange, initialAngle, recurrentNoisySeverity);
                }
                initialAngle = Math.PI * (Math.sin(2 * Math.PI * changeCounter / periodicity) + 1) / 12.;
                noisy = recurrentNoisySeverity * Util.nextGaussian();
                positionStandardChange(initialAngle + noisy, changeCounter);
                calculateGlobalOptima();
                break;
            }
        }

        if (dimensionChanging) {
            changeDimension(changeCounter);
        }
    }


    @Override
    public double eval(double[] x) {
        double[] tempX = new double[numberOfDimensions];
        double[] functionFitness = new double[numberOfPeaksOrFunctions];

        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {    // calculate weight for each function
            weight[i] = 0.0;
            for (int j = 0; j < numberOfDimensions; j++) {
                weight[i] += (x[j] - peakPositions[i][j]) * (x[j] - peakPositions[i][j]);
            }
            weight[i] = Math.exp(-Math.sqrt(weight[i] / (2 * numberOfDimensions * convergeSeverity[i] * convergeSeverity[i])));
        }

        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {    // calculate objective value for each function
            for (int j = 0; j < numberOfDimensions; j++) {  // calculate the objective value of tranformation function i
                tempX[j] = (x[j] - peakPositions[i][j]) / stretchSeverity[i]; // ((1+fabs(position[i][j]/boundary[j].upper))*
            }
            Matrix m = new Matrix(numberOfDimensions, 1);
            m.setData(tempX, numberOfDimensions);

            m = m.multiply(rotationMatrix[i]);

            System.arraycopy(m.getData()[0], 0, tempX, 0, numberOfDimensions);
            basicFunctions[i].setFeasible(tempX); // correction(componentFunction[i]);
            functionFitness[i] = basicFunctions[i].eval(tempX);

            for (int j = 0; j < numberOfDimensions; j++) {   // calculate the estimate max value of function i
                tempX[j] = gUpperLimit / stretchSeverity[i];
            }
            m.setData(tempX, numberOfDimensions);
            m = m.multiply(rotationMatrix[i]);
            System.arraycopy(m.getData()[0], 0, tempX, 0, numberOfDimensions);
            basicFunctions[i].setFeasible(tempX);
            double fMax = basicFunctions[i].eval(tempX); // double fMax = selectFun(componentFunction[i]);
            if (fMax != 0) {
                functionFitness[i] = heightNormalizeSeverity * functionFitness[i] / Math.abs(fMax);
            }
        }

        /*
        if (objectiveMaximizationFlags[0]) {
            globalOptima = Arrays.stream(peakHeights).min(Comparator.comparing(Double::doubleValue)).orElseThrow(NoSuchElementException::new);   // Global.extremum(height, numPeakOrFun, Compare.MIN);
        }
        else {
            globalOptima = Arrays.stream(peakHeights).max(Comparator.comparing(Double::doubleValue)).orElseThrow(NoSuchElementException::new);
        }
        */

        double sumWeight = 0;
        double maxWeight = Arrays.stream(weight).max().orElseThrow(NoSuchElementException::new);
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            if (weight[i] != maxWeight) {
                weight[i] = weight[i] * (1 - Math.pow(maxWeight, 10));
            }
        }
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            sumWeight += weight[i];
        }

        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            weight[i] /= sumWeight;
        }

        double obj = 0;
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            obj += weight[i] * (functionFitness[i] + peakHeights[i]);
        }

        return obj;
    }
}
