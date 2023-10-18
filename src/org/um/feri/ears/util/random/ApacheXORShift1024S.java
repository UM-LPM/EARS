package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXORShift1024S extends ApacheRandomGenerator {

    public ApacheXORShift1024S() {
        super(RandomSource.XOR_SHIFT_1024_S);
    }

    public ApacheXORShift1024S(long seed) {
        super(RandomSource.XOR_SHIFT_1024_S, seed);
    }
}