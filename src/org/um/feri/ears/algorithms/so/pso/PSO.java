package org.um.feri.ears.algorithms.so.pso;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class PSO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter (
            name = "omega",
            description = "inertia weight"
    )
    private double omega;
    @AlgorithmParameter (
            name = "c1",
            description = "cognitive coefficient (acceleration constant)"
    )
    private double c1;
    @AlgorithmParameter (
            name = "c2",
            description = "social coefficient (acceleration constant)"
    )
    private double c2;

    private ArrayList<PsoSolution> population;
    private NumberSolution<Double> gBest; //global best

    public PSO() {
        this(10);
    }

    public PSO(int popSize) {
        this(popSize, 0.7, 2, 2);
    }

    public PSO(int popSize, double om, double c1, double c2) {
        super();
        this.popSize = popSize;
        this.omega = om;
        this.c1 = c1;
        this.c2 = c2;
        setDebug(debug);  //EARS prints some debug info
        ai = new AlgorithmInfo("PSO", "Particle Swarm Optimization", "");
        au = new Author("Matej", "matej.crepinsek@um.si");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();
        double[] velocity;
        while (!task.isStopCriterion()) {
            for (int i = 0; i < popSize; i++) {
                velocity = new double[task.problem.getNumberOfDimensions()];
                // r*vec(x) double r = RNG.nextDouble();
                for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {
                    velocity[d] = omega * (
                            population.get(i).velocity[d]) +
                            c1 * RNG.nextDouble() * (population.get(i).pBest.getValue(d) - population.get(i).getValue(d)) +
                            c2 * RNG.nextDouble() * (gBest.getValue(d) - population.get(i).getValue(d));
                    //if (v[d]>(task.getIntervalLength()[d])) v[d]=task.getIntervalLength()[d];
                    //if (v[d]<(task.getIntervalLength()[d])) v[d]=-task.getIntervalLength()[d];
                }

                population.get(i).updatePosition(velocity);
                task.problem.makeFeasible(population.get(i));
                task.eval(population.get(i));
                if (task.problem.isFirstBetter(population.get(i), population.get(i).pBest))
                    population.get(i).pBest = new PsoSolution(population.get(i));

                if (task.problem.isFirstBetter(population.get(i), gBest)) gBest = new PsoSolution(population.get(i));
                if (task.isStopCriterion()) break;
            }
            task.incrementNumberOfIterations();
        }
        return gBest;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            population.add(new PsoSolution(task));
            if (i == 0) gBest = population.get(0);
            else if (task.problem.isFirstBetter(population.get(i), gBest)) gBest = new PsoSolution(population.get(i));
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}