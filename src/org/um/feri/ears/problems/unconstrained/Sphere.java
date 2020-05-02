package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/spheref.html
http://benchmarkfcns.xyz/benchmarkfcns/spherefcn.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Sphere
*/
public class Sphere extends Problem {
    public Sphere(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Sphere";
    }

	@Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += +pow(x[i], 2);
        }
        return fitness;
    }
}
