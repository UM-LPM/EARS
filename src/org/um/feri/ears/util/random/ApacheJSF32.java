package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheJSF32 extends ApacheRandomGenerator {

    public ApacheJSF32() {
        super(RandomSource.JSF_32);
    }

    public ApacheJSF32(long seed) {
        super(RandomSource.JSF_32, seed);
    }
}