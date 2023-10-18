package org.um.feri.ears.problems.unconstrained.cec2005;

import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Collections;

public class F04 extends CEC2005Base {

	static final public String DEFAULT_FILE_DATA = "schwefel_102_data.txt";

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;

	public F04(int d) {
		super("Shifted Schwefel's Problem 1.2 with Noise in Fitness", d,4);

		lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
		upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));

		// Note: dimension starts from 0
		m_o = new double[d];
		m_z = new double[d];

		// Load the shifted global optimum
		loadRowVectorFromFile(DEFAULT_FILE_DATA, d, m_o);
		decisionSpaceOptima[0] = m_o;
	}

	@Override
	public double eval(double[] x) {
		double result = 0.0;

		shift(m_z, x, m_o);

		result = schwefel_102(m_z);

		// NOISE
		// Comment the next line to remove the noise
		result *= (1.0 + 0.4 * Math.abs(RNG.nextGaussian()));

		result += m_biases[funcNum -1];

		return result;
	}

}
