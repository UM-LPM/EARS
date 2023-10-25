package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApachePcgXshRr32Os extends ApacheRandomGenerator {

    public ApachePcgXshRr32Os() {
        super(RandomSource.PCG_XSH_RR_32_OS);
    }

    public ApachePcgXshRr32Os(long seed) {
        super(RandomSource.PCG_XSH_RR_32_OS, seed);
    }
}