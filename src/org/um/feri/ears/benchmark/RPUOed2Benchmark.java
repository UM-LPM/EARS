package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.*;

public class RPUOed2Benchmark extends Benchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {
    public RPUOed2Benchmark() {
        super();
        name="Real Parameter Unconstrained Optimization Problems with maximum evaluation condition";
        shortName = "RPUOed2";
        stopCriterion = StopCriterion.EVALUATIONS;
        timeLimit = 10;
        maxEvaluations=10000;
        maxIterations = 500; 
    }

    @Override
    protected void addTask(DoubleProblem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }
    
    @Override
    public void initAllProblems() {
        addTask(new Ackley1(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new Bohachevsky1(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new Beale(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new Booth(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new Branin1(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new Easom(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new GoldsteinPrice(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new Griewank(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new MartinAndGaddy(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new PowellBadlyScaledFunction(), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new Rastrigin(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new RosenbrockDeJong2(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new Schwefel226(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new SchwefelRidge(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
        addTask(new Sphere(2), stopCriterion, 2*maxEvaluations, timeLimit, maxIterations);
    }
        
    @Override
    public String getName() {
        return name + " D=" + dimension;
    }
}
