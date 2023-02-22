//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.pesa2;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PESA2Selection;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class PESAII<N extends Number, P extends NumberProblem<N>, T extends Task<NumberSolution<N>,P>> extends MOAlgorithm<T, N> {

    int populationSize = 100;
    int archiveSize = 100;
    int bisections = 5;
    ParetoSolution<N> population;
    AdaptiveGridArchive<N> archive;

    CrossoverOperator<N, P, NumberSolution<N>> cross;
    MutationOperator<P, NumberSolution<N>> mut;


    public PESAII(CrossoverOperator<N, P, NumberSolution<N>> crossover, MutationOperator<P, NumberSolution<N>> mutation, int populationSize, int archiveSize) {
        this.populationSize = populationSize;
        this.archiveSize = archiveSize;

        this.cross = crossover;
        this.mut = mutation;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo(
                "PESAII", "Pareto Envelope-Based Selection Algorithm",
                "\\bibitem{corne2001}\nD.W.~Corne,N.R.~Jerram,J.D.~Knowles,M.J.~Oates\n\\newblock PESA-II: Region-based Selection in Evolutionary Multiobjective Optimization.\n\\newblock \\emph{Proceedings of the Genetic and Evolutionary Computation Conference (GECCO-2001)}, 283--290, 2001."
        );
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
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

        archive = new AdaptiveGridArchive<N>(archiveSize, bisections, numObj);
        population = new ParetoSolution<N>(populationSize);
    }

    @Override
    protected void start() throws StopCriterionException {

        PESA2Selection<N> selection = new PESA2Selection<N>();

        // Create the initial individual and evaluate it and his constraints
        for (int i = 0; i < populationSize; i++) {
            if (task.isStopCriterion())
                return;
            NumberSolution<N> solution = new NumberSolution<N>(task.getRandomEvaluatedSolution());
            // problem.evaluateConstraints(solution);
            population.add(solution);
        }

        // Incorporate non-dominated solution to the archive
        for (int i = 0; i < population.size(); i++) {
            archive.add(population.get(i)); // Only non dominated are accepted by the archive
        }

        // Clear the init solutionSet
        population.clear();

        // Iterations....
        NumberSolution<N>[] parents = new NumberSolution[2];

        do {
            // -> Create the offSpring solutionSet
            while (population.size() < populationSize) {
                parents[0] = selection.execute(archive);
                parents[1] = selection.execute(archive);

                NumberSolution<N>[] offSpring = cross.execute(parents, task.problem);
                mut.execute(offSpring[0], task.problem);
                if (task.isStopCriterion())
                    break;
                task.eval(offSpring[0]);
                // problem.evaluateConstraints(offSpring[0]);
                population.add(offSpring[0]);
            }

            for (int i = 0; i < population.size(); i++)
                archive.add(population.get(i));

            // Clear the solutionSet
            population.clear();
            task.incrementNumberOfIterations();
        } while (!task.isStopCriterion());

        best = archive;
    }
}
