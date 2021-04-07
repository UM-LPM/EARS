package org.um.feri.ears.tuning;

import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.EnumBenchmarkInfoParameters;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.*;

import java.util.Vector;

public class TuningBenchmark extends Benchmark {

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
        addParameter(EnumBenchmarkInfoParameters.DIMENSION, "10");
        addParameter(EnumBenchmarkInfoParameters.EVAL, String.valueOf(evaluationsOnDimension));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM, "abs(evaluation_diff) < " + draw_limit);
    }

    public TuningBenchmark(double draw_limit, int aDim, int aEval) {
        super();
        this.draw_limit = draw_limit;
        evaluationsOnDimension = aEval;
        dimension = aDim;
        addParameter(EnumBenchmarkInfoParameters.DIMENSION, String.valueOf(dimension));
        addParameter(EnumBenchmarkInfoParameters.EVAL, String.valueOf(evaluationsOnDimension));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM, "abs(evaluation_diff) < " + draw_limit);
    }

    @Override
    protected void addTask(Problem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations,
                           double epsilon) {
        tasks.add(new Task(problem, stopCriterion, maxEvaluations, time, maxIterations, epsilon));
        String[] optimum = new String[2];
        optimum[0] = problem.getName();
        optimum[1] = problem.getGlobalOptimum() + "";
        optimums.add(optimum);
    }

    @Override
    protected void initAllProblems() {
        addTask(new Sphere(dimension), stopCriterion, evaluationsOnDimension, 1000, 500, draw_limit);      // f1
        addTask(new RosenbrockDeJong2(dimension), stopCriterion, evaluationsOnDimension, 1000, 500, draw_limit);  // f2
        addTask(new Step1(dimension), stopCriterion, evaluationsOnDimension, 1000, 500, draw_limit);        // f3
        addTask(new Schaffer1(), stopCriterion, evaluationsOnDimension, 1000, 500, draw_limit);    // f4
        addTask(new Rastrigin(dimension), stopCriterion, evaluationsOnDimension, 1000, 500, draw_limit);   // f5
        addTask(new Schwefel226(dimension), stopCriterion, evaluationsOnDimension, 1000, 500, draw_limit);    // f6
        addTask(new Griewank(dimension), stopCriterion, evaluationsOnDimension, 1000, 500, draw_limit);    // f7
        addTask(new Ackley1(dimension), stopCriterion, evaluationsOnDimension, 1000, 500, draw_limit);      // f8
    }
}
