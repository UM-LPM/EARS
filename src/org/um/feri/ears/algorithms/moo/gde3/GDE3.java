//  GDE3.java
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

package org.um.feri.ears.algorithms.moo.gde3;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.DifferentialEvolutionSelection;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.CrowdingComparator;
import org.um.feri.ears.util.Distance;
import org.um.feri.ears.util.comparator.SolutionDominanceComparator;
import org.um.feri.ears.util.Ranking;

/**
 * This class implements the GDE3 algorithm.
 */
public class GDE3<N extends Number, P extends NumberProblem<N>> extends MOAlgorithm<N, NumberSolution<N>, P> {

    ParetoSolution<N> population;
    ParetoSolution<N> offspringPopulation;

    int populationSize;

    CrossoverOperator<P, NumberSolution<N>> cross;

    public GDE3(CrossoverOperator<P, NumberSolution<N>> crossover, int populationSize) {
        this.populationSize = populationSize;
        this.cross = crossover;

        au = new Author("miha", "miha.ravber at gamil.com");
        ai = new AlgorithmInfo(
                "GDE3", "Generalized Differential Evolution 3",
                "\\bibitem{Kukkonen2009}\nS.~Kukkonen, J.~Lampinen\n\\newblock Performance Assessment of Generalized Differential Evolution 3 with a Given Set of Constrained Multi-Objective Test Problems.\n\\newblock \\emph{2009 IEEE Congress on Evolutionary Computation}, 1943--1950, 2009."
                );
    }

    @Override
    protected void init() {
        population = new ParetoSolution<N>(populationSize);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

    @Override
    protected void start() throws StopCriterionException {

        Distance<N> distance;

        distance = new Distance<>();
        SolutionDominanceComparator dominance = new SolutionDominanceComparator();

        NumberSolution<N>[] parent;

        DifferentialEvolutionSelection<N> des = new DifferentialEvolutionSelection<>();

        // Create the initial solutionSet
        NumberSolution<N> newSolution;
        for (int i = 0; i < populationSize; i++) {
            if (task.isStopCriterion())
                return;
            newSolution = new NumberSolution<>(task.generateRandomEvaluatedSolution());
            // problem.evaluateConstraints(newSolution);
            population.add(newSolution);
        }

        // Generations ...
        while (!task.isStopCriterion()) {
            // Create the offSpring solutionSet
            offspringPopulation = new ParetoSolution(populationSize * 2);

            for (int i = 0; i < populationSize; i++) {
                // Obtain parents. Two parameters are required: the population and the index of the current individual
                try {
                    des.setCurrentIndex(i);
                    parent = des.execute(population, task.problem);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("The population has less than four solutions");
                    break;
                }

                NumberSolution<N> child;
                // Crossover. Two parameters are required: the current
                // individual and the array of parents
                cross.setCurrentSolution(population.get(i));
                child = cross.execute(parent, task.problem)[0];

                if (task.isStopCriterion())
                    break;
                task.eval(child);

                // Dominance test
                int result;
                result = dominance.compare(population.get(i), child);
                if (result == -1) { // Solution i dominates child
                    offspringPopulation.add(population.get(i));
                } else if (result == 1) { // child dominates
                    offspringPopulation.add(child);
                } else { // the two solutions are non-dominated
                    offspringPopulation.add(child);
                    offspringPopulation.add(population.get(i));
                }
            }

            // Ranking the offspring population
            Ranking<N> ranking = new Ranking<N>(offspringPopulation);

            int remain = populationSize;
            int index = 0;
            ParetoSolution<N> front = null;
            population.clear();

            // Obtain the next front
            front = ranking.getSubfront(index);

            while ((remain > 0) && (remain >= front.size())) {
                // Assign crowding distance to individuals
                distance.crowdingDistanceAssignment(front, numObj);
                // Add the individuals of this front
                for (int k = 0; k < front.size(); k++) {
                    population.add(front.get(k));
                }

                // Decrement remain
                remain = remain - front.size();

                // Obtain the next front
                index++;
                if (remain > 0) {
                    front = ranking.getSubfront(index);
                }
            }

            // remain is less than front(index).size, insert only the best one
            if (remain > 0) { // front contains individuals to insert
                while (front.size() > remain) {
                    distance.crowdingDistanceAssignment(front, numObj);
                    front.remove(front.indexWorst(new CrowdingComparator<>()));
                }
                for (int k = 0; k < front.size(); k++) {
                    population.add(front.get(k));
                }
                remain = 0;
            }
            task.incrementNumberOfIterations();
        }

        // Return the first non-dominated front
        Ranking<N> ranking = new Ranking<>(population);
        best = ranking.getSubfront(0);
    }
}
