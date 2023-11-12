package org.um.feri.ears.algorithms.gp;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.GPAlgorithm2;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem2;
import org.um.feri.ears.problems.gp.ProgramSolution2;

import java.util.Collections;
import java.util.List;

public class RandomWalkGPAlgorithm extends GPAlgorithm2 {
    private ProgramSolution2 best;

    public RandomWalkGPAlgorithm() {
        ai = new AlgorithmInfo("RW", "Random Walk", "");
        au = new Author("Marko", "marko.smid2@um.si");
    }

    @Override
    public ProgramSolution2 execute(Task<ProgramSolution2, ProgramProblem2> task) throws StopCriterionException {

        ProgramSolution2 newSolution;
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
    public ProgramSolution2 executeStep() throws StopCriterionException{
        return null;
    }

    @Override
    public ProgramSolution2 executeGeneration() throws StopCriterionException{
        return null;
    }

    @Override
    public List<ProgramSolution2> getPopulation() {
        return Collections.singletonList(best);
    }

    @Override
    public void setPopulation(List<ProgramSolution2> population) {
        best = population.get(0);
    }

    @Override
    public ProgramSolution2 getBest() {
        return best;
    }


}
