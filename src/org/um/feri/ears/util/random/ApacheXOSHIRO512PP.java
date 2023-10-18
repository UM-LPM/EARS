package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOSHIRO512PP extends ApacheRandomGenerator {

    public ApacheXOSHIRO512PP() {
        super(RandomSource.XO_SHI_RO_512_PP);
    }

    public ApacheXOSHIRO512PP(long seed) {
        super(RandomSource.XO_SHI_RO_512_PP, seed);
    }
}