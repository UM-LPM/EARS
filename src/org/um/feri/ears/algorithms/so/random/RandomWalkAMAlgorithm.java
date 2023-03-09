package org.um.feri.ears.algorithms.so.random;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class RandomWalkAMAlgorithm extends NumberAlgorithm {
    private NumberSolution<Double> bestSolution;

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
            am[i] = task.problem.setFeasible(newX[i] + (newX[i] - old[i]), i); // if out of
            // range
        }
        return am;
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        NumberSolution<Double> currentSolution, iAritmetic, iExtend;
        this.task = task;
        bestSolution = task.getRandomEvaluatedSolution();
        if (debug)
            System.out.println(task.getNumberOfEvaluations() + " " + bestSolution);
        while (!task.isStopCriterion()) {
            currentSolution = task.getRandomEvaluatedSolution();
            if (task.problem.isFirstBetter(currentSolution, bestSolution)) {
                if (!task.isStopCriterion()) { // try also arithmetic mean

                    iAritmetic = new NumberSolution<>(Util.toDoubleArrayList(xArithmeticMeanOf(Util.toDoubleArray(bestSolution.getVariables()), Util.toDoubleArray(currentSolution.getVariables()))));
                    task.eval(iAritmetic);

                    if (task.problem.isFirstBetter(iAritmetic, currentSolution)) {
                        currentSolution = iAritmetic; // even better
                    } else {
                        if (!task.isStopCriterion()) { // try also extend

                            iExtend = new NumberSolution<>(Util.toDoubleArrayList(xInSameDirection(Util.toDoubleArray(bestSolution.getVariables()), Util.toDoubleArray(currentSolution.getVariables()))));
                            task.eval(iExtend);
                            if (task.problem.isFirstBetter(iExtend, currentSolution)) {
                                currentSolution = iExtend; // even better
                            }
                        }
                    }
                }
                bestSolution = currentSolution;
                if (debug)
                    System.out.println(task.getNumberOfEvaluations() + " " + bestSolution);
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
