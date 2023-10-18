package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheWell512A extends ApacheRandomGenerator {

    public ApacheWell512A() {
        super(RandomSource.WELL_512_A);
    }

    public ApacheWell512A(long seed) {
        super(RandomSource.WELL_512_A, seed);
    }
}