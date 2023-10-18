package org.um.feri.ears.visualization.graphing.tests;

import org.um.feri.ears.algorithms.so.pso.PSO;
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

public class Main_SO {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Benchmark.printInfo = true; //prints one on one results
        BenchmarkRunnerBestAlgSettings rbs = new BenchmarkRunnerBestAlgSettings(true,false, new RPUOed2Benchmark());
        
//        rbs.addAlgorithm(new RandomWalkAlgorithm());
        //rbs.addAlgorithm(new RandomWalkAMAlgorithm());
        rbs.addAlgorithm(new PSO());
        //rbs.addAlgorithm(new DEAlgorithm(3,20)); 	// Ne dela, zastarela koda?
//        rbs.addAlgorithm(new ES1p1sAlgorithm());
        rbs.addAlgorithm(new TLBOAlgorithm());
        
        rbs.run(50);
        System.out.println(rbs);
        
        // ----------------------------------------------------------------
        
        
        //*
        //GraphDataSet datas = GraphDataManager.GetDataFor(null, ProblemAckley.class);
        //GraphDataSet datas = GraphDataManager.GetDataFor(PSO.class, ProblemEasom.class);
        GraphDataSet datas = GraphDataManager.GetDataFor(null, null);
        
        //DEBUG//
        /*
        RecordedData[][] test = datas.getSubsets();
        System.err.println("test.length="+test.length);
        System.err.println("test[0].length="+test[0].length);
        System.err.println("test[0][0].iteration="+test[0][0].iteration+"  test[0][last].iteration="+test[0][test[0].length-1].iteration);
        */
        
        //GraphSet graphs = new GraphSet(datas, 40);
        GraphSet graphs = new GraphSet(datas);
        graphs.setOutputFilesAutomatic(true);
        graphs.setCanvasSize(1280, 960);
        //graphs.setPlotColorScheme(PlotColorScheme.Colored);
        //graphs.setPlotColorScheme(PlotColorScheme.Grayscale);
        
        graphs.Plot(PlotType.AVERAGE_OF_ITERATIONS);
        graphs.Plot(PlotType.STANDARD_DEVIATION_OF_ITERATIONS);
        //graphs.Plot(PlotType.BestOfIterations);
        //graphs.Plot(PlotType.WorstOfIterations);
        //graphs.setTitle(0, "FERI FTW");
        graphs.Flush();
        //*/
        
        
        //graphs.SaveToPlotFiles();
        //graphs.SaveStatisticsToFiles();
        
        
        // alternative: graphs.Add(graphs.getCombinedGraphsByProblem());
        GraphSet combinedGraphs = graphs.getCombinedGraphsByProblem();
        combinedGraphs.Flush();
        
    }

}