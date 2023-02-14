package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
 https://www.sfu.ca/~ssurjano/rastr.html
 http://infinity77.net/global_optimization/test_functions_nd_R.html#go_benchmark.Rastrigin
 http://benchmarkfcns.xyz/benchmarkfcns/rastriginfcn.html
 */
public class Rastrigin extends DoubleProblem {

    public Rastrigin(int d) {
        super(d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.12));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.12));
        name = "Rastrigin";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(x[i], 2) - 10 * cos(2 * PI * x[i]);
        }
        return fitness + 10 * numberOfDimensions;
    }
}
