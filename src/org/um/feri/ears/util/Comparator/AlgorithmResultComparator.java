package org.um.feri.ears.util.Comparator;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.benchmark.AlgorithmRunResult;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.Task;

import java.util.Comparator;

public class AlgorithmResultComparator implements Comparator<AlgorithmRunResult<DoubleSolution, Algorithm, Task>> {
    TaskComparator tc;
    public AlgorithmResultComparator(Task t) {
        tc = new TaskComparator(t);
    }

    @Override
    public int compare(AlgorithmRunResult<DoubleSolution, Algorithm, Task> r1, AlgorithmRunResult<DoubleSolution, Algorithm, Task> r2) {
        return tc.compare(r1.getSolution(), r2.getSolution());
    }
}