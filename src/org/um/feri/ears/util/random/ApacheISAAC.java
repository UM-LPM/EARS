package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheISAAC extends ApacheRandomGenerator {

    public ApacheISAAC() {
        super(RandomSource.ISAAC);
    }

    public ApacheISAAC(long seed) {
        super(RandomSource.ISAAC, seed);
    }
}
