package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.*;

public class RatingRPUOed30 extends RatingBenchmark {

    public RatingRPUOed30() {
        super();
        name="Real Parameter Unconstrained Optimization Problems with maximum evaluation condition";
        shortName = "RPUOed30";
        maxIterations = 0;
        dimension = 30;
        maxEvaluations = 100000;
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,String.valueOf(dimension));
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+ drawLimit);
    }

    @Override
    protected void registerTask(Problem p, EnumStopCriterion sc, int eval, long time, int maxIterations, double epsilon) {
        tasks.add(new Task(sc, eval, time, maxIterations, epsilon, p));
    }

    @Override
    protected void initFullProblemList() {
        registerTask(new Ackley1(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        registerTask(new Griewank(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        registerTask(new Rastrigin(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        registerTask(new RosenbrockDeJong2(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        registerTask(new Schwefel226(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        registerTask(new SchwefelRidge(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        registerTask(new Sphere(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
    }
        
    @Override
    public String getName() {
        return name + "("+ getParameters().get(EnumBenchmarkInfoParameters.DIMENSION)+")";
    }
}
