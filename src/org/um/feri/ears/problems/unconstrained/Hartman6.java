package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class Hartman6 extends Problem {

    private double[][] a;
    private double[][] p;
    private double[] c;


    public Hartman6() {
        super(6, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));
        name = "Hartman6";

        optimum[0] = new double[]{0.20168952, 0.15001069, 0.47687398, 0.27533243, 0.31165162, 0.65730054};

        a = new double[][]{
                {10, 3, 17, 3.5, 1.7, 8},
                {0.05, 10, 17, 0.1, 8, 14},
                {3, 3.5, 1.7, 10, 17, 8},
                {17, 8, 0.05, 10, 0.1, 14}
        };
        p = new double[][]{
                {0.1312, 0.1696, 0.5569, 0.0124, 0.8283, 0.5886},
                {0.2329, 0.4135, 0.8307, 0.3736, 0.1004, 0.9991},
                {0.2348, 0.1415, 0.3522, 0.2883, 0.3047, 0.6650},
                {0.4047, 0.8828, 0.8732, 0.5743, 0.1091, 0.0381}
        };
        c = new double[]{1, 1.2, 3, 3.2};

    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum;
        for (int i = 0; i < 4; i++) {
            sum = 0;
            for (int j = 0; j < numberOfDimensions; j++) {
                sum += a[i][j] * pow(x[j] - p[i][j], 2);
            }
            fitness += c[i] * exp(sum * (-1));
        }
        return fitness * -1.0;
    }

    @Override
    public double getGlobalOptimum() {
        return -3.32236801141551;
    }
}
