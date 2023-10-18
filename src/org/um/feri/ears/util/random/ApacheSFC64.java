package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheSFC64 extends ApacheRandomGenerator {

    public ApacheSFC64() {
        super(RandomSource.SFC_64);
    }

    public ApacheSFC64(long seed) {
        super(RandomSource.SFC_64, seed);
    }
}