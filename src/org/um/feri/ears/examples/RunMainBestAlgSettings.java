package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.benchmark.EnumBenchmarkInfoParameters;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;

import java.util.ArrayList;
import java.util.List;

public class RunMainBestAlgSettings extends RunMain {
    public static final int NUMBER_OF_EVALUATIONS = 3;
    public static final int MAX_PARAM_TEST = 8;
    private ArrayList<Algorithm> allAlgorithmWithBestSettings;
    private ArrayList<Rating> allAlgorithmWithBestSettingsRating;

    public RunMainBestAlgSettings(boolean printDebug, boolean printSingleRunDuration, Benchmark banchmark) {
        super(printDebug, printSingleRunDuration, banchmark);
        allAlgorithmWithBestSettings = new ArrayList<Algorithm>();
        allAlgorithmWithBestSettingsRating = new ArrayList<Rating>();
        banchmark.addParameter(EnumBenchmarkInfoParameters.NUMBER_OF_TEST_CONFIGURATIONS, MAX_PARAM_TEST + "");
    }

    @Override
    public void run(int repeat) {
        benchMark.clearPlayers();
        for (int i = 0; i < allAlgorithmWithBestSettings.size(); i++) {
            super.addAlgorithm(allAlgorithmWithBestSettings.get(i), allAlgorithmWithBestSettingsRating.get(i));
        }
        super.run(repeat);
    }

    @Override
    public void addAlgorithm(Algorithm al, Rating startRating) {

        GraphDataRecorder.enabled = false;

        System.out.println(al.getID());
        long t = System.currentTimeMillis();
        allAlgorithmWithBestSettingsRating.add(startRating);
        RunMain findBestSettings = new RunMain(false, false, benchMark);
        //System.out.println("Add:"+al.getID());
        List<AlgorithmBase> allSettings = al.getAlgorithmParameterTest(benchMark.getParameters(), 8);
        //allSettings.add(al);
        if (allSettings.size() == 0) {
            allAlgorithmWithBestSettings.add(al);
            return;
        }
        if (allSettings.size() == 1) {
            allAlgorithmWithBestSettings.add((Algorithm) allSettings.get(0));
            return;
        }
        if (allSettings.size() > 1) {
            Algorithm a;
            for (int k = 0; k < allSettings.size(); k++) {
                a = (Algorithm) allSettings.get(k);
                a.setTempAlgorithmInfo(new AlgorithmInfo("" + k,"" + k, ""));
                findBestSettings.addAlgorithm(a, new Rating(startRating));
            }
            findBestSettings.run(NUMBER_OF_EVALUATIONS);
            //System.out.println(findBestSettings);
            ArrayList<Player> all = findBestSettings.getListAll(); //sorted
            a = (Algorithm) all.get(0).getAlgorithm();
            System.out.println(a.getID());
            a.setAlgorithmInfoFromTmp();
            a.resetDuration();
            allAlgorithmWithBestSettings.add(a);
            benchMark.clearPlayers();
            // BEST
        }

        GraphDataRecorder.enabled = true;
        System.out.println("Time min:" + ((System.currentTimeMillis() - t) / 60000));
        //System.out.println("End:"+al.getID());
    }

}
