package org.um.feri.ears.algorithms.so.woa;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class WOA extends Algorithm {
    DoubleSolution bestSolution;

    int popSize;
    Task task;

    // Parameters
    double A;
    double C;
    double r1;
    double r2;

    double a; // Decreases linearly from 2 to 0 over iterations (Eq 2.3)
    double a2; // Linearly decreases from -1 to -2 to calculate t in Eq 3.12

    double b; // Parameter for Eq 2.5
    double l; // Parameter for Eq 2.5

    double p; // 50% whether to choose Shrinking encircling mechanism or the spiral model to update the position of whale

    ArrayList<DoubleSolution> population;

    public WOA() {
        this(30);
    }

    public WOA(int popSize) {
        this(popSize, false, false);
    }

    public WOA(int pop_size, boolean useMockRandom, boolean debug) {
        super();
        this.popSize = pop_size;
        setDebug(debug);

        au = new Author("janez", "janezk7@gmail.com");
        ai = new AlgorithmInfo("WOA", "@article{mirjalili2016whale,\n" +
				"  title={The whale optimization algorithm},\n" +
				"  author={Mirjalili, Seyedali and Lewis, Andrew},\n" +
				"  journal={Advances in engineering software},\n" +
				"  volume={95},\n" +
				"  pages={51--67},\n" +
				"  year={2016},\n" +
				"  publisher={Elsevier}\n" +
				"}", "WOA", "Whale Optimization Algorithm");
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
        task = taskProblem;

        initPopulation();

        int maxIt = 200;

        //bestSolution = population.get(0);
        updateBest();

        if (task.getStopCriteria() == EnumStopCriteria.ITERATIONS) {
            maxIt = task.getMaxIteratirons();
        }

        if (task.getStopCriteria() == EnumStopCriteria.EVALUATIONS) {
            maxIt = task.getMaxEvaluations() / popSize;
        }

        if (debug)
            System.out.println("E: " + bestSolution.getEval());
        while (!task.isStopCriteria()) {
            a = 2.0 - task.getNumberOfIterations() * (2.0 / maxIt);
            a2 = -1.0 + task.getNumberOfIterations() * ((-1.0) / maxIt);

            // For each search agent
            for (int index = 0; index < popSize; index++) {
                DoubleSolution currentAgent = population.get(index);
                double[] newPosition = new double[task.getNumberOfDimensions()];

                // Randoms for A and C
                r1 = Util.nextDouble();
                r2 = Util.nextDouble();

                A = (2.0 * a * r1) - a; // Random value on the interval of shrinking a
                C = 2.0 * r2;

                // Eq 2.5 parameters
                b = 1.0;
                l = (a2 - 1.0) * Util.nextDouble() + 1.0;

                // Get p
                p = Util.nextDouble();

                // For each dimension
                for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                    if (p < 0.5) {
                        // Shrinking encircling mechanism
                        if (Math.abs(A) >= 1) {
                            // Exploration
                            // Select random agent and update position of current (Eq. 2.8)
                            int randAgentIndex = Util.nextInt(popSize);
                            DoubleSolution randAgent = population.get(randAgentIndex);
                            double D_X_rand = Math.abs(C * randAgent.getValue(i) - currentAgent.getValue(i));
                            newPosition[i] = randAgent.getValue(i) - A * D_X_rand;
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

                newPosition = task.setFeasible(newPosition);

                if (task.isStopCriteria())
                    break;

                DoubleSolution newWhale = task.eval(newPosition);
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

    private void initPopulation() throws StopCriteriaException {
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriteria())
                break;
            population.add(task.getRandomSolution());
        }
    }

    private void updateBest() {
        ArrayList<DoubleSolution> popCopy = new ArrayList<DoubleSolution>(population);
        popCopy.sort(new TaskComparator(task));
        if (bestSolution == null || task.isFirstBetter(popCopy.get(0), bestSolution))
            bestSolution = new DoubleSolution(popCopy.get(0));
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
