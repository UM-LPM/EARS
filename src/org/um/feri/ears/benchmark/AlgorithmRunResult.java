package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.TaskBase;

public class AlgorithmRunResult<S extends SolutionBase<?>, A extends AlgorithmBase<?,?>, T extends TaskBase<?>> {
    S solution;
    A algorithm;
    T task; //stores info about stopping criterion
    public AlgorithmRunResult(S solution, A algorithm, T task) {
        this.solution = solution;
        this.algorithm = algorithm;
        this.task = task;
    }
    public S getSolution() {
        return solution;
    }
    public A getAlgorithm() {
        return algorithm;
    }
    public T getTask() {
        return task;
    }
}
