package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOSHIRO256Plus extends ApacheRandomGenerator {

    public ApacheXOSHIRO256Plus() {
        super(RandomSource.XO_SHI_RO_256_PLUS);
    }

    public ApacheXOSHIRO256Plus(long seed) {
        super(RandomSource.XO_SHI_RO_256_PLUS, seed);
    }
}