package org.um.feri.ears.problems;

import org.um.feri.ears.visualization.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.quality_indicator.QualityIndicator;

import java.util.concurrent.TimeUnit;

public abstract class MOTask<T extends Number, P extends MOProblemBase<T>> extends TaskBase<P> {

    /**
     * @param problem the problem to be solved
     * @param stopCriterion the stopping criterion
     * @param maxEvaluations the maximum number of evaluations allowed
     * @param allowedTime the maximum CPU time allowed in milliseconds
     * @param maxIterations the maximum number of iterations
     * @param epsilonForGlobal the epsilon value used to check closeness to the global optimum
     */
    public MOTask(P problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, double epsilonForGlobal) {

        precisionOfRealNumbersInDecimalPlaces = (int) Math.log10((1. / epsilonForGlobal) + 1);
        this.stopCriterion = stopCriterion;
        this.maxEvaluations = maxEvaluations;
        numberOfEvaluations = 0;
        this.epsilonForGlobal = epsilonForGlobal;
        isStop = false;
        isGlobal = false;
        super.problem = problem; // TODO generic type in TaskBase
        this.allowedCPUTime = TimeUnit.MILLISECONDS.toNanos(allowedTime);
        this.maxIterations = maxIterations;
    }

    /**
     * @param problem the problem to be solved
     * @param stopCriterion the stopping criterion
     * @param maxEvaluations the maximum number of evaluations allowed
     * @param allowedTime the maximum CPU time allowed in milliseconds
     * @param maxIterations the maximum number of iterations
     */
    public MOTask(P problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations) {
        this(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations, 0);
    }

    public MOTask(MOTask<T, P> task) {
        precisionOfRealNumbersInDecimalPlaces = task.precisionOfRealNumbersInDecimalPlaces;
        stopCriterion = task.stopCriterion;
        maxEvaluations = task.maxEvaluations;
        numberOfEvaluations = task.numberOfEvaluations;
        epsilonForGlobal = task.epsilonForGlobal;
        isStop = task.isStop;
        isGlobal = task.isGlobal;
        maxIterations = task.maxIterations;
        allowedCPUTime = task.allowedCPUTime;
        super.problem = task.problem;  //TODO deep copy?
    }

    abstract public boolean areDimensionsInFeasibleInterval(ParetoSolution<T> solution);

    /**
     * @return The number of objectives
     */
    public int getNumberOfObjectives() {
        return problem.getNumberOfObjectives();
    }

    /**
     * @return The file name of the problem
     */
    public String getProblemFileName() {
        return problem.getFileName();
    }

    public String getBenchmarkName() {
        return problem.getBenchmarkName();
    }

    public int getNumberOfConstrains() {
        return problem.getNumberOfConstraints();
    }

    abstract public MOSolutionBase<T> getRandomMOSolution() throws StopCriterionException;

    public boolean isFirstBetter(ParetoSolution<T> x, ParetoSolution<T> y, QualityIndicator<T> qi) {
        return problem.isFirstBetter(x, y, qi);
    }

	/**
	 *  Evaluates the given solution
	 * @param solution to be evaluated
	 * @throws StopCriterionException is thrown if the method is called after the stop criteria is met.
	 * To prevent exception call {@link #isStopCriterion()} method to check if the stop criterion is already met.
	 */
    public void eval(MOSolutionBase<T> solution) throws StopCriterionException {

        if (stopCriterion == StopCriterion.EVALUATIONS) {
            incrementNumberOfEvaluations();
            problem.evaluate(solution);
            problem.evaluateConstraints(solution);
            GraphDataRecorder.AddRecord(solution, this.getProblemName());
        } else if (stopCriterion == StopCriterion.ITERATIONS) {
            if (isStop)
                throw new StopCriterionException("Max iterations");
            incrementNumberOfEvaluations();
            problem.evaluate(solution);
            problem.evaluateConstraints(solution);
            GraphDataRecorder.AddRecord(solution, this.getProblemName());
        } else if (stopCriterion == StopCriterion.CPU_TIME) {
            if (!isStop) {
                hasTheCpuTimeBeenExceeded(); // if CPU time is exceed allow last eval
                incrementNumberOfEvaluations();
                problem.evaluate(solution);
                problem.evaluateConstraints(solution);
                GraphDataRecorder.AddRecord(solution, this.getProblemName());
            } else {
                throw new StopCriterionException("CPU Time");
            }
        }
    }
}
