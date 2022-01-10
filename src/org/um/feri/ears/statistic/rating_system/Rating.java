package org.um.feri.ears.statistic.rating_system;

import static java.lang.Math.pow;

/**
 * Container for a players Rating
 */
public class Rating {
    protected double rating;
    protected double ratingDeviation;

    public Rating(double mean, double standardDeviation) {

        this.rating = mean;
        this.ratingDeviation = standardDeviation;
    }

    public Rating(Rating rating) {
        this.rating = rating.rating;
        this.ratingDeviation = rating.ratingDeviation;
    }

    /**
     * The Rating value in Glicko2 or the statistical mean value of the rating in TrueSkill (also known as μ).
     **/
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * The Rating deviation in Glicko2 or the standard deviation (the spread) of the rating in TrueSkill (also known as σ).
     **/
    public double getRatingDeviation() {
        return ratingDeviation;
    }

    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    /**
     * The variance of the rating (standard deviation squared)
     */
    public double getVariance() {
        return pow(getRatingDeviation(), 2);
    }
}
