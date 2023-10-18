package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePCGXSHRS32OS extends ApacheRandomGenerator {

    public ApachePCGXSHRS32OS() {
        super(RandomSource.PCG_XSH_RS_32_OS);
    }

    public ApachePCGXSHRS32OS(long seed) {
        super(RandomSource.PCG_XSH_RS_32_OS, seed);
    }
}