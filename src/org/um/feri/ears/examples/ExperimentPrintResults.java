package org.um.feri.ears.examples;

import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.benchmark.BenchmarkResults;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.statistic.friedman.FriedmanTransport;
import org.um.feri.ears.rating.Rating;

public class ExperimentPrintResults {
    public static void main(String[] args) {
        RunMain m = new RunMain(false, false, new RatingRPUOed2());
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
