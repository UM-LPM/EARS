package org.um.feri.ears.statistic.rating_system.glicko2;

import org.um.feri.ears.statistic.rating_system.GameResult;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.RatingType;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.comparator.RatingComparator;
import org.um.feri.ears.visualization.rating.RatingIntervalPlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TournamentResults {
    private HashMap<String, Player> playersMap;
    private ArrayList<Player> players;
    private List<Game> allGames;
    int period; // Rating period for Glicko2

    public TournamentResults() {
        this(1);
    }

    public TournamentResults(int period) {
        playersMap = new HashMap<>();
        players = new ArrayList<>();
        allGames = new ArrayList<>();
        this.period = period;
    }

    public void addPlayer(Player player) {
        if (playersMap.containsKey(player.getId())) {
            System.out.println("Arena already contains player with id: " + player.getId());
            return;
        }
        playersMap.put(player.getId(), player);
        players.add(player);
    }

    public void addPlayer(String id) {
        if (playersMap.containsKey(id)) {
            System.out.println("Arena already contains player with id: " + id);
            return;
        }
        Player player = new Player(id);
        playersMap.put(id, player);
        players.add(player);
    }

    public Player getPlayer(String id) {
        return playersMap.get(id);
    }

    /**
     * Adds a game results between players {@code playerOneId} and {@code playerTwoId}
     *
     * @param gameResult  result of the game
     * @param playerOneId id of player one
     * @param playerTwoId id of player two
     * @param problemName name of the problem solved
     */
    public void addGameResult(GameResult gameResult, String playerOneId, String playerTwoId, String problemName) {
        Player one = playersMap.get(playerOneId);
        Player two = playersMap.get(playerTwoId);
        Game newGame = new Game(gameResult, one, two, problemName);
        one.addGame(newGame);
        two.addGame(newGame);
        allGames.add(newGame);
    }

    /**
     * Adds a game results between players {@code @playerOneId} and {@code @playerTwoId}
     *
     * @param gameResult  result of the game
     * @param playerOneId id of player one
     * @param playerTwoId id of player two
     * @param problemName name of the problem solved
     * @param indicator   used in the comparison
     */
    public void addGameResult(GameResult gameResult, String playerOneId, String playerTwoId, String problemName, String indicator) {
        Player one = playersMap.get(playerOneId);
        Player two = playersMap.get(playerTwoId);
        Game newGame = new Game(gameResult, one, two, problemName, indicator);
        one.addGame(newGame);
        two.addGame(newGame);
        allGames.add(newGame);
    }

    public void displayResults(boolean showRatingCharts) {

        players.sort(new RatingComparator(RatingType.TRUE_SKILL_ONE_ON_ONE));
        System.out.println("TrueSkill One-On-One rating:");
        for (Player p : players) System.out.println(p.getId() + " - " + p.getRating(RatingType.TRUE_SKILL_ONE_ON_ONE));
        if (showRatingCharts)
            RatingIntervalPlot.displayChart(players, RatingType.TRUE_SKILL_ONE_ON_ONE, "TrueSkill One-On-One");

        players.sort(new RatingComparator(RatingType.TRUE_SKILL_FREE_FOR_ALL));
        System.out.println("\nTrueSkill Free-For-All rating:");
        for (Player p : players)
            System.out.println(p.getId() + " - " + p.getRating(RatingType.TRUE_SKILL_FREE_FOR_ALL));
        if (showRatingCharts)
            RatingIntervalPlot.displayChart(players, RatingType.TRUE_SKILL_FREE_FOR_ALL, "TrueSkill Free-For-All");

        players.sort(new RatingComparator(RatingType.GLICKO2));
        System.out.println("\nGlicko2 rating:");
        for (Player p : players) System.out.println(p.getId() + " - " + p.getRating(RatingType.GLICKO2));
        System.out.println("\nGame results:");
        for (Player p : players) System.out.println(p);
        if (showRatingCharts)
            RatingIntervalPlot.displayChart(players, RatingType.GLICKO2, "Rating Interval");
    }

    public void saveToFile(String fileName) {

        StringBuilder sb = new StringBuilder();

        players.sort(new RatingComparator(RatingType.TRUE_SKILL_ONE_ON_ONE));
        sb.append("TrueSkill One-On-One rating:\n");
        for (Player p : players) sb.append(p.getId()).append(" - ").append(p.getRating(RatingType.TRUE_SKILL_ONE_ON_ONE)).append("\n");

        players.sort(new RatingComparator(RatingType.TRUE_SKILL_FREE_FOR_ALL));
        sb.append("\nTrueSkill Free-For-All rating:\n");
        for (Player p : players) sb.append(p.getId()).append(" - ").append(p.getRating(RatingType.TRUE_SKILL_FREE_FOR_ALL)).append("\n");

        players.sort(new RatingComparator(RatingType.GLICKO2));
        sb.append("\nGlicko2 rating:\n");
        for (Player p : players) sb.append(p.getId()).append(" - ").append(p.getRating(RatingType.GLICKO2)).append("\n");
        sb.append("\nGame results:\n");
        for (Player p : players) sb.append(p).append("\n");
        RatingIntervalPlot.saveChartToFile(players, RatingType.GLICKO2, fileName, RatingIntervalPlot.FileType.EPS);
        RatingIntervalPlot.saveChartToFile(players, RatingType.GLICKO2, fileName, RatingIntervalPlot.FileType.PNG);

        Util.writeToFile(fileName+".txt", sb.toString());
    }

    /**
     * Recalculates ranks for all games played. All ranks need to be updated.
     */
    public void recalculateRatings() {
        period++;
        Glicko2RatingCalculator.calculateNewRatings(playersMap, true);
    }

    /**
     * Calculates the Glicko2 ratings for all unevaluated games.
     */
    public void calculateRatings() {
        period++;
        Glicko2RatingCalculator.calculateNewRatings(playersMap, false);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public List<Game> getUnevaluatedGames() {
        List<Game> unevaluatedGames = new ArrayList<Game>();

        for (Game game : allGames) {
            if (!game.isEvaluated())
                unevaluatedGames.add(game);
        }
        return unevaluatedGames;
    }

    /**
     * Removes the player with the give {@code @id}
     *
     * @param id of the player to be removed
     */
    public void removePlayer(String id) {
        Player player = playersMap.get(id);
        players.remove(player);
        playersMap.remove(id);
    }

    public Player.JsonPlayer[] getPlayersJson() {

        Player.JsonPlayer[] jsonPlayers = new Player.JsonPlayer[playersMap.size()];
        int index = 0;
        for (Player p : playersMap.values()) {
            jsonPlayers[index++] = p.toJson();
        }

        return jsonPlayers;
    }
}
