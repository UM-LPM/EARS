package org.um.feri.ears.problems.gp;


import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

public class DefaultGPAlgorithm extends GPAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    @Override
    public ProgramSolution<Double> execute(Task<ProgramSolution<Double>, ProgramProblem<Double>> task) throws StopCriterionException {
        return null;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
