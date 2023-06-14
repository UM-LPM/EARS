package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
    https://www.sfu.ca/~ssurjano/hart3.html
 */
public class Hartman3 extends DoubleProblem {

    private static final double[][] a = {
        {3, 10, 30},
        {0.1, 10, 35},
        {3, 10, 30},
        {0.1, 10, 35}
    };
    private static final double[][] p = {
        {0.3689, 0.1170, 0.2673},
        {0.4699, 0.4387, 0.7470},
        {0.1091, 0.8732, 0.5547},
        {0.03815, 0.5743, 0.8828}
    };
    private static final double[] c = {1, 1.2, 3, 3.2};

    public Hartman3() {
        super("Hartman3", 3, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.0));

        decisionSpaceOptima[0] = new double[]{0.1, 0.55592003, 0.85218259};
        objectiveSpaceOptima[0] = -3.86278214782076;
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
        return fitness * -1;
    }
}
