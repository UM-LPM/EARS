package org.um.feri.ears.visualization.graphing.tests;

import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.BenchmarkRunnerBestAlgSettings;
import org.um.feri.ears.benchmark.RPUOed2Benchmark;
import org.um.feri.ears.visualization.graphing.data.RecordedCombination;
import org.um.feri.ears.visualization.graphing.data.RecordedData;
import org.um.feri.ears.visualization.graphing.recording.GraphDataRecorder;

//import net.sourceforge.jswarm_pso.SwarmAlgorithm;
//import com.erciyes.karaboga.bee.BeeColonyAlgorithm;
//import com.um.feri.brest.de.DEAlgorithm;

public class Test_05_RecordedDataManipulation {

    public static void main(String[] args) {
        Benchmark.printInfo = true; //prints one on one results
        BenchmarkRunnerBestAlgSettings rbs = new BenchmarkRunnerBestAlgSettings(true,false, new RPUOed2Benchmark());
        rbs.addAlgorithm(new RandomWalkAlgorithm());
        rbs.addAlgorithm(new RandomWalkAMAlgorithm())  ;
        rbs.addAlgorithm(new ES1p1sAlgorithm());
        rbs.addAlgorithm(new TLBOAlgorithm());
//        rbs.addAlgorithm(new SwarmAlgorithm());
//        rbs.addAlgorithm(new BeeColonyAlgorithm());
//        for (int k=1;k<11;k++)
//            rbs.addAlgorithm(new DEAlgorithm(k,20));
        //rbs.addAlgorithm(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin,20));
        rbs.run(2);	//30
        System.out.println(rbs);
 
        
        System.out.flush();
        System.err.println("-----------------------------------------------------------------------------------------------\n");
        System.err.flush();
        
        Test02_Combinations();
        
        System.out.flush();
        System.err.println("-----------------------------------------------------------------------------------------------\n");
        System.err.flush();
        
        Test03_AllData();
    }

    public static void Test02_Combinations()
    {
    	RecordedCombination[] rcs = GraphDataRecorder.GetAllRecordedCombinations();
    	System.out.println("Number of combinations: " + rcs.length);
    	for (int i=0; i<rcs.length; i++)
    	{
    		System.out.println((i+1)+" = "+rcs[i]+"  iterations:"+rcs[i].iterationCount);
    	}
    }
    
    public static void Test03_AllData()
    {
    	
    	RecordedData[] allData = GraphDataRecorder.GetAllData();
    	System.out.println("All data length: "+allData.length);
    }
    
}