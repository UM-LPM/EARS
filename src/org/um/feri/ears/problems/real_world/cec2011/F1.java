package org.um.feri.ears.problems.real_world.cec2011;


import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Problem function!
 *
 * @author Matej Črepinšek
 * @version 1
 **/
public class F1 extends DoubleProblem {
    /*
     * fun_num=1   Parameter Estimation for Frequency-Modulated (FM) Sound Waves,initialization range=[0,6.35], bound=[-6.4,6.35] , length of x=6.
     *
     */
    public F1() {
        super("RWP_1", 6, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -6.4));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 6.35));

        description = "RWP_1 Parameter Estimation for Frequency-Modulated (FM) Sound Waves";
    }

    public double eval(double[] x) {
        double theta = 2. * Math.PI / 100;
        double f = 0;
        double y_t, y_0_t;
        for (int t = 0; t <= 100; t++) {
            y_t = x[0] * Math.sin(x[1] * t * theta + x[2] * Math.sin(x[3] * t * theta + x[4] * Math.sin(x[5] * t * theta)));
            y_0_t = 1 * Math.sin(5 * t * theta - 1.5 * Math.sin(4.8 * t * theta + 2 * Math.sin(4.9 * t * theta)));
            f = f + (y_t - y_0_t) * (y_t - y_0_t);
        }
        return f;
    }

    @Override
    public double[] getRandomVariables() {
        //initialization range=[0,6.35]
        double[] var = new double[numberOfDimensions];
        for (int j = 0; j < numberOfDimensions; j++) {
            var[j] = RNG.nextDouble(0, 6.35);
        }
        return var;
    }

    @Override
    public NumberSolution<Double> getRandomEvaluatedSolution() {
        double[] var = getRandomVariables();
        return new NumberSolution<>(Util.toDoubleArrayList(var), eval(var));
    }
}