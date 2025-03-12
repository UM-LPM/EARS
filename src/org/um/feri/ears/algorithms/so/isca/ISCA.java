package org.um.feri.ears.algorithms.so.isca;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class ISCA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;

    public ISCA() {
        this(30);
    }

    public ISCA(int popSize) {
        super();
        this.popSize = popSize;
        ai = new AlgorithmInfo("ISCA", "Improved Sine Cosine Algorithm",
                ""
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        initPopulation();

        int maxIteration = 10000;

        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIteration = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIteration = (task.getMaxEvaluations() - popSize) / popSize;
        }

        NumberSolution<Double> destinationPosition = task.generateRandomEvaluatedSolution();

        while (!task.isStopCriterion()) {
            for (int index = 0; index < popSize; index++) {
                NumberSolution<Double> current = population.get(index);

                task.problem.makeFeasible(current.getVariables());

                if (task.problem.isFirstBetter(current, destinationPosition)) {
                    destinationPosition = current;
                }
            }

            // Equation 5
            double wStart = 1.0;
            double wEnd = 0.0;
            double weight = wEnd + (wStart - wEnd) * (maxIteration - task.getNumberOfIterations()) / maxIteration;

            // Equation 6
            double k = 0.1;
            double aStart = 2.0;
            double aEnd = 0.0;

            double r1 = (aStart - aEnd) * Math.exp(-Math.pow(task.getNumberOfIterations(), 2) / (k * Math.pow(maxIteration, 2))) + aEnd;

            for (int index = 0; index < popSize; index++) {
                NumberSolution<Double> current = population.get(index);
                double[] newPosition = new double[task.problem.getNumberOfDimensions()];

                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                    // Equation (3.3)
                    double r2 = (2.0 * Math.PI) * RNG.nextDouble();
                    double r3 = 2 * RNG.nextDouble();
                    double r4 = RNG.nextDouble();

                    // Equation (3.3)
                    if (r4 < 0.5) {
                        // Equation (3.1)
                        newPosition[j] = weight * current.getValue(j) + r1 * Math.sin(r2) * Math.abs(r3 * destinationPosition.getValue(j) - current.getValue(j));
                    } else {
                        // equation (3.2)
                        newPosition[j] = weight * current.getValue(j) + r1 * Math.cos(r2) * Math.abs(r3 * destinationPosition.getValue(j) - current.getValue(j));
                    }
                }

                task.problem.makeFeasible(newPosition);

                if (task.isStopCriterion()) {
                    break;
                }

                NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newSolution);

                population.set(index, newSolution);
            }

            task.incrementNumberOfIterations();
        }

        return destinationPosition;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) {
                break;
            }

            population.add(task.generateRandomEvaluatedSolution());
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

}