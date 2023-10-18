package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXOROShiRO1024S extends ApacheRandomGenerator {

    public ApacheXOROShiRO1024S() {
        super(RandomSource.XO_RO_SHI_RO_1024_S);
    }

    public ApacheXOROShiRO1024S(long seed) {
        super(RandomSource.XO_RO_SHI_RO_1024_S, seed);
    }
}
