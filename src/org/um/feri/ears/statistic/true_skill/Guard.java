package org.um.feri.ears.statistic.true_skill;

/**
 * Verifies argument contracts.
 * <p>
 * These are used until I figure out how to do this better in Java
 */
public class Guard {

    /** No instances allowed **/ private Guard() { }

    public static void argumentInRangeInclusive(double value, double min,
            double max, String parameterName) {
        if ((value < min) || (value > max)) {
            throw new IndexOutOfBoundsException(parameterName);
        }
    }
}