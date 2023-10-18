package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOROShiRO128SS extends ApacheRandomGenerator {

    public ApacheXOROShiRO128SS() {
        super(RandomSource.XO_RO_SHI_RO_128_SS);
    }

    public ApacheXOROShiRO128SS(long seed) {
        super(RandomSource.XO_RO_SHI_RO_128_SS, seed);
    }
}