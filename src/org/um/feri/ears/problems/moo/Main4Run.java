package org.um.feri.ears.problems.moo;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.ibea.D_IBEA;
import org.um.feri.ears.algorithms.moo.ibea.IBEA;
import org.um.feri.ears.algorithms.moo.moead.I_MOEAD;
import org.um.feri.ears.algorithms.moo.moead.MOEAD;
import org.um.feri.ears.algorithms.moo.moead_dra.MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.moead_dra.MOEAD_STM;
import org.um.feri.ears.algorithms.moo.nsga2.I_NSGAII;
import org.um.feri.ears.algorithms.moo.nsga2.NSGAII;
import org.um.feri.ears.algorithms.moo.nsga3.D_NSGAIII;
import org.um.feri.ears.algorithms.moo.nsga3.NSGAIII;
import org.um.feri.ears.algorithms.moo.pesa2.D_PESAII;
import org.um.feri.ears.algorithms.moo.pesa2.PESAII;
import org.um.feri.ears.algorithms.moo.pesa2MOEA.D_PESA2;
import org.um.feri.ears.algorithms.moo.pso.OMOPSO;
import org.um.feri.ears.algorithms.moo.spea2.SPEA2;
import org.um.feri.ears.benchmark.DoubleEliminationTournament;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PMXCrossover;
import org.um.feri.ears.operators.PermutationSwapMutation;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.operators.SBXCrossover;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.moo.dtlz.DTLZ1;
import org.um.feri.ears.problems.moo.dtlz.DTLZ2;
import org.um.feri.ears.problems.moo.real_world.CITOProblem;
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
import org.um.feri.ears.problems.moo.wfg.WFG2;
import org.um.feri.ears.problems.moo.zdt.ZDT1;
import org.um.feri.ears.qualityIndicator.GenerationalDistance;
import org.um.feri.ears.qualityIndicator.InvertedGenerationalDistance;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.Util;

public class Main4Run {
    public static void main(String[] args) {
    	Util.rnd.setSeed(System.currentTimeMillis());
    	
    	ZDT1 zdt = new ZDT1();
    	ParetoSolution filePareto = new ParetoSolution();
    	filePareto.loadObjectivesFromFile("test_pareto//ZDT1_middle.dat");
    	//filePareto.displayData("Uniform",zdt.getName(), zdt);
    	//filePareto.displayAllUnaryQulaityIndicators(zdt);

    	
    	/*DoubleEliminationTournament det = new DoubleEliminationTournament();
    	
    	ArrayList<IndicatorName> indicators = new ArrayList<IndicatorName>();
    	indicators.add(IndicatorName.IGD);
    	indicators.add(IndicatorName.Hypervolume);
    	indicators.add(IndicatorName.Spacing);
    	indicators.add(IndicatorName.MaximumSpread);
    	indicators.add(IndicatorName.CovergeOfTwoSets);
    	
    	ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
        players.add(new MOEAD(100));
        players.add(new NSGAII(100));
        players.add(new SPEA2(100));
        players.add(new PESAII(100));
        players.add(new IBEA(100,100));*/

    	
    	//det.run(50, indicators, players, new ZDT1(),50);
        

        DoubleMOTask t1 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem1());
       /*DoubleMOTask t2 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem2());
        DoubleMOTask t3 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem3());
        DoubleMOTask t4 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem4());
        DoubleMOTask t5 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem5());
        DoubleMOTask t6 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem6());
        DoubleMOTask t7 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem7());
        DoubleMOTask t8 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem8());
        DoubleMOTask t9 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem9());
        DoubleMOTask t10 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem10());
        DoubleMOTask t11 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new DTLZ2(2));*/
        //DoubleMOTask t12 = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new DTLZ2(3));
    	ArrayList<DoubleMOTask> tasks = new ArrayList<DoubleMOTask>();
    	//tasks.add(t1);
    	//tasks.add(t2);
    	//tasks.add(t3);
    	tasks.add(t1);
    	//tasks.add(t5);
    	//tasks.add(t6);
    	//tasks.add(t7);
    	//tasks.add(t8);
    	//tasks.add(t9);
    	//tasks.add(t10);
    	//tasks.add(uc);
    	String data ="";
    	
    	
    	
    	for (DoubleMOTask task : tasks) {
    		
   	
    		CrossoverOperator<Integer, IntegerMOTask> cross = new PMXCrossover();
    		MutationOperator<Integer, IntegerMOTask> mut = new PermutationSwapMutation(0.2);

    		NSGAIII nsgaiii = new NSGAIII<IntegerMOTask,Integer>(cross, mut);
    		D_PESA2 mpesa = new D_PESA2();

    		I_MOEAD moead = new I_MOEAD();
    		D_NSGAIII nsga = new D_NSGAIII();

    		D_IBEA ibea = new D_IBEA(200,200);

    		ibea.setDisplayData(true);
    		ibea.setSaveData(true);
    		
            try {
            	
            	// OA_AJHsqldb OO_BCEL OO_MyBatis
            	//ParetoSolution best1 = nsgaiii.run(new IntegerMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new CITOProblem("OO_MyBatis")));
            	
            	ParetoSolution best = ibea.run(new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem1()));
            	
            	best.printFeasibleFUN("D:\\Benchmark results\\IBEA_UF1.csv");
            	//moead.run(new IntegerMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new CITOProblem("OO_MyBatis")));
            	
            	best.evaluate(new InvertedGenerationalDistance<Double>(task.getNumberOfObjectives(), task.getProblemFileName()));
            	System.out.println(best.getEval());
            	best.evaluate(new GenerationalDistance<Double>(task.getNumberOfObjectives(), task.getProblemFileName()));
            	System.out.println(best.getEval());
            	
                //ParetoSolution best2 = moeadtsm.run(new MOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new DTLZ2(3)));
            	
            	/*best1.evaluate(new NativeHV(task.getProblem()));
            	System.out.println(best1.getEval());
            	best1.evaluate(new InvertedGenerationalDistance(task.getProblem()));
            	System.out.println(best1.getEval());
            	best1.evaluate(new GenerationalDistance(task.getProblem()));
            	System.out.println(best1.getEval());*/
            	
            	//best2.printFeasibleFUN("D:\\Benchmark results\\DTLZ1_10D.dat");
            	//best1.evaluate(new InvertedGenerationalDistance(task.getProblem()));


            	//System.out.println(best.getEval());
            	//data+= best.getEval()+"\n";
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
    	data = data.replace('.', ',');
    	//System.out.println(data);
    	
        
    	
        //test.setDisplayData(true);
        //test.setSaveData(true);
        /*double[] results = new double[50];
        double sum = 0.0;
        double avg, sd = 0.0;
        for (int i = 0; i < results.length; i++) {
        	UnconstrainedProblem4 uc4 = new UnconstrainedProblem4();
            Task t = new Task(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem4());
            PESAII test = new PESAII();
            
        	try {
            	Individual best = test.run(t);
            	System.out.println(best.getEval());
            	results[i] = best.getEval();
            	sum+= best.getEval();
            } catch (StopCriteriaException e) {
                e.printStackTrace();
            }
		}
        avg = sum / results.length;
        
        for (int i=0; i<results.length;i++)
        {
            sd += Math.pow(results[i] - avg, 2);
        }
        sd = Math.sqrt(sd / results.length);
        
        System.out.println("Avg: "+avg+"\nStd: "+sd);*/
    }
}
