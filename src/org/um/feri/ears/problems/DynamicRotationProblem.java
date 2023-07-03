package org.um.feri.ears.problems;

import org.um.feri.ears.benchmark.CEC2009DynamicBenchmark;
import org.um.feri.ears.problems.dynamic.cec2009.ChangeType;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public class DynamicRotationProblem extends DynamicProblem {

    private final double widthSeverity;   // width severity for each peak
    private final double minWidth, maxWidth;  // peak width

    public DynamicRotationProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints,
                                  int numberOfPeaksOrFunctions, double minHeight, double maxHeight, double chaoticConstant, ChangeType changeType,
                                  int periodicity, boolean isDimensionChanged, int minDimension, int maxDimension, double minWidth, double maxWidth,
                                  int changeFrequency, int heightSeverity, double gLowerLimit, double gUpperLimit, double widthSeverity) {

        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints,
                numberOfPeaksOrFunctions, minHeight, maxHeight, chaoticConstant, changeType, periodicity,
                isDimensionChanged, minDimension, maxDimension, changeFrequency, heightSeverity, gLowerLimit, gUpperLimit);

        this.widthSeverity = widthSeverity;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        setWeight(5);   // TODO: pass value to constructor? between 1 and 10
        calculateGlobalOptima();
    }

    @Override
    public void setWeight(double weight) {
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            if (changeType == ChangeType.CHAOTIC) {
                this.weight[i] = minWidth + (maxWidth - minWidth) * CEC2009DynamicBenchmark.myRandom.nextDouble();    // TODO: use appropriate random
            } else {
                this.weight[i] = weight;
            }
        }
    }

    public void widthStandardChange() {
        double step;
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            step = widthSeverity * standardChange(minWidth, maxWidth);
            weight[i] = weight[i] + step;

            if (weight[i] > maxWidth || weight[i] < minWidth) {
                weight[i] = weight[i] - step;
            }
        }
    }

    @Override
    protected void calculateGlobalOptima() {
        globalOptima = Arrays.stream(peakHeights).max().orElseThrow(NoSuchElementException::new);
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            if (peakHeights[i] == globalOptima) {
                System.arraycopy(peakPositions[i], 0, decisionSpaceOptima[0], 0, numberOfDimensions);
            }
        }
    }

    @Override
    public void increaseDimension(int newDimension, int changeCounter) {
        numberOfDimensions = newDimension;
        int newDimensionIndex = newDimension - 1;

        if (changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY) {
            for (int i = 0; i < periodicity; i++) {
                if (changeCounter <= i) {
                    break;
                }
                for (int j = 0; j < numberOfPeaksOrFunctions; j++) {
                    System.arraycopy(rotationPlanes[i][j], 0, rotationPlanes[i][j], 0, newDimensionIndex);   // TODO: mogoče ne rabim, ker imam velikost nastavljeno na 'maxDimension'?
                }
            }
        }

        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            peakPositions[i][newDimensionIndex] = gLowerLimit + (gUpperLimit - gLowerLimit) * CEC2009DynamicBenchmark.myRandom.nextDouble();    // TODO: use appropriate random
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
    public void decreaseDimension(int newDimension, int changeCounter) {
        numberOfDimensions = newDimension;

        if (changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY) {
            for (int i = 0; i < periodicity; i++) {
                if (changeCounter <= i) {
                    break;
                }
                for (int j = 0; j < numberOfPeaksOrFunctions; j++) {
                    for (int m = 0, k = 0; k < newDimension; k++, m++) {
                        if (rotationPlanes[i][j][m] == newDimension) {
                            k--;
                            continue;
                        } else {
                            rotationPlanes[i][j][k] = rotationPlanes[i][j][m];
                        }
                    }
                }
            }
        }

        calculateGlobalOptima();
    }

    @Override
    public void performChange(int changeCounter) {
        switch (changeType) {
            case SMALL_STEP:
                heightStandardChange();
                widthStandardChange();
                positionStandardChange(0, changeCounter);
                calculateGlobalOptima();
                break;
            case LARGE_STEP:
                heightStandardChange();
                widthStandardChange();
                positionStandardChange(0, changeCounter);
                calculateGlobalOptima();
                break;
            case U_RANDOM:
                heightStandardChange();
                widthStandardChange();
                positionStandardChange(0, changeCounter);
                calculateGlobalOptima();
                break;
            case RECURRENT:
                double initialAngle;
                double heightRange = maxHeight - minHeight;
                double widthRange = maxWidth - minWidth;
                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    initialAngle = (double) periodicity * i / numberOfPeaksOrFunctions;
                    peakHeights[i] = minHeight + heightRange * (Math.sin(2 * Math.PI * (changeCounter + initialAngle) / periodicity) + 1) / 2.;
                    weight[i] = minWidth + widthRange * (Math.sin(2 * Math.PI * (changeCounter + initialAngle) / periodicity) + 1) / 2.;
                }
                initialAngle = Math.PI * (Math.sin(2 * Math.PI * changeCounter / periodicity) + 1) / 12.;
                positionStandardChange(initialAngle, changeCounter);
                calculateGlobalOptima();
                break;
            case CHAOTIC:
                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    peakHeights[i] = getChaoticValue(peakHeights[i], minHeight, maxHeight);
                    weight[i] = getChaoticValue(weight[i], minWidth, maxWidth);
                }
                positionStandardChange(0, changeCounter);
                calculateGlobalOptima();
                break;
            case RECURRENT_NOISY:
                double initialAngle2;   // TODO: poimenovanje za vse tri spremenljivke
                double heightRange2 = maxHeight - minHeight;
                double widthRange2 = maxWidth - minWidth;
                double noisy;
                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    initialAngle2 = (double) periodicity * i / numberOfPeaksOrFunctions;
                    peakHeights[i] = sinValueNoisy(changeCounter, minHeight, maxHeight, heightRange2, initialAngle2, recurrentNoisySeverity);
                    weight[i] = sinValueNoisy(changeCounter, minWidth, maxWidth, widthRange2, initialAngle2, recurrentNoisySeverity);
                }
                initialAngle2 = Math.PI * (Math.sin(2 * Math.PI * changeCounter / periodicity) + 1) / 12.;
                noisy = recurrentNoisySeverity * CEC2009DynamicBenchmark.myRandom.nextGaussian();    // TODO: use appropriate random
                positionStandardChange(initialAngle2 + noisy, changeCounter);
                calculateGlobalOptima();
                break;
        }

        if (dimensionChanging) {
            changeDimension(changeCounter);
        }
    }

    @Override
    public double eval(double[] x) {
        double[] peakFitness = new double[numberOfPeaksOrFunctions];
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            peakFitness[i] = 0.0;
            for (int j = 0; j < numberOfDimensions; j++)
                peakFitness[i] += (x[j] - peakPositions[i][j]) * (x[j] - peakPositions[i][j]);
            peakFitness[i] = Math.sqrt(peakFitness[i] / numberOfDimensions);
            peakFitness[i] = peakHeights[i] / (1 + weight[i] * peakFitness[i]);
        }
        return Arrays.stream(peakFitness).max().orElseThrow(NoSuchElementException::new);
    }
}
