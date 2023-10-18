package org.um.feri.ears.util.random;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class RNG {


    public enum RngType {
        APACHE_JDK,
        APACHE_WELL_512_A,
        APACHE_WELL_1024_A,
        APACHE_WELL_19937_A,
        APACHE_WELL_19937_C,
        APACHE_WELL_44497_A,
        APACHE_WELL_44497_B,
        APACHE_MT,
        APACHE_ISAAC,
        APACHE_SPLIT_MIX_64,
        APACHE_TWO_CMRES,
        APACHE_MT_64,
        APACHE_MWC_256,
        APACHE_KISS,
        APACHE_XOR_SHIFT_1024_S,
        APACHE_XO_RO_SHI_RO_64_S,
        APACHE_XO_RO_SHI_RO_64_SS,
        APACHE_XO_SHI_RO_128_PLUS,
        APACHE_XO_SHI_RO_128_SS,
        APACHE_XO_RO_SHI_RO_128_PLUS,
        APACHE_XO_RO_SHI_RO_128_SS,
        APACHE_XO_SHI_RO_256_PLUS,
        APACHE_XO_SHI_RO_256_SS,
        APACHE_XO_SHI_RO_512_PLUS,
        APACHE_XO_SHI_RO_512_SS,
        APACHE_PCG_XSH_RR_32,
        APACHE_PCG_XSH_RS_32,
        APACHE_PCG_RXS_M_XS_64,
        APACHE_PCG_MCG_XSH_RR_32,
        APACHE_PCG_MCG_XSH_RS_32,
        APACHE_MSWS,
        APACHE_SFC_32,
        APACHE_SFC_64,
        APACHE_JSF_32,
        APACHE_JSF_64,
        APACHE_XO_SHI_RO_128_PP,
        APACHE_XO_RO_SHI_RO_128_PP,
        APACHE_XO_SHI_RO_256_PP,
        APACHE_XO_SHI_RO_512_PP,
        APACHE_XO_RO_SHI_RO_1024_PP,
        APACHE_XO_RO_SHI_RO_1024_S,
        APACHE_XO_RO_SHI_RO_1024_SS,
        APACHE_PCG_XSH_RR_32_OS,
        APACHE_PCG_XSH_RS_32_OS,
        APACHE_PCG_RXS_M_XS_64_OS
    }

    public static Random getAsRandom() {
        RandomGenerator randomGenerator = randomGenerators.get(selectedGenerator);
        return new RandomAdapter(randomGenerator);
    }

    private static class RandomAdapter extends Random {
        private final RandomGenerator randomGenerator;

        public RandomAdapter(RandomGenerator randomGenerator) {
            this.randomGenerator = randomGenerator;
        }

        @Override
        protected int next(int bits) {
            return randomGenerator.nextInt() >>> (32 - bits);
        }
        @Override
        public double nextDouble() {
            return randomGenerator.nextInt();
        }

        @Override
        public int nextInt() {
            return randomGenerator.nextInt();
        }

        @Override
        public boolean nextBoolean() {
            return randomGenerator.nextBoolean();
        }
    }

    private static final HashMap<RngType, RandomGenerator> randomGenerators = new HashMap<>();
    private static ConcurrentHashMap<RngType, Double> nextGaussian = new ConcurrentHashMap<>();

    private static RngType selectedGenerator = RngType.APACHE_MT_64; // the default selected random generator

    static {
        long defaultSeed = System.currentTimeMillis();
        for (RngType rs : RngType.values()) {
            randomGenerators.put(rs, createRng(rs, defaultSeed));
            nextGaussian.put(rs, Double.NaN);
        }
    }

    /**
     * Sets the seed of all random generators to the same value.
     *
     * @param seed
     */
    public static void setGlobalSeed(long seed) {
        for (RngType rs : RngType.values()) {
            randomGenerators.put(rs, createRng(rs, seed));
            nextGaussian.put(rs, Double.NaN);
        }
    }

    public static void setSeed(long seed) {
        randomGenerators.put(selectedGenerator, createRng(selectedGenerator, seed));
        nextGaussian.put(selectedGenerator, Double.NaN);
    }

    public void setSeed(RngType randomGeneratorType, long seed) {
        randomGenerators.put(randomGeneratorType, createRng(randomGeneratorType, seed));
        nextGaussian.put(randomGeneratorType, Double.NaN);
    }

    public static void setSelectedRandomGenerator(RngType selectedGenerator) {
        RNG.selectedGenerator = selectedGenerator;
    }

    //TODO rename randomGenerators to use camelCase

    //TODO test benchmark before replacing Util.rnd with RNG + speed test
    //TODO separate methods for default and specific RNG
    //TODO synchronized causes overhead, ThreadLocalRandom, separate generator for each thread

    public static void main(String[] args) {
        for (RngType rs : RngType.values()) {
            selectedGenerator = rs;
            System.out.println(rs.toString());
            for (int i = 0; i < 100; i++) {
                System.out.println(nextDouble());
            }
        }
    }

    /**
     * Creates a random generator of the given type with the given seed.
     *
     * @param type
     * @param seed
     * @return RandomGenerator
     */
    public static RandomGenerator createRng(RngType type, long seed) {
        switch (type) {
            case APACHE_JDK:
                return new ApacheJdk(seed);
            case APACHE_WELL_512_A:
                return new ApacheWell512A(seed);
            case APACHE_WELL_1024_A:
                return new ApacheWell1024A(seed);
            case APACHE_WELL_19937_A:
                return new ApacheWell19937A(seed);
            case APACHE_WELL_19937_C:
                return new ApacheWell19937C(seed);
            case APACHE_WELL_44497_A:
                return new ApacheWell44497A(seed);
            case APACHE_WELL_44497_B:
                return new ApacheWell44497B(seed);
            case APACHE_MT:
                return new ApacheMT(seed);
            case APACHE_ISAAC:
                return new ApacheISAAC(seed);
            case APACHE_SPLIT_MIX_64:
                return new ApacheSplitMix64(seed);
            case APACHE_TWO_CMRES:
                return new ApacheTwoCMRes(seed);
            case APACHE_MT_64:
                return new ApacheMT64(seed);
            case APACHE_MWC_256:
                return new ApacheMWC256(seed);
            case APACHE_KISS:
                return new ApacheKISS(seed);
            case APACHE_XOR_SHIFT_1024_S:
                return new ApacheXORShift1024S(seed);
            case APACHE_XO_RO_SHI_RO_64_S:
                return new ApacheXOROShiRO64S(seed);
            case APACHE_XO_RO_SHI_RO_64_SS:
                return new ApacheXOROShiRO64SS(seed);
            case APACHE_XO_SHI_RO_128_PLUS:
                return new ApacheXOSHIRO128Plus(seed);
            case APACHE_XO_SHI_RO_128_SS:
                return new ApacheXOSHIRO128SS(seed);
            case APACHE_XO_RO_SHI_RO_128_PLUS:
                return new ApacheXOROShiRO128Plus(seed);
            case APACHE_XO_RO_SHI_RO_128_SS:
                return new ApacheXOROShiRO128SS(seed);
            case APACHE_XO_SHI_RO_256_PLUS:
                return new ApacheXOSHIRO256Plus(seed);
            case APACHE_XO_SHI_RO_256_SS:
                return new ApacheXOSHIRO256SS(seed);
            case APACHE_XO_SHI_RO_512_PLUS:
                return new ApacheXOSHIRO512Plus(seed);
            case APACHE_XO_SHI_RO_512_SS:
                return new ApacheXOSHIRO512SS(seed);
            case APACHE_PCG_XSH_RR_32:
                return new ApachePCGXSHRR32(seed);
            case APACHE_PCG_XSH_RS_32:
                return new ApachePCGXSHRS32(seed);
            case APACHE_PCG_RXS_M_XS_64:
                return new ApachePCGRXSMXS64(seed);
            case APACHE_PCG_MCG_XSH_RR_32:
                return new ApachePCGMCGXSHRR32(seed);
            case APACHE_PCG_MCG_XSH_RS_32:
                return new ApachePCGMCGXSHRS32(seed);
            case APACHE_MSWS:
                return new ApacheMSWS(seed);
            case APACHE_SFC_32:
                return new ApacheSFC32(seed);
            case APACHE_SFC_64:
                return new ApacheSFC64(seed);
            case APACHE_JSF_32:
                return new ApacheJSF32(seed);
            case APACHE_JSF_64:
                return new ApacheJSF64(seed);
            case APACHE_XO_SHI_RO_128_PP:
                return new ApacheXOSHIRO128PP(seed);
            case APACHE_XO_RO_SHI_RO_128_PP:
                return new ApacheXOROShiRO128PP(seed);
            case APACHE_XO_SHI_RO_256_PP:
                return new ApacheXOSHIRO256PP(seed);
            case APACHE_XO_SHI_RO_512_PP:
                return new ApacheXOSHIRO512PP(seed);
            case APACHE_XO_RO_SHI_RO_1024_PP:
                return new ApacheXOROShiRO1024PP(seed);
            case APACHE_XO_RO_SHI_RO_1024_S:
                return new ApacheXOROShiRO1024S(seed);
            case APACHE_XO_RO_SHI_RO_1024_SS:
                return new ApacheXOROShiRO1024SS(seed);
            case APACHE_PCG_XSH_RR_32_OS:
                return new ApachePCGXSHRR32OS(seed);
            case APACHE_PCG_XSH_RS_32_OS:
                return new ApachePCGXSHRS32OS(seed);
            case APACHE_PCG_RXS_M_XS_64_OS:
                return new ApachePCGRXSMXS64OS(seed);
            default:
                return new ApacheJdk(seed);
        }
    }

    /**
     * Returns the next random, uniformly distributed {@code int} value between
     * {@code 0} (inclusive) and {@code upperBound} (exclusive).
     *
     * @return the next random, uniformly distributed {@code int} value between
     * {@code 0} (inclusive) and {@code upperBound} (exclusive).
     */
    public static int nextInt(int upperBound) {
        return randomGenerators.get(selectedGenerator).nextInt(upperBound);
    }

    /**
     * Returns the next random, uniformly distributed {@code int} value.
     *
     * @return the next random, uniformly distributed {@code int} value.
     */
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

    /**
     * Returns the next random, uniformly distributed {@code long} value.
     *
     * @return the next random, uniformly distributed {@code long} value.
     */
    public static double nextCauchy(double a, double b) {
        return a + b * Math.tan(Math.PI * (nextFloat() - 0.5));
    }

    /**
     * Returns the next random, uniformly distributed {@code double} value
     * between {@code 0.0} (inclusive) and {@code 1.0} (exclusive).
     *
     * @return the next random, uniformly distributed {@code double} value
     * between {@code 0.0} (inclusive) and {@code 1.0} (exclusive)
     */
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

    /**
     * Returns the next random, uniformly distributed {@code float} value
     * between {@code 0.0} (inclusive) and {@code 1.0} (exclusive).
     *
     * @return the next random, uniformly distributed {@code float} value
     * between {@code 0.0} (inclusive) and {@code 1.0} (exclusive)
     */
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

    /**
     * Returns the next pseudorandom, Gaussian ("normally") distributed
     * {@code double} value with mean {@code 0.0} and standard
     * deviation {@code 1.0} from this random number generator's sequence.
     * <p>
     * The default implementation uses the <em>Polar Method</em>
     * due to G.E.P. Box, M.E. Muller and G. Marsaglia, as described in
     * D. Knuth, <u>The Art of Computer Programming</u>, 3.4.1C.</p>
     * <p>
     * The algorithm generates a pair of independent random values. One of
     * these is cached for reuse, so the full algorithm is not executed on each
     * activation.</p>
     *
     * @return  the next pseudorandom, Gaussian ("normally") distributed
     * {@code double} value with mean {@code 0.0} and
     * standard deviation {@code 1.0} from this random number
     *  generator's sequence
     */
    public static double nextGaussian() {
        double nextGaussian = RNG.nextGaussian.get(selectedGenerator);
        if (!Double.isNaN(nextGaussian)) {
            RNG.nextGaussian.put(selectedGenerator, Double.NaN);
            return nextGaussian;
        } else {
            // Generate two random values using the Box-Muller transform
            double x, y, distanceSquared;
            do {
                x = 2.0 * nextDouble() - 1.0; // Random value between -1 and 1
                y = 2.0 * nextDouble() - 1.0; // Random value between -1 and 1
                distanceSquared = x * x + y * y;
            } while (distanceSquared >= 1.0 || distanceSquared == 0.0); // Ensure the generated point is within the unit circle

            double scaleFactor = Math.sqrt(-2.0 * Math.log(distanceSquared) / distanceSquared);
            RNG.nextGaussian.put(selectedGenerator, x * scaleFactor); // Store one of the values and return the other
            return y * scaleFactor;
        }
    }

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
