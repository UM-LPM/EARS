package org.um.feri.ears.algorithms.so.hc;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;

public class HillClimbing extends Algorithm {

    public enum HillClimbingStrategy {ANY_ASCENT, STEEPEST_ASCENT, RANDOM_RESTART}

    private DoubleSolution globalBest, currentBest;
    private Task task;
    private double dxPercent;
    private HillClimbingStrategy strategy;

    public HillClimbing() {
        this(HillClimbingStrategy.RANDOM_RESTART, 0.001);
    }

    public HillClimbing(HillClimbingStrategy strategy, double dxPercent) {
        super();
        this.strategy = strategy;
        this.dxPercent = dxPercent;
        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("HC",
                "@article{russell2002artificial," +
                        "title={Artificial intelligence: a modern approach}," +
                        "author={Russell, Stuart and Norvig, Peter}," +
                        "year={2002}" +
                        "}",
                "HC_" + strategy.name(), "Hill Climbing");
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriterionException {
        task = taskProblem;

        double[] interval = task.getInterval();
        globalBest = task.getRandomEvaluatedSolution();
        currentBest = new DoubleSolution(globalBest);
        boolean improvement;

        while (!task.isStopCriterion()) {
            improvement = false;
            if (strategy == HillClimbingStrategy.ANY_ASCENT) {

                double[] currentPosition = currentBest.getDoubleVariables();

                for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                    double[] newPosition = currentPosition.clone();
                    newPosition[i] -= interval[i] * dxPercent;

                    if (checkImprovement(newPosition)) {
                        if (task.isFirstBetter(currentBest, globalBest))
                            globalBest = currentBest;
                        improvement = true;
                        break;
                    }

                    newPosition = currentPosition.clone();
                    newPosition[i] += interval[i] * dxPercent;

                    if (checkImprovement(newPosition)) {
                        if (task.isFirstBetter(currentBest, globalBest))
                            globalBest = currentBest;
                        improvement = true;
                        break;
                    }
                }
                //stop the search if no improvement
                if (!improvement)
                    return globalBest;
            } else if (strategy == HillClimbingStrategy.STEEPEST_ASCENT) {

                double[] currentPosition = currentBest.getDoubleVariables();

                for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                    double[] newPosition = currentPosition.clone();
                    newPosition[i] -= interval[i] * dxPercent;

                    if (checkImprovement(newPosition)) {
                        if (task.isFirstBetter(currentBest, globalBest))
                            globalBest = currentBest;
                        improvement = true;
                    }

                    newPosition = currentPosition.clone();
                    newPosition[i] += interval[i] * dxPercent;

                    if (checkImprovement(newPosition)) {
                        if (task.isFirstBetter(currentBest, globalBest))
                            globalBest = currentBest;
                        improvement = true;
                    }
                }
                //stop the search if no improvement
                if (!improvement)
                    return globalBest;
            } else if (strategy == HillClimbingStrategy.RANDOM_RESTART) {
                double[] currentPosition = currentBest.getDoubleVariables();

                for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                    double[] newPosition = currentPosition.clone();
                    newPosition[i] -= interval[i] * dxPercent;

                    if (checkImprovement(newPosition)) {
                        if (task.isFirstBetter(currentBest, globalBest))
                            globalBest = currentBest;
                        improvement = true;
                    }

                    newPosition = currentPosition.clone();
                    newPosition[i] += interval[i] * dxPercent;

                    if (checkImprovement(newPosition)) {
                        if (task.isFirstBetter(currentBest, globalBest))
                            globalBest = currentBest;
                        improvement = true;
                    }
                }
                //restart search if no improvement
                if (!improvement) {
                    if (!task.isStopCriterion())
                        currentBest = task.getRandomEvaluatedSolution();
                }
            }
        }
        return globalBest;
    }

    private boolean checkImprovement(double[] newPosition) throws StopCriterionException {

        if (task.isFeasible(newPosition)) {
            if (!task.isStopCriterion()) {
                DoubleSolution newSolution = task.eval(newPosition);
                if (task.isFirstBetter(newSolution, currentBest)) {
                    currentBest = newSolution;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
