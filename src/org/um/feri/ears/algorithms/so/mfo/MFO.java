package org.um.feri.ears.algorithms.so.mfo;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Comparator.TaskComparator;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

import java.util.ArrayList;

public class MFO extends Algorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private DoubleSolution bestFlame;
    private double flameNum;
    private Task task;
    private ArrayList<DoubleSolution> population;

    public MFO() {
        this(20);
    }

    public MFO(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("MFO", "Moth Flame Optimization",
                "@article{mirjalili2015moth,"
                        + "title={Moth-flame optimization algorithm: A novel nature-inspired heuristic paradigm},"
                        + "author={Mirjalili, Seyedali},"
                        + "journal={Knowledge-Based Systems},"
                        + "volume={89},"
                        + "pages={228--249},"
                        + "year={2015},"
                        + "publisher={Elsevier}}"
        );
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<DoubleSolution>();

        for (int i = 0; i < popSize; i++) {
            population.add(task.getRandomEvaluatedSolution());
            if (task.isStopCriterion())
                break;
        }
    }

    @Override
    public DoubleSolution execute(Task task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        population.sort(new TaskComparator(task));

        bestFlame = new DoubleSolution(population.get(0));

        int maxIt = 10000;
        if (task.getStopCriterion() == EnumStopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }

        if (task.getStopCriterion() == EnumStopCriterion.EVALUATIONS) {
            maxIt = task.getMaxEvaluations() / popSize;
        }

        while (!task.isStopCriterion()) {
            flameNum = Math.round(popSize - task.getNumberOfIterations() * ((popSize - 1) / maxIt));

            task.incrementNumberOfIterations();
        }

        return bestFlame;
    }


    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
