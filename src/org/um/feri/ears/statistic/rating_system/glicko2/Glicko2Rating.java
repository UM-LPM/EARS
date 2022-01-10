package org.um.feri.ears.statistic.rating_system.glicko2;

import org.um.feri.ears.statistic.rating_system.Rating;
import org.um.feri.ears.util.Util;

public class Glicko2Rating extends Rating {

    private double ratingVolatility; // the expected fluctuation of a player's rating (also known as ro)
    public static final double GLICKO2_CONSTANT = 173.7178;

    /**
     * Constructs a Glicko2 rating.
     *
     * @param rating           the rating of the player, which indicates the player's absolute power
     * @param ratingDeviation  indicates how reliable the player’s rating
     * @param ratingVolatility volatility measure which indicates the degree of expected fluctuation in a player’s rating
     */
    public Glicko2Rating(double rating, double ratingDeviation, double ratingVolatility) {
        super(rating, ratingDeviation);
        this.ratingVolatility = ratingVolatility;
    }

    public Glicko2Rating(Glicko2Rating rating) {
        super(rating);
        this.ratingVolatility = rating.ratingVolatility;
    }

    /**
     * The expected fluctuation of a player's rating (also known as ro)
     * @return expected fluctuation of a player's rating (also known as ro)
     */
    public double getRatingVolatility() {
        return ratingVolatility;
    }

    public void setRatingVolatility(double ratingVolatility) {
        this.ratingVolatility = ratingVolatility;
    }

    public double getGlicko2Rating() {
        return (rating - 1500) / GLICKO2_CONSTANT;
    }

    public double getGlicko2RatingDeviation() {
        return ratingDeviation / GLICKO2_CONSTANT;
    }

    public static double getGlicko2Rating(double glicko2Rating) {
        return (GLICKO2_CONSTANT * glicko2Rating) + 1500;
    }

    public static double getGlicko2RatingDeviation(double glicko2RatingDeviation) {
        return GLICKO2_CONSTANT * glicko2RatingDeviation;
    }

    public String toString() {
        return String.format("Rating=%s RD=%s ro=%s", Util.df1.format(rating), Util.df1.format(ratingDeviation), Util.df3.format(this.ratingVolatility));
    }
}
