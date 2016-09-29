package org.um.feri.ears.experiments;

import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_DRA;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.wfg.WFG1;
import org.um.feri.ears.qualityIndicator.NativeHV;
import org.um.feri.ears.qualityIndicator.wfg.WfgHypervolume;

public class HypervolumeTest {

	public static void main(String[] args) {
		
		
		DoubleMOTask task = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new WFG1(5));
		D_MOEAD_DRA moead = new D_MOEAD_DRA();
    	try {
    		
    		ParetoSolution best = moead.execute(task);
			best.evaluate(new NativeHV(task.getNumberOfObjectives(), task.getProblemFileName()));
			System.out.println(best.getEval());
			
			best.evaluate(new WfgHypervolume(task.getNumberOfObjectives(), task.getProblemFileName()));
			System.out.println(best.getEval());
			
			/*best.evaluate(new Hypervolume(task.getProblem()));
			System.out.println(best.getEval());*/
			

			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	
    	
		/*
		ParetoSolution test1 = new ParetoSolution(280);
    	test1.loadObjectivesFromFile("test_pareto//WFG1_M5.dat");
    	
    	ParetoSolution testHV = new ParetoSolution(10);
    	testHV.loadObjectivesFromFile("test_pareto//DTLZ1_10D.dat");
    	
    	try {
			test1.evaluate(new WFGHypervolume(new WFG1(5)));
			System.out.println(test1.getEval());
			
    		testHV.evaluate(new WFGHypervolume(new DTLZ1(10)));
			System.out.println(testHV.getEval());
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
    	
    	

	}

}
