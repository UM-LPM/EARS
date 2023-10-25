package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePcgMcgXshRs32 extends ApacheRandomGenerator {

    public ApachePcgMcgXshRs32() {
        super(RandomSource.PCG_MCG_XSH_RS_32);
    }

    public ApachePcgMcgXshRs32(long seed) {
        super(RandomSource.PCG_MCG_XSH_RS_32, seed);
    }
}