package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import static java.lang.Math.*;

/*
http://benchmarkfcns.xyz/benchmarkfcns/shubertfcn.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Shubert01
 */
public class Shubert1 extends Problem {
    public Shubert1(int d) {
        super(d, 0, 18);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Shubert1";
        optimum[0] = new double[]{-7.0835, 4.8580};
        optimum[1] = new double[]{-7.0835, -7.7083};
        optimum[2] = new double[]{-1.4251, -7.0835};
        optimum[3] = new double[]{-1.4251, -0.8003};
        optimum[4] = new double[]{-7.7083, -7.0835};
        optimum[5] = new double[]{-7.7083, -0.8003};
        optimum[6] = new double[]{-0.8003, -7.7083};
        optimum[7] = new double[]{-0.8003, 4.8580};
        optimum[8] = new double[]{5.4828, -7.7083};
        optimum[9] = new double[]{5.4828, -1.4251};
        optimum[10] = new double[]{5.4828, 4.8580};
        optimum[11] = new double[]{4.8580, 5.4828};
        optimum[12] = new double[]{-7.0835, -1.4251};
        optimum[13] = new double[]{-7.7083, 5.4828};
        optimum[14] = new double[]{-0.8003, -1.4251};
        optimum[15] = new double[]{-1.4251, 5.4828};
        optimum[16] = new double[]{4.8580, -7.0835};
        optimum[17] = new double[]{4.8580, -0.8003};
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

    @Override
    public double getGlobalOptimum() {
        return -186.73090120018114;
    }
}
