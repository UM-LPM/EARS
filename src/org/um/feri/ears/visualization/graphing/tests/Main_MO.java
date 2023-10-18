package org.um.feri.ears.visualization.graphing.tests;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.algorithms.moo.paes.D_PAES;
import org.um.feri.ears.algorithms.moo.pesa2.D_PESAII;
import org.um.feri.ears.algorithms.moo.spea2.D_SPEA2;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.CEC2009Benchmark;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.visualization.graphing.GraphSet;
import org.um.feri.ears.visualization.graphing.MOGraphSet;
import org.um.feri.ears.visualization.graphing.PlotType;
import org.um.feri.ears.visualization.graphing.data.GraphDataManager;
import org.um.feri.ears.visualization.graphing.data.GraphDataSet;

import java.util.ArrayList;
import java.util.List;



public class Main_MO
{
	public static void main(String[] args) 
	{
        Benchmark.printInfo = true; //prints one on one results
        ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
        players.add(new D_MOEAD_DRA());
        players.add(new D_NSGAII());
        players.add(new D_SPEA2());	//SLOW AF
        players.add(new D_PESAII());
        players.add(new D_PAES());
        // players.add(new GDE3());

        List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD);
        
        CEC2009Benchmark cec = new CEC2009Benchmark(indicators, 0.0000001); //Create banchmark
        for (MOAlgorithm al:players) {
          cec.addAlgorithm(al);
        }
        cec.run(5); //repeat competition 50X

        
        // -------------------------------------------------------------------
        
        // Gather data:
        //GraphDataSet datas = GraphDataManager.GetDataFor(NSGAII.class, UnconstrainedProblem1.class, 0);
        //GraphDataSet datas = GraphDataManager.GetDataFor(MOEAD_DRA.class, null, 0);
        //GraphDataSet datas = GraphDataManager.GetDataFor(MOEAD_DRA.class, null, 0);
        for (int i=0; i< 5; i++)
        {
        GraphDataSet datas = GraphDataManager.GetDataFor(null, null, i);
        MOGraphSet graphs = new MOGraphSet(datas);
        
        // Settings:
        graphs.setOutputFilesAutomatic(false);
        graphs.setCanvasSize(1280, 960);
        graphs.setZoomScale(0.3);
        graphs.Plot(PlotType.MO_FINAL_PARETO_FRONT);
        //graphs.Flush();
        MOGraphSet combinedGraphs = graphs.getCombinedGraphsByProblem();
        combinedGraphs.Flush();
        }
        
