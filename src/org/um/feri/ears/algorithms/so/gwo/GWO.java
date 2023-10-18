package org.um.feri.ears.algorithms.so.gwo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class GWO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> alpha, beta, delta;

    public GWO() {
        this(30);
    }

    public GWO(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("GWO", "Gray Wolf Optimizer",
                "@article{mirjalili2014grey,"
                        + "  title={Grey wolf optimizer},"
                        + "  author={Mirjalili, Seyedali and Mirjalili, Seyed Mohammad and Lewis, Andrew},"
                        + "  journal={Advances in Engineering Software},"
                        + "  volume={69},"
                        + "  pages={46--61},"
                        + "  year={2014},"
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
            double a = 2.0 - task.getNumberOfIterations() * (2.0 / maxIt);

            for (int index = 0; index < popSize; index++) {
                NumberSolution<Double> wolf = population.get(index);
                double[] newPosition = new double[task.problem.getNumberOfDimensions()];
                for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {

                    double r1 = RNG.nextDouble();
                    double r2 = RNG.nextDouble();

                    double a1 = 2 * a * r1 - a; // Equation (3.3)
                    double c = 2 * r2; // Equation (3.4)

                    double D_alpha = Math.abs(c * alpha.getValue(i) - wolf.getValue(i)); //Equation (3.5)-part 1
                    double x1 = alpha.getValue(i) - a1 * D_alpha; //Equation (3.6)-part 1


                    r1 = RNG.nextDouble();
                    r2 = RNG.nextDouble();

                    a1 = 2 * a * r1 - a; // Equation (3.3)
                    c = 2 * r2; // Equation (3.4)

                    double D_beta = Math.abs(c * beta.getValue(i) - wolf.getValue(i)); //Equation (3.5)-part 2
                    double x2 = beta.getValue(i) - a1 * D_beta; //Equation (3.6)-part 2


                    r1 = RNG.nextDouble();
                    r2 = RNG.nextDouble();

                    a1 = 2 * a * r1 - a; // Equation (3.3)
                    c = 2 * r2; // Equation (3.4)

                    double D_delta = Math.abs(c * delta.getValue(i) - wolf.getValue(i)); //Equation (3.5)-part 3
                    double x3 = delta.getValue(i) - a1 * D_delta; //Equation (3.6)-part 3

                    newPosition[i] = (x1 + x2 + x3) / 3; // Equation (3.7)
                }
                task.problem.setFeasible(newPosition);

                if (task.isStopCriterion())
                    break;
                NumberSolution<Double> newWolf = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newWolf);

                population.set(index, newWolf);
            }
            updateABD();
            task.incrementNumberOfIterations();
        }
        return alpha;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.getRandomEvaluatedSolution());
        }
        updateABD();
    }

    /**
     * Update alpha, beta and delta wolfs.
     */
    private void updateABD() {

        population.sort(new ProblemComparator<>(task.problem));
        alpha = population.get(0);
        beta = population.get(1);
        delta = population.get(2);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

}
