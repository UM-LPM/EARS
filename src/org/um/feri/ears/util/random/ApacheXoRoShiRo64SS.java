package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoRoShiRo64SS extends ApacheRandomGenerator {

    public ApacheXoRoShiRo64SS() {
        super(RandomSource.XO_RO_SHI_RO_64_SS);
    }

    public ApacheXoRoShiRo64SS(long seed) {
        super(RandomSource.XO_RO_SHI_RO_64_SS, seed);
    }
}