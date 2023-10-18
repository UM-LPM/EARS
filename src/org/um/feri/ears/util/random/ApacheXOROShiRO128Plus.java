package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOROShiRO128Plus extends ApacheRandomGenerator {

    public ApacheXOROShiRO128Plus() {
        super(RandomSource.XO_RO_SHI_RO_128_PLUS);
    }

    public ApacheXOROShiRO128Plus(long seed) {
        super(RandomSource.XO_RO_SHI_RO_128_PLUS, seed);
    }
}