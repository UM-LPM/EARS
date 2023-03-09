package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;
import static java.lang.Math.*;

/*
https://al-roomi.org/benchmarks/unconstrained/3-dimensions/129-schmidt-vetters-function
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.SchmidtVetters
 */
public class SchmidtVetters extends DoubleProblem {
    public SchmidtVetters() {
        super("SchmidtVetters",3, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "SchmidtVetters";

        decisionSpaceOptima[0] = new double[]{7.0708, 10.0, 3.1416};

        objectiveSpaceOptima[0] = -0.19397252244022722;
    }

    @Override
    public double eval(double[] x) {
        double aux1 = (1 / (1 + pow(x[0] - x[1], 2)));
        double aux2 = sin((Math.PI * x[1] + x[2]) / 2);
        double aux3 = exp(pow((x[0] + x[1]) / (x[1]) - 2, 2));
        return aux1 + aux2 + aux3;
    }
}