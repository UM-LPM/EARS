package org.um.feri.ears.rating;

public class Game {
    public static final double LOSS = 0;
    public static final double WIN = 1;
    public static final double DRAW = 0.5;
    private double gameResult;
    private String idProblem; //some print info data
    private Player a, b;
    private String indicator; //only for multi-objective
    private boolean evaluated;

    public Game() {
        setEvaluated(false);
    }

    /**
     * Sets game result for first (a). if a win than b is lost.
     * With creation of new object new game result is set.
     *
     * @param gameResult
     * @param a
     * @param b
     */
    public Game(double gameResult, Player a, Player b, String idProblem) {
        super();
        setEvaluated(false);
        this.gameResult = gameResult;
        this.idProblem = idProblem; //needed in add
        this.a = a;
        this.b = b;
    }

    /**
     * Sets game result for first (a). if a win than b is lost.
     * With creation of new object new game result is set.
     *
     * @param gameResult
     * @param a
     * @param b
     */
    public Game(double gameResult, Player a, Player b, String idProblem, String indicator) {
        super();
        setEvaluated(false);
        this.gameResult = gameResult;
        this.idProblem = idProblem; //needed in add
        this.indicator = indicator;
        this.a = a;
        this.b = b;
    }

    public String toString() {
        return a.getPlayerId() + " : " + b.getPlayerId() + " r:" + gameResult;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    public String getOpponent(String one) {
        if (a.getPlayerId().equals(one)) return b.getPlayerId();
        return a.getPlayerId();
    }

    public Player getOpponent(Player player) {
        if (a.getPlayerId().equals(player.getPlayerId()))
            return b;
        return a;
    }

    public double getGameResult() {
        return gameResult;
    }

    public String getIdProblem() {
        return idProblem;
    }

    public String getIndicator() {
        return indicator;
    }


    /**
     * Different result depend for who we are asking. First or second.
     *
     * @param id
     * @return
     */
    public double getGameResult(String id) {
        if (a.getPlayerId().equals(id)) {
            return gameResult;
        }
        return 1 - gameResult; //win -> loss; loss->win draw->draw
    }

    public Player getA() {
        return a;
    }

    public void setA(Player a) {
        this.a = a;
    }

    public Player getB() {
        return b;
    }

    public void setB(Player b) {
        this.b = b;
    }

}
