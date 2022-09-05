package org.um.feri.ears.statistic.rating_system;

import java.util.ArrayList;
import java.util.HashMap;

import org.um.feri.ears.statistic.rating_system.glicko2.Game;
import org.um.feri.ears.statistic.rating_system.glicko2.Glicko2Rating;
import org.um.feri.ears.statistic.rating_system.glicko2.GameOutcomes;
import org.um.feri.ears.statistic.rating_system.true_skill.Guard;
import org.um.feri.ears.statistic.rating_system.true_skill.TrueSkillRating;

public class Player {
    protected String id; // The identifier for the player, such as a name.
    protected Glicko2Rating glicko2rating; // Glicko2 rating
    protected TrueSkillRating oneOnOneTrueSkill; // TrueSkill One-On-One rating
    protected TrueSkillRating freeForAllTrueSkill; // TrueSkill Free-For-All rating
    protected ArrayList<Glicko2Rating> ratingHistory;
    protected ArrayList<Game> listOfGamesPlayed; // in last period (not evaluated yet)
    protected GameOutcomes sumGameOutcomes;
    public HashMap<String, GameOutcomes> wldPlayers; //id is algorithm
    public HashMap<String, GameOutcomes> wldProblems; //id is problem
    public HashMap<String, GameOutcomes> wldIndicator; //id is indicator

    private static final double DefaultPartialPlayPercentage = 1.0; // = 100% play time
    private static final double DefaultPartialUpdatePercentage = 1.0; // = receive 100% update

    /**
     * Indicates the percent of the time the player should be weighted where 0.0
     * indicates the player didn't play and 1.0 indicates the player played 100%
     * of the time.
     */
    private final double partialPlayPercentage;

    /**
     * Indicated how much of a skill update a player should receive where 0.0
     * represents no update and 1.0 represents 100% of the update.
     */
    private final double partialUpdatePercentage;

    /**
     * Constructs a player with the default Rating.
     *
     * @param id The identifier for the player, such as a name.
     */
    public Player(String id) {
        this(id, DefaultPartialPlayPercentage, DefaultPartialUpdatePercentage);
    }

    /**
     * Constructs a player with the default Rating.
     *
     * @param id                    The identifier for the player, such as a name.
     * @param partialPlayPercentage The weight percentage to give this player when calculating a
     *                              new rank.
     */
    public Player(String id, double partialPlayPercentage) {
        this(id, partialPlayPercentage, DefaultPartialUpdatePercentage);
    }

    /**
     * Constructs a player.
     *
     * @param id                      The identifier for the player, such as a name.
     * @param partialPlayPercentage   The weight percentage to give this player when calculating a
     *                                new rank.
     * @param partialUpdatePercentage Indicates how much of a skill update a player should receive
     *                                where 0 represents no update and 1.0 represents 100% of the
     *                                update.
     */
    public Player(String id, double partialPlayPercentage, double partialUpdatePercentage) {

        this.id = id;
        glicko2rating = GameInfo.getDefaultGlicko2Rating();
        freeForAllTrueSkill = GameInfo.getDefaultTrueSkillRating();
        oneOnOneTrueSkill = GameInfo.getDefaultTrueSkillRating();
        wldPlayers = new HashMap<>();
        wldProblems = new HashMap<>();
        wldIndicator = new HashMap<>();
        sumGameOutcomes = new GameOutcomes(0, 0, 0);
        listOfGamesPlayed = new ArrayList<>();
        ratingHistory = new ArrayList<>();
        ratingHistory.add(glicko2rating);

        // If they don't want to give a player an id, that's ok...
        Guard.argumentInRangeInclusive(partialPlayPercentage, 0, 1.0, "partialPlayPercentage");
        Guard.argumentInRangeInclusive(partialUpdatePercentage, 0, 1.0, "partialUpdatePercentage");
        this.partialPlayPercentage = partialPlayPercentage;
        this.partialUpdatePercentage = partialUpdatePercentage;
    }

    public Player(Player player) {
        id = player.id;
        glicko2rating = new Glicko2Rating(player.glicko2rating);
        freeForAllTrueSkill = new TrueSkillRating(player.freeForAllTrueSkill);
        oneOnOneTrueSkill = new TrueSkillRating(player.oneOnOneTrueSkill);
        sumGameOutcomes = new GameOutcomes(player.sumGameOutcomes);
        partialPlayPercentage = player.partialPlayPercentage;
        partialUpdatePercentage = player.partialUpdatePercentage;
    }

