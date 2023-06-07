package org.um.feri.ears.problems.dynamic.cec2009;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;

public class DynamicRotationProblem extends DynamicProblem {

    private Double widthSeverity;   // width severity for each peak
    private Double minWidth, maxWidth;  // peak width

    public DynamicRotationProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints,
                                  int numberOfPeaksOrFunctions, Double minHeight, Double maxHeight, Double chaoticConstant, ChangeType changeType,
                                  int periodicity, boolean isDimensionChanged, int minDimension, int maxDimension, Double minWidth, Double maxWidth,
                                  int changeFrequency, int heightSeverity, Double gLowerLimit, Double gUpperLimit, Double widthSeverity) {

        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints,
                numberOfPeaksOrFunctions, minHeight, maxHeight, chaoticConstant, changeType, periodicity,
                isDimensionChanged, minDimension, maxDimension, changeFrequency, heightSeverity, gLowerLimit, gUpperLimit);

        this.widthSeverity = widthSeverity;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
    }

    @Override
    public void setWeight(Double weight) {
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            if (changeType == ChangeType.CHAOTIC) {
                this.weight[i] = minWidth + (maxWidth - minWidth) * new Random().nextGaussian();    // TODO: use appropriate random
            } else {
                this.weight[i] = weight;
            }
        }
    }

    @Override
    public void setPeriodicity(int periodicity) {
        if (periodicity < 1) {
            return; // TODO
        }
        super.setPeriodicity(periodicity);
    }

    public void widthStandardChange() {
        Double step;
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
        globalOptima = Arrays.stream(peakHeight).max(Comparator.comparing(Double::doubleValue)).orElseThrow(NoSuchElementException::new);
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            if (peakHeight[i] == globalOptima) {
                System.arraycopy(position[i], 0, globalOptimaPosition, 0, numberOfDimensions);
            }
        }
    }

    @Override
    public void increaseDimension(int newDimension) {
        // TODO
    }

    @Override
    public void decreaseDimension(int newDimension) {
        // TODO
    }

    @Override
    public void makeChange() {
        changeCounter++;
        switch (changeType) {
            case SMALL_STEP:
                heightStandardChange();
                widthStandardChange();
                positionStandardChange(0);
                calculateGlobalOptima();
                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.SMALL_STEP);
                break;
            case LARGE_STEP:
                heightStandardChange();
                widthStandardChange();
                positionStandardChange(0);
                calculateGlobalOptima();
                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.LARGE_STEP);
                break;
            case U_RANDOM:
                heightStandardChange();
                widthStandardChange();
                positionStandardChange(0);
                calculateGlobalOptima();
                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.U_RANDOM);
                break;
            case RECURRENT:
                double initialAngle;
                double heightRange = maxHeight - minHeight;
                double widthRange = maxWidth - minWidth;
                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    initialAngle = (double) periodicity * i / numberOfPeaksOrFunctions;
                    peakHeight[i] = minHeight + heightRange * (Math.sin(2 * Math.PI * (changeTypeCounter.getNumberOfOccurrences(ChangeType.RECURRENT) + initialAngle) / periodicity) + 1) / 2.;
                    weight[i] = minWidth + widthRange * (Math.sin(2 * Math.PI * (changeTypeCounter.getNumberOfOccurrences(ChangeType.RECURRENT) + initialAngle) / periodicity) + 1) / 2.;
                }
                initialAngle = Math.PI * (Math.sin(2 * Math.PI * (changeTypeCounter.getNumberOfOccurrences(ChangeType.RECURRENT)) / periodicity) + 1) / 12.;
                positionStandardChange(initialAngle);

                calculateGlobalOptima();

                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.RECURRENT);
                break;
            case CHAOTIC:
                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    peakHeight[i] = getChaoticValue(peakHeight[i], minHeight, maxHeight);
                    weight[i] = getChaoticValue(weight[i], minWidth, maxWidth);
                }
                positionStandardChange(0);
                calculateGlobalOptima();
                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.CHAOTIC);
                break;
            case RECURRENT_NOISY:
                double initialAngle2;   // TODO: poimenovanje za vse tri spremenljivke
                double heightRange2 = maxHeight - minHeight;
                double widthRange2 = maxWidth - minWidth;
                double noisy;
                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    initialAngle2 = (double) periodicity * i / numberOfPeaksOrFunctions;
                    peakHeight[i] = sinValueNoisy(changeTypeCounter.getNumberOfOccurrences(ChangeType.RECURRENT_NOISY), minHeight, maxHeight, heightRange2, initialAngle2, recurrentNoisySeverity);
                    weight[i] = sinValueNoisy(changeTypeCounter.getNumberOfOccurrences(ChangeType.RECURRENT_NOISY), minWidth, maxWidth, widthRange2, initialAngle2, recurrentNoisySeverity);
                }
                initialAngle2 = Math.PI * (Math.sin(2 * Math.PI * (changeTypeCounter.getNumberOfOccurrences(ChangeType.RECURRENT_NOISY)) / periodicity) + 1) / 12.;
                noisy = recurrentNoisySeverity * new Random().nextGaussian();    // TODO: use appropriate random
                positionStandardChange(initialAngle2 + noisy);

                calculateGlobalOptima();
                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.RECURRENT_NOISY);
                break;
        }
    }
}
