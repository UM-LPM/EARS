package org.um.feri.ears.problems.unconstrained.cec2010;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CEC2010 extends DoubleProblem {

    double[] OShift, M, y, z, x_bound;
    int funcNum;

    int[] P;
    int m = 50;

    public CEC2010(int d, int funcNum) {
        super(d, 1, 1, 0);

        this.funcNum = funcNum;
        shortName = "F" + funcNum;
        benchmarkName = "CEC2010";

        if (d <= 50)
            System.err.println("The number of diemnsions must be larger than 50!");

        //Search Range
        if (funcNum == 1 | funcNum == 4 | funcNum == 7 | funcNum == 8 | funcNum == 9 | funcNum == 12 |
                funcNum == 13 | funcNum == 14 | funcNum == 17 | funcNum == 18 | funcNum == 19 | funcNum == 20) {

            lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
            upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        }
        if (funcNum == 2 | funcNum == 5 | funcNum == 10 | funcNum == 15) {
            lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
            upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
        }
        if (funcNum == 3 | funcNum == 6 | funcNum == 11 | funcNum == 16) {
            lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -32.0));
            upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 32.0));
        }
        decisionSpaceOptima[0] = OShift;
    }

    protected List<Double> getPermutatedIndices(List<Double> x, int[] perm, int start, int length) {
        List<Double> s = new ArrayList<Double>();

        for (int i = start; i < start + length; i++) {
            s.add(x.get(perm[i]));
        }

        return s;
    }

    protected double[] getPermutatedIndices(double[] x, int[] perm, int start, int length) {
        double[] s = new double[length];
        int k = 0;
        for (int i = start; i < start + length; i++) {
            s[k++] = x[perm[i]];
        }

        return s;
    }
}