    /**
     * Adds a game the Player has played.
     *
     * @param game to be added.
     */
    public void addGame(Game game) {
        GameOutcomes tmpPlayer = wldPlayers.get(game.getOpponent(id));
        GameOutcomes tmpProblem = wldProblems.get(game.getProblemName());
        GameOutcomes tmpIndicator = wldIndicator.get(game.getIndicator());

        if (tmpPlayer == null) {
            tmpPlayer = new GameOutcomes(0, 0, 0);
            wldPlayers.put(game.getOpponent(id), tmpPlayer);
        }
        if (tmpProblem == null) {
            tmpProblem = new GameOutcomes(0, 0, 0);
            wldProblems.put(game.getProblemName(), tmpProblem);
        }
        if (tmpIndicator == null) {
            tmpIndicator = new GameOutcomes(0, 0, 0);
            if (game.getIndicator() != null)
                wldIndicator.put(game.getIndicator(), tmpIndicator);
        }
        if (game.getGameResult(id) == GameResult.DRAW) {
            sumGameOutcomes.incDraw();
            tmpPlayer.incDraw();
            tmpProblem.incDraw();
            tmpIndicator.incDraw();
        } else if (game.getGameResult(id) == GameResult.WIN) {
            sumGameOutcomes.incWin();
            tmpPlayer.incWin();
            tmpProblem.incWin();
            tmpIndicator.incWin();
        } else {
            sumGameOutcomes.incLoss();
            tmpPlayer.incLoss();
            tmpProblem.incLoss();
            tmpIndicator.incLoss();
        }
        listOfGamesPlayed.add(game);
    }

    public ArrayList<Glicko2Rating> getRatingHistory() {
        return ratingHistory;
    }

    public ArrayList<Game> getUnevaluatedGames() {

        ArrayList<Game> unevaluatedGames = new ArrayList<Game>();
        for (Game g : listOfGamesPlayed) {
            if (!g.isEvaluated())
                unevaluatedGames.add(g);
        }
        return unevaluatedGames;
    }

    public ArrayList<Game> getAllGames() {
        return listOfGamesPlayed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rating getRating(RatingType type) {
        switch (type) {
            case GLICKO2: return getGlicko2Rating();
            case TRUE_SKILL_ONE_ON_ONE: return getOneOnOneTrueSkill();
            case TRUE_SKILL_FREE_FOR_ALL: return getFreeForAllTrueSkill();
        }
        return null;
    }

    public TrueSkillRating getOneOnOneTrueSkill() {
        return oneOnOneTrueSkill;
    }

    public void setOneOnOneTrueSkill(TrueSkillRating oneOnOneTrueSkill) {
        this.oneOnOneTrueSkill = oneOnOneTrueSkill;
    }

    public Rating getFreeForAllTrueSkill() {
        return freeForAllTrueSkill;
    }

    public void setFreeForAllTrueSkill(TrueSkillRating freeForAllTrueSkill) {
        this.freeForAllTrueSkill = freeForAllTrueSkill;
    }

    public Glicko2Rating getGlicko2Rating() {
        return glicko2rating;
    }

    /**
     * Sets the new Glicko2 Rating.
     *
     * @param rating the new Glicko2 Rating.
     */
    public void setGlicko2Rating(Glicko2Rating rating) {
        //listOfGamesPlayed.clear(); //commented -> flag if game is evaluated
        this.glicko2rating = rating;
        ratingHistory.add(rating);
    }

    public GameOutcomes getSumWinLossDraw() {
        return sumGameOutcomes;
    }

    public double getPartialPlayPercentage() {
        return this.partialPlayPercentage;
    }

    public double getPartialUpdatePercentage() {
        return this.partialUpdatePercentage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Player other = (Player) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

    public String toString() {
        return id + " " + sumGameOutcomes + "\n\t Against:" + wldPlayers + "\n\t Problems:" + wldProblems + ((wldIndicator.size() == 0) ? "" : "\n\t Indicators:" + wldIndicator);
    }

    public static class JsonPlayer {
        public String playerId;
        public String submissionId;
        public String submissionAuthor;
        public double rating;
        public double RD;
        public double ratingIntervalLeft;
        public double ratingIntervalRight;
        public double ratingVolatility;
        public String sumWinLossDraw;
    }

    public JsonPlayer toJson() {
        JsonPlayer jp = new JsonPlayer();
        jp.playerId = id;
        jp.rating = glicko2rating.getRating();
        jp.RD = glicko2rating.getRatingDeviation();
        jp.ratingIntervalLeft = jp.rating - 2 * jp.RD;
        jp.ratingIntervalRight = jp.rating + 2 * jp.RD;
        jp.ratingVolatility = glicko2rating.getRatingVolatility();
        jp.sumWinLossDraw = sumGameOutcomes.toString() + " Against:" + wldPlayers.toString() + " Problems:" + wldProblems.toString() + ((wldIndicator.size() == 0) ? "" : " Indicators:" + wldIndicator.toString());
        return jp;
    }
}
