package org.um.feri.ears.problems.unconstrained.cec2005;

import java.util.ArrayList;
import java.util.Collections;

public class F12 extends CEC2005Base {

    static final public String DEFAULT_FILE_DATA = "schwefel_213_data.txt";

    private final double[][] m_a;
    private final double[][] m_b;

    // In order to avoid excessive memory allocation,
    // a fixed memory buffer is allocated for each function object.
    private double[] m_A;
    private double[] m_B;

    public F12(int d) {
        super("Schwefel's Problem 2.13", d, 12);

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -Math.PI));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, Math.PI));

        m_o = new double[d];
        m_a = new double[d][d];
        m_b = new double[d][d];

        m_A = new double[d];
        m_B = new double[d];

        // Data:
        //	1. a 		100x100
        //	2. b 		100x100
        //	3. alpha	1x100
        double[][] m_data = new double[100 + 100 + 1][d];

        // Load the shifted global optimum
        loadMatrixFromFile(DEFAULT_FILE_DATA, m_data.length, d, m_data);
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                m_a[i][j] = m_data[i][j];
                m_b[i][j] = m_data[100 + i][j];
            }
            m_o[i] = m_data[100 + 100][i];
        }

        for (int i = 0; i < d; i++) {
            m_A[i] = 0.0;
            for (int j = 0; j < d; j++) {
                m_A[i] += (m_a[i][j] * Math.sin(m_o[j]) + m_b[i][j] * Math.cos(m_o[j]));
            }
        }
        decisionSpaceOptima[0] = m_o;
    }

    @Override
    public double eval(double[] x) {
        double sum = 0.0;

        for (int i = 0; i < numberOfDimensions; i++) {
            m_B[i] = 0.0;
            for (int j = 0; j < numberOfDimensions; j++) {
                m_B[i] += (m_a[i][j] * Math.sin(x[j]) + m_b[i][j] * Math.cos(x[j]));
            }

            double temp = m_A[i] - m_B[i];
            sum += (temp * temp);
        }

        return sum + m_biases[funcNum - 1];
    }

}
