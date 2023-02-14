package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.cos;

/*
http://benchmarkfcns.xyz/benchmarkfcns/shubertfcn.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Shubert01
 */
public class Shubert1 extends DoubleProblem {
    public Shubert1(int d) {
        super(d, 18, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Shubert1";
        decisionSpaceOptima[0] = new double[]{-7.0835, 4.8580};
        decisionSpaceOptima[1] = new double[]{-7.0835, -7.7083};
        decisionSpaceOptima[2] = new double[]{-1.4251, -7.0835};
        decisionSpaceOptima[3] = new double[]{-1.4251, -0.8003};
        decisionSpaceOptima[4] = new double[]{-7.7083, -7.0835};
        decisionSpaceOptima[5] = new double[]{-7.7083, -0.8003};
        decisionSpaceOptima[6] = new double[]{-0.8003, -7.7083};
        decisionSpaceOptima[7] = new double[]{-0.8003, 4.8580};
        decisionSpaceOptima[8] = new double[]{5.4828, -7.7083};
        decisionSpaceOptima[9] = new double[]{5.4828, -1.4251};
        decisionSpaceOptima[10] = new double[]{5.4828, 4.8580};
        decisionSpaceOptima[11] = new double[]{4.8580, 5.4828};
        decisionSpaceOptima[12] = new double[]{-7.0835, -1.4251};
        decisionSpaceOptima[13] = new double[]{-7.7083, 5.4828};
        decisionSpaceOptima[14] = new double[]{-0.8003, -1.4251};
        decisionSpaceOptima[15] = new double[]{-1.4251, 5.4828};
        decisionSpaceOptima[16] = new double[]{4.8580, -7.0835};
        decisionSpaceOptima[17] = new double[]{4.8580, -0.8003};
        objectiveSpaceOptima[0] = -186.73090120018114;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 1.0;
        double sum;
        for (int n = 0; n < numberOfDimensions; n++) {
            sum = 0;
            for (int i = 1; i <= 5; i++) {
                sum += i * cos((i + 1) * x[n] + i);
            }
            fitness *= sum;
        }
        return fitness;
    }
}
