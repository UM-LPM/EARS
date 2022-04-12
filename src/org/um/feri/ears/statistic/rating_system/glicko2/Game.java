package org.um.feri.ears.statistic.rating_system.glicko2;

import org.um.feri.ears.statistic.rating_system.GameResult;
import org.um.feri.ears.statistic.rating_system.Player;

public class Game {
    private GameResult gameResult;
    private String problemName;
    private Player playerOne, playerTwo;
    private String indicator; //only for multi-objective
    private boolean evaluated;

    public Game() {
        setEvaluated(false);
    }

    /**
     * Creates a game with result {@code gameResult} between players {@code playerOne} and {@code playerTwo}. If {@code playerOne} won then {@code playerTwo} lost
     *
     * @param gameResult result of the game
     * @param playerOne first player
     * @param playerTwo second player
     */
    public Game(GameResult gameResult, Player playerOne, Player playerTwo, String problemName) {
        super();
        setEvaluated(false);
        this.gameResult = gameResult;
        this.problemName = problemName;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    /**
     * Creates a game with result {@code gameResult} between players {@code playerOne} and {@code playerTwo}. If {@code playerOne} won then {@code playerTwo} lost.
     *
     * @param gameResult result of the game
     * @param playerOne first player
     * @param playerTwo second player
     */
    public Game(GameResult gameResult, Player playerOne, Player playerTwo, String problemName, String indicator) {
        super();
        setEvaluated(false);
        this.gameResult = gameResult;
        this.problemName = problemName;
        this.indicator = indicator;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public String toString() {
        return playerOne.getId() + " : " + playerTwo.getId() + " r:" + gameResult;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    public String getOpponent(String one) {
        if (playerOne.getId().equals(one)) return playerTwo.getId();
        return playerOne.getId();
    }

    public Player getOpponent(Player player) {
        if (playerOne.getId().equals(player.getId()))
            return playerTwo;
        return playerOne;
    }

    public String getProblemName() {
        return problemName;
    }

    public String getIndicator() {
        return indicator;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    /**
     * Returns the game result for player with {@code id}
     *
     * @param id of the player
     * @return game result for player with {@code id}
     */
    public GameResult getGameResult(String id) {
        if (playerOne.getId().equals(id)) {
            return gameResult;
        }

        switch (gameResult) {
            case WIN: return GameResult.LOSE;
            case LOSE: return GameResult.WIN;
        }
        return GameResult.DRAW;
    }

    /**
     * Returns the game result score for player with {@code id}
     *
     * @param id of the player
     * @return game result score for player with {@code id}
     */
    public double getGameResultScore(String id) {
        if (playerOne.getId().equals(id)) {
            return gameResult.glicko2Score;
        }
        return 1 - gameResult.glicko2Score; //win -> loss, lose->win, draw->draw
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

}
