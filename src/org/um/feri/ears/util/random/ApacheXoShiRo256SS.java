package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoShiRo256SS extends ApacheRandomGenerator {

    public ApacheXoShiRo256SS() {
        super(RandomSource.XO_SHI_RO_256_SS);
    }

    public ApacheXoShiRo256SS(long seed) {
        super(RandomSource.XO_SHI_RO_256_SS, seed);
    }
}