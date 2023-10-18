package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheSFC32 extends ApacheRandomGenerator {

    public ApacheSFC32() {
        super(RandomSource.SFC_32);
    }

    public ApacheSFC32(long seed) {
        super(RandomSource.SFC_32, seed);
    }
}