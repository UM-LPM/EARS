package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePcgXshRr32 extends ApacheRandomGenerator {

    public ApachePcgXshRr32() {
        super(RandomSource.PCG_XSH_RR_32);
    }

    public ApachePcgXshRr32(long seed) {
        super(RandomSource.PCG_XSH_RR_32, seed);
    }
}