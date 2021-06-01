package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.*;

import java.util.ArrayList;

public class ThirtySixFunctionsBenchmark extends Benchmark {

    private double optimumEpsilon = 0.000001;
    public ThirtySixFunctionsBenchmark() {
        super();
        name="Benchmark containing 36 functions from the paper: A conceptual comparison of several metaheuristic algorithms on continuous optimisation problems";
        shortName = "36Functions";
        maxIterations = 0;
        dimension = 30;
        maxEvaluations = 100000;
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,String.valueOf(dimension));
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+ drawLimit);
    }

    @Override
    protected void addTask(Problem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations, double epsilon) {
        tasks.add(new Task(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    @Override
    protected void initAllProblems() {
        ArrayList<Problem> problems = new ArrayList<Problem>();

        problems.add(new Beale());
        problems.add(new Easom()); //wrong global minimum in paper
        problems.add(new Matyas());
        problems.add(new Bohachevsky1());
        problems.add(new Booth());
        problems.add(new Michalewicz2());
        problems.add(new Schaffer1());
        problems.add(new SixHumpCamelBack()); //x1[-3,3], x2[-2,2]
        problems.add(new Bohachevsky2());
        problems.add(new Bohachevsky3());
        problems.add(new Shubert1(2));
        problems.add(new Colville());
        problems.add(new Michalewicz5());
        problems.add(new Zakharov(30));
        problems.add(new Michalewicz10());
        problems.add(new Step1(30)); //x[-5.12,5.12]
        problems.add(new Sphere(30));
        problems.add(new SumSquares(30));
        problems.add(new Quartic(30)); //paper uses noise
        problems.add(new Schwefel222(30)); //x[-10,10]
        problems.add(new Schwefel12());
        problems.add(new RosenbrockDeJong2(30)); //x[-30,30]
        problems.add(new DixonPrice(30));
        problems.add(new Rastrigin(30));
        problems.add(new Griewank(30)); // x - 100 in paper's equation
        problems.add(new Ackley1(30)); // x - 100 in paper's equation
        problems.add(new DropWave());
        problems.add(new Hartman3());
        problems.add(new Hartman6()); //error in table Hartman10 instead of Hartman6
        problems.add(new Shekel5());
        problems.add(new Shekel7());
        problems.add(new Shekel10());
        problems.add(new Branin1());
        problems.add(new GoldsteinPrice());
        problems.add(new Salomon(5));
        problems.add(new Salomon(6)); //error in table Salomon10 instead of Salomon6

        for (Problem p : problems) {
            addTask(p, stopCriterion, maxEvaluations, timeLimit, maxIterations, optimumEpsilon);
        }
    }

    @Override
    public String getName() {
        return name + "("+ getParameters().get(EnumBenchmarkInfoParameters.DIMENSION)+")";
    }
}