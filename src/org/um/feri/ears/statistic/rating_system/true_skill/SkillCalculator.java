package org.um.feri.ears.statistic.rating_system.true_skill;

import org.jetbrains.annotations.NotNull;
import org.um.feri.ears.statistic.rating_system.GameInfo;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.Rating;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

/**
 * Base class for all skill calculator implementations.
 */
public abstract class SkillCalculator {
    
    public enum SupportedOptions { PartialPlay, PartialUpdate}

    private final EnumSet<SupportedOptions> supportedOptions;
    private final Range<Player> playersPerTeamAllowed;
    private final Range<ITeam> totalTeamsAllowed;
    
    protected SkillCalculator(EnumSet<SupportedOptions> supportedOptions,
                              Range<ITeam> totalTeamsAllowed, 
                              Range<Player> playerPerTeamAllowed) {
        this.supportedOptions = supportedOptions;
        this.totalTeamsAllowed = totalTeamsAllowed;
        this.playersPerTeamAllowed = playerPerTeamAllowed;
    }

    public boolean isSupported(SupportedOptions option) {
        return supportedOptions.contains(option);
    }
    
    /**
     * Calculates new ratings based on the prior ratings and team ranks.
     * 
     * @param gameInfo
     *            Parameters for the game.
     * @param teams
     *            A mapping of team players and their ratings.
     * @param teamRanks
     *            The ranks of the teams where 1 is first place. For a tie,
     *            repeat the number (e.g. 1, 2, 2)
     * @return All the players and their new ratings.
     */
    public abstract Map<Player, Rating> calculateNewRatings(GameInfo gameInfo,
                                                            Collection<ITeam> teams, int... teamRanks);

    /**
     * Calculates the match quality as the likelihood of all teams drawing.
     * 
     * @param gameInfo
     *            Parameters for the game.
     * @param teams
     *            A mapping of team players and their ratings.
     * @return The quality of the match between the teams as a percentage (0% =
     *          bad, 100% = well matched).
     */
    public abstract double calculateMatchQuality(GameInfo gameInfo,
            Collection<ITeam> teams);

    protected void validateTeamCountAndPlayersCountPerTeam(
            Collection<ITeam> teams) {
        validateTeamCountAndPlayersCountPerTeam(teams, totalTeamsAllowed, playersPerTeamAllowed);
    }

    private static void validateTeamCountAndPlayersCountPerTeam(
            @NotNull Collection<ITeam> teams,
            Range<ITeam> totalTeams,
            Range<Player> playersPerTeam) {
        int countOfTeams = 0;
        for (ITeam currentTeam : teams) {
            if (playersPerTeam.isInRange(currentTeam.size())) {
                throw new IllegalArgumentException();
            }
            countOfTeams++;
        }

        if (totalTeams.isInRange(countOfTeams)) {
            throw new IllegalArgumentException();
        }
    }
}