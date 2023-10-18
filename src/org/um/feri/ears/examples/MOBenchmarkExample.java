package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.gde3.D_GDE3;
import org.um.feri.ears.algorithms.moo.moead_dra.D_MOEAD_DRA;
import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.algorithms.moo.spea2.D_SPEA2;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.CEC2009Benchmark;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;

import java.util.ArrayList;
import java.util.List;

public class MOBenchmarkExample {

    public static void main(String[] args) {

        Benchmark.printInfo = true; //prints one on one results

        ArrayList<MOAlgorithm<Double, NumberSolution<Double>, NumberProblem<Double>>> players = new ArrayList<>();
        players.add(new D_MOEAD_DRA());
        players.add(new D_NSGAII());
        players.add(new D_SPEA2());
        players.add(new D_GDE3());

        List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD); // add quality indicator

        CEC2009Benchmark cec = new CEC2009Benchmark(indicators, 0.0000001); //Create benchmark
        for (MOAlgorithm<Double, NumberSolution<Double>, NumberProblem<Double>> al : players) {
            cec.addAlgorithm(al);
        }

        long initTime = System.currentTimeMillis();
        cec.run(20); //repeat competition 20X
        long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
        System.out.println("Benchmark execution time: " + estimatedTime + "s");
    }
}
