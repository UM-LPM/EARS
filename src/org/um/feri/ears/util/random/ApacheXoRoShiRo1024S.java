package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheXoRoShiRo1024S extends ApacheRandomGenerator {

    public ApacheXoRoShiRo1024S() {
        super(RandomSource.XO_RO_SHI_RO_1024_S);
    }

    public ApacheXoRoShiRo1024S(long seed) {
        super(RandomSource.XO_RO_SHI_RO_1024_S, seed);
    }
}
