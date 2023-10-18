package org.um.feri.ears.algorithms.so.woa;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class WOA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private NumberSolution<Double> bestSolution;

    private double A;
    private double C;
    private double r1;
    private double r2;
    private double a; // Decreases linearly from 2 to 0 over iterations (Eq 2.3)
    private double a2; // Linearly decreases from -1 to -2 to calculate t in Eq 3.12
    private double b; // Parameter for Eq 2.5
    private double l; // Parameter for Eq 2.5
    private double p; // 50% whether to choose Shrinking encircling mechanism or the spiral model to update the position of whale

    private ArrayList<NumberSolution<Double>> population;

    public WOA() {
        this(30);
    }

    public WOA(int popSize) {
        this(popSize, false);
    }

    public WOA(int popSize, boolean debug) {
        super();
        this.popSize = popSize;
        setDebug(debug);

        au = new Author("janez", "janezk7@gmail.com");
        ai = new AlgorithmInfo( "WOA", "Whale Optimization Algorithm",
                "@article{mirjalili2016whale,\n" +
                "  title={The whale optimization algorithm},\n" +
                "  author={Mirjalili, Seyedali and Lewis, Andrew},\n" +
                "  journal={Advances in engineering software},\n" +
                "  volume={95},\n" +
                "  pages={51--67},\n" +
                "  year={2016},\n" +
                "  publisher={Elsevier}\n" +
                "}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        initPopulation();

        int maxIt = 200;

        //bestSolution = population.get(0);
        updateBest();

        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        if (debug)
            System.out.println("E: " + bestSolution.getEval());
        while (!task.isStopCriterion()) {
            a = 2.0 - task.getNumberOfIterations() * (2.0 / maxIt);
            a2 = -1.0 + task.getNumberOfIterations() * ((-1.0) / maxIt);

            // For each search agent
            for (int index = 0; index < popSize; index++) {
                NumberSolution<Double> currentAgent = population.get(index);
                double[] newPosition = new double[task.problem.getNumberOfDimensions()];

                // Randoms for A and C
                r1 = RNG.nextDouble();
                r2 = RNG.nextDouble();

                A = (2.0 * a * r1) - a; // Random value on the interval of shrinking a
                C = 2.0 * r2;

                // Eq 2.5 parameters
                b = 1.0;
                l = (a2 - 1.0) * RNG.nextDouble() + 1.0;

                // Get p
                p = RNG.nextDouble();

                // For each dimension
                for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                    if (p < 0.5) {
                        // Shrinking encircling mechanism
                        if (Math.abs(A) >= 1) {
                            // Exploration
                            // Select random agent and update position of current (Eq. 2.8)
                            int randAgentIndex = RNG.nextInt(popSize);
                            NumberSolution<Double> randAgent = population.get(randAgentIndex);
                            double dXRand = Math.abs(C * randAgent.getValue(i) - currentAgent.getValue(i));
                            newPosition[i] = randAgent.getValue(i) - A * dXRand;
                        } else if (Math.abs(A) < 1) {
                            // Exploitation
                            // Select best agent and Update position of current (Eq. 2.1)
                            // Search in a shrinking (A) spiral.
                            double dBest = Math.abs(C * bestSolution.getValue(i) - currentAgent.getValue(i));
                            newPosition[i] = bestSolution.getValue(i) - A * dBest;
                        }
                    } else {
                        // Spiral model (Eq 2.5)
                        double dXLeader = Math.abs(bestSolution.getValue(i) - currentAgent.getValue(i));
                        newPosition[i] = dXLeader * Math.exp(b * l) * Math.cos(l * 2.0 * Math.PI) + bestSolution.getValue(i);
                    }
                }

                task.problem.setFeasible(newPosition);

                if (task.isStopCriterion())
                    break;

                NumberSolution<Double> newWhale = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newWhale);

                population.set(index, newWhale);

                // Check if the changed is better ?
                //if(task.isFirstBetter(newWhale,  population.get(index)))
                //	population.set(index,  newWhale);
            }
            updateBest();
            if (debug)
                System.out.println(bestSolution.getEval());
            task.incrementNumberOfIterations();
        }
        return bestSolution;
    }

    private void initPopulation() throws StopCriterionException {
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.getRandomEvaluatedSolution());
        }
    }

    private void updateBest() {
        ArrayList<NumberSolution<Double>> popCopy = new ArrayList<>(population);
        popCopy.sort(new ProblemComparator<>(task.problem));
        if (bestSolution == null || task.problem.isFirstBetter(popCopy.get(0), bestSolution))
            bestSolution = new NumberSolution<>(popCopy.get(0));
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
