package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.*;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;

import java.util.ArrayList;
import java.util.List;

public class BIOMABenchmark extends MOBenchmark<Double, NumberSolution<Double>, DoubleProblem, Task<NumberSolution<Double>, DoubleProblem>> {

    public BIOMABenchmark() {
        this(null, 0.0000001, true);
        List<IndicatorName> indicators = new ArrayList<>();
        indicators.add(IndicatorName.IGD); //Default indicator
        this.indicators = indicators;
    }

    public BIOMABenchmark(List<IndicatorName> indicators, double drawLimit, boolean randomIndicator) {
        super(indicators);
        name = "BIOMA Benchmark";
        this.randomIndicator = randomIndicator;
        this.drawLimit = drawLimit;
        maxEvaluations = 300000;
        stopCriterion = StopCriterion.EVALUATIONS;
        maxIterations = 500;
        timeLimit = 5000; //milliseconds
    }

    @Override
    protected void addTask(StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, DoubleProblem problem) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations));
    }

    @Override
    public void initAllProblems() {

        ArrayList<DoubleProblem> problems = new ArrayList<>();

        problems.add(new UnconstrainedProblem1());
        problems.add(new UnconstrainedProblem2());
        problems.add(new UnconstrainedProblem3());
        problems.add(new UnconstrainedProblem4());
        problems.add(new UnconstrainedProblem5());
        problems.add(new UnconstrainedProblem6());
        problems.add(new UnconstrainedProblem7());
        problems.add(new UnconstrainedProblem8());
        problems.add(new UnconstrainedProblem9());
        problems.add(new UnconstrainedProblem10());

        for (DoubleProblem moProblem : problems) {
            addTask(stopCriterion, maxEvaluations, timeLimit, maxIterations, moProblem);
        }
    }
}
