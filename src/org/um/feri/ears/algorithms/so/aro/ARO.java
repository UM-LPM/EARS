package org.um.feri.ears.algorithms.so.aro;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.PredefinedRandom;
import org.um.feri.ears.util.random.RNG;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ARO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private final boolean usePredefinedRandom = false;
    PredefinedRandom random;

    public ARO() {
        this(50);
    }

    public ARO(int popSize) {
        this.popSize = popSize;

        au = new Author("Aleksandra", "aleksandra.bojic@student.um.si");
        ai = new AlgorithmInfo("ARO", "Artificial Rabbit Optimization", "");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        DoubleProblem problem = task.problem;
        int dim = problem.getNumberOfDimensions();
        double[] low = problem.getLowerLimit().stream().mapToDouble(Double::doubleValue).toArray();
        double[] up = problem.getUpperLimit().stream().mapToDouble(Double::doubleValue).toArray();

        int maxIterations = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIterations = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIterations = (task.getMaxEvaluations() - popSize) / popSize;
        }

        double[][] popPos = new double[popSize][dim];
        double[] popFit = new double[popSize];
        double[] bestX = new double[dim];
        double bestF = Double.POSITIVE_INFINITY;

        // Initialize population
        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < dim; j++) {
                popPos[i][j] = RNG.nextDouble() * (up[j] - low[j]) + low[j];
            }

            List<Double> vars = Arrays.stream(popPos[i]).boxed().toList();
            NumberSolution<Double> sol = new NumberSolution<>(vars, 0.0);

            if (task.isStopCriterion())
                break;

            task.eval(sol);
            popFit[i] = sol.getEval();

            NumberSolution<Double> currentSol = new NumberSolution<>(Arrays.stream(popPos[i]).boxed().toList(), popFit[i]);
            NumberSolution<Double> bestFSol = new NumberSolution<>(Arrays.stream(bestX).boxed().toList(), bestF);
            if (task.problem.isFirstBetter(currentSol, bestFSol)) {
                bestF = popFit[i];
                System.arraycopy(popPos[i], 0, bestX, 0, dim);
            }
        }

        while (!task.isStopCriterion()) {
            int[][] direct1 = new int[popSize][dim];
            double[][] direct2 = new double[popSize][dim];
            double theta = 2 * (1.0 - (double) (task.getNumberOfIterations() + 1) / maxIterations);

            for (int i = 0; i < popSize; i++) {
                double randomNum = RNG.nextDouble();
                double L = (Math.exp(1) -
                        Math.exp(Math.pow(((double) task.getNumberOfIterations()) / maxIterations, 2))) *
                        Math.sin(2 * Math.PI * randomNum); // Eq. 3

                int rd = (int) Math.ceil(RNG.nextDouble() * dim);

                double[] c = new double[dim];
                int[] prem = RNG.randomPermutation(dim);

                for (int k = 0; k < rd; k++) {
                    direct1[i][prem[k]] = 1;
                }
                for (int j = 0; j < dim; j++) {
                    c[j] = direct1[i][j];
                }

                double[] R = new double[dim];
                for (int j = 0; j < dim; j++) {
                    R[j] = L * c[j]; // Eq. 2
                }

                double A = 2 * Math.log(1 / RNG.nextDouble()) * theta;
                double[] newPopPos = new double[dim];

                if (A > 1) {
                    // Detour foraging (Eq.1)
                    int[] K = new int[popSize - 1];
                    for (int j = 0, index = 0; j < popSize; j++) {
                        if (j != i) {
                            K[index++] = j;
                        }
                    }
                    int randInd = K[RNG.nextInt(0, popSize - 1)];

                    double randomGaussian = RNG.nextGaussian();
                    randomNum = RNG.nextDouble();

                    for (int j = 0; j < dim; j++) {
                        newPopPos[j] = popPos[randInd][j] + R[j] * (popPos[i][j] - popPos[randInd][j]) +
                                Math.round(0.5 * (0.05 + randomNum)) * randomGaussian;
                    }

                } else {
                    // Random hiding (Eq.11-13)
                    direct2[i][(int) Math.ceil(RNG.nextDouble() * dim) - 1] = 1;
                    double[] gr = Arrays.copyOf(direct2[i], dim); // Eq. 12

                    double H = ((maxIterations - (task.getNumberOfIterations() + 1) + 1) / (double) maxIterations) * RNG.nextGaussian(); // Eq. 8

                    double[] b = new double[dim];
                    randomNum = RNG.nextDouble();
                    for (int j = 0; j < dim; j++) {
                        b[j] = popPos[i][j] + H * gr[j] * popPos[i][j]; // Eq. 13
                        newPopPos[j] = popPos[i][j] + R[j] * (randomNum * b[j] - popPos[i][j]); // Eq. 11
                    }
                }

                if (!task.problem.isFeasible(Arrays.stream(newPopPos).boxed().toList())) {
                    task.problem.makeFeasible(newPopPos);
                }

                List<Double> newPopPosVars = Arrays.stream(newPopPos).boxed().toList();
                NumberSolution<Double> newPopPosSol = new NumberSolution<>(newPopPosVars, 0.0);

                if (task.isStopCriterion()) {
                    break;
                }
                task.eval(newPopPosSol);
                double newPopFit = newPopPosSol.getEval();

                NumberSolution<Double> solution1 = new NumberSolution<>(Arrays.stream(newPopPos).boxed().toList(), newPopFit);
                NumberSolution<Double> solution2 = new NumberSolution<>(Arrays.stream(popPos[i]).boxed().toList(), popFit[i]);
                if (task.problem.isFirstBetter(solution1, solution2)) {
                    popFit[i] = newPopFit;
                    popPos[i] = newPopPos;
                    System.arraycopy(newPopPos, 0, popPos[i], 0, dim);
                }

                NumberSolution<Double> bestSol = new NumberSolution<>(Arrays.stream(bestX).boxed().toList(), bestF);
                if (task.problem.isFirstBetter(newPopPosSol, bestSol)) {
                    bestF = newPopFit;
                    System.arraycopy(newPopPos, 0, bestX, 0, dim);
                }
            }
            task.incrementNumberOfIterations();
        }

        List<Double> variableList = Arrays.stream(bestX).boxed().toList();
        NumberSolution<Double> solution = new NumberSolution<>(variableList, bestF);

        return solution;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
