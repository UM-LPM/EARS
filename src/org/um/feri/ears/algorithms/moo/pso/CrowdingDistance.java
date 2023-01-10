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

package org.um.feri.ears.algorithms.moo.pso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.ObjectiveComparator;


/**
 * This class implements the crowding distance
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class CrowdingDistance<Type extends Number> {

    /**
     * Assigns crowding distances to all solutions in a <code>SolutionSet</code>.
     *
     * @param solutionList The <code>SolutionSet</code>.
     */
    public void computeDensityEstimator(ParetoSolution<Type> solutionList) {
        int size = solutionList.size();

        if (size == 0) {
            return;
        }

        if (size == 1) {
            solutionList.get(0).setAttribute(getAttributeID(), Double.POSITIVE_INFINITY);
            return;
        }

        if (size == 2) {
            solutionList.get(0).setAttribute(getAttributeID(), Double.POSITIVE_INFINITY);
            solutionList.get(1).setAttribute(getAttributeID(), Double.POSITIVE_INFINITY);

            return;
        }

        //Use a new SolutionSet to avoid altering the original solutionSet
        List<NumberSolution<Type>> front = new ArrayList<>(size);
        for (NumberSolution<Type> solution : solutionList) {
            front.add(solution);
        }

        for (int i = 0; i < size; i++) {
            front.get(i).setAttribute(getAttributeID(), 0.0);
        }

        double objetiveMaxn;
        double objetiveMinn;
        double distance;

        int numberOfObjectives = solutionList.get(0).getNumberOfObjectives();

        for (int i = 0; i < numberOfObjectives; i++) {
            // Sort the population by Obj n
            Collections.sort(front, new ObjectiveComparator<Type>(i));
            objetiveMinn = front.get(0).getObjective(i);
            objetiveMaxn = front.get(front.size() - 1).getObjective(i);

            //Set de crowding distance
            front.get(0).setAttribute(getAttributeID(), Double.POSITIVE_INFINITY);
            front.get(size - 1).setAttribute(getAttributeID(), Double.POSITIVE_INFINITY);

            for (int j = 1; j < size - 1; j++) {
                distance = front.get(j + 1).getObjective(i) - front.get(j - 1).getObjective(i);
                distance = distance / (objetiveMaxn - objetiveMinn);
                distance += (double) front.get(j).getAttribute(getAttributeID());
                front.get(j).setAttribute(getAttributeID(), distance);
            }
        }
    }


    public Object getAttribute(NumberSolution<Type> solution) {
        return solution.getAttribute(getAttributeID());
    }


    public void setAttribute(NumberSolution<Type> solution, Object value) {
        solution.setAttribute(getAttributeID(), value);
    }

    public Object getAttributeID() {
        return this.getClass();
    }
}