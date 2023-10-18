package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheJdk extends ApacheRandomGenerator {
    public ApacheJdk() {
        super(RandomSource.JDK);
    }

    public ApacheJdk(long seed) {
        super(RandomSource.JDK, seed);
    }
}
