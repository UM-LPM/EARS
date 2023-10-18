package org.um.feri.ears.algorithms.so.sa;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

// source: https://uk.mathworks.com/matlabcentral/fileexchange/53149-real-coded-simulated-annealing-sa

public class SimulatedAnnealing extends NumberAlgorithm {

    @AlgorithmParameter(name = "mutation rate")
    private double mu = 0.5;
    @AlgorithmParameter(name = "temperature", description = "initial and final temperature")
    public static double T = 0.1;
    @AlgorithmParameter(name = "alpha", description = "temperature Reduction Rate")
    private static final double ALPHA = 0.99;
    private int subIterations = 20;

    private NumberSolution<Double> globalBest, currentBest;
    private double[] sigma;

    public SimulatedAnnealing() {
        super();

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("SA", "Simulated Annealing",
                "@article{kirkpatrick1983optimization,\n" +
                        "  title={Optimization by simulated annealing},\n" +
                        "  author={Kirkpatrick, Scott and Gelatt, C Daniel and Vecchi, Mario P},\n" +
                        "  journal={science},\n" +
                        "  volume={220},\n" +
                        "  number={4598},\n" +
                        "  pages={671--680},\n" +
                        "  year={1983},\n" +
                        "  publisher={American association for the advancement of science}\n" +
                        "}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        sigma = task.problem.getInterval();
        for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
            sigma[i] *= 0.1;
        }

        globalBest = task.getRandomEvaluatedSolution();
        currentBest = new NumberSolution<>(globalBest);

        while (!task.isStopCriterion()) {

            for (int i = 0; i < subIterations; i++) {

                if (task.isStopCriterion())
                    break;
                // create a neighbor by mutating the current best solution
                NumberSolution<Double> neighbor = createNeighbor(currentBest);

                if (task.problem.isFirstBetter(neighbor, currentBest)) {
                    currentBest = neighbor;
                    if (task.problem.isFirstBetter(currentBest, globalBest))
                        globalBest = currentBest;
                } else {
                    double DELTA = (currentBest.getEval() - neighbor.getEval()) / (currentBest.getEval() + 0.000000001);
                    double P = Math.exp(DELTA / T);

                    if (RNG.nextDouble() <= P)
                        currentBest = neighbor;
                }
            }
            // Decreases T, cooling phase
            T *= ALPHA;
            for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                sigma[i] *= 0.98;
            }
        }
        return globalBest;
    }

    private NumberSolution<Double> createNeighbor(NumberSolution<Double> currentBest) throws StopCriterionException {

        double[] currentVariables = Util.toDoubleArray(currentBest.getVariables());
        double[] x = new double[task.problem.getNumberOfDimensions()];
        for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
            if (RNG.nextDouble() <= mu) {
                x[i] = currentVariables[i] + sigma[i] * RNG.nextGaussian();
            } else
                x[i] = currentVariables[i];
        }
        task.problem.setFeasible(x);

        NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(x));
        task.eval(newSolution);

        return newSolution;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
