package org.um.feri.ears.experiments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.ibea.D_IBEA;
import org.um.feri.ears.algorithms.moo.moead.D_MOEAD;
import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.algorithms.moo.pesa2.D_PESAII;
import org.um.feri.ears.algorithms.moo.pesa2MOEA.D_PESA2;
import org.um.feri.ears.algorithms.moo.spea2.D_SPEA2;
import org.um.feri.ears.benchmark.BIOMABenchmark;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Reporting;
import org.um.feri.ears.util.Util;

public class BIOMABencmarkTest {

		public static void main(String[] args) {

	        Util.rnd.setSeed(System.currentTimeMillis());
	        RatingBenchmark.debugPrint = true; //prints one on one results
	        ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();
	        players.add(new D_MOEAD(300));
	        players.add(new D_NSGAII(300));
	        players.add(new D_SPEA2(300,300));
	        players.add(new D_PESA2(300));
	        players.add(new D_IBEA(300,300));
	        MOAlgorithm.setRunWithOptimalParameters(true);
	        
	        ResultArena ra = new ResultArena(100);
	        
	        /*List<IndicatorName> indi =  Arrays.asList(IndicatorName.values());
		    Collections.sort(indi, new Comparator<IndicatorName>() {
		    	@Override
		    	public int compare(IndicatorName in1, IndicatorName in2) {
		    		return in1.compareTo(in2);
		    	}
		    } );*/
		    
		    List<IndicatorName> indicators = new ArrayList<>();
		    
		    
		    indicators.add(IndicatorName.IGDPlus);
		    indicators.add(IndicatorName.NativeHV);
		    indicators.add(IndicatorName.Epsilon);
		    indicators.add(IndicatorName.MaximumSpread);
		    indicators.add(IndicatorName.R2);



        	BIOMABenchmark cec = new BIOMABenchmark(indicators, 0.0000001, true); //Create banchmark
        	for (MOAlgorithm al:players) {
        		ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
        		cec.registerAlgorithm(al);
        	}
        	BankOfResults ba = new BankOfResults();
        	long initTime = System.currentTimeMillis();
        	cec.run(ra, ba, 30);
        	long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
        	System.out.println("Benchmark execution time: "+estimatedTime + "s");
        	ArrayList<Player> list = new ArrayList<Player>();
        	list.addAll(ra.recalcRangs()); //new ranks

        	Reporting.createLatexTable(list, "D:\\Benchmark results\\BIOMA_benchmark_rand.tex");
        	Reporting.savePlayersToFile(list, "D:\\Benchmark results\\BIOMA_benchmark_rand.tex",0);
        	for (Player p: list) System.out.println(p); //print ranks
	        
		    
		    /*
			long initTime = System.currentTimeMillis();
			
	    	ArrayList<MOProblem> problems = new ArrayList<MOProblem>();
	    	ra
	    	problems.add(new UnconstrainedProblem2());
	    	problems.add(new UnconstrainedProblem3());
	    	problems.add(new UnconstrainedProblem4());
	    	problems.add(new UnconstrainedProblem5());
	    	problems.add(new UnconstrainedProblem6());
	    	problems.add(new UnconstrainedProblem7());
	    	problems.add(new UnconstrainedProblem8());
	    	problems.add(new UnconstrainedProblem9());
	    	problems.add(new UnconstrainedProblem10());
			
	    	for (MOProblem mop : problems)
	    	{
	    		StringBuilder sb = new StringBuilder();
	    		// average and std for Friedman test
	    		for (IndicatorName name : indi)
	    		{
	    			sb.append(name.toString());
	    			sb.append("\n");
	    			for (MOAlgorithm p : players) {

	    				double[] results = new double[30];
	    				double sum = 0.0;
	    				double avg, sd = 0.0;
	    				for (int i = 0; i < results.length; i++) {
	    					MOTask t = new MOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, mop);

	    					try {
	    						ParetoSolution best = p.run(t);
	    						QualityIndicator qi = IndicatorFactory.createIndicator(name, t.getProblem());

	    						try {
	    							best.evaluate(qi);
	    						} catch (Exception e) {
	    							e.printStackTrace();
	    						}

	    						//System.out.println(best.getEval());
	    						results[i] = best.getEval();
	    						sum+= best.getEval();
	    					} catch (StopCriteriaException e) {
	    						e.printStackTrace();
	    					}
	    				}
	    				avg = sum / results.length;

	    				for (int i=0; i < results.length;i++)
	    				{
	    					sd += Math.pow(results[i] - avg, 2);
	    				}
	    				sd = Math.sqrt(sd / results.length);
	    				sb.append(p.getAlgorithmInfo().getPublishedAcronym()+" avg: "+avg+" Std: "+sd+" ");
	    				System.out.println(name.toString()+" "+p.getAlgorithmInfo().getPublishedAcronym()+" avg: "+avg+" Std: "+sd);
	    			}
	    			sb.append("\n");
	    		}
	    		System.out.println(sb.toString());
	    		long estimatedTime = System.currentTimeMillis() - initTime;
	    		System.out.println("Total execution time: "+estimatedTime + "ms");

	    		try {
	    			FileOutputStream fos = new FileOutputStream("D:\\Benchmark results\\Friedman_"+mop.name+".txt");
	    			OutputStreamWriter osw = new OutputStreamWriter(fos);
	    			BufferedWriter bw = new BufferedWriter(osw);

	    			bw.write(sb.toString());
	    			bw.close();

	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	    	}
	    	*/
		}

	}
