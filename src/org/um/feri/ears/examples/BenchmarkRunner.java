package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.BenchmarkResults;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.util.Comparator.RatingComparator;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class BenchmarkRunner {
    private ArrayList<Player> listAll;
    private boolean printDebug;
    private ArrayList<Algorithm> players;
    protected Benchmark benchMark; // suopm = new RatingRPUOed2();
    private long duration;

    public ArrayList<Player> getListAll() {
        return listAll;
    }

    public boolean isPrintDebug() {
        return printDebug;
    }

    public Benchmark getBenchmark() {
        return benchMark;
    }

    public long getDuration() {
        return duration;
    }

    /**
     * Set all data!
     *
     * @param printDebug
     * @param banchmark
     */
    public BenchmarkRunner(boolean printDebug, boolean printSingleRunDuration, Benchmark banchmark) {
        Util.rnd.setSeed(System.currentTimeMillis());
        players = new ArrayList<Algorithm>();
        this.printDebug = printDebug;
        benchMark = banchmark;
        listAll = new ArrayList<>();
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
        Benchmark.printInfo = printDebug; // prints one on one results
        benchMark.run(repeat);
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

    public BenchmarkResults<Task, DoubleSolution, Algorithm> getBenchmarkResults() {
        return benchMark.getBenchmarkResults();
    }
}
