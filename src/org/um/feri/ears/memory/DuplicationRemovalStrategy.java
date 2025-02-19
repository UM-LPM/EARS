package org.um.feri.ears.memory;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

abstract public class DuplicationRemovalStrategy {
    protected Task<NumberSolution<Double>, DoubleProblem> t;

    abstract public String getID();

    abstract public void changeSolution(NumberSolution<Double> solution);

    abstract public boolean criteria4Change(int hits);

    public boolean isForceExplore() {
        return false;
    }

    public boolean forceIncEvaluation() {
        return false;
    }

    public void setTask(Task<NumberSolution<Double>, DoubleProblem> t) {
        this.t = t;
    }
}
