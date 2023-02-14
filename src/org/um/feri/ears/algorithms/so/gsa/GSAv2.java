package org.um.feri.ears.algorithms.so.gsa;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;


import java.util.*;

/**
 * Created by Nik Orter on 22. 10. 2016.
 */
public class GSAv2 extends NumberAlgorithm {

    int popSize;
    ArrayList<Agent> pop;
    Agent best;

    Task<NumberSolution<Double>, DoubleProblem> task;

    double G;
    double epsilon = 0.0000000000000000000000000001;

    public GSAv2(int popSize) {
        this.popSize = popSize;
        ai = new AlgorithmInfo("GSAv2", "GSAv2", "");
        au = new Author("nikorter", "nik.orter@gmail.com"); //EARS author info
        pop = new ArrayList<>();
    }

    public GSAv2() {
        this(50);
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {

        this.task = task;

        epsilon = task.getEpsilonForGlobal();

        initPop();
        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        }

        while (!task.isStopCriterion()) {
            nextGeneration(task.getNumberOfIterations(), maxIt);
            task.incrementNumberOfIterations();
        }
        return best;
    }

    void initPop() throws StopCriterionException {
        pop.clear();
        best = null;
        for (int i = 0; i < popSize; i++) {
            Agent newAgent = new Agent(task, i);
            if (best == null)
                best = new Agent(newAgent);
            else if (task.problem.isFirstBetter(newAgent, best)) {
                best = new Agent(newAgent);
                //System.out.println(best);
            }
            pop.add(newAgent);

            if (task.isStopCriterion()) break;
        }
    }

    void updateMasses() {
        //first we need best and worst fit in population
        double bestFit = pop.get(0).getEval();
        double worstFit = pop.get(0).getEval();
        for (Agent agent : pop) {
            if (bestFit > agent.getEval())
                bestFit = agent.getEval();
            if (agent.getEval() > worstFit)
                worstFit = agent.getEval();
        }
        //calculate tmp  mass
        double[] m = new double[popSize];
        double sumM = 0.0f;
        for (int i = 0; i < popSize; i++) {
            m[i] = (pop.get(i).getEval() - worstFit) / (bestFit - worstFit + epsilon);
            sumM += m[i];
        }
        //update Mass for every agent
        for (int i = 0; i < popSize; i++) {
            pop.get(i).setMass(m[i] / sumM);
        }
    }

    double[] getForceActingOnMi(double G, double M_i, double M_j, double[] position_i, double[] position_j) {
        double sum = 0;
        //calculate euclidean distance
        for (int i = 0; i < position_i.length; i++) {
            sum += Math.pow(position_j[i] - position_i[i], 2);
        }
        double R = Math.sqrt(sum);

        double[] force = new double[position_i.length];
        for (int i = 0; i < position_i.length; i++) {
            //force[i] = G*(M_i*M_j)/(R+epsilon)*(position_j[i]-position_i[i]);
            force[i] = Math.random() * M_j * (position_j[i] - position_i[i]) / (R + epsilon);
        }
        return force;
    }

    double[] getTotalForces(List<double[]> forces) {
        double[] totalForce = new double[forces.get(0).length];
        for (double[] f_vector : forces) {
            for (int i = 0; i < f_vector.length; i++) {
                totalForce[i] += f_vector[i] * Math.random();
            }
        }
        return totalForce;
    }


    Agent updatePosition(int popIndex) throws StopCriterionException {
        double[] positions = Util.toDoubleArray(pop.get(popIndex).getVariables());
        double[] velocities = pop.get(popIndex).getVelocities();
        double[] newPosition = new double[positions.length];
        for (int i = 0; i < positions.length; i++) {
            newPosition[i] = task.problem.setFeasible(positions[i] + velocities[i], i);
        }

        NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
        task.eval(newSolution);

        return new Agent(newSolution);
    }

    void nextGeneration(int iteration, int maxIteration) throws StopCriterionException {

        //G = 10f * Math.exp(-0.9*( iteration / (double)(maxIteration)));
        //calculate gravity
        double alfa = 10, g_init = 160;
        G = g_init * Math.exp(-alfa * iteration / maxIteration);
        //update masses for agents
        updateMasses();
        List<Agent> solutions = new ArrayList<>(pop);
        solutions.sort((o1, o2) -> Double.compare(o2.getMass(), o1.getMass()));

        //only k best solution apply forces to others
        //int kBest = (int) (pop_size - (pop_size-1)*(iteration/(double)maxIteration));
        int kBest = (int) (2 + (1 - iteration / (double) maxIteration) * (popSize - 2));
        kBest = Math.round(popSize * kBest / popSize);

        //calculate new force
        for (int i = 0; i < popSize; i++) {
            List<double[]> tmpForces = new ArrayList<>();
            for (int j = 0; j < kBest; j++) {
                if (pop.get(i).getID() == solutions.get(j).getID())
                    continue;
                double M_i = pop.get(i).getMass();
                double M_j = solutions.get(j).getMass();
                tmpForces.add(getForceActingOnMi(G, M_i, M_j, Util.toDoubleArray(pop.get(i).getVariables()), Util.toDoubleArray(solutions.get(j).getVariables())));
            }
            pop.get(i).setForces(getTotalForces(tmpForces));
        }
        //update velocities
        for (Agent a : pop) {
            for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                double newVelocity = Math.random() * a.getVelocityAtIndex(i) + a.getAccelerationOfSolution(i, G);
                a.setVelocityAtIndex(i, newVelocity);
            }
        }

        for (int i = 0; i < popSize; i++) {
            Agent tmpAgent = updatePosition(i);
            if (task.problem.isFirstBetter(tmpAgent, best)) {
                best = new Agent(tmpAgent);
                //System.out.println("ID:" + best.getID() + "  Fitness:" + best.getEval() + "  Solution:" + best );
                //System.out.println("[" +Util.toDoubleArray(best.getVariables()) + "]");
            }

            pop.set(i, tmpAgent);

            if (task.isStopCriterion())
                break;
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
