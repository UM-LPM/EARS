package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoShiRo128P extends ApacheRandomGenerator {

    public ApacheXoShiRo128P() {
        super(RandomSource.XO_SHI_RO_128_PLUS);
    }

    public ApacheXoShiRo128P(long seed) {
        super(RandomSource.XO_SHI_RO_128_PLUS, seed);
    }
}