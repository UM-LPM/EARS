/**
 * Rating system is similar what chess players use. <p>
 * Currently one of the best rating models is described by Mark Glickman (Glicko-2).
 * Source page: http://www.glicko.net/glicko.html <p>
 * RatingCalculations is glicko 2 besed implementation.
 * 
 * Implementation is based on Derek Hilder implementation of jre service.
 * From http://java.net/projects/jrs/ project.
 * 
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RatingCalculations {
    private static double T = 0.5; // Constant that constrains the change in
                                   // volatility (0.3 to 1.2) (Glicko2)

    public static void computePlayerRatings(HashMap<String, Player> prePeriodRatings) {
        HashMap<String, Rating> newPlayerRatings = new HashMap<String, Rating>();
        Iterator<String> playerIds = prePeriodRatings.keySet().iterator();
        while (playerIds.hasNext()) {

            String id = playerIds.next();
            Player prePlayerInfo = prePeriodRatings.get(id);

            double rating = prePlayerInfo.getRatingData().getGlicko2Rating();
            double ratingDeviation = prePlayerInfo.getRatingData().getGlicko2RatingDeviation();
            double ratingVolatility = prePlayerInfo.getRatingData().getRatingVolatility();

            double postRating = rating;
            double postRD = ratingDeviation;
            double postRatingVolatility = ratingVolatility;

            ArrayList<Game> gameResultsList = prePlayerInfo.getUnEvaluatedGames();
            if (gameResultsList == null || gameResultsList.isEmpty()) {
                // Did not play - player's rating volatility remains the same,
                // but the RD increases.
                postRD = Math.sqrt(ratingDeviation * ratingDeviation + ratingVolatility * ratingVolatility);
            } else {
                // Compute the estimated variance of the player's rating based
                // only
                // on game outcomes.
                double variance = 0;
                double performanceRatingFromGameOutcomes = 0;
                Iterator<Game> gameResults = gameResultsList.iterator();
                while (gameResults.hasNext()) {
                    Game gameResult = gameResults.next();
                    Player opponent = prePeriodRatings.get(gameResult.getB().getPlayerId());
                    double opponentRating = opponent.getRatingData().getGlicko2Rating();
                    double opponentRatingDeviation = opponent.getRatingData().getGlicko2RatingDeviation();

                    double g = g(opponentRatingDeviation);
                    double E = E(rating, opponentRating, opponentRatingDeviation);
                    performanceRatingFromGameOutcomes += g * (gameResult.getGameResult(id) - E);
                    variance += (g * g) * E * (1 - E);
                }
                variance = 1. / variance;
                double improvement = variance * performanceRatingFromGameOutcomes;
                // Determine the new value of the volatility using iteration.
                double t = ratingDeviation;
                double s = ratingVolatility;
                double D = improvement;
                double prevX = 0;
                double a = Math.log(s * s);
                double x = a;
                double ex = Math.exp(x);
                double v = variance;
                double d, h1, h2, xabs;
                do {
                    d = (t * t) + v + ex;
                    h1 = -(x - a) / (T * T) - 0.5 * ex / d + 0.5 * ex * ((D / d) * (D / d));
                    h2 = -1 / (T * T) - 0.5 * ex * ((t * t) + v) / (d * d) + 0.5 * (D * D) * ex * ((t * t) + v - ex) / (d * d * d);
                    prevX = x;
                    x = x - h1 / h2;
                    xabs = Math.abs(x - prevX);
                } while (xabs > .0000001);
                postRatingVolatility = Math.exp(x / 2);
                double updatedRD = Math.sqrt(Math.pow(ratingDeviation, 2) + Math.pow(postRatingVolatility, 2));
                postRD = 1 / Math.sqrt((1 / (updatedRD * updatedRD)) + (1 / variance));

                postRating = rating + (postRD * postRD) * performanceRatingFromGameOutcomes;
            }
            if (postRD<50/173.7178) postRD = 50/173.7178; //set min RD
            if (postRD>350/173.7178) postRD = 350/173.7178; //set max RD
            Rating tmp = new Rating(Rating.setGlicko2Rating(postRating), Rating.setGlicko2RatingDeviation(postRD), postRatingVolatility);
            newPlayerRatings.put(id, tmp);
        }
        // update Ranks
        playerIds = newPlayerRatings.keySet().iterator();
        while (playerIds.hasNext()) {
            String id = playerIds.next();
            prePeriodRatings.get(id).setRatingData(newPlayerRatings.get(id));
            
            //set games as evaluated
            ArrayList<Game> gameResultsList =  prePeriodRatings.get(id).getUnEvaluatedGames();
            
            for(Game g : gameResultsList)
            {
            	g.setEvaluated(true);
            }
        }

    }

    /**
     * Function used internally by the Glicko-2 algorithm.
     * 
     * @param ratingDeviation
     * @return
     */
    private static double g(double ratingDeviation) {
        return 1 / Math.sqrt(1 + ((3 * ratingDeviation * ratingDeviation) / (Math.PI * Math.PI)));
    }

    /**
     * Function used internally by the Glicko-2 algorithm.
     * 
     * @param playerRating
     * @param opponentRating
     * @param opponentRatingDeviation
     * @return
     */
    private static double E(double playerRating, double opponentRating, double opponentRatingDeviation) {
        return 1 / (1 + Math.exp((g(opponentRatingDeviation) * -1) * (playerRating - opponentRating)));
    }

	public static void computePlayerRatings(HashMap<String, Player> players, List<Game> unevaluatedGames) {

		Player one, two;
		for (Game game : unevaluatedGames) {

			one = game.getA();
			two = game.getB();
			Rating p1 = calculateRatingForSingleGame(one, game);
			Rating p2 = calculateRatingForSingleGame(two, game);

			one.setRatingData(p1);
			two.setRatingData(p2);
			game.setEvaluated(true);
		}
		
		//check for players who didn't participate in current rating period
        Iterator<String> playerIds = players.keySet().iterator();
        while (playerIds.hasNext()) 
        {
        	String id = playerIds.next();
        	boolean hasParticapated = false;
        	for (Game game : unevaluatedGames) {
        		if(id.equals(game.getA().getPlayerId()) || id.equals(game.getB().getPlayerId()))
        		{
        			hasParticapated = true;
        			break;
        		}
        	}
        	
        	// player didn't participate
        	if(!hasParticapated)
        	{
        		Player player = players.get(id);
                double rating = player.getRatingData().getGlicko2Rating();
                double ratingDeviation = player.getRatingData().getGlicko2RatingDeviation();
                double ratingVolatility = player.getRatingData().getRatingVolatility();

                double postRD;

                // Did not play - player's rating volatility remains the same, but the RD increases.
                postRD = Math.sqrt(ratingDeviation * ratingDeviation + ratingVolatility * ratingVolatility);
                
                if (postRD<50/173.7178) postRD = 50/173.7178; //set min RD
                if (postRD>350/173.7178) postRD = 350/173.7178; //set max RD
                Rating tmp = new Rating(Rating.setGlicko2Rating(rating), Rating.setGlicko2RatingDeviation(postRD), ratingVolatility);
                player.setRatingData(tmp);
        	}
        }
	}

	private static Rating calculateRatingForSingleGame(Player player, Game game) {


		double rating = player.getRatingData().getGlicko2Rating();
		double ratingDeviation = player.getRatingData().getGlicko2RatingDeviation();
		double ratingVolatility = player.getRatingData().getRatingVolatility();

		double postRating = rating;
		double postRD = ratingDeviation;
		double postRatingVolatility = ratingVolatility;


		double variance = 0;
		double performanceRatingFromGameOutcomes = 0;

		Player opponent = game.getOpponent(player);
		double opponentRating = opponent.getRatingData().getGlicko2Rating();
		double opponentRatingDeviation = opponent.getRatingData().getGlicko2RatingDeviation();

		double g = g(opponentRatingDeviation);
		double E = E(rating, opponentRating, opponentRatingDeviation);
		performanceRatingFromGameOutcomes += g * (game.getGameResult(player.getPlayerId()) - E);
		variance += (g * g) * E * (1 - E);

		variance = 1. / variance;
		double improvement = variance * performanceRatingFromGameOutcomes;
		// Determine the new value of the volatility using iteration.
		double t = ratingDeviation;
		double s = ratingVolatility;
		double D = improvement;
		double prevX = 0;
		double a = Math.log(s * s);
		double x = a;
		double ex = Math.exp(x);
		double v = variance;
		double d, h1, h2, xabs;
		do {
			d = (t * t) + v + ex;
			h1 = -(x - a) / (T * T) - 0.5 * ex / d + 0.5 * ex * ((D / d) * (D / d));
			h2 = -1 / (T * T) - 0.5 * ex * ((t * t) + v) / (d * d) + 0.5 * (D * D) * ex * ((t * t) + v - ex) / (d * d * d);
			prevX = x;
			x = x - h1 / h2;
			xabs = Math.abs(x - prevX);
		} while (xabs > .0000001);
		postRatingVolatility = Math.exp(x / 2);
		double updatedRD = Math.sqrt(Math.pow(ratingDeviation, 2) + Math.pow(postRatingVolatility, 2));
		postRD = 1 / Math.sqrt((1 / (updatedRD * updatedRD)) + (1 / variance));

		postRating = rating + (postRD * postRD) * performanceRatingFromGameOutcomes;

		if (postRD<50/173.7178) postRD = 50/173.7178; //set min RD
		if (postRD>350/173.7178) postRD = 350/173.7178; //set max RD
		Rating tmp = new Rating(Rating.setGlicko2Rating(postRating), Rating.setGlicko2RatingDeviation(postRD), postRatingVolatility);

		return tmp;
	}

}
