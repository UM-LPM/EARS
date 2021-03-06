package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.moo.IntegerMOProblem;
import org.um.feri.ears.problems.moo.real_world.CITOProblem;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;

import java.util.ArrayList;
import java.util.List;

public class CITOBenchmark extends MOBenchmark<Integer, IntegerMOTask, IntegerMOProblem> {

    public CITOBenchmark() {
        this(null, 0.0000001, true);
        List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD); //Default indicator
        this.indicators = indicators;
    }

    public CITOBenchmark(List<IndicatorName> indicators, double drawLimit, boolean randomIndicator) {
        super(indicators);
        name = "CITO Benchmark";
        this.randomIndicator = randomIndicator;
        this.drawLimit = drawLimit;
        maxEvaluations = 60000;
        addParameter(EnumBenchmarkInfoParameters.EVAL, String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM, "abs(evaluation_diff) < " + drawLimit);
    }

    @Override
    protected void addTask(EnumStopCriterion sc, int eval, long allowedTime, int maxIterations, double epsilon, IntegerMOProblem p) {
        tasks.add(new IntegerMOTask(sc, eval, allowedTime, maxIterations, epsilon, p));
    }

    @Override
    protected void initAllProblems() {

        ArrayList<IntegerMOProblem> problems = new ArrayList<IntegerMOProblem>();

//    	problems.add(new CITOProblem(CITOProblem.Problems.OA_AJHotDraw.name()));
        problems.add(new CITOProblem(CITOProblem.Problems.OA_AJ_HSQLDB.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OA_HealthWatcher.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OA_TollSystems.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OO_BCEL.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OO_JBoss.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OO_JHotDraw.name()));
//		problems.add(new CITOProblem(CITOProblem.Problems.OO_MyBatis.name()));

        for (IntegerMOProblem moProblem : problems) {
            addTask(stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001, moProblem);
        }
    }
}
