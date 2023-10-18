package org.um.feri.ears.visualization.graphing.tests;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.moead_dra.MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.CEC2009Benchmark;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.visualization.graphing.GraphSet;
import org.um.feri.ears.visualization.graphing.PlotType;
import org.um.feri.ears.visualization.graphing.data.GraphDataManager;
import org.um.feri.ears.visualization.graphing.data.GraphDataSet;
import org.um.feri.ears.visualization.graphing.data.RecordedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public class Test_08_MainMOO
{
	public static void main(String[] args) 
	{
		//if (TEST3()) return;
		
        Benchmark.printInfo = true; //prints one on one results
        ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
        players.add(new D_MOEAD_DRA());
        players.add(new D_NSGAII());
        //players.add(new SPEA2());	//SLOW AF
        //players.add(new PESAII());
        //players.add(new PAES());
        //players.add(new GDE3());

        List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD);
        
        CEC2009Benchmark cec = new CEC2009Benchmark(indicators, 0.0000001); //Create banchmark
        for (MOAlgorithm al:players) {
          cec.addAlgorithm(al);
        }
        cec.run(1); //repeat competition 50X

        
        // -------------------------------------------------------------------
        
        //TEST1();
        
        GraphDataSet datas = GraphDataManager.GetDataFor(MOEAD_DRA.class, null, 0);
        
        /*RecordedData[][] test = datas.getSubsets();
        for (int i=0; i<test.length; i++)
        {
        	System.err.println("At "+i+" solving problem "+test[i][0].problem.name + " obj. count: "+((MOSolution)test[i][0].solution).getObjectives().length);
        }
        if (!false)
        	return;*/
        
        
        GraphSet graphs = new GraphSet(datas);
        graphs.setOutputFilesAutomatic(true);
        graphs.setCanvasSize(1280, 960);
        graphs.setAnimationDuration(5000);
        //graphs.Plot(GraphType.MOAllIndividuals);
        //graphs.Plot(GraphType.MOFinalIndividuals);
        //graphs.Plot(GraphType.MOAnimatedParetoFrontSearchGenerationIndividuals);
        graphs.Plot(PlotType.MO_ANIMATED_PARETO_FRONT_SEARCH);
        graphs.Plot(PlotType.MO_ALL_INDIVIDUALS);
        graphs.Flush();
        
        /*RecordedData[] rd = datas.getSubsets()[0];
        MOProblem p = (MOProblem)rd[0].problem;
        int obj_count = p.getNumberOfObjectives();
    	System.err.println("OBJECTIVE COUNT: " + obj_count);
        for (int i=1; i<rd.length; i++)
        {
        	MOSolution s1 = (MOSolution)rd[i-1].solution;
        	MOSolution s2 = (MOSolution)rd[i].solution;
        	
        	//p.isFirstBetter(x, y, qi)
        }*/
        
        
        
        // COUNTS:
        boolean debug = false;
        if (debug)
        {
	        GraphDataSet datasT = GraphDataManager.GetDataFor(null, null, 0);
	        int countT = datasT.getSubsets().length;
	        System.err.println("MO CONTEXT COUNT: "+countT);
	        for (int i=0; i<countT; i++)
	        {
	        	int countT2 = datasT.getSubsets()[i].length;
	        	System.err.println("MO CONTEXT["+i+"] DATA COUNT: "+countT2);
	        }
        }
    }
	
	
	public static void TEST1()
	{
		/*GraphDataSet datas = GraphDataManager.GetDataFor(null, UnconstrainedProblem1.class, 0);
		RecordedData[] data = datas.getSubsets()[0];
		
		MOProblem problem = (MOProblem)data[0].problem;
		//for (int i=)*/
	}
	
	
	public static boolean TEST2()
	{
		for (int i=0; i<10; i++)
		{
			String uID = UUID.randomUUID().toString();
			System.err.println(uID);
		}
		return true;
	}
	
	public static boolean TEST3()
	{
		//*
		RecordedData[] test = new RecordedData[]
		{
			new RecordedData(new double []{0.1, 0.4, 0.5}),
			//new RecordedData(new double []{0.2, 0.3, 0.5}),
			new RecordedData(new double []{0.25, 0.25, 0.6}),
			//new RecordedData(new double []{0.3, 0.2, 0.5}),
			new RecordedData(new double []{0.4, 0.1, 0.5}),
			//new RecordedData(new double []{0.2, 0.2})		
		};//*/
		/*
		RecordedData[] test = new RecordedData[]
		{
			new RecordedData(new double []{0.1, 0.1}),
			new RecordedData(new double []{0.2, 0.2})		
		};//*/
		test = GraphDataManager.GetParetoFront(test, 1, 2);
		System.err.println("COUNT: "+test.length);
		for (int i=0; i<test.length; i++)
		{
			NumberSolution sol = (NumberSolution)test[i].solution;
			System.err.println(sol.getObjective(0) + "   " + sol.getObjective(1) + "   " + sol.getObjective(2));
		}
		return true;
	}
}
