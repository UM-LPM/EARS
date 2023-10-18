package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheSplitMix64 extends ApacheRandomGenerator {

    public ApacheSplitMix64() {
        super(RandomSource.SPLIT_MIX_64);
    }

    public ApacheSplitMix64(long seed) {
        super(RandomSource.SPLIT_MIX_64, seed);
    }
}