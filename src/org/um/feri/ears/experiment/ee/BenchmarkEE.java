package org.um.feri.ears.experiment.ee;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.benchmark.SOBenchmark;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.Ackley1;
import org.um.feri.ears.problems.unconstrained.Griewank;
import org.um.feri.ears.problems.unconstrained.Rastrigin;
import org.um.feri.ears.problems.unconstrained.RosenbrockDeJong2;
import org.um.feri.ears.problems.unconstrained.Schwefel226;
import org.um.feri.ears.problems.unconstrained.SchwefelRidge;
import org.um.feri.ears.problems.unconstrained.Sphere;

public class BenchmarkEE extends SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {
    int dim;

    public BenchmarkEE() {
        this(10, 1);
    }

    public BenchmarkEE(int D, int EV) {
        super();
        name = "Real Parameter Unconstrained Optimization Problems with maximum evaluation condition";
        shortName = "RPUOed30";
        maxEvaluations = 10000;
        dim = D;
        maxIterations = 0;
    }

    @Override
    protected void addTask(DoubleProblem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    @Override
    public void initAllProblems() {
        addTask(new Ackley1(dim), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new Griewank(dim), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new Rastrigin(dim), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new RosenbrockDeJong2(dim), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new Schwefel226(dim), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new SchwefelRidge(dim), stopCriterion, maxEvaluations, 0, maxIterations);
        addTask(new Sphere(dim), stopCriterion, maxEvaluations, 0, maxIterations);
    }
}
