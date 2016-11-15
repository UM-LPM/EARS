package org.um.feri.ears.problems.moo;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.gde3.D_GDE3;
import org.um.feri.ears.algorithms.moo.ibea.D_IBEA;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_STM;
import org.um.feri.ears.algorithms.moo.nsga3.D_NSGAIII;
import org.um.feri.ears.algorithms.moo.spea2.D_SPEA2;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.zdt.ZDT1;
import org.um.feri.ears.problems.moo.zdt.ZDT2;
import org.um.feri.ears.problems.moo.zdt.ZDT3;
import org.um.feri.ears.problems.moo.zdt.ZDT4;
import org.um.feri.ears.problems.moo.zdt.ZDT6;
import org.um.feri.ears.util.Cache;

public class FillCache {

	public static void main(String[] args) {

    	ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
        /*players.add(new D_MOEAD());
        players.add(new D_NSGAII());
        players.add(new D_NSGAIII());
        players.add(new D_PESAII());*/
        
        
        players.add(new D_GDE3());
        players.add(new D_MOEAD_STM());
        players.add(new D_SPEA2());
        players.add(new D_IBEA());
        players.add(new D_NSGAIII());
        
		MOAlgorithm.setCaching(Cache.Save);
		MOAlgorithm.setRunWithOptimalParameters(true);
		
        
        /*DoubleMOTask t1 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem1());
        DoubleMOTask t2 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem2());
        DoubleMOTask t3 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem3());
        DoubleMOTask t4 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem4());
        DoubleMOTask t5 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem5());
        DoubleMOTask t6 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem6());
        DoubleMOTask t7 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem7());
        DoubleMOTask t8 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem8());
        DoubleMOTask t9 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem9());
        DoubleMOTask t10 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem10());*/
        DoubleMOTask t1 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 500, 300, 0.0001, new ZDT1());
        DoubleMOTask t2 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 500, 300, 0.0001, new ZDT2());
        DoubleMOTask t3 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 500, 300, 0.0001, new ZDT3());
        DoubleMOTask t4 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 500, 300, 0.0001, new ZDT4());
        DoubleMOTask t5 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 500, 300, 0.0001, new ZDT6());
        
        //DoubleMOTask t1 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new WFG1(2));
        /*DoubleMOTask t2 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new WFG2(2));
        DoubleMOTask t3 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new WFG3(2));
        DoubleMOTask t4 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new WFG4(2));
        DoubleMOTask t5 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new WFG5(2));
        DoubleMOTask t6 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new WFG6(2));
        DoubleMOTask t7 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new WFG7(2));
        DoubleMOTask t8 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new WFG8(2));
        DoubleMOTask t9 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new WFG9(2));
        DoubleMOTask t10 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new DTLZ1(2));
        DoubleMOTask t11 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new DTLZ2(2));
        DoubleMOTask t12 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new DTLZ3(2));
        DoubleMOTask t13 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new DTLZ4(2));
        DoubleMOTask t14 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new DTLZ5(2));
        DoubleMOTask t15 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new DTLZ6(2));
        DoubleMOTask t16 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new DTLZ7(2));*/
        
    	ArrayList<DoubleMOTask> tasks = new ArrayList<DoubleMOTask>();
    	tasks.add(t1);
    	tasks.add(t2);
    	tasks.add(t3);
    	tasks.add(t4);
    	tasks.add(t5);
//    	tasks.add(t6);
//    	tasks.add(t7);
//    	tasks.add(t8);
//    	tasks.add(t9);
//    	tasks.add(t10);
    	

		for(int i = 0 ; i < 100; i++)
		{
			System.out.println("-------------------------------------");
			System.out.println("Run: "+i);
			System.out.println("-------------------------------------");
	    	for (DoubleMOTask task : tasks) 
	    	{
	    		System.out.println(task.getProblemName());
	    		for(MOAlgorithm alg : players)
	    		{
	    			System.out.println("Alg: "+alg.getAlgorithmInfo().getPublishedAcronym());
	    			task.resetCounter();
	    			try {
						alg.execute(task);
					} catch (StopCriteriaException e) {
						e.printStackTrace();
					}
	    		}
	    		
	    	}
			
		}
		
	}

}
