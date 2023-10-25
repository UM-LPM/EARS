package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoRoShiRo128SS extends ApacheRandomGenerator {

    public ApacheXoRoShiRo128SS() {
        super(RandomSource.XO_RO_SHI_RO_128_SS);
    }

    public ApacheXoRoShiRo128SS(long seed) {
        super(RandomSource.XO_RO_SHI_RO_128_SS, seed);
    }
}