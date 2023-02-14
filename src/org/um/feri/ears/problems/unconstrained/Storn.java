package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/102-storn-s-function
 */
public class Storn extends DoubleProblem {

    int m = 3; // 1, 2 or 3

    public Storn() {
        super(2, 2, 1, 0);

        double lower, upper;
        switch (m) {
            case 1:
                lower = -2.0;
                upper = 2.0;
                decisionSpaceOptima[0] = new double[]{0, 1.386952327146511};
                decisionSpaceOptima[1] = new double[]{0, -1.386952327146511};
                objectiveSpaceOptima[0] = -0.407461605632581;
                break;
            case 2:
                lower = -4.0;
                upper = 4.0;
                decisionSpaceOptima[0] = new double[]{0, 2.608906424592038};
                decisionSpaceOptima[1] = new double[]{0, -2.608906424592038};
                objectiveSpaceOptima[0] = -18.058696657349238;
                break;
            case 3:
                lower = -8.0;
                upper = 8.0;
                decisionSpaceOptima[0] = new double[]{0, 4.701739810796703};
                decisionSpaceOptima[1] = new double[]{0, -4.701739810796703};
                objectiveSpaceOptima[0] = -227.7657499670953;
                break;
            default:
                System.err.println("The paramter m must be 1, 2 or 3");
                objectiveSpaceOptima[0] = Double.NaN;
                return;
        }
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, lower));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, upper));

        name = "Storn";
    }

    @Override
    public double eval(double[] x) {
        return pow(10, m) * pow(x[0], 2) + pow(x[1], 2) - pow(pow(x[0], 2) + pow(x[1], 2), 2) + pow(10, -m) * pow(pow(x[0], 2) + pow(x[1], 2), 4);
    }
}