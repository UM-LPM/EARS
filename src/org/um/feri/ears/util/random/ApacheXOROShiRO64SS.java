package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOROShiRO64SS extends ApacheRandomGenerator {

    public ApacheXOROShiRO64SS() {
        super(RandomSource.XO_RO_SHI_RO_64_SS);
    }

    public ApacheXOROShiRO64SS(long seed) {
        super(RandomSource.XO_RO_SHI_RO_64_SS, seed);
    }
}