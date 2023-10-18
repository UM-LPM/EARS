package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePCGXSHRR32OS extends ApacheRandomGenerator {

    public ApachePCGXSHRR32OS() {
        super(RandomSource.PCG_XSH_RR_32_OS);
    }

    public ApachePCGXSHRR32OS(long seed) {
        super(RandomSource.PCG_XSH_RR_32_OS, seed);
    }
}