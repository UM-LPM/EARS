package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePCGRXSMXS64 extends ApacheRandomGenerator {

    public ApachePCGRXSMXS64() {
        super(RandomSource.PCG_RXS_M_XS_64);
    }

    public ApachePCGRXSMXS64(long seed) {
        super(RandomSource.PCG_RXS_M_XS_64, seed);
    }
}