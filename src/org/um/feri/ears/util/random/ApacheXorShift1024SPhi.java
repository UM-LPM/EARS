package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXorShift1024SPhi extends ApacheRandomGenerator {

    public ApacheXorShift1024SPhi() {
        super(RandomSource.XOR_SHIFT_1024_S_PHI);
    }

    public ApacheXorShift1024SPhi(long seed) {
        super(RandomSource.XOR_SHIFT_1024_S_PHI, seed);
    }
}
