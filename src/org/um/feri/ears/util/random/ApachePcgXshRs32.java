package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePcgXshRs32 extends ApacheRandomGenerator {

    public ApachePcgXshRs32() {
        super(RandomSource.PCG_XSH_RS_32);
    }

    public ApachePcgXshRs32(long seed) {
        super(RandomSource.PCG_XSH_RS_32, seed);
    }
}