package org.um.feri.ears.util.random;

import org.apache.commons.rng.simple.RandomSource;

public class ApacheWell1024A extends ApacheRandomGenerator {

    public ApacheWell1024A() {
        super(RandomSource.WELL_1024_A);
    }

    public ApacheWell1024A(long seed) {
        super(RandomSource.WELL_1024_A, seed);
    }
}