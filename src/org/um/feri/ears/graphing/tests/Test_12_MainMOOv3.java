package org.um.feri.ears.graphing.tests;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingCEC2009;
import org.um.feri.ears.graphing.MOGraphSet;
import org.um.feri.ears.graphing.PlotType;
import org.um.feri.ears.graphing.data.GraphDataManager;
import org.um.feri.ears.graphing.data.GraphDataSet;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;



public class Test_12_MainMOOv3
{
	public static void main(String[] args) 
	{
        Util.rnd.setSeed(System.currentTimeMillis());
        RatingBenchmark.debugPrint = true; //prints one on one results
        ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
        players.add(new D_MOEAD_DRA());
        players.add(new D_NSGAII());
        //players.add(new SPEA2());	//SLOW AF
       // players.add(new PESAII());
       // players.add(new PAES());
       // players.add(new GDE3());
        
		MOAlgorithm.setRunWithOptimalParameters(true);
        
        ResultArena ra = new ResultArena(100);
        
        List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD);
        
        RatingCEC2009 cec = new RatingCEC2009(indicators, 0.0000001); //Create banchmark
        for (MOAlgorithm al:players) {
          ra.addPlayer(al, al.getID()); //init rating 1500
          cec.registerAlgorithm(al);
        }
        BankOfResults ba = new BankOfResults();
        cec.run(ra, ba, 1); //repeat competition 50X
        ArrayList<Player> list = ra.getPlayers();
        for (Player p: list) System.out.println(p); //print rangs
        
        
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
