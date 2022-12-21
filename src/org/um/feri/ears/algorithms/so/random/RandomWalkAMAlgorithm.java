package org.um.feri.ears.algorithms.so.random;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class RandomWalkAMAlgorithm extends Algorithm {
    private NumberSolution<Double> bestSolution;
    private Task task;

    public RandomWalkAMAlgorithm() {
        this.debug = false;
        ai = new AlgorithmInfo("RWAM", "Random Walk Arithmetic", "");
        au = new Author("matej", "matej.crepinsek@um.si");
    }


    private double[] xArithmeticMeanOf(double[] x, double[] y) {
        double[] am = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            am[i] = (x[i] + y[i]) * 0.5;
        }
        return am;
    }

    private double[] xInSameDirection(double[] old, double[] newX) {
        double[] am = new double[old.length];
        for (int i = 0; i < old.length; i++) {
            am[i] = task.setFeasible(newX[i] + (newX[i] - old[i]), i); // if out of
            // range
        }
        return am;
    }

    @Override
    public NumberSolution<Double> execute(Task taskProblem) throws StopCriterionException {
        NumberSolution<Double> currentSolution, iAritmetic, iExtend;
        task = taskProblem;
        bestSolution = taskProblem.getRandomEvaluatedSolution();
        if (debug)
            System.out.println(taskProblem.getNumberOfEvaluations() + " " + bestSolution);
        while (!taskProblem.isStopCriterion()) {
            currentSolution = taskProblem.getRandomEvaluatedSolution();
            if (taskProblem.isFirstBetter(currentSolution, bestSolution)) {
                if (!taskProblem.isStopCriterion()) { // try also arithmetic mean
                    iAritmetic = taskProblem.eval(xArithmeticMeanOf(Util.toDoubleArray(bestSolution.getVariables()), Util.toDoubleArray(currentSolution.getVariables())));
                    if (taskProblem.isFirstBetter(iAritmetic, currentSolution)) {
                        currentSolution = iAritmetic; // even better
                    } else {
                        if (!taskProblem.isStopCriterion()) { // try also extend
                            iExtend = taskProblem.eval(xInSameDirection(Util.toDoubleArray(bestSolution.getVariables()), Util.toDoubleArray(currentSolution.getVariables())));
                            if (taskProblem.isFirstBetter(iExtend, currentSolution)) {
                                currentSolution = iExtend; // even better
                            }
                        }
                    }
                }
                bestSolution = currentSolution;
                if (debug)
                    System.out.println(taskProblem.getNumberOfEvaluations() + " " + bestSolution);
            }
            task.incrementNumberOfIterations();
        }
        return bestSolution;
    }


    @Override
    public void resetToDefaultsBeforeNewRun() {
        bestSolution = null;
    }

}
