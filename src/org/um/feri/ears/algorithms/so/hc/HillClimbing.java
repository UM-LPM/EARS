package org.um.feri.ears.algorithms.so.hc;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

import java.util.ArrayList;
import java.util.List;

public class HillClimbing extends NumberAlgorithm {

    public enum HillClimbingStrategy {ANY_ASCENT, STEEPEST_ASCENT, RANDOM_RESTART}

    @AlgorithmParameter(name = "strategy")
    private HillClimbingStrategy strategy;
    @AlgorithmParameter
    private double dxPercent;

    private NumberSolution<Double> globalBest, currentBest;
    private Task<NumberSolution<Double>, DoubleProblem> task;

    public HillClimbing() {
        this(HillClimbingStrategy.RANDOM_RESTART, 0.001);
    }

    public HillClimbing(HillClimbingStrategy strategy, double dxPercent) {
        super();
        this.strategy = strategy;
        this.dxPercent = dxPercent;
        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("HC_" + strategy.name(), "Hill Climbing " + strategy.name(),
                "@article{russell2002artificial," +
                        "title={Artificial intelligence: a modern approach}," +
                        "author={Russell, Stuart and Norvig, Peter}," +
                        "year={2002}" +
                        "}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        double[] interval = task.problem.getInterval();
        globalBest = task.getRandomEvaluatedSolution();
        currentBest = new NumberSolution<>(globalBest);
        boolean improvement;

        while (!task.isStopCriterion()) {
            improvement = false;
            if (strategy == HillClimbingStrategy.ANY_ASCENT) {

                ArrayList<Double> currentPosition = currentBest.getVariables();

                for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                    ArrayList<Double> newPosition = new ArrayList<>(currentPosition);
                    newPosition.set(i, newPosition.get(i) - interval[i] * dxPercent);

                    if (checkImprovement(newPosition)) {
                        if (task.problem.isFirstBetter(currentBest, globalBest))
                            globalBest = currentBest;
                        improvement = true;
                        break;
                    }

                    newPosition = new ArrayList<>(currentPosition);
                    newPosition.set(i, newPosition.get(i) + interval[i] * dxPercent);

                    if (checkImprovement(newPosition)) {
                        if (task.problem.isFirstBetter(currentBest, globalBest))
                            globalBest = currentBest;
                        improvement = true;
                        break;
                    }
                }
                //stop the search if no improvement
                if (!improvement)
                    return globalBest;
            } else if (strategy == HillClimbingStrategy.STEEPEST_ASCENT) {

                ArrayList<Double> currentPosition = currentBest.getVariables();

                for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                    ArrayList<Double> newPosition = new ArrayList<>(currentPosition);
                    newPosition.set(i, newPosition.get(i) - interval[i] * dxPercent);

                    if (checkImprovement(newPosition)) {
                        if (task.problem.isFirstBetter(currentBest, globalBest))
                            globalBest = currentBest;
                        improvement = true;
                    }

                    newPosition = new ArrayList<>(currentPosition);
                    newPosition.set(i, newPosition.get(i) + interval[i] * dxPercent);

                    if (checkImprovement(newPosition)) {
                        if (task.problem.isFirstBetter(currentBest, globalBest))
                            globalBest = currentBest;
                        improvement = true;
                    }
                }
                //stop the search if no improvement
                if (!improvement)
                    return globalBest;
            } else if (strategy == HillClimbingStrategy.RANDOM_RESTART) {
                ArrayList<Double> currentPosition = currentBest.getVariables();

                for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                    ArrayList<Double> newPosition = new ArrayList<>(currentPosition);
                    newPosition.set(i, newPosition.get(i) - interval[i] * dxPercent);

                    if (checkImprovement(newPosition)) {
                        if (task.problem.isFirstBetter(currentBest, globalBest))
                            globalBest = currentBest;
                        improvement = true;
                    }

                    newPosition = new ArrayList<>(currentPosition);
                    newPosition.set(i, newPosition.get(i) + interval[i] * dxPercent);

                    if (checkImprovement(newPosition)) {
                        if (task.problem.isFirstBetter(currentBest, globalBest))
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

    private boolean checkImprovement(List<Double> newPosition) throws StopCriterionException {

        if (task.problem.isFeasible(newPosition)) {
            if (!task.isStopCriterion()) {

                NumberSolution<Double> newSolution = new NumberSolution<>(newPosition);
                task.eval(newSolution);

                if (task.problem.isFirstBetter(newSolution, currentBest)) {
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
