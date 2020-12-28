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

package org.um.feri.ears.util.Comparator;

import java.util.Comparator;

import org.um.feri.ears.problems.moo.MOSolutionBase;


/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on a constraint violation test + dominance checking, as in NSGA-II.
 */
public class DominanceComparator<Type> implements Comparator<MOSolutionBase<Type>> {

    private double epsilon;
    OverallConstraintViolationComparator<Type> violationConstraintComparator;

    public DominanceComparator() {
        this(0.0);
    }

    public DominanceComparator(double epsilon) {
        violationConstraintComparator = new OverallConstraintViolationComparator<>();
        this.epsilon = epsilon;
    }


    /**
     * Compares the dominance relation of two solutions.
     *
     * @param solution1 Object representing the first <code>Solution</code>.
     * @param solution2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if solution1 dominates solution2, both are
     * non-dominated, or solution1  is dominated by solution2, respectively.
     */
    public int compare(MOSolutionBase<Type> solution1, MOSolutionBase<Type> solution2) {
        if (solution1 == null)
            return 1;
        else if (solution2 == null)
            return -1;

        int dominate1; // dominate1 indicates if some objective of solution1
        // dominates the same objective in solution2. dominate2
        int dominate2; // is the complementary of dominate1.

        dominate1 = 0;
        dominate2 = 0;

        int flag; // stores the result of the comparison


        // Test to determine whether at least one solution violates some constraint
        if (violationConstraintComparator.needToCompare(solution1, solution2))
            return violationConstraintComparator.compare(solution1, solution2);

        // Equal number of violated constraints. Applying a dominance Test then
        double value1, value2;
        for (int i = 0; i < solution1.numberOfObjectives(); i++) {
            value1 = solution1.getObjective(i);
            value2 = solution2.getObjective(i);
            if (value1 / (1 + epsilon) < value2) {
                flag = -1;
            } else if (value2 / (1 + epsilon) < value1) {
                flag = 1;
            } else {
                flag = 0;
            }

            if (flag == -1) {
                dominate1 = 1;
            }

            if (flag == 1) {
                dominate2 = 1;
            }
        }

        if (dominate1 == dominate2) {
            return 0; // No one dominate the other
        }
        if (dominate1 == 1) {
            return -1; // solution1 dominates solution2
        }
        return 1; // solution2 dominates solution1
    }
}
