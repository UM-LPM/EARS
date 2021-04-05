package org.um.feri.ears.tuning;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.algorithms.moo.moead.I_MOEAD;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.moo.real_world.CITOProblem;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.util.Util;

public class TestMOCRSTuning {

	public static void main(String[] args) {
		
		
	    Util.rnd.setSeed(System.currentTimeMillis());
	    
    	ArrayList<ControlParameter> control_parameters = new ArrayList<ControlParameter>();
    	ControlParameter populationSize = new ControlParameter("populationSize", "int", 20, 500);
        control_parameters.add(populationSize);
        control_parameters.add(new ControlParameter("crossoverProbability", "double", 0.1, 1));
        control_parameters.add(new ControlParameter("mutationProbability", "double", 0.1, 1));
        /*control_parameters.add(new ControlParameter("delta", "double", 0.1, 1));
        control_parameters.add(new ControlParameter("T", "int", 1, populationSize));
        control_parameters.add(new ControlParameter("nr", "int", 1, populationSize));*/
        
	    int popSize = 20;
	    int maxGen = 30;
	    int problemNumEval = 60000;
	    int tuningRuns = 8;
        
    	ArrayList<MOTask> tasks = new ArrayList<MOTask>();
    	List<IndicatorName> indicators = new ArrayList<IndicatorName>();
    	
    	
    	/*
		Name			Dependencies	Classes		Aspects		LOC
		AJHotDraw		1592			290			31			18586
		AJHSQLDB		1338			276			15			68550
		MyBatis			1271			331			-			23535
		JHotDraw		809				197			-			20273
		JBoss			367				150			-			8434
		HealthWatcher	289				95			22			5479
		BCEL			289				45			-			2999
		TollSystems		188				53			24			2496
    	*/
    	
		tasks.add(new IntegerMOTask(StopCriterion.EVALUATIONS, problemNumEval, 5000, 3000, 1.0E-4, new CITOProblem(CITOProblem.Problems.OA_AJ_HOT_DRAW.name())));
		tasks.add(new IntegerMOTask(StopCriterion.EVALUATIONS, problemNumEval, 5000, 3000, 1.0E-4, new CITOProblem(CITOProblem.Problems.OA_AJ_HSQLDB.name())));
		tasks.add(new IntegerMOTask(StopCriterion.EVALUATIONS, problemNumEval, 5000, 3000, 1.0E-4, new CITOProblem(CITOProblem.Problems.OO_MY_BATIS.name())));
		tasks.add(new IntegerMOTask(StopCriterion.EVALUATIONS, problemNumEval, 5000, 3000, 1.0E-4, new CITOProblem(CITOProblem.Problems.OO_J_HOT_DRAW.name())));
		tasks.add(new IntegerMOTask(StopCriterion.EVALUATIONS, problemNumEval, 5000, 3000, 1.0E-4, new CITOProblem(CITOProblem.Problems.OO_J_BOSS.name())));
//		tasks.add(new IntegerMOTask(EnumStopCriterion.EVALUATIONS, problemNumEval, 5000, 3000, 1.0E-4, new CITOProblem(CITOProblem.Problems.OA_HealthWatcher.name())));
		tasks.add(new IntegerMOTask(StopCriterion.EVALUATIONS, problemNumEval, 5000, 3000, 1.0E-4, new CITOProblem(CITOProblem.Problems.OO_BCEL.name())));
		tasks.add(new IntegerMOTask(StopCriterion.EVALUATIONS, problemNumEval, 5000, 3000, 1.0E-4, new CITOProblem(CITOProblem.Problems.OA_TOLL_SYSTEMS.name())));

	    indicators.add(IndicatorName.IGD_PLUS);
	    indicators.add(IndicatorName.NATIVE_HV);
	    indicators.add(IndicatorName.EPSILON);
	    indicators.add(IndicatorName.MAXIMUM_SPREAD);
	    indicators.add(IndicatorName.R2);
        
        long initTime = System.currentTimeMillis();
        ArrayList<CRSSolution> bestSolutions = new ArrayList<CRSSolution>();
        try {
        	for (int i = 0; i < tuningRuns; i++) {
        		System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////////////");
        		System.out.println("RUN: "+(i+1));
            	MOCRSTuning m = new MOCRSTuning();        	
            	CRSSolution best = m.tune(I_MOEAD.class, "MOEA/D", control_parameters, tasks, indicators, popSize, maxGen);
            	bestSolutions.add(best);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
        
		long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
		System.out.println("Total execution time: "+estimatedTime + "s");
		
		for(CRSSolution solution : bestSolutions) {
			System.out.println(solution.name);
			System.out.println(solution.getEval());
		}

	}

}
