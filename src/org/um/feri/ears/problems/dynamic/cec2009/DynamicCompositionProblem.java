package org.um.feri.ears.problems.dynamic.cec2009;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.unconstrained.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;

public class DynamicCompositionProblem extends DynamicProblem {

    private DoubleProblem[] basicFunctions;   // which basic function is used to compose the composition function TODO: je DoubleProblem ustrezen tip?
    private Double[] convergeSeverity;  // severity of converge range for each function
    private Double[] stretchSeverity;   // severity of stretching original function, greater than 1 for stretch, less than 1 for compress the original function
    private final Double heightNormalizeSeverity;   // the constant number for normalizing all basic functions with similar height

    public DynamicCompositionProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives,
                                     int numberOfConstraints, int numberOfPeaksOrFunctions, Double minHeight, Double maxHeight,
                                     Double chaoticConstant, ChangeType changeType, int periodicity, boolean isDimensionChanged,
                                     int minDimension, int maxDimension, int changeFrequency, int heightSeverity,
                                     Double heightNormalizeSeverity, Double gLowerLimit, Double gUpperLimit, BasicFunction basicFunction) {

        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints,
                numberOfPeaksOrFunctions, minHeight, maxHeight, chaoticConstant, changeType, periodicity,
                isDimensionChanged, minDimension, maxDimension, changeFrequency, heightSeverity, gLowerLimit, gUpperLimit);

        this.heightNormalizeSeverity = heightNormalizeSeverity;
        convergeSeverity = new Double[numberOfPeaksOrFunctions];
        stretchSeverity = new Double[numberOfPeaksOrFunctions];
        initStretchSeverity();

        basicFunctions = new DoubleProblem[numberOfPeaksOrFunctions];
        initBasicFunctions(basicFunction);
    }

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
                // TODO: benchmark CEC 2009 ima vedno 10 dimenzij, zato bo OK. smiselno bi bilo dodati preverjanje dolžine polja
                // TODO: ali pa da je BasicFunctions že inicializiran array, ki se pošlje v konstruktor
                // if (basicFunctions.length != 10) throw new IllegalStateException("The number of dimension should be 10 for CEC2009DOBenchmark.");
                basicFunctions[0] = basicFunctions[1] = new Sphere(numberOfDimensions);
                basicFunctions[2] = basicFunctions[3] = new Rastrigin(numberOfDimensions);
                basicFunctions[4] = basicFunctions[5] = new Weierstrass();
                basicFunctions[6] = basicFunctions[7] = new Griewank(numberOfDimensions);
                basicFunctions[8] = basicFunctions[9] = new Ackley1(numberOfDimensions);
                break;
            default:
                throw new IllegalArgumentException("Unknown BasicFunction value passed in constructor.");
        }

    }

    private void initStretchSeverity() {
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            stretchSeverity[i] = convergeSeverity[i] * (gUpperLimit - gLowerLimit) / (basicFunctions[i].getLowerLimit(0) - basicFunctions[i].getLowerLimit(0));
        }
    }

    @Override
    public void increaseDimension(int newDimension) {
        int oldDimension = numberOfDimensions;
        numberOfDimensions = newDimension;

        // TODO: parameterSetting(this);

        Double lower = gLowerLimit; // lowerLimit.get(oldDimension);
        Double upper = gUpperLimit; // upperLimit.get(oldDimension);

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

        calculateGlobalOptima();
    }

    @Override
    protected void calculateGlobalOptima() {
        globalOptima = Arrays.stream(peakHeight).min(Comparator.comparing(Double::doubleValue)).orElseThrow(NoSuchElementException::new);   // Global.extremum(height, numPeakOrFun, Compare.MIN);
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            if (peakHeight[i] == globalOptima) {
                System.arraycopy(position[i], 0, globalOptimaPosition, 0, numberOfDimensions);
            }
        }
    }

    @Override
    public void decreaseDimension(int newDimension) {
        numberOfDimensions = newDimension;
        // TODO: ta pogoj vključno z operacijo mislim, da je odveč. to v original kodi potreboval, ker je kopiral objekt...
        if (changeType == ChangeType.RECURRENT || changeType == ChangeType.RECURRENT_NOISY) {
            setPeriodicity(periodicity);
        }
        // TODO: naredi to, kar se zgodi v parameterSetting metodi (mislim, da popravi polja glede na novo zmanjšanja dimenzijo)
        calculateGlobalOptima();
    }

    @Override
    public void makeChange() {
        changeCounter++;
        switch (changeType) {
            case SMALL_STEP:
                heightStandardChange();
                positionStandardChange(0);
                calculateGlobalOptima();
                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.SMALL_STEP);
                break;
            case LARGE_STEP:
                heightStandardChange();
                positionStandardChange(0);
                calculateGlobalOptima();
                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.LARGE_STEP);
                break;
            case U_RANDOM:
                // change the global minimum value of each function
                heightStandardChange();
                // change the position of global optimum of each function randomly
                positionStandardChange(0);
                calculateGlobalOptima();
                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.U_RANDOM);
                break;
            case RECURRENT:
                double initialAngle;
                double heightRange = maxHeight - minHeight;

                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    initialAngle = (double) periodicity * i / numberOfPeaksOrFunctions;
                    peakHeight[i] = minHeight + heightRange * (Math.sin(2 * Math.PI *
                            (changeTypeCounter.getNumberOfOccurrences(ChangeType.RECURRENT) + initialAngle) / periodicity) + 1) / 2.;
                }
                initialAngle = Math.PI * (Math.sin(2 * Math.PI * changeTypeCounter.getNumberOfOccurrences(ChangeType.RECURRENT) / periodicity) + 1) / 12.;
                positionStandardChange(initialAngle);

                calculateGlobalOptima();
                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.RECURRENT);
                break;
            case CHAOTIC:
                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    peakHeight[i] = getChaoticValue(peakHeight[i], minHeight, maxHeight);
                }

                positionStandardChange(0);

                calculateGlobalOptima();
                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.CHAOTIC);
                break;
            case RECURRENT_NOISY:
                double initialAngle2;   // TODO: poimenovanje...
                double heightRange2 = maxHeight - minHeight; // TODO: poimenovanje

                double noisy;
                for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
                    initialAngle2 = (double) periodicity * i / numberOfPeaksOrFunctions;
                    peakHeight[i] = sinValueNoisy(changeTypeCounter.getNumberOfOccurrences(ChangeType.RECURRENT_NOISY), minHeight, maxHeight, heightRange2, initialAngle2, recurrentNoisySeverity);
                }
                initialAngle2 = Math.PI * (Math.sin(2 * Math.PI * (changeTypeCounter.getNumberOfOccurrences(ChangeType.RECURRENT_NOISY)) / periodicity) + 1) / 12.;
                noisy = recurrentNoisySeverity * new Random().nextDouble();   // TODO: use appropriate random
                positionStandardChange(initialAngle2 + noisy);

                calculateGlobalOptima();
                changeTypeCounter.increaseNumberOfOccurrences(ChangeType.RECURRENT_NOISY);
                break;
        }
    }

    @Override
    public double eval(double[] x) {
        // System.arraycopy(x, 0, genes, 0, dimension); // TODO: ne veš še kaj so genes, z Mihom sva se pogovarjala, da prejme eval x-e v metodo in sma predvidevala, da je x enako genes

        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {    // calculate weight for each function
            weight[i] = 0.0;
            for (int j = 0; j < numberOfDimensions; j++) {
                // weight[i] += (genes[j] - position[i][j]) * (genes[j] - position[i][j]);  // TODO: genes
            }
            weight[i] = Math.exp(-Math.sqrt(weight[i] / (2 * numberOfDimensions * convergeSeverity[i] * convergeSeverity[i])));
        }

        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {   // calculate objective value for each function
            for (int j = 0; j < numberOfDimensions; j++) {  // calculate the objective value of tranformation function i
                // genes[j] = (genes[j] - position[i][j]) / stretchSeverity[i]; // ((1+fabs(position[i][j]/boundary[j].upper))* // TODO: genes
            }
            Matrix m = new Matrix(numberOfDimensions, 1);
            // m.setData(genes, dimension); // TODO: genes

            m = m.multiply(rotationMatrix[i]);

            // System.arraycopy(m.getData()[0], 0, genes, 0, dimension);    // TODO: genes
            // correction(componentFunction[i]);    // TODO: makeFeasible?
            // fit[i] = basicFunctions[i].eval(x);  // TODO: je 'x' pravi argument?

            for (int j = 0; j < numberOfDimensions; j++) {   // calculate the estimate max value of funciton i
                // genes[j] = boundary[j].upper;    // TODO: genes
                // genes[j] /= stretchSeverity[i];  // TODO: genes
            }
            // m.setData(genes, dimension); // TODO: genes
            m = m.multiply(rotationMatrix[i]);
            // System.arraycopy(m.getData()[0], 0, genes, 0, dimension);    // TODO: genes
            // correction(componentFunction[i]);    // TODO: makeFeasible?
            // double fMax = basicFunctions[i].eval(x); // double fMax = selectFun(componentFunction[i]);   // TODO: je 'x' pravi argument?
            // if (fMax != 0) {
            //     fit[i] = heightNormalizeSeverity * fit[i] / Math.abs(fMax);
            // }

            // System.arraycopy(x, 0, genes, 0, dimension); // TODO: genes
        }

        globalOptima = Arrays.stream(peakHeight).min(Comparator.comparing(Double::doubleValue)).orElseThrow(NoSuchElementException::new);   // Global.extremum(height, numPeakOrFun, Compare.MIN);

        double sumWeight = 0;
        double maxWeight = Arrays.stream(weight).max(Comparator.comparing(Double::doubleValue)).orElseThrow(NoSuchElementException::new);
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
            obj += weight[i] * (fit[i] + peakHeight[i]);
        }

        return obj;
    }
}
