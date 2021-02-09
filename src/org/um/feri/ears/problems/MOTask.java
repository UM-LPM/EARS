package org.um.feri.ears.problems;

import org.um.feri.ears.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.qualityIndicator.QualityIndicator;

import java.util.concurrent.TimeUnit;

public abstract class MOTask<T extends Number, P extends MOProblemBase<T>> extends TaskBase<P> {

    /**
     * Task constructor for multiobjective optimization.
     *
	 * @param stop        the stopping criterion
	 * @param eval        the maximum number of evaluations allowed
	 * @param allowedTime the maximum CPU time allowed in milliseconds
	 * @param epsilonForGlobal     the epsilon value for global optimum
	 * @param p           the problem
     */
    public MOTask(EnumStopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilonForGlobal, P p) {

        this(stop, eval, allowedTime, maxIterations, epsilonForGlobal, p, (int) Math.log10((1. / epsilonForGlobal) + 1));
    }

    abstract public boolean areDimensionsInFeasibleInterval(ParetoSolution<T> bestByALg);

    /**
     * Task constructor for multi-objective optimization.
     *
	 * @param stop        the stopping criterion
	 * @param eval        the maximum number of evaluations allowed
	 * @param allowedTime the maximum CPU time allowed in milliseconds
	 * @param epsilonForGlobal     the epsilon value for global optimum
	 * @param p           the problem
     */
    public MOTask(EnumStopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilonForGlobal, P p, int precisionOfRealNumbers) {

        precisionOfRealNumbersInDecimalPlaces = precisionOfRealNumbers;
        stopCriterion = stop;
        maxEvaluations = eval;
        numberOfEvaluations = 0;
        this.epsilonForGlobal = epsilonForGlobal;
        isStop = false;
        isGlobal = false;
        super.p = p; // TODO generic type in TaskBase
        this.allowedCPUTime = TimeUnit.MILLISECONDS.toNanos(allowedTime);
        this.maxIterations = maxIterations;
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
        super.p = task.p;  //TODO deep copy?
    }

    /**
     * @return The number of objectives
     */
    public int getNumberOfObjectives() {
        return p.getNumberOfObjectives();
    }

    /**
     * @return The file name of the problem
     */
    public String getProblemFileName() {
        return p.getFileName();
    }

    public String getBenchmarkName() {
        return p.getBenchmarkName();
    }

    public int getNumberOfConstrains() {
        return p.getNumberOfConstraints();
    }

    abstract public MOSolutionBase<T> getRandomMOSolution() throws StopCriterionException;

    public boolean isFirstBetter(ParetoSolution<T> x, ParetoSolution<T> y, QualityIndicator<T> qi) {
        return p.isFirstBetter(x, y, qi);
    }

	/**
	 *  Evaluates the given solution
	 * @param solution to be evaluated
	 * @throws StopCriterionException is thrown if the method is called after the stop criteria is met.
	 * To prevent exception call {@link #isStopCriterion()} method to check if the stop criterion is already met.
	 */
    public void eval(MOSolutionBase<T> solution) throws StopCriterionException {

        if (stopCriterion == EnumStopCriterion.EVALUATIONS) {
            incrementNumberOfEvaluations();
            p.evaluate(solution);
            p.evaluateConstraints(solution);
            GraphDataRecorder.AddRecord(solution, this.getProblemName());
        } else if (stopCriterion == EnumStopCriterion.ITERATIONS) {
            if (isStop)
                throw new StopCriterionException("Max iterations");
            incrementNumberOfEvaluations();
            p.evaluate(solution);
            p.evaluateConstraints(solution);
            GraphDataRecorder.AddRecord(solution, this.getProblemName());
        } else if (stopCriterion == EnumStopCriterion.CPU_TIME) {
            if (!isStop) {
                hasTheCpuTimeBeenExceeded(); // if CPU time is exceed allow last eval
                incrementNumberOfEvaluations();
                p.evaluate(solution);
                p.evaluateConstraints(solution);
                GraphDataRecorder.AddRecord(solution, this.getProblemName());
            } else {
                throw new StopCriterionException("CPU Time");
            }
        }
    }

    /**
     * Returns a deep copy of the Task object
     *
     * @return deep copy of the task object
     */
    abstract public MOTask returnCopy();
}
