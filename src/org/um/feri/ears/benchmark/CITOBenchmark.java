package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.IntegerProblem;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.moo.real_world.CITOProblem;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;

import java.util.ArrayList;
import java.util.List;

public class CITOBenchmark extends MOBenchmark<Integer, IntegerMOTask, IntegerProblem> {

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
    }

    @Override
    protected void addTask(StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, IntegerProblem problem) {
        tasks.add(new IntegerMOTask(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations));
    }

    @Override
    public void initAllProblems() {

        ArrayList<IntegerProblem> problems = new ArrayList<IntegerProblem>();

//    	problems.add(new CITOProblem(CITOProblem.Problems.OA_AJHotDraw.name()));
        problems.add(new CITOProblem(CITOProblem.Problems.OA_AJ_HSQLDB.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OA_HealthWatcher.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OA_TollSystems.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OO_BCEL.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OO_JBoss.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OO_JHotDraw.name()));
//		problems.add(new CITOProblem(CITOProblem.Problems.OO_MyBatis.name()));

        for (IntegerProblem moProblem : problems) {
            addTask(stopCriterion, maxEvaluations, timeLimit, maxIterations, moProblem);
        }
    }
}
