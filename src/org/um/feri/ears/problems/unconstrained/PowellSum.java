package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;
import java.util.ArrayList;
import java.util.Collections;
import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/sumpow.html
http://benchmarkfcns.xyz/benchmarkfcns/powellsumfcn.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Sodp
 */

public class PowellSum extends Problem {

    public PowellSum(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));
        name = "Powell Sum"; // also known as Sum Of Different Powers
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(abs(x[i]), (i + 2));
        }
        return fitness;
    }
}