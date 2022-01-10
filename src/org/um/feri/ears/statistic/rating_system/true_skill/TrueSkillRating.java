package org.um.feri.ears.statistic.rating_system.true_skill;

import org.um.feri.ears.statistic.rating_system.Rating;
import org.um.feri.ears.util.Util;

public class TrueSkillRating extends Rating {

    private static final int defaultConservativeStandardDeviationMultiplier = 3;
    private double conservativeStandardDeviationMultiplier; //The number of standardDeviation to subtract from the mean to achieve a conservative rating.
    private double conservativeRating; // A conservative estimate of skill based on the mean and standard deviation.

    /**
     * Constructs a TrueSKill rating.
     *
     * @param mean              the statistical mean value of the rating (also known as μ).
     * @param standardDeviation the number of standardDeviation to subtract from the mean to achieve a conservative rating.
     */
    public TrueSkillRating(double mean, double standardDeviation) {
        super(mean, standardDeviation);
        this.conservativeStandardDeviationMultiplier = defaultConservativeStandardDeviationMultiplier;
        this.conservativeRating = mean - conservativeStandardDeviationMultiplier * standardDeviation;
    }

    public TrueSkillRating(TrueSkillRating rating) {
        super(rating);
        this.conservativeStandardDeviationMultiplier = rating.conservativeStandardDeviationMultiplier;
        this.conservativeRating = rating.conservativeRating;
    }

    /**
     *  The standardDeviation to subtract from the mean to achieve a conservative rating
     * @return standardDeviation to subtract from the mean to achieve a conservative rating
     */
    public double getConservativeStandardDeviationMultiplier() {
        return conservativeStandardDeviationMultiplier;
    }

    /**
     * A conservative estimate of skill based on the mean and standard deviation
     * @return conservative estimate of skill based on the mean and standard deviation
     */
    public double getConservativeRating() {
        return this.conservativeRating;
    }

    public String toString() {
        return String.format("Mean(μ)=%s, Std-Dev(σ)=%s", Util.df.format(rating), Util.df.format(ratingDeviation));
    }
}
