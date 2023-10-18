package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOSHIRO256SS extends ApacheRandomGenerator {

    public ApacheXOSHIRO256SS() {
        super(RandomSource.XO_SHI_RO_256_SS);
    }

    public ApacheXOSHIRO256SS(long seed) {
        super(RandomSource.XO_SHI_RO_256_SS, seed);
    }
}