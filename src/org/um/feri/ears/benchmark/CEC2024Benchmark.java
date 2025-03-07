package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.cec2017.*;

public class CEC2024Benchmark extends SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {

    public CEC2024Benchmark() {
        this(1e-7);
    }

    public CEC2024Benchmark(double drawLimit) {
        super();
        name = "Benchmark CEC 2024";
        this.drawLimit = drawLimit;
        dimension = 30;
        maxEvaluations = dimension * 10_000;
        timeLimit = 2500;
        maxIterations = 2500;
        stopCriterion = StopCriterion.EVALUATIONS;
    }

    @Override
    protected void addTask(DoubleProblem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    @Override
    public void initAllProblems() {

        addTask(new F1(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F2(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F3(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F4(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F5(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F6(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F7(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F8(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F9(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F10(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F11(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F12(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F13(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F14(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F15(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F16(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F17(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F18(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F19(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F20(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F21(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F22(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F23(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F24(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F25(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F26(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F27(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F28(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
        addTask(new F29(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations);
    }
}