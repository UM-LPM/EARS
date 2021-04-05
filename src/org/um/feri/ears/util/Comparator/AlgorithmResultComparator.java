package org.um.feri.ears.util.Comparator;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.benchmark.AlgorithmRunResult;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.Task;

import java.util.Comparator;

public class AlgorithmResultComparator implements Comparator<AlgorithmRunResult<DoubleSolution, Algorithm, Task>> {
    TaskComparator tc;
    Task task;

    public AlgorithmResultComparator(Task task) {
        this.task = task;
        tc = new TaskComparator(task);
    }

    @Override
    public int compare(AlgorithmRunResult<DoubleSolution, Algorithm, Task> r1, AlgorithmRunResult<DoubleSolution, Algorithm, Task> r2) {

        int taskCompare = tc.compare(r1.solution, r2.solution);
        // if the results are equal in case of global optimum stop criterion then compare number of evaluations used
        if (taskCompare == 0 && task.getStopCriterion() == EnumStopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
            taskCompare = Integer.compare(r1.task.getNumberOfEvaluations(), r2.task.getNumberOfEvaluations());
        }
        return taskCompare;
    }
}