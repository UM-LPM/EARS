package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOSHIRO128Plus extends ApacheRandomGenerator {

    public ApacheXOSHIRO128Plus() {
        super(RandomSource.XO_SHI_RO_128_PLUS);
    }

    public ApacheXOSHIRO128Plus(long seed) {
        super(RandomSource.XO_SHI_RO_128_PLUS, seed);
    }
}