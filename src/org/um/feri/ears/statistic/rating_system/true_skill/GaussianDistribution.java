package org.um.feri.ears.statistic.rating_system.true_skill;

import org.um.feri.ears.statistic.rating_system.Rating;

import static java.lang.Math.*;

/**
 * Immutable representation of the Gaussian distribution of one variable. Not normalized:
 * 
 * <pre>
 *            1          -(x)^2 / (2)
 * P(x) = ----------- * e
 *        sqrt(2*pi)
 * </pre>
 * 
 * Normalized:
 * 
 * <pre>
 *               1           -(x-μ)^2 / (2*σ^2)
 * P(x) = --------------- * e
 *        σ * sqrt(2*pi)
 * </pre>
 * 
 * @see http://mathworld.wolfram.com/NormalDistribution.html
 */

public class GaussianDistribution {
	/**
	 * The Gaussian representation of a flat line.
	 **/
	public static final GaussianDistribution UNIFORM = fromPrecisionMean(0, 0);
	/** The peak of the Gaussian, μ **/
	private final double mean;
	/** The width of the Gaussian, σ, where the height drops to max/e **/
	private final double standardDeviation;
	/** The square of the standard deviation, σ^2 **/
	private final double variance;
	// Precision and PrecisionMean are used because they make multiplying and
	// dividing simpler (see the accompanying math paper for more details)
	/** 1/σ^2 **/
	private final double precision;
	/** Precision times mean, μ/σ^2 **/
	private final double precisionMean;

	/**
	 * The normalization constant multiplies the exponential and causes the integral over (-Inf,Inf) to equal 1
	 * 
	 * @return 1/sqrt(2*pi*σ)
	 */
	public double getNormalizationConstant() {
		// Great derivation of this is at
		// http://www.astro.psu.edu/~mce/A451_2/A451/downloads/notes0.pdf
		return 1.0 / (sqrt(2 * PI) * standardDeviation);
	}

	/**
	 * Private constructor that sets everything at once.
	 * <p>
	 * Only allow other constructors to use this because if the public were to mess up the relationship between the
	 * parameters, who knows what would happen?
	 */
	private GaussianDistribution(double mean, double standardDeviation, double variance, double precision,
                                 double precisionMean) {

		this.mean = mean;
		this.standardDeviation = standardDeviation;
		this.variance = variance;
		this.precision = precision;
		this.precisionMean = precisionMean;
	}

	public GaussianDistribution(double mean, double standardDeviation) {
		this(mean, standardDeviation, pow(standardDeviation, 2), 1.0 / pow(standardDeviation, 2), mean
				/ pow(standardDeviation, 2));
	}

	public GaussianDistribution(Rating rating) {
		this(rating.getRating(), rating.getRatingDeviation());
	}

	public GaussianDistribution(GaussianDistribution distribution) {
		this(distribution.mean, distribution.standardDeviation, distribution.variance, distribution.precision,
				distribution.precisionMean);
	}

	public static GaussianDistribution fromPrecisionMean(double precisionMean, double precision) {
		return new GaussianDistribution(precisionMean / precision, sqrt(1.0 / precision), 1.0 / precision, precision,
				precisionMean);
	}

	public GaussianDistribution mult(GaussianDistribution other) {
		return mult(this, other);
	}

	public static GaussianDistribution mult(GaussianDistribution left, GaussianDistribution right) {
		// Although we could use equations from
		// http://www.tina-vision.net/tina-knoppix/tina-memo/2003-003.pdf
		// for multiplication, the precision mean ones are easier to write :)
		return fromPrecisionMean(left.precisionMean + right.precisionMean, left.precision + right.precision);
	}

	/** Computes the absolute difference between two Gaussians **/
	public static double absoluteDifference(GaussianDistribution left, GaussianDistribution right) {
		return max(abs(left.precisionMean - right.precisionMean), sqrt(abs(left.precision - right.precision)));
	}

	/** Computes the absolute difference between two Gaussians **/
	public static double sub(GaussianDistribution left, GaussianDistribution right) {
		return absoluteDifference(left, right);
	}

	public static double logProductNormalization(GaussianDistribution left, GaussianDistribution right) {
		if ((left.precision == 0) || (right.precision == 0))
			return 0;
		double varianceSum = left.variance + right.variance;
		double meanDifference = left.mean - right.mean;
		double logSqrt2Pi = log(sqrt(2 * PI));
		return -logSqrt2Pi - (log(varianceSum) / 2.0) - (pow(meanDifference, 2) / (2.0 * varianceSum));
	}

	public static GaussianDistribution divide(GaussianDistribution numerator, GaussianDistribution denominator) {
		return fromPrecisionMean(numerator.precisionMean - denominator.precisionMean, numerator.precision
				- denominator.precision);
	}

	public static double logRatioNormalization(GaussianDistribution numerator, GaussianDistribution denominator) {
		if ((numerator.precision == 0) || (denominator.precision == 0))
			return 0;
		double varianceDifference = denominator.variance - numerator.variance;
		double meanDifference = numerator.mean - denominator.mean;
		double logSqrt2Pi = log(sqrt(2 * PI));
		return log(denominator.variance) + logSqrt2Pi - log(varianceDifference) / 2.0 + pow(meanDifference, 2)
				/ (2 * varianceDifference);
	}

	/**
	 * <pre>
	 *               1          -(x)^2 / (2*stdDev^2)
	 *   P(x) = ------------ * e
	 *           sqrt(2*pi)
	 * </pre>
	 * 
	 * @param x the location to evaluate a normalized Gaussian at
	 * @return the value at x of a normalized Gaussian centered at 0 of unit width.
	 * @see http://mathworld.wolfram.com/NormalDistribution.html
	 */
	public static double at(double x) {
		return at(x, 0, 1);
	}

