package org.um.feri.ears.statistic.rating_system.glicko2;

import org.um.feri.ears.statistic.rating_system.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Glicko2RatingCalculator {
    private static final double T = 0.5; // Constant that constrains the change in volatility (0.3 to 1.2) (Glicko2)

    public static void calculateNewRatings(HashMap<String, Player> prePeriodRatings, boolean allGames) {
        HashMap<String, Glicko2Rating> newPlayerRatings = new HashMap<>();
        Iterator<String> playerIds = prePeriodRatings.keySet().iterator();

        if (allGames) { //reset ratings if recalculating all games
            while (playerIds.hasNext()) {
                String id = playerIds.next();
                Player player = prePeriodRatings.get(id);

                Glicko2Rating initialSettings = player.getRatingHistory().get(0);
                if (initialSettings != null) {
                    player.getRatingHistory().clear();
                    player.setGlicko2Rating(initialSettings);
                }
            }
            playerIds = prePeriodRatings.keySet().iterator();
        }

        while (playerIds.hasNext()) {

            String id = playerIds.next();
            Player prePlayerInfo = prePeriodRatings.get(id);

            double rating = prePlayerInfo.getGlicko2Rating().getGlicko2Rating();
            double ratingDeviation = prePlayerInfo.getGlicko2Rating().getGlicko2RatingDeviation();
            double ratingVolatility = prePlayerInfo.getGlicko2Rating().getRatingVolatility();

            double postRating = rating;
            double postRD = ratingDeviation;
            double postRatingVolatility = ratingVolatility;

            ArrayList<Game> gameResultsList;

            if (allGames) {
                gameResultsList = prePlayerInfo.getAllGames();
            } else {
                gameResultsList = prePlayerInfo.getUnevaluatedGames();
            }

            if (gameResultsList == null || gameResultsList.isEmpty()) {
                // Did not play - player's rating volatility remains the same,
                // but the RD increases.
                postRD = Math.sqrt(ratingDeviation * ratingDeviation + ratingVolatility * ratingVolatility);
            } else {
                // Compute the estimated variance of the player's rating based only on game outcomes.
                double variance = 0;
                double performanceRatingFromGameOutcomes = 0;
                Iterator<Game> gameResults = gameResultsList.iterator();
                while (gameResults.hasNext()) {
                    Game gameResult = gameResults.next();
                    Player opponent = prePeriodRatings.get(gameResult.getPlayerTwo().getId());
                    double opponentRating = opponent.getGlicko2Rating().getGlicko2Rating();
                    double opponentRatingDeviation = opponent.getGlicko2Rating().getGlicko2RatingDeviation();

                    double g = g(opponentRatingDeviation);
                    double E = E(rating, opponentRating, opponentRatingDeviation);
                    performanceRatingFromGameOutcomes += g * (gameResult.getGameResultScore(id) - E);
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
            if (postRD < 50 / Glicko2Rating.GLICKO2_CONSTANT) postRD = 50 / Glicko2Rating.GLICKO2_CONSTANT; //set min RD
            if (postRD > 350 / Glicko2Rating.GLICKO2_CONSTANT)
                postRD = 350 / Glicko2Rating.GLICKO2_CONSTANT; //set max RD
            Glicko2Rating tmp = new Glicko2Rating(Glicko2Rating.getGlicko2Rating(postRating), Glicko2Rating.getGlicko2RatingDeviation(postRD), postRatingVolatility);
            newPlayerRatings.put(id, tmp);
        }
        // update ratings
        playerIds = newPlayerRatings.keySet().iterator();
        while (playerIds.hasNext()) {
            String id = playerIds.next();
            prePeriodRatings.get(id).setGlicko2Rating(newPlayerRatings.get(id));

            //set games as evaluated
            ArrayList<Game> gameResultsList = prePeriodRatings.get(id).getUnevaluatedGames();

            for (Game g : gameResultsList) {
                g.setEvaluated(true);
            }
        }
    }

    private static double g(double ratingDeviation) {
        return 1 / Math.sqrt(1 + ((3 * ratingDeviation * ratingDeviation) / (Math.PI * Math.PI)));
    }

    private static double E(double playerRating, double opponentRating, double opponentRatingDeviation) {
        return 1 / (1 + Math.exp((g(opponentRatingDeviation) * -1) * (playerRating - opponentRating)));
    }
}
