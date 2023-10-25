package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoRoShiRo1024PP extends ApacheRandomGenerator {

    public ApacheXoRoShiRo1024PP() {
        super(RandomSource.XO_RO_SHI_RO_1024_PP);
    }

    public ApacheXoRoShiRo1024PP(long seed) {
        super(RandomSource.XO_RO_SHI_RO_1024_PP, seed);
    }
}