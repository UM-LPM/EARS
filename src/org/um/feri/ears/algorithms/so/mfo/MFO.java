package org.um.feri.ears.algorithms.so.mfo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

import java.util.ArrayList;

public class MFO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private NumberSolution<Double> bestFlame;
    private double flameNum;
    private ArrayList<NumberSolution<Double>> population;

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
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            population.add(task.getRandomEvaluatedSolution());
            if (task.isStopCriterion())
                break;
        }
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        population.sort(new ProblemComparator<>(task.problem));

        bestFlame = new NumberSolution<>(population.get(0));

        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        while (!task.isStopCriterion()) {
            flameNum = Math.round(popSize - task.getNumberOfIterations() * ((popSize - 1) / maxIt));
            //TODO implement
            task.incrementNumberOfIterations();
        }

        return bestFlame;
    }


    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
