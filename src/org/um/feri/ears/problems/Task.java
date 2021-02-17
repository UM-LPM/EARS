package org.um.feri.ears.problems;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.um.feri.ears.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.problems.moo.IntegerMOProblem;

public class Task extends TaskBase<Problem> {

    /**
     * @param stop        the stopping criterion
     * @param eval        the maximum number of evaluations allowed
     * @param allowedTime the maximum CPU time allowed in milliseconds
     * @param epsilonForGlobal     the epsilon value for global optimum
     * @param p           the problem
     */
    public Task(EnumStopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilonForGlobal, Problem p) {
        this(stop, eval, allowedTime, maxIterations, epsilonForGlobal, p, (int) Math.log10((1. / epsilonForGlobal) + 1));
    }

    public Task(EnumStopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilonForGlobal, Problem p, int precisionOfRealNumbers) {
        precisionOfRealNumbersInDecimalPlaces = precisionOfRealNumbers;
        stopCriterion = stop;
        maxEvaluations = eval;
        numberOfEvaluations = 0;
        this.epsilonForGlobal = epsilonForGlobal;
        this.maxIterations = maxIterations;
        isStop = false;
        isGlobal = false;
        this.p = p;
        this.allowedCPUTime = TimeUnit.MILLISECONDS.toNanos(allowedTime);

        // set initial best eval
        bestEval = p.isMinimize() ? Double.MAX_VALUE : Double.MIN_VALUE;
    }

    public Task(Task task) {
        super(task);
    }

    public double[] getInterval() {
        double[] interval = new double[p.upperLimit.size()];
        for (int i = 0; i < interval.length; i++) {
            interval[i] = p.upperLimit.get(i) - p.lowerLimit.get(i);
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
        return p.setFeasible(value, d);
    }

    /**
     * Checks if all the values in the array are inside the lower and upper bounds. If any value is outside the
     * lower bound or upper bound then it is set to lower or upper bound.
     *
     * @param x array to be set feasible
     * @return array with feasible values
     */
    public double[] setFeasible(double[] x) {
        return p.setFeasible(x);
    }

    /**
     * Checks if all the values in the solution are inside the lower and upper bounds. If any value is outside the
     * lower bound or upper bound then it is set to lower or upper bound.
     *
     * @param solution to be set feasible
     */
    public void setFeasible(DoubleSolution solution) {
        solution.variable = p.setFeasible(solution.variable);
    }

    /**
     * Checks if the provided value is inside the upper and lower bounds
     *
     * @param x value to be checked
     * @return true if the value is inside the upper and lower bounds, false otherwise
     */
    public boolean isFeasible(double x, int d) {
        return p.isFeasible(x, d);
    }

    /**
     * Checks if the provided solution is inside the interval given by the upper and lower bounds
     *
     * @param solution to be checked
     * @return true if all the values in the solution are inside upper and lower bounds, false otherwise
     */
    public boolean isFeasible(DoubleSolution solution) {
        return p.isFeasible(solution.getVariables());
    }

    /**
     * Checks if the provided array is inside the interval given by the upper and lower bounds
     *
     * @param x array to be checked
     * @return true if all the values in the array are inside upper and lower bounds, false otherwise
     */
    public boolean isFeasible(double[] x) {
        return p.isFeasible(x);
    }

    /**
     * Checks if the provided vector is inside the interval given by the upper and lower bounds
     *
     * @param x vector to be checked
     * @return true if all the values in the vector are inside upper and lower bounds, false otherwise
     */
    public boolean isFeasible(List<Double> x) {
        return p.isFeasible(x);
    }

    public boolean isFirstBetter(DoubleSolution first, DoubleSolution second) {
        return p.isFirstBetter(first.getVariables(), first.getEval(), second.getVariables(), second.getEval());
    }

    public boolean isFirstBetter(double a, double b) {
        return p.isFirstBetter(a, b);
    }

    public double[] getLowerLimit() {

        double[] target = new double[p.lowerLimit.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = p.lowerLimit.get(i);
        }
        return target;
    }

    public double[] getUpperLimit() {
        double[] target = new double[p.upperLimit.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = p.upperLimit.get(i);
        }
        return target;
    }

    public double getLowerLimit(int i) {
        return p.lowerLimit.get(i);
    }

    public double getUpperLimit(int i) {
        return p.upperLimit.get(i);
    }

    private void checkIfGlobalReached(double d) {
        isGlobal = isEqualToGlobalOptimum(d);
    }

    public boolean isEqualToGlobalOptimum(double d) {
        return Math.abs(d - p.getGlobalOptimum()) <= epsilonForGlobal;
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
        return p.evaluateConstrains(ds);
    }

    public double[] getRandomVariables() {
        return p.getRandomVariables();
    }

    /**
     * Generates a random unevaluated solution.
     *
     * @return randomly generated unevaluated solution
     */
    public DoubleSolution getRandomSolution() {
        return p.getRandomSolution();
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
        if (stopCriterion == EnumStopCriterion.EVALUATIONS) {
            tmpSolution = performEvaluation(x);
        } else if (stopCriterion == EnumStopCriterion.ITERATIONS) {
            if (isStop)
                throw new StopCriterionException("Max iterations");
            tmpSolution = performEvaluation(x);
        } else if (stopCriterion == EnumStopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
            if (isGlobal)
                throw new StopCriterionException("Global optimum already found");
            tmpSolution = performEvaluation(x);
        } else if (stopCriterion == EnumStopCriterion.CPU_TIME) {
            if (!isStop) {
                hasTheCpuTimeBeenExceeded(); // if CPU time is exceed allow last eval
                tmpSolution = performEvaluation(x);
            } else {
                throw new StopCriterionException("CPU Time");
            }
        } else if (stopCriterion == EnumStopCriterion.STAGNATION) {
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
        DoubleSolution tmpSolution = new DoubleSolution(x, p.eval(x), p.evaluateConstrains(x));
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

        if(p.isFirstBetter(eval, bestEval)){
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
