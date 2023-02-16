//  DominanceComparator.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.um.feri.ears.util.comparator;


import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Solution;

import java.util.Comparator;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on a constraint violation test + dominance checking, as in NSGA-II.
 */
public class DominanceComparator implements Comparator<Solution> {

    private final double epsilon;
    protected boolean[] objectiveMaximizationFlags;
    OverallConstraintViolationComparator violationConstraintComparator;
    public DominanceComparator() {
        this(0.0);
    }

    public DominanceComparator(double epsilon) {
        violationConstraintComparator = new OverallConstraintViolationComparator();
        this.epsilon = epsilon;
    }

    public void setObjectiveMaximizationFlags(boolean[] objectiveMaximizationFlags) {
        this.objectiveMaximizationFlags = objectiveMaximizationFlags;
    }

    /**
     * Compares the dominance relation of two solutions.
     *
     * @param solution1 Object representing the first <code>Solution</code>.
     * @param solution2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if solution1 dominates solution2, both are
     * non-dominated, or solution1  is dominated by solution2, respectively.
     */
    @Override
    public int compare(Solution solution1, Solution solution2) {
        if (solution1 == null)
            return 1;
        else if (solution2 == null)
            return -1;

        int dominate1 = 0; // dominate1 indicates if some objective of solution1 dominates the same objective in solution2.
        int dominate2 = 0; // dominate2 is complementary to dominate1.
        int flag; // stores the result of the comparison


        // Test to determine whether at least one solution violates some constraint
        if (violationConstraintComparator.needToCompare(solution1, solution2))
            return violationConstraintComparator.compare(solution1, solution2);

        // Equal number of violated constraints. Applying a dominance Test then
        double value1, value2;
        for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
            value1 = solution1.getObjective(i);
            value2 = solution2.getObjective(i);

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
            return -1; // solution1 dominates solution2
        }
        return 1; // solution2 dominates solution1
    }
}
