package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePcgMcgXshRr32 extends ApacheRandomGenerator {

    public ApachePcgMcgXshRr32() {
        super(RandomSource.PCG_MCG_XSH_RR_32);
    }

    public ApachePcgMcgXshRr32(long seed) {
        super(RandomSource.PCG_MCG_XSH_RR_32, seed);
    }
}