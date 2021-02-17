package org.um.feri.ears.benchmark;

import org.um.feri.ears.memory.DuplicationRemovalStrategyRandom;
import org.um.feri.ears.memory.TaskWithMemory;
import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.unconstrained.*;

public class RatingRPUOed30Memory extends RatingBenchmark {
    int precision;
    int maxHits;

    public RatingRPUOed30Memory() {
        this(30, 30000, 4, 4);
    }

    public RatingRPUOed30Memory(int D, int EV, int prec, int maxHits) {
        super();
        name = "Real Parameter Unconstrained Optimization Problems with maximum evaluation condition memory";
        shortName = "RPUOed30Mem";
        maxEvaluations = EV;
        maxIterations = 0;
        dimension = D;
        precision = prec;
        addParameter(EnumBenchmarkInfoParameters.DIMENSION, String.valueOf(D));
        addParameter(EnumBenchmarkInfoParameters.EVAL, String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM, "abs(evaluation_diff) < " + drawLimit);
    }

    @Override
    protected void registerTask(Problem p, EnumStopCriterion sc, int eval, long time, int maxIterations, double epsilon) {
        tasks.add(new TaskWithMemory(sc, eval, time, maxIterations, epsilon, p, precision, new DuplicationRemovalStrategyRandom(maxHits)));
    }

    @Override
    protected void initFullProblemList() {
        registerTask(new Ackley1(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.0001);
        registerTask(new Griewank(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.0001);
        registerTask(new Rastrigin(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.0001);
        registerTask(new RosenbrockDeJong2(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.0001);
        registerTask(new Schwefel226(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.0001);
        registerTask(new SchwefelRidge(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.0001);
        registerTask(new Sphere(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.0001);
    }

    @Override
    public String getName() {
        return name + "(" + getParameters().get(EnumBenchmarkInfoParameters.DIMENSION) + ")";
    }
}
