package org.um.feri.ears.statistic.friedman;

import java.util.ArrayList;
import java.util.Vector;

import org.um.feri.ears.algorithms.so.de.DE;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomSearch;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.BenchmarkResults;
import org.um.feri.ears.benchmark.CEC2010Benchmark;
import org.um.feri.ears.benchmark.BenchmarkRunner;
import org.um.feri.ears.statistic.rating_system.Player;

public class EARS_Friedman {
    public static void main(String[] args) {
    	CEC2010Benchmark b2 = new CEC2010Benchmark(0.001);
        BenchmarkRunner m = new BenchmarkRunner(false, false, b2) ;
        m.addAlgorithm(new RandomSearch()); // RWSi
        //m.addAlgorithm(new BeeColonyAlgorithm());  // ABC
        m.addAlgorithm(new TLBOAlgorithm());       // TLBO
        m.addAlgorithm(new ES1p1sAlgorithm()); // ES
		for (DE.Strategy strategy : DE.Strategy.values())
			m.addAlgorithm(new DE(strategy, 20));
		m.run(25);
        BenchmarkResults br = m.getBenchmarkResults();
        FriedmanTransport fr = FriedmanTransport.calc4Friedman(br.getResultsByAlgorithm());
        fr.print();

        //System.out.println(br);
        System.out.println(m);
        
        ArrayList<Player> vsi = m.getPlayers();
        
        Friedman_2.setStatistics(fr.getDatasets(), fr.getAlgoritms(), fr.getMean());
        Results[] statistics_results = Friedman_2.getResults();
        
		Vector<String> al = fr.getAlgoritms();
		

		
		StringBuffer sb_new = new StringBuffer();
		sb_new.append("Algorithm").append('\t');
		sb_new.append("Average rank").append('\t');
		sb_new.append("Rating").append('\t');
		sb_new.append("Rating dev.").append('\t');
		sb_new.append("Rating interval").append('\t');
		sb_new.append("Sig. Diff. Nemenyi's test").append('\t');
		sb_new.append("Sig. Diff. Holm's test").append('\t');
		sb_new.append("Sig. Diff. Shaffer's test").append('\t');
		sb_new.append('\n');
		
       
        for (int k=0; k<vsi.size();k++) {
        	for (int l=0;l<statistics_results.length;l++){
				if (statistics_results[l].Name == (String)vsi.get(k).getId()){
					statistics_results[l].Rating = vsi.get(k).getGlicko2Rating().getRating();
					statistics_results[l].RatingDev = vsi.get(k).getGlicko2Rating().getRatingDeviation();
					statistics_results[l].RatingVol = vsi.get(k).getGlicko2Rating().getRatingVolatility();
					statistics_results[l].DiffersNeme = Friedman_2.getDiffers((String)vsi.get(k).getId(), 0.05, "Nemenyi");
					statistics_results[l].DiffersHolm = Friedman_2.getDiffers((String)vsi.get(k).getId(), 0.05, "Holm");
					statistics_results[l].DiffersShaf = Friedman_2.getDiffers((String)vsi.get(k).getId(), 0.05, "Shaffer");
					break;
				}
        	}
        }
        
		long rating_L = 0;
		long rating_R = 0;
		for (int k=0; k<al.size();k++) {
			sb_new.append(al.get(k)).append('\t');
			for (int l=0;l<statistics_results.length;l++){
				if (statistics_results[l].Name == (String)al.get(k)){
					sb_new.append(statistics_results[l].AverageRank).append('\t');
				    sb_new.append(Math.round(statistics_results[l].Rating)).append('\t');
					sb_new.append(Math.round(statistics_results[l].RatingDev)).append('\t');
					rating_L = Math.round(statistics_results[l].Rating) - 2*Math.round(statistics_results[l].RatingDev);
					rating_R = Math.round(statistics_results[l].Rating) + 2*Math.round(statistics_results[l].RatingDev);
					sb_new.append("[" + rating_L + ", " + rating_R + "]").append('\t');
					sb_new.append(statistics_results[l].DiffersNeme).append('\t');
					sb_new.append(statistics_results[l].DiffersHolm).append('\t');
					sb_new.append(statistics_results[l].DiffersShaf).append('\t');
					break;
				}
			}
			sb_new.append('\n');
		}

		System.out.println(sb_new);

		
    }
}
