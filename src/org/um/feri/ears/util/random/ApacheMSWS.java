package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheMSWS extends ApacheRandomGenerator {

    public ApacheMSWS() {
        super(RandomSource.MSWS);
    }

    public ApacheMSWS(long seed) {
        super(RandomSource.MSWS, seed);
    }
}