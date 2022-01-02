package org.um.feri.ears.statistic.glicko2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.um.feri.ears.algorithms.AlgorithmBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.um.feri.ears.util.Comparator.RatingComparator;

public class ResultArena {
    private HashMap<String, Player> players;
    private List<Game> allGames;
    int period;

    public ResultArena() {
        this(1);
    }

    public ResultArena(int period) {
        players = new HashMap<String, Player>();
        allGames = new ArrayList<Game>();
        this.period = period;
    }

    public void addPlayer(Player p) {
        players.put(p.getPlayerId(), p);
    }

    //Default values
    public void addPlayer(AlgorithmBase algorithm, String id) {
        addPlayer(algorithm, id, 1500, 350, 0.06, 0, 0, 0);
    }

    public void addPlayer(AlgorithmBase algorithm, String id, double rating, double RD, double ratingVolatility) {
        addPlayer(algorithm, id, rating, RD, ratingVolatility, 0, 0, 0);
    }

    public void addPlayer(AlgorithmBase algorithm, String id, double rating, double RD, double ratingVolatility, int w, int l, int d) {
        players.put(id, new Player(algorithm, id, new Rating(rating, RD, ratingVolatility), w, l, d));
    }

    public Player getPlayer(String id) {
        return players.get(id);
    }

    /**
     * Players need to be in arena!
     *
     * @param gameResult
     * @param a
     * @param b
     * @param algorithm
     */
    public void addGameResult(double gameResult, String a, String b, String algorithm) {
        Player one = players.get(a);
        Player two = players.get(b);
        Game newGame = new Game(gameResult, one, two, algorithm);
        one.addGame(newGame);
        two.addGame(newGame);
        allGames.add(newGame);
    }

    /**
     * Players need to be in arena!
     *
     * @param gameResult
     * @param a
     * @param b
     * @param algorithm
     * @param indicator
     */
    public void addGameResult(double gameResult, String a, String b, String algorithm, String indicator) {
        Player one = players.get(a);
        Player two = players.get(b);
        Game newGame = new Game(gameResult, one, two, algorithm, indicator);
        one.addGame(newGame);
        two.addGame(newGame);
        allGames.add(newGame);
    }

    /**
     * Recalculates ranks for all games played. All ranks need to be updated.
     *
     * @return list of all players in the arena with new ratings.
     */
    public ArrayList<Player> recalcRatings() {
        period++;
        ArrayList<Player> ap = new ArrayList<Player>();
        RatingCalculations.computePlayerRatings(players, true); //changes ratings
        ap.addAll(players.values());
        Collections.sort(ap, new RatingComparator());
        return ap;

    }

    /**
     * Calculates the ratings for all unevaluated games.
     *
     * @return list of all players in the arena with new ratings.
     */
    public ArrayList<Player> calculateRatings() {
        period++;
        ArrayList<Player> ap = new ArrayList<Player>();
        RatingCalculations.computePlayerRatings(players, false); //changes ratings
        ap.addAll(players.values());
        Collections.sort(ap, new RatingComparator());
        return ap;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> ap = new ArrayList<Player>();
        ap.addAll(players.values());
        Collections.sort(ap, new RatingComparator());
        return ap;
    }

    public List<Game> getUnevaluatedGames() {
        List<Game> unevaluatedGames = new ArrayList<Game>();

        for (Game g : allGames) {
            if (!g.isEvaluated())
                unevaluatedGames.add(g);
        }
        return unevaluatedGames;
    }

    public void removePlayer(String id) {
        players.remove(id);

    }

    public String getPlayersJson() {

        JsonPlayer[] jsonPlayers = new JsonPlayer[players.size()];
        int index = 0;
        for (Player p : players.values()) {
            jsonPlayers[index++] = p.toJson();
        }

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = gson.toJson(jsonPlayers);

        return json;
    }
}
