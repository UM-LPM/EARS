package org.um.feri.ears.experiment.ee;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class BenchmarkEEExecute {
	
    public static void main(String[] args) {
    	
    	Util.rnd.setSeed(System.currentTimeMillis());

    	Benchmark.printInfo = true; //prints one on one results
    	ArrayList<Algorithm> players = new ArrayList<Algorithm>();

    	players.add(new RandomWalkAlgorithm());  
    	players.add(new RandomWalkAMAlgorithm())  ;  
    	players.add(new ES1p1sAlgorithm());  
    	players.add(new TLBOAlgorithm());  
    	players.add(new JADELogging());  
    	players.add(new PSOoriginalLogging());
    	for (int k=1;k<11;k++)
    		players.add(new DEAlgorithmLogging(k,20));
    	players.add(new RandomWalkAlgorithm());
    	
    	BenchmarkEE suopm = new BenchmarkEE(); //Create banchmark
    	//RatingCEC2015 suopm = new RatingCEC2015();
    	for (Algorithm al:players) {
    		suopm.addAlgorithm(al);
    	}
    	suopm.run(5);
    }
}
