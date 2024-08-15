package org.um.feri.ears.algorithms.gp;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.Configuration;
import org.um.feri.ears.util.RunConfiguration;

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
    public ProgramSolution execute(GPAlgorithmExecutor gpAlgorithmExecutor, RunConfiguration runConfiguration, String saveGPAlgorithmStateFilename) throws StopCriterionException {
        System.out.println("Run configuration: (" + runConfiguration.Name + ")");

        // Set EARS configuration
        int generations = gpAlgorithmExecutor.setEARSConfiguration(runConfiguration);

        // Save Unity configuration
        Configuration.serializeUnityConfig(runConfiguration, gpAlgorithmExecutor.getConfiguration().UnityConfigDestFilePath);

        // Start Unity Instances
        gpAlgorithmExecutor.restartUnityInstances();

        // Run algorithm for X generations
        execute(generations, saveGPAlgorithmStateFilename);

        System.out.println("Run configuration: (" + runConfiguration.Name + ") done");

        // 3. Return best solution
        return this.best;
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
