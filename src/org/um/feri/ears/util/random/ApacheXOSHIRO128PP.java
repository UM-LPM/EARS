package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOSHIRO128PP extends ApacheRandomGenerator {

    public ApacheXOSHIRO128PP() {
        super(RandomSource.XO_SHI_RO_128_PP);
    }

    public ApacheXOSHIRO128PP(long seed) {
        super(RandomSource.XO_SHI_RO_128_PP, seed);
    }
}