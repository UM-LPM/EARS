package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.cec2015.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class CEC2015Benchmark extends Benchmark {
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
        /*addParameter(EnumBenchmarkInfoParameters.STOPPING_CRITERIA,""+stopCriterion);
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,""+dimension);
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+drawLimit);*/
    }

    @Override
    protected void addTask(Problem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations, double epsilon) {
        tasks.add(new Task(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    @Override
    public int getNumberOfRuns() {
        //number of runs set to 10 to reduce server execution time
        return 10;
    }

    @Override
    protected void initAllProblems() {

        ArrayList<Problem> problems = new ArrayList<Problem>();

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

        for (Problem p : problems) {
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

            addTask(p, stopCriterion, maxEvaluations, timeLimit, maxIterations, optimumEpsilon);
        }
    }

    private long calculateTime(Problem p) {

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
