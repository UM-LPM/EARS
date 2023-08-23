package org.um.feri.ears.util.comparator;

import org.um.feri.ears.problems.Solution;

import java.util.Comparator;

public class DominanceComparator implements Comparator<double[]> {

    private final double epsilon;
    protected boolean[] objectiveMaximizationFlags;

    public DominanceComparator() {
        this(0.0);
    }

    public DominanceComparator(double epsilon) {
        this.epsilon = epsilon;
    }

    public void setObjectiveMaximizationFlags(boolean[] objectiveMaximizationFlags) {
        this.objectiveMaximizationFlags = objectiveMaximizationFlags;
    }

    /**
     * Compares the dominance relation of two solutions.
     *
     * @param point1 Object representing the first <code>Solution</code>.
     * @param point2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if point1 dominates point2, both are
     * non-dominated, or point1  is dominated by point2, respectively.
     */
    @Override
    public int compare(double[] point1, double[] point2) {
        if (point1 == null)
            return 1;
        else if (point2 == null)
            return -1;

        int dominate1 = 0; // dominate1 indicates if some objective of point1 dominates the same objective in point2.
        int dominate2 = 0; // dominate2 is complementary to dominate1.
        int flag; // stores the result of the comparison

        // Equal number of violated constraints. Applying a dominance Test then
        double value1, value2;
        for (int i = 0; i < point1.length; i++) {
            value1 = point1[i];
            value2 = point2[i];

            // if objectiveMaximizationFlags is not set use minimization as default
            if (objectiveMaximizationFlags != null && objectiveMaximizationFlags[i]) {
                if (value1 / (1 + epsilon) > value2) {
                    flag = -1;
                } else if (value2 / (1 + epsilon) > value1) {
                    flag = 1;
                } else {
                    flag = 0;
                }
            }
            else {
                if (value1 / (1 + epsilon) < value2) {
                    flag = -1;
                } else if (value2 / (1 + epsilon) < value1) {
                    flag = 1;
                } else {
                    flag = 0;
                }
            }

            if (flag == -1) {
                dominate1 = 1;
            }else if (flag == 1) {
                dominate2 = 1;
            }
        }

        if (dominate1 == dominate2) {
            return 0; // No one dominate the other
        } else if (dominate1 == 1) {
            return -1; // point1 dominates point2
        }
        return 1; // point2 dominates point1
    }
}