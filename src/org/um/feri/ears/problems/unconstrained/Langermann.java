package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_L.html#go_benchmark.Langermann
 */
public class Langermann extends DoubleProblem {

    static final double[][] a = new double[][]{{3.0, 5.0, 2.0, 1.0, 7.0}, {5.0, 2.0, 1.0, 4.0, 9.0}};
    static final double[] c = new double[]{1.0, 2.0, 5.0, 2.0, 3.0};
    static final double m = 5;

    public Langermann() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Langermann";

        decisionSpaceOptima[0] = new double[]{2.00299219, 1.006096};
        objectiveSpaceOptima[0] = -5.1621259;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0.0;
        for (int i = 1; i <= m; i++) {
            double sum2 = 0.0;
            for (int j = 1; j <= numberOfDimensions; j++) {
                double xj = x[(j - 1)];
                sum2 += (xj - a[(j - 1)][(i - 1)]) * (xj - a[(j - 1)][(i - 1)]);
            }
            fitness += c[(i - 1)] * exp(-1.0 / PI * sum2) * cos(PI * sum2);
        }
        return fitness;
    }
}
