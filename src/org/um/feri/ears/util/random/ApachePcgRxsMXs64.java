package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePcgRxsMXs64 extends ApacheRandomGenerator {

    public ApachePcgRxsMXs64() {
        super(RandomSource.PCG_RXS_M_XS_64);
    }

    public ApachePcgRxsMXs64(long seed) {
        super(RandomSource.PCG_RXS_M_XS_64, seed);
    }
}