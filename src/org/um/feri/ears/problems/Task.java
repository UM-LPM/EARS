package org.um.feri.ears.problems;

import org.um.feri.ears.visualization.graphing.recording.GraphDataRecorder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Task<S extends Solution, P extends Problem<S>> extends TaskBase<P> {

    protected S bestSolution; //Keeps track of the best solution found.

    /**
     * @param problem          the problem to be solved
     * @param stopCriterion    the stopping criterion
     * @param maxEvaluations   the maximum number of evaluations allowed
     * @param allowedTime      the maximum CPU time allowed in milliseconds
     * @param maxIterations    the maximum number of iterations
     * @param epsilonForGlobal the epsilon value used to check closeness to the global optimum
     */
    public Task(P problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, double epsilonForGlobal) {

        this.epsilonForGlobal = epsilonForGlobal;
        precisionOfRealNumbersInDecimalPlaces = (int) Math.log10((1. / epsilonForGlobal) + 1);
        this.stopCriterion = stopCriterion;
        this.maxEvaluations = maxEvaluations;
        numberOfEvaluations = 0;
        this.maxIterations = maxIterations;
        isStop = false;
        isGlobal = false;
        this.problem = problem;
        this.allowedCPUTimeNs = TimeUnit.MILLISECONDS.toNanos(allowedTime);
    }

    /**
     * @param problem        the problem to be solved
     * @param stopCriterion  the stopping criterion
     * @param maxEvaluations the maximum number of evaluations allowed
     * @param allowedTime    the maximum CPU time allowed in milliseconds
     * @param maxIterations  the maximum number of iterations
     */
    public Task(P problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations) {
        this(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations, 0);
    }

    public Task(Task task) {
        super(task);
    }

    private void checkIfGlobalReached(S solution) {
        if (problem.numberOfObjectives == 1) //TODO what to do in case of multi-objective optimization? (maybe always check all objectives)
            isGlobal = isEqualToGlobalOptimum(solution);
    }

    public boolean isEqualToGlobalOptimum(S solution) {
        return Math.abs(solution.getEval() - problem.getGlobalOptima()[0]) <= epsilonForGlobal;
    }

    public void addAncestors(S solution, List<Solution> parents) {
        if (isAncestorLoggingEnabled) {
            solution.parents = parents;
            solution.timeStamp = System.currentTimeMillis();
            solution.generationNumber = this.getNumberOfIterations();
            solution.evaluationNumber = this.getNumberOfEvaluations();
            ancestors.add(solution);
        }
    }

    /**
     * Generates a random evaluated solution.
     *
     * @return a random evaluated solution.
     * @throws StopCriterionException is thrown if the method is called after the stop criteria is met.
     *                                To prevent exception call {@link #isStopCriterion()} method to check if the stop criterion is already met.
     */
    public S getRandomEvaluatedSolution() throws StopCriterionException {

        S tmpSolution = problem.getRandomSolution();
        eval(tmpSolution);
        return tmpSolution;
    }

    /**
     * Evaluates the given solution
     *
     * @param solution to be evaluated
     * @throws StopCriterionException is thrown if the method is called after the stop criteria is met.
     *                                To prevent exception call {@link #isStopCriterion()} method to check if the stop criterion is already met.
     */
    public void eval(S solution) throws StopCriterionException {

        switch (stopCriterion) {
            case EVALUATIONS:
                performEvaluation(solution);
                break;
            case ITERATIONS:
                if (isStop)
                    throw new StopCriterionException("Max iterations");
                performEvaluation(solution);
                break;
            case GLOBAL_OPTIMUM_OR_EVALUATIONS:
                if (isGlobal)
                    throw new StopCriterionException("Global optimum already found");
                performEvaluation(solution);
                break;
            case CPU_TIME:
                if (!isStop) {
                    hasTheCpuTimeBeenExceeded(); // if CPU time is exceed allow last eval
                    performEvaluation(solution);
                } else {
                    throw new StopCriterionException("CPU Time");
                }
                break;
            case STAGNATION:
                if (isStop)
                    throw new StopCriterionException("Solution stagnation");
                performEvaluation(solution);
                break;
        }

        if (isAncestorLoggingEnabled) {
            solution.timeStamp = System.currentTimeMillis();
            solution.generationNumber = getNumberOfIterations();
            solution.evaluationNumber = getNumberOfEvaluations();
            ancestors.add(solution);
        }
    }

    public void eval(S solution, List<Solution> parents) throws StopCriterionException {

        eval(solution);

        if (isAncestorLoggingEnabled) {
            solution.parents = parents;
            solution.timeStamp = System.currentTimeMillis();
            solution.generationNumber = this.getNumberOfIterations();
            solution.evaluationNumber = this.getNumberOfEvaluations();
            ancestors.add(solution);
        }
    }

    private void performEvaluation(S solution) throws StopCriterionException {
        incrementNumberOfEvaluations();
        long start = System.nanoTime();
        problem.evaluate(solution);
        problem.evaluateConstraints(solution);
        checkImprovement(solution);
        evaluationTime += System.nanoTime() - start;
        checkIfGlobalReached(solution);
        GraphDataRecorder.AddRecord(solution, this.getProblemName());
        if (bestSolution != null && isEvaluationHistoryEnabled && (numberOfEvaluations % storeEveryNthEvaluation == 0))
            evaluationHistory.add(new EvaluationStorage.Evaluation(getNumberOfEvaluations(), getNumberOfIterations(), evaluationTime, bestSolution.getEval()));
    }

    /**
     * Checks if the current {@code solution} is better than the current best solution. If there is no improvement after
     * a certain amount of tries the stopping criterion is met. If there is an improvement the stagnation trial counter is reset.
     *
     * @param solution current evaluation
     */
    private void checkImprovement(S solution) {

        if (bestSolution == null)
            bestSolution = (S) solution.copy();

        if (problem.isFirstBetter(solution, bestSolution)) {
            bestSolution = (S) solution.copy();
            stagnationTrialCounter = 0;
        } else {
            stagnationTrialCounter++;
        }

        if (stopCriterion == StopCriterion.STAGNATION && stagnationTrialCounter >= maxTrialsBeforeStagnation) {
            isStop = true;
        }
    }

    public static void resetLoggingID() {
        Solution.resetLoggingID();
    }

    @Override
    public Task<S, P> clone() {
        return new Task<>(this);
    }
}
