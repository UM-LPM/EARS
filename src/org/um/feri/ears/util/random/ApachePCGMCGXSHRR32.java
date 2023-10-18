package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePCGMCGXSHRR32 extends ApacheRandomGenerator {

    public ApachePCGMCGXSHRR32() {
        super(RandomSource.PCG_MCG_XSH_RR_32);
    }

    public ApachePCGMCGXSHRR32(long seed) {
        super(RandomSource.PCG_MCG_XSH_RR_32, seed);
    }
}