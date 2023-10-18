package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOSHIRO512Plus extends ApacheRandomGenerator {

    public ApacheXOSHIRO512Plus() {
        super(RandomSource.XO_SHI_RO_512_PLUS);
    }

    public ApacheXOSHIRO512Plus(long seed) {
        super(RandomSource.XO_SHI_RO_512_PLUS, seed);
    }
}