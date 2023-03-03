package org.um.feri.ears.tuning;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.*;

import java.util.Vector;

public class TuningBenchmark extends Benchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {

    protected int evaluationsOnDimension = 10000;
    protected int dimension = 10;
    private double draw_limit = 0.000001;
    public static final Vector<String[]> optimums = new Vector();


    public TuningBenchmark() {
        this(0.0000001);
    }

    public TuningBenchmark(double draw_limit) {
        super();
        name = "Tuning 1";
        this.draw_limit = draw_limit;
        evaluationsOnDimension = 10000;
        dimension = 10;
    }

    public TuningBenchmark(double draw_limit, int aDim, int aEval) {
        super();
        this.draw_limit = draw_limit;
        evaluationsOnDimension = aEval;
        dimension = aDim;
    }

    @Override
    protected void addTask(DoubleProblem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, time, maxIterations));
        String[] optimum = new String[2];
        optimum[0] = problem.getName();
        optimum[1] = problem.getGlobalOptima() + "";
        optimums.add(optimum);
    }

    @Override
    public void initAllProblems() {
        addTask(new Sphere(dimension), stopCriterion, evaluationsOnDimension, 1000, 500);      // f1
        addTask(new RosenbrockDeJong2(dimension), stopCriterion, evaluationsOnDimension, 1000, 500);  // f2
        addTask(new Step2(dimension), stopCriterion, evaluationsOnDimension, 1000, 500);        // f3
        addTask(new Schaffer1(), stopCriterion, evaluationsOnDimension, 1000, 500);    // f4
        addTask(new Rastrigin(dimension), stopCriterion, evaluationsOnDimension, 1000, 500);   // f5
        addTask(new Schwefel226(dimension), stopCriterion, evaluationsOnDimension, 1000, 500);    // f6
        addTask(new Griewank(dimension), stopCriterion, evaluationsOnDimension, 1000, 500);    // f7
        addTask(new Ackley1(dimension), stopCriterion, evaluationsOnDimension, 1000, 500);      // f8
    }
}
