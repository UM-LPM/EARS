package org.um.feri.ears.statistic.glicko2;

import java.util.ArrayList;
import java.util.HashMap;

import org.um.feri.ears.algorithms.AlgorithmBase;

public class Player {
    protected AlgorithmBase algorithm;
    protected String playerId; // name
    protected Rating r; // current rating
    protected ArrayList<Rating> ratingHistory;
    protected ArrayList<Game> listOfGamesPlayed; // in last period (not evaluated yet)
    protected WinLossDraw sumWinLossDraw;
    public HashMap<String, WinLossDraw> wldPlayers; //id is algorithm
    public HashMap<String, WinLossDraw> wldProblems; //id is problem
    public HashMap<String, WinLossDraw> wldIndicator; //id is indicator

    public Player(AlgorithmBase algorithm, String playerId, Rating r, int w, int l, int d) {
        this.algorithm = algorithm;
        wldPlayers = new HashMap<String, WinLossDraw>();
        wldProblems = new HashMap<String, WinLossDraw>();
        wldIndicator = new HashMap<String, WinLossDraw>();
        sumWinLossDraw = new WinLossDraw(w, l, d);
        this.playerId = playerId;
        this.r = r;
        listOfGamesPlayed = new ArrayList<Game>();
        ratingHistory = new ArrayList<Rating>();
        ratingHistory.add(r);
    }

    public Player(String playerId) {
        this(null, playerId, new Rating(1500, 350, 0.06), 0, 0, 0); // default from org. paper
    }

    public Player(Player p) {
        this.algorithm = p.algorithm;
        this.playerId = p.playerId;
        this.r = new Rating(p.r);
        this.sumWinLossDraw = new WinLossDraw(p.sumWinLossDraw);
    }

    public AlgorithmBase getAlgorithm() {
        return algorithm;
    }

    /**
     * Adds new game
     *
     * @param newone
     */
    public void addGame(Game newone) {
        WinLossDraw tmpPlayer = wldPlayers.get(newone.getOpponent(playerId));
        WinLossDraw tmpProblem = wldProblems.get(newone.getIdProblem());
        WinLossDraw tmpIndicator = wldIndicator.get(newone.getIndicator());

        if (tmpPlayer == null) {
            tmpPlayer = new WinLossDraw(0, 0, 0);
            wldPlayers.put(newone.getOpponent(playerId), tmpPlayer);
        }
        if (tmpProblem == null) {
            tmpProblem = new WinLossDraw(0, 0, 0);
            wldProblems.put(newone.getIdProblem(), tmpProblem);
        }
        if (tmpIndicator == null) {
            tmpIndicator = new WinLossDraw(0, 0, 0);
            if (newone.getIndicator() != null)
                wldIndicator.put(newone.getIndicator(), tmpIndicator);
        }
        if (newone.getGameResult(playerId) == Game.DRAW) {
            sumWinLossDraw.incDraw();
            tmpPlayer.incDraw();
            tmpProblem.incDraw();
            tmpIndicator.incDraw();
        } else if (newone.getGameResult(playerId) == Game.WIN) {
            sumWinLossDraw.incWin();
            tmpPlayer.incWin();
            tmpProblem.incWin();
            tmpIndicator.incWin();
        } else {
            sumWinLossDraw.incLoss();
            tmpPlayer.incLoss();
            tmpProblem.incLoss();
            tmpIndicator.incLoss();
        }
        listOfGamesPlayed.add(newone);

    }

    public ArrayList<Rating> getRatingHistory() {
        return ratingHistory;
    }

    public ArrayList<Game> getUnEvaluatedGames() {

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

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Rating getRatingData() {
        return r;
    }

    /**
     * New rating is calculated. listOfGamePlayed is deleted!
     *
     * @param r
     */
    public void setRatingData(Rating r) {
        //listOfGamesPlayed.clear(); //commented -> flag if game is evaluated
        this.r = r;
        ratingHistory.add(r);
    }

    public WinLossDraw getSumWinLossDraw() {
        return sumWinLossDraw;
    }

    public String toString() {
        return playerId + "; " + r + sumWinLossDraw + "\n\t Against:" + wldPlayers + "\n\t Problems:" + wldProblems + ((wldIndicator.size() == 0) ? "" : "\n\t Indicators:" + wldIndicator);
    }

    public JsonPlayer toJson() {
        JsonPlayer jp = new JsonPlayer();
        jp.playerId = playerId;
        jp.rating = r.getRating();
        jp.RD = r.getRD();
        jp.ratingIntervalLeft = jp.rating - 2 * jp.RD;
        jp.ratingIntervalRight = jp.rating + 2 * jp.RD;
        jp.ratingVolatility = r.getRatingVolatility();
        jp.sumWinLossDraw = sumWinLossDraw.toString() + " Against:" + wldPlayers.toString() + " Problems:" + wldProblems.toString() + ((wldIndicator.size() == 0) ? "" : " Indicators:" + wldIndicator.toString());
        if (algorithm != null) {
            jp.submissionAuthor = algorithm.getCustomInfoByKey("submissionAuthor");
            jp.submissionId = algorithm.getCustomInfoByKey("submissionId");
        }
        return jp;
    }
}
