package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.memory.DuplicationRemovalStrategyRandom;
import org.um.feri.ears.memory.TaskWithMemory;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.*;

public class RPUOed30MemoryBenchmark extends Benchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {
    int precision;
    int maxHits;

    public RPUOed30MemoryBenchmark() {
        this(30, 30000, 4, 4);
    }

    public RPUOed30MemoryBenchmark(int D, int EV, int prec, int maxHits) {
        super();
        name = "Real Parameter Unconstrained Optimization Problems with maximum evaluation condition memory";
        shortName = "RPUOed30Mem";
        maxEvaluations = EV;
        maxIterations = 0;
        dimension = D;
        precision = prec;
    }

    @Override
    protected void addTask(DoubleProblem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new TaskWithMemory(stopCriterion, maxEvaluations, time, maxIterations,0.00001, problem, precision, new DuplicationRemovalStrategyRandom(maxHits)));
    }

    @Override
    public void initAllProblems() {
        addTask(new Ackley1(dimension), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new Griewank(dimension), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new Rastrigin(dimension), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new RosenbrockDeJong2(dimension), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new Schwefel226(dimension), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new SchwefelRidge(dimension), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new Sphere(dimension), stopCriterion, maxEvaluations, 0, maxIterations);
    }

    @Override
    public String getName() {
        return name + "  D=" + dimension;
    }
}
