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
public class CrowdingDistanceArchive<N extends Number> {
    private Comparator<NumberSolution<N>> crowdingDistanceComparator;
    private CrowdingDistance<N> crowdingDistance;
    protected NondominatedPopulation<N> archive;
    protected int maxSize;

    public CrowdingDistanceArchive(int maxSize) {
        this.maxSize = maxSize;
        this.archive = new NondominatedPopulation<N>();
        crowdingDistanceComparator = new CrowdingDistanceComparator<N>();
        crowdingDistance = new CrowdingDistance<N>();
    }


    public void prune() {
        if (getSolutionList().size() > getMaxSize()) {
            computeDensityEstimator();
            NumberSolution<N> worst = new SolutionListUtils().findWorstSolution(getSolutionList(), crowdingDistanceComparator);
            getSolutionList().remove(worst);
        }
    }

    public Comparator<NumberSolution<N>> getComparator() {
        return crowdingDistanceComparator;
    }

    public void computeDensityEstimator() {
        crowdingDistance.computeDensityEstimator(getSolutionList());
    }


    public boolean add(NumberSolution<N> solution) {
        boolean success = archive.add(solution);
        if (success) {
            prune();
        }

        return success;
    }

    public NumberSolution<N> get(int index) {
        return getSolutionList().get(index);
    }

    public ParetoSolution<N> getSolutionList() {
        return archive;
    }

    public int size() {
        return archive.size();
    }

    public int getMaxSize() {
        return maxSize;
    }
}
