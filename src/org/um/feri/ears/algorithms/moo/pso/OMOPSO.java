//  OMOPSO.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.um.feri.ears.algorithms.moo.pso;


import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.NondominatedPopulation;
import org.um.feri.ears.util.comparator.SolutionDominanceComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.Comparator;

/**
 * Implementation of OMOPSO, a multi-objective particle swarm optimizer (MOPSO).
 * According to [2], OMOPSO is one of the top-performing PSO algorithms.
 * <p>
 * References:
 * <ol>
 *   <li>Sierra, M. R. and C. A. Coello Coello (2005).  Improving PSO-based
 *       Multi-Objective Optimization using Crowding, Mutation and
 *       &epsilon;-Dominance.  Evolutionary Multi-Criterion Optimization,
 *       pp. 505-519.
 *   <li>Durillo, J. J., J. Garc�a-Nieto, A. J. Nebro, C. A. Coello Coello,
 *       F. Luna, and E. Alba (2009).  Multi-Objective Particle Swarm
 *       Optimizers: An Experimental Comparison.  Evolutionary Multi-Criterion
 *       Optimization, pp. 495-509.
 * </ol>
 */

public class OMOPSO extends MOAlgorithm<Double, NumberSolution<Double>, DoubleProblem> {

    private int swarmSize;
    private int archiveSize;
    private int currentIteration;
    private int maxIterations;

    private ParetoSolution<Double> swarm;
    private NumberSolution<Double>[] localBest;
    private CrowdingDistanceArchive<Double> leaderArchive;
    private NondominatedPopulation<Double> epsilonArchive;

    private double[][] speed;

    private SolutionDominanceComparator solutionDominanceComparator;
    private Comparator<NumberSolution<Double>> crowdingDistanceComparator;

    private UniformMutation uniformMutation;
    private NonUniformMutation nonUniformMutation;

    private double eta = 0.0075;

    private CrowdingDistance<Double> crowdingDistance;

    public OMOPSO() {
        this(100, 100);
    }

    /**
     * Constructor
     */
    public OMOPSO(int swarmSize, int archiveSize) {

        this.swarmSize = swarmSize;
        this.archiveSize = archiveSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo(
                "OMOPSO", "Multi Objective Particle Swarm Optimization",
                "\\bibitem{Deb2002}\nK.~Deb, S.~Agrawal, A.~Pratap, T.~Meyarivan\n\\newblock A fast and elitist multiobjective genetic algorithm: {NSGA-II}.\n\\newblock \\emph{IEEE Transactions on Evolutionary Computation}, 6(2):182--197, 2002."
        );
    }

    protected void initializeLeader(ParetoSolution<Double> swarm) {
        for (NumberSolution<Double> solution : swarm) {
            NumberSolution<Double> particle = solution.copy();
            if (leaderArchive.add(particle)) {
                epsilonArchive.add(particle.copy());
            }
        }
    }

    protected void initializeParticlesMemory(ParetoSolution<Double> swarm) {
        for (int i = 0; i < swarm.size(); i++) {
            localBest[i] = swarm.get(i).copy();
        }
    }

    protected void updateVelocity(ParetoSolution<Double> swarm) {
        double r1, r2, W, C1, C2;
        NumberSolution<Double> bestGlobal;

        for (int i = 0; i < swarmSize; i++) {
            NumberSolution<Double> particle = swarm.get(i);
            NumberSolution<Double> bestParticle = localBest[i];

            //Select a global localBest for calculate the speed of particle i, bestGlobal
            NumberSolution<Double> one;
            NumberSolution<Double> two;
            int pos1 = RNG.nextInt(0, leaderArchive.getSolutionList().size());
            int pos2 = RNG.nextInt(0, leaderArchive.getSolutionList().size());
            one = leaderArchive.getSolutionList().get(pos1);
            two = leaderArchive.getSolutionList().get(pos2);

            if (crowdingDistanceComparator.compare(one, two) < 1) {
                bestGlobal = one;
            } else {
                bestGlobal = two;
            }

            //Parameters for velocity equation
            r1 = RNG.nextDouble();
            r2 = RNG.nextDouble();
            C1 = RNG.nextDouble(1.5, 2.0);
            C2 = RNG.nextDouble(1.5, 2.0);
            W = RNG.nextDouble(0.1, 0.5);
            //

            for (int var = 0; var < task.problem.getNumberOfDimensions(); var++) {
                //Computing the velocity of this particle
                speed[i][var] = W * speed[i][var] + C1 * r1 * (bestParticle.getValue(var) -
                        particle.getValue(var)) +
                        C2 * r2 * (bestGlobal.getValue(var) - particle.getValue(var));
            }
        }
    }

