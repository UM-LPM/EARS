package org.um.feri.ears.examples;

import java.util.ArrayList;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.cro.CRO;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.fwa.FWA;
import org.um.feri.ears.algorithms.so.gwo.GWO;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.benchmark.RatingRPUOed30;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class SORatingExample {
	
    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis()); //set the seed of the random generator    
        RatingBenchmark.debugPrint = false; //prints one on one results
        //add algorithms to list of players
        ArrayList<Algorithm> players = new ArrayList<Algorithm>();
        players.add(new ABC());
        players.add(new CRO());
        players.add(new GWO());
        players.add(new FWA());
        players.add(new TLBOAlgorithm());
        players.add(new RandomWalkAlgorithm());
        players.add(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin));
        players.add(new JADE());
        ResultArena ra = new ResultArena(100); 
        RatingRPUOed2 rpuoed2 = new RatingRPUOed2(); //Create benchmark
        RatingRPUOed30 rpuoed30 = new RatingRPUOed30();
        
        rpuoed2.registerAlgorithms(players);
        rpuoed30.registerAlgorithms(players);
        
        //set initial rating data for each participating player
        for (Algorithm al:players) {
            ra.addPlayer(al, al.getID(), 1500, 350, 0.06,0,0,0); //init rating 1500
        }
        BankOfResults ba = new BankOfResults();
        
        //rpuoed2.run(ra, ba, 10);
        rpuoed30.run(ra, ba, 10); //start the tournament
        
        //display the leaderboard
        ArrayList<Player> list = ra.getPlayers();
        for (Player p: list) System.out.println(p);
    }
}
