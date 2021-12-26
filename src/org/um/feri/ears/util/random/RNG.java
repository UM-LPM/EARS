package org.um.feri.ears.util.random;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class RNG {
    private static HashMap<RandomSource, UniformRandomProvider> randomGenerators = new HashMap<>();
    private static RandomSource selectedGenerator = RandomSource.MT; // the default selected random generator

    static {
        for (RandomSource rs : RandomSource.values()) {
            if (rs == RandomSource.TWO_CMRES_SELECT) continue; // not working
            randomGenerators.put(rs, rs.create());
        }
    }

    public static void main(String[] args) {

        long seed = 0;
        RNG.setSeed(seed);

        for (RandomSource rs : RandomSource.values()) {
            if (rs == RandomSource.TWO_CMRES_SELECT) continue; // not working
            RNG.setSelectedRandomGenerator(rs);
            System.out.println(rs.toString());
            for (int i = 0; i < 10; i++) {
                System.out.println(RNG.nextDouble());
            }
            System.out.println();
        }
    }

    public static void setSeed(long seed) {
        for (RandomSource rs : RandomSource.values()) {
            if (rs == RandomSource.TWO_CMRES_SELECT) continue; // not working
            randomGenerators.put(selectedGenerator, rs.create(seed));
        }
    }

    public void setSeed(RandomSource randomGenerator, long seed) {
        randomGenerators.put(randomGenerator, randomGenerator.create(seed));
    }

    public static void setSelectedRandomGenerator(RandomSource selectedGenerator) {
        RNG.selectedGenerator = selectedGenerator;
    }


    //TODO remove old Mersenne Twister
    //TODO nextGaussian
    //TODO annotations from apache
    //TODO test benchmark before replacing Util.rnd + speed test
    //TODO separate methods for default and specific RNG
    //TODO synchronized causes overhead, ThreadLocalRandom, separate generator for each thread

    /**
     * @param upperBound
     * @return the next random, uniformly distributed {@code int} value between
     * {@code 0} (inclusive) and {@code upperBound} (exclusive).
     */
    public static int nextInt(int upperBound) {
        return randomGenerators.get(selectedGenerator).nextInt(upperBound);
    }

    public static int nextInt() {
        return randomGenerators.get(selectedGenerator).nextInt();
    }

    /**
     * Returns the next random, uniformly distributed {@code int} value between
     * {@code 0} (inclusive) and {@code n} (exclusive).
     *
     * @return the next random, uniformly distributed {@code int} value between
     * {@code lowerBound} (inclusive) and {@code upperBound} (exclusive).
     */
    public static int nextInt(int lowerBound, int upperBound) {
        return lowerBound + randomGenerators.get(selectedGenerator).nextInt(upperBound - lowerBound);
    }

    public static double cauchyrnd(double a, double b) {
        return a + b * Math.tan(Math.PI * (nextFloat() - 0.5));
    }

    public static double nextDouble() {
        return randomGenerators.get(selectedGenerator).nextDouble();
    }

    /**
     * Returns the next random, uniformly distributed {@code double} value
     * between {@code lowerBound} and {@code upperBound}.
     *
     * @return the next random, uniformly distributed {@code double} value
     * between {@code lowerBound} and {@code upperBound}
     */
    public static double nextDouble(double lowerBound, double upperBound) {
        return lowerBound + randomGenerators.get(selectedGenerator).nextDouble() * (upperBound - lowerBound);
    }

    public static double nextFloat() {
        return randomGenerators.get(selectedGenerator).nextFloat();
    }

    /**
     * Shuffles the elements of the specified array using the same algorithm as
     * {@link Collections#shuffle}.
     *
     * @param <T>   the type of element stored in the array
     * @param array the array to be shuffled
     */
    public static <T> void shuffle(T[] array) {
        for (int i = array.length - 1; i >= 1; i--) {
            int j = nextInt(i + 1);

            if (i != j) {
                T temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
    }

    /**
     * Shuffles the elements of the specified list using the same algorithm as
     * {@link Collections#shuffle}.
     *
     * @param <T>  the type of element stored in the List
     * @param list the list to be shuffled
     */
    public static <T> void shuffle(List<T> list) {
        for (int i = list.size() - 1; i >= 1; i--) {
            int j = nextInt(i + 1);

            if (i != j) {
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
    }

    /**
     * Generates a random permutation array between 0 and (length - 1)
     *
     * @param length the length of the array
     * @return a random permutation array between 0 and (length - 1)
     */
    public static int[] randomPermutation(int length) {
        int[] permutation = new int[length];

        for (int i = 0; i < length; i++) {
            permutation[i] = i;
        }

        shuffle(permutation);

        return permutation;
    }

    /**
     * Shuffles the elements of the specified array using the same algorithm as
     * {@link Collections#shuffle}.
     *
     * @param array the array to be shuffled
     */
    public static void shuffle(int[] array) {
        for (int i = array.length - 1; i >= 1; i--) {
            int j = nextInt(i + 1);

            if (i != j) {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
    }

    /**
     * Returns a randomly selected item from the specified list.
     *
     * @param <T>  the type of the elements stored in the list
     * @param list the list from which the item is randomly selected
     * @return a randomly selected item from the specified list
     */
    public static <T> T nextItem(List<T> list) {
        return list.get(nextInt(list.size()));
    }

    /**
     * Returns the next random, uniformly distributed {@code boolean} value.
     *
     * @return the next random, uniformly distributed {@code boolean} value.
     */
    public static boolean nextBoolean() {
        return randomGenerators.get(selectedGenerator).nextBoolean();
    }

    public static double nextGaussian() {
        return 0;
    } //TODO

    /**
     * Returns a random real number from a Gaussian distribution with mean &mu;
     * and standard deviation &sigma;.
     *
     * @param mu    the mean
     * @param sigma the standard deviation
     * @return a real number distributed according to the Gaussian distribution
     * with mean <tt>mu</tt> and standard deviation <tt>sigma</tt>
     */
    public static double nextGaussian(double mu, double sigma) {
        return mu + sigma * nextGaussian();
    }
}
