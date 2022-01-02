package org.um.feri.ears.statistic.true_skill;

import java.util.*;

/**
 * Helper class for working with a single team.
 */
public class Team extends HashMap<IPlayer, Rating> implements ITeam {

    /** Generated UID for serialization **/
    private static final long serialVersionUID = -5274158841312594600L;

    /** Constructs a new team. **/
    public Team() { }

    /**
     * Constructs a Team and populates it with the specified player.
     * 
     * @param player
     *            The player to add.
     * @param rating
     *            The rating of the player.
     */
    public Team(IPlayer player, Rating rating) { addPlayer(player, rating); }

    /**
     * Adds the player to the team.
     * 
     * @param player
     *            The player to add.
     * @param rating
     *            The rating of the player
     * @returns The instance of the team (for chaining convenience).
     */
    public final Team addPlayer(IPlayer player, Rating rating) {
        put(player, rating);
        return this;
    }

    /**
     * Concatenates multiple teams into a list of teams.
     * 
     * @param teams
     *            The teams to concatenate together.
     * @returns A sequence of teams.
     */
    public static Collection<ITeam> concat(ITeam... teams) {
        List<ITeam> teamslist = new ArrayList<>();
        Collections.addAll(teamslist, teams);
        return teamslist;
    }
}