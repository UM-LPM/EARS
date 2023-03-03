package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.*;

public class RPUOed30Benchmark extends Benchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {

    public RPUOed30Benchmark() {
        super();
        name="Real Parameter Unconstrained Optimization Problems with maximum evaluation condition";
        shortName = "RPUOed30";
        maxIterations = 0;
        dimension = 30;
        maxEvaluations = 100000;
    }

    @Override
    protected void addTask(DoubleProblem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, time, maxIterations));
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
