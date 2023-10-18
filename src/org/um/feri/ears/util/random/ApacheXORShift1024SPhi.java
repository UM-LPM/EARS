package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXORShift1024SPhi extends ApacheRandomGenerator {

    public ApacheXORShift1024SPhi() {
        super(RandomSource.XOR_SHIFT_1024_S_PHI);
    }

    public ApacheXORShift1024SPhi(long seed) {
        super(RandomSource.XOR_SHIFT_1024_S_PHI, seed);
    }
}
