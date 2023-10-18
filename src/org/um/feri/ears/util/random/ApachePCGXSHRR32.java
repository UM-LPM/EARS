package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePCGXSHRR32 extends ApacheRandomGenerator {

    public ApachePCGXSHRR32() {
        super(RandomSource.PCG_XSH_RR_32);
    }

    public ApachePCGXSHRR32(long seed) {
        super(RandomSource.PCG_XSH_RR_32, seed);
    }
}