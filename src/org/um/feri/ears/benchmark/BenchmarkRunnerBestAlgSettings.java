package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.visualization.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.statistic.rating_system.Player;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkRunnerBestAlgSettings extends BenchmarkRunner {
    public static final int NUMBER_OF_RUNS = 3;
    public static final int MAX_PARAM_TEST = 8;
    private ArrayList<NumberAlgorithm> algorithmsWithBestSettings;

    public BenchmarkRunnerBestAlgSettings(boolean printDebug, boolean printSingleRunDuration, SOBenchmark banchmark) {
        super(printDebug, printSingleRunDuration, banchmark);
        algorithmsWithBestSettings = new ArrayList<>();
    }

    @Override
    public void run(int repeat) {
        benchmark.clearPlayers();
        for (int i = 0; i < algorithmsWithBestSettings.size(); i++) {
            super.addAlgorithm(algorithmsWithBestSettings.get(i));
        }
        super.run(repeat);
    }

    @Override
    public void addAlgorithm(NumberAlgorithm algorithm) {

        GraphDataRecorder.enabled = false;

        System.out.println(algorithm.getId());
        long t = System.currentTimeMillis();
        BenchmarkRunner findBestSettings = new BenchmarkRunner(false, false, benchmark);
        //System.out.println("Add:"+algorithm.getID());
        List<Algorithm> allSettings = algorithm.getAlgorithmParameterTest(benchmark.dimension, 8);
        //allSettings.add(algorithm);
        if (allSettings.size() == 0) {
            algorithmsWithBestSettings.add(algorithm);
            return;
        }
        if (allSettings.size() == 1) {
            algorithmsWithBestSettings.add((NumberAlgorithm) allSettings.get(0));
            return;
        }
        NumberAlgorithm bestAlgorithm;
        for (int k = 0; k < allSettings.size(); k++) {
            bestAlgorithm = (NumberAlgorithm) allSettings.get(k);
            bestAlgorithm.setTempAlgorithmInfo(new AlgorithmInfo(bestAlgorithm.getId()+ " " + k,"" + k, ""));
            findBestSettings.addAlgorithm(bestAlgorithm);
        }
        findBestSettings.run(NUMBER_OF_RUNS);
        //System.out.println(findBestSettings);
        ArrayList<Player> all = findBestSettings.getPlayers(); //sorted
        bestAlgorithm = findBestSettings.getAlgorithm(all.get(0).getId());
        System.out.println(bestAlgorithm.getId());
        bestAlgorithm.setAlgorithmInfoFromTmp();
        bestAlgorithm.resetDuration();
        algorithmsWithBestSettings.add(bestAlgorithm);
        benchmark.clearPlayers();
        // BEST

        GraphDataRecorder.enabled = true;
        System.out.println("Time min:" + ((System.currentTimeMillis() - t) / 60000));
        //System.out.println("End:"+algorithm.getID());
    }
}