    /**
     * Update the position of each particle
     */
    protected void updatePosition(ParetoSolution<Double> swarm) {
        for (int i = 0; i < swarmSize; i++) {
            NumberSolution<Double> particle = swarm.get(i);
            for (int var = 0; var < task.problem.getNumberOfDimensions(); var++) {
                particle.setValue(var, particle.getValue(var) + speed[i][var]);
                if (particle.getValue(var) < task.problem.getLowerLimit(var)) {
                    particle.setValue(var, task.problem.getLowerLimit(var));
                    speed[i][var] = speed[i][var] * -1.0;
                }
                if (particle.getValue(var) > task.problem.getUpperLimit(var)) {
                    particle.setValue(var, task.problem.getUpperLimit(var));
                    speed[i][var] = speed[i][var] * -1.0;
                }
            }
        }
    }

    protected void updateParticlesMemory(ParetoSolution<Double> swarm) {
        for (int i = 0; i < swarm.size(); i++) {
            int flag = solutionDominanceComparator.compare(swarm.get(i), localBest[i]);
            if (flag != 1) {
                localBest[i] = swarm.get(i).copy();
            }
        }
    }

    protected void initializeVelocity(ParetoSolution<Double> swarm) {
        for (int i = 0; i < swarm.size(); i++) {
            for (int j = 0; j < numObj; j++) {
                speed[i][j] = 0.0;
            }
        }
    }

    /**
     * Apply a mutation operator to all particles in the swarm (perturbation)
     */
    protected void perturbation(ParetoSolution<Double> swarm) {

        nonUniformMutation.setCurrentIteration(currentIteration);

        for (int i = 0; i < swarm.size(); i++) {
            if (i % 3 == 0) {
                nonUniformMutation.execute(swarm.get(i), task.problem);
            } else if (i % 3 == 1) {
                uniformMutation.execute(swarm.get(i), task.problem);
            }
        }
    }

    /**
     * Update leaders method
     *
     * @param swarm List of solutions (swarm)
     */
    protected void updateLeaders(ParetoSolution<Double> swarm) {
        for (NumberSolution<Double> solution : swarm) {
            NumberSolution<Double> particle = solution.copy();
            if (leaderArchive.add(particle)) {
                epsilonArchive.add(particle.copy());
            }
        }
    }

    @Override
    protected void init() throws StopCriterionException {

        if (optimalParam) {
            switch (numObj) {
                case 1: {
                    swarmSize = 100;
                    archiveSize = 100;
                    break;
                }
                case 2: {
                    swarmSize = 100;
                    archiveSize = 100;
                    break;
                }
                case 3: {
                    swarmSize = 300;
                    archiveSize = 300;
                    break;
                }
                default: {
                    swarmSize = 500;
                    archiveSize = 500;
                    break;
                }
            }
        }

        currentIteration = 1;
        swarm = new ParetoSolution<>(swarmSize);
        this.maxIterations = task.getMaxEvaluations() / swarmSize;

        double mutationProbability = 1.0 / task.problem.getNumberOfDimensions();
        this.uniformMutation = new UniformMutation(mutationProbability, 0.5);
        this.nonUniformMutation = new NonUniformMutation(mutationProbability, 0.5, maxIterations);

        localBest = new NumberSolution[swarmSize];
        leaderArchive = new CrowdingDistanceArchive<>(this.archiveSize);
        epsilonArchive = new NondominatedPopulation<>(new SolutionDominanceComparator(eta));

        solutionDominanceComparator = new SolutionDominanceComparator();
        crowdingDistanceComparator = new CrowdingDistanceComparator<>();

        speed = new double[swarmSize][task.problem.getNumberOfDimensions()];


        // Create the initial population
        NumberSolution<Double> newSolution;
        for (int i = 0; i < swarmSize; i++) {
            if (task.isStopCriterion())
                return;
            newSolution = new NumberSolution<>(task.getRandomEvaluatedSolution());
            // problem.evaluateConstraints(newSolution);
            swarm.add(newSolution);
        }

        initializeVelocity(swarm);
        initializeParticlesMemory(swarm);
        initializeLeader(swarm);

        crowdingDistance = new CrowdingDistance();
        crowdingDistance.computeDensityEstimator(leaderArchive.getSolutionList());
    }

    @Override
    protected void start() throws StopCriterionException {

        // Generations
        while (!task.isStopCriterion()) {

            updateVelocity(swarm);
            updatePosition(swarm);
            perturbation(swarm);

            for (NumberSolution<Double> s : swarm) {
                if (task.isStopCriterion())
                    break;
                task.eval(s);
            }

            updateLeaders(swarm);
            updateParticlesMemory(swarm);

            crowdingDistance.computeDensityEstimator(leaderArchive.getSolutionList());
            currentIteration++;
            task.incrementNumberOfIterations();
        }

        best = epsilonArchive;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

}
