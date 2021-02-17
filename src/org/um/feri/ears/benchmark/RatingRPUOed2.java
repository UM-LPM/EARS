package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.*;

public class RatingRPUOed2 extends RatingBenchmark {
    public RatingRPUOed2() {
        super();
        name="Real Parameter Unconstrained Optimization Problems with maximum evaluation condition";
        shortName = "RPUOed2";
        stopCriterion = EnumStopCriterion.EVALUATIONS;
        timeLimit = 10;
        maxEvaluations=10000;
        maxIterations = 500; 
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,"2");
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations*2));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+ drawLimit);
    }

    @Override
    protected void registerTask(Problem p, EnumStopCriterion sc, int eval, long time, int maxIterations, double epsilon) {
        tasks.add(new Task(sc, eval, time, maxIterations, epsilon, p));
    }
    
    @Override
    protected void initFullProblemList() {
        registerTask(new Ackley1(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Bohachevsky1(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Beale(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Booth(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Branin1(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Easom(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new GoldsteinPrice(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Griewank(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new MartinAndGaddy(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new PowellBadlyScaledFunction(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Rastrigin(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new RosenbrockDeJong2(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Schwefel226(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new SchwefelRidge(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Sphere(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
    }
        
    @Override
    public String getName() {
        return name + "("+ getParameters().get(EnumBenchmarkInfoParameters.DIMENSION)+")";
    }
}
