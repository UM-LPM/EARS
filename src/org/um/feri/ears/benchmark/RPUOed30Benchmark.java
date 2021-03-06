package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.*;

public class RPUOed30Benchmark extends Benchmark {

    public RPUOed30Benchmark() {
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
    protected void addTask(Problem p, EnumStopCriterion sc, int eval, long time, int maxIterations, double epsilon) {
        tasks.add(new Task(sc, eval, time, maxIterations, epsilon, p));
    }

    @Override
    protected void initAllProblems() {
        addTask(new Ackley1(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new Griewank(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new Rastrigin(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new RosenbrockDeJong2(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new Schwefel226(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new SchwefelRidge(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
        addTask(new Sphere(dimension), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
    }
        
    @Override
    public String getName() {
        return name + "("+ getParameters().get(EnumBenchmarkInfoParameters.DIMENSION)+")";
    }
}
