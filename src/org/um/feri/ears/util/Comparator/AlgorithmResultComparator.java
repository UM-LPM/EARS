package org.um.feri.ears.util.Comparator;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.benchmark.AlgorithmRunResult;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EvaluationStorage;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;

import java.util.ArrayList;
import java.util.Comparator;

public class AlgorithmResultComparator implements Comparator<AlgorithmRunResult<DoubleSolution, Algorithm, Task>> {
    TaskComparator tc;
    Task task;
    int evaluationNumber;

    public AlgorithmResultComparator(Task task, int evaluationNumber) {
        this.task = task;
        tc = new TaskComparator(task);
        this.evaluationNumber = evaluationNumber;
    }

    @Override
    public int compare(AlgorithmRunResult<DoubleSolution, Algorithm, Task> r1, AlgorithmRunResult<DoubleSolution, Algorithm, Task> r2) {

        if (evaluationNumber == -1) {

            int taskCompare = tc.compare(r1.solution, r2.solution);
            // if the results are equal in case of global optimum stop criterion then compare number of evaluations used
            if (taskCompare == 0 && task.getStopCriterion() == StopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
                taskCompare = Integer.compare(r1.task.getNumberOfEvaluations(), r2.task.getNumberOfEvaluations());
            }
            return taskCompare;
        }
        else {
            ArrayList<EvaluationStorage.Evaluation> r1EvaluationHistory =  r1.task.getEvaluationHistory();
            ArrayList<EvaluationStorage.Evaluation> r2EvaluationHistory =  r2.task.getEvaluationHistory();

            DoubleSolution r1Solution = new DoubleSolution();
            DoubleSolution r2Solution = new DoubleSolution();

            for (int i = 0; i < r1EvaluationHistory.size(); i++) {
                if(r1EvaluationHistory.get(i).evalNum == evaluationNumber) {
                    r1Solution.setEval(r1EvaluationHistory.get(i).fitness);
                    r2Solution.setEval(r2EvaluationHistory.get(i).fitness);
                    return tc.compare(r1Solution, r2Solution);
                }
            }
            System.err.println("Evaluation with " + evaluationNumber + " does not exists");
            return 0;
        }
    }
}