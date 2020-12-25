package org.um.feri.ears.algorithms.so.mfo;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;

import java.util.ArrayList;

public class MFO extends Algorithm {

    private DoubleSolution bestFlame;

    private int popSize;
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
        ai = new AlgorithmInfo("MFO",
                "@article{mirjalili2015moth,"
                        + "title={Moth-flame optimization algorithm: A novel nature-inspired heuristic paradigm},"
                        + "author={Mirjalili, Seyedali},"
                        + "journal={Knowledge-Based Systems},"
                        + "volume={89},"
                        + "pages={228--249},"
                        + "year={2015},"
                        + "publisher={Elsevier}}",
                "MFO", "Moth Flame Optimization");
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
    }

    private void initPopulation() throws StopCriteriaException {
        population = new ArrayList<DoubleSolution>();

        for (int i = 0; i < popSize; i++) {
            population.add(task.getRandomSolution());
            if (task.isStopCriteria())
                break;
        }
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {

        initPopulation();

        population.sort(new TaskComparator(task));

        bestFlame = new DoubleSolution(population.get(0));

        int maxIt = 10000;
        if (task.getStopCriteria() == EnumStopCriteria.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }

        if (task.getStopCriteria() == EnumStopCriteria.EVALUATIONS) {
            maxIt = task.getMaxEvaluations() / popSize;
        }

        while (!task.isStopCriteria()) {
            flameNum = Math.round(popSize - task.getNumberOfIterations() * ((popSize - 1) / maxIt));

            task.incrementNumberOfIterations();
        }

        return bestFlame;
    }


    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
