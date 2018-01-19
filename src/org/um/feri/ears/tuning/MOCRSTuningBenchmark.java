package org.um.feri.ears.tuning;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.ibea.I_IBEA;
import org.um.feri.ears.algorithms.moo.moead.I_MOEAD;
import org.um.feri.ears.algorithms.moo.nsga2.I_NSGAII;
import org.um.feri.ears.algorithms.moo.pesa2.I_PESAII;
import org.um.feri.ears.algorithms.moo.spea2.I_SPEA2;
import org.um.feri.ears.benchmark.CITOBenchmark;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class MOCRSTuningBenchmark {

	public static void main(String[] args) {
		
		Util.rnd.setSeed(System.currentTimeMillis());
		RatingBenchmark.debugPrint = true; //prints one on one results
		ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();

		//default parameters
		players.add(new I_MOEAD());
		players.add(new I_NSGAII());
		players.add(new I_SPEA2());
		players.add(new I_PESAII());
		players.add(new I_IBEA());
		//tuned parameters
		
		players.add(new I_MOEAD(0.5, 0.98, 192, "MOEAD-tuned")); //this(new PMXCrossover(0.5), new PermutationSwapMutation(0.98), 192);
		players.add(new I_NSGAII(1.0,0.86,108,"NSGAII-tuned")); //this(new PMXCrossover(1.0), new PermutationSwapMutation(0.86), 108);
		players.add(new I_SPEA2(0.74, 0.87, 190, 100, "SPEA2-tuned")); //this(new PMXCrossover(0.74), new PermutationSwapMutation(0.87), 190, 190);
		players.add(new I_PESAII(0.37, 1.0, 110, 100, "PESAII-tuned")); //this(new PMXCrossover(0.37), new PermutationSwapMutation(1.0), 110, 110);
		players.add(new I_IBEA(0.63, 0.9, 173, 100, "IBEA-tuned")); //this(new PMXCrossover(0.63), new PermutationSwapMutation(0.9), 173, 100);

		MOAlgorithm.setRunWithOptimalParameters(true);

		ResultArena ra = new ResultArena(100);

		ArrayList<IndicatorName> indicators = new ArrayList<IndicatorName>();

		indicators.add(IndicatorName.NativeHV);
//		indicators.add(IndicatorName.IGD);
		indicators.add(IndicatorName.IGDPlus);
		indicators.add(IndicatorName.Epsilon);
		indicators.add(IndicatorName.R2);
		indicators.add(IndicatorName.MaximumSpread);
		
		CITOBenchmark cb = new CITOBenchmark(indicators, 1e-8, false); //Create banchmark
		cb.setDisplayRatingIntervalChart(true);

		for (MOAlgorithm al:players) {
			ra.addPlayer(al, al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
			cb.registerAlgorithm(al);
		}
		
		BankOfResults ba = new BankOfResults();
		cb.run(ra, ba, 15); //repeat competition 50X
		ArrayList<Player> list = new ArrayList<Player>();
		list.addAll(ra.calculteRatings()); //new ranks
		for (Player p: list) System.out.println(p); //print ranks

	}

}
