package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoShiRo256P extends ApacheRandomGenerator {

    public ApacheXoShiRo256P() {
        super(RandomSource.XO_SHI_RO_256_PLUS);
    }

    public ApacheXoShiRo256P(long seed) {
        super(RandomSource.XO_SHI_RO_256_PLUS, seed);
    }
}