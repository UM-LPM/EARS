//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.spea2;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.BinaryTournament2;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Ranking;


public class SPEA2<T extends MOTask, Type extends Number> extends MOAlgorithm<T, Type> {

    int populationSize;
    int archiveSize = 100;
    ParetoSolution<Type> population;
    ParetoSolution<Type> archive;

    CrossoverOperator<Type, T, MOSolutionBase<Type>> cross;
    MutationOperator<Type, T, MOSolutionBase<Type>> mut;

    public int tournamentRounds = 1;

    public SPEA2(CrossoverOperator crossover, MutationOperator mutation, int populationSize, int archiveSize) {
        this.populationSize = populationSize;
        this.archiveSize = archiveSize;

        this.cross = crossover;
        this.mut = mutation;

        au = new Author("miha", "miha.ravber at gamil.com");
        ai = new AlgorithmInfo(
                "SPEA2", "Strength Pareto Evolutionary Algorithm 2",
                "\\bibitem{Zitzler2002}\nE.~Zitzler,M.~Laumanns,L.~Thiele\n\\newblock SPEA2: Improving the Strength Pareto Evolutionary Algorithm for Multiobjective Optimization.\n\\newblock \\emph{EUROGEN 2001. Evolutionary Methods for Design, Optimization and Control with Applications to Industrial Problems}, 95--100, 2002."
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

        //archiveSize = populationSize;

        population = new ParetoSolution<Type>(populationSize);
        archive = new ParetoSolution<Type>(archiveSize);
    }

    @Override
    protected void start() throws StopCriterionException {

        ParetoSolution<Type> offspringPopulation;

        BinaryTournament2<Type> bt2 = new BinaryTournament2<Type>();

        // -> Create the initial solutionSet
        for (int i = 0; i < populationSize; i++) {
            if (task.isStopCriterion())
                return;
            MOSolutionBase<Type> newSolution = new MOSolutionBase<Type>(task.getRandomMOSolution());
            // problem.evaluateConstraints(newSolution);;
            population.add(newSolution);
        }

        while (!task.isStopCriterion()) {
            ParetoSolution<Type> union = ((ParetoSolution<Type>) population).union(archive);
            Spea2fitness spea = new Spea2fitness(union);
            spea.fitnessAssign();
            archive = spea.environmentalSelection(archiveSize);
            // Create a new offspringPopulation
            offspringPopulation = new ParetoSolution<Type>(populationSize);
            MOSolutionBase<Type>[] parents = new MOSolutionBase[2];
            while (offspringPopulation.size() < populationSize) {
                int j = 0;
                do {
                    j++;
                    parents[0] = bt2.execute(archive);
                } while (j < tournamentRounds);
                int k = 0;
                do {
                    k++;
                    parents[1] = bt2.execute(archive);
                } while (k < tournamentRounds);

                // make the crossover
                MOSolutionBase<Type>[] offSpring = cross.execute(parents, task);
                mut.execute(offSpring[0], task);
                if (task.isStopCriterion())
                    break;
                task.eval(offSpring[0]);
                // problem.evaluateConstraints(offSpring[0]);
                offspringPopulation.add(offSpring[0]);
            }
            // End Create a offSpring solutionSet
            population = offspringPopulation;
            task.incrementNumberOfIterations();
        }

        Ranking<Type> ranking = new Ranking<Type>(archive);
        best = ranking.getSubfront(0);
    }
}
