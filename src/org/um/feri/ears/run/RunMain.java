package org.um.feri.ears.run;

import java.util.ArrayList;
import java.util.Collections;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.algorithms.PlayerAlgorithmExport;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class RunMain {
	private BankOfResults allSingleProblemRunResults;
    private ArrayList<PlayerAlgorithmExport> listAll;
    private boolean printDebug;
    private boolean printSingleRunDuration;
    private ArrayList<Algorithm> players;
    private ResultArena ra;
    protected RatingBenchmark benchMark; // suopm = new RatingRPUOed2();
    private long duration;
    
    public ArrayList<PlayerAlgorithmExport> getListAll() {
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
     * @param arenaName
     * @param arenaOwner
     */
    public RunMain(boolean printDebug, boolean printSingleRunDuration, RatingBenchmark banchmark) {
        Util.rnd.setSeed(System.currentTimeMillis());
        players = new ArrayList<Algorithm>();
        this.printDebug = printDebug;
        benchMark = banchmark;
        listAll = new ArrayList<PlayerAlgorithmExport>();
        Util.rnd.setSeed(System.currentTimeMillis());
        ra = new ResultArena(100);
        this.printSingleRunDuration = printSingleRunDuration;
        allSingleProblemRunResults =  new BankOfResults();
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
        if (al==null) System.out.println("Add null algorithm");
        if (al.getAlgorithmInfo()==null) System.out.println("Add algorithm with null AlgorithmInfo "+al.getClass().getName());
        if (al.getImplementationAuthor()==null)  System.out.println("Add algorithm with null Author "+al.getClass().getName());
        PlayerAlgorithmExport<Algorithm> tmp;
        tmp = new PlayerAlgorithmExport<Algorithm>(al, startRating, 0, 0, 0);
        listAll.add(tmp);
        ra.addPlayer(tmp);
        benchMark.registerAlgorithm(al);
    }
    /**
     * In the end algorithms are sorted by their rating. Best is first!
     * 
     * @param repeat number of repetitions 
     */
    public void run(int repeat) {
        long stTime = System.currentTimeMillis();
        RatingBenchmark.debugPrint = printDebug; // prints one on one results
        RatingBenchmark.printSingleRunDuration = printSingleRunDuration;
        benchMark.run(ra,allSingleProblemRunResults, repeat);
        ra.calculteRatings();
        Collections.sort(listAll, new Player.RatingComparator());
        long endTime = System.currentTimeMillis();
        duration = endTime - stTime;
       // System.out.println("Benchmark DURATION: "+duration/1000+"s");
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Results for benchmark:").append(benchMark.getAcronym()).append("Benchmark DURATION: ("+duration/1000+"s)").append("\n").append("\n");;
        for (PlayerAlgorithmExport a:listAll) {
            sb.append(a.getPlayerId()).append(" ").append(a.getRatingData().toString()).append("\n");
        }
       return sb.toString();
    }
    public BankOfResults getBankOfResults() {
    	return allSingleProblemRunResults;
    }

}
