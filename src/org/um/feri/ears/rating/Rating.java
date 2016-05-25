/**
 * Basic rating data!
 * <p>
 * 
 * @author Matej Crepinsek
 * @version 1
 * 
 *          <h3>License</h3>
 * 
 *          Copyright (c) 2011 by Matej Crepinsek. <br>
 *          All rights reserved. <br>
 * 
 *          <p>
 *          Redistribution and use in source and binary forms, with or without
 *          modification, are permitted provided that the following conditions
 *          are met:
 *          <ul>
 *          <li>Redistributions of source code must retain the above copyright
 *          notice, this list of conditions and the following disclaimer.
 *          <li>Redistributions in binary form must reproduce the above
 *          copyright notice, this list of conditions and the following
 *          disclaimer in the documentation and/or other materials provided with
 *          the distribution.
 *          <li>Neither the name of the copyright owners, their employers, nor
 *          the names of its contributors may be used to endorse or promote
 *          products derived from this software without specific prior written
 *          permission.
 *          </ul>
 *          <p>
 *          THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *          "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *          LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *          FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *          COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *          INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *          BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *          LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *          CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *          LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *          ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *          POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package org.um.feri.ears.rating;

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
        return Util.df1.format(rating)+" SD:"+Util.df1.format(RD)+" ro:"+Util.df3.format(this.ratingVolatility);
    }

}
