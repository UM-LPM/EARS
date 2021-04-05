package org.um.feri.ears.experiment.ee;

import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.EnumBenchmarkInfoParameters;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.Ackley1;
import org.um.feri.ears.problems.unconstrained.Griewank;
import org.um.feri.ears.problems.unconstrained.Rastrigin;
import org.um.feri.ears.problems.unconstrained.RosenbrockDeJong2;
import org.um.feri.ears.problems.unconstrained.Schwefel226;
import org.um.feri.ears.problems.unconstrained.SchwefelRidge;
import org.um.feri.ears.problems.unconstrained.Sphere;

public class BenchmarkEE extends Benchmark {
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
        addParameter(EnumBenchmarkInfoParameters.DIMENSION, String.valueOf(D));
        addParameter(EnumBenchmarkInfoParameters.EVAL, String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM, "abs(evaluation_diff) < " + drawLimit);
    }

    @Override
    protected void addTask(Problem p, StopCriterion sc, int eval, long time, int maxIterations, double epsilon) {
        tasks.add(new Task(sc, eval, time, maxIterations, epsilon, p));
    }

    @Override
    protected void initAllProblems() {
        addTask(new Ackley1(dim), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new Griewank(dim), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new Rastrigin(dim), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new RosenbrockDeJong2(dim), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new Schwefel226(dim), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new SchwefelRidge(dim), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new Sphere(dim), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
    }
}
