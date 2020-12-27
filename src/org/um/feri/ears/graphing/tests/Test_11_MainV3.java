package org.um.feri.ears.graphing.tests;

import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.examples.RunMainBestAlgSettings;
import org.um.feri.ears.graphing.GraphSet;
import org.um.feri.ears.graphing.PlotType;
import org.um.feri.ears.graphing.data.GraphDataManager;
import org.um.feri.ears.graphing.data.GraphDataSet;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.util.Util;

//import net.sourceforge.jswarm_pso.SwarmAlgorithm;
//import com.erciyes.karaboga.bee.BeeColonyAlgorithm;
//import com.um.feri.brest.de.DEAlgorithm;

/**
 * @author Administrator
 *
 */
public class Test_11_MainV3 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis());
        RatingBenchmark.debugPrint = true; //prints one on one results
        RunMainBestAlgSettings rbs = new RunMainBestAlgSettings(true,false, new RatingRPUOed2());
        rbs.addAlgorithm(new RandomWalkAlgorithm(),new Rating(1500, 350, 0.06));  
        rbs.addAlgorithm(new RandomWalkAMAlgorithm(),new Rating(1500, 350, 0.06));  
        rbs.addAlgorithm(new ES1p1sAlgorithm(),new Rating(1500, 350, 0.06));  
        rbs.addAlgorithm(new TLBOAlgorithm(),new Rating(1500, 350, 0.06));  
        rbs.run(20);
        System.out.println(rbs);
        
        // ----------------------------------------------------------------
        
        /*
        RecordedCombination[] rcs = GraphDataRecorder.GetAllRecordedCombinations();
        ArrayList<RecordedData> dataset = rcs[3].allRecords.get(0);
        GraphEARS graph = new GraphEARS(dataset.toArray(new RecordedData[0]));
        graph.setOutputFile("TLBO_TEST2.png"); 
        graph.setCanvasSize(1000, 800);
        graph.Plot(PlotType.AverageOfIterations);
        graph.Plot(PlotType.StandardDeviationOfIterations);
        graph.Plot(PlotType.BestOfIterations);
        graph.Plot(PlotType.WorstOfIterations);
        graph.Flush();
        //System.err.println(graph.GetGeneratedScript());
        //*/
        
        //*
        GraphDataSet datas = GraphDataManager.GetDataFor(null, "Ackley");
        
        //DEBUG//
        /*
        RecordedData[][] test = datas.getSubsets();
        System.err.println("test.length="+test.length);
        System.err.println("test[0].length="+test[0].length);
        System.err.println("test[0][0].iteration="+test[0][0].iteration+"  test[0][last].iteration="+test[0][test[0].length-1].iteration);
        */
        
        GraphSet graphs = new GraphSet(datas);
        graphs.setOutputFilesAutomatic(true);
        graphs.setCanvasSize(1280, 960);
        //graphs.SetPlotColorScheme(PlotColorScheme.Colored);
        //graphs.SetPlotColorScheme(PlotColorScheme.Grayscale);
        
        graphs.Plot(PlotType.AVERAGE_OF_ITERATIONS);
        graphs.Plot(PlotType.STANDARD_DEVIATION_OF_ITERATIONS);
        //graphs.Plot(PlotType.BestOfIterations);
        //graphs.Plot(PlotType.WorstOfIterations);
        //graphs.setTitle(0, "FERI FTW");
        graphs.Flush();
        //System.err.println(graphs.getGraph(0).GetGeneratedScript());
        //*/
        
        
        //graphs.SaveToPlotFiles();
        //graphs.SaveStatisticsToFiles();
        
        
        // alternative: graphs.Add(graphs.getCombinedGraphsByProblem());
        GraphSet combinedGraphs = graphs.getCombinedGraphsByProblem();
        combinedGraphs.Flush();
        
    }

}