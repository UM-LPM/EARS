package org.um.feri.ears.visualization.graphing.tests;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.CEC2009Benchmark;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.visualization.graphing.MOGraphSet;
import org.um.feri.ears.visualization.graphing.PlotType;
import org.um.feri.ears.visualization.graphing.data.GraphDataManager;
import org.um.feri.ears.visualization.graphing.data.GraphDataSet;

import java.util.ArrayList;
import java.util.List;



public class Test_12_MainMOOv3
{
	public static void main(String[] args) 
	{
        Benchmark.printInfo = true; //prints one on one results
        ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
        players.add(new D_MOEAD_DRA());
        players.add(new D_NSGAII());
        //players.add(new SPEA2());	//SLOW AF
       // players.add(new PESAII());
       // players.add(new PAES());
       // players.add(new GDE3());
        
		MOAlgorithm.setRunWithOptimalParameters(true);
        
        List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD);
        
        CEC2009Benchmark cec = new CEC2009Benchmark(indicators, 0.0000001); //Create banchmark
        for (MOAlgorithm al:players) {
          cec.addAlgorithm(al);
        }
        cec.run(1); //repeat competition 50X

        
        // -------------------------------------------------------------------

        // Gather data:
        GraphDataSet datas = GraphDataManager.GetDataFor(D_NSGAII.class, "UF8", 0);
        
        //GraphDataSet datas = GraphDataManager.GetDataFor(MOEAD_DRA.class, null, 0);
        //GraphDataSet datas = GraphDataManager.GetDataFor(null, null, 0);
        
        
        MOGraphSet graphs = new MOGraphSet(datas); // generate graphs from the dataset
        
        // Settings:
        graphs.setOutputFilesAutomatic(true);
        graphs.setCanvasSize(1280, 960);
        graphs.setAnimationFramesPerSecond(10);
        graphs.setAnimationEvaluationsPerFrame(400);
        graphs.setZoomScale(0.2);
        
        //graphs.setAnimationDuration(10000);
        
        // Plotting:
        //graphs.Plot(GraphType.MOAllIndividuals);
        //graphs.Plot(GraphType.MOFinalIndividuals);
        //graphs.Plot(GraphType.MOAnimatedParetoFrontSearchGenerationIndividuals);
        //graphs.Plot(PlotType.MOAnimatedFinalParetoFront);
        graphs.Plot(PlotType.MO_ANIMATED_PARETO_FRONT_SEARCH);
        //graphs.Plot(PlotType.MOAnimatedParetoFrontSearchDominatedSpaceSoFar);
        //graphs.Plot(GraphType.MOAllIndividuals);
        //graphs.Plot(GraphType.MOParetoIndividuals);
        
        // Output:
        graphs.Flush();
    }
	
}
