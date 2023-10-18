package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheMT extends ApacheRandomGenerator {

    public ApacheMT() {
        super(RandomSource.MT);
    }

    public ApacheMT(long seed) {
        super(RandomSource.MT, seed);
    }
}