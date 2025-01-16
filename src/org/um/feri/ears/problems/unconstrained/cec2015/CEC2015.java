package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.DoubleProblem;

import org.um.feri.ears.problems.unconstrained.cec2015.input_data.DataReader;

import java.util.ArrayList;
import java.util.Collections;

public abstract class CEC2015 extends DoubleProblem {

    protected double[] oShift;
    protected double[] M;
    protected double[] y;
    protected double[] z;
    protected double[] xBound;
    protected int funcNum;
    protected int[] SS;


    /**
     * Expensive Test functions are only defined for D=10, 30.
     *
     * @param d       number of dimensions
     * @param funcNum Function number 1-15
     */
    public CEC2015(String name, int d, int funcNum) {
        super("CEC2015" + name, d, 1, 1, 0);

        this.funcNum = funcNum;
        if ((funcNum < 1) || (funcNum > 15)) {
            System.err.println("Function number must be between 1 and 15!");
        }

        benchmarkName = "CEC20015";
        shortName = "F" + funcNum;

        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));

        // cf_num not correct for D2
        int cf_num = 10, i, j;

        y = new double[d];
        z = new double[d];
        xBound = new double[d];

        for (i = 0; i < d; i++) {
            xBound[i] = 100.0;
        }


        if (!((d == 10) || (d == 30))) {
            System.out.println("\nError: Expensive Test functions are only defined for D=10, 30.");
        }


        /* Load Matrix M**************************************************** */
        M = DataReader.readRotation(funcNum, d, cf_num);

        /* Load shift_data************************************************** */
        oShift = DataReader.readShiftData(funcNum, d, cf_num);

        /* Load Shuffle_data****************************************** */
        SS = DataReader.readShuffleData(funcNum, d);
        decisionSpaceOptima[0] = oShift;
        objectiveSpaceOptima[0] = funcNum * 100;
    }
}
