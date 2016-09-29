package org.um.feri.ears.experiments;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.dbea.NondominatedPopulation;
import org.um.feri.ears.algorithms.moo.ibea.I_IBEA;
import org.um.feri.ears.algorithms.moo.moead_dra.I_MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.nsga2.I_NSGAII;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.real_world.CITOProblem;
import org.um.feri.ears.util.Util;

public class ParetoFrontCollector {
	
	public static void main(String[] args) {
		
		/*
		ParetoSolution truePareto = new ParetoSolution();
    	truePareto.loadObjectivesFromFile("pf_data//WFG2.2D.dat.old");
    	
    	ParetoSolution resultPareto = new ParetoSolution();
    	resultPareto.loadObjectivesFromFile("test_pareto//WFG2_2D.dat");
    	
    	NondominatedPopulation joint = new NondominatedPopulation(truePareto.size()+resultPareto.size());
		for (MOSolution moSolution : truePareto) {
			joint.add(moSolution);
		}
		
		for (MOSolution moSolution : resultPareto) {
			joint.add(moSolution);
		}
		
		int max_cap = 1000;
		double min_distance = Double.MAX_VALUE, curr_distance;
		MOSolution min1 = null, min2 = null;
		EuclideanDistance ed = new EuclideanDistance();
		
		while(joint.solutions.size() > 500)
		{
			min_distance = Double.MAX_VALUE;
			for (MOSolution sol1 : joint) {
				for (MOSolution sol2 : joint) {
					if(sol1 == sol2)
						continue;
					try {
						curr_distance = ed.compute(sol1.getObjectives(), sol2.getObjectives());
						if(min_distance > curr_distance)
						{
							min_distance = curr_distance;
							min1 = sol1;
							min2 = sol2;
						}
							
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			joint.remove(min1);
		}
		joint.printFeasibleFUN("D:\\Benchmark results\\WFG2_2D_new.dat");*/
		
	
		Util.rnd.setSeed(System.currentTimeMillis());
		
		//DoubleMOProblem mop = new WFG2(5);
		
		// OA_AJHsqldb OO_BCEL OO_MyBatis
		CITOProblem mop = new CITOProblem("OO_BCEL");
		
		ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
        players.add(new I_MOEAD_DRA(500));
        players.add(new I_NSGAII(500));
        players.add(new I_IBEA(500,500));

		
		NondominatedPopulation archive = new NondominatedPopulation(10000);
		ParetoSolution<Integer> best;
		IntegerMOTask task;
		
		for (MOAlgorithm alg : players) {
			
			for (int i = 0; i < 40; i++) {
				task = new IntegerMOTask(EnumStopCriteria.EVALUATIONS, 600000, 0.0001, mop);

				try {
					best = alg.execute(task);
					for (MOSolutionBase<Integer> moSolution : best) {
						archive.add(moSolution);
					}

				} catch (StopCriteriaException e) {
					e.printStackTrace();
				}
			}
		}
		
		archive.printFeasibleFUN("D:\\Benchmark results\\CITO_OO_BCEL.dat");
	}

}
