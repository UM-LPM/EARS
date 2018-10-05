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
public class Test_07_MainV2 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis());
        RatingBenchmark.debugPrint = true; //prints one on one results
        RunMainBestAlgSettings rbs = new RunMainBestAlgSettings(true,false, new RatingRPUOed2());
        rbs.addAlgorithm(new RandomWalkAlgorithm(),new Rating(1500, 350, 0.06));  
        rbs.addAlgorithm(new RandomWalkAMAlgorithm(),new Rating(1500, 350, 0.06))  ;  
        rbs.addAlgorithm(new ES1p1sAlgorithm(),new Rating(1500, 350, 0.06));  
        rbs.addAlgorithm(new TLBOAlgorithm(),new Rating(1500, 350, 0.06));  
        rbs.run(1);
        System.out.println(rbs);
        
        // ----------------------------------------------------------------
        
        /*
        RecordedCombination[] rcs = GraphDataRecorder.GetAllRecordedCombinations();
        ArrayList<RecordedData> dataset = rcs[3].allRecords.get(0);
        GraphEARS graph = new GraphEARS(dataset.toArray(new RecordedData[0]));
        //graph.setOutputFile("TLBO_TEST2.png");
        graph.setCanvasSize(1000, 800);
        graph.Plot(GraphType.AverageInGeneration);
        graph.Plot(GraphType.StandardDeviationInGeneration);
        graph.Plot(GraphType.BestInGeneration);
        graph.Plot(GraphType.WorstInGeneration);
        graph.Flush();
        //System.err.println(graph.GetGeneratedScript());
        //*/
        
        
        //*
        GraphDataSet datas = GraphDataManager.GetDataFor(null, "Ackley");
        GraphSet graphs = new GraphSet(datas);
        graphs.setOutputFilesAutomatic(true);
        graphs.setCanvasSize(1280, 960);
        graphs.Plot(PlotType.AverageOfIterations);
        graphs.Plot(PlotType.StandardDeviationOfIterations);
        //graphs.Plot(GraphType.BestInGeneration);
        //graphs.Plot(GraphType.WorstInGeneration);
        //graphs.setTitle(0, "FERI FTW");
        graphs.Flush();
        //System.err.println(graphs.getGraph(0).GetGeneratedScript());
        //*/
        
        
    }

}