package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.gwo.GWO;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingRPUOed30;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class SORatingExample {

    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis()); //set the seed of the random generator
        RatingBenchmark.debugPrint = false; //prints one on one results
        //add algorithms to a list
        ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
        algorithms.add(new ABC());
        algorithms.add(new GWO());
        algorithms.add(new TLBOAlgorithm());
        algorithms.add(new RandomWalkAlgorithm());
        algorithms.add(new JADE());
        ResultArena ra = new ResultArena(100); // the result arena holds information about the players (wins, loses, draws, ratings...)

        RatingRPUOed30 rpuoed30 = new RatingRPUOed30(); // benchmark with prepared tasks and settings

        rpuoed30.registerAlgorithms(algorithms);  // register the algorithms in the benchmark

        for (Algorithm al:algorithms) {
            ra.addPlayer(al, al.getID()); // add players with initial ratings
        }
        BankOfResults ba = new BankOfResults();

        rpuoed30.run(ra, ba, 10); //start the tournament with 10 runs/repetitions

        //display the leaderboard
        ArrayList<Player> list = ra.getPlayers();
        for (Player p: list) System.out.println(p);
    }
}
