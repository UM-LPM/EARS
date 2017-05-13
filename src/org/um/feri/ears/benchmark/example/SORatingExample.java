package org.um.feri.ears.benchmark.example;

import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.cro.CRO;
import org.um.feri.ears.algorithms.so.cs.CS;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.fwa.FWA;
import org.um.feri.ears.algorithms.so.goa.GOA;
import org.um.feri.ears.algorithms.so.gsa.GSA;
import org.um.feri.ears.algorithms.so.gwo.GWO;
import org.um.feri.ears.algorithms.so.hc.HillClimbing;
import org.um.feri.ears.algorithms.so.ica.ICA;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.rmo.RMO;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingCEC2010;
import org.um.feri.ears.benchmark.RatingCEC2015;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.benchmark.RatingRPUOed30;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.problems.unconstrained.Sphere;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class SORatingExample {
	
    public static void main(String[] args) {
        Util.rnd.setSeed(10);
        
		GOA goa = new GOA();
		
		try {
			DoubleSolution best = goa.execute(new Task(EnumStopCriteria.EVALUATIONS,3000,500,1000,0.001,new Sphere(15)));//Sphere(15) Griewank(3,2)
			System.out.println(best.getEval());
		} catch (StopCriteriaException e1) {
			e1.printStackTrace();
		}
       
        RatingBenchmark.debugPrint = false; //prints one on one results
        ArrayList<Algorithm> players = new ArrayList<Algorithm>();
        //players.add(new PSOTS());
        players.add(new ABC());
        players.add(new CRO());
        players.add(new GWO());
       // players.add(new BA());
        players.add(new GOA());
//        players.add(new FPA());
        players.add(new CS());
        players.add(new FWA());
        players.add(new ICA());
        players.add(new RMO());
        players.add(new GSA());
        players.add(new org.um.feri.ears.algorithms.so.gsa.GSAv2());
        players.add(new HillClimbing(0.01));
//        players.add(new PSOS());
//        players.add(new PSOQ());
//        players.add(new PSOPBC());
//        players.add(new PSOM());
//        players.add(new PSOIWS());
//        players.add(new PSOIWD());
//        players.add(new PSOIS());
//        players.add(new PSOFS());
//        players.add(new PSODOP());
//        players.add(new PSOoriginal());
//        players.add(new PSOCBCW()); 
        //players.add(new ES1p1sAlgorithm());
        players.add(new TLBOAlgorithm());
       // players.add(new ITLBO());
        players.add(new RandomWalkAlgorithm());
        players.add(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin));
        players.add(new JADE());
        ResultArena ra = new ResultArena(100); 
        RatingRPUOed2 rpuoed2 = new RatingRPUOed2(); //Create banchmark
        RatingRPUOed30 rpuoed30 = new RatingRPUOed30();
        RatingCEC2015 cec = new RatingCEC2015();
        
        RatingCEC2010 cec10 = new RatingCEC2010();
        
        //RatingCEC2014 suopm = new RatingCEC2014();
        rpuoed2.registerAlgorithms(players);
        rpuoed30.registerAlgorithms(players);
        cec.registerAlgorithms(players);
        cec10.registerAlgorithms(players);
        
        for (Algorithm al:players) {
            ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
        }
        BankOfResults ba = new BankOfResults();
        
        //rpuoed2.run(ra, ba, 10);
        //rpuoed30.run(ra, ba, 10);
        //cec.run(ra, ba, 10);
        cec.run(ra, ba, 10);
        
        ArrayList<Player> list = new ArrayList<Player>();
        list.addAll(ra.getPlayers());
        for (Player p: list) System.out.println(p);

    }
}
