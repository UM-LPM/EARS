package org.um.feri.ears.problems;

import org.um.feri.ears.problems.moo.ParetoSolution;

public class IntegerMOTask extends MOTask<Integer> {

    public IntegerMOTask(IntegerProblem problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, double epsilon) {
        super(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations, epsilon);
    }

    public IntegerMOTask(IntegerProblem problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations) {
        super(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations, 0);
    }

    public IntegerMOTask(IntegerMOTask task) {
        super(task);
    }

    public NumberSolution<Integer> getRandomMOSolution() throws StopCriterionException {

        if (stopCriterion == StopCriterion.EVALUATIONS) {
            incrementNumberOfEvaluations();
            NumberSolution<Integer> newSolution = problem.getRandomSolution();
            //p.evaluateConstraints(newSolution);
            return newSolution;
        } else if (stopCriterion == StopCriterion.ITERATIONS) {
            if (isStop)
                throw new StopCriterionException("Max iterations");

            incrementNumberOfEvaluations();
            NumberSolution<Integer> newSolution = problem.getRandomSolution();
            return newSolution;

        } else if (stopCriterion == StopCriterion.CPU_TIME) {
            // check if the CPU time is not exceeded yet
            if (!isStop) {
                hasTheCpuTimeBeenExceeded(); // if CPU time is exceed allow last eval
                incrementNumberOfEvaluations();
                NumberSolution<Integer> newSolution = problem.getRandomSolution();
                return newSolution;
            } else {
                throw new StopCriterionException("CPU Time");
            }
        }
        return null;
    }

    public Integer[] getLowerLimit() {
        Integer[] arr = new Integer[problem.lowerLimit.size()];
        arr = problem.lowerLimit.toArray(arr);
        return arr;
    }

    public Integer[] getUpperLimit() {
        Integer[] arr = new Integer[problem.upperLimit.size()];
        arr = problem.upperLimit.toArray(arr);
        return arr;
    }

    @Override
    public MOTask<Integer> clone() {
        return new IntegerMOTask(this);
    }
}
