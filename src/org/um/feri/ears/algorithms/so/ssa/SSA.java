package org.um.feri.ears.algorithms.so.ssa;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class SSA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> food;

    public SSA() {
        this(30);
    }

    public SSA(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("Jasa Zupancic", "jasa.zupancic@student.um.si");
        ai = new AlgorithmInfo("SSA", "Salp Swarm Algorithm",
                "@article{mirjalili2017salp,"
                        + "  title={Salp swarm algorithm: A bio-inspired optimizer for engineering design problems},"
                        + "  author={Mirjalili, Seyedali and others},"
                        + "  journal={Advances in Engineering Software},"
                        + "  volume={114},"
                        + "  pages={163--191},"
                        + "  year={2017},"
                        + "  publisher={Elsevier}}"
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
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        while (!task.isStopCriterion()) {
            double c1 = 2 * Math.exp(-Math.pow(4.0 * task.getNumberOfIterations() / maxIt, 2));

            for (int index = 0; index < popSize; index++) {
                NumberSolution<Double> salp = population.get(index);
                double[] newPosition = new double[task.problem.getNumberOfDimensions()];

                if (index < popSize / 2) {
                    for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                        double c2 = RNG.nextDouble();
                        double c3 = RNG.nextDouble();

                        if (c3 < 0.5) {
                            newPosition[i] = food.getValue(i) + c1 * ((task.problem.getUpperLimit(i) - task.problem.getLowerLimit(i)) * c2 + task.problem.getLowerLimit(i));
                        } else {
                            newPosition[i] = food.getValue(i) - c1 * ((task.problem.getUpperLimit(i) - task.problem.getLowerLimit(i)) * c2 + task.problem.getLowerLimit(i));
                        }
                    }
                } else {
                    for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                        newPosition[i] = (population.get(index - 1).getValue(i) + salp.getValue(i)) / 2.0;
                    }
                }

                task.problem.makeFeasible(newPosition);

                if (task.isStopCriterion())
                    break;
                NumberSolution<Double> newSalp = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newSalp);

                population.set(index, newSalp);
            }

            updateFood();
            task.incrementNumberOfIterations();
        }

        return food;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.generateRandomEvaluatedSolution());
        }
        updateFood();
    }

    private void updateFood() {
        population.sort(new ProblemComparator<>(task.problem));
        food = population.get(0);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

}