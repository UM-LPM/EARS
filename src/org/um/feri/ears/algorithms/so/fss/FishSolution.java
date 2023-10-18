package org.um.feri.ears.algorithms.so.fss;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.random.RNG;

import java.util.Comparator;

public class FishSolution extends NumberSolution<Double> {

    public static class FSSComparatorByFitnessGain implements Comparator<FishSolution> {

        @Override
        public int compare(FishSolution o1, FishSolution o2) {
            return Double.compare(o1.delta_f, o2.delta_f);
        }

    }

    public NumberSolution<Double> neighbor = null;
    public NumberSolution<Double> best = null;
    public double weightNow = Double.NaN;
    public double weightPast = Double.NaN;
    public double[] deltaX = null;
    public double delta_f = Double.NaN;
    public double fitnessGainNormalized = Double.NaN;

    public boolean individualMoveSuccess = false;
    public boolean instinctiveMoveSuccess = false;
    public boolean volatileMoveSuccess = false;


    public FishSolution(NumberSolution<Double> s) {
        super(s);
        neighbor = new NumberSolution<>(s);
        best = new NumberSolution<>(s);

        deltaX = new double[s.getVariables().size()];
        //init fish weight
        weightNow = RNG.nextDouble(FSS.FISH_WEIGHT_MIN, FSS.FISH_WEIGHT_MAX);
        weightPast = weightNow;
    }
}
