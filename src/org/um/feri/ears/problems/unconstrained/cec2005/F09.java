package org.um.feri.ears.problems.unconstrained.cec2005;

import java.util.ArrayList;
import java.util.Collections;

public class F09 extends CEC2005Base {

    static final public String DEFAULT_FILE_DATA = "rastrigin_func_data.txt";

    // In order to avoid excessive memory allocation,
    // a fixed memory buffer is allocated for each function object.
    private double[] m_z;

    public F09(int d) {
        super("Shifted Rastrigin's Function", d, 9);

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));

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
        result = rastrigin(m_z);
        result += m_biases[funcNum - 1];
        return result;
    }

}
