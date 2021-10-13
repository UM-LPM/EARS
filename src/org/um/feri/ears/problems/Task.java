package org.um.feri.ears.problems;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.um.feri.ears.visualization.graphing.recording.GraphDataRecorder;

import javax.annotation.CheckReturnValue;

public class Task extends TaskBase<Problem> {

    /**
     * @param problem the problem to be solved
     * @param stopCriterion the stopping criterion
     * @param maxEvaluations the maximum number of evaluations allowed
     * @param allowedTime the maximum CPU time allowed in milliseconds
     * @param maxIterations the maximum number of iterations
     * @param epsilonForGlobal the epsilon value used to check closeness to the global optimum
     */
    public Task(Problem problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, double epsilonForGlobal) {

        this.epsilonForGlobal = epsilonForGlobal;
        precisionOfRealNumbersInDecimalPlaces = (int) Math.log10((1. / epsilonForGlobal) + 1);
        this.stopCriterion = stopCriterion;
        this.maxEvaluations = maxEvaluations;
        numberOfEvaluations = 0;
        this.maxIterations = maxIterations;
        isStop = false;
        isGlobal = false;
        this.problem = problem;
        this.allowedCPUTime = TimeUnit.MILLISECONDS.toNanos(allowedTime);

        // set initial best eval
        bestEval = problem.isMinimize() ? Double.MAX_VALUE : Double.MIN_VALUE;
    }
    /**
     * @param problem the problem to be solved
     * @param stopCriterion the stopping criterion
     * @param maxEvaluations the maximum number of evaluations allowed
     * @param allowedTime the maximum CPU time allowed in milliseconds
     * @param maxIterations the maximum number of iterations
     */
    public Task(Problem problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations) {
        this(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations,0);
    }

    public Task(Task task) {
        super(task);
    }

    public double[] getInterval() {
        double[] interval = new double[problem.upperLimit.size()];
        for (int i = 0; i < interval.length; i++) {
            interval[i] = problem.upperLimit.get(i) - problem.lowerLimit.get(i);
        }
        return interval;
    }

    /**
     * Checks if the given value is inside the lower and upper bounds of the given dimension. If the value is outside the
     * lower bound or upper bound then the lower or upper bound is returned. If the value is inside the bounds then the
     * unchanged value is returned.
     *
     * @param value to be set feasible
     * @param d dimension index
     * @return feasible value
     */
    public double setFeasible(double value, int d) {
        return problem.setFeasible(value, d);
    }

    /**
     * Checks if all the values in the array are inside the lower and upper bounds. If any value is outside the
     * lower bound or upper bound then it is set to lower or upper bound.
     *
     * @param x array to be set feasible
     * @return array with feasible values
     */
    @CheckReturnValue
    public double[] setFeasible(double[] x) {
        return problem.setFeasible(x);
    }

    /**
     * Checks if all the values in the solution are inside the lower and upper bounds. If any value is outside the
     * lower bound or upper bound then it is set to lower or upper bound.
     *
     * @param solution to be set feasible
     */
    public void setFeasible(DoubleSolution solution) {
        solution.variable = problem.setFeasible(solution.variable);
    }

    /**
     * Checks if the provided value is inside the upper and lower bounds
     *
     * @param x value to be checked
     * @return true if the value is inside the upper and lower bounds, false otherwise
     */
    public boolean isFeasible(double x, int d) {
        return problem.isFeasible(x, d);
    }

    /**
     * Checks if the provided solution is inside the interval given by the upper and lower bounds
     *
     * @param solution to be checked
     * @return true if all the values in the solution are inside upper and lower bounds, false otherwise
     */
    public boolean isFeasible(DoubleSolution solution) {
        return problem.isFeasible(solution.getVariables());
    }

    /**
     * Checks if the provided array is inside the interval given by the upper and lower bounds
     *
     * @param x array to be checked
     * @return true if all the values in the array are inside upper and lower bounds, false otherwise
     */
    public boolean isFeasible(double[] x) {
        return problem.isFeasible(x);
    }

    /**
     * Checks if the provided vector is inside the interval given by the upper and lower bounds
     *
     * @param x vector to be checked
     * @return true if all the values in the vector are inside upper and lower bounds, false otherwise
     */
    public boolean isFeasible(List<Double> x) {
        return problem.isFeasible(x);
    }

    public boolean isFirstBetter(DoubleSolution first, DoubleSolution second) {
        return problem.isFirstBetter(first.getVariables(), first.getEval(), second.getVariables(), second.getEval());
    }

    public boolean isFirstBetter(double a, double b) {
        return problem.isFirstBetter(a, b);
    }

    public double[] getLowerLimit() {

        double[] target = new double[problem.lowerLimit.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = problem.lowerLimit.get(i);
        }
        return target;
    }

    public double[] getUpperLimit() {
        double[] target = new double[problem.upperLimit.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = problem.upperLimit.get(i);
        }
        return target;
    }

    public double getLowerLimit(int i) {
        return problem.lowerLimit.get(i);
    }

    public double getUpperLimit(int i) {
        return problem.upperLimit.get(i);
    }

    private void checkIfGlobalReached(double d) {
        isGlobal = isEqualToGlobalOptimum(d);
    }

    public boolean isEqualToGlobalOptimum(double d) {
        return Math.abs(d - problem.getGlobalOptimum()) <= epsilonForGlobal;
    }

