package org.um.feri.ears.memory;

import org.um.feri.ears.problems.NumberSolution;

public class DuplicationRemovalStrategyLeaveIt extends DuplicationRemovalStrategy {
    //This is strategy without strategy. It doesn't use knowledge about it
    public DuplicationRemovalStrategyLeaveIt() {
    }

    @Override
    public void changeSolution(double[] x) {
        //do not change it, it will take additional evaluation
    }

    @Override
    public void changeSolution(NumberSolution<Double> solution) {

    }

    @Override
    public boolean criteria4Change(int hits) {
        return false; //always
    }

    @Override
    public boolean forceIncEvaluation() {
        return true;
    }

    public String toString() {
        return Class.class.getSimpleName();
    }
}
