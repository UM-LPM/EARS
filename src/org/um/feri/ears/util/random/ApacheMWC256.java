package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheMWC256 extends ApacheRandomGenerator {

    public ApacheMWC256() {
        super(RandomSource.MWC_256);
    }

    public ApacheMWC256(long seed) {
        super(RandomSource.MWC_256, seed);
    }
}