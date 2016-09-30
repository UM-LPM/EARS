package org.um.feri.ears.run.experiment;

import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.run.RunMainBestAlgSettings;


public class ExperimentAlgorithmWithBestSettings {
    public static void main(String[] args) {
        RunMainBestAlgSettings m = new RunMainBestAlgSettings(false, false, new RatingRPUOed2()) ;
        m.addAlgorithm(new RandomWalkAlgorithm(),new Rating(1500, 350, 0.06));
        m.addAlgorithm(new RandomWalkAMAlgorithm(),new Rating(1500, 350, 0.06));
        m.addAlgorithm(new ES1p1sAlgorithm(),new Rating(1500, 350, 0.06));
        //m.addAlgorithm(new SwarmAlgorithm(),new Rating(1500, 350, 0.06));
        //m.addAlgorithm(new BeeColonyAlgorithm(),new Rating(1500, 350, 0.06));
        m.addAlgorithm(new TLBOAlgorithm(),new Rating(1500, 350, 0.06));
        for (int k=1;k<11;k++)
            m.addAlgorithm(new DEAlgorithm(k,20),new Rating(1500, 350, 0.06));
            
        m.addAlgorithm(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin, 20),new Rating(1500, 350, 0.06));
        System.out.println(m);        
        m.run(30);
        System.out.println(m);
    }
}
