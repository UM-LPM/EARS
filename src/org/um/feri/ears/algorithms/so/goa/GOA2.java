package org.um.feri.ears.algorithms.so.goa;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GOA2 extends NumberAlgorithm {
    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> best;

    public GOA2() {
        this(30);
    }

    public GOA2(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("sinisa", "sinisa.vucetic@student.um.si");
        ai = new AlgorithmInfo("GOA", "Golf Optimization Algorithm",
                "@article{biomimetics8050386,"
                        + "  title={Golf Optimization Algorithm: A New Game-Based Metaheuristic Algorithm and Its Application to Energy Commitment Problem Considering Resilience},"
                        + "  author={Montazeri, Zeinab and Niknam, Taher and Aghaei, Jamshid and Malik, Om Parkash and Dehghani, Mohammad and Dhiman, Gaurav},"
                        + "  journal={Biomimetics},"
                        + "  volume={8},"
                        + "  pages={386},"
                        + "  year={2023},"
                        + "  publisher={MDPI}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        while (!task.isStopCriterion()) {
            updateBest();
            NumberSolution<Double> currentBestForIter = best;
            explorationPhase(currentBestForIter);

            if (task.isStopCriterion()) break;
            exploitationPhase();
            task.incrementNumberOfIterations();
        }
        return best;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        best = task.generateRandomEvaluatedSolution();
        population.add(best);
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.generateRandomEvaluatedSolution());

            if (task.problem.isFirstBetter(population.get(i), best)) {
                best = new NumberSolution<>(population.get(i));
            }
        }
    }

    private void updateBest() {
        for (int i = 0; i < population.size(); i++) {
            if (task.isStopCriterion()) break;

            if (task.problem.isFirstBetter(population.get(i), best)) {
                best = population.get(i);
            }
        }
    }

    private void explorationPhase(NumberSolution<Double> prevBest) throws StopCriterionException {
        int dim = task.problem.getNumberOfDimensions();

        for (int i = 0; i < popSize; i++) {
            NumberSolution<Double> agent = population.get(i);
            double[] xP1 = new double[dim];

            double r = RNG.nextDouble();
            double[] I = new double[dim];
            double[] rand = new double[dim];

            if (r < 0.5) {
                double scalarI = Math.round(1.0 + RNG.nextDouble());
                double scalarRAND = RNG.nextDouble();
                for (int d = 0; d < dim; d++) {
                    I[d] = scalarI;
                    rand[d] = scalarRAND;
                }
            } else {
                for (int d = 0; d < dim; d++) {
                    I[d] = Math.round(1.0 + RNG.nextDouble());
                }
                for (int d = 0; d < dim; d++) {
                    rand[d] = RNG.nextDouble();
                }
            }

            for (int d = 0; d < dim; d++) {
                xP1[d] = agent.getValue(d) + rand[d] * (prevBest.getValue(d) - I[d] * agent.getValue(d));
            }

            NumberSolution<Double> candidate = new NumberSolution<>(Util.toDoubleArrayList(xP1));

            task.problem.makeFeasible(candidate);

            if (task.isStopCriterion()) break;
            task.eval(candidate);

            if (task.problem.isFirstBetter(candidate, agent)) {
                population.set(i, candidate);
            }
        }
    }

    private void exploitationPhase() throws StopCriterionException {
        int dim = task.problem.getNumberOfDimensions();
        int t = task.getNumberOfIterations() + 1;

        List<Double> lb = task.problem.getLowerLimit();
        List<Double> ub = task.problem.getUpperLimit();

        for (int i = 0; i < popSize; i++) {
            NumberSolution<Double> agent = population.get(i);
            double[] xP2 = new double[dim];

            double r1 = RNG.nextDouble();
            double r2 = RNG.nextDouble();

            for (int d = 0; d < dim; d++) {
                double lowerTerm = lb.get(d) / t;
                double upperTerm = ub.get(d) / t;

                xP2[d] = agent.getValue(d) + (1 - 2 * r1) * (lowerTerm + r2 * (upperTerm - lowerTerm));
            }

            task.problem.makeFeasible(xP2);
            NumberSolution<Double> candidate = new NumberSolution<>(Util.toDoubleArrayList(xP2));

            if (task.isStopCriterion()) break;
            task.eval(candidate);

            if (task.problem.isFirstBetter(candidate, agent)) {
                population.set(i, candidate);
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
