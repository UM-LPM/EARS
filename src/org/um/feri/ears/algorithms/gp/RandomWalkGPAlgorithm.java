package org.um.feri.ears.algorithms.gp;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.Configuration;
import org.um.feri.ears.util.GPProblemType;
import org.um.feri.ears.util.RunConfiguration;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.gp_stats.GPAlgorithmMultiConfigurationsProgressData;

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
    public ProgramSolution execute(GPAlgorithmExecutor gpAlgorithmExecutor, RunConfiguration runConfiguration, String saveGPAlgorithmStateFilename, GPAlgorithmMultiConfigurationsProgressData multiConfigurationsProgressData) throws StopCriterionException {
        System.out.println("Run configuration: (" + runConfiguration.Name + ")");

        // Set EARS configuration
        int generations = gpAlgorithmExecutor.setEARSConfiguration(runConfiguration);

        if(runConfiguration.EARSConfiguration.ProblemType == GPProblemType.BEHAVIOR) {
            // Save Unity configuration
            Configuration.serializeUnityConfig(runConfiguration.UnityConfiguration, gpAlgorithmExecutor.getConfiguration().UnityConfigDestFilePath);

            // Start Unity Instances
            gpAlgorithmExecutor.restartUnityInstances(true);
        }

        // Run algorithm for X generations
        execute(generations, saveGPAlgorithmStateFilename, "Main_run", multiConfigurationsProgressData);

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

    @Override
    public ProblemComparator<ProgramSolution> getComparator() {
        return null;
    }

    @Override
    public void setHallOfFameSize(int hallOfFameSize) {    }

    @Override
    public List<ProgramSolution> getBestGenSolutions() {
        return null;
    }

    @Override
    public List<ProgramSolution> getBestGenSolutionsConvergenceGraph() {
        return null;
    }

    @Override
    public List<ProgramSolution> getBestGenSolutionsMasterTournament() {
        return null;
    }
}
