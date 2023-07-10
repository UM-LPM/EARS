package org.um.feri.ears.problems;

import scala.Dynamic;

import java.util.List;

public class DynamicTask<S extends Solution, P extends Problem<S>> extends Task<S, P> {

    private int changeCounter = 0;  // counter for the number of changes
    private boolean changeOccurred = false;
    private final int changeFrequencyPerDimension;

    public DynamicTask(P problem, StopCriterion stopCriterion, int maxEvaluations, long allowedTime, int maxIterations,
                       int changeFrequencyPerDimension) {
        super(problem, stopCriterion, maxEvaluations, allowedTime, maxIterations);
        this.changeFrequencyPerDimension = changeFrequencyPerDimension;

        if (!(problem instanceof DynamicProblem)) {
            /*
                TODO: Get rid of reflection and object-type casting.
                 Temporary solution because of generics. Think about the inheritance hierarchy Problem -> DynamicProblem
                 -> NumberDynamicProblem -> DoubleDynamicProblem to solve this temporary solution with the exception.
                 Currently, the inheritance hierarchy is Problem -> NumberProblem -> DoubleProblem -> DynamicProblem.
             */
            throw new IllegalArgumentException("DynamicTask should only be used with DynamicProblem.");
        }
    }

    public DynamicTask(DynamicTask<S, P> task) {
        super(task);
        changeCounter = task.changeCounter;
        changeOccurred = task.changeOccurred;
        changeFrequencyPerDimension = task.changeFrequencyPerDimension;
    }

    @Override
    public DynamicTask<S, P> clone() {
        return new DynamicTask<>(this);
    }

    @Override
    public void eval(S solution) throws StopCriterionException {
        super.eval(solution);
        changeCounter++;
        if (changeCounter == changeFrequencyPerDimension * ((DynamicProblem) problem).getNumberOfDimensions()) {
            ((DynamicProblem) problem).performChange(changeCounter);
            changeCounter = 0;
            changeOccurred = true;
        }
    }

    @Override
    public void eval(S solution, List<Solution> parents) throws StopCriterionException {
        super.eval(solution, parents);
        changeCounter++;
        if (changeCounter == changeFrequencyPerDimension * ((DynamicProblem) problem).getNumberOfDimensions()) {
            ((DynamicProblem) problem).performChange(changeCounter);
            changeCounter = 0;
            changeOccurred = true;
        }
    }

    // TODO: hint for moro: to pokliči v algoritmu
    public boolean hasChangeOccurred() {
        if (changeOccurred) {
            changeOccurred = false;
            return true;
        }
        return false;
    }
}
