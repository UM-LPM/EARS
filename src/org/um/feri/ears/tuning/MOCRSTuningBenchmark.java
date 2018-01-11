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

		players.add(new I_MOEAD());
		players.add(new I_NSGAII());
		players.add(new I_SPEA2());
		players.add(new I_PESAII());
		players.add(new I_IBEA());

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
			ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
			cb.registerAlgorithm(al);
		}
		
		BankOfResults ba = new BankOfResults();
		cb.run(ra, ba, 15); //repeat competition 50X
		ArrayList<Player> list = new ArrayList<Player>();
		list.addAll(ra.calculteRatings()); //new ranks
		for (Player p: list) System.out.println(p); //print ranks

	}

}
