package org.um.feri.ears.problems.unconstrained.cec2005;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;

public class F06 extends CEC2005Base {

	static final public String DEFAULT_FILE_DATA = "rosenbrock_func_data.txt";

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;

	public F06(int d) {
		super(d,6);

		name = "Shifted Rosenbrock's Function";

		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));

		// Note: dimension starts from 0
		m_o = new double[d];
		m_z = new double[d];

		// Load the shifted global optimum
		loadRowVectorFromFile(DEFAULT_FILE_DATA, d, m_o);

		// z = x - o + 1 = x - (o - 1)
		// Do the "(o - 1)" part first
		for (int i = 0 ; i < d ; i ++) {
			m_o[i] -= 1.0;
		}
		optimum[0] = m_o;
	}

	@Override
	public double eval(double[] x) {
		double result = 0.0;

		shift(m_z, x, m_o);

		result = rosenbrock(m_z);

		result += m_biases[func_num-1];

		return result;
	}

}
