package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheMT64 extends ApacheRandomGenerator {

    public ApacheMT64() {
        super(RandomSource.MT_64);
    }

    public ApacheMT64(long seed) {
        super(RandomSource.MT_64, seed);
    }
}