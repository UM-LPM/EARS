package org.um.feri.ears.statistic.true_skill;

import java.util.TreeMap;

/**
 * Represents a comparison between two players.
 * <p>
 * The actual values for the enum were chosen so that the also correspond to the
 * multiplier for updates to means.
 */
public enum PairwiseComparison {
    WIN(1),
    DRAW(0),
    LOSE(-1);
    
    public final int multiplier;
    
    PairwiseComparison(int multiplier) { this.multiplier = multiplier; }

    private static TreeMap<Integer, PairwiseComparison> revmap = new TreeMap<>();
    static { for (PairwiseComparison pc : PairwiseComparison.values())
            revmap.put(pc.multiplier, pc);
    }
    
    public static PairwiseComparison fromMultiplier(int multiplier) {
        return revmap.get(multiplier);
    }
}