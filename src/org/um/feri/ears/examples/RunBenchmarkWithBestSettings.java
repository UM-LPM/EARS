package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.so.de.DE;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomSearch;
import org.um.feri.ears.algorithms.so.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBO;
import org.um.feri.ears.benchmark.BenchmarkRunnerBestAlgSettings;
import org.um.feri.ears.benchmark.RPUOed2Benchmark;


public class RunBenchmarkWithBestSettings {
    public static void main(String[] args) {
        BenchmarkRunnerBestAlgSettings m = new BenchmarkRunnerBestAlgSettings(false, false, new RPUOed2Benchmark());
        m.addAlgorithm(new RandomSearch());
        m.addAlgorithm(new RandomWalkAMAlgorithm());
        m.addAlgorithm(new ES1p1sAlgorithm());
        //m.addAlgorithm(new SwarmAlgorithm(),new Rating(1500, 350, 0.06));
        //m.addAlgorithm(new BeeColonyAlgorithm(),new Rating(1500, 350, 0.06));
        m.addAlgorithm(new TLBO());
        for (DE.Strategy strategy : DE.Strategy.values())
            m.addAlgorithm(new DE(strategy, 20));
        System.out.println(m);
        m.run(30);
        System.out.println(m);
    }
}
