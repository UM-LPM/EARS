package org.um.feri.ears.statistic.true_skill;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.lang.Math.pow;

/**
 * Calculates the new ratings for only two players.
 * <remarks>
 * When you only have two players, a lot of the math simplifies. The main purpose of this class
 * is to show the bare minimum of what a TrueSkill implementation should have.
 * </remarks>
 */
public class TwoPlayerTrueSkillCalculator extends SkillCalculator
{
    public TwoPlayerTrueSkillCalculator()
    {
        super(EnumSet.noneOf(SupportedOptions.class), Range.<ITeam>exactly(2), Range.<IPlayer>exactly(1));
    }

    @Override
    public Map<IPlayer, Rating> calculateNewRatings(@NotNull GameInfo gameInfo, Collection<ITeam> teams, int... teamRanks)
    {
        validateTeamCountAndPlayersCountPerTeam(teams);

        // Make sure things are in order
        List<ITeam> teamsl = RankSorter.sort(teams, teamRanks);

        // Since we verified that each team has one player, we know the player is the first one
        ITeam winningTeam = teamsl.get(0);
        IPlayer winner = winningTeam.keySet().iterator().next();
        Rating winnerPreviousRating = winningTeam.get(winner);

        Map<IPlayer, Rating> losingTeam = teamsl.get(1);
        IPlayer loser = losingTeam.keySet().iterator().next();
        Rating loserPreviousRating = losingTeam.get(loser);

        boolean wasDraw = (teamRanks[0] == teamRanks[1]);

        Map<IPlayer, Rating> results = new HashMap<>();
        results.put(winner, CalculateNewRating(gameInfo, winnerPreviousRating, loserPreviousRating,
                                             wasDraw ? PairwiseComparison.DRAW : PairwiseComparison.WIN));
        results.put(loser, CalculateNewRating(gameInfo, loserPreviousRating, winnerPreviousRating,
                                            wasDraw ? PairwiseComparison.DRAW : PairwiseComparison.LOSE));

        // And we're done!
        return results;
    }

    private static Rating CalculateNewRating(GameInfo gameInfo, Rating selfRating, Rating opponentRating,
                                             PairwiseComparison comparison)
    {
        double drawMargin = DrawMargin.getDrawMarginFromDrawProbability(gameInfo.getDrawProbability(), gameInfo.getBeta());

        double c =
            Math.sqrt(
                pow(selfRating.getStandardDeviation(), 2)
                +
                  pow(opponentRating.getStandardDeviation(), 2)
                +
                2*pow(gameInfo.getBeta(), 2));

        double winningMean = selfRating.getMean();
        double losingMean = opponentRating.getMean();

        switch (comparison)
        {
            case WIN: case DRAW: /* NOP */ break;
            case LOSE:
                winningMean = opponentRating.getMean();
                losingMean = selfRating.getMean();
                break;
        }

        double meanDelta = winningMean - losingMean;

        double v;
        double w;
        double rankMultiplier;

        if (comparison != PairwiseComparison.DRAW)
        {
            // non-draw case
            v = TruncatedGaussianCorrectionFunctions.VExceedsMargin(meanDelta, drawMargin, c);
            w = TruncatedGaussianCorrectionFunctions.WExceedsMargin(meanDelta, drawMargin, c);
            rankMultiplier = comparison.multiplier;
        }
        else
        {
            v = TruncatedGaussianCorrectionFunctions.VWithinMargin(meanDelta, drawMargin, c);
            w = TruncatedGaussianCorrectionFunctions.WWithinMargin(meanDelta, drawMargin, c);
            rankMultiplier = 1;
        }

        double meanMultiplier = (pow(selfRating.getStandardDeviation(), 2) + pow(gameInfo.getDynamicsFactor(), 2))/c;

        double varianceWithDynamics = pow(selfRating.getStandardDeviation(), 2) + pow(gameInfo.getDynamicsFactor(), 2);
        double stdDevMultiplier = varianceWithDynamics/pow(c, 2);

        double newMean = selfRating.getMean() + (rankMultiplier*meanMultiplier*v);
        double newStdDev = Math.sqrt(varianceWithDynamics*(1 - w*stdDevMultiplier));

        return new Rating(newMean, newStdDev);
    }

    @Override
    public double calculateMatchQuality(@NotNull GameInfo gameInfo, Collection<ITeam> teams)
    {
        validateTeamCountAndPlayersCountPerTeam(teams);

        Iterator<ITeam> teamIt = teams.iterator();
        
        Rating player1Rating = teamIt.next().values().iterator().next();
        Rating player2Rating = teamIt.next().values().iterator().next();

        // We just use equation 4.1 found on page 8 of the TrueSkill 2006 paper:
        double betaSquared = pow(gameInfo.getBeta(), 2);
        double player1SigmaSquared = pow(player1Rating.getStandardDeviation(), 2);
        double player2SigmaSquared = pow(player2Rating.getStandardDeviation(), 2);

        // This is the square root part of the equation:
        double sqrtPart =
            Math.sqrt(
                (2*betaSquared)
                /
                (2*betaSquared + player1SigmaSquared + player2SigmaSquared));

        // This is the exponent part of the equation:
        double expPart =
            Math.exp(
                (-1*pow(player1Rating.getMean() - player2Rating.getMean(), 2))
                /
                (2*(2*betaSquared + player1SigmaSquared + player2SigmaSquared)));

        return sqrtPart*expPart;
    }
}