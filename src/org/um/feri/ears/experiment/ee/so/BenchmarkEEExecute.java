package org.um.feri.ears.experiment.ee.so;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.experiment.so.pso.PSOCBCW;
import org.um.feri.ears.experiment.so.pso.PSODOP;
import org.um.feri.ears.experiment.so.pso.PSOFS;
import org.um.feri.ears.experiment.so.pso.PSOIS;
import org.um.feri.ears.experiment.so.pso.PSOIWD;
import org.um.feri.ears.experiment.so.pso.PSOIWS;
import org.um.feri.ears.experiment.so.pso.PSOM;
import org.um.feri.ears.experiment.so.pso.PSOPBC;
import org.um.feri.ears.experiment.so.pso.PSOQ;
import org.um.feri.ears.experiment.so.pso.PSOS;
import org.um.feri.ears.experiment.so.pso.PSOTS;
import org.um.feri.ears.experiment.so.pso.PSOoriginal;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.run.RunMainBestAlgSettings;
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
    		players.add(new DEAlgorithm(k,20)); 
    	players.add(new RandomWalkAlgorithm());
    	
    	ResultArena ra = new ResultArena(100); 
    	BenchmarkEE suopm = new BenchmarkEE(); //Create banchmark
    	//RatingCEC2015 suopm = new RatingCEC2015();
    	for (Algorithm al:players) {
    		ra.addPlayer(al, al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
    		suopm.registerAlgorithm(al);
    	}
    	BankOfResults ba = new BankOfResults();
    	suopm.run(ra, ba, 5); //repeat competition 50X
    	ArrayList<Player> list = new ArrayList<Player>();
    	list.addAll(ra.calculteRatings()); //new rangs
    	for (Player p: list) System.out.println(p); //print rangs
 
    }
}
