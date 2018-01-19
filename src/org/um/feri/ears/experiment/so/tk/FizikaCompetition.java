package org.um.feri.ears.experiment.so.tk;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.cro.CRO;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.gsa.GSAv2;
import org.um.feri.ears.algorithms.so.pso.PSO;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingCEC2014;
import org.um.feri.ears.benchmark.RatingCEC2015;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.benchmark.RatingRPUOed30;
import org.um.feri.ears.experiment.so.pso.PSOTS;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.run.RunMainBestAlgSettings;
import org.um.feri.ears.util.Util;


//CEC2014 benchmark (30 problemov noter)
// 1) D = 10
// 2) D = 20
public class FizikaCompetition {

	//run metoda main
	public static void main(String[] args) {
		Util.rnd.setSeed(System.currentTimeMillis());
//		RatingBenchmark.debugPrint = true; //prints one on one results
//		RunMainBestAlgSettings rbs = new RunMainBestAlgSettings(true, false, new RatingCEC2015());
//
//
//		//algoritmi iz magistrske naloge
//		rbs.addAlgorithm(new CSS(),new Rating(1500, 350, 0.06));   
//		rbs.addAlgorithm(new EML2(),new Rating(1500, 350, 0.06));   
//		rbs.addAlgorithm(new ECBO(),new Rating(1500, 350, 0.06));   
//		rbs.addAlgorithm(new LSA(),new Rating(1500, 350, 0.06));   
//
//
//		//dodano za primerjavo
//		rbs.addAlgorithm(new PSO(),new Rating(1500, 350, 0.06));
//		rbs.addAlgorithm(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin, 20), new Rating(1500, 350, 0.06));
//
//		//30 ponovitev na funkcijo
//		rbs.run(10);
//
//		System.out.println(rbs);

		
		Util.rnd.setSeed(System.currentTimeMillis());


		RatingBenchmark.debugPrint = false; //prints one on one results
		ArrayList<Algorithm> players = new ArrayList<Algorithm>();
		players.add(new PSOTS());
		players.add(new CRO());
		players.add(new TLBOAlgorithm());
		players.add(new RandomWalkAlgorithm());
		players.add(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin));
		
		//PHYSICS BASED ALGORITHMS
		players.add(new CSS());
		players.add(new EM());
		players.add(new ECBO());
		players.add(new LSA());
		players.add(new GSAv2());
		
		ResultArena ra = new ResultArena(100); 
		//RatingRPUOed2 rpuoed2 = new RatingRPUOed2(); //Create banchmark
		//RatingRPUOed30 rpuoed30 = new RatingRPUOed30();
		RatingCEC2015 cec = new RatingCEC2015();

		//RatingCEC2014 suopm = new RatingCEC2014();
		//rpuoed2.registerAlgorithms(players);
		//rpuoed30.registerAlgorithms(players);
		cec.registerAlgorithms(players);


		for (Algorithm al:players) {
			ra.addPlayer(al, al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
		}
		BankOfResults ba = new BankOfResults();
		//rpuoed2.run(ra, ba, 50);
		//rpuoed30.run(ra, ba, 10);
		//rpuoed30.run(ra, ba, 10);
		
		cec.run(ra, ba, 1);
		
		ArrayList<Player> list = new ArrayList<Player>();
		list.addAll(ra.calculteRatings());
		for (Player p: list) System.out.println(p);

	}
}

/*
JDE/rand/1/bin 1.913,3 SD:50 ro:0,06
ECBO 1.682,4 SD:50 ro:0,06
LSA 1.651,1 SD:50 ro:0,06
CSS 1.534,2 SD:50 ro:0,06
PSO_Wiki 1.425,9 SD:50 ro:0,06
EML 1.192,2 SD:50 ro:0,06
GSA 1.101 SD:50 ro:0,06
 */
