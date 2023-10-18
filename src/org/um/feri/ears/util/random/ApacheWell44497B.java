package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheWell44497B extends ApacheRandomGenerator {

    public ApacheWell44497B() {
        super(RandomSource.WELL_44497_B);
    }

    public ApacheWell44497B(long seed) {
        super(RandomSource.WELL_44497_B, seed);
    }
}