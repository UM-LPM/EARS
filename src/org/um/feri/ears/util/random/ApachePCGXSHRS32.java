package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePCGXSHRS32 extends ApacheRandomGenerator {

    public ApachePCGXSHRS32() {
        super(RandomSource.PCG_XSH_RS_32);
    }

    public ApachePCGXSHRS32(long seed) {
        super(RandomSource.PCG_XSH_RS_32, seed);
    }
}