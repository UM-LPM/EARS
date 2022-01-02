package org.um.feri.ears.statistic.true_skill;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Helper class to sort ranks in non-decreasing order.
 */
public class RankSorter {

    /** Static usage only **/ private RankSorter() { }

    /**
     * Returns a list of all the elements in items, sorted in non-descending
     * order, according to itemRanks. Uses a stable sort, and also sorts
     * itemRanks (in place)
     * 
     * @param items
     *            The items to sort according to the order specified by ranks.
     * @param itemRanks
     *            The ranks for each item where 1 is first place.
     * @return the items sorted according to their ranks
     */
    public static <T> List<T> sort(@NotNull Collection<T> items, @NotNull int[] itemRanks) {
        int lastObservedRank = 0;
        boolean needToSort = false;

        for (int currentRank : itemRanks) {
            // We're expecting ranks to go up (e.g. 1, 2, 2, 3, ...)
            // If it goes down, then we've got to sort it.
            if (currentRank < lastObservedRank) {
                needToSort = true;
                break;
            }

            lastObservedRank = currentRank;
        }

        // Don't bother doing more work, it's already in a good order
        if (!needToSort) return new ArrayList<>(items);

        // Get the existing items as an indexable list.
        List<T> itemsInList = new ArrayList<>(items);

        // item -> rank
        final Map<T, Integer> itemToRank = new HashMap<>();
        for (int i = 0; i < itemsInList.size(); i++)
            itemToRank.put(itemsInList.get(i), itemRanks[i]);
        
        Collections.sort(itemsInList, (o1, o2) -> itemToRank.get(o1).compareTo(itemToRank.get(o2)));
        
        Arrays.sort(itemRanks);
        return itemsInList;
    }
}