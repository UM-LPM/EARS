package org.um.feri.ears.util.comparator;

import java.util.Comparator;

public class PointComparator implements Comparator<double[]> {
    private boolean maximizing;

    public PointComparator() {
        this.maximizing = true;
    }

    public void setMaximizing() {
        maximizing = true;
    }

    public void setMinimizing() {
        maximizing = false;
    }

    /**
     * Compares two double arrays representing points with multiple objectives.
     *
     * @param pointOne A double array representing a point.
     * @param pointTwo A double array representing a point.
     * @return -1 if pointOne is better than pointTwo, 1 if pointTwo is better than pointOne, or 0 if they are equal.
     */
    @Override
    public int compare(double[] pointOne, double[] pointTwo) {
        for (int i = pointOne.length - 1; i >= 0; i--) {
            if (isBetter(pointOne[i], pointTwo[i])) {
                return -1;
            } else if (isBetter(pointTwo[i], pointOne[i])) {
                return 1;
            }
        }
        return 0;
    }

    private boolean isBetter(double v1, double v2) {
        if (maximizing) {
            return (v1 > v2);
        } else {
            return (v2 > v1);
        }
    }
}
