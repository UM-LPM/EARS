package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoShiRo512PP extends ApacheRandomGenerator {

    public ApacheXoShiRo512PP() {
        super(RandomSource.XO_SHI_RO_512_PP);
    }

    public ApacheXoShiRo512PP(long seed) {
        super(RandomSource.XO_SHI_RO_512_PP, seed);
    }
}