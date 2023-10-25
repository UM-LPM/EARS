package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoRoShiRo1024P extends ApacheRandomGenerator {

    public ApacheXoRoShiRo1024P() {
        super(RandomSource.XO_RO_SHI_RO_128_PLUS);
    }

    public ApacheXoRoShiRo1024P(long seed) {
        super(RandomSource.XO_RO_SHI_RO_128_PLUS, seed);
    }
}