package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoShiRo512SS extends ApacheRandomGenerator {

    public ApacheXoShiRo512SS() {
        super(RandomSource.XO_SHI_RO_512_SS);
    }

    public ApacheXoShiRo512SS(long seed) {
        super(RandomSource.XO_SHI_RO_512_SS, seed);
    }
}

