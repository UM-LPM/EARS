package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Collections;

/**
 * http://al-roomi.org/benchmarks/unconstrained/n-dimensions/161-quartic-or-modified-4th-de-jong-s-function
 */
public class Quartic extends DoubleProblem {

    boolean noise = false;

    public Quartic(int d) {
        this(d, false);
    }


    public Quartic(int d, boolean noise) {
        super("Quartic / Modified De Jong" + (noise ? " with noise" : ""), d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -1.28));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.28));
        this.noise = noise;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += (i + 1) * Math.pow(x[i], 4);
        }
        if (noise) {
            fitness += RNG.nextDouble();
        }
        return fitness;
    }
}
