package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOSHIRO512SS extends ApacheRandomGenerator {

    public ApacheXOSHIRO512SS() {
        super(RandomSource.XO_SHI_RO_512_SS);
    }

    public ApacheXOSHIRO512SS(long seed) {
        super(RandomSource.XO_SHI_RO_512_SS, seed);
    }
}

