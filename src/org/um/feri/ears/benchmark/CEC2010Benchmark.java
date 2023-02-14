package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.cec2010.*;

public class CEC2010Benchmark extends Benchmark<NumberSolution<Double>, DoubleProblem, Task<NumberSolution<Double>, DoubleProblem>, NumberAlgorithm> {
    protected int dimension = 1000; //recommended

    public CEC2010Benchmark() {
        this(1e-7);
    }

    public CEC2010Benchmark(double draw_limit) {
        super();
        String name = "Benchmark CEC 2010";
        this.drawLimit = draw_limit;
        stopCriterion = StopCriterion.EVALUATIONS;
        maxEvaluations = 10000; //(int) (1 * 1e6);
        maxIterations = 0;
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
    }
}
