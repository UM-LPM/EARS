package org.um.feri.ears.memory;

import org.um.feri.ears.problems.NumberSolution;

public class DuplicationRemovalStrategyLeaveIt2 extends DuplicationRemovalStrategy {
    //This is strategy without strategy. It doesn't use knowledge about it
    public DuplicationRemovalStrategyLeaveIt2() {
    }

    @Override
    public String getID() {
        return "i";
    } //i-ignore

    @Override
    public void changeSolution(NumberSolution<Double> solution) { //ignore duplicates
    }

    @Override
    public boolean criteria4Change(int hits, int duplicates) {
        return false; //always ignore
    }

    @Override
    public boolean forceIncEvaluation() {
        return false;
    } //always ignore

    public String toString() {
        return Class.class.getSimpleName();
    }
}
