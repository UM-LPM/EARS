package org.um.feri.ears.experiments;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingEnsemble;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class RatingEnsembleTest {

	public static void main(String[] args) {

        Util.rnd.setSeed(System.currentTimeMillis());
        RatingBenchmark.debugPrint = true; //prints one on one results
        ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
        players.add(new D_MOEAD_DRA());
        //players.add(new MOEAD_STM());
        //players.add(new DummyMOAlgorithm("moeadstm"));
        players.add(new D_NSGAII());
        /*players.add(new SPEA2(100));
        players.add(new PESAII(100));*/
        //players.add(new IBEA(100,100));
        
        MOAlgorithm.setRunWithOptimalParameters(true);
        
        ResultArena ra = new ResultArena(100);
        
        ArrayList<IndicatorName> indicators = new ArrayList<IndicatorName>();
    	indicators.add(IndicatorName.IGD);
    	//indicators.add(IndicatorName.IGDPlus);
    	//indicators.add(IndicatorName.GD);
    	indicators.add(IndicatorName.Hypervolume);
    	//indicators.add(IndicatorName.Spacing);
    	//indicators.add(IndicatorName.MaximumSpread);
    	//indicators.add(IndicatorName.CovergeOfTwoSets);

        RatingEnsemble re = new RatingEnsemble(indicators, 1e-8, false); //Create banchmark
        for (MOAlgorithm al:players) {
        	ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
        	re.registerAlgorithm(al);
        }
        BankOfResults ba = new BankOfResults();
        re.run(ra, ba, 30); //repeat competition 50X
        ArrayList<Player> list = new ArrayList<Player>();
        list.addAll(ra.recalcRangs()); //new ranks
        for (Player p: list) System.out.println(p); //print ranks
        list.clear();
        list.addAll(ra.recalcRatingByGame());
        for (Player p: list) System.out.println(p);

	}

}
