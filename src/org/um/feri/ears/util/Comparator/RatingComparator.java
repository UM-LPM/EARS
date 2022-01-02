package org.um.feri.ears.util.Comparator;

import org.um.feri.ears.statistic.glicko2.Player;

import java.util.Comparator;

public class RatingComparator implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        return Double.compare(b.getRatingData().getRating(), a.getRatingData().getRating());
    }
}
