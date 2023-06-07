package org.um.feri.ears.problems.dynamic.cec2009;

public class DynamicRotationProblem extends DynamicProblem {

    private Double minWidth, maxWidth;  // peak width
    private Double widthSeverity;   // width severity for each peak

    public DynamicRotationProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints,
                                  int numberOfPeaksOrFunctions, Double minHeight, Double maxHeight, Double chaoticConstant, ChangeType changeType,
                                  int periodicity, boolean isDimensionChanged, int minDimension, int maxDimension, Double minWidth, Double maxWidth,
                                  int changeFrequency, int heightSeverity, Double gLowerLimit, Double gUpperLimit) {
        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints,
                numberOfPeaksOrFunctions, minHeight, maxHeight, chaoticConstant, changeType, periodicity,
                isDimensionChanged, minDimension, maxDimension, changeFrequency, heightSeverity, gLowerLimit, gUpperLimit);
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        widthSeverity = 0.0;
    }

    @Override
    public void increaseDimension(int newDimension) {

    }

    @Override
    public void decreaseDimension(int newDimension) {

    }

    @Override
    public void makeChange() {
        changeCounter++;
        switch (changeType) {
            case SMALL_STEP:
                // TODO
                break;
            case LARGE_STEP:
                // TODO
                break;
            case U_RANDOM:
                // TODO
                break;
            case RECURRENT:
                // TODO
                break;
            case CHAOTIC:
                // TODO
                break;
            case RECURRENT_NOISY:
                // TODO
                break;
        }
    }
}
