package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/102-storn-s-function
 */
public class Storn extends Problem {

    int m = 3; // 1, 2 or 3

    public Storn() {
        super(2, 0, 2);

        double lower, upper;
        switch (m) {
            case 1:
                lower = -2.0;
                upper = 2.0;
                optimum[0] = new double[]{0, 1.386952327146511};
                optimum[1] = new double[]{0, -1.386952327146511};
                break;
            case 2:
                lower = -4.0;
                upper = 4.0;
                optimum[0] = new double[]{0, 2.608906424592038};
                optimum[1] = new double[]{0, -2.608906424592038};
                break;
            case 3:
                lower = -8.0;
                upper = 8.0;
                optimum[0] = new double[]{0, 4.701739810796703};
                optimum[1] = new double[]{0, -4.701739810796703};
                break;
            default:
                System.err.println("The paramter m must be 1, 2 or 3");
                return;
        }
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, lower));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, upper));

        name = "Storn";
    }

    @Override
    public double eval(double[] x) {
        return pow(10, m) * pow(x[0], 2) + pow(x[1], 2) - pow(pow(x[0], 2) + pow(x[1], 2), 2) + pow(10, -m) * pow(pow(x[0], 2) + pow(x[1], 2), 4);
    }

    @Override
    public double getGlobalOptimum() {
        switch (m) {
            case 1:
                return -0.407461605632581;
            case 2:
                return -18.058696657349238;
            case 3:
                return -227.7657499670953;
            default:
                return Double.NaN;
        }
    }
}