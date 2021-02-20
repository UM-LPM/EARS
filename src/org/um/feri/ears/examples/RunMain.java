package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Comparator.RatingComparator;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class RunMain {
    private BankOfResults allSingleProblemRunResults;
    private ArrayList<Player> listAll;
    private boolean printDebug;
    private boolean printSingleRunDuration;
    private ArrayList<Algorithm> players;
    private ResultArena ra;
    protected RatingBenchmark benchMark; // suopm = new RatingRPUOed2();
    private long duration;

    public ArrayList<Player> getListAll() {
        return listAll;
    }

    public boolean isPrintDebug() {
        return printDebug;
    }

    public RatingBenchmark getRatingBenchmark() {
        return benchMark;
    }

    public long getDuration() {
        return duration;
    }

    public void setRa(ResultArena ra) {
        this.ra = ra;
    }

    /**
     * Set all data!
     *
     * @param printDebug
     * @param banchmark
     */
    public RunMain(boolean printDebug, boolean printSingleRunDuration, RatingBenchmark banchmark) {
        Util.rnd.setSeed(System.currentTimeMillis());
        players = new ArrayList<Algorithm>();
        this.printDebug = printDebug;
        benchMark = banchmark;
        listAll = new ArrayList<Player>();
        ra = new ResultArena(100);
        this.printSingleRunDuration = printSingleRunDuration;
        allSingleProblemRunResults = new BankOfResults();
    }

    /**
     * Add algorithms in arena.
     * Then run!
     *
     * @param al
     * @param startRating
     */
    public void addAlgorithm(Algorithm al, Rating startRating) {

        players.add(al);
        if (al == null) System.out.println("Add null algorithm");
        if (al.getAlgorithmInfo() == null)
            System.out.println("Add algorithm with null AlgorithmInfo " + al.getClass().getName());
        if (al.getImplementationAuthor() == null)
            System.out.println("Add algorithm with null Author " + al.getClass().getName());
        Player tmp = new Player(al, al.getID(), startRating, 0, 0, 0);
        listAll.add(tmp);
        benchMark.addAlgorithm(al);
    }

    /**
     * In the end algorithms are sorted by their rating. Best is first!
     *
     * @param repeat number of repetitions
     */
    public void run(int repeat) {
        long stTime = System.currentTimeMillis();
        RatingBenchmark.printInfo = printDebug; // prints one on one results
        benchMark.run(allSingleProblemRunResults, repeat);
        listAll.sort(new RatingComparator());
        long endTime = System.currentTimeMillis();
        duration = endTime - stTime;
        // System.out.println("Benchmark DURATION: "+duration/1000+"s");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Results for benchmark:").append(benchMark.getShortName()).append("Benchmark DURATION: (" + duration / 1000 + "s)").append("\n").append("\n");
        ;
        for (Player a : listAll) {
            sb.append(a.getPlayerId()).append(" ").append(a.getRatingData().toString()).append("\n");
        }
        return sb.toString();
    }

    public BankOfResults getBankOfResults() {
        return allSingleProblemRunResults;
    }

}
