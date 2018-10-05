/**
 * Insert data
 * <p>
 * 
 * @author Matej Crepinsek
 * @version 1
 * 
 *          <h3>License</h3>
 * 
 *          Copyright (c) 2011 by Matej Crepinsek. <br>
 *          All rights reserved. <br>
 * 
 *          <p>
 *          Redistribution and use in source and binary forms, with or without
 *          modification, are permitted provided that the following conditions
 *          are met:
 *          <ul>
 *          <li>Redistributions of source code must retain the above copyright
 *          notice, this list of conditions and the following disclaimer.
 *          <li>Redistributions in binary form must reproduce the above
 *          copyright notice, this list of conditions and the following
 *          disclaimer in the documentation and/or other materials provided with
 *          the distribution.
 *          <li>Neither the name of the copyright owners, their employers, nor
 *          the names of its contributors may be used to endorse or promote
 *          products derived from this software without specific prior written
 *          permission.
 *          </ul>
 *          <p>
 *          THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *          "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *          LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *          FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *          COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *          INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *          BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *          LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *          CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *          LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *          ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *          POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package org.um.feri.ears.statistic.friedman;

import java.util.ArrayList;
import java.util.Vector;

import org.um.feri.ears.algorithms.PlayerAlgorithmExport;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingCEC2010;
import org.um.feri.ears.examples.RunMain;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.problems.results.FriedmanTransport;
import org.um.feri.ears.rating.Rating;

/**
 * @author Administrator
 * 
 */
public class EARS_Friedman {
    public static void main(String[] args) {
    	RatingCEC2010 b2 = new RatingCEC2010(0.001);
        RunMain m = new RunMain(false, false, b2) ;
        m.addAlgorithm(new RandomWalkAlgorithm(),new Rating(1500, 350, 0.06)); // RWSi
        //m.addAlgorithm(new BeeColonyAlgorithm(),new Rating(1500, 350, 0.06));  // ABC
        m.addAlgorithm(new TLBOAlgorithm(),new Rating(1500, 350, 0.06));       // TLBO
        m.addAlgorithm(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin, 20),new Rating(1500, 350, 0.06));  //jDE
        m.addAlgorithm(new ES1p1sAlgorithm(),new Rating(1500, 350, 0.06)); // ES
        for (int k=1;k<11;k++)
            m.addAlgorithm(new DEAlgorithm(k,20),new Rating(1500, 350, 0.06));
        m.run(25);
        BankOfResults br = m.getBankOfResults();
        FriedmanTransport fr = br.calc4Friedman();
        fr.print();

        //System.out.println(br);
        System.out.println(m);
        
        ArrayList<PlayerAlgorithmExport> vsi = m.getListAll();  
        
        Friedman_2.setStatistics(fr.getDatasets(), fr.getAlgoritms(), fr.getMean());
        Results[] statistics_results = Friedman_2.getResults();
        
		Vector<String> al = fr.getAlgoritms();
		
		/* OUTPUT */
		
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
