package org.um.feri.ears.algorithms.so.mbf;

import org.um.feri.ears.problems.NumberSolution;

import java.util.List;

public class CichlidsSolution extends NumberSolution<Double> {

    public double[] movement;
    public int[] NCC;
    public NumberSolution<Double> localBest;


    public CichlidsSolution(NumberSolution<Double> s) {
        super(s);
        localBest = new NumberSolution<>(s);
        movement = new double[s.getVariables().size()];
    }

    public CichlidsSolution(int size, List<Double> position) {
        super(1, position);
        NCC = new int[size];
    }
}
