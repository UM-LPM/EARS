package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoShiRo128PP extends ApacheRandomGenerator {

    public ApacheXoShiRo128PP() {
        super(RandomSource.XO_SHI_RO_128_PP);
    }

    public ApacheXoShiRo128PP(long seed) {
        super(RandomSource.XO_SHI_RO_128_PP, seed);
    }
}