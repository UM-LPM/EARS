package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Solution;
import org.um.feri.ears.problems.Task;

public class AlgorithmRunResult<R extends Solution, S extends Solution, P extends Problem<S>, A extends Algorithm<R, S, P>> {
    public R solution;
    public A algorithm;
    public Task task; //stores info about stopping criterion
    public AlgorithmRunResult(R solution, A algorithm, Task task) {
        this.solution = solution;
        this.algorithm = algorithm;
        this.task = task;
    }
}
