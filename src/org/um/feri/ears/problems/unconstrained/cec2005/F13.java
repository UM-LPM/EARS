package org.um.feri.ears.problems.unconstrained.cec2005;

import java.util.ArrayList;
import java.util.Collections;

public class F13 extends CEC2005Base {

    static final public String DEFAULT_FILE_DATA = "EF8F2_func_data.txt";

    // In order to avoid excessive memory allocation,
    // a fixed memory buffer is allocated for each function object.
    private double[] m_z;

    public F13(int d) {
        super(d, 13);

        name = "Shifted Expanded Griewank's plus Rosenbrock's Function";

        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -3.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));

        m_o = new double[d];
        m_z = new double[d];

        // Load the shifted global optimum
        loadRowVectorFromFile(DEFAULT_FILE_DATA, d, m_o);

        // z = x - o + 1 = x - (o - 1)
        // Do the "(o - 1)" part first
        for (int i = 0; i < d; i++) {
            m_o[i] -= 1.0;
        }
        decisionSpaceOptima[0] = m_o;
    }

    @Override
    public double eval(double[] x) {
        double result = 0.0;

        shift(m_z, x, m_o);

        result = F8F2(m_z);

        result += m_biases[funcNum - 1];

        return result;
    }

}
