package org.um.feri.ears.benchmark.example;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.pso.variations.PSOCBCW;
import org.um.feri.ears.algorithms.so.pso.variations.PSODOP;
import org.um.feri.ears.algorithms.so.pso.variations.PSOFS;
import org.um.feri.ears.algorithms.so.pso.variations.PSOIS;
import org.um.feri.ears.algorithms.so.pso.variations.PSOIWD;
import org.um.feri.ears.algorithms.so.pso.variations.PSOIWS;
import org.um.feri.ears.algorithms.so.pso.variations.PSOM;
import org.um.feri.ears.algorithms.so.pso.variations.PSOPBC;
import org.um.feri.ears.algorithms.so.pso.variations.PSOQ;
import org.um.feri.ears.algorithms.so.pso.variations.PSOS;
import org.um.feri.ears.algorithms.so.pso.variations.PSOTS;
import org.um.feri.ears.algorithms.so.pso.variations.PSOoriginal;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingCEC2015;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.expirment.ee.so.PSOoriginalLogging;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.problems.unconstrained.ProblemSphere;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class SORatingExample {
	
    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis());
        
        PSOoriginalLogging psoLogging = new PSOoriginalLogging();
        
        Locale.setDefault(Locale.ENGLISH);
        try {
        	Task t = new Task(EnumStopCriteria.EVALUATIONS, 3000, 0, 0.001, new ProblemSphere(2));
        	t.enableAncestorLogging();
			psoLogging.execute(t);
			t.saveAncestorLogging(psoLogging.getID());
		} catch (StopCriteriaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
//        RatingBenchmark.debugPrint = true; //prints one on one results
//        ArrayList<Algorithm> players = new ArrayList<Algorithm>();
//        players.add(new PSOTS());
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
//        //players.add(new ES1p1sAlgorithm());
//        //players.add(new TLBOAlgorithm());
//        players.add(new RandomWalkAlgorithm());
//        ResultArena ra = new ResultArena(100); 
//        RatingRPUOed2 suopm = new RatingRPUOed2(); //Create banchmark
//        //RatingCEC2015 suopm = new RatingCEC2015();
//        for (Algorithm al:players) {
//            ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
//            suopm.registerAlgorithm(al);
//        }
//        BankOfResults ba = new BankOfResults();
//        suopm.run(ra, ba, 50); //repeat competition 50X
//        ArrayList<Player> list = new ArrayList<Player>();
//        list.addAll(ra.recalcRangs()); //new rangs
//        for (Player p: list) System.out.println(p); //print rangs
        
    }
}
