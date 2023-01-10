//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.um.feri.ears.algorithms.moo.pso;

import java.util.Comparator;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.NondominatedPopulation;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.SolutionListUtils;

/**
 * Created by Antonio J. Nebro on 24/09/14.
 * Modified by Juanjo on 07/04/2015
 */
public class CrowdingDistanceArchive<Type extends Number> {
    private Comparator<NumberSolution<Type>> crowdingDistanceComparator;
    private CrowdingDistance<Type> crowdingDistance;
    protected NondominatedPopulation<Type> archive;
    protected int maxSize;

    public CrowdingDistanceArchive(int maxSize) {
        this.maxSize = maxSize;
        this.archive = new NondominatedPopulation<Type>();
        crowdingDistanceComparator = new CrowdingDistanceComparator<Type>();
        crowdingDistance = new CrowdingDistance<Type>();
    }


    public void prune() {
        if (getSolutionList().size() > getMaxSize()) {
            computeDensityEstimator();
            NumberSolution<Type> worst = new SolutionListUtils().findWorstSolution(getSolutionList(), crowdingDistanceComparator);
            getSolutionList().remove(worst);
        }
    }

    public Comparator<NumberSolution<Type>> getComparator() {
        return crowdingDistanceComparator;
    }

    public void computeDensityEstimator() {
        crowdingDistance.computeDensityEstimator(getSolutionList());
    }


    public boolean add(NumberSolution<Type> solution) {
        boolean success = archive.add(solution);
        if (success) {
            prune();
        }

        return success;
    }

    public NumberSolution<Type> get(int index) {
        return getSolutionList().get(index);
    }

    public ParetoSolution<Type> getSolutionList() {
        return archive;
    }

    public int size() {
        return archive.size();
    }

    public int getMaxSize() {
        return maxSize;
    }
}
