package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.gde3.D_GDE3;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.algorithms.moo.paes.D_PAES;
import org.um.feri.ears.algorithms.moo.pesa2.D_PESAII;
import org.um.feri.ears.algorithms.moo.spea2.D_SPEA2;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingCEC2009;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkExample {
    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis());
        RatingBenchmark.printInfo = true; //prints one on one results
        ArrayList<MOAlgorithm> players = new ArrayList<>();
        players.add(new D_MOEAD_DRA());
        players.add(new D_NSGAII());
        players.add(new D_SPEA2());
        players.add(new D_PESAII());
        players.add(new D_PAES());
        players.add(new D_GDE3());

        List<IndicatorName> indicators = new ArrayList<>();
        indicators.add(IndicatorName.IGD);

        RatingCEC2009 cec = new RatingCEC2009(indicators, 0.0000001); //Create benchmark
        for (MOAlgorithm al:players) {
          cec.addAlgorithm(al);
        }
        cec.run(50); //repeat competition 50X
    }
}
