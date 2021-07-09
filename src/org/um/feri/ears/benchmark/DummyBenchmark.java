package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.DummyProblem;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;

public class DummyBenchmark extends Benchmark {
    protected int dimension;

    public DummyBenchmark() {
        this(0.000001);
    }

    public DummyBenchmark(double drawLimit) {
        super();
        name = "Dummy benchmark";
        this.drawLimit = 1e-6;
        maxEvaluations = 3000;
        dimension = 3;
        maxIterations = 0;
        addParameter(EnumBenchmarkInfoParameters.DIMENSION, "3");
        addParameter(EnumBenchmarkInfoParameters.EVAL, String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM, "abs(evaluation_diff) < " + drawLimit);
    }

    @Override
    protected void addTask(Problem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations, double epsilon) {
        tasks.add(new Task(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    public void addDummyTask(String name) {
        addTask(new DummyProblem(name), stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
    }

    @Override
    public void initAllProblems() {
    	
    	
    	/*registerTask(new DummyProblem("i3_te1"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("i3_te2"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("i3_te3"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);*/
    	
    	/*registerTask(new DummyProblem("f1"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f2"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f3"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f4"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f5"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f6"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f7"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f8"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f9"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f10"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f11"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f12"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f13"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f14"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f15"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f16"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f17"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f18"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f19"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f20"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f21"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f22"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);*/

        //Grouped by method
    	/*
    	registerTask(new DummyProblem("en300_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("en300_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("en300_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("en300_c4"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c4"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c4"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c4"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c4"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c4"),stopCriterion, evaluationsOnDimension, 0.001);*/
    	/*
    	registerTask(new DummyProblem("en300_c5"),stopCriterion,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("1HZ_c5"),stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("50HZ_c5"),stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("300HZ_c5"),stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("750HZ_c5"),stopCriterion, maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("9s20_c5"),stopCriterion, maxEvaluations, 0, maxIterations, 0.001);*/


        // Grouped by material
    	/*
    	registerTask(new DummyProblem("en300_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("en300_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("en300_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("en300_c4"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("en300_c5"),stopCriterion, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("1HZ_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c4"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c5"),stopCriterion, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("50HZ_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c4"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c5"),stopCriterion, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("300HZ_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c4"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c5"),stopCriterion, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("750HZ_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c4"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c5"),stopCriterion, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("9s20_c1"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c2"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c3"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c4"),stopCriterion, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c5"),stopCriterion, evaluationsOnDimension, 0.001);*/
    }
}
