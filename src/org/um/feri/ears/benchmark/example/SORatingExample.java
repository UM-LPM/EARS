package org.um.feri.ears.benchmark.example;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingCEC2014;
import org.um.feri.ears.benchmark.RatingCEC2015;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.benchmark.RatingRPUOed30;
import org.um.feri.ears.experiment.ee.so.PSOoriginalLogging;
import org.um.feri.ears.experiment.so.pso.PSOCBCW;
import org.um.feri.ears.experiment.so.pso.PSODOP;
import org.um.feri.ears.experiment.so.pso.PSOFS;
import org.um.feri.ears.experiment.so.pso.PSOIS;
import org.um.feri.ears.experiment.so.pso.PSOIWD;
import org.um.feri.ears.experiment.so.pso.PSOIWS;
import org.um.feri.ears.experiment.so.pso.PSOM;
import org.um.feri.ears.experiment.so.pso.PSOPBC;
import org.um.feri.ears.experiment.so.pso.PSOQ;
import org.um.feri.ears.experiment.so.pso.PSOS;
import org.um.feri.ears.experiment.so.pso.PSOTS;
import org.um.feri.ears.experiment.so.pso.PSOoriginal;
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
        
      
        RatingBenchmark.debugPrint = true; //prints one on one results
        ArrayList<Algorithm> players = new ArrayList<Algorithm>();
        players.add(new PSOTS());
        players.add(new PSOS());
        players.add(new PSOQ());
        players.add(new PSOPBC());
        players.add(new PSOM());
        players.add(new PSOIWS());
        players.add(new PSOIWD());
        players.add(new PSOIS());
        players.add(new PSOFS());
        players.add(new PSODOP());
        players.add(new PSOoriginal());
        players.add(new PSOCBCW()); 
        //players.add(new ES1p1sAlgorithm());
        //players.add(new TLBOAlgorithm());
        players.add(new RandomWalkAlgorithm());
        ResultArena ra = new ResultArena(100); 
        RatingRPUOed30 suopm = new RatingRPUOed30(); //Create banchmark
        //RatingCEC2014 suopm = new RatingCEC2014();
        for (Algorithm al:players) {
            ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
            suopm.registerAlgorithm(al);
        }
        BankOfResults ba = new BankOfResults();
        suopm.run(ra, ba, 50); //repeat competition 50X
        ArrayList<Player> list = new ArrayList<Player>();
        list.addAll(ra.recalcRangs()); //new rangs
        for (Player p: list) System.out.println(p); //print rangs
        
    }
}
