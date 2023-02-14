package org.um.feri.ears.util.comparator;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.benchmark.AlgorithmRunResult;
import org.um.feri.ears.problems.*;

import java.util.ArrayList;
import java.util.Comparator;

public class AlgorithmResultComparator<S extends Solution, P extends Problem<S>, T extends TaskBase<P>, A extends Algorithm<T,S>> implements Comparator<AlgorithmRunResult<S, A, T>> {
    ProblemComparator<S> pc;
    T task;
    int evaluationNumber;

    public AlgorithmResultComparator(T task, int evaluationNumber) {
        this.task = task;
        pc = new ProblemComparator<>(task.problem);
        this.evaluationNumber = evaluationNumber;
    }

    @Override
    public int compare(AlgorithmRunResult<S, A, T> r1, AlgorithmRunResult<S, A, T> r2) {

        if (evaluationNumber == -1) {

            int taskCompare = pc.compare(r1.solution, r2.solution);
            // if the results are equal in case of global optimum stop criterion then compare number of evaluations used
            if (taskCompare == 0 && task.getStopCriterion() == StopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
                taskCompare = Integer.compare(r1.task.getNumberOfEvaluations(), r2.task.getNumberOfEvaluations());
            }
            return taskCompare;
        }
        else {
            ArrayList<EvaluationStorage.Evaluation> r1EvaluationHistory =  r1.task.getEvaluationHistory();
            ArrayList<EvaluationStorage.Evaluation> r2EvaluationHistory =  r2.task.getEvaluationHistory();

            for (int i = 0; i < r1EvaluationHistory.size(); i++) {
                if(r1EvaluationHistory.get(i).evalNum == evaluationNumber) {
                    S r1Solution = (S) new NumberSolution(1);
                    r1Solution.setObjective(0, r1EvaluationHistory.get(i).fitness);
                    S r2Solution = (S) new NumberSolution(1);
                    r2Solution.setObjective(0, r2EvaluationHistory.get(i).fitness);
                    return pc.compare(r1Solution, r2Solution);
                }
            }
            System.err.println("Evaluation with " + evaluationNumber + " does not exists");
            return 0;
        }
    }
}