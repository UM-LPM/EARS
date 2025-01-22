package org.um.feri.ears.algorithms.so.scso;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class SCSO extends NumberAlgorithm {
    @AlgorithmParameter(name = "population size")
    private final int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> bestSolution;

    public SCSO() {
        this(30);
    }

    public SCSO(int popSize) {
        this.popSize = popSize;

        au = new Author("tim", "tim.vehovar@student.um.si");
        ai = new AlgorithmInfo("SCSO", "Sand Cat Sawrm Optimizer",
                "@article{SCSO2023Amir,"
                        + "  title={Sand Cat swarm optimization: a nature‑inspired algorithm to solve\n" +
                        "global optimization problems},"
                        + "  Author={Amir Seyyedabbasi}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        initPopulation();
        int[] p = new int[360];
        for (int i = 0; i < p.length; i++) p[i] = i + 1; // 1 to 360

        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        while (!task.isStopCriterion()) {
            double S = 2.0; // Sensitivity range
            double rg = S - (S * task.getNumberOfIterations() / maxIt); // Decrease sensitivity range

            for (NumberSolution<Double> solution : population) {
                double ran = RNG.nextDouble();
                double r = ran * rg;
                double R = (2 * rg * RNG.nextDouble()) - rg;

                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {

                    double theta = rouletteWheelSelection(p) + 1;

                    if (-1 <= R && R <= 1) { // Transition phase 1
                        double randPosition = Math.abs(RNG.nextDouble() * bestSolution.getVariables().get(j) - solution.getVariables().get(j));
                        solution.getVariables().set(j, bestSolution.getVariables().get(j) - r * randPosition * Math.cos(theta));
                    } else { // Transition phase 2
                        double rand = RNG.nextDouble();
                        int cp = (int) Math.floor(popSize * rand);
                        NumberSolution<Double> candidatePosition = population.get(cp);
                        solution.getVariables().set(j, r * (candidatePosition.getVariables().get(j) - RNG.nextDouble() * solution.getVariables().get(j)));
                    }
                }
            }

            for (NumberSolution<Double> solution : population) {

                task.problem.makeFeasible(solution);

                if (task.isStopCriterion())
                    break;
                task.eval(solution);

                if (task.problem.isFirstBetter(solution, bestSolution)) {
                    bestSolution = solution.copy();
                }
            }
            task.incrementNumberOfIterations();
        }
        return bestSolution;
    }

    private double rouletteWheelSelection(int[] p) {
        double r = RNG.nextDouble();
        int s = 0;
        for (int P : p) {
            s += P;
        }
        double cumsum = 0;
        for (int i = 0; i < p.length; i++) {
            cumsum += (double) p[i] / s;
            if (r <= cumsum) {
                return i;
            }
        }
        return p.length - 1;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        bestSolution = task.generateRandomEvaluatedSolution();
        population.add(bestSolution.copy());

        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            NumberSolution<Double> newSolution = task.generateRandomEvaluatedSolution();
            if(task.problem.isFirstBetter(newSolution, bestSolution)) {
                bestSolution = newSolution.copy();
            }
            population.add(newSolution);
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {}
}
