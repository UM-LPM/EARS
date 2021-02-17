package org.um.feri.ears.problems;

import org.um.feri.ears.problems.moo.IntegerMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class IntegerMOTask extends MOTask<Integer, IntegerMOProblem> {

    public IntegerMOTask(EnumStopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilon, IntegerMOProblem p) {
        super(stop, eval, allowedTime, maxIterations, epsilon, p);
    }

    public IntegerMOTask(IntegerMOTask task) {
        super(task);
    }

    public MOSolutionBase<Integer> getRandomMOSolution() throws StopCriterionException {

        if (stopCriterion == EnumStopCriterion.EVALUATIONS) {
            incrementNumberOfEvaluations();
            MOSolutionBase<Integer> newSolution = p.getRandomSolution();
            //p.evaluateConstraints(newSolution);
            return newSolution;
        } else if (stopCriterion == EnumStopCriterion.ITERATIONS) {
            if (isStop)
                throw new StopCriterionException("Max iterations");

            incrementNumberOfEvaluations();
            MOSolutionBase<Integer> newSolution = p.getRandomSolution();
            return newSolution;

        } else if (stopCriterion == EnumStopCriterion.CPU_TIME) {
            // check if the CPU time is not exceeded yet
            if (!isStop) {
                hasTheCpuTimeBeenExceeded(); // if CPU time is exceed allow last eval
                incrementNumberOfEvaluations();
                MOSolutionBase<Integer> newSolution = p.getRandomSolution();
                return newSolution;
            } else {
                throw new StopCriterionException("CPU Time");
            }
        }
        return null;
    }

    public boolean areDimensionsInFeasibleInterval(ParetoSolution<Integer> bestByALg) {
        return p.areDimensionsInFeasableInterval(bestByALg);
    }


    public Integer[] getLowerLimit() {
        Integer[] arr = new Integer[p.lowerLimit.size()];
        arr = p.lowerLimit.toArray(arr);
        return arr;
    }

    public Integer[] getUpperLimit() {
        Integer[] arr = new Integer[p.upperLimit.size()];
        arr = p.upperLimit.toArray(arr);
        return arr;
    }

    @Override
    public MOTask<Integer, IntegerMOProblem> clone() {
        return new IntegerMOTask(this);
    }
}
