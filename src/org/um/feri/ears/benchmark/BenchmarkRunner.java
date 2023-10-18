package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.util.comparator.RatingComparator;

import java.util.ArrayList;
import java.util.HashMap;

public class BenchmarkRunner {
    private ArrayList<Player> players;
    private boolean printDebug;
    private HashMap<String, NumberAlgorithm> algorithms;
    protected SOBenchmark benchmark;
    private long duration;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public NumberAlgorithm getAlgorithm(String id) {
        return algorithms.get(id);
    }

    public boolean isPrintDebug() {
        return printDebug;
    }

    public SOBenchmark getBenchmark() {
        return benchmark;
    }

    public long getDuration() {
        return duration;
    }

    public BenchmarkRunner(boolean printDebug, boolean printSingleRunDuration, SOBenchmark benchmark) {
        players = new ArrayList<>();
        algorithms = new HashMap<>();
        this.printDebug = printDebug;
        this.benchmark = benchmark;
    }

    public void addAlgorithm(NumberAlgorithm algorithm) {

        algorithms.put(algorithm.getId(), algorithm);
        players.add(new Player(algorithm.getId()));
        benchmark.addAlgorithm(algorithm);
    }

    /**
     * In the end algorithms are sorted by their rating. Best is first!
     *
     * @param repeat number of repetitions
     */
    public void run(int repeat) {
        long stTime = System.currentTimeMillis();
        Benchmark.printInfo = printDebug; // prints one on one results
        benchmark.run(repeat);
        players.sort(new RatingComparator());
        long endTime = System.currentTimeMillis();
        duration = endTime - stTime;
        // System.out.println("Benchmark DURATION: "+duration/1000+"s");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Results for benchmark:").append(benchmark.getShortName()).append("Benchmark DURATION: (" + duration / 1000 + "s)").append("\n").append("\n");
        ;
        for (Player a : players) {
            sb.append(a.getId()).append(" ").append(a.getGlicko2Rating().toString()).append("\n");
        }
        return sb.toString();
    }

    public BenchmarkResults<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> getBenchmarkResults() {
        return benchmark.getBenchmarkResults();
    }
}
