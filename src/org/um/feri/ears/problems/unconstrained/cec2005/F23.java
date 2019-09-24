package org.um.feri.ears.problems.unconstrained.cec2005;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
public class F23 extends CEC2005Base {

	static final public String DEFAULT_FILE_DATA = "hybrid_func3_data.txt";
	static final public String DEFAULT_FILE_MX_PREFIX = "hybrid_func3_M_D";
	static final public String DEFAULT_FILE_MX_SUFFIX = ".txt";

	// Number of functions
	static final public int NUM_FUNC = 10;

	private final HybridCompositionData hcData = new LocalHCData();

	// Shifted global optimum
	private final double[][] m_o;
	private final double[][][] m_M;
	private final double[] m_sigma = {
			1.0,	1.0,	1.0,	1.0,	1.0,
			2.0,	2.0,	2.0,	2.0,	2.0
	};
	private final double[] m_lambda = {
			5.0*5.0/100.0,	5.0/100.0,	5.0*1.0,	1.0,			5.0*1.0,
			1.0,			5.0*10.0,	10.0,		5.0*5.0/200.0,	5.0/200.0
	};
	private final double[] m_func_biases = {
			0.0,	100.0,	200.0,	300.0,	400.0,
			500.0,	600.0,	700.0,	800.0,	900.0
	};
	private final double[] m_testPoint;
	private final double[] m_testPointM;
	private final double[] m_fmax;

	// In order to avoid excessive memory allocation,
	// a fixed memory buffer is allocated for each function object.
	private double[] m_w;
	private double[][] m_z;
	private double[][] m_zM;

	public F23(int d) {
		super(d,23);

		name = "Non-Continuous Rotated Hybrid Composition Function 3";

		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));

		m_o = new double[NUM_FUNC][d];
		m_M = new double[NUM_FUNC][d][d];

		m_testPoint = new double[d];
		m_testPointM = new double[d];
		m_fmax = new double[NUM_FUNC];

		m_w = new double[NUM_FUNC];
		m_z = new double[NUM_FUNC][d];
		m_zM = new double[NUM_FUNC][d];

		// Load the shifted global optimum
		loadMatrixFromFile(DEFAULT_FILE_DATA, NUM_FUNC, d, m_o);
		// Load the matrix
		loadNMatrixFromFile(DEFAULT_FILE_MX_PREFIX + d + DEFAULT_FILE_MX_SUFFIX, NUM_FUNC, d, d, m_M);

		// Initialize the hybrid composition job object
		hcData.num_func = NUM_FUNC;
		hcData.num_dim = d;
		hcData.C = 2000.0;
		hcData.sigma = m_sigma;
		hcData.biases = m_func_biases;
		hcData.lambda = m_lambda;
		hcData.o = m_o;
		hcData.M = m_M;
		hcData.w = m_w;
		hcData.z = m_z;
		hcData.zM = m_zM;
		// Calculate/estimate the fmax for all the functions involved
		for (int i = 0 ; i < NUM_FUNC ; i ++) {
			for (int j = 0 ; j < d ; j ++) {
				m_testPoint[j] = (5.0 / m_lambda[i]);
			}
			rotate(m_testPointM, m_testPoint, m_M[i]);
			m_fmax[i] = Math.abs(hcData.basicFunc(i, m_testPointM));
		}
		hcData.fmax = m_fmax;
	}

	private class LocalHCData extends HybridCompositionData {
		public double basicFunc(int func_no, double[] x) {
			double result = 0.0;
			switch(func_no) {
			case 0:
			case 1:
				result = EScafferF6(x);
				break;
			case 2:
			case 3:
				result = rastrigin(x);
				break;
			case 4:
			case 5:
				result = F8F2(x);
				break;
			case 6:
			case 7:
				result = weierstrass(x);
				break;
			case 8:
			case 9:
				result = griewank(x);
				break;
			default:
				System.err.println("func_no is out of range.");
				System.exit(-1);
			}
			return result;
		}
	}
	
	@Override
	public double[][] getOptimalVector() {
		return m_o;
	}

	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

	@Override
	public double eval(double[] x) {
		double result = 0.0;
		for (int i = 0 ; i < numberOfDimensions ; i ++) {
			x[i] = myXRound(x[i], m_o[0][i]);
		}
		result = hybrid_composition(x, hcData);
		result += m_biases[func_num-1];
		return result;
	}

}
