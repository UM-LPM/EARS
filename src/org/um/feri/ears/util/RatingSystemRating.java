package org.um.feri.ears.util;

import java.io.Serializable;

public class RatingSystemRating implements Serializable {
    public double Rating;
    public double StandardDeviation;

    public RatingSystemRating()
    {
        this(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public RatingSystemRating(double rating, double standardDeviation){
        this.Rating = rating;
        this.StandardDeviation = standardDeviation;
    }
}
