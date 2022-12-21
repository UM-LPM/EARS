package org.um.feri.ears.algorithms.so.pso;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;


import java.util.ArrayList;

public class PSOOmega extends Algorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<PsoSolution> population;
    private Task task;
    private PsoSolution gBest; //global best
    private double omega, phiG, phiP;

    public PSOOmega() {
        this(10, 0.7, 2, 2);
    }

    public PSOOmega(int popSize, double om, double p1, double p2) {
        super();
        this.popSize = popSize;
        this.omega = om;
        this.phiP = p1;
        this.phiG = p2;
        setDebug(debug);  //EARS prints some debug info
        ai = new AlgorithmInfo("PSOomega", "Particle Swarm Optimization Omega", "");
        au = new Author("Matej", "matej.crepinsek@um.si");
    }

    @Override
    public NumberSolution<Double> execute(Task taskProblem) throws StopCriterionException {
        task = taskProblem;
        initPopulation();
        double[] velocity;
        while (!task.isStopCriterion()) {
            for (int i = 0; i < popSize; i++) {
                velocity = new double[task.getNumberOfDimensions()];
                // r*vec(x) double r = Util.rnd.nextDouble();
                for (int d = 0; d < task.getNumberOfDimensions(); d++) {
                    //http://www.atscience.org/IJISAE/article/view/7
                    //omega different formula omega multiplies with
                    velocity[d] = omega * (
                            population.get(i).velocity[d] +
                                    phiP * Util.rnd.nextDouble() * (population.get(i).pBest.getValue(d) - population.get(i).getValue(d)) +
                                    phiG * Util.rnd.nextDouble() * (gBest.getValue(d) - population.get(i).getValue(d)));
                    //if (v[d]>(taskProblem.getIntervalLength()[d])) v[d]=taskProblem.getIntervalLength()[d];
                    //if (v[d]<(taskProblem.getIntervalLength()[d])) v[d]=-taskProblem.getIntervalLength()[d];
                }
                population.get(i).updatePosition(velocity);
                task.setFeasible(population.get(i));
                task.eval(population.get(i));
                if (task.isFirstBetter(population.get(i), population.get(i).pBest))
                    population.get(i).pBest = new PsoSolution(population.get(i));

                if (task.isFirstBetter(population.get(i), gBest)) gBest = new PsoSolution(population.get(i));
                if (task.isStopCriterion()) break;
            }
            task.incrementNumberOfIterations();
        }
        return gBest;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            population.add(new PsoSolution(task));
            if (i == 0) gBest = population.get(0);
            else if (task.isFirstBetter(population.get(i), gBest)) gBest = new PsoSolution(population.get(i));
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}