        /*datas = GraphDataManager.GetDataFor(null, null, 0);
        graphs = new GraphSet(datas);
        
        // Settings:
        graphs.setOutputFilesAutomatic(true);
        graphs.setCanvasSize(1280, 960);
        //graphs.setAnimationDuration(10000);
        graphs.setAnimationFramesPerSecond(10);
        graphs.setAnimationEvaluationsPerFrame(400);
        graphs.setZoomScale(0.3);
        
        // Plotting:
        //graphs.Plot(GraphType.MOAllIndividuals);
        //graphs.Plot(GraphType.MOFinalIndividuals);
        //graphs.Plot(GraphType.MOAnimatedParetoFrontSearchGenerationIndividuals);
        //graphs.Plot(PlotType.MOAnimatedFinalParetoFront);
        graphs.Plot(PlotType.MOAnimatedParetoFrontSearch);
        //graphs.Plot(PlotType.MOAnimatedParetoFrontSearchDominatedSpaceSoFar);
        //graphs.Plot(GraphType.MOAllIndividuals);
        //graphs.Plot(GraphType.MOParetoIndividuals);
        
        // Output:
        graphs.Flush();

        
        
        if (!true)
        {
	        // Gather data:
	        //GraphDataSet datas = GraphDataManager.GetDataFor(NSGAII.class, UnconstrainedProblem1.class, 0);
	        datas = GraphDataManager.GetDataFor(NSGAII.class, null, 0);
	        //GraphDataSet datas = GraphDataManager.GetDataFor(MOEAD_DRA.class, null, 0);
	        //GraphDataSet datas = GraphDataManager.GetDataFor(null, null, 0);
	        graphs = new GraphSet(datas);
	        
	        // Settings:
	        graphs.setOutputFilesAutomatic(false);
	        graphs.setCanvasSize(1280, 960);
	        graphs.setZoomScale(0.3);
	        //graphs.setAnimationDuration(10000);
	        graphs.setAnimationFramesPerSecond(10);
	        graphs.setAnimationEvaluationsPerFrame(400);
	        
	        // Plotting:
	        //graphs.Plot(GraphType.MOAllIndividuals);
	        //graphs.Plot(GraphType.MOFinalIndividuals);
	        //graphs.Plot(GraphType.MOAnimatedParetoFrontSearchGenerationIndividuals);
	        //graphs.Plot(PlotType.MOAnimatedFinalParetoFront);
	        graphs.Plot(PlotType.MOAnimatedParetoFrontSearch);
	        //graphs.Plot(PlotType.MOAnimatedParetoFrontSearchDominatedSpaceSoFar);
	        //graphs.Plot(GraphType.MOAllIndividuals);
	        //graphs.Plot(GraphType.MOParetoIndividuals);
	        
	        // Output:
	        graphs.Flush();
        }
        
        
        if (!true)
        {
	        graphs = new GraphSet(datas);
	        // Settings:
	        graphs.setOutputFilesAutomatic(false);
	        graphs.setCanvasSize(1280, 960);
	        //graphs.setAnimationDuration(10000);
	        graphs.setAnimationFramesPerSecond(10);
	        graphs.setAnimationEvaluationsPerFrame(400);
	        graphs.setZoomScale(0.5);
	        // Plotting:
	        graphs.Plot(PlotType.MOAnimatedParetoFrontSearchDominatedSpaceSoFar);
	        // Output:
	        graphs.Flush();
	        
	        
	        graphs = new GraphSet(datas);
	        // Settings:
	        graphs.setOutputFilesAutomatic(false);
	        graphs.setCanvasSize(1280, 960);
	        //graphs.setAnimationDuration(10000);
	        graphs.setAnimationFramesPerSecond(10);
	        graphs.setAnimationEvaluationsPerFrame(400);
	        graphs.setZoomScale(0.5);
	        // Plotting:
	        graphs.Plot(PlotType.MOAnimatedParetoFrontSearchDominatedSpaceCurrent);
	        // Output:
	        graphs.Flush();
	        
	        
	        graphs = new GraphSet(datas);
	        // Settings:
	        graphs.setOutputFilesAutomatic(false);
	        graphs.setCanvasSize(1280, 960);
	        //graphs.setAnimationDuration(10000);
	        graphs.setAnimationFramesPerSecond(10);
	        graphs.setAnimationEvaluationsPerFrame(400);
	        graphs.setZoomScale(0.5);
	        // Plotting:
	        graphs.Plot(PlotType.MOAnimatedParetoFrontSearchAllIndividuals);
	        // Output:
	        graphs.Flush();
	        
	        
	        graphs = new GraphSet(datas);
	        // Settings:
	        graphs.setOutputFilesAutomatic(false);
	        graphs.setCanvasSize(1280, 960);
	        //graphs.setAnimationDuration(10000);
	        graphs.setAnimationFramesPerSecond(10);
	        graphs.setAnimationEvaluationsPerFrame(400);
	        graphs.setZoomScale(0.5);
	        // Plotting:
	        graphs.Plot(PlotType.MOAnimatedParetoFrontSearchParetoIndividuals);
	        // Output:
	        graphs.Flush();
		}*/
    }
	
	public static void DEBUG(GraphSet gs)
	{
		double d = gs.getGraph(0).maxX;
		if (d < -10000)
			System.err.println("ERROR!");
	}
}
