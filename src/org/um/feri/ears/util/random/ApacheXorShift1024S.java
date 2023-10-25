package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXorShift1024S extends ApacheRandomGenerator {

    public ApacheXorShift1024S() {
        super(RandomSource.XOR_SHIFT_1024_S);
    }

    public ApacheXorShift1024S(long seed) {
        super(RandomSource.XOR_SHIFT_1024_S, seed);
    }
}