package org.um.feri.ears.problems.unconstrained.cec2005;

import java.util.ArrayList;
import java.util.Collections;

public class F03 extends CEC2005Base {

    static final public String DEFAULT_FILE_DATA = "high_cond_elliptic_rot_data.txt";
    static final public String DEFAULT_FILE_MX_PREFIX = "Elliptic_M_D";
    static final public String DEFAULT_FILE_MX_SUFFIX = ".txt";

    private final double[][] m_matrix;

    // In order to avoid excessive memory allocation,
    // a fixed memory buffer is allocated for each function object.
    private double[] m_z;
    private double[] m_zM;

    private double constant;

    public F03(int d) {
        super(d, 3);

        name = "Shifted Rotated High Conditioned Elliptic Function";

        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));

        m_o = new double[d];
        m_matrix = new double[d][d];

        m_z = new double[d];
        m_zM = new double[d];

        // Load the shifted global optimum
        loadRowVectorFromFile(DEFAULT_FILE_DATA, d, m_o);
        // Load the matrix
        loadMatrixFromFile(DEFAULT_FILE_MX_PREFIX + d + DEFAULT_FILE_MX_SUFFIX, d, d, m_matrix);
        optimum[0] = m_o;
        constant = Math.pow(1.0e6, 1.0 / (d - 1.0));
    }

    @Override
    public double eval(double[] x) {

        double result = 0.0;

        shift(m_z, x, m_o);
        rotate(m_zM, m_z, m_matrix);

        double sum = 0.0;

        for (int i = 0; i < numberOfDimensions; i++) {
            sum += Math.pow(constant, i) * m_zM[i] * m_zM[i];
        }

        result = sum + m_biases[funcNum - 1];

        return result;
    }

}
