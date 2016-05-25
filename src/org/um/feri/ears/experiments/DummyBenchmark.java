package org.um.feri.ears.experiments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.DummyAlgorithm;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.demo.DEMO;
import org.um.feri.ears.algorithms.moo.gde3.GDE3;
import org.um.feri.ears.algorithms.moo.moead_dra.MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.nsga2.NSGAII;
import org.um.feri.ears.algorithms.moo.paes.PAES;
import org.um.feri.ears.algorithms.moo.spea2.SPEA2;
import org.um.feri.ears.benchmark.DummyRating;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingCEC2009;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Reporting;
import org.um.feri.ears.util.Util;

public class DummyBenchmark {

	public static void main(String[] args) {
		
		HashMap<IndicatorName, ArrayList<Player>> indikatorResults = new HashMap<>();
		
        Util.rnd.setSeed(System.currentTimeMillis());
        RatingBenchmark.debugPrint = true; //prints one on one results
        ArrayList<DummyAlgorithm> players = new ArrayList<DummyAlgorithm>();
        players.add(new DummyAlgorithm("ga"));
        players.add(new DummyAlgorithm("s2"));
        players.add(new DummyAlgorithm("s5"));
        players.add(new DummyAlgorithm("s6"));
        players.add(new DummyAlgorithm("abc"));
        players.add(new DummyAlgorithm("tlbo"));
        
        ResultArena ra = new ResultArena(100);

        DummyRating cec = new DummyRating(0.000001); //Create banchmark
        for (DummyAlgorithm al:players) {
        	ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
        	cec.registerAlgorithm(al);
        }
        BankOfResults ba = new BankOfResults();
        long initTime = System.currentTimeMillis();
        cec.run(ra, ba, 10000); //repeat competition 50X
        long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
        System.out.println("Benchmark execution time: "+estimatedTime + "s");
        ArrayList<Player> list = new ArrayList<Player>();
        list.addAll(ra.recalcRangs()); //new ranks
        for (Player p: list) System.out.println(p); //print ranks

	}

}
