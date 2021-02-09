package org.um.feri.ears.problems;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.um.feri.ears.graphing.recording.GraphDataRecorder;

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

    public double[] getInterval() {
        double[] interval = new double[p.upperLimit.size()];
        for (int i = 0; i < interval.length; i++) {
            interval[i] = p.upperLimit.get(i) - p.lowerLimit.get(i);
        }
        return interval;
    }

    /**
     * Works only for basic interval setting!
     * Sets interval!
     * for example -40<x_i<40 <p>
     * if x_i <-40 -> -40 same for 40!
     *
     * @param d value
     * @param i index of dimension
     * @return
     */
    public double setFeasible(double d, int i) {
        return p.setFeasible(d, i);
    }

    public double[] setFeasible(double[] d) {
        return p.setFeasible(d);
    }

    public void setFeasible(DoubleSolution sol) {
        sol.variable = p.setFeasible(sol.variable);
    }

    public boolean isFeasible(double x, int d) {
        return p.isFeasible(x, d);
    }

    public boolean isFeasible(DoubleSolution solution) {
        return p.isFeasible(solution.getVariables());
    }

    public boolean isFeasible(double[] solution) {
        return p.isFeasible(solution);
    }

    /**
     * Checks if the provided vector is inside the interval given by the upper and lower limits
     *
     * @param x vector to be checked
     * @return true if the vector is inside interval, false otherwise
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
     * @return
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
     *
     * @param solution
     * @return
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
                hasTheCPUTimeBeenExceeded(); // if CPU time is exceed allow last eval
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
        DoubleSolution tmpSolution = new DoubleSolution(x, p.eval(x), p.evaluateConstrains(x), p.upperLimit, p.lowerLimit);
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
}