	/**
	 * <pre>
	 *               1          -(x)^2 / (2*stdDev^2)
	 *   P(x) = ------------ * e
	 *           sqrt(2*pi)
	 * </pre>
	 * 
	 * @param x the location to evaluate a normalized Gaussian at
	 * @return the value at x of a normalized Gaussian centered at 0 of unit width.
	 * @see http://mathworld.wolfram.com/NormalDistribution.html
	 */
	public static double at(double x, double mean, double standardDeviation) {
		double multiplier = 1.0 / (standardDeviation * sqrt(2 * PI));
		double expPart = exp((-1.0 * pow(x - mean, 2.0)) / (2 * (standardDeviation * standardDeviation)));
		return multiplier * expPart;
	}

	public static double cumulativeTo(double x) {
		double invsqrt2 = -0.7071067811865476;
		double result = errorFunctionCumulativeTo(invsqrt2 * x);
		return 0.5 * result;
	}

	private static double errorFunctionCumulativeTo(double x) {
		// Derived from page 265 of Numerical Recipes 3rd Edition
		double z = abs(x);
		double t = 2.0 / (2.0 + z);
		double ty = 4 * t - 2;
		double[] coefficients = { -1.3026537197817094, 0.6419697923564902, 0.019476473204185836, -0.00956151478680863,
				-9.46595344482036E-4, 3.66839497852761E-4, 4.2523324806907E-5, -2.0278578112534E-5, -1.624290004647E-6,
				1.30365583558E-6, 1.5626441722E-8, -8.5238095915E-8, 6.529054439E-9, 5.059343495E-9, -9.91364156E-10,
				-2.27365122E-10, 9.6467911E-11, 2.394038E-12, -6.886027E-12, 8.94487E-13, 3.13092E-13, -1.12708E-13,
				3.81E-16, 7.106E-15, -1.523E-15, -9.4E-17, 1.21E-16, -2.8E-17 };
		int ncof = coefficients.length;
		double d = 0.0;
		double dd = 0.0;
		for (int j = ncof - 1; j > 0; j--) {
			double tmp = d;
			d = ty * d - dd + coefficients[j];
			dd = tmp;
		}
		double ans = t * exp(-z * z + 0.5 * (coefficients[0] + ty * d) - dd);
		return x >= 0.0 ? ans : (2.0 - ans);
	}

	private static double inverseErrorFunctionCumulativeTo(double p) {
		// From page 265 of numerical recipes
		if (p >= 2.0)
			return -100;
		if (p <= 0.0)
			return 100;
		double pp = (p < 1.0) ? p : 2 - p;
		double t = sqrt(-2 * log(pp / 2.0)); // Initial guess
		double x = -0.70711 * ((2.30753 + t * 0.27061) / (1.0 + t * (0.99229 + t * 0.04481)) - t);
		for (int j = 0; j < 2; j++) {
			double err = errorFunctionCumulativeTo(x) - pp;
			x += err / (1.1283791670955126 * exp(-(x * x)) - x * err); // Halley
		}
		return p < 1.0 ? x : -x;
	}

	public static double inverseCumulativeTo(double x, double mean, double standardDeviation) {
		// From numerical recipes, page 320
		return mean - sqrt(2) * standardDeviation * inverseErrorFunctionCumulativeTo(2 * x);
	}

	public static double inverseCumulativeTo(double x) {
		return inverseCumulativeTo(x, 0, 1);
	}

	@Override
	public String toString() {
		// Debug help
		return String.format("Mean(μ)=%f, Std-Dev(σ)=%f", mean, standardDeviation);
	}

	@Override
	@SuppressWarnings("all")
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (!(o instanceof GaussianDistribution))
			return false;
		final GaussianDistribution other = (GaussianDistribution) o;
		if (!other.canEqual((Object) this))
			return false;
		if (Double.compare(this.getMean(), other.getMean()) != 0)
			return false;
		if (Double.compare(this.getStandardDeviation(), other.getStandardDeviation()) != 0)
			return false;
		if (Double.compare(this.getVariance(), other.getVariance()) != 0)
			return false;
		if (Double.compare(this.getPrecision(), other.getPrecision()) != 0)
			return false;
		if (Double.compare(this.getPrecisionMean(), other.getPrecisionMean()) != 0)
			return false;
		return true;
	}

	@SuppressWarnings("all")
	public boolean canEqual(final Object other) {
		return other instanceof GaussianDistribution;
	}

	@Override
	@SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		final long $mean = Double.doubleToLongBits(this.getMean());
		result = result * PRIME + (int) ($mean >>> 32 ^ $mean);
		final long $standardDeviation = Double.doubleToLongBits(this.getStandardDeviation());
		result = result * PRIME + (int) ($standardDeviation >>> 32 ^ $standardDeviation);
		final long $variance = Double.doubleToLongBits(this.getVariance());
		result = result * PRIME + (int) ($variance >>> 32 ^ $variance);
		final long $precision = Double.doubleToLongBits(this.getPrecision());
		result = result * PRIME + (int) ($precision >>> 32 ^ $precision);
		final long $precisionMean = Double.doubleToLongBits(this.getPrecisionMean());
		result = result * PRIME + (int) ($precisionMean >>> 32 ^ $precisionMean);
		return result;
	}

	@SuppressWarnings("all")
	public double getMean() {
		return this.mean;
	}

	@SuppressWarnings("all")
	public double getStandardDeviation() {
		return this.standardDeviation;
	}

	@SuppressWarnings("all")
	public double getVariance() {
		return this.variance;
	}

	@SuppressWarnings("all")
	public double getPrecision() {
		return this.precision;
	}

	@SuppressWarnings("all")
	public double getPrecisionMean() {
		return this.precisionMean;
	}
}