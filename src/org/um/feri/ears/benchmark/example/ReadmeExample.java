package org.um.feri.ears.benchmark.example;
import java.util.ArrayList;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class ReadmeExample {
	public static void main(String[] args) {
		
		Util.rnd.setSeed(20);

		ArrayList<Algorithm> players = new ArrayList<Algorithm>();
		for (int i = 0; i < 100; i++) {
		    Algorithm algorithm = new ES1p1sAlgorithm();
		    // Give each instance different name, so they don't clash.
		    algorithm.setAlgorithmInfo(new AlgorithmInfo("", "", Integer.toString(i), ""));
		    players.add(algorithm);
		}

		ResultArena ra = new ResultArena(100);
		RatingRPUOed2 suopm = new RatingRPUOed2(); //Create banchmark
		suopm.setDisplayRatingIntervalChart(false);
		for (Algorithm al:players) {
		    ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
		    suopm.registerAlgorithm(al);
		}
		BankOfResults ba = new BankOfResults();
		suopm.run(ra, ba, 20); //repeat competition 20X
	}
	
}


