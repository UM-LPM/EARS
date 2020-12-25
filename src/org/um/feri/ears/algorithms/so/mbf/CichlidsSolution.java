package org.um.feri.ears.algorithms.so.mbf;

import org.um.feri.ears.problems.DoubleSolution;

public class CichlidsSolution extends DoubleSolution {

    public double[] movment;
    public int[] NCC;
    public DoubleSolution localBest;


    public CichlidsSolution(DoubleSolution s) {
        super(s);
        localBest = new DoubleSolution(s);
        movment = new double[s.getVariables().size()];
    }

    public CichlidsSolution(int size) {
        NCC = new int[size];
    }
}
