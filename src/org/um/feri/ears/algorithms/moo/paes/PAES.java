//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.paes;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.pesa2.AdaptiveGridArchive;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.comparator.SolutionDominanceComparator;

public class PAES<N extends Number, P extends NumberProblem<N>> extends MOAlgorithm<N, NumberSolution<N>, P> {

    AdaptiveGridArchive<N> archive;
    int archiveSize = 100;
    int bisections = 5;

    MutationOperator<P, NumberSolution<N>> mut;

    public PAES(MutationOperator mutation, int populationSize) {

        this.archiveSize = populationSize;
        this.mut = mutation;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo(
                "PAES", "Pareto Archived Evolution Strategy",
                "\\bibitem{knowles1999}\nJ.~Knowles,D.W.~Corne\n\\newblock The Pareto Archived Evolution Strategy: A New Baseline Algorithm for Pareto Multiobjective Optimisation.\n\\newblock \\emph{Proceedings of the Congress of Evolutionary Computation}, 98--105, 1999."
        );
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }

    @Override
    protected void init() {
        archive = new AdaptiveGridArchive<>(archiveSize, bisections, numObj);
    }

    public void start() throws StopCriterionException {

        SolutionDominanceComparator dominance = new SolutionDominanceComparator();

        if (task.isStopCriterion())
            return;
        NumberSolution<N> solution = new NumberSolution<N>(task.generateRandomEvaluatedSolution());
        // problem.evaluateConstraints(solution);

        archive.add(new NumberSolution<N>(solution));

        do {
            // Create the mutate one
            NumberSolution<N> mutatedIndividual = new NumberSolution<N>(solution);
            mut.execute(mutatedIndividual, task.problem);

            if (task.isStopCriterion())
                break;
            task.eval(mutatedIndividual);
            // problem.evaluateConstraints(mutatedIndividual);

            // Check dominance
            int flag = dominance.compare(solution, mutatedIndividual);

            if (flag == 1) { // If mutate solution dominate
                solution = new NumberSolution<N>(mutatedIndividual);
                archive.add(mutatedIndividual);
            } else if (flag == 0) { // If none dominate the other
                if (archive.add(mutatedIndividual)) {
                    solution = test(solution, mutatedIndividual, archive);
                }
            }
            /*
             * if ((evaluations % 100) == 0) {
             * archive.printObjectivesToFile("FUN"+evaluations) ;
             * archive.printVariablesToFile("VAR"+evaluations) ;
             * archive.printObjectivesOfValidSolutionsToFile("FUNV"+evaluations)
             * ; }
             */
            task.incrementNumberOfIterations();
        } while (!task.isStopCriterion());

        best = archive;
    }

    public NumberSolution<N> test(NumberSolution<N> solution,
                                  NumberSolution<N> mutatedSolution, AdaptiveGridArchive<N> archive) {

        int originalLocation = archive.getGrid().location(solution);
        int mutatedLocation = archive.getGrid().location(mutatedSolution);

        if (originalLocation == -1) {
            return new NumberSolution<N>(mutatedSolution);
        }

        if (mutatedLocation == -1) {
            return new NumberSolution<N>(solution);
        }

        if (archive.getGrid().getLocationDensity(mutatedLocation) < archive
                .getGrid().getLocationDensity(originalLocation)) {
            return new NumberSolution<N>(mutatedSolution);
        }

        return new NumberSolution<N>(solution);
    }

}
