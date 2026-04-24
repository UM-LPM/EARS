package org.um.feri.ears.algorithms.so.zoa;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class ZOA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    @AlgorithmParameter(
            name = "R",
            description = "some constant number for lion attacking in Phase 2"
    )
    private double R;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> fBest;
    private NumberSolution<Double> lBest;

    public ZOA() {
        this(30);
    }

    public ZOA(int popSize) {
        this(popSize, 0.1);
    }

    public ZOA(int popSize, double R) {
        super();
        this.popSize = popSize;
        this.R = R;

        setDebug(debug);

        au = new Author("Rok", "rok.zerdoner@student.um.si");
        ai = new AlgorithmInfo("ZOA", "Zebra Optimization Algorithm",
                "@article{IEEE Access},"
                        + "  title={Zebra Optimization Algorithm: A New Bio-Inspired Optimization Algorithm for Solving Optimization Algorithm},"
                        + "  author={Eva Trojovská, Mohammad Dehghani and Pavel Trojovský},"
                        + "  journal={IEEE Access},"
                        + "  volume={10},"
                        + "  pages={ 49445--49473},"
                        + "  year={2022},"
                        + "  publisher={IEEE}"
        );
    }


    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }
        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = ((task.getMaxEvaluations() - popSize) / popSize) / 2;
        }

        while (!task.isStopCriterion()) {
            if (task.problem.isFirstBetter(lBest, fBest)) {
                fBest = lBest;
            }
            NumberSolution<Double> PZ = fBest;

            // PHASE 1
            for (int index = 0; index < popSize; index++) {
                double I = Math.round(1 + RNG.nextDouble());
                NumberSolution<Double> zebra = population.get(index);
                double[] newPosition = new double[task.problem.getNumberOfDimensions()];

                for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                    double r = RNG.nextDouble();

                    newPosition[i] = zebra.getVariables().get(i) + r * (PZ.getVariables().get(i) - I * zebra.getVariables().get(i));
                }
                task.problem.makeFeasible(newPosition);

                if (task.isStopCriterion()) break;
                NumberSolution<Double> newZebra = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newZebra);

                if (task.problem.isFirstBetter(newZebra, zebra)) population.set(index, newZebra);
            }

            // PHASE 2
            double ps = RNG.nextDouble();
            //int k = RNG.randomPermutation(popSize)[0];
            int k = 0;
            NumberSolution<Double> attackedZebra = population.get(k); //AZ

            for (int index = 0; index < popSize; index++) {
                NumberSolution<Double> zebra = population.get(index);
                double[] newPosition = new double[task.problem.getNumberOfDimensions()];

                if (ps < 0.5) { // LION attacks
                    for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                        double r = RNG.nextDouble();
                        newPosition[i] = zebra.getVariables().get(i) + R * (2 * r - 1) * (1 - (float) (task.getNumberOfIterations() + 1) / maxIt) * zebra.getVariables().get(i);
                    }
                } else { // Other animals attack
                    double I = Math.round(1 + RNG.nextDouble());
                    for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                        double r = RNG.nextDouble();
                        newPosition[i] = zebra.getVariables().get(i) + r * (attackedZebra.getVariables().get(i) - I * zebra.getVariables().get(i));
                    }
                }

                task.problem.makeFeasible(newPosition);

                if (task.isStopCriterion()) break;
                NumberSolution<Double> newZebra = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newZebra);
                if (task.problem.isFirstBetter(newZebra, zebra)) {
                    population.set(index, newZebra);
                }
                if (task.problem.isFirstBetter(population.get(index), lBest)) lBest = population.get(index);
            }
            task.incrementNumberOfIterations();
        }
        return fBest;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            NumberSolution<Double> newZebra = task.generateRandomEvaluatedSolution();
            population.add(newZebra);

            if (i == 0)
                fBest = population.get(0);
            else if (task.problem.isFirstBetter(population.get(i), fBest))
                fBest = population.get(i);
        }
        lBest = fBest;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
