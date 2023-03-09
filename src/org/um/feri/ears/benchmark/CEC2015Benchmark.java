package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.cec2015.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class CEC2015Benchmark extends SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {
    protected boolean calculateTime = false;
    protected int warmupIterations = 10000;
    private double optimumEpsilon = 0.000001;

    public CEC2015Benchmark() {
        this(1e-7);
    }

    public CEC2015Benchmark(double drawLimit) {
        super();
        name = "Benchmark CEC 2015";
        this.drawLimit = drawLimit;
        maxEvaluations = 300000;
        dimension = 30;
        timeLimit = 2500;
        maxIterations = 2500;
        stopCriterion = StopCriterion.EVALUATIONS;
    }

    @Override
    protected void addTask(DoubleProblem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    @Override
    public int getNumberOfRuns() {
        //number of runs set to 10 to reduce server execution time
        return 10;
    }

    @Override
    public void initAllProblems() {

        ArrayList<DoubleProblem> problems = new ArrayList<>();

        problems.add(new F1(dimension));
        problems.add(new F2(dimension));
        problems.add(new F3(dimension));
        problems.add(new F4(dimension));
        problems.add(new F5(dimension));
        problems.add(new F6(dimension));
        problems.add(new F7(dimension));
        problems.add(new F8(dimension));
        problems.add(new F9(dimension));
        problems.add(new F10(dimension));
        problems.add(new F11(dimension));
        problems.add(new F12(dimension));
        problems.add(new F13(dimension));
        problems.add(new F14(dimension));
        problems.add(new F15(dimension));

        for (DoubleProblem p : problems) {
    		/*if(stopCriterion == EnumStopCriterion.CPU_TIME && calculateTime)
    		{
    			System.out.println("Calculating time for problem: "+p.getName());
    			timeLimit = calculateTime(p);
    		}*/

            if (stopCriterion == StopCriterion.CPU_TIME) {
                for (int i = 0; i < warmupIterations; i++) {
                    p.getRandomEvaluatedSolution();
                }
            }

            addTask(p, stopCriterion, maxEvaluations, timeLimit, maxIterations);
        }
    }

    private long calculateTime(DoubleProblem p) {

        long start = System.nanoTime();
        long duration;
        for (int i = 0; i < maxEvaluations; i++) {
            p.getRandomEvaluatedSolution();
        }
        duration = System.nanoTime() - start;
        // add algorithm runtime
        duration += (int) (duration * (10.0f / 100.0f));

        return TimeUnit.NANOSECONDS.toMillis(duration);
    }
}
