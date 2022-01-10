package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.benchmark.BenchmarkResults;
import org.um.feri.ears.benchmark.RPUOed2Benchmark;
import org.um.feri.ears.statistic.friedman.FriedmanTransport;

public class ExperimentPrintResults {
    public static void main(String[] args) {
        BenchmarkRunner m = new BenchmarkRunner(false, false, new RPUOed2Benchmark());
        m.addAlgorithm(new RandomWalkAlgorithm());
        //m.addAlgorithm(new BeeColonyAlgorithm());
        // m.addAlgorithm(new TLBOAlgorithm());
        // m.addAlgorithm(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin, 20));
        m.run(2);
        BenchmarkResults br = m.getBenchmarkResults();
        FriedmanTransport fr = FriedmanTransport.calc4Friedman(br.getResultsByAlgorithm());
        fr.print();
        System.out.println(br);
        System.out.println(m);
    }
}
