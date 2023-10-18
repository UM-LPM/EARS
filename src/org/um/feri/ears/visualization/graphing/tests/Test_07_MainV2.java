package org.um.feri.ears.visualization.graphing.tests;

import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.BenchmarkRunnerBestAlgSettings;
import org.um.feri.ears.benchmark.RPUOed2Benchmark;
import org.um.feri.ears.visualization.graphing.GraphSet;
import org.um.feri.ears.visualization.graphing.PlotType;
import org.um.feri.ears.visualization.graphing.data.GraphDataManager;
import org.um.feri.ears.visualization.graphing.data.GraphDataSet;

//import net.sourceforge.jswarm_pso.SwarmAlgorithm;
//import com.erciyes.karaboga.bee.BeeColonyAlgorithm;
//import com.um.feri.brest.de.DEAlgorithm;

public class Test_07_MainV2 {

    public static void main(String[] args) {
        Benchmark.printInfo = true; //prints one on one results
        BenchmarkRunnerBestAlgSettings rbs = new BenchmarkRunnerBestAlgSettings(true,false, new RPUOed2Benchmark());
        rbs.addAlgorithm(new RandomWalkAlgorithm());
        rbs.addAlgorithm(new RandomWalkAMAlgorithm())  ;
        rbs.addAlgorithm(new ES1p1sAlgorithm());
        rbs.addAlgorithm(new TLBOAlgorithm());
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
        graphs.Plot(PlotType.AVERAGE_OF_ITERATIONS);
        graphs.Plot(PlotType.STANDARD_DEVIATION_OF_ITERATIONS);
        //graphs.Plot(GraphType.BestInGeneration);
        //graphs.Plot(GraphType.WorstInGeneration);
        //graphs.setTitle(0, "FERI FTW");
        graphs.Flush();
        //System.err.println(graphs.getGraph(0).GetGeneratedScript());
        //*/
        
        
    }

}