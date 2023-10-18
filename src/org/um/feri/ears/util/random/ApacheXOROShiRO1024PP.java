package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOROShiRO1024PP extends ApacheRandomGenerator {

    public ApacheXOROShiRO1024PP() {
        super(RandomSource.XO_RO_SHI_RO_1024_PP);
    }

    public ApacheXOROShiRO1024PP(long seed) {
        super(RandomSource.XO_RO_SHI_RO_1024_PP, seed);
    }
}