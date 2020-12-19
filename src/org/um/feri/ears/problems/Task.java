package org.um.feri.ears.problems;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.graphing.recording.GraphDataRecorder;

public class Task extends TaskBase<Problem> {

    /**
     * @param stop        the stopping criteria
     * @param eval        the maximum number of evaluations allowed
     * @param allowedTime the maximum CPU time allowed in milliseconds
     * @param epsilon     the epsilon value for global optimum
     * @param p           the problem
     */


    public Task(EnumStopCriteria stop, int eval, long allowedTime, int maxIterations, double epsilon, Problem p) {
        this(stop, eval, allowedTime, maxIterations, epsilon, p, (int) Math.log10((1. / epsilon) + 1));
    }

    public Task(EnumStopCriteria stop, int eval, long allowedTime, int maxIterations, double epsilon, Problem p, int precisonOfRealNumbers) {
        precisionOfRealNumbersInDecimalPlaces = precisonOfRealNumbers;
        stopCriteria = stop;
        maxEvaluations = eval;
        numberOfEvaluations = 0;
        this.epsilon = epsilon;
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

    public double[] getRandomVariables() {
        return p.getRandomVariables();
    }

    public DoubleSolution getRandomSolution() throws StopCriteriaException {

        if (stopCriteria == EnumStopCriteria.EVALUATIONS) {
            incEvaluate();
            long start = System.nanoTime();
            DoubleSolution tmpSolution = p.getRandomSolution();
            evaluationTime += System.nanoTime() - start;
            checkIfGlobalReached(tmpSolution.getEval());
            GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
            if (isAncestorLoggingEnabled) {
                tmpSolution.timeStamp = System.currentTimeMillis();
                tmpSolution.generationNumber = this.getNumberOfIterations();
                tmpSolution.evaluationNumber = this.getNumberOfEvaluations();
                ancestors.add(tmpSolution);
				/*ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
				ancestorSB.append("\n");*/
            }

            return tmpSolution;
        } else if (stopCriteria == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
            if (isGlobal)
                throw new StopCriteriaException("Global optimum already found");
            incEvaluate();
            long start = System.nanoTime();
            DoubleSolution tmpSolution = p.getRandomSolution();
            evaluationTime += System.nanoTime() - start;
            checkIfGlobalReached(tmpSolution.getEval());
            GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
            if (isAncestorLoggingEnabled) {
                ancestors.add(tmpSolution);
				/*ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
				ancestorSB.append("\n");*/
            }
            return tmpSolution;
        } else if (stopCriteria == EnumStopCriteria.ITERATIONS) {
            if (isStop)
                throw new StopCriteriaException("Max iterations");

            incEvaluate();
            long start = System.nanoTime();
            DoubleSolution tmpSolution = p.getRandomSolution();
            evaluationTime += System.nanoTime() - start;
            checkIfGlobalReached(tmpSolution.getEval());
            GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
            if (isAncestorLoggingEnabled) {
                ancestors.add(tmpSolution);
				/*ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
				ancestorSB.append("\n");*/
            }

            return tmpSolution;
        } else if (stopCriteria == EnumStopCriteria.CPU_TIME) {
            // check if the CPU time is not exceeded yet
            if (!isStop) {
                hasTheCPUTimeBeenExceeded(); // if CPU time is exceed allow last eval
                incEvaluate();
                long start = System.nanoTime();
                DoubleSolution tmpSolution = p.getRandomSolution();
                evaluationTime += System.nanoTime() - start;
                checkIfGlobalReached(tmpSolution.getEval());
                GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
                if (isAncestorLoggingEnabled) {
                    ancestors.add(tmpSolution);
					/*ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
					ancestorSB.append("\n");*/
                }

                return tmpSolution;
            } else {
                throw new StopCriteriaException("CPU Time");
            }
        } else if (stopCriteria == EnumStopCriteria.STAGNATION) {
            if (isStop)
                throw new StopCriteriaException("Solution stagnation");

            incEvaluate();
            long start = System.nanoTime();
            DoubleSolution tmpSolution = p.getRandomSolution();
            evaluationTime += System.nanoTime() - start;
            checkIfGlobalReached(tmpSolution.getEval());
            checkImprovement(tmpSolution.getEval());
            GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
            if (isAncestorLoggingEnabled) {
                ancestors.add(tmpSolution);
				/*ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
				ancestorSB.append("\n");*/
            }

            return tmpSolution;
        }

        return null;
    }

    /**
     * Checks if the provided vector is inside the interval given by the upper and lower limits
     *
     * @param x vector to be checked
     * @return true if the vector is inside interval, false otherwise
     */
    public boolean areDimensionsInFeasibleInterval(List<Double> x) {
        return p.areDimensionsInFeasibleInterval(x);
    }

    /**
     * Better use method eval returns Individual with calculated fitness and constrains
     *
     * @param ds real vector to be evaluated (just calc constraints)
     * @return
     * @deprecated
     */
    public double[] calcConstrains(List<Double> ds) {
        return p.computeConstraints(ds);
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

        for (int i = 0; i < solution.numberOfVariables(); i++) {
            if (!isFeasible(solution.getValue(i), i))
                return false;
        }
        return true;
    }

    public boolean isFeasible(double[] solution) {
        for (int i = 0; i < solution.length; i++) {
            if (!isFeasible(solution[i], i))
                return false;
        }
        return true;
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

    public DoubleSolution eval(double[] x) throws StopCriteriaException {

        //Double[] ds = ArrayUtils.toObject(x);
        List<Double> ds = Arrays.asList(ArrayUtils.toObject(x));
        DoubleSolution tmpSolution = null;
        if (stopCriteria == EnumStopCriteria.EVALUATIONS) {
            tmpSolution =  performEvaluation(ds);
        } else if (stopCriteria == EnumStopCriteria.ITERATIONS) {
            if (isStop)
                throw new StopCriteriaException("Max iterations");
            tmpSolution = performEvaluation(ds);
        } else if (stopCriteria == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
            if (isGlobal)
                throw new StopCriteriaException("Global optimum already found");
            tmpSolution =  performEvaluation(ds);
        } else if (stopCriteria == EnumStopCriteria.CPU_TIME) {
            if (!isStop) {
                hasTheCPUTimeBeenExceeded(); // if CPU time is exceed allow last eval
                tmpSolution = performEvaluation(ds);
            } else {
                throw new StopCriteriaException("CPU Time");
            }
        } else if (stopCriteria == EnumStopCriteria.STAGNATION) {
            if (isStop)
                throw new StopCriteriaException("Solution stagnation");

            tmpSolution = performEvaluation(ds);
        }
        if (tmpSolution!=null) {
            if (isAncestorLoggingEnabled) {
                tmpSolution.timeStamp = System.currentTimeMillis();
                tmpSolution.generationNumber = this.getNumberOfIterations();
                tmpSolution.evaluationNumber = this.getNumberOfEvaluations();
                ancestors.add(tmpSolution);
				/*ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
				ancestorSB.append("\n");*/
            }
            return tmpSolution;
        } else {
            assert false; // Execution should never reach this point!
            throw new StopCriteriaException("Not evaluated");
        }

    }

    private DoubleSolution performEvaluation(List<Double> ds) throws StopCriteriaException {

        incEvaluate();
        long start = System.nanoTime();
        DoubleSolution tmpSolution = new DoubleSolution(ds, p.eval(ds), p.computeConstraints(ds), p.upperLimit, p.lowerLimit);
        evaluationTime += System.nanoTime() - start;
        checkIfGlobalReached(tmpSolution.getEval());
        GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
        return tmpSolution;
    }

    private void checkIfGlobalReached(double d) {

        if (Math.abs(d - p.getGlobalOptimum()) <= epsilon) {
            isGlobal = true;
        }

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

    public DoubleSolution eval(double[] x, List<DoubleSolution> parents) throws StopCriteriaException {

        DoubleSolution tmpSolution = eval(x);

        if (isAncestorLoggingEnabled) {
            tmpSolution.parents = parents;
            tmpSolution.timeStamp = System.currentTimeMillis();
            tmpSolution.generationNumber = this.getNumberOfIterations();
            tmpSolution.evaluationNumber = this.getNumberOfEvaluations();
		/*	ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";[");
			for(int i = 0; i < parents.size(); i++)
			{
				ancestorSB.append(parents.get(i).getID());
				if(i+1 < parents.size())
					ancestorSB.append(",");
			}

			ancestorSB.append("]\n");*/
            ancestors.add(tmpSolution);
        }

        return tmpSolution;
    }

    protected void checkImprovement(double eval) {
        if (p.isMinimize()) {
            if (eval < bestEval) {
                bestEval = eval;
                stagnationTrials = 0;
            } else {
                stagnationTrials++;
            }
        } else {
            if (eval > bestEval) {
                bestEval = eval;
                stagnationTrials = 0;
            } else {
                stagnationTrials++;
            }
        }

        if (stagnationTrials >= maxEvaluationsBeforeStagnation) {
            isStop = true;
        }
    }

    public static void resetLoggingID() {
        ancestors.clear();
        SolutionBase.resetLoggingID();
    }

    public DoubleSolution eval(DoubleSolution newSolution) throws StopCriteriaException {
        return eval(newSolution.getDoubleVariables());
    }
}
