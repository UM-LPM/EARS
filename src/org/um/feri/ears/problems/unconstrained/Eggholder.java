package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/**
 * https://www.sfu.ca/~ssurjano/branin.html
 */

public class Eggholder extends DoubleProblem {

    public Eggholder() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -512.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 512.0));
        name = "Eggholder";

        decisionSpaceOptima[0][0] = 512.0;
        decisionSpaceOptima[0][1] = 404.2319;
        objectiveSpaceOptima[0] = -959.6406627106155;
    }

    @Override
    public double eval(double[] x) {
        return -(x[1] + 47.0) * sin(sqrt(abs(x[1] + x[0] / 2.0 + 47.0))) - x[0] * sin(sqrt(abs(x[0] - (x[1] + 47.0))));
    }
}