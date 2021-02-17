package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.*;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;

import java.util.ArrayList;
import java.util.List;

public class BIOMABenchmark extends MORatingBenchmark<Double, DoubleMOTask, DoubleMOProblem> {

    public BIOMABenchmark() {
        this(null, 0.0000001, true);
        List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD); //Default indicator
        this.indicators = indicators;
    }

    public BIOMABenchmark(List<IndicatorName> indicators, double drawLimit, boolean randomIndicator) {
        super(indicators);
        name = "BIOMA Benchmark";
        this.randomIndicator = randomIndicator;
        this.drawLimit = drawLimit;
        maxEvaluations = 300000;
        stopCriterion = EnumStopCriterion.EVALUATIONS;
        maxIterations = 500;
        timeLimit = 5000; //millisecnods
        addParameter(EnumBenchmarkInfoParameters.EVAL, String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM, "abs(evaluation_diff) < " + drawLimit);
    }

    @Override
    protected void registerTask(EnumStopCriterion sc, int eval, long allowedTime, int maxIterations, double epsilon, DoubleMOProblem p) {
        tasks.add(new DoubleMOTask(sc, eval, allowedTime, maxIterations, epsilon, p));
    }

    @Override
    protected void initFullProblemList() {

        ArrayList<DoubleMOProblem> problems = new ArrayList<DoubleMOProblem>();

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

        for (DoubleMOProblem moProblem : problems) {
            registerTask(stopCriterion, maxEvaluations, timeLimit, maxIterations, 1.0E-4, moProblem);
        }
    }
}
