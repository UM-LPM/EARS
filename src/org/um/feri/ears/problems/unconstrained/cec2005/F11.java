package org.um.feri.ears.problems.unconstrained.cec2005;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;

public class F11 extends CEC2005Base {

	static final public String DEFAULT_FILE_DATA = "weierstrass_data.txt";
	static final public String DEFAULT_FILE_MX_PREFIX = "weierstrass_M_D";
	static final public String DEFAULT_FILE_MX_SUFFIX = ".txt";

	static final public double PIx2 = Math.PI * 2.0;
	static final public int Kmax = 20;
	static final public double a = 0.5;
	static final public double b = 3.0;

	private final double[][] m_matrix;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_z;
	private double[] m_zM;

	public F11(int d) {
		super(d,11);

		name = "Shifted Rotated Weierstrass Function";

		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -0.5));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.5));

		m_o = new double[d];
		m_matrix = new double[d][d];

		m_z = new double[d];
		m_zM = new double[d];

		// Load the shifted global optimum
		loadRowVectorFromFile(DEFAULT_FILE_DATA, d, m_o);
		// Load the matrix
		loadMatrixFromFile(DEFAULT_FILE_MX_PREFIX + d + DEFAULT_FILE_MX_SUFFIX, d, d, m_matrix);
		optimum[0] = m_o;
	}

	@Override
	public double eval(double[] x) {
		double result = 0.0;

		shift(m_z, x, m_o);
		xA(m_zM, m_z, m_matrix);

		result = weierstrass(m_zM);

		result += m_biases[func_num-1];

		return result;
	}

}
