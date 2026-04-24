package org.um.feri.ears.algorithms.so.hoa;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class HOA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> globalBest;

    public HOA() {
        this(10); // default population size from source code
    }

    public HOA(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("mlinmiha", "miha.mlinaric@student.um.si");
        ai = new AlgorithmInfo("HOA", "Hiking Optimization Algorithm",
                "@article{Article 111880,"
                        + "  title={The Hiking Optimization Algorithm: A novel human-based metaheuristic approach},"
                        + "  author={Sunday O. Oladejo, Stephen O. Ekwe, Seyedali Mirjalili},"
                        + "  journal={Knowledge-Based Systems},"
                        + "  volume={296},"
                        + "  year={2024},"
                        + "  publisher={Elsevier B.V.}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {

        this.task = task;
        initPopulation();

        while (!task.isStopCriterion()) {
            for (int i = 0; i < popSize; i++) {
                if (task.problem.isFirstBetter(population.get(i), globalBest)) {
                    globalBest = population.get(i);
                }
            }

            for (int index = 0; index < popSize; index++) {

                NumberSolution<Double> xIni = population.get(index);            // hiker
                double[] newPosition = new double[task.problem.getNumberOfDimensions()];

                int theta = RNG.nextInt(0, 51);           // randomize elevation angle of hiker
                double s = Math.tan(theta);                                     // compute slope
                int SF = RNG.nextInt(1, 3);               // sweep factor

                double velScalar = 6.0 * Math.exp(-3.5 * Math.abs(s + 0.05));   // compute walking velocity based on Tobler's Hiking Function

                for (int dim = 0; dim < task.problem.getNumberOfDimensions(); dim++) {
                    double newVel = velScalar + RNG.nextDouble() * (globalBest.getValue(dim) - SF * xIni.getValue(dim));
                    newPosition[dim] = xIni.getValue(dim) + newVel;
                }

                if (task.isStopCriterion())
                    break;

                NumberSolution<Double> candidate = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.problem.makeFeasible(candidate);
                task.eval(candidate);

                if (task.problem.isFirstBetter(candidate, xIni)) {
                    population.set(index, candidate);
                }
            }
            task.incrementNumberOfIterations();
        }
        return globalBest;
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