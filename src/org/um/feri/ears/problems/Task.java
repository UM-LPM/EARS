package org.um.feri.ears.problems;

import org.um.feri.ears.visualization.graphing.recording.GraphDataRecorder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Task<S extends Solution, P extends Problem<S>> {

    public static final class Accessor { private Accessor() {} }
    private static final Accessor ACCESSOR_INSTANCE = new Accessor();

    //add "Task.Accessor accessor" to method call

    public P problem;
    protected S bestSolution;
    protected StopCriterion stopCriterion;
    protected int maxEvaluations;
    protected int numberOfEvaluations = 0;
    protected int maxIterations;
    protected int numberOfIterations = 0;
    protected long allowedCPUTimeNs;
    protected long evaluationTimeNs = 0;
    protected long timerStart;
    protected double epsilonForGlobal = 0; // epsilon representing the error margin difference when comparing a solution to the global optimum
    protected boolean isStop;
    protected boolean isGlobal;
    private int resetCount = 0;
    protected int maxTrialsBeforeStagnation = 10000;
    protected int stagnationTrialCounter = 0;

    //protected StringBuilder ancestorSB;
    protected ArrayList<Solution> ancestors;
    protected boolean isAncestorLoggingEnabled = false;
    protected ArrayList<EvaluationStorage.Evaluation> evaluationHistory;
    protected boolean isEvaluationHistoryEnabled = false;
    protected int storeEveryNthEvaluation = 10000;


    /**
     * @param problem          the problem to be solved
     * @param stopCriterion    the stopping criterion
     * @param maxEvaluations   the maximum number of evaluations allowed
     * @param allowedTimeMs      the maximum CPU time allowed in milliseconds
     * @param maxIterations    the maximum number of iterations
     * @param epsilonForGlobal the epsilon value used to check closeness to the global optimum
     */
    public Task(P problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTimeMs, int maxIterations, double epsilonForGlobal) {

        this.epsilonForGlobal = epsilonForGlobal;
        this.stopCriterion = stopCriterion;
        this.maxEvaluations = maxEvaluations;
        this.maxIterations = maxIterations;
        numberOfEvaluations = 0;
        isStop = false;
        isGlobal = false;
        this.problem = problem;
        this.allowedCPUTimeNs = TimeUnit.MILLISECONDS.toNanos(allowedTimeMs);
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

    public Task(Task<S, P> task) {

        problem = task.problem;
        stopCriterion = task.stopCriterion;
        maxEvaluations = task.maxEvaluations;
        numberOfEvaluations = task.numberOfEvaluations;
        maxIterations = task.maxIterations;
        numberOfIterations = task.numberOfIterations;
        allowedCPUTimeNs = task.allowedCPUTimeNs;
        evaluationTimeNs = task.evaluationTimeNs;
        timerStart = task.timerStart;
        epsilonForGlobal = task.epsilonForGlobal;
        isStop = task.isStop;
        isGlobal = task.isGlobal;
        resetCount = task.resetCount;
        maxTrialsBeforeStagnation = task.maxTrialsBeforeStagnation;
        stagnationTrialCounter = task.stagnationTrialCounter;
        isAncestorLoggingEnabled = task.isAncestorLoggingEnabled;
        isEvaluationHistoryEnabled = task.isEvaluationHistoryEnabled;
        storeEveryNthEvaluation = task.storeEveryNthEvaluation;
    }

    private void checkIfGlobalReached(S solution) {
        if (problem.numberOfObjectives == 1) //Only for single-objective optimization
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
                    hasCpuTimeExceeded(); // if CPU time is exceed allow last eval
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
        if(problem.numberOfConstraints > 0)
            problem.evaluateConstraints(solution);
        checkImprovement(solution);
        evaluationTimeNs += System.nanoTime() - start;
        checkIfGlobalReached(solution);
        GraphDataRecorder.AddRecord(solution, this.getProblemName());
        if (bestSolution != null && isEvaluationHistoryEnabled && (numberOfEvaluations % storeEveryNthEvaluation == 0))
            evaluationHistory.add(new EvaluationStorage.Evaluation(getNumberOfEvaluations(), getNumberOfIterations(), evaluationTimeNs, bestSolution.getEval()));
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

    /**
     * Has the global optimum been reached.
     *
     * @return true if the global optimum found else false.
     */
    public boolean isGlobal() {
        return isGlobal;
    }

    public long getEvaluationTimeNs() {
        return evaluationTimeNs;
    }

    public long getEvaluationTimeMs() {
        return TimeUnit.NANOSECONDS.toMillis(evaluationTimeNs);
    }

    public StopCriterion getStopCriterion() {
        return stopCriterion;
    }

    public void startTimer() {
        timerStart = System.nanoTime();
    }

    public void setEvaluationTimeNs(long evaluationTimeNs) {
        this.evaluationTimeNs = evaluationTimeNs;
    }

    /**
     * Increments the number of iterations.
     *
     * @throws StopCriterionException if the number of iterations exceeds the maximum number of iterations.
     */
    public void incrementNumberOfIterations() throws StopCriterionException {
        if (stopCriterion == StopCriterion.ITERATIONS) {
            if (numberOfIterations >= maxIterations)
                throw new StopCriterionException("Max iterations");
        }

        numberOfIterations++;
        if (numberOfIterations >= maxIterations && stopCriterion == StopCriterion.ITERATIONS) {
            isStop = true;
        }
    }

    /**
     * Increments the number of evaluations. This method shouldn't be called manually.
     *
     * @throws StopCriterionException if the number of evaluations exceeds the maximum number of evaluations .
     */
    public void incrementNumberOfEvaluations() throws StopCriterionException {
        if (numberOfEvaluations >= maxEvaluations && stopCriterion == StopCriterion.EVALUATIONS)
            throw new StopCriterionException("Max evaluations");
        numberOfEvaluations++;
        if (numberOfEvaluations >= maxEvaluations && (stopCriterion == StopCriterion.EVALUATIONS || stopCriterion == StopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS))
            isStop = true;
    }

    /**
     * When comparing a solution to the global optimum and the error margin difference is less or equal to epsilon, then the
     * global optimum has been reached
     *
     * @return epsilon representing the error margin difference when comparing a solution to the global optimum
     */
    public double getEpsilonForGlobal() {
        return epsilonForGlobal;
    }

    public void enableAncestorLogging() {
        isAncestorLoggingEnabled = true;
        if (ancestors == null)
            ancestors = new ArrayList<>();
    }

    public void disableAncestorLogging() {
        isAncestorLoggingEnabled = false;
    }

    public void enableEvaluationHistory() {
        isEvaluationHistoryEnabled = true;
        if (evaluationHistory == null)
            evaluationHistory = new ArrayList<>();
    }

    public void disableEvaluationHistory() {
        isEvaluationHistoryEnabled = false;
    }

    public ArrayList<EvaluationStorage.Evaluation> getEvaluationHistory() {
        return evaluationHistory;
    }

    public void setEvaluationHistory(ArrayList<EvaluationStorage.Evaluation> evaluationHistory) {
        this.evaluationHistory = evaluationHistory;
    }

    public int getStoreEveryNthEvaluation() {
        return storeEveryNthEvaluation;
    }

    public void setStoreEveryNthEvaluation(int storeEveryNthEvaluation) {
        this.storeEveryNthEvaluation = storeEveryNthEvaluation;
    }

    public ArrayList<Solution> getAncestors() {
        return ancestors;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public long getAllowedCPUTimeNs() {
        return allowedCPUTimeNs;
    }

    public int getNumberOfEvaluations() {
        return numberOfEvaluations;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public long getAvailableCPUTime() {
        return allowedCPUTimeNs - System.nanoTime();
    }

    public int getMaxTrialsBeforeStagnation() {
        return maxTrialsBeforeStagnation;
    }

    public long getUsedCPUTime() {
        return System.nanoTime() - timerStart;
    }

    public boolean isStopCriterion() {

        if (stopCriterion == StopCriterion.CPU_TIME) {
            hasCpuTimeExceeded();
        }

        //stop only if stop criteria set to global optimum
        if (stopCriterion == StopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS && isGlobal)
            return true;

        return isStop;
    }

    public boolean hasCpuTimeExceeded() {
        if (numberOfEvaluations == 0) { //set start time if 0 evaluations
            startTimer();
        }

        if (System.nanoTime() - timerStart > allowedCPUTimeNs) {
            isStop = true;
            return true;
        }
        return false;
    }

    /**
     * Use to check if the problem is to be minimized.
     *
     * @return true if the problem is to be minimized
     */
    public boolean isMinimize() {
        return problem.isMinimize();
    }

    /**
     * @return name of the problem
     */
    public String getProblemName() {
        return problem.getName();
    }

    public String getFileNameString() {
        return problem.getFileNameString();
    }

    public int getProblemHashCode() {
        return problem.hashCode();
    }

    public void resetCounter() {
        resetCount++;
        numberOfEvaluations = 0;
        numberOfIterations = 0;
        evaluationTimeNs = 0;
        isStop = false;
        isGlobal = false;
        timerStart = System.nanoTime();
        stagnationTrialCounter = 0;
    }

    public int getResetCount() {
        return resetCount;
    }

    public String getStopCriterionString() {
        if (stopCriterion == StopCriterion.EVALUATIONS) {
            return "Max evaluation = " + getMaxEvaluations();
        }
        if (stopCriterion == StopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
            return "Global optimum epsilon = " + epsilonForGlobal + " or  max evaluations = " + getMaxEvaluations();
        }
        if (stopCriterion == StopCriterion.ITERATIONS) {
            return "Max iterations = " + getMaxIterations();
        }
        if (stopCriterion == StopCriterion.CPU_TIME) {
            return "CPU time = " + TimeUnit.NANOSECONDS.toMillis(getAllowedCPUTimeNs()) + " ms";
        }
        if (stopCriterion == StopCriterion.STAGNATION) {
            return "Stagnation trials = " + stagnationTrialCounter;
        }
        return "not defined";
    }

    @Override
    public String toString() {
        return "Task [stopCriterion= [" + getStopCriterionString() + "], epsilon="
                + epsilonForGlobal + ", isStop=" + isStop + ", isGlobal=" + isGlobal + ", p=" + problem + "]";
    }

    public String getTaskInfoCSV() {
        return problem.getProblemInfoCSV() + "task stop criterion:" + getStopCriterionString() + ",epsilon:" + epsilonForGlobal + ",";
    }

    /**
     * Returns a string containing all the tasks information that doesn't change.
     *
     * @return string containing all information about the task
     */
    public String getTaskInfo() {

        if (stopCriterion == StopCriterion.EVALUATIONS) {
            return "Task = " + problem + " stopCriterion=" + stopCriterion + ", maxEvaluations=" + maxEvaluations + ", epsilon="
                    + epsilonForGlobal;
        } else if (stopCriterion == StopCriterion.ITERATIONS) {
            return "Task = " + problem + " stopCriterion=" + stopCriterion + ", maxIterations=" + maxIterations + ", epsilon="
                    + epsilonForGlobal;
        } else if (stopCriterion == StopCriterion.CPU_TIME) {
            return "Task = " + problem + " stopCriterion=" + stopCriterion + ", allowedCPUTime=" + allowedCPUTimeNs + ", epsilon="
                    + epsilonForGlobal;
        } else
            return "";
    }

}
