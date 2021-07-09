package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.cec2014.*;


public class CEC2014Benchamrk extends Benchmark {
    protected int dimension;

    public CEC2014Benchamrk() {
        this(1e-7);
    }

    public CEC2014Benchamrk(double drawLimit) {
        super();
        name = "Benchmark CEC 2014";
        this.drawLimit = drawLimit;
        maxEvaluations = 30000;
        dimension = 30;
        timeLimit = 0;
        maxIterations = 0;
        addParameter(EnumBenchmarkInfoParameters.DIMENSION, "" + dimension);
        addParameter(EnumBenchmarkInfoParameters.EVAL, String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM, "abs(evaluation_diff) < " + drawLimit);
    }

    @Override
    protected void addTask(Problem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations, double epsilon) {
        tasks.add(new Task(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    @Override
    public void initAllProblems() {

        addTask(new F1(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F2(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F3(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F4(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F5(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F6(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F7(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F8(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F9(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F10(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F11(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F12(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F13(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F14(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F15(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F16(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F17(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F18(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F19(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F20(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F21(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F22(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F23(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F24(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F25(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F26(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F27(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F28(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F29(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new F30(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);

    }
}