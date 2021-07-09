package org.um.feri.ears.benchmark;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem1;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem10;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem2;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem3;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem4;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem5;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem6;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem7;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem8;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem9;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;

public class IndicatorEnsembleBenchmark extends MOBenchmark<Double, DoubleMOTask, DoubleMOProblem> {

    public IndicatorEnsembleBenchmark(){
    	this(null, 0.0000001);
    	List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD); //Default indicator
        this.indicators = indicators;
    }
    
    public IndicatorEnsembleBenchmark(List<IndicatorName> indicators, double drawLimit) {
        super(indicators);
		name = "Rating Ensemble";
        this.drawLimit = drawLimit;
        maxEvaluations=300000;
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+drawLimit);

    }
    public IndicatorEnsembleBenchmark(ArrayList<IndicatorName> indicators, double[] weights, double drawLimit) {
        super(indicators, weights);
		name = "Rating Ensemble";
        this.drawLimit = drawLimit;
        maxEvaluations=300000;
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+drawLimit);
	}

    @Override
    protected void addTask(StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations, double epsilon, DoubleMOProblem problem) {
        tasks.add(new DoubleMOTask(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations));
    }
    
    @Override
    public void initAllProblems() {
    	
    	ArrayList<DoubleMOProblem> problems = new ArrayList<DoubleMOProblem>();
    	
    	/*problems.add(new ZDT1());
    	problems.add(new ZDT2());
    	problems.add(new ZDT3());
    	problems.add(new ZDT4());
    	problems.add(new ZDT6());
    	problems.add(new DTLZ2(3));
    	problems.add(new WFG1(5));
    	problems.add(new WFG2(5));
    	problems.add(new DTLZ1(10));*/
    	
//    	problems.add(new ZDT1());
//    	problems.add(new ZDT2());
//    	problems.add(new ZDT3());
//    	problems.add(new ZDT4());
//    	problems.add(new ZDT6());
    	    	
    	
//    	problems.add(new WFG1(2));
//    	problems.add(new WFG2(2));
//    	problems.add(new WFG3(2));
//    	problems.add(new WFG4(2));
//    	problems.add(new WFG5(2));
//    	problems.add(new WFG6(2));
//    	problems.add(new WFG7(2));
//    	problems.add(new WFG8(2));
//    	problems.add(new WFG9(2));
    	
    	
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

    	
    	
    	/*problems.add(new DTLZ1(3));
    	problems.add(new DTLZ2(3));
    	problems.add(new DTLZ3(3));
    	problems.add(new DTLZ4(3));
    	problems.add(new DTLZ5(3)); // ?
    	problems.add(new DTLZ6(3));
    	problems.add(new DTLZ7(3));*/
    	
    	
    	for (DoubleMOProblem moProblem : problems) {
    		addTask(stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.0001, moProblem);
		}
    }
}
