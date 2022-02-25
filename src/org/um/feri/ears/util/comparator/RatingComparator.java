package org.um.feri.ears.util.comparator;

import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.RatingType;

import java.util.Comparator;

public class RatingComparator implements Comparator<Player> {

    RatingType type;

    public RatingComparator() {
        this(RatingType.GLICKO2);
    }

    public RatingComparator(RatingType type) {
        this.type = type;
    }

    @Override
    public int compare(Player a, Player b) {
        return Double.compare(b.getRating(type).getRating(), a.getRating(type).getRating());
    }
}
