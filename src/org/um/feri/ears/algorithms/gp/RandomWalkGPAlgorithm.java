package org.um.feri.ears.algorithms.gp;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

import java.util.Collections;
import java.util.List;

public class RandomWalkGPAlgorithm extends GPAlgorithm {
    private ProgramSolution best;

    public RandomWalkGPAlgorithm() {
        ai = new AlgorithmInfo("RW", "Random Walk", "");
        au = new Author("Marko", "marko.smid2@um.si");
    }

    @Override
    public ProgramSolution execute(Task<ProgramSolution, ProgramProblem> task) throws StopCriterionException {

        ProgramSolution newSolution;
        best = task.getRandomEvaluatedSolution();

        while (!task.isStopCriterion()) {

            newSolution = task.getRandomEvaluatedSolution();
            if (task.problem.isFirstBetter(newSolution, best)) {
                best = newSolution;
            }
            task.incrementNumberOfIterations();
        }
        return best;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        if(this.task != null){
            this.task.resetCounter();
        }
        this.best = null;
    }


    @Override
    public ProgramSolution executeStep() throws StopCriterionException{
        return null;
    }

    @Override
    public ProgramSolution executeGeneration() throws StopCriterionException{
        return null;
    }

    @Override
    public List<ProgramSolution> getPopulation() {
        return Collections.singletonList(best);
    }

    @Override
    public void setPopulation(List<ProgramSolution> population) {
        best = population.get(0);
    }

    @Override
    public ProgramSolution getBest() {
        return best;
    }


}
