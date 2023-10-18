package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOSHIRO128SS extends ApacheRandomGenerator {

    public ApacheXOSHIRO128SS() {
        super(RandomSource.XO_SHI_RO_128_SS);
    }

    public ApacheXOSHIRO128SS(long seed) {
        super(RandomSource.XO_SHI_RO_128_SS, seed);
    }
}