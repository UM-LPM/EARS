package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOROShiRO128PP extends ApacheRandomGenerator {

    public ApacheXOROShiRO128PP() {
        super(RandomSource.XO_RO_SHI_RO_128_PP);
    }

    public ApacheXOROShiRO128PP(long seed) {
        super(RandomSource.XO_RO_SHI_RO_128_PP, seed);
    }
}