package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoRoShiRo64S extends ApacheRandomGenerator {

    public ApacheXoRoShiRo64S() {
        super(RandomSource.XO_RO_SHI_RO_64_S);
    }

    public ApacheXoRoShiRo64S(long seed) {
        super(RandomSource.XO_RO_SHI_RO_64_S, seed);
    }
}