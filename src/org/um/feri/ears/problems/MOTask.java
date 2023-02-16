package org.um.feri.ears.problems;

import org.um.feri.ears.visualization.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.quality_indicator.QualityIndicator;

import java.util.concurrent.TimeUnit;

public class MOTask<Type extends Number> extends TaskBase<NumberProblem<Type>> {

    /**
     * @param problem the problem to be solved
     * @param stopCriterion the stopping criterion
     * @param maxEvaluations the maximum number of evaluations allowed
     * @param allowedTime the maximum CPU time allowed in milliseconds
     * @param maxIterations the maximum number of iterations
     * @param epsilonForGlobal the epsilon value used to check closeness to the global optimum
     */
    public MOTask(NumberProblem<Type> problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, double epsilonForGlobal) {

        precisionOfRealNumbersInDecimalPlaces = (int) Math.log10((1. / epsilonForGlobal) + 1);
        this.stopCriterion = stopCriterion;
        this.maxEvaluations = maxEvaluations;
        numberOfEvaluations = 0;
        this.epsilonForGlobal = epsilonForGlobal;
        isStop = false;
        isGlobal = false;
        super.problem = problem;
        this.allowedCPUTimeNs = TimeUnit.MILLISECONDS.toNanos(allowedTime);
        this.maxIterations = maxIterations;
    }

    /**
     * @param problem the problem to be solved
     * @param stopCriterion the stopping criterion
     * @param maxEvaluations the maximum number of evaluations allowed
     * @param allowedTime the maximum CPU time allowed in milliseconds
     * @param maxIterations the maximum number of iterations
     */
    public MOTask(NumberProblem<Type> problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations) {
        this(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations, 0);
    }

    public MOTask(MOTask<Type> task) {
        precisionOfRealNumbersInDecimalPlaces = task.precisionOfRealNumbersInDecimalPlaces;
        stopCriterion = task.stopCriterion;
        maxEvaluations = task.maxEvaluations;
        numberOfEvaluations = task.numberOfEvaluations;
        epsilonForGlobal = task.epsilonForGlobal;
        isStop = task.isStop;
        isGlobal = task.isGlobal;
        maxIterations = task.maxIterations;
        allowedCPUTimeNs = task.allowedCPUTimeNs;
        super.problem = task.problem;
    }

    public String getBenchmarkName() {
        return problem.getBenchmarkName();
    }

    public int getNumberOfConstrains() {
        return problem.getNumberOfConstraints();
    }

    //abstract public NumberSolution<Type> getRandomMOSolution() throws StopCriterionException;

    public boolean isFirstBetter(ParetoSolution<Type> x, ParetoSolution<Type> y, QualityIndicator<Type> qi) {
        return problem.isFirstBetter(x, y, qi);
    }

    public NumberSolution<Type> getRandomEvaluatedSolution() throws StopCriterionException {

        NumberSolution<Type> tmpSolution = problem.getRandomSolution();
        eval(tmpSolution);
        return tmpSolution;
    }

	/**
	 *  Evaluates the given solution
	 * @param solution to be evaluated
	 * @throws StopCriterionException is thrown if the method is called after the stop criteria is met.
	 * To prevent exception call {@link #isStopCriterion()} method to check if the stop criterion is already met.
	 */
    public void eval(NumberSolution<Type> solution) throws StopCriterionException {

        incrementNumberOfEvaluations();
        problem.evaluate(solution);
        if(problem.numberOfConstraints > 0)
            problem.evaluateConstraints(solution);
        GraphDataRecorder.AddRecord(solution, getProblemName());

        if (stopCriterion == StopCriterion.ITERATIONS && isStop) {
            throw new StopCriterionException("Max iterations");
        } else if (stopCriterion == StopCriterion.CPU_TIME) {
            if (hasCpuTimeExceeded()) {
                throw new StopCriterionException("CPU Time");
            }
        }
    }

    public Type getLowerLimit(int i) {
        return problem.lowerLimit.get(i);
    }

    public Type getUpperLimit(int i) {
        return problem.upperLimit.get(i);
    }

    @Override
    public MOTask<Type> clone() {
        return new MOTask<>(this);
    }
}
