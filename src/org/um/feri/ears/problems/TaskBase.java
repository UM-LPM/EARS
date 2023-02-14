package org.um.feri.ears.problems;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public abstract class TaskBase<P extends Problem<?>> {

    protected StopCriterion stopCriterion;
    protected int maxEvaluations; // for Stop criterion
    protected int numberOfEvaluations = 0; // for Stop criterion
	protected int maxIterations;
	protected int numberOfIterations = 0;
	protected long allowedCPUTimeNs;
	protected long evaluationTime = 0;
	protected long timerStart;
	protected double epsilonForGlobal = 0; // epsilon representing the error margin difference when comparing a solution to the global optimum
	protected boolean isStop;
	protected boolean isGlobal;
	protected int precisionOfRealNumbersInDecimalPlaces; //used only for discreet problem presentation (bit presentation in GA)
	public P problem;
	private int resetCount;
    protected int maxTrialsBeforeStagnation = 10000;
    protected int stagnationTrialCounter = 0;

    //protected StringBuilder ancestorSB;
    protected ArrayList<Solution> ancestors;
    protected boolean isAncestorLoggingEnabled = false;
    protected ArrayList<EvaluationStorage.Evaluation> evaluationHistory;
    protected boolean isEvaluationHistoryEnabled = false;
    protected int storeEveryNthEvaluation = 10000;

    public TaskBase (TaskBase<P> task) {

        stopCriterion = task.stopCriterion;
        maxEvaluations = task.maxEvaluations;
        numberOfEvaluations = task.numberOfEvaluations;
        maxIterations = task.maxIterations;
        numberOfIterations = task.numberOfIterations;
        allowedCPUTimeNs = task.allowedCPUTimeNs;
        evaluationTime = task.evaluationTime;
        timerStart = task.timerStart;
        epsilonForGlobal = task.epsilonForGlobal;
        isStop = task.isStop;
        isGlobal = task.isGlobal;
        precisionOfRealNumbersInDecimalPlaces = task.precisionOfRealNumbersInDecimalPlaces;
        problem = task.problem;  //TODO deep copy?
        resetCount = task.resetCount;
        maxTrialsBeforeStagnation = task.maxTrialsBeforeStagnation;
        stagnationTrialCounter = task.stagnationTrialCounter;
        storeEveryNthEvaluation = task.storeEveryNthEvaluation;
    }

    /**
     * Returns a deep copy of the Task object
     *
     * @return deep copy of the task object
     */
    abstract public TaskBase<P> clone();

    /**
     * Has the global optimum been reached.
     *
     * @return true if the global optimum found else false.
     */
    public boolean isGlobal() {
        return isGlobal;
    }

    public long getEvaluationTimeNs() {
        return evaluationTime;
    }

    public long getEvaluationTimeMs() {
        return TimeUnit.NANOSECONDS.toMillis(evaluationTime);
    }

    public StopCriterion getStopCriterion() {
        return stopCriterion;
    }

    public TaskBase() {
        resetCount = 0;
    }

    public void startTimer() {
        timerStart = System.nanoTime();
    }

    public void setEvaluationTime(long evaluationTime) {
        this.evaluationTime = evaluationTime;
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

    /**
     * Used only for discreet problem presentation (bit presentation in GA)
     *
     * @return the precision of real numbers in decimal places
     */
    public int getPrecisionMinDecimal() {
        return precisionOfRealNumbersInDecimalPlaces;
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
            hasTheCpuTimeBeenExceeded();
        }

        //stop only if stop criteria set to global optimum
        if (stopCriterion == StopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS && isGlobal)
            return true;

        return isStop;
    }

    public boolean hasTheCpuTimeBeenExceeded() {
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
        evaluationTime = 0;
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
                + epsilonForGlobal + ", isStop=" + isStop + ", isGlobal=" + isGlobal + ", precisionOfRealNumbersInDecimalPlaces="
                + precisionOfRealNumbersInDecimalPlaces + ", p=" + problem + "]";
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
                    + epsilonForGlobal + ", precisionOfRealNumbersInDecimalPlaces="
                    + precisionOfRealNumbersInDecimalPlaces;
        } else if (stopCriterion == StopCriterion.ITERATIONS) {
            return "Task = " + problem + " stopCriterion=" + stopCriterion + ", maxIterations=" + maxIterations + ", epsilon="
                    + epsilonForGlobal + ", precisionOfRealNumbersInDecimalPlaces="
                    + precisionOfRealNumbersInDecimalPlaces;
        } else if (stopCriterion == StopCriterion.CPU_TIME) {
            return "Task = " + problem + " stopCriterion=" + stopCriterion + ", allowedCPUTime=" + allowedCPUTimeNs + ", epsilon="
                    + epsilonForGlobal + ", precisionOfRealNumbersInDecimalPlaces="
                    + precisionOfRealNumbersInDecimalPlaces;
        } else
            return "";
    }
}
