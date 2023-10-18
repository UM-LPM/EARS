package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheJSF64 extends ApacheRandomGenerator {

    public ApacheJSF64() {
        super(RandomSource.JSF_64);
    }

    public ApacheJSF64(long seed) {
        super(RandomSource.JSF_64, seed);
    }
}