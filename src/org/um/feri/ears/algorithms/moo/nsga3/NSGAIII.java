//Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
package org.um.feri.ears.algorithms.moo.nsga3;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.BinaryTournament2;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Ranking;


/**
 * Created by ajnebro on 30/10/14.
 * Modified by Juanjo on 13/11/14
 * <p>
 * This implementation is based on the code of Tsung-Che Chiang
 * http://web.ntnu.edu.tw/~tcchiang/publications/nsga3cpp/nsga3cpp.htm
 * <p>
 * Reference:
 * Deb, K., & Jain, H. (2014). An evolutionary many-objective optimization
 * algorithm using reference-point-based nondominated sorting approach, part I:
 * Solving problems with box constraints.
 * Evolutionary Computation, IEEE Transactions on, 18(4), 577-601.
 */
public class NSGAIII<N extends Number, P extends NumberProblem<N>> extends MOAlgorithm<N, NumberSolution<N>, P> {

    int populationSize = 100;

    double[][] lambda_; // reference points

    ParetoSolution<N> population;
    ParetoSolution<N> offspringPopulation;
    ParetoSolution<N> union;

    protected Vector<Integer> numberOfDivisions;
    protected List<ReferencePoint<N>> referencePoints;

    BinaryTournament2<N> bt2;
    SBXCrossover sbx;
    PolynomialMutation plm;

    CrossoverOperator<P, NumberSolution<N>> cross;
    MutationOperator<P, NumberSolution<N>> mut;


    public NSGAIII(CrossoverOperator<P, NumberSolution<N>> crossover, MutationOperator<P,NumberSolution<N>> mutation) {

        this.cross = crossover;
        this.mut = mutation;

        referencePoints = new Vector<>();
        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo(
                "NSGAIII", "Nondominated Sorting Genetic Algorithm III",
                "\\bibitem{Deb2014}\nK.~Deb, H.~Jain\n\\newblock An evolutionary many-objective optimization algorithm using reference-point-based nondominated sorting approach, part I: Solving problems with box constraints.\n\\newblock \\emph{IEEE Transactions on Evolutionary Computation}, 18(4):577--601, 2014."
        );
    }

    @Override
    protected void start() throws StopCriterionException {
        // Create the initial population
        NumberSolution<N> newSolution;
        for (int i = 0; i < populationSize; i++) {
            if (task.isStopCriterion())
                return;
            newSolution = new NumberSolution<N>(task.generateRandomEvaluatedSolution());
            // problem.evaluateConstraints(newSolution);
            population.add(newSolution);
        }

        ParetoSolution<N> offspringPopulation;
        ParetoSolution<N> matingPopulation;

        while (!task.isStopCriterion()) {
            matingPopulation = selection(population);
            offspringPopulation = reproduction(matingPopulation);
            population = replacement(population, offspringPopulation);
            task.incrementNumberOfIterations();
        }

        // Return the first non-dominated front
        Ranking<N> ranking = new Ranking<N>(population);
        best = ranking.getSubfront(0);
    }

    private ParetoSolution<N> selection(ParetoSolution<N> population) {

        ParetoSolution<N> matingPopulation = new ParetoSolution(population.size());
        for (int i = 0; i < population.size(); i++) {
            NumberSolution<N> solution = bt2.execute(population);
            matingPopulation.add(solution);
        }
        return matingPopulation;
    }

    protected ParetoSolution<N> reproduction(ParetoSolution<N> population) throws StopCriterionException {
        ParetoSolution<N> offspringPopulation = new ParetoSolution(population.size());
        for (int i = 0; i < population.size(); i += 2) {
            NumberSolution<N>[] parents = new NumberSolution[2];
            parents[0] = population.get(i);
            parents[1] = (population.get(Math.min(i + 1, population.size() - 1)));

            NumberSolution<N>[] offspring = cross.execute(parents, task.problem);

            mut.execute(offspring[0], task.problem);
            mut.execute(offspring[1], task.problem);

            if (task.isStopCriterion())
                break;
            task.eval(offspring[0]);
            if (task.isStopCriterion())
                break;
            task.eval(offspring[1]);

            offspringPopulation.add(offspring[0]);
            offspringPopulation.add(offspring[1]);
        }
        return offspringPopulation;
    }

    protected ParetoSolution<N> replacement(ParetoSolution<N> population, ParetoSolution<N> offspringPopulation) {

        ParetoSolution<N> jointPopulation = new ParetoSolution(population.getCapacity() + offspringPopulation.getCapacity());
        jointPopulation.addAll(population);
        jointPopulation.addAll(offspringPopulation);

        Ranking<N> ranking = new Ranking<N>(jointPopulation);


        List<NumberSolution<N>> pop = new ArrayList<>();
        List<List<NumberSolution<N>>> fronts = new ArrayList<>();
        int rankingIndex = 0;
        int candidateSolutions = 0;
        while (candidateSolutions < populationSize) {
            fronts.add(ranking.getSubfront(rankingIndex).solutions);
            candidateSolutions += ranking.getSubfront(rankingIndex).size();
            if ((pop.size() + ranking.getSubfront(rankingIndex).size()) <= populationSize)
                addRankedSolutionsToPopulation(ranking, rankingIndex, pop);
            rankingIndex++;
        }

        // A copy of the reference list should be used as parameter of the environmental selection
        EnvironmentalSelection<N> selection = new EnvironmentalSelection(fronts, populationSize, getReferencePointsCopy(), numObj);

        pop = selection.execute(pop);

        return new ParetoSolution(pop);
    }

    private void addRankedSolutionsToPopulation(Ranking<N> ranking, int rank, List<NumberSolution<N>> population) {
        List<NumberSolution<N>> front;

        front = ranking.getSubfront(rank).solutions;

        for (int i = 0; i < front.size(); i++) {
            population.add(front.get(i));
        }
    }

    private List<ReferencePoint<N>> getReferencePointsCopy() {
        List<ReferencePoint<N>> copy = new ArrayList<>();
        for (ReferencePoint<N> r : this.referencePoints) {
            copy.add(new ReferencePoint(r));
        }
        return copy;
    }

    @Override
    protected void init() {

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

        bt2 = new BinaryTournament2<N>();
        sbx = new SBXCrossover(0.9, 20.0);
        plm = new PolynomialMutation(1.0 / task.problem.getNumberOfDimensions(), 20.0);
        referencePoints.clear();
        switch (numObj) {
            case 2:
                numberOfDivisions = new Vector<>(1);
                numberOfDivisions.add(99);
                break;
            case 3:
                numberOfDivisions = new Vector<>(1);
                numberOfDivisions.add(12);
                break;
            case 5:
                numberOfDivisions = new Vector<>(1);
                numberOfDivisions.add(6);
                break;
            case 8:
                numberOfDivisions = new Vector<>(3);
                numberOfDivisions.add(2);
                break;
            case 10:
                numberOfDivisions = new Vector<>(3);
                numberOfDivisions.add(2);
                break;
            case 15:
                numberOfDivisions = new Vector<>(2);
                numberOfDivisions.add(1);
                break;

            default:
                numberOfDivisions = new Vector<>(1);
                numberOfDivisions.add(12);
                break;
        }

        (new ReferencePoint()).generateReferencePoints(referencePoints, numObj, numberOfDivisions);
        System.out.println(populationSize);
        populationSize = referencePoints.size();
        while (populationSize % 4 > 0) {
            populationSize++;
        }

        population = new ParetoSolution(populationSize);
    }


    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
