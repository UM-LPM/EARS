package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoShiRo256PP extends ApacheRandomGenerator {

    public ApacheXoShiRo256PP() {
        super(RandomSource.XO_SHI_RO_256_PP);
    }

    public ApacheXoShiRo256PP(long seed) {
        super(RandomSource.XO_SHI_RO_256_PP, seed);
    }
}