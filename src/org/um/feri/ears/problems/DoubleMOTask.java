package org.um.feri.ears.problems;

import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class DoubleMOTask extends MOTask<Double, DoubleMOProblem> {

    public DoubleMOTask(DoubleMOProblem problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, double epsilon) {
        super(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations, epsilon);
    }

    public DoubleMOTask(DoubleMOProblem problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations) {
        super(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations, 0);
    }

    public DoubleMOTask(DoubleMOTask task) {
        super(task);
    }

    public MOSolutionBase<Double> getRandomMOSolution() throws StopCriterionException {


        if (stopCriterion == StopCriterion.EVALUATIONS) {
            incrementNumberOfEvaluations();
            MOSolutionBase<Double> newSolution = problem.getRandomSolution();
            //p.evaluateConstraints(newSolution);
            return newSolution;
        } else if (stopCriterion == StopCriterion.ITERATIONS) {
            if (isStop)
                throw new StopCriterionException("Max iterations");
            incrementNumberOfEvaluations();
            MOSolutionBase<Double> newSolution = problem.getRandomSolution();
            return newSolution;

        } else if (stopCriterion == StopCriterion.CPU_TIME) {
            // check if the CPU time is not exceeded yet
            if (!isStop) {
                hasTheCpuTimeBeenExceeded(); // if CPU time is exceed allow last eval
                incrementNumberOfEvaluations();
                MOSolutionBase<Double> newSolution = problem.getRandomSolution();
                return newSolution;
            } else {
                throw new StopCriterionException("CPU Time");
            }
        }

        return null;
    }

    /**
     * with no evaluations just checks
     * if algorithm result is in interval.
     * This is not checking constrains, just basic intervals!
     * Delegated from Problem!
     *
     * @param solution vector of possible solution
     * @return
     */
    public boolean areDimensionsInFeasibleInterval(ParetoSolution<Double> solution) {
        return problem.areDimensionsInFeasableInterval(solution);
    }

    public Double[] getLowerLimit() {
        Double[] arr = new Double[problem.lowerLimit.size()];
        arr = problem.lowerLimit.toArray(arr);
        return arr;
    }

    public Double[] getUpperLimit() {
        Double[] arr = new Double[problem.upperLimit.size()];
        arr = problem.upperLimit.toArray(arr);
        return arr;
    }

    public double getLowerLimit(int i) {
        return problem.lowerLimit.get(i);
    }

    public double getUpperLimit(int i) {
        return problem.upperLimit.get(i);
    }

    @Override
    public MOTask<Double, DoubleMOProblem> clone() {
        return new DoubleMOTask(this);
    }
}
