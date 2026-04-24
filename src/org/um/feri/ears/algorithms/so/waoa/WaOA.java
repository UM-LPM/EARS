package org.um.feri.ears.algorithms.so.waoa;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class WaOA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> bestSolution;

    public WaOA() {
        this(30);
    }

    public WaOA(int popSize) {
        super();
        this.popSize = popSize;
        au = new Author("Tim", "tim.trojner@student.um.si"); // Placeholder, user can update
        ai = new AlgorithmInfo("WaOA", "Walrus Optimization Algorithm",
                "@article{trojovsky2023walrus,\n" +
                        "  title={Walrus Optimization Algorithm: A New Bio-Inspired Metaheuristic Algorithm},\n" +
                        "  author={Trojovský, Pavel and Dehghani, Mohammad},\n" +
                        "  journal={Mathematics},\n" +
                        "  volume={11},\n" +
                        "  number={10},\n" +
                        "  pages={2258},\n" +
                        "  year={2023},\n" +
                        "  publisher={MDPI}\n" +
                        "}");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task)
            throws StopCriterionException {
        this.task = task;
        this.bestSolution = null;
        initPopulation();

        while (!task.isStopCriterion()) {
            updateBest();

            int t = task.getNumberOfIterations() + 1;

            NumberSolution<Double> SW = bestSolution; // Strongest walrus

            for (int i = 0; i < popSize; i++) {
                NumberSolution<Double> currentAgent = population.get(i);
                int dimension = task.problem.getNumberOfDimensions();

                // PHASE 1: FEEDING STRATEGY (EXPLORATION)
                double I = Math.round(1 + RNG.nextDouble());

                double[] xP1 = new double[dimension];
                for (int d = 0; d < dimension; d++) {
                    xP1[d] = currentAgent.getValue(d) + RNG.nextDouble() * (SW.getValue(d) - I * currentAgent.getValue(d));
                }

                // Update position based on Eq (4)
                NumberSolution<Double> solP1 = new NumberSolution<>(Util.toDoubleArrayList(xP1));

                if (!task.problem.isFeasible(solP1)) {
                    task.problem.makeFeasible(solP1);
                }

                if (task.isStopCriterion())
                    break;
                task.eval(solP1);

                if (task.problem.isFirstBetter(solP1, currentAgent)) {
                    currentAgent = solP1;
                    population.set(i, currentAgent);
                }

                if (task.isStopCriterion())
                    break;

                // PHASE 2: MIGRATION
                I = Math.round(1 + RNG.nextDouble());

                // Select a random agent K different from i
                int kIndex = RNG.nextInt(popSize);
                while (kIndex == i) {
                    kIndex = RNG.nextInt(popSize);
                }
                NumberSolution<Double> xK = population.get(kIndex);

                double[] xP2 = new double[dimension];

                boolean kIsBetter = task.problem.isFirstBetter(xK, currentAgent);
                double rand2 = RNG.nextDouble();

                for (int d = 0; d < dimension; d++) {
                    if (kIsBetter) {
                        xP2[d] = currentAgent.getValue(d) + rand2 * (xK.getValue(d) - I * currentAgent.getValue(d));
                    } else {
                        xP2[d] = currentAgent.getValue(d) + rand2 * (currentAgent.getValue(d) - xK.getValue(d));
                    }
                }

                NumberSolution<Double> solP2 = new NumberSolution<>(Util.toDoubleArrayList(xP2));

                if (!task.problem.isFeasible(solP2)) {
                    task.problem.makeFeasible(solP2);
                }

                if (task.isStopCriterion())
                    break;
                task.eval(solP2);

                if (task.problem.isFirstBetter(solP2, currentAgent)) {
                    currentAgent = solP2;
                    population.set(i, currentAgent);
                }

                if (task.isStopCriterion())
                    break;

                // PHASE 3: ESCAPING AND FIGHTING AGAINST PREDATORS (EXPLOITATION)
                double[] xP3 = new double[dimension];

                double rand3 = RNG.nextDouble();
                for (int d = 0; d < dimension; d++) {
                    double lb = task.problem.getLowerLimit(d);
                    double ub = task.problem.getUpperLimit(d);

                    double loLocal = lb / t;
                    double hiLocal = ub / t;

                    xP3[d] = currentAgent.getValue(d) + loLocal + rand3 * (hiLocal - loLocal);

                    // Manual bound checking for local search intensity
                    if (xP3[d] < loLocal)
                        xP3[d] = loLocal;
                    if (xP3[d] > hiLocal)
                        xP3[d] = hiLocal;
                }

                NumberSolution<Double> solP3 = new NumberSolution<>(Util.toDoubleArrayList(xP3));

                if (!task.problem.isFeasible(solP3)) {
                    task.problem.makeFeasible(solP3);
                }

                if (task.isStopCriterion())
                    break;
                task.eval(solP3);

                if (task.problem.isFirstBetter(solP3, currentAgent)) {
                    currentAgent = solP3;
                    population.set(i, currentAgent);
                }
                if (task.isStopCriterion())
                    break;
            }

            //updateBest(); // not in original code, but could improve performance
            try {
                task.incrementNumberOfIterations();
            } catch (StopCriterionException e) {
                break;
            }
        }

        return bestSolution;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            NumberSolution<Double> sol = task.generateRandomEvaluatedSolution();
            population.add(sol);
        }
    }

    private void updateBest() {
        if (population.isEmpty())
            return;

        bestSolution = population.get(0);
        for (NumberSolution<Double> sol : population) {
            if (task.problem.isFirstBetter(sol, bestSolution)) {
                bestSolution = new NumberSolution<>(sol);
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
