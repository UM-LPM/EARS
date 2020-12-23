package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * http://al-roomi.org/benchmarks/unconstrained/n-dimensions/161-quartic-or-modified-4th-de-jong-s-function
 */
public class Quartic extends Problem {

    boolean noise = false;

    public Quartic(int d) {
        this(d, false);
    }


    public Quartic(int d, boolean noise) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.28));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.28));
        this.noise = noise;
        name = "Quartic / Modified De Jong" + (noise ? " with noise" : "");
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += (i + 1) * Math.pow(x[i], 4);
        }
        if (noise) {
            fitness += Util.nextDouble();
        }
        return fitness;
    }
}
