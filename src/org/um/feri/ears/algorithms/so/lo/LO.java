package org.um.feri.ears.algorithms.so.lo;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class LO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private final int popSize;

    @AlgorithmParameter(name = "jumping rate min")
    private final double jumpingRateMin;

    @AlgorithmParameter(name = "jumping rate max")
    private final double jumpingRateMax;

    private ArrayList<NumberSolution<Double>> population;
    public NumberSolution<Double> bestSolution;

    public LO() {
        this(30);
    }

    public LO(int popSize) {
        super();
        this.popSize = popSize;
        this.jumpingRateMin = 0.1; //Parameters used in the paper: 0.5
        this.jumpingRateMax = 0.5; //Parameters used in the paper: 0.7

        au = new Author("David Jerman", "");
        ai = new AlgorithmInfo("LO", "Lemurs Optimizer",
                "@Article{app121910057,"
                        + "  AUTHOR = {Abasi, Ammar Kamal and Makhadmeh, Sharif Naser and Al-Betar, Mohammed Azmi and Alomari, Osama Ahmad and Awadallah, Mohammed A. and Alyasseri, Zaid Abdi Alkareem and Doush, Iyad Abu and Elnagar, Ashraf and Alkhammash, Eman H. and Hadjouni, Myriam},"
                        + "  TITLE = {Lemurs Optimizer: A New Metaheuristic Algorithm for Global Optimization},"
                        + "  JOURNAL = {Applied Sciences},"
                        + "  VOLUME = {12},"
                        + "  YEAR = {2022},"
                        + "  NUMBER = {19},"
                        + "  ARTICLE-NUMBER = {10057},"
                        + "  URL = {https://www.mdpi.com/2076-3417/12/19/10057},"
                        + "  ISSN = {2076-3417},"
                        + "  DOI = {10.3390/app121910057}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        int maxIt = 10000;

        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        } else if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        while (!task.isStopCriterion()) {

            // Compute the jumping rate
            double currentIter = task.getNumberOfIterations();
            double jumping_rate = jumpingRateMax - currentIter * ((jumpingRateMax - jumpingRateMin) / maxIt);

            // Sort the population based on fitness
            double[] fitness = new double[popSize];
            Integer[] sortedIndices = new Integer[popSize];

            for (int i = 0; i < popSize; i++) {
                fitness[i] = calculateFitness(population.get(i).getObjective(0));
                sortedIndices[i] = i;
            }

            Arrays.sort(sortedIndices, Comparator.comparingDouble(i -> fitness[i]));

            for (int i = 0; i < popSize; i++) {
                // Find rank of current solution
                /// current_solution = find(sorted_indexes == i);
                int currentRank = -1;
                for (int k = 0; k < popSize; k++) {
                    if (sortedIndices[k] == i) {
                        currentRank = k;
                        break;
                    }
                }

                // Determine "Near Solution" (neighbor in rank)
                /// near_solution_postion = current_solution - 1
                int nearSolutionRank = currentRank - 1;
                if (nearSolutionRank < 0) nearSolutionRank = 0; // Boundary check: if 0, stay 0

                NumberSolution<Double> currentSol = population.get(i);
                NumberSolution<Double> nearSol = population.get(sortedIndices[nearSolutionRank]);

                double[] newPosition = new double[task.problem.getNumberOfDimensions()];

                // Dimension Loop (jumping logic)
                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                    double r = RNG.nextDouble(); // rand()

                    double currentVal = currentSol.getValue(j);

                    if (r < jumping_rate) {
                        // Dance Hub (Local Search)
                        double nearVal = nearSol.getValue(j);
                        newPosition[j] = currentVal + Math.abs(currentVal - nearVal) * (RNG.nextDouble() - 0.5) * 2;
                    } else {
                        // Leap Up (Global Search)
                        double bestVal = bestSolution.getValue(j);
                        newPosition[j] = currentVal + Math.abs(currentVal - bestVal) * (RNG.nextDouble() - 0.5) * 2;
                    }
                }

                task.problem.makeFeasible(newPosition);
                if (task.isStopCriterion()) break;

                NumberSolution<Double> newSol = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newSol);

                if (task.problem.isFirstBetter(newSol, currentSol)) {
                    population.set(i, newSol);
                    if (task.problem.isFirstBetter(newSol, bestSolution)) {
                        bestSolution = newSol;
                    }
                }
            }
            task.incrementNumberOfIterations();
        }
        return bestSolution;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            NumberSolution<Double> sol = task.generateRandomEvaluatedSolution();
            population.add(sol);

            if (bestSolution == null || task.problem.isFirstBetter(sol, bestSolution)) {
                bestSolution = sol;
            }
        }
    }

    /**
     * This transforms the objective value into a positive fitness score for ranking.
     */
    private double calculateFitness(double objVal) {
        if (objVal >= 0) {
            return 1.0 / (objVal + 1.0);
        } else {
            return 1.0 + Math.abs(objVal);
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        population = null;
        bestSolution = null;
    }
}