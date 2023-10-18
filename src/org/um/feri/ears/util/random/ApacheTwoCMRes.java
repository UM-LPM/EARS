package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheTwoCMRes extends ApacheRandomGenerator {

    public ApacheTwoCMRes() {
        super(RandomSource.TWO_CMRES);
    }

    public ApacheTwoCMRes(long seed) {
        super(RandomSource.TWO_CMRES, seed);
    }
}