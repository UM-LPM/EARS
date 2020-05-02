package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/62-muller-browns-surface-function
 */
public class MullerBrown extends Problem {

    public MullerBrown() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "MullerBrown";

        lowerLimit.set(0, -1.5);
        upperLimit.set(0, 1.0);

        lowerLimit.set(1, -0.5);
        upperLimit.set(1, 2.5);

        optimum[0] = new double[]{-0.558223638251928, 1.441725828290487};
    }

    @Override
    public double eval(double[] x) {
        double fitness =
        ((-200)*exp(  (  -1)*(pow(x[0]-  1,2)) +   0*((x[0]-  1) *(x[1]-  0))  + ( -10)*(pow(x[1] -   0,2)))
        + (-100)*exp( (  -1)*(pow(x[0]-  0,2)) +   0*((x[0]-  0) *(x[1]-0.5))  + ( -10)*(pow(x[1] - 0.5,2)))
        + (-170)*exp( (-6.5)*(pow(x[0]+0.5,2)) +  11*((x[0]+0.5) *(x[1]-1.5))  + (-6.5)*(pow(x[1] - 1.5,2)))
        + (  15)*exp( ( 0.7)*(pow(x[0]+  1,2)) + 0.6*((x[0]+  1) *(x[1]-  1))  + ( 0.7)*(pow(x[1] -   1,2))));
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -146.6995172099539;
    }
}