package org.um.feri.ears.problems.unconstrained.cec2005;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;

public class F05 extends CEC2005Base {

	static final public String DEFAULT_FILE_DATA = "schwefel_206_data.txt";

	private final double[][] m_A;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_B;
	private double[] m_z;

	public F05(int d) {
		super(d,5);

		name = "Schwefel's Problem 2.6 with Global Optimum on Bounds";

		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));

		m_o = new double[d];
		m_A = new double[d][d];

		m_B = new double[d];
		m_z = new double[d];

		double[][] m_data = new double[d+1][d];

		// Load the shifted global optimum
		loadMatrixFromFile(DEFAULT_FILE_DATA, d+1, d, m_data);
		for (int i = 0 ; i < d ; i ++) {
			if ((i+1) <= Math.ceil(d / 4.0))
				m_o[i] = -100.0;
			else if ((i+1) >= Math.floor((3.0 * d) / 4.0))
				m_o[i] = 100.0;
			else
				m_o[i] = m_data[0][i];
		}
		for (int i = 0 ; i < d ; i ++) {
			for (int j = 0 ; j < d ; j ++) {
				m_A[i][j] = m_data[i+1][j];
			}
		}
		Ax(m_B, m_A, m_o);
	}

	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

	@Override
	public double eval(double[] x) {

		double max = Double.NEGATIVE_INFINITY;

		Ax(m_z, m_A, x);

		for (int i = 0 ; i < numberOfDimensions ; i ++) {
			double temp = Math.abs(m_z[i] - m_B[i]);
			if (max < temp)
				max = temp;
		}

		return max + m_biases[func_num-1];
	}

}
