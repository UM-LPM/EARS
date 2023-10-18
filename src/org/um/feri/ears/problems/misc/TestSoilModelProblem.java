package org.um.feri.ears.problems.misc;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;

public class TestSoilModelProblem {
    public static void main(String[] args) throws StopCriterionException {
        NumberAlgorithm algorithm = new DEAlgorithm(DEAlgorithm.Strategy.JDE_RAND_1_BIN, 50);

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
                ti2 = new Task(i2, StopCriterion.EVALUATIONS, 20790, 0, 0);
                ti3 = new Task(i3, StopCriterion.EVALUATIONS, 20790, 0, 0, 0.0001);
                System.out.println(algorithm.execute(ti2).getEval());
                System.out.println(algorithm.execute(ti3).getEval());
            }
        }
    }
}
