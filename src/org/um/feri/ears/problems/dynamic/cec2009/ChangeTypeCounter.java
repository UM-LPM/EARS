package org.um.feri.ears.problems.dynamic.cec2009;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Counter for each environment change type from ChangeType enum class.
 */
public class ChangeTypeCounter {

    private final int[] occurrences = new int[ChangeType.values().length];

    public ChangeTypeCounter() {
        Arrays.fill(occurrences, 0);
    }

    public void increaseNumberOfOccurrences(@NotNull ChangeType changeType) {
        occurrences[changeType.ordinal()]++;
    }

    public int getNumberOfOccurrences(@NotNull ChangeType changeType) {
        return occurrences[changeType.ordinal()];
    }
}
