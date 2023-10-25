package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePcgXshRs32Os extends ApacheRandomGenerator {

    public ApachePcgXshRs32Os() {
        super(RandomSource.PCG_XSH_RS_32_OS);
    }

    public ApachePcgXshRs32Os(long seed) {
        super(RandomSource.PCG_XSH_RS_32_OS, seed);
    }
}