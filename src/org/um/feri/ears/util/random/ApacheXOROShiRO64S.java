package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOROShiRO64S extends ApacheRandomGenerator {

    public ApacheXOROShiRO64S() {
        super(RandomSource.XO_RO_SHI_RO_64_S);
    }

    public ApacheXOROShiRO64S(long seed) {
        super(RandomSource.XO_RO_SHI_RO_64_S, seed);
    }
}