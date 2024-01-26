//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.nsga2;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.BinaryTournament2;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.CrowdingComparator;
import org.um.feri.ears.util.Distance;
import org.um.feri.ears.util.Ranking;


/**
 * Implementation of NSGA-II.
 * This implementation of NSGA-II makes use of a QualityIndicator object
 * to obtained the convergence speed of the algorithm. This version is used
 * in the paper:
 * A.J. Nebro, J.J. Durillo, C.A. Coello Coello, F. Luna, E. Alba
 * "A Study of Convergence Speed in Multi-Objective Metaheuristics."
 * To be presented in: PPSN'08. Dortmund. September 2008.
 */
public class NSGAII<N extends Number, P extends NumberProblem<N>> extends MOAlgorithm<N, NumberSolution<N>, P> {

    int populationSize = 100;

    ParetoSolution<N> population;
    ParetoSolution<N> offspringPopulation;
    ParetoSolution<N> union;


    CrossoverOperator<P, NumberSolution<N>> cross;
    MutationOperator<P, NumberSolution<N>> mut;

    public NSGAII(CrossoverOperator<P, NumberSolution<N>> crossover, MutationOperator<P, NumberSolution<N>> mutation, int populationSize) {

        this.cross = crossover;
        this.mut = mutation;
        this.populationSize = populationSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo(
                "NSGAII", "Nondominated Sorting Genetic Algorithm II",
                "\\bibitem{Deb2002}\nK.~Deb, S.~Agrawal, A.~Pratap, T.~Meyarivan\n\\newblock A fast and elitist multiobjective genetic algorithm: {NSGA-II}.\n\\newblock \\emph{IEEE Transactions on Evolutionary Computation}, 6(2):182--197, 2002."
        );
    }

    @Override
    protected void start() throws StopCriterionException {
        Distance<N> distance = new Distance<N>();
        BinaryTournament2<N> bt2 = new BinaryTournament2<N>();

        // Create the initial population
        NumberSolution<N> newSolution;
        for (int i = 0; i < populationSize; i++) {
            if (task.isStopCriterion())
                return;
            newSolution = task.getRandomEvaluatedSolution();
            // problem.evaluateConstraints(newSolution);
            population.add(newSolution);
        }

        // Generations
        while (!task.isStopCriterion()) {
            // Create the offSpring solutionSet
            offspringPopulation = new ParetoSolution(populationSize);
            NumberSolution<N>[] parents = new NumberSolution[2];

            for (int i = 0; i < (populationSize / 2); i++) {
                if (!task.isStopCriterion()) {
                    // obtain parents
                    parents[0] = bt2.execute(population);
                    parents[1] = bt2.execute(population);
                    NumberSolution<N>[] offSpring = cross.execute(parents, task.problem);

                    mut.execute(offSpring[0], task.problem);
                    mut.execute(offSpring[1], task.problem);
                    if (task.isStopCriterion())
                        break;

                    task.problem.makeFeasible(offSpring[0]);
                    task.eval(offSpring[0]);
                    offspringPopulation.add(offSpring[0]);
                    // problem.evaluateConstraints(offSpring[0]);
                    if (task.isStopCriterion())
                        break;
                    task.problem.makeFeasible(offSpring[1]);
                    task.eval(offSpring[1]);
                    // problem.evaluateConstraints(offSpring[1]);
                    offspringPopulation.add(offSpring[1]);
                }
            }

            // Create the solutionSet union of solutionSet and offSpring
            union = population.union(offspringPopulation);

            // Ranking the union
            Ranking<N> ranking = new Ranking<N>(union);

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

            // Remain is less than front(index).size, insert only the best one
            if (remain > 0) { // front contains individuals to insert
                distance.crowdingDistanceAssignment(front, numObj);
                front.sort(new CrowdingComparator<>());
                for (int k = 0; k < remain; k++) {
                    population.add(front.get(k));
                }
                remain = 0;
            }
            task.incrementNumberOfIterations();
        }

        Ranking<N> ranking = new Ranking<N>(population);
        best = ranking.getSubfront(0);
    }

    @Override
    protected void init() {

        if (optimalParam) {
            switch (numObj) {
                case 1: {
                    populationSize = 100;
                    break;
                }
                case 2: {
                    populationSize = 100;
                    break;
                }
                case 3: {
                    populationSize = 300;
                    break;
                }
                default: {
                    populationSize = 500;
                    break;
                }
            }
        }

        population = new ParetoSolution<N>(populationSize);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }

}