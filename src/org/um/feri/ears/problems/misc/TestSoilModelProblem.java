package org.um.feri.ears.problems.misc;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class TestSoilModelProblem {
    public static void main(String[] args) throws StopCriteriaException {
        Util.rnd.setSeed(System.currentTimeMillis());

        Algorithm algorithm = new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin, 50);

        String[] data = {"TE1", "TE2", "TE3"};
        int run_num = 10;

        for (int k = 0; k < data.length; k++) {
            String m = data[k];
            System.out.println("data: " + m);
            SoilModelProblem i2 = new SoilModelProblem(3, 2, m); // numberOfDimensions, numberOfConstraints, layers, filename
            SoilModelProblem i3 = new SoilModelProblem(5, 3, m);

            Task ti2, ti3;
            for (int i = 0; i < run_num; i++) {
                System.out.println(i);
                ti2 = new Task(EnumStopCriteria.EVALUATIONS, 20790, 0, 0, 0.0001, i2);
                ti3 = new Task(EnumStopCriteria.EVALUATIONS, 20790, 0, 0, 0.0001, i3);
                System.out.println(algorithm.execute(ti2).getEval());
                System.out.println(algorithm.execute(ti3).getEval());
            }
        }
    }
}
