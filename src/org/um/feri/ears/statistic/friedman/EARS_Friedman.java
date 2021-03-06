package org.um.feri.ears.statistic.friedman;

import java.util.ArrayList;
import java.util.Vector;

import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.BenchmarkResults;
import org.um.feri.ears.benchmark.RatingCEC2010;
import org.um.feri.ears.examples.RunMain;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;

public class EARS_Friedman {
    public static void main(String[] args) {
    	RatingCEC2010 b2 = new RatingCEC2010(0.001);
        RunMain m = new RunMain(false, false, b2) ;
        m.addAlgorithm(new RandomWalkAlgorithm(),new Rating(1500, 350, 0.06)); // RWSi
        //m.addAlgorithm(new BeeColonyAlgorithm(),new Rating(1500, 350, 0.06));  // ABC
        m.addAlgorithm(new TLBOAlgorithm(),new Rating(1500, 350, 0.06));       // TLBO
        m.addAlgorithm(new ES1p1sAlgorithm(),new Rating(1500, 350, 0.06)); // ES
		for (DEAlgorithm.Strategy strategy : DEAlgorithm.Strategy.values())
			m.addAlgorithm(new DEAlgorithm(strategy, 20), new Rating(1500, 350, 0.06));
		m.run(25);
        BenchmarkResults br = m.getBenchmarkResults();
        FriedmanTransport fr = FriedmanTransport.calc4Friedman(br.getResultsByAlgorithm());
        fr.print();

        //System.out.println(br);
        System.out.println(m);
        
        ArrayList<Player> vsi = m.getListAll();
        
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
				if (statistics_results[l].Name == (String)vsi.get(k).getPlayerId()){
					statistics_results[l].Rating = vsi.get(k).getRatingData().getRating();
					statistics_results[l].RatingDev = vsi.get(k).getRatingData().getRD();
					statistics_results[l].RatingVol = vsi.get(k).getRatingData().getRatingVolatility();
					statistics_results[l].DiffersNeme = Friedman_2.getDiffers((String)vsi.get(k).getPlayerId(), 0.05, "Nemenyi");
					statistics_results[l].DiffersHolm = Friedman_2.getDiffers((String)vsi.get(k).getPlayerId(), 0.05, "Holm");
					statistics_results[l].DiffersShaf = Friedman_2.getDiffers((String)vsi.get(k).getPlayerId(), 0.05, "Shaffer");
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
