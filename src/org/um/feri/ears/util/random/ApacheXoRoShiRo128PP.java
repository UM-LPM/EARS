package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoRoShiRo128PP extends ApacheRandomGenerator {

    public ApacheXoRoShiRo128PP() {
        super(RandomSource.XO_RO_SHI_RO_128_PP);
    }

    public ApacheXoRoShiRo128PP(long seed) {
        super(RandomSource.XO_RO_SHI_RO_128_PP, seed);
    }
}