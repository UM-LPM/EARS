package org.um.feri.ears.util.random;

import org.apache.commons.math3.distribution.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class RNG {


    public enum RngType {
        APACHE_JDK("JDK"),
        APACHE_WELL_512_A("Well512a"),
        APACHE_WELL_1024_A("Well1024a"),
        APACHE_WELL_19937_A("Well19937a"),
        APACHE_WELL_19937_C("Well19937c"),
        APACHE_WELL_44497_A("Well44497a"),
        APACHE_WELL_44497_B("Well44497b"),
        APACHE_MT("Mersenne Twister"),
        APACHE_ISAAC("ISAAC"),
        APACHE_SPLIT_MIX_64("SplitMix64"),
        APACHE_TWO_CMRES("TwoCmres"),
        APACHE_MT_64("Mersenne Twister 64"),
        APACHE_MWC_256("MultiplyWithCarry256"),
        APACHE_KISS("KISS"),
        APACHE_XOR_SHIFT_1024_S("XoRShift1024star"),
        APACHE_XOR_SHIFT_1024_S_PHI("XorShift1024StarPhi"),
        APACHE_XO_RO_SHI_RO_64_S("XoRoShiRo64Star"),
        APACHE_XO_RO_SHI_RO_64_SS("XoRoShiRo64StarStar"),
        APACHE_XO_SHI_RO_128_PLUS("XoShiRo128Plus"),
        APACHE_XO_SHI_RO_128_SS("XoShiRo128StarStar"),
        APACHE_XO_RO_SHI_RO_128_PLUS("XoRoShiRo128Plus"),
        APACHE_XO_RO_SHI_RO_128_SS("XoRoShiRo128StarStar"),
        APACHE_XO_SHI_RO_256_PLUS("XoShiRo256Plus"),
        APACHE_XO_SHI_RO_256_SS("XoShiRo256StarStar"),
        APACHE_XO_SHI_RO_512_PLUS("XoShiRo512Plus"),
        APACHE_XO_SHI_RO_512_SS("XoShiRo512StarStar"),
        APACHE_PCG_XSH_RR_32("PcgXshRr32"),
        APACHE_PCG_XSH_RS_32("PcgXshRs32"),
        APACHE_PCG_RXS_M_XS_64("PcgRxsMXs64"),
        APACHE_PCG_MCG_XSH_RR_32("PcgMcgXshRr32"),
        APACHE_PCG_MCG_XSH_RS_32("PcgMcgXshRs32"),
        APACHE_MSWS("MiddleSquareWeylSequence"),
        APACHE_SFC_32("SmallFastCounting32"),
        APACHE_SFC_64("SmallFastCounting64"),
        APACHE_JSF_32("JenkinsSmallFast32"),
        APACHE_JSF_64("JenkinsSmallFast64"),
        APACHE_XO_SHI_RO_128_PP("XoShiRo128PlusPlus"),
        APACHE_XO_RO_SHI_RO_128_PP("XoRoShiRo128PlusPlus"),
        APACHE_XO_SHI_RO_256_PP("XoShiRo256PlusPlus"),
        APACHE_XO_SHI_RO_512_PP("XoShiRo512PlusPlus"),
        APACHE_XO_RO_SHI_RO_1024_PP("XoRoShiRo1024PlusPlus"),
        APACHE_XO_RO_SHI_RO_1024_S("XoRoShiRo1024Star"),
        APACHE_XO_RO_SHI_RO_1024_SS("XoRoShiRo1024StarStar"),
        APACHE_PCG_XSH_RR_32_OS("PcgXshRr32Os"),
        APACHE_PCG_XSH_RS_32_OS("PcgXshRs32Os"),
        APACHE_PCG_RXS_M_XS_64_OS("PcgRxsMXs64Os");

        public final String name;
        RngType(String name) {
            this.name = name;
        }
    }

    public enum DistributionType {
        BETA("Beta"),
        CAUCHY("Cauchy"),
        EXPONENTIAL("Exponential"),
        GAUSSIAN("Gaussian"),
        GAMMA("Gamma"),
        LOGISTIC("Logistic"),
        NORMAL("Normal"),
        TRIANGULAR("Triangular"),
        UNIFORM("Uniform"),
        WEIBULL("Weibull");

        public final String name;
        DistributionType(String name) {
            this.name = name;
        }
    }

    public static Random getSelectedRng() {
        return randomGenerators.get(selectedGenerator);
    }

    private static final HashMap<RngType, RandomGenerator> randomGenerators = new HashMap<>();
    private static ConcurrentHashMap<RngType, Double> nextGaussian = new ConcurrentHashMap<>();

    private static RngType selectedGenerator = RngType.APACHE_MT_64; // the default selected random generator
    private static DistributionType selectedDistribution = DistributionType.UNIFORM; // the default selected distribution

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

    public static void setSelectedDistribution(DistributionType selectedDistribution) {
        RNG.selectedDistribution = selectedDistribution;
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
                return new ApacheXorShift1024S(seed);
            case APACHE_XOR_SHIFT_1024_S_PHI:
                return new ApacheXorShift1024SPhi(seed);
            case APACHE_XO_RO_SHI_RO_64_S:
                return new ApacheXoRoShiRo64S(seed);
            case APACHE_XO_RO_SHI_RO_64_SS:
                return new ApacheXoRoShiRo64SS(seed);
            case APACHE_XO_SHI_RO_128_PLUS:
                return new ApacheXoShiRo128P(seed);
            case APACHE_XO_SHI_RO_128_SS:
                return new ApacheXoShiRo128SS(seed);
            case APACHE_XO_RO_SHI_RO_128_PLUS:
                return new ApacheXoRoShiRo1024P(seed);
            case APACHE_XO_RO_SHI_RO_128_SS:
                return new ApacheXoRoShiRo128SS(seed);
            case APACHE_XO_SHI_RO_256_PLUS:
                return new ApacheXoShiRo256P(seed);
            case APACHE_XO_SHI_RO_256_SS:
                return new ApacheXoShiRo256SS(seed);
            case APACHE_XO_SHI_RO_512_PLUS:
                return new ApacheXoShiRo512P(seed);
            case APACHE_XO_SHI_RO_512_SS:
                return new ApacheXoShiRo512SS(seed);
            case APACHE_PCG_XSH_RR_32:
                return new ApachePcgXshRr32(seed);
            case APACHE_PCG_XSH_RS_32:
                return new ApachePcgXshRs32(seed);
            case APACHE_PCG_RXS_M_XS_64:
                return new ApachePcgRxsMXs64(seed);
            case APACHE_PCG_MCG_XSH_RR_32:
                return new ApachePcgMcgXshRr32(seed);
            case APACHE_PCG_MCG_XSH_RS_32:
                return new ApachePcgMcgXshRs32(seed);
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
                return new ApacheXoShiRo128PP(seed);
            case APACHE_XO_RO_SHI_RO_128_PP:
                return new ApacheXoRoShiRo128PP(seed);
            case APACHE_XO_SHI_RO_256_PP:
                return new ApacheXoShiRo256PP(seed);
            case APACHE_XO_SHI_RO_512_PP:
                return new ApacheXoShiRo512PP(seed);
            case APACHE_XO_RO_SHI_RO_1024_PP:
                return new ApacheXoRoShiRo1024PP(seed);
            case APACHE_XO_RO_SHI_RO_1024_S:
                return new ApacheXoRoShiRo1024S(seed);
            case APACHE_XO_RO_SHI_RO_1024_SS:
                return new ApacheXoRoShiRo1024SS(seed);
            case APACHE_PCG_XSH_RR_32_OS:
                return new ApachePcgXshRr32Os(seed);
            case APACHE_PCG_XSH_RS_32_OS:
                return new ApachePcgXshRs32Os(seed);
            case APACHE_PCG_RXS_M_XS_64_OS:
                return new ApachePcgRxsMXs64Os(seed);
            default:
                return new ApacheJdk(seed);
        }
    }

    /**
     * Returns the next random, {@code double} value
     * between {@code 0.0} (inclusive) and {@code 1.0} (exclusive) distributed based on the selected distribution.
     *
     * @return the next random, {@code double} value
     * between {@code 0.0} (inclusive) and {@code 1.0} (exclusive) distributed based on the selected distribution.
     */
    public static double nextDouble() {
        switch (selectedDistribution) {
            case UNIFORM:
                return nextUniform();
            case NORMAL:
                return generateNormalInRange();
            case EXPONENTIAL:
                return generateExponentialInRange();
            case BETA:
                return generateBeta();
            case CAUCHY:
                return generateCauchyInRange();
            case TRIANGULAR:
                return generateTriangular();
            case LOGISTIC:
                return generateLogisticInRange();
            case WEIBULL:
                return generateWeibullInRange();
            default:
                return nextUniform();
        }
    }

    /**
     * Returns the next random, {@code double} value
     * between {@code lowerBound} and {@code upperBound} distributed based on the selected distribution.
     *
     * @return the next random, {@code double} value
     * between {@code lowerBound} and {@code upperBound} distributed based on the selected distribution
     */
    public static double nextDouble(double lowerBound, double upperBound) {
        return lowerBound + nextDouble() * (upperBound - lowerBound);
    }

    /**
     * Returns the next random, uniformly distributed {@code double} value
     * between {@code 0.0} (inclusive) and {@code 1.0} (exclusive).
     *
     * @return the next random, uniformly distributed {@code double} value
     * between {@code 0.0} (inclusive) and {@code 1.0} (exclusive)
     */
    public static double nextUniform() {
        return randomGenerators.get(selectedGenerator).nextDouble();
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
        return lowerBound + nextInt(upperBound - lowerBound);
    }

    /*public static long nextLong() {
        return randomGenerators.get(selectedGenerator).nextLong();
    }

    public static long nextLong(long upperBound) {
        return randomGenerators.get(selectedGenerator).nextLong(upperBound);
    }

    public static long nextLong(long lowerBound, long upperBound) {
        return lowerBound + nextLong(upperBound - lowerBound);
    }*/

    /**
     * Returns the next random, uniformly distributed {@code long} value.
     *
     * @return the next random, uniformly distributed {@code long} value.
     */
    public static double nextCauchy(double a, double b) {
        return a + b * Math.tan(Math.PI * (nextUniform() - 0.5));
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
     * @return the next pseudorandom, Gaussian ("normally") distributed
     * {@code double} value with mean {@code 0.0} and
     * standard deviation {@code 1.0} from this random number
     * generator's sequence
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
                x = 2.0 * nextUniform() - 1.0; // Random value between -1 and 1
                y = 2.0 * nextUniform() - 1.0; // Random value between -1 and 1
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

    public static double generateNormalInRange() {
        // setting the mean and standard deviation so most of the values are in the range [0.0, 1.0]
        double mean = 0.5;
        double stdDev = 0.15;

        double generatedValue;
        do {
            generatedValue = nextGaussian(mean, stdDev);
        } while (generatedValue < 0.0 || generatedValue > 1.0);

        return generatedValue;
    }

    public static int generatePoisson(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= nextUniform();
        } while (p > L);

        return k - 1;
    }

    public static double generateExponential(double rate) {
        return -Math.log(1 - nextUniform()) / rate;
    }

    public static double generateExponentialInRange() {
        double rate = 10.0;
        double randomValue = -Math.log(1 - nextUniform()) / rate;

        // Scale the randomValue to be within [0.0, 1.0]
        return randomValue / (1 + randomValue);
    }

    static CauchyDistribution cauchyDistribution = new CauchyDistribution(0.5, 0.05);

    public static double generateCauchyInRange() {
        double generatedValue;
        do {
            generatedValue = cauchyDistribution.sample();
        } while (generatedValue < 0.0 || generatedValue > 1.0);

        return generatedValue;
    }

    static LogisticDistribution logisticDistribution = new LogisticDistribution(0.5, 0.1);
    public static double generateLogisticInRange() {
        double generatedValue;
        do {
            generatedValue = logisticDistribution.sample();
        } while (generatedValue < 0.0 || generatedValue > 1.0);

        return generatedValue;
    }
    static WeibullDistribution weibullDistribution = new WeibullDistribution(0.5, 0.1);

    static double generateWeibullInRange() {
        return  1 - Math.exp(-Math.pow(0.1 * weibullDistribution.sample(), 0.5));
    }

    static TriangularDistribution triangularDistribution = new TriangularDistribution(0.0, 0.5, 1.0);

    public static double generateTriangular() {
        return triangularDistribution.sample();
    }

    static BetaDistribution betaDistribution = new BetaDistribution(0.5, 0.5);

    public static double generateBeta() {
        return betaDistribution.sample();
    }
}
