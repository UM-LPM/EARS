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


import java.util.Comparator;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.DominanceComparator;
import org.um.feri.ears.util.NonDominatedSolutionList;
import org.um.feri.ears.util.Util;

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
public class OMOPSO extends MOAlgorithm<DoubleMOTask, Double>{

	private int swarmSize;
	private int archiveSize;
	private int currentIteration;
	private int maxIterations;

	private ParetoSolution<Double> swarm;
	private MOSolutionBase<Double>[] localBest;
	private CrowdingDistanceArchive<Double> leaderArchive;
	private NonDominatedSolutionList<Double> epsilonArchive;

	private double[][] speed;

	private Comparator<MOSolutionBase<Double>> dominanceComparator;
	private Comparator<MOSolutionBase<Double>> crowdingDistanceComparator;

	private UniformMutation uniformMutation;
	private NonUniformMutation nonUniformMutation;

	private double eta = 0.0075;

	private CrowdingDistance<Double> crowdingDistance;

	public OMOPSO() {
		this(100,100);
	}
	
	/** Constructor */
	public OMOPSO(int swarmSize, int archiveSize) {

		this.swarmSize = swarmSize;
		this.archiveSize = archiveSize;
		
		au = new Author("miha", "miha.ravber at gamil.com");
		ai = new AlgorithmInfo(
				"OMOPSO",
				"\\bibitem{Deb2002}\nK.~Deb, S.~Agrawal, A.~Pratap, T.~Meyarivan\n\\newblock A fast and elitist multiobjective genetic algorithm: {NSGA-II}.\n\\newblock \\emph{IEEE Transactions on Evolutionary Computation}, 6(2):182--197, 2002.\n",
				"OMOPSO", "Nondominated Sorting Genetic Algorithm II ");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, swarmSize+"");
		ai.addParameter(EnumAlgorithmParameters.ARCHIVE_SIZE, archiveSize+"");
		ai.addParameter(EnumAlgorithmParameters.ETA, eta+"");
		ai.addParameter(EnumAlgorithmParameters.P_M, (1.0 / num_var)+"");
	}

	
	protected void initializeLeader(ParetoSolution<Double> swarm) {
		for (MOSolutionBase<Double> solution : swarm) {
			MOSolutionBase<Double> particle = solution.copy();
			if (leaderArchive.add(particle)) {
				epsilonArchive.add(particle.copy());
			}
		}
	}

	protected void initializeParticlesMemory(ParetoSolution<Double> swarm)  {
		for (int i = 0; i < swarm.size(); i++) {
			localBest[i] = swarm.get(i).copy();
		}
	}

	protected void updateVelocity(ParetoSolution<Double> swarm)  {
		double r1, r2, W, C1, C2;
		MOSolutionBase<Double> bestGlobal;

		for (int i = 0; i < swarmSize; i++) {
			MOSolutionBase<Double> particle = swarm.get(i);
			MOSolutionBase<Double> bestParticle =  localBest[i];

			//Select a global localBest for calculate the speed of particle i, bestGlobal
			MOSolutionBase<Double> one ;
			MOSolutionBase<Double> two;
			int pos1 = Util.nextInt(0, leaderArchive.getSolutionList().size() - 1);
			int pos2 = Util.nextInt(0, leaderArchive.getSolutionList().size() - 1);
			one = leaderArchive.getSolutionList().get(pos1);
			two = leaderArchive.getSolutionList().get(pos2);

			if (crowdingDistanceComparator.compare(one, two) < 1) {
				bestGlobal = one ;
			} else {
				bestGlobal = two ;
			}

			//Parameters for velocity equation
			r1 = Util.nextDouble();
			r2 = Util.nextDouble();
			C1 = Util.nextDouble(1.5, 2.0);
			C2 = Util.nextDouble(1.5, 2.0);
			W = Util.nextDouble(0.1, 0.5);
			//

			for (int var = 0; var < num_var; var++) {
				//Computing the velocity of this particle
				speed[i][var] = W * speed[i][var] + C1 * r1 * (bestParticle.getValue(var) -
						particle.getValue(var)) +
						C2 * r2 * (bestGlobal.getValue(var) - particle.getValue(var));
			}
		}
	}

	/** Update the position of each particle */
	protected void updatePosition(ParetoSolution<Double> swarm)  {
		for (int i = 0; i < swarmSize; i++) {
			MOSolutionBase<Double> particle = swarm.get(i);
			for (int var = 0; var < num_var; var++) {
				particle.setValue(var, particle.getValue(var) + speed[i][var]);
				if (particle.getValue(var) < task.getLowerLimit()[var]) {
					particle.setValue(var, task.getLowerLimit()[var]);
					speed[i][var] = speed[i][var] * -1.0;
				}
				if (particle.getValue(var) > task.getUpperLimit()[var]) {
					particle.setValue(var,task.getUpperLimit()[var]);
					speed[i][var] = speed[i][var] * -1.0;
				}
			}
		}
	}

	protected void updateParticlesMemory(ParetoSolution<Double> swarm) {
		for (int i = 0; i < swarm.size(); i++) {
			int flag = dominanceComparator.compare(swarm.get(i), localBest[i]);
			if (flag != 1) {
				localBest[i] = swarm.get(i).copy();
			}
		}
	}

	protected void initializeVelocity(ParetoSolution<Double> swarm) {
		for (int i = 0; i < swarm.size(); i++) {
			for (int j = 0; j < num_obj; j++) {
				speed[i][j] = 0.0;
			}
		}
	}

	/**  Apply a mutation operator to all particles in the swarm (perturbation) */
	protected void perturbation(ParetoSolution<Double> swarm)  {
		
		nonUniformMutation.setCurrentIteration(currentIteration);

		for (int i = 0; i < swarm.size(); i++) {
			if (i % 3 == 0) {
				nonUniformMutation.execute(swarm.get(i), task);
			} else if (i % 3 == 1) {
				uniformMutation.execute(swarm.get(i), task);
			}
		}
	}

	/**
	 * Update leaders method
	 * @param swarm List of solutions (swarm)
	 */
	protected void updateLeaders(ParetoSolution<Double> swarm) {
		for (MOSolutionBase<Double> solution : swarm) {
			MOSolutionBase<Double> particle = solution.copy();
			if (leaderArchive.add(particle)) {
				epsilonArchive.add(particle.copy());
			}
		}
	}
	
	@Override
	protected void init() throws StopCriteriaException {

		if(optimalParam)
		{
			switch(num_obj){
			case 1:
			{
				swarmSize = 100;
				archiveSize = 100;
				break;
			}
			case 2:
			{
				swarmSize = 100;
				archiveSize = 100;
				break;
			}
			case 3:
			{
				swarmSize = 300;
				archiveSize = 300;
				break;
			}
			default:
			{
				swarmSize = 500;
				archiveSize = 500;
				break;
			}
			}
		}
		
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, swarmSize+"");
		ai.addParameter(EnumAlgorithmParameters.ARCHIVE_SIZE, archiveSize+"");
		
		currentIteration = 1;
		swarm = new ParetoSolution<Double>(swarmSize);
		this.maxIterations = task.getMaxEvaluations() / swarmSize;
		
	    double mutationProbability = 1.0 / num_var;
		this.uniformMutation = new UniformMutation(mutationProbability, 0.5);
		this.nonUniformMutation = new NonUniformMutation(mutationProbability, 0.5, maxIterations);

		localBest = new MOSolutionBase[swarmSize];
		leaderArchive = new CrowdingDistanceArchive<Double>(this.archiveSize);
		epsilonArchive = new NonDominatedSolutionList<Double>(new DominanceComparator(eta));

		dominanceComparator = new DominanceComparator<Double>();
		crowdingDistanceComparator = new CrowdingDistanceComparator<Double>();

		speed = new double[swarmSize][num_var];

		
		// Create the initial population
		MOSolutionBase<Double> newSolution;
		for (int i = 0; i < swarmSize; i++) {
			if (task.isStopCriteria())
				return;
			newSolution = new MOSolutionBase<Double>(task.getRandomMOSolution());
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
	protected void start() throws StopCriteriaException {

		// Generations
		while (!task.isStopCriteria()) {
			
			updateVelocity(swarm);
			updatePosition(swarm);
			perturbation(swarm);
			
			for(MOSolutionBase<Double> s : swarm)
			{
				if (task.isStopCriteria())
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
	public void resetDefaultsBeforNewRun() {
	}

}
