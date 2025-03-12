package org.um.feri.ears.algorithms.so.pdwoa;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class PDWOA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;

    private Double a, a2, l, p, r1, r2, A, C, b, crr = 0.8;
    boolean randomCrr = false;

    private NumberSolution<Double> bestSolution;


    public PDWOA() { this(30); }

    public PDWOA(int popSize) {
        this(popSize, false);
    }

    public PDWOA(int popSize, double crr) {
        this(popSize, false, crr);
    }

    public PDWOA(int popSize, boolean debug) {
        this(popSize, debug, 0.8);
    }

    public PDWOA(int popSize, boolean debug, double crr) {
        super();
        this.popSize = popSize;
        setDebug(debug);
        this.crr = crr;
        if(crr == 0.0) {
            randomCrr = true;
        }

        au = new Author("LinuxAlex", "aleks.marinic@kdemail.net");
        ai = new AlgorithmInfo(
                randomCrr ? "PDWOA (crr: rand)" : String.format("PDWOA (crr: %.2f)", crr),
                "Pbest-guided differential Whale Optimization Algorithm",
                "@article{rahimnejad2023improved,\n" +
                        "  title={An improved hybrid whale optimization algorithm for global optimization and engineering design problems},\n" +
                        "  author={Rahimnejad, Abolfazl and Akbari, Ebrahim and Mirjalili, Seyedali and Gadsden, Stephen Andrew and Trojovsk{\\y}, Pavel and Trojovsk{\\'a}, Eva},\n" +
                        "  journal={PeerJ Computer Science},\n" +
                        "  volume={9},\n" +
                        "  year={2023},\n" +
                        "  publisher={PeerJ, Inc}\n" +
                        "}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        initPopulation();
        updateBest();

        int maxIt = 1000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        if(debug) {
            System.out.println("E: " + bestSolution.getEval());
        }

        while(!task.isStopCriterion()) {
            if(randomCrr) {
                crr = RNG.nextDouble();
            }

            a = 2.0 - task.getNumberOfIterations() * (2.0 / maxIt);
            a2 = -1.0 + task.getNumberOfIterations() * ((-1.0) / maxIt);

            // For each search agent
            for (int index = 0; index < popSize; index++) {
                NumberSolution<Double> currentAgent = population.get(index);
                double[] newPosition = new double[task.problem.getNumberOfDimensions()];

                // Random for A and C
                r1 = RNG.nextDouble();
                r2 = RNG.nextDouble();

                A = (2.0 * a * r1) - a;
                C = 2.0 * r2;

                b = 1.0;
                l = (a2 - 1.0) * RNG.nextDouble() + 1.0;

                p = RNG.nextDouble();


                for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                    if (p < 0.5) {
                        if (Math.abs(A) >= 1) {
                            int rand_leader_index = RNG.nextInt(popSize);
                            NumberSolution<Double> X_rand = population.get(rand_leader_index);
                            double D_X_rand = Math.abs(C * X_rand.getValue(i) - currentAgent.getValue(i));
                            newPosition[i] = X_rand.getValue(i) - A * D_X_rand;
                        } else {
                            double D_Leader = Math.abs(C * bestSolution.getValue(i) - currentAgent.getValue(i));
                            newPosition[i] = bestSolution.getValue(i) - A * D_Leader;
                        }
                    } else {
                        double distance2Leader = Math.abs(bestSolution.getValue(i) - currentAgent.getValue(i));
                        newPosition[i] = distance2Leader * Math.exp(b * l) * Math.cos(l * 2.0 * Math.PI) + bestSolution.getValue(i);
                    }
                }

                if(RNG.nextDouble() > crr) {
                    int m1 = RNG.nextInt(task.problem.getNumberOfDimensions());
                    int m2 = RNG.nextInt(task.problem.getNumberOfDimensions());

                    while (m1 == m2) {
                        m2 = RNG.nextInt(task.problem.getNumberOfDimensions());
                    }

                    for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                        if (RNG.nextDouble() < crr) {
                            newPosition[i] = currentAgent.getValue(i) + RNG.nextDouble() * (bestSolution.getValue(i) - currentAgent.getValue(i)) + RNG.nextDouble() * (population.get(m1).getValue(i) - population.get(m2).getValue(i));
                        }
                    }

                    task.problem.makeFeasible(newPosition);
                    population.set(index, new NumberSolution<>(Util.toDoubleArrayList(newPosition)));
                }

                task.problem.makeFeasible(newPosition);

                if (task.isStopCriterion())
                    break;

                NumberSolution<Double> newWhale = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newWhale);

                population.set(index, newWhale);
            }

            updateBest();

            if(debug) {
                System.out.println("E: " + bestSolution.getEval());
            }
            task.incrementNumberOfIterations();
        }
        return bestSolution;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.generateRandomEvaluatedSolution());
        }
    }

    private void updateBest() {
        ArrayList<NumberSolution<Double>> popCopy = new ArrayList<>(population);
        popCopy.sort(new ProblemComparator<>(task.problem));

        if(bestSolution == null || task.problem.isFirstBetter(popCopy.get(0), bestSolution)) {
            bestSolution = new NumberSolution<>(popCopy.get(0));
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}