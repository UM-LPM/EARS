package org.um.feri.ears.experiments;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.gde3.D_GDE3;
import org.um.feri.ears.algorithms.moo.ibea.D_IBEA;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_STM;
import org.um.feri.ears.algorithms.moo.nsga3.D_NSGAIII;
import org.um.feri.ears.algorithms.moo.spea2.D_SPEA2;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingEnsemble;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.Reporting;
import org.um.feri.ears.util.Util;

public class WeightedEnsemble {

	public static void main(String[] args) {

		Util.rnd.setSeed(System.currentTimeMillis());
		RatingBenchmark.debugPrint = true; //prints one on one results
		ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
		
		players.add(new D_MOEAD_STM());
		players.add(new D_NSGAIII());
		players.add(new D_SPEA2());
		players.add(new D_GDE3());
		players.add(new D_IBEA());

		MOAlgorithm.setRunWithOptimalParameters(true);
		

		ResultArena ra = new ResultArena(100);

		ArrayList<IndicatorName> indicators = new ArrayList<IndicatorName>();
		
		
		indicators.add(IndicatorName.MaximumSpread);
		indicators.add(IndicatorName.R2);
		indicators.add(IndicatorName.Epsilon);
		indicators.add(IndicatorName.NativeHV);
		indicators.add(IndicatorName.IGDPlus);

		
		MOAlgorithm.setCaching(Cache.RoundRobin);
		long initTime = System.currentTimeMillis();

		StringBuilder sb = new StringBuilder();
		sb.append("QI;");
		for (MOAlgorithm p : players)
		{
			sb.append(p.getAlgorithmInfo().getPublishedAcronym());
			sb.append(";");
		}
		sb.append("\n");
		
		
		for(int i = 0; i <= 100; i++)
		{
			double[] weights = generateWeights(indicators.size(), 100, i);
			//System.out.println(i+ " " +Arrays.toString(weights));


			RatingEnsemble re = new RatingEnsemble(indicators, weights, 1e-7, true, false); //Create banchmark
			for (MOAlgorithm al:players) {
				ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
				re.registerAlgorithm(al);
			}
			BankOfResults ba = new BankOfResults();
			re.run(ra, ba, 30); //repeat competition 50X
			ArrayList<Player> list = new ArrayList<Player>();
			list.addAll(ra.recalcRangs()); //new ranks

			long estimatedTime = System.currentTimeMillis() - initTime;
			System.out.println("Total execution time: "+estimatedTime + "ms");

			//Reporting.saveLeaderboard(list, "D:\\Benchmark results\\IEEE\\IEEE_benchmark_leaderboard_WFG_3.txt");
			//Reporting.createLatexTable(list, "D:\\Benchmark results\\IEEE\\IEEE_benchmark_table_WFG_3.tex");

			for (Player p: list) System.out.println(p); //print ranks
			
			sb.append(i+";");
			list = sortListByName(list, players);
			for (Player p : list)
			{
				sb.append(p.getRatingData().getRating());
				sb.append(";");
			}
			sb.append("\n");
		}
		
		
		try {
			FileOutputStream fos = new FileOutputStream("D:\\Benchmark results\\IEEE\\MS_weighted.csv");
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(sb.toString());
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static ArrayList<Player> sortListByName(ArrayList<Player> list, ArrayList<MOAlgorithm> players) {
		
		ArrayList<Player> sortedList = new ArrayList<Player>();

		for(MOAlgorithm alg : players)
		{
			for(Player p: list)
			{
				if(p.getPlayerId().equals(alg.getAlgorithmInfo().getPublishedAcronym()))
				{
					sortedList.add(p);
					continue;
				}
			}
		}
		
		return sortedList;
	}

	private static double[] generateWeights(int numWeights, int numberOfSteps, int currentStep) {
		
		double current = (1.0 / numberOfSteps) * currentStep;
		double rest = (1.0 - current) / (numWeights - 1);
		
		double[] weights = new double[numWeights];
		
		weights[0] = current;
		for(int i = 1; i < numWeights; i++)
		{
			weights[i] = weights[i-1] + rest; 
		}
		
		weights[numWeights-1] = 1.1; // if random is 1.0 this is selected
		
		return weights;
	}

}
