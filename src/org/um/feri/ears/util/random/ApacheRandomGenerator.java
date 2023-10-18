package org.um.feri.ears.util.random;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;


public class ApacheRandomGenerator implements RandomGenerator {

    private UniformRandomProvider rng;

    public ApacheRandomGenerator(RandomSource randomSource) {
        rng = randomSource.create();
    }

    public ApacheRandomGenerator(RandomSource randomSource, long seed) {
        rng = randomSource.create(seed);
    }

    @Override
    public void nextBytes(byte[] bytes) {
        rng.nextBytes(bytes);
    }

    @Override
    public void nextBytes(byte[] bytes, int start, int len) {
        rng.nextBytes(bytes, start, len);
    }

    @Override
    public int nextInt() {
        return rng.nextInt();
    }

    @Override
    public int nextInt(int n) {
        return rng.nextInt(n);
    }

    @Override
    public long nextLong() {
        return rng.nextLong();
    }

    @Override
    public long nextLong(long n) {
        return rng.nextLong(n);
    }

    @Override
    public boolean nextBoolean() {
        return rng.nextBoolean();
    }

    @Override
    public float nextFloat() {
        return rng.nextFloat();
    }

    @Override
    public double nextDouble() {
        return rng.nextDouble();
    }
}
