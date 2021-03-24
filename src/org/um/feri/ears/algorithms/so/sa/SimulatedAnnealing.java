package org.um.feri.ears.algorithms.so.sa;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

// source: https://uk.mathworks.com/matlabcentral/fileexchange/53149-real-coded-simulated-annealing-sa

public class SimulatedAnnealing extends Algorithm {

    @AlgorithmParameter(name = "mutation rate")
    private double mu = 0.5;
    @AlgorithmParameter(name = "temperature", description = "initial and final temperature")
    public static double T = 0.1;
    @AlgorithmParameter(name = "alpha", description = "temperature Reduction Rate")
    private static final double ALPHA = 0.99;
    private int subIterations = 20;

    private DoubleSolution globalBest, currentBest;
    private Task task;
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
    public DoubleSolution execute(Task taskProblem) throws StopCriterionException {
        task = taskProblem;
        sigma = task.getInterval();
        for (int i = 0; i < task.getNumberOfDimensions(); i++) {
            sigma[i] *= 0.1;
        }

        globalBest = task.getRandomEvaluatedSolution();
        currentBest = new DoubleSolution(globalBest);

        while (!task.isStopCriterion()) {

            for (int i = 0; i < subIterations; i++) {

                if (task.isStopCriterion())
                    break;
                // create a neighbor by mutating the current best solution
                DoubleSolution neighbor = neighbor(currentBest);

                if (task.isFirstBetter(neighbor, currentBest)) {
                    currentBest = neighbor;
                    if (task.isFirstBetter(currentBest, globalBest))
                        globalBest = currentBest;
                } else {
                    double DELTA = (currentBest.getEval() - neighbor.getEval()) / (currentBest.getEval() + 0.000000001);
                    double P = Math.exp(DELTA / T);

                    if (Util.nextDouble() <= P)
                        currentBest = neighbor;
                }
            }
            // Decreases T, cooling phase
            T *= ALPHA;
            for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                sigma[i] *= 0.98;
            }
        }
        return globalBest;
    }

    private DoubleSolution neighbor(DoubleSolution currentBest) throws StopCriterionException {

        double[] currentVariables = currentBest.getDoubleVariables();
        double[] x = new double[task.getNumberOfDimensions()];
        for (int i = 0; i < task.getNumberOfDimensions(); i++) {
            if (Util.nextDouble() <= mu) {
                x[i] = currentVariables[i] + sigma[i] * Util.rnd.nextGaussian();
            } else
                x[i] = currentVariables[i];
        }
        x = task.setFeasible(x);
        return task.eval(x);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
