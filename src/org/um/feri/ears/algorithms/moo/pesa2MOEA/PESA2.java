/* Copyright 2009-2015 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.um.feri.ears.algorithms.moo.pesa2MOEA;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.random.RNG;

import java.util.*;
import java.util.Map.Entry;

/**
 * Implementation of the Pareto Envelope-based Selection Algorithm (PESA2).
 * <p>
 * References:
 * <ol>
 *   <li>Corne, D. W., N. R. Jerram, J. D. Knowles, and M. J. Oates (2001).
 *       PESA-II: Region-based Selection in Evolutionary Multiobjective
 *       Optimization.  Proceedings of the Genetic and Evolutionary
 *       Computation Conference (GECCO 2001), pp. 283-290.
 *   <li>Corne, D. W., J. D. Knowles, and M. J. Oates (2000).  The Pareto
 *       Envelope-based Selection Algorithm for Multiobjective Optimization.
 *       Parallel Problem Solving from Nature PPSN VI, pp. 839-848.
 * </ol>
 */

// VERY SLOW
public class PESA2<N extends Number, P extends NumberProblem<N>> extends MOAlgorithm<N, NumberSolution<N>, P> {

    int populationSize = 100;
    int archiveSize = 100;
    int bisections = 8;
    ParetoSolution<N> population;
    AdaptiveGridArchive<N> archive;

    /**
     * A mapping from grid index to the solutions occupying that grid index.
     * This enables PESA2's region-based selection.
     */
    protected Map<Integer, List<NumberSolution<N>>> gridMap;

    CrossoverOperator<P, NumberSolution<N>> cross;
    MutationOperator<P, NumberSolution<N>> mut;

    public PESA2(CrossoverOperator<P, NumberSolution<N>> crossover, MutationOperator<P, NumberSolution<N>> mutation, int populationSize) {
        this.populationSize = populationSize;

        this.cross = crossover;
        this.mut = mutation;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo(
                "PESA2", "Pareto Envelope-Based Selection Algorithm",
                "\\bibitem{corne2001}\nD.W.~Corne,N.R.~Jerram,J.D.~Knowles,M.J.~Oates\n\\newblock PESA-II: Region-based Selection in Evolutionary Multiobjective Optimization.\n\\newblock \\emph{Proceedings of the Genetic and Evolutionary Computation Conference (GECCO-2001)}, 283--290, 2001."
        );
    }

    @Override
    protected void start() throws StopCriterionException {

        RegionBasedSelection selection = new RegionBasedSelection();

        // Create the initial individual and evaluate it and his constraints
        for (int i = 0; i < populationSize; i++) {
            if (task.isStopCriterion())
                return;
            NumberSolution<N> solution = new NumberSolution<N>(task.getRandomEvaluatedSolution());
            // problem.evaluateConstraints(solution);
            population.add(solution);
        }


        if (archive != null) {
            archive.addAll(population);
        }

        do {
            // clear the population; selection draws from archive only
            population.clear();

            // generate the mapping between grid indices and solutions
            gridMap = createGridMap();

            while (population.size() < populationSize) {
                NumberSolution<N>[] parents = selection.select(2, archive);
                NumberSolution<N>[] offSpring = cross.execute(parents, task.problem);
                mut.execute(offSpring[0], task.problem);
                if (task.isStopCriterion())
                    break;
                task.eval(offSpring[0]);
                // problem.evaluateConstraints(offSpring[0]);
                population.add(offSpring[0]);
            }

            archive.addAll(population);
            task.incrementNumberOfIterations();
        } while (!task.isStopCriterion());

        best = archive;
    }

    /**
     * Returns a mapping from grid index to the solutions occupying that grid
     * index.  The key is the grid index, and the value is the list of solutions
     * occupying that index.
     *
     * @return a mapping from grid index to the solutions occupying that grid
     * index
     */
    protected Map<Integer, List<NumberSolution<N>>> createGridMap() {
        Map<Integer, List<NumberSolution<N>>> result = new HashMap<Integer, List<NumberSolution<N>>>();

        for (NumberSolution<N> solution : archive) {
            int index = archive.findIndex(solution);
            List<NumberSolution<N>> solutions = result.get(index);

            if (solutions == null) {
                solutions = new ArrayList<NumberSolution<N>>();
                result.put(index, solutions);
            }

            solutions.add(solution);
        }

        return result;
    }

    /**
     * Region-based selection.  Instead of selecting individual solutions,
     * PESA2 first selects hyperboxes using binary tournament selection to
     * favor hyperboxes with lower density.  Then, one solution from the
     * selected hyperbox is returned.
     */
    public class RegionBasedSelection {

        /**
         * Constructs a new region-based selection instance.
         */
        public RegionBasedSelection() {
            super();
        }

        public NumberSolution<N>[] select(int arity, ParetoSolution<N> population) {
            NumberSolution<N>[] result = new NumberSolution[arity];

            for (int i = 0; i < arity; i++) {
                result[i] = select();
            }

            return result;
        }

        /**
         * Draws a random entry from the map.
         *
         * @return the randomly selected map entry
         */
        protected Entry<Integer, List<NumberSolution<N>>> draw() {
            int index = RNG.nextInt(gridMap.size());
            Iterator<Entry<Integer, List<NumberSolution<N>>>> iterator = gridMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry<Integer, List<NumberSolution<N>>> entry = iterator.next();

                if (index == 0) {
                    return entry;
                } else {
                    index--;
                }
            }

            throw new NoSuchElementException();
        }

        /**
         * Selects one solution using PESA2's region-based selection scheme.
         *
         * @return the selected solution
         */
        protected NumberSolution<N> select() {
            Entry<Integer, List<NumberSolution<N>>> entry1 = draw();
            Entry<Integer, List<NumberSolution<N>>> entry2 = draw();
            Entry<Integer, List<NumberSolution<N>>> selection = entry1;

            // pick the grid index with smaller density
            if (entry1 != entry2) {
                if ((archive.getDensity(entry2.getKey()) < archive.getDensity(entry1.getKey())) ||
                        (archive.getDensity(entry2.getKey()) == archive.getDensity(entry1.getKey()) && RNG.nextBoolean())) {
                    selection = entry2;
                }
            }

            // randomly pick a solution from the selected grid index
            return RNG.nextItem(selection.getValue());
        }

    }

    @Override
    protected void init() {

        if (optimalParam) {
            switch (numObj) {
                case 1: {
                    populationSize = 100;
                    archiveSize = 100;
                    break;
                }
                case 2: {
                    populationSize = 100;
                    archiveSize = 100;
                    break;
                }
                case 3: {
                    populationSize = 300;
                    archiveSize = 300;
                    break;
                }
                default: {
                    populationSize = 500;
                    archiveSize = 500;
                    break;
                }
            }
        }

        archive = new AdaptiveGridArchive<N>(archiveSize, numObj, ArithmeticUtils.pow(2, bisections));
        population = new ParetoSolution<N>(populationSize);
    }


    @Override
    public void resetToDefaultsBeforeNewRun() {

    }

}
