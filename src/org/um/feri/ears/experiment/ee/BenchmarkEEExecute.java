package org.um.feri.ears.experiment.ee;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.de.DE;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.de.jade.JADE;
import org.um.feri.ears.algorithms.so.pso.PSO;
import org.um.feri.ears.algorithms.so.random.RandomSearch;
import org.um.feri.ears.algorithms.so.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;

import java.util.ArrayList;

public class BenchmarkEEExecute {
	
    public static void main(String[] args) {
    	
    	Benchmark.printInfo = true; //prints one on one results
    	ArrayList<NumberAlgorithm> players = new ArrayList<NumberAlgorithm>();

    	players.add(new RandomSearch());
    	players.add(new RandomWalkAMAlgorithm());
    	players.add(new ES1p1sAlgorithm());  
    	players.add(new TLBOAlgorithm());  
    	players.add(new JADE());
    	players.add(new PSO());
    	for (DE.Strategy strategy: DE.Strategy.values())
    		players.add(new DE(strategy,20));
    	players.add(new RandomSearch());
    	
    	BenchmarkEE suopm = new BenchmarkEE(); //Create banchmark
    	//RatingCEC2015 suopm = new RatingCEC2015();
    	for (NumberAlgorithm al:players) {
    		suopm.addAlgorithm(al);
    	}
    	suopm.run(5);
    }
}
