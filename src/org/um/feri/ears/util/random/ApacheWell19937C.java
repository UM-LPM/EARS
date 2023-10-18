package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheWell19937C extends ApacheRandomGenerator {

    public ApacheWell19937C() {
        super(RandomSource.WELL_19937_C);
    }

    public ApacheWell19937C(long seed) {
        super(RandomSource.WELL_19937_C, seed);
    }
}