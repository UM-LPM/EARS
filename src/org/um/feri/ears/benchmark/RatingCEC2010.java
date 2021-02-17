package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.cec2010.*;

public class RatingCEC2010 extends RatingBenchmark {
    protected int dimension = 1000; //recommended

    public RatingCEC2010() {
        this(1e-7);
    }

    public RatingCEC2010(double draw_limit) {
        super();
        String name = "Benchmark CEC 2010";
        this.drawLimit = draw_limit;
        stopCriterion = EnumStopCriterion.EVALUATIONS;
        maxEvaluations = 10000; //(int) (1 * 1e6);
        maxIterations = 0;
        addParameter(EnumBenchmarkInfoParameters.DIMENSION, "" + dimension);
        addParameter(EnumBenchmarkInfoParameters.EVAL, String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM, "abs(evaluation_diff) < " + draw_limit);
    }

    @Override
    protected void registerTask(Problem p, EnumStopCriterion sc, int eval, long time, int maxIterations, double epsilon) {
        tasks.add(new Task(sc, eval, time, maxIterations, epsilon, p));
    }

    @Override
    protected void initFullProblemList() {

        registerTask(new F1(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F2(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F3(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F4(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F5(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F6(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F7(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F8(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F9(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F10(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F11(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F12(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F13(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F14(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F15(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F16(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F17(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F18(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F19(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new F20(dimension), stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001);
    }
}