    public void addAncestors(DoubleSolution solution, List<DoubleSolution> parents) {
        if (isAncestorLoggingEnabled) {
            solution.parents = parents;
            solution.timeStamp = System.currentTimeMillis();
            solution.generationNumber = this.getNumberOfIterations();
            solution.evaluationNumber = this.getNumberOfEvaluations();
            ancestors.add(solution);
        }
    }

    /**
     * Better use method eval returns Individual with calculated fitness and constrains
     *
     * @param ds real vector to be evaluated (just calc constraints)
     * @return evaluated constrains
     * @deprecated
     */
    public double[] evaluateConstrains(List<Double> ds) {
        return problem.evaluateConstrains(ds);
    }

    public double[] getRandomVariables() {
        return problem.getRandomVariables();
    }

    /**
     * Generates a random unevaluated solution.
     *
     * @return randomly generated unevaluated solution
     */
    public DoubleSolution getRandomSolution() {
        return problem.getRandomSolution();
    }

    /**
     * Generates a random evaluated solution.
     * @return a random evaluated solution.
     * @throws StopCriterionException is thrown if the method is called after the stop criteria is met.
     * To prevent exception call {@link #isStopCriterion()} method to check if the stop criterion is already met.
     */
    public DoubleSolution getRandomEvaluatedSolution() throws StopCriterionException {

        DoubleSolution tmpSolution = getRandomSolution();
        eval(tmpSolution);
        return tmpSolution;
    }

    /**
     *  Evaluates the given solution
     * @param solution to be evaluated
     * @throws StopCriterionException is thrown if the method is called after the stop criteria is met.
     * To prevent exception call {@link #isStopCriterion()} method to check if the stop criterion is already met.
     */
    public void eval(DoubleSolution solution) throws StopCriterionException {
        DoubleSolution evaluatedSolution = eval(solution.getDoubleVariables());
        if(evaluatedSolution.getConstraints() != null)
            solution.setConstraints(evaluatedSolution.getConstraints());
        solution.setEval(evaluatedSolution.getEval());
    }

    public DoubleSolution eval(double[] x, List<DoubleSolution> parents) throws StopCriterionException {

        DoubleSolution tmpSolution = eval(x);

        if (isAncestorLoggingEnabled) {
            tmpSolution.parents = parents;
            tmpSolution.timeStamp = System.currentTimeMillis();
            tmpSolution.generationNumber = this.getNumberOfIterations();
            tmpSolution.evaluationNumber = this.getNumberOfEvaluations();
            ancestors.add(tmpSolution);
        }

        return tmpSolution;
    }

    public DoubleSolution eval(double[] x) throws StopCriterionException {

        //Double[] ds = ArrayUtils.toObject(x);
        //List<Double> ds = Arrays.asList(ArrayUtils.toObject(x));
        DoubleSolution tmpSolution = null;
        if (stopCriterion == StopCriterion.EVALUATIONS) {
            tmpSolution = performEvaluation(x);
        } else if (stopCriterion == StopCriterion.ITERATIONS) {
            if (isStop)
                throw new StopCriterionException("Max iterations");
            tmpSolution = performEvaluation(x);
        } else if (stopCriterion == StopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
            if (isGlobal)
                throw new StopCriterionException("Global optimum already found");
            tmpSolution = performEvaluation(x);
        } else if (stopCriterion == StopCriterion.CPU_TIME) {
            if (!isStop) {
                hasTheCpuTimeBeenExceeded(); // if CPU time is exceed allow last eval
                tmpSolution = performEvaluation(x);
            } else {
                throw new StopCriterionException("CPU Time");
            }
        } else if (stopCriterion == StopCriterion.STAGNATION) {
            if (isStop)
                throw new StopCriterionException("Solution stagnation");

            tmpSolution = performEvaluation(x);
            checkImprovement(tmpSolution.getEval());
        }
        if (tmpSolution != null) {
            if (isAncestorLoggingEnabled) {
                tmpSolution.timeStamp = System.currentTimeMillis();
                tmpSolution.generationNumber = getNumberOfIterations();
                tmpSolution.evaluationNumber = getNumberOfEvaluations();
                ancestors.add(tmpSolution);
            }
            return tmpSolution;
        } else {
            // Execution should never reach this point!
            throw new StopCriterionException("Not evaluated");
        }
    }

    private DoubleSolution performEvaluation(double[] x) throws StopCriterionException {
        incrementNumberOfEvaluations();
        long start = System.nanoTime();
        DoubleSolution tmpSolution = new DoubleSolution(x, problem.eval(x), problem.evaluateConstrains(x));
        evaluationTime += System.nanoTime() - start;
        checkIfGlobalReached(tmpSolution.getEval());
        GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
        if(isEvaluationHistoryEnabled)
            evaluationHistory.add(new EvaluationStorage.Evaluation(getNumberOfEvaluations(), getNumberOfIterations(), evaluationTime, tmpSolution.getEval()));
        return tmpSolution;
    }

    /**
     * Checks if the current evaluation {@code eval} is better than the best evaluation. If there is no improvement after
     * a certain amount of tries the stopping criterion is met. If there is an improvement the stagnation trial counter is reset.
     * @param eval current evaluation
     */
    private void checkImprovement(double eval) {

        if(problem.isFirstBetter(eval, bestEval)){
            bestEval = eval;
            stagnationTrialCounter = 0;
        }
        else {
            stagnationTrialCounter++;
        }

        if (stagnationTrialCounter >= maxTrialsBeforeStagnation) {
            isStop = true;
        }
    }

    public static void resetLoggingID() {
        SolutionBase.resetLoggingID();
    }

    @Override
    public Task clone() {
        return new Task(this);
    }
}
