package org.um.feri.ears.experiment.ee;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class BenchmarkEEExecute {
	
    public static void main(String[] args) {
    	
    	Util.rnd.setSeed(System.currentTimeMillis());

    	RatingBenchmark.debugPrint = true; //prints one on one results
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
    	
    	ResultArena ra = new ResultArena(100); 
    	BenchmarkEE suopm = new BenchmarkEE(); //Create banchmark
    	//RatingCEC2015 suopm = new RatingCEC2015();
    	for (Algorithm al:players) {
    		ra.addPlayer(al, al.getID());
    		suopm.registerAlgorithm(al);
    	}
    	BankOfResults ba = new BankOfResults();
    	suopm.run(ra, ba, 5);
    	ArrayList<Player> list = ra.getPlayers();
    	for (Player p: list) System.out.println(p);
 
    }
}
