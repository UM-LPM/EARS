package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePcgRxsMXs64Os extends ApacheRandomGenerator {

    public ApachePcgRxsMXs64Os() {
        super(RandomSource.PCG_RXS_M_XS_64_OS);
    }

    public ApachePcgRxsMXs64Os(long seed) {
        super(RandomSource.PCG_RXS_M_XS_64_OS, seed);
    }
}