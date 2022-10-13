package org.um.feri.ears.algorithms.so.fss;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.util.Util;

import java.util.Comparator;

public class FishSolution extends DoubleSolution {

    public static class FSSComparatorByFitnessGain implements Comparator<FishSolution> {

        @Override
        public int compare(FishSolution o1, FishSolution o2) {
            return Double.compare(o1.delta_f, o2.delta_f);
        }

    }

    public DoubleSolution neighbor = null;
    public DoubleSolution best = null;
    public DoubleSolution current = null;
    public double weightNow = Double.NaN;
    public double weightPast = Double.NaN;
    public double[] deltaX = null;
    public double delta_f = Double.NaN;
    public double fitnessGainNormalized = Double.NaN;

    public boolean individualMoveSuccess = false;
    public boolean instinctiveMoveSuccess = false;
    public boolean volatileMoveSuccess = false;


    public FishSolution(DoubleSolution s) {
        current = s;
        neighbor = new DoubleSolution(s);
        best = new DoubleSolution(s);

        deltaX = new double[s.getVariables().size()];
        //init fish weight
        weightNow = Util.nextDouble(FSS.FISH_WEIGHT_MIN, FSS.FISH_WEIGHT_MAX);
        weightPast = weightNow;
    }
}
