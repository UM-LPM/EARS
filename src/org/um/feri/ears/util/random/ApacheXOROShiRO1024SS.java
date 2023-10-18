package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOROShiRO1024SS extends ApacheRandomGenerator {

    public ApacheXOROShiRO1024SS() {
        super(RandomSource.XO_RO_SHI_RO_1024_SS);
    }

    public ApacheXOROShiRO1024SS(long seed) {
        super(RandomSource.XO_RO_SHI_RO_1024_SS, seed);
    }
}