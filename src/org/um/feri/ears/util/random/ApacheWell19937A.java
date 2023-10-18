package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheWell19937A extends ApacheRandomGenerator {

    public ApacheWell19937A() {
        super(RandomSource.WELL_19937_A);
    }

    public ApacheWell19937A(long seed) {
        super(RandomSource.WELL_19937_A, seed);
    }
}