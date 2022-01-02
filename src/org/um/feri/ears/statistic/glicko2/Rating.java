package org.um.feri.ears.statistic.glicko2;

import org.um.feri.ears.util.Util;

public class Rating {
    private double rating; //rating r
    private double RD; // rating deviation RD,
    private double ratingVolatility; //ro f expected fluctuation in a player's rating
    private static final double GLICKO2_CONSTANT = 173.7178;
    
    public Rating(double rating, double ratingDeviation, double ratingVolatility) {
        super();
        this.rating = rating;
        this.RD = ratingDeviation;
        this.ratingVolatility = ratingVolatility;
    }
    
    public Rating(Rating ra) {
        this.rating = ra.rating;
        this.RD = ra.RD;
        this.ratingVolatility = ra.ratingVolatility;    
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public double getRD() {
        return RD;
    }
    
    public void setRD(double ratingDeviation) {
        this.RD = ratingDeviation;
    }
    
    public double getRatingVolatility() {
        return ratingVolatility;
    }
    
    public void setRatingVolatility(double ratingVolatility) {
        this.ratingVolatility = ratingVolatility;
    }
    double getGlicko2Rating() {
        return (rating - 1500) / GLICKO2_CONSTANT;
    }
    double getGlicko2RatingDeviation() {
        return RD / GLICKO2_CONSTANT;
    }
    public static double setGlicko2Rating(double glicko2Rating) {
        return (GLICKO2_CONSTANT * glicko2Rating) + 1500;
    }
    public static double setGlicko2RatingDeviation(double glicko2RatingDeviation) {
        return GLICKO2_CONSTANT * glicko2RatingDeviation;
    }
    public String toString() {
        return Util.df1.format(rating)+" RD:"+Util.df1.format(RD)+" ro:"+Util.df3.format(this.ratingVolatility);
    }

}
