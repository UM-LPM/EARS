package org.um.feri.ears.problems.dynamic.cec2009;

import org.um.feri.ears.problems.DoubleProblem;

import java.util.Random;

public abstract class DynamicProblem extends DoubleProblem {

    // Iz Global v Problem: dimension, numPeakOrFun (število globalnih optimumov, preveri v problemu), hnS, boundary,
    // minHeight, maxHeight, chaoticConstant, minWidth, maxWidth, change, periodicity, minDimension, maxDimension,
    // changeFrequency, alpha, maxAlpha; GeneralDbg: recurrentNoisySeverity, flagDimensionChange, dirDimensionChange;
    // RealDbg: genes so x-i (Solution) -> pri evaluate parameter Solution (iz nadrazreda), position=decisionSpaceOptima,
    // initialPosition?, height (če je fitness, že obstaja in je objectiveSpaceOptima), heightSeverity, fit?, weight?,
    // rotationMatrix?, rotationPlanes?, globalOptimaPosition?

    // metode: change abstraktna

    protected final ChangeTypeCounter changeTypeCounter;
    protected int numberOfPeaksOrFunctions; // TODO: preveri ali je to število globalnih optimumov (v problemu numberOfGlobalOptimums?)
    private Double minHeight, maxHeight;    // minimum/maximum height of all peaks (local optima) in RotationDBG (CompositionDBG)
    private Double chaoticConstant;
    private Double[] peakHeight;    // peak height in RotationDBG, height of global optima in CompositionDBG   // TODO: preveri, če to predstavlja fitness, potem že obstaja in je to spremeljivka objectiveSpaceOptima
    protected ChangeType changeType;
    protected int periodicity;    // definite period for values repeating
    private boolean isDimensionChanged;  // true if the number of dimensions has changed, false otherwise
    private int minDimension, maxDimension;
    private boolean isDimensionIncreasing;  // true if the direction should be changed, false otherwise
    protected int changeCounter;  // counter for the number of changes
    private int changeFrequency;    // number of evaluations between two successive changes
    private int heightSeverity;
    protected Double[][] position;  // positions of local or global optima (local optima in RotationDBG, global optima of basic function in CompositionDBG) // TODO: preveri ali je positon=decisionSpaceOptima
    protected Double[][] initialPosition;   // save the initial positions   // TODO: preveri kaj predstavlja ta matrika
    protected int[][][] rotationPlanes;    // save the planes rotated during one periodicity
    protected Double[] fit;   // objective value of each basic function in CompositionDBG, peak height in RotationDBG
    protected Double[] weight;   // weight value of each basic function in CompositionDBG, peak width in RotationDBG
    protected float recurrentNoisySeverity; // deviation severity from the trajectory of recurrent change
    protected final Double gLowerLimit, gUpperLimit;  // solution space

    public DynamicProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints,
                          int numberOfPeaksOrFunctions, Double minHeight, Double maxHeight, Double chaoticConstant, ChangeType changeType,
                          int periodicity, boolean isDimensionChanged, int minDimension, int maxDimension, int changeFrequency,
                          int heightSeverity, Double gLowerLimit, Double gUpperLimit) {

        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints);

        changeTypeCounter = new ChangeTypeCounter();
        this.numberOfPeaksOrFunctions = numberOfPeaksOrFunctions;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.chaoticConstant = chaoticConstant;
        peakHeight = new Double[numberOfPeaksOrFunctions];
        this.changeType = changeType;
        this.periodicity = periodicity;
        this.isDimensionChanged = isDimensionChanged;
        this.minDimension = minDimension;
        this.maxDimension = maxDimension;
        changeCounter = 0;
        this.changeFrequency = changeFrequency;
        this.heightSeverity = heightSeverity;
        fit = new Double[numberOfPeaksOrFunctions];
        weight = new Double[numberOfPeaksOrFunctions];
        this.gLowerLimit = gLowerLimit;
        this.gUpperLimit = gUpperLimit;

        if (changeType == ChangeType.RECURRENT_NOISY) {
            recurrentNoisySeverity = 0.8f;
        }
    }

    // return a value calculated by the logistics function
    protected Double getChaoticValue(final Double x, final Double min, final Double max) {
        if (min > max) {
            return -1.0;
        }
        double chaoticValue;
        chaoticValue = (x - min) / (max - min);
        chaoticValue = chaoticConstant * chaoticValue * (1 - chaoticValue);
        return min + chaoticValue * (max - min);
    }

    // return a value in recurrent with noisy dynamism environment
    protected Double sinValueNoisy(final int x, final Double min, final Double max, final Double amplitude, final double angle, final double noisySeverity) {
        double y = min + amplitude * (Math.sin(2 * Math.PI * (x + angle) / periodicity) + 1) / 2.;
        double noisy = noisySeverity * new Random().nextGaussian();    // TODO: use appropriate random
        double t = y + noisy;
        return (t > min && t < max) ? t : (t - noisy);
    }

    // dimension changes (linear increase or decrease).
    public void changeDimension() {
        if (!isDimensionChanged) {
            return;
        }

        int newDimension = numberOfDimensions;
        if (numberOfDimensions == minDimension) {
            isDimensionIncreasing = true;
        }
        if (numberOfDimensions == maxDimension) {
            isDimensionIncreasing = false;
        }

        if (isDimensionIncreasing) {
            newDimension++;
        } else {
            newDimension--;
        }

        if (newDimension < numberOfDimensions) {
            decreaseDimension(newDimension);
        } else {
            increaseDimension(newDimension);
        }
    }

    protected abstract void increaseDimension(int newDimension);

    protected abstract void decreaseDimension(int newDimension);

    public abstract void makeChange();

    // TODO: premisli, če je to pravo mesto za to metodo, ker potem razred DynamicProblem več ni tako "splošen". Ideja: podrazred GeneralizedDynamicBenchmark
    public Double standardChange(final Double min, final Double max) {
        double step = 0.0;
        int sign;

        final double ALPHA = 0.04;    // TODO: where to put this variable?
        final double MAX_ALPHA = 0.1;   // TODO: where to put this variable?

        switch (changeType) {
            case SMALL_STEP:
                step = -1 + 2 * new Random().nextGaussian();    // TODO: use appropriate random
                step = ALPHA * step * (max - min);
                break;
            case U_RANDOM:
                step = new Random().nextGaussian();    // TODO: use appropriate random
                break;
            case LARGE_STEP:
                step = -1 + 2 * new Random().nextGaussian();    // TODO: use appropriate random
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

    // TODO: premisli, če je to pravo mesto za to metodo, ker potem razred DynamicProblem več ni tako "splošen"
    // TODO: premisli glede imena metode
    public void heightStandardChange() {
        double step;
        for (int i = 0; i < numberOfPeaksOrFunctions; i++) {
            step = heightSeverity * standardChange(minHeight, maxHeight);
            peakHeight[i] = peakHeight[i] + step;
            if (peakHeight[i] > maxHeight || peakHeight[i] < minHeight) {
                peakHeight[i] = peakHeight[i] - step;
            }
        }
    }
}
