package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoShiRo128SS extends ApacheRandomGenerator {

    public ApacheXoShiRo128SS() {
        super(RandomSource.XO_SHI_RO_128_SS);
    }

    public ApacheXoShiRo128SS(long seed) {
        super(RandomSource.XO_SHI_RO_128_SS, seed);
    }
}