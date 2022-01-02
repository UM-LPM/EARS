package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.benchmark.BenchmarkResults;
import org.um.feri.ears.benchmark.RPUOed2Benchmark;
import org.um.feri.ears.statistic.friedman.FriedmanTransport;
import org.um.feri.ears.statistic.glicko2.Rating;

public class ExperimentPrintResults {
    public static void main(String[] args) {
        BenchmarkRunner m = new BenchmarkRunner(false, false, new RPUOed2Benchmark());
        m.addAlgorithm(new RandomWalkAlgorithm(), new Rating(1500, 350, 0.06));
        //m.addAlgorithm(new BeeColonyAlgorithm(),new Rating(1500, 350, 0.06));
        // m.addAlgorithm(new TLBOAlgorithm(),new Rating(1500, 350, 0.06));
        // m.addAlgorithm(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin, 20),new Rating(1500, 350, 0.06));
        m.run(2);
        BenchmarkResults br = m.getBenchmarkResults();
        FriedmanTransport fr = FriedmanTransport.calc4Friedman(br.getResultsByAlgorithm());
        fr.print();
        System.out.println(br);
        System.out.println(m);
    }
}
