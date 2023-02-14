package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.problems.Solution;
import org.um.feri.ears.problems.TaskBase;

public class AlgorithmRunResult<S extends Solution, A extends Algorithm<?,?>, T extends TaskBase<?>> {
    public S solution;
    public A algorithm;
    public T task; //stores info about stopping criterion
    public AlgorithmRunResult(S solution, A algorithm, T task) {
        this.solution = solution;
        this.algorithm = algorithm;
        this.task = task;
    }
}
