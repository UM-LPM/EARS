package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoShiRo512P extends ApacheRandomGenerator {

    public ApacheXoShiRo512P() {
        super(RandomSource.XO_SHI_RO_512_PLUS);
    }

    public ApacheXoShiRo512P(long seed) {
        super(RandomSource.XO_SHI_RO_512_PLUS, seed);
    }
}