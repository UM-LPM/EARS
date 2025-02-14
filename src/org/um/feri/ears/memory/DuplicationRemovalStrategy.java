package org.um.feri.ears.memory;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Task;

abstract public class DuplicationRemovalStrategy {
    Task<NumberSolution<Double>, DoubleProblem> t;

    abstract public void changeSolution(double[] x);
    abstract public void changeSolution(NumberSolution<Double> solution);
    abstract public boolean criteria4Change(int hits);

    public boolean forceIncEvaluation() {
        return false;
    }

    public void setTask(Task<NumberSolution<Double>, DoubleProblem> t) {
        this.t = t;
    }
}
