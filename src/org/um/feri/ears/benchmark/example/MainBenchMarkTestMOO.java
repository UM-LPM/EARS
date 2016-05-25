package org.um.feri.ears.benchmark.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.demo.DEMO;
import org.um.feri.ears.algorithms.moo.gde3.GDE3;
import org.um.feri.ears.algorithms.moo.moead_dra.MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.nsga2.NSGAII;
import org.um.feri.ears.algorithms.moo.paes.PAES;
import org.um.feri.ears.algorithms.moo.pesa2.D_PESAII;
import org.um.feri.ears.algorithms.moo.pesa2.PESAII;
import org.um.feri.ears.algorithms.moo.pesa2MOEA.D_PESA2;
import org.um.feri.ears.algorithms.moo.pesa2MOEA.PESA2;
import org.um.feri.ears.algorithms.moo.spea2.SPEA2;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingCEC2009;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Reporting;
import org.um.feri.ears.util.Util;

public class MainBenchMarkTestMOO {

	public static void main(String[] args) {
		
		HashMap<IndicatorName, ArrayList<Player>> indikatorResults = new HashMap<>();
		
        Util.rnd.setSeed(System.currentTimeMillis());
        RatingBenchmark.debugPrint = true; //prints one on one results
        ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
        //players.add(new MOEAD_DRA());
        //players.add(new NSGAII());
        //players.add(new SPEA2());
        players.add(new D_PESAII());
        players.add(new D_PESA2());
        //players.add(new PAES());
        //players.add(new GDE3());
        //players.add(new DEMO());
        
        ResultArena ra = new ResultArena(100);

	   /* List<IndicatorName> indi =  Arrays.asList(IndicatorName.values());
	    Collections.sort(indi, new Comparator<IndicatorName>() {
	    	@Override
	    	public int compare(IndicatorName in1, IndicatorName in2) {
	    		return in1.compareTo(in2);
	    	}
	    } );

        for (IndicatorName name : indi)
        {*/
        	List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        	indicators.add(IndicatorName.IGD);
        	RatingCEC2009 cec = new RatingCEC2009(indicators, 0.0000001); //Create banchmark
        	for (MOAlgorithm al:players) {
        		ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
        		cec.registerAlgorithm(al);
        	}
        	BankOfResults ba = new BankOfResults();
        	long initTime = System.currentTimeMillis();
        	cec.run(ra, ba, 20); //repeat competition 50X
        	long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
        	System.out.println("Benchmark execution time: "+estimatedTime + "s");
        	ArrayList<Player> list = new ArrayList<Player>();
        	list.addAll(ra.recalcRangs()); //new ranks

        	//Reporting.SavePlayersToFile(list, "D:\\"+cec.getAcronym()+"_"+name+".txt", 0);
        	//indikatorResults.put(name, list);
        	for (Player p: list) System.out.println(p); //print ranks
        //}
    	//Reporting.createLatexTable(indikatorResults, "D:\\RatingCEC2009_all_indicators.tex");
	}

}
