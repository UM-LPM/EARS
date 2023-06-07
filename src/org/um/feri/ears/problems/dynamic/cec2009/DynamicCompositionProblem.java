package org.um.feri.ears.problems.dynamic.cec2009;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.*;

import java.util.Random;

public class DynamicCompositionProblem extends DynamicProblem {

    private Problem[] basicFunctions;   // TODO: ne vem še ali rabim ta array, mogoče bom rešil drugače
    private Double[] convergeSeverity;  // severity of converge range for each function
    private Double[] stretchSeverity;   // severity of stretching original function, greater than 1 for stretch, less than 1 for compress the original function
    private final Double heightNormalizeSeverity;   // the constant number for normalizing all basic functions with similar height
    // private BasicFunction[] componentFunction;  // which basic function used to compose the composition function TODO: ne razumem poante te spremenljivke v izvorni kodi...

    public DynamicCompositionProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives,
                                     int numberOfConstraints, int numberOfPeaksOrFunctions, Double minHeight, Double maxHeight,
                                     Double chaoticConstant, ChangeType changeType, int periodicity, boolean isDimensionChanged,
                                     int minDimension, int maxDimension, int changeFrequency, int heightSeverity,
                                     Double heightNormalizeSeverity, Double gLowerLimit, Double gUpperLimit) {

        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints,
                numberOfPeaksOrFunctions, minHeight, maxHeight, chaoticConstant, changeType, periodicity,
                isDimensionChanged, minDimension, maxDimension, changeFrequency, heightSeverity, gLowerLimit, gUpperLimit);

        this.heightNormalizeSeverity = heightNormalizeSeverity;
        convergeSeverity = new Double[numberOfPeaksOrFunctions];
        stretchSeverity = new Double[numberOfPeaksOrFunctions];
        setStretchSeverity();
        // componentFunction = new BasicFunction[numberOfPeaksOrFunctions];

        // TODO: razmisli kako bi to drugače implementiral, ker trenutno se mi ne "sveti" kako bi
        basicFunctions = new Problem[BasicFunction.values().length];    // 5 basic functions (Sphere, Rastrigin, Weierstrass, Griewank, Ackley)
        basicFunctions[BasicFunction.SPHERE.ordinal()] = new Sphere(numberOfDimensions);
        basicFunctions[BasicFunction.RASTRIGIN.ordinal()] = new Rastrigin(numberOfDimensions);
        basicFunctions[BasicFunction.WEIERSTRASS.ordinal()] = new Weierstrass();    // TODO: zakaj ne podam število dimenzij v ta problem?
        basicFunctions[BasicFunction.GRIEWANK.ordinal()] = new Griewank(numberOfDimensions);
        basicFunctions[BasicFunction.ACKLEY.ordinal()] = new Ackley1(numberOfDimensions);
    }

    private void setStretchSeverity() {
        /*for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            stretchSeverity[i] = convergeSeverity[i] * (upperLimit - lowerLimit) /
                    (basicFunctions[componentFunction[i].ordinal()][0].upper - comBoundary[(int) componentFunction[i].ordinal()][0].lower);
        }*/
    }

    @Override
    public void increaseDimension(int newDimension) {
        int oldDimension = numberOfDimensions;
        numberOfDimensions = newDimension;

        Double lower = lowerLimit.get(oldDimension);
        Double upper = upperLimit.get(oldDimension);

        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            position[i][oldDimension] = lower + (upper - lower) * new Random().nextGaussian();    // TODO: use appropriate random
            initialPosition[i][oldDimension] = position[i][oldDimension];
        }

        if (changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY) {
            for (int i = 0; i < periodicity; i++) {
                if (changeTypeCounter.getNumberOfOccurrences(changeType) <= i) {
                    break;
                }
                for (int j = 0; j < numberOfPeaksOrFunctions; j++) {
                    rotationPlanes[i][j][oldDimension] = oldDimension;
                }
            }
        }
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
