package org.um.feri.ears.experiment.ee;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.util.Util;

public class BenchmarkEEExecute {
	
    public static void main(String[] args) {
    	
    	Util.rnd.setSeed(System.currentTimeMillis());

    	RatingBenchmark.printInfo = true; //prints one on one results
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
    	BankOfResults ba = new BankOfResults();
    	suopm.run(ba, 5);

    }
}
