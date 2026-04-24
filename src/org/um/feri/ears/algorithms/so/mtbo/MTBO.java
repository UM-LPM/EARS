package org.um.feri.ears.algorithms.so.mtbo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class MTBO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> leader;

    public MTBO() {
        this(100); // default in source code = 100
    }


    public MTBO(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("jernej", "jernej.hozjan@student.um.si");
        ai = new AlgorithmInfo("MTBO", "Mountaineering Team-Based Optimization",
                "@article{faridmehr2023mountaineering,"
                        + "  title={Mountaineering Team-Based Optimization: A Novel Human-Based Metaheuristic Algorithm},"
                        + "  author={Faridmehr, Iman and Nehdi, Moncef L. and Davoudkhani, Iraj Faraji and Poolad, Alireza},"
                        + "  journal={Mathematics},"
                        + "  volume={11},"
                        + "  number={5},"
                        + "  pages={1273},"
                        + "  year={2023},"
                        + "  publisher={MDPI}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        int dim = task.problem.getNumberOfDimensions();

        initPopulation();

        while (!task.isStopCriterion()) {

            double[] mean = new double[dim];
            for (int d = 0; d < dim; d++) {
                double sum = 0.0;
                for (int i = 0; i < popSize; i++) {
                    sum += population.get(i).getValue(d);
                }
                mean[d] = sum / popSize;
            }

            leader = population.get(0);
            for (int i = 1; i < popSize; i++) {
                if (task.problem.isFirstBetter(population.get(i), leader)) {
                    leader = population.get(i);
                }
            }

            for (int i = 0; i < popSize; i++) {
                if (task.isStopCriterion()) break;

                NumberSolution<Double> current = population.get(i);
                double[] newPosition = new double[dim];

                int ii = (i + 1) % popSize;

                double lI = 0.25 + 0.25 * RNG.nextDouble(); // action probability
                double aI = 0.75 + 0.25 * RNG.nextDouble(); // avalanche probability
                double mI = 0.75 + 0.25 * RNG.nextDouble(); // member death probability

                if (RNG.nextDouble() < lI) {
                    // Phase 1: Coordinated mountaineering
                    // X_new = X_i + rand*(X_ii - X_i) + rand*(X_Leader - X_ii)
                    double[] r1 = new double[dim];
                    double[] r2 = new double[dim];
                    for (int d = 0; d < dim; d++) r1[d] = RNG.nextDouble();
                    for (int d = 0; d < dim; d++) r2[d] = RNG.nextDouble();
                    for (int d = 0; d < dim; d++) {
                        newPosition[d] = current.getValue(d)
                                + r1[d] * (population.get(ii).getValue(d) - current.getValue(d))
                                + r2[d] * (leader.getValue(d) - population.get(ii).getValue(d));
                    }
                } else if (RNG.nextDouble() < aI) {
                    // Phase 2: Escape from Natural Disasters - Avalanche effect (escape from worst)
                    // X_new = X_i + rand*(X_i - X_last)
                    // X_last = worst member (last in sorted population)
                    NumberSolution<Double> worst = population.get(popSize - 1);
                    for (int d = 0; d < dim; d++) {
                        double r = RNG.nextDouble();
                        newPosition[d] = current.getValue(d)
                                + r * (current.getValue(d) - worst.getValue(d));
                    }
                } else if (RNG.nextDouble() < mI) {
                    // Phase 3: Team effort against disasters (move towards mean)
                    // X_new = X_i + rand*(Mean - X_i)
                    for (int d = 0; d < dim; d++) {
                        double r = RNG.nextDouble();
                        newPosition[d] = current.getValue(d)
                                + r * (mean[d] - current.getValue(d));
                    }
                } else {
                    // Phase 4: Possible death of member (random reinitialization)
                    for (int d = 0; d < dim; d++) {
                        newPosition[d] = task.problem.getLowerLimit(d)
                                + RNG.nextDouble() * (task.problem.getUpperLimit(d) - task.problem.getLowerLimit(d));
                    }
                }

                task.problem.makeFeasible(newPosition);

                if (task.isStopCriterion()) break;

                NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newSolution);

                if (task.problem.isFirstBetter(newSolution, current)) {
                    population.set(i, newSolution);

                    if (task.problem.isFirstBetter(newSolution, leader)) {
                        leader = newSolution;
                    }
                }
            }
            population.sort(new ProblemComparator<>(task.problem));
            task.incrementNumberOfIterations();
        }

        return leader;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            NumberSolution<Double> solution = task.generateRandomEvaluatedSolution();
            population.add(solution);
        }

        // Sort population and set initial leader
        population.sort(new ProblemComparator<>(task.problem));
        leader = population.get(0);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        population = null;
        leader = null;
    }
}
