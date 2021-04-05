package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.*;

public class RPUOed2Benchmark extends Benchmark {
    public RPUOed2Benchmark() {
        super();
        name="Real Parameter Unconstrained Optimization Problems with maximum evaluation condition";
        shortName = "RPUOed2";
        stopCriterion = StopCriterion.EVALUATIONS;
        timeLimit = 10;
        maxEvaluations=10000;
        maxIterations = 500; 
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,"2");
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations*2));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+ drawLimit);
    }

    @Override
    protected void addTask(Problem problem, StopCriterion stopCriterion, int eval, long time, int maxIterations, double epsilon) {
        tasks.add(new Task(problem, stopCriterion, eval, time, maxIterations));
    }
    
    @Override
    protected void initAllProblems() {
        addTask(new Ackley1(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new Bohachevsky1(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new Beale(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new Booth(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new Branin1(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new Easom(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new GoldsteinPrice(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new Griewank(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new MartinAndGaddy(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new PowellBadlyScaledFunction(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new Rastrigin(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new RosenbrockDeJong2(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new Schwefel226(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new SchwefelRidge(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        addTask(new Sphere(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
    }
        
    @Override
    public String getName() {
        return name + "("+ getParameters().get(EnumBenchmarkInfoParameters.DIMENSION)+")";
    }
}
