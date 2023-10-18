package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOSHIRO256PP extends ApacheRandomGenerator {

    public ApacheXOSHIRO256PP() {
        super(RandomSource.XO_SHI_RO_256_PP);
    }

    public ApacheXOSHIRO256PP(long seed) {
        super(RandomSource.XO_SHI_RO_256_PP, seed);
    }
}