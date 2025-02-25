package org.um.feri.ears.memory;

import org.um.feri.ears.problems.NumberSolution;

public class DuplicationRemovalStrategyLeaveIt extends DuplicationRemovalStrategy {

    //This is strategy without strategy. It doesn't use knowledge about it
    public DuplicationRemovalStrategyLeaveIt() {
    }

    @Override
    public String getID() {
        return "SLcount";
    }


    @Override
    public void changeSolution(NumberSolution<Double> solution) {

    }

    @Override
    public boolean criteria4Change(int hits, int duplicates) {
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
