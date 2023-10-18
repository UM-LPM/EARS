package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePCGMCGXSHRS32 extends ApacheRandomGenerator {

    public ApachePCGMCGXSHRS32() {
        super(RandomSource.PCG_MCG_XSH_RS_32);
    }

    public ApachePCGMCGXSHRS32(long seed) {
        super(RandomSource.PCG_MCG_XSH_RS_32, seed);
    }
}