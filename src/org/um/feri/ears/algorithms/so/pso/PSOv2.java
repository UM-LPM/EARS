package org.um.feri.ears.algorithms.so.pso;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.BoundaryControl;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.List;

public class PSOv2 extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter(
            name = "omega",
            description = "inertia weight"
    )
    private double omega;
    @AlgorithmParameter(
            name = "c1",
            description = "cognitive coefficient (acceleration constant)"
    )
    private double c1;
    @AlgorithmParameter(
            name = "c2",
            description = "social coefficient (acceleration constant)"
    )
    private double c2;

    private ArrayList<PsoSolutionv2> population;
    private NumberSolution<Double> gBest; //global best

    public PSOv2() {
        this(50);
    }

    public PSOv2(int popSize) {
        this(popSize, 0.72984, 2.05 * 0.72984, 2.05 * 0.72984);
    }

    public PSOv2(int popSize, double om, double c1, double c2) {
        super();
        this.popSize = popSize;
        this.omega = om;
        this.c1 = c1;
        this.c2 = c2;
        setDebug(debug);  //EARS prints some debug info
        ai = new AlgorithmInfo("PSO", "Particle Swarm Optimization", "");
        au = new Author("Miha", "miha.ravber@um.si");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();
        updateLocalBest();
        List<Double> velocity;
        while (!task.isStopCriterion()) {
            for (int i = 0; i < popSize; i++) {
                velocity = new ArrayList<>(task.problem.getNumberOfDimensions());
                for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {
                    double v = omega * (
                            population.get(i).velocity.get(d)) +
                            c1 * RNG.nextDouble() * (population.get(i).pBest.getValue(d) - population.get(i).getValue(d)) +
                            c2 * RNG.nextDouble() * (population.get(i).lBest.getValue(d) - population.get(i).getValue(d));
                    //c2 * RNG.nextDouble() * (gBest.getValue(d) - population.get(i).getValue(d));
                    velocity.add(v);
                }

                BoundaryControl.clamp(velocity, task.problem.lowerLimit, task.problem.upperLimit);

                population.get(i).updatePosition(velocity);

                if (!task.problem.isFeasible(population.get(i))) {
                    task.problem.makeFeasible(population.get(i), BoundaryControl.BoundaryControlMethod.REFLECT);
                    for (int j = 0; j < population.get(i).velocity.size(); j++) {
                        population.get(i).velocity.set(j, 0.0);
                    }
                }

                task.eval(population.get(i));
                if (task.problem.isFirstBetter(population.get(i), population.get(i).pBest))
                    population.get(i).pBest = new PsoSolutionv2(population.get(i));

                if (task.problem.isFirstBetter(population.get(i), gBest)) gBest = new PsoSolutionv2(population.get(i));
                if (task.isStopCriterion()) break;
            }
            updateLocalBest();
            task.incrementNumberOfIterations();
        }
        return gBest;
    }

    private void updateLocalBest() {
        int popSize = population.size();

        // Particle 1's neighbors (circular indexing)
        PsoSolutionv2 sm1 = population.get(popSize - 1); // Neighbor on the left
        PsoSolutionv2 sm2 = population.get(0);           // Self
        PsoSolutionv2 sm3 = population.get(1);           // Neighbor on the right

        PsoSolutionv2 bestNeighbor;
        if (task.problem.isFirstBetter(sm1, sm2)) {
            bestNeighbor = task.problem.isFirstBetter(sm1, sm3) ? sm1 : sm3;
        } else {
            bestNeighbor = task.problem.isFirstBetter(sm2, sm3) ? sm2 : sm3;
        }
        population.get(0).lBest = new PsoSolutionv2(bestNeighbor);

        // For particles 2 to popSize-1
        for (int i = 1; i < popSize - 1; i++) {
            sm1 = population.get(i - 1); // Left neighbor
            sm2 = population.get(i);     // Self
            sm3 = population.get(i + 1); // Right neighbor

            if (task.problem.isFirstBetter(sm1, sm2)) {
                bestNeighbor = task.problem.isFirstBetter(sm1, sm3) ? sm1 : sm3;
            } else {
                bestNeighbor = task.problem.isFirstBetter(sm2, sm3) ? sm2 : sm3;
            }
            population.get(i).lBest = new PsoSolutionv2(bestNeighbor);
        }

        // Particle popSize's neighbors (circular indexing)
        sm1 = population.get(popSize - 2); // Neighbor on the left
        sm2 = population.get(popSize - 1); // Self
        sm3 = population.get(0);           // Neighbor on the right

        if (task.problem.isFirstBetter(sm1, sm2)) {
            bestNeighbor = task.problem.isFirstBetter(sm1, sm3) ? sm1 : sm3;
        } else {
            bestNeighbor = task.problem.isFirstBetter(sm2, sm3) ? sm2 : sm3;
        }
        population.get(popSize - 1).lBest = new PsoSolutionv2(bestNeighbor);
    }

    /*private void updateLocalBest() {
        int neighborhoodSize = 3; // Define the size of the neighborhood

        for (int i = 0; i < population.size(); i++) {
            PsoSolutionv2 bestNeighbor = population.get(i).pBest;
            for (int offset = -neighborhoodSize / 2; offset <= neighborhoodSize / 2; offset++) {
                int neighborIndex = (i + offset + population.size()) % population.size(); // Circular neighborhood
                if (task.problem.isFirstBetter(population.get(neighborIndex).pBest, bestNeighbor)) {
                    bestNeighbor = population.get(neighborIndex).pBest;
                }
            }
            population.get(i).lBest = bestNeighbor;
        }
    }*/

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            population.add(new PsoSolutionv2(task));
            if (i == 0) gBest = population.get(0);
            else if (task.problem.isFirstBetter(population.get(i), gBest)) gBest = new PsoSolutionv2(population.get(i));
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}