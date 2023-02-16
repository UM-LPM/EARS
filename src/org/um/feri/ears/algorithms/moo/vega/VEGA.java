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
package org.um.feri.ears.algorithms.moo.vega;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.VEGASelection;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.ObjectiveComparator;
import org.um.feri.ears.util.Util;

/**
 * Implementation of the Vector Evaluated Genetic Algorithm (VEGA).  VEGA should
 * be avoided in practice, since many modern algorithms outperform it and
 * exhibit better convergence properties, but is included due to its historical
 * significance.  VEGA is considered the earliest MOEA.  It supports M
 * objectives during the selection phase by selecting M different subgroups,
 * each selected based on the i-th objective value, for i=1,...,M.
 * <p>
 * There is one small algorithmic difference between this implementation and
 * [1].  In [1], applying the genetic operators fills the entire population.
 * However, since custom variation operators can be specified, it is possible
 * that the population will not be filled completely.  As a result, this
 * implementation will continue selecting parents until the population is full.
 * <p>
 * References:
 * <ol>
 *   <li>Schaffer, D. (1985).  Multiple Objective Optimization with Vector
 *       Evaluated Genetic Algorithms.  Proceedings of the 1st International
 *       Conference on Genetic Algorithms, pp. 93-100.
 * </ol>
 */
public class VEGA<Type extends Number, P extends Problem<NumberSolution<Type>>, T extends MOTask<Type>> extends MOAlgorithm<P, T, Type> {

    int populationSize;
    ParetoSolution<Type> population;

    CrossoverOperator<Type, T, NumberSolution<Type>> cross;
    MutationOperator<Type, T, NumberSolution<Type>> mut;

    public VEGA(CrossoverOperator crossover, MutationOperator mutation, int pop_size) {
        this.populationSize = pop_size;

        this.cross = crossover;
        this.mut = mutation;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo(
                "VEGA", "Vector Evaluated Genetic Algorithm",
                "\\bibitem{Schaffer1985}\nD.~Schaffer.\n\\newblock Multiple Objective Optimization with Vector Evaluated Genetic Algorithms.\n\\newblock \\emph{Proceedings of the 1st International Conference on Genetic Algorithms}, 93--100, 1985."
        );
    }

    @Override
    protected void start() throws StopCriterionException {

        // Create the initial solutionSet
        NumberSolution<Type> newSolution;
        for (int i = 0; i < populationSize; i++) {
            if (task.isStopCriterion())
                return;
            newSolution = new NumberSolution<Type>(task.getRandomEvaluatedSolution());
            // problem.evaluateConstraints(newSolution);
            population.add(newSolution);
        }

        do {
            // select the parents from the M different subgroups
            NumberSolution<Type>[] parents = performSelection(populationSize, population);

            // shuffle the parents
            Util.shuffle(parents);

            // loop until the next generation is filled
            int index = 0;
            boolean filled = false;

            population.clear();

            while (!filled) {

                NumberSolution<Type>[] offSpring = cross.execute(select(parents, index, 2), task);
                mut.execute(offSpring[0], task);
                mut.execute(offSpring[1], task);

                for (int i = 0; i < offSpring.length; i++) {
                    population.add(offSpring[i]);

                    if (population.size() >= populationSize) {
                        filled = true;
                        break;
                    }
                }

                index += 2 % populationSize;
            }

            // evaluate the offspring
            for (NumberSolution<Type> ind : population.solutions) {
                if (task.isStopCriterion())
                    return;
                task.eval(ind);
            }
            task.incrementNumberOfIterations();
        } while (!task.isStopCriterion());

        best = population;

    }

    @Override
    protected void init() {
        population = new ParetoSolution<Type>(populationSize);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

    /**
     * Returns the subset of parents for the next variation operator.
     *
     * @param parents all parents
     * @param index   the starting index
     * @param size    the size of the subset
     * @return the subset of parents
     */
    private NumberSolution<Type>[] select(NumberSolution<Type>[] parents, int index, int size) {
        NumberSolution<Type>[] result = new NumberSolution[size];

        for (int i = 0; i < size; i++) {
            result[i] = parents[(index + i) % parents.length];
        }

        return result;
    }

    /**
     * VEGA selection operator that selects parents based on only one of the
     * objectives.
     */

    private NumberSolution<Type>[] performSelection(int pop_size, ParetoSolution<Type> population) {
        VEGASelection<Type>[] selectors = new VEGASelection[numObj];
        for (int i = 0; i < numObj; i++) {
            selectors[i] = new VEGASelection<Type>(
                    new ObjectiveComparator(i));
        }

        NumberSolution<Type>[] result = new NumberSolution[pop_size];

        for (int i = 0; i < pop_size; i++) {
            VEGASelection<Type> selector = selectors[i % numObj];
            result[i] = new NumberSolution<Type>(selector.execute(population));
        }

        return result;

    }

}
