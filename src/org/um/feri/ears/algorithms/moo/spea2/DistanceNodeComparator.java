package org.um.feri.ears.algorithms.moo.spea2;

import java.util.Comparator;

public class DistanceNodeComparator implements Comparator<DistanceNode> {

    /**
     * Compares two <code>DistanceNode</code>.
     *
     * @param node1 Object representing a DistanceNode
     * @param node2 Object representing a DistanceNode
     * @return -1 if the distance of node1 is smaller than the distance of node2,
     * 0 if the distance of both are equals, and
     * 1 if the distance of node1 is bigger than the distance of node2
     */
    public int compare(DistanceNode node1, DistanceNode node2) {
        return Double.compare(node1.getDistance(), node2.getDistance());
    }
}
