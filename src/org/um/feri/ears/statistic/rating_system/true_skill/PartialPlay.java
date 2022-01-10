package org.um.feri.ears.statistic.rating_system.true_skill;

public final class PartialPlay {

    public static double getPartialPlayPercentage(Object player) {
        // If the player doesn't support the interface, assume 1.0 == 100%
        if (player instanceof ISupportPartialPlay) {
            double partialPlayPercentage = ((ISupportPartialPlay) player)
                    .getPartialPlayPercentage();

            // HACK to get around bug near 0
            final double smallestPercentage = 0.0001;
            if (partialPlayPercentage < smallestPercentage) {
                partialPlayPercentage = smallestPercentage;
            }

            return partialPlayPercentage;
        }
        return 1.0;
    }
}