package org.um.feri.ears.statistic.rating_system;

import java.util.TreeMap;

/**
 * Represents a game result in a comparison between two players
 */
public enum GameResult {
    WIN(1, 1),
    DRAW(0.5, 0),
    LOSE(0, -1);

    public final double glicko2Score;
    public final int trueSkillMultiplier;

    GameResult(double glicko2Score, int trueSkillMultiplier) {
        this.glicko2Score = glicko2Score;
        this.trueSkillMultiplier = trueSkillMultiplier;
    }

    private static TreeMap<Integer, GameResult> revmap = new TreeMap<>();

    static {
        for (GameResult pc : GameResult.values())
            revmap.put(pc.trueSkillMultiplier, pc);
    }

    public static GameResult fromMultiplier(int multiplier) {
        return revmap.get(multiplier);
    }
}