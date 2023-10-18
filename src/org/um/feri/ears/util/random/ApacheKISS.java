package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheKISS extends ApacheRandomGenerator {

    public ApacheKISS() {
        super(RandomSource.KISS);
    }

    public ApacheKISS(long seed) {
        super(RandomSource.KISS, seed);
    }
}