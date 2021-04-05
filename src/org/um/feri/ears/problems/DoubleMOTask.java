package org.um.feri.ears.problems;

import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class DoubleMOTask extends MOTask<Double, DoubleMOProblem> {

    public DoubleMOTask(StopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilon, DoubleMOProblem p) {
        super(stop, eval, allowedTime, maxIterations, epsilon, p);
    }

    public DoubleMOTask(DoubleMOTask task) {
        super(task);
    }

    public MOSolutionBase<Double> getRandomMOSolution() throws StopCriterionException {


        if (stopCriterion == StopCriterion.EVALUATIONS) {
            incrementNumberOfEvaluations();
            MOSolutionBase<Double> newSolution = p.getRandomSolution();
            //p.evaluateConstraints(newSolution);
            return newSolution;
        } else if (stopCriterion == StopCriterion.ITERATIONS) {
            if (isStop)
                throw new StopCriterionException("Max iterations");
            incrementNumberOfEvaluations();
            MOSolutionBase<Double> newSolution = p.getRandomSolution();
            return newSolution;

        } else if (stopCriterion == StopCriterion.CPU_TIME) {
            // check if the CPU time is not exceeded yet
            if (!isStop) {
                hasTheCpuTimeBeenExceeded(); // if CPU time is exceed allow last eval
                incrementNumberOfEvaluations();
                MOSolutionBase<Double> newSolution = p.getRandomSolution();
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
     * @param paretoSolution vector of possible solution
     * @return
     */
    public boolean areDimensionsInFeasibleInterval(ParetoSolution<Double> paretoSolution) {
        return p.areDimensionsInFeasableInterval(paretoSolution);
    }

    public Double[] getLowerLimit() {
        Double[] arr = new Double[p.lowerLimit.size()];
        arr = p.lowerLimit.toArray(arr);
        return arr;
    }

    public Double[] getUpperLimit() {
        Double[] arr = new Double[p.upperLimit.size()];
        arr = p.upperLimit.toArray(arr);
        return arr;
    }

    public double getLowerLimit(int i) {
        return p.lowerLimit.get(i);
    }

    public double getUpperLimit(int i) {
        return p.upperLimit.get(i);
    }

    @Override
    public MOTask<Double, DoubleMOProblem> clone() {
        return new DoubleMOTask(this);
    }
}
