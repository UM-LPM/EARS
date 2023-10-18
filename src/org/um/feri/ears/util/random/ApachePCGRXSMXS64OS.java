package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePCGRXSMXS64OS extends ApacheRandomGenerator {

    public ApachePCGRXSMXS64OS() {
        super(RandomSource.PCG_RXS_M_XS_64_OS);
    }

    public ApachePCGRXSMXS64OS(long seed) {
        super(RandomSource.PCG_RXS_M_XS_64_OS, seed);
    }
}