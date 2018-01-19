package org.um.feri.ears.benchmark.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.gde3.D_GDE3;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.algorithms.moo.spea2.D_SPEA2;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingCEC2009;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class MainBenchMarkTestMOO {

	public static void main(String[] args) {
		
        Util.rnd.setSeed(System.currentTimeMillis());
        RatingBenchmark.debugPrint = true; //prints one on one results
        ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
        players.add(new D_MOEAD_DRA());
        players.add(new D_NSGAII());
        players.add(new D_SPEA2());
        players.add(new D_GDE3());

        
        ResultArena ra = new ResultArena(100);

        List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD); // add quality indicator
        
        RatingCEC2009 cec = new RatingCEC2009(indicators, 0.0000001); //Create banchmark
        for (MOAlgorithm al:players) {
        	ra.addPlayer(al, al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
        	cec.registerAlgorithm(al);
        }
        BankOfResults ba = new BankOfResults();
        long initTime = System.currentTimeMillis();
        cec.run(ra, ba, 20); //repeat competition 20X
        long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
        System.out.println("Benchmark execution time: "+estimatedTime + "s");
        ArrayList<Player> list = new ArrayList<Player>();
        list.addAll(ra.calculteRatings()); //new ranks


        for (Player p: list) System.out.println(p); //print ranks

	}

}
