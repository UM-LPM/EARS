package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheWell44497A extends ApacheRandomGenerator {

    public ApacheWell44497A() {
        super(RandomSource.WELL_44497_A);
    }

    public ApacheWell44497A(long seed) {
        super(RandomSource.WELL_44497_A, seed);
    }
}