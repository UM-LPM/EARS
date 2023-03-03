package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem1;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;

import java.util.ArrayList;
import java.util.List;

public class CEC2009Benchmark extends MOBenchmark<Double> {

    public CEC2009Benchmark() {
        this(null, 0.0000001);
        List<IndicatorName> indicators = new ArrayList<>();
        indicators.add(IndicatorName.IGD); //Default indicator
        this.indicators = indicators;
    }

    public CEC2009Benchmark(List<IndicatorName> indicators, double drawLimit) {
        super(indicators);
        name = "Benchmark CEC 2009";
        this.drawLimit = drawLimit;
        maxEvaluations = 30000;
    }

    @Override
    protected void addTask(StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, NumberProblem<Double> problem) {
        tasks.add(new Task<>(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations));
    }

    @Override
    public void initAllProblems() {

        ArrayList<DoubleProblem> problems = new ArrayList<>();

        problems.add(new UnconstrainedProblem1());
    	/*problems.add(new UnconstrainedProblem2());
    	problems.add(new UnconstrainedProblem3());
    	problems.add(new UnconstrainedProblem4());
    	problems.add(new UnconstrainedProblem5());
    	problems.add(new UnconstrainedProblem6());
    	problems.add(new UnconstrainedProblem7());
    	problems.add(new UnconstrainedProblem8());
    	problems.add(new UnconstrainedProblem9());
    	problems.add(new UnconstrainedProblem10());*/

        for (DoubleProblem moProblem : problems) {
            addTask(stopCriterion, maxEvaluations, timeLimit, maxIterations, moProblem);
        }
    }
}
