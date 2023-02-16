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
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.util.comparator.DominanceComparator;

public class PAES<Type extends Number, P extends Problem<NumberSolution<Type>>, T extends MOTask<Type>> extends MOAlgorithm<P, T, Type> {

    AdaptiveGridArchive<Type> archive;
    int archiveSize = 100;
    int bisections = 5;

    MutationOperator<Type, T, NumberSolution<Type>> mut;

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

        DominanceComparator dominance = new DominanceComparator();

        if (task.isStopCriterion())
            return;
        NumberSolution<Type> solution = new NumberSolution<Type>(task.getRandomEvaluatedSolution());
        // problem.evaluateConstraints(solution);

        archive.add(new NumberSolution<Type>(solution));

        do {
            // Create the mutate one
            NumberSolution<Type> mutatedIndividual = new NumberSolution<Type>(solution);
            mut.execute(mutatedIndividual, task);

            if (task.isStopCriterion())
                break;
            task.eval(mutatedIndividual);
            // problem.evaluateConstraints(mutatedIndividual);

            // Check dominance
            int flag = dominance.compare(solution, mutatedIndividual);

            if (flag == 1) { // If mutate solution dominate
                solution = new NumberSolution<Type>(mutatedIndividual);
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

    public NumberSolution<Type> test(NumberSolution<Type> solution,
                                     NumberSolution<Type> mutatedSolution, AdaptiveGridArchive<Type> archive) {

        int originalLocation = archive.getGrid().location(solution);
        int mutatedLocation = archive.getGrid().location(mutatedSolution);

        if (originalLocation == -1) {
            return new NumberSolution<Type>(mutatedSolution);
        }

        if (mutatedLocation == -1) {
            return new NumberSolution<Type>(solution);
        }

        if (archive.getGrid().getLocationDensity(mutatedLocation) < archive
                .getGrid().getLocationDensity(originalLocation)) {
            return new NumberSolution<Type>(mutatedSolution);
        }

        return new NumberSolution<Type>(solution);
    }

}
