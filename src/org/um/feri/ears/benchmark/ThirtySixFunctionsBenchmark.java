package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.*;

import java.util.ArrayList;

public class ThirtySixFunctionsBenchmark extends SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {

    public ThirtySixFunctionsBenchmark() {
        super();
        name="Benchmark containing 36 functions from the paper: A conceptual comparison of several metaheuristic algorithms on continuous optimisation problems";
        shortName = "36Functions";
        maxIterations = 0;
        dimension = 30;
        maxEvaluations = 100000;
    }

    @Override
    protected void addTask(DoubleProblem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    @Override
    public void initAllProblems() {
        ArrayList<DoubleProblem> problems = new ArrayList<>();

        problems.add(new Ackley1(30)); // x - 100 in paper's equation
        problems.add(new Beale());
        problems.add(new Bohachevsky1());
        problems.add(new Bohachevsky2());
        problems.add(new Bohachevsky3());
        problems.add(new Booth());
        problems.add(new Branin1());
        problems.add(new Colville());
        problems.add(new DixonPrice(30));
        problems.add(new DropWave());
        problems.add(new Easom());
        problems.add(new GoldsteinPrice());
        problems.add(new Griewank(30)); // x - 100 in paper's equation
        problems.add(new Hartman3());
        problems.add(new Hartman6());
        problems.add(new Matyas());
        problems.add(new Michalewicz2());
        problems.add(new Michalewicz5());
        problems.add(new Michalewicz10());
        problems.add(new Quartic(30)); //paper uses noise
        problems.add(new Rastrigin(30));
        problems.add(new RosenbrockDeJong2(30)); //x[-30,30]
        problems.add(new Salomon(5));
        problems.add(new Salomon(6));
        problems.add(new Schaffer1());
        problems.add(new Schwefel222(30)); //x[-10,10]
        problems.add(new Schwefel12(2));
        problems.add(new Shekel5());
        problems.add(new Shekel7());
        problems.add(new Shekel10());
        problems.add(new Shubert1(2));
        problems.add(new SixHumpCamelBack()); //x1[-3,3], x2[-2,2]
        problems.add(new Sphere(30));
        problems.add(new Step2(30)); //x[-5.12,5.12]
        problems.add(new SumOfSquares(30));
        problems.add(new Zakharov(30));

        for (DoubleProblem p : problems) {
            addTask(p, stopCriterion, maxEvaluations, timeLimit, maxIterations);
        }
    }

    @Override
    public String getName() {
        return name + "  D="+ dimension;
    }
}