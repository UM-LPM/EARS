//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.moead_dra;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.DifferentialEvolutionCrossover;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.operators.PolynomialMutation;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Distance;
import org.um.feri.ears.util.InitWeight;
import org.um.feri.ears.util.Util;

/**
 * Reference: Q. Zhang,  W. Liu,  and H Li, The Performance of a New Version of 
 * MOEA/D on CEC09 Unconstrained MOP Test Instances, Working Report CES-491, 
 * School of CS & EE, University of Essex, 02/2009 
  */
public class MOEAD_DRA<T extends MOTask, Type extends Number> extends MOAlgorithm<T, Type> {
	
	List<Integer> twoDimfiles = asList(100, 300, 400, 500, 600, 800, 1000);
	List<Integer> threeDimfiles = asList(500, 600, 800, 1000, 1200);
	List<Integer> fiveDimfiles = asList(1000, 1200, 1500, 1800, 2000, 2500);

	int populationSize;
	/**
	 * Stores the population
	 */
	ParetoSolution<Type> population;
	/**
	 * Stores the values of the individuals
	 */
	MOSolutionBase<Type>[] savedValues;

	double[] utility;
	int[] frequency;

	/**
	 * Z vector (ideal point)
	 */
	double[] z;
	/**
	 * Lambda vectors
	 */
	// Vector<Vector<Double>> lambda_ ;
	double[][] lambda;
	/**
	 * T: neighbour size
	 */
	int T = 20;
	/**
	 * Neighborhood
	 */
	int[][] neighborhood;
	/**
	 * delta: probability that parent solutions are selected from neighbourhood
	 */
	double delta = 0.9;
	/**
	 * nr: maximal number of solutions replaced by each child solution
	 */
	int nr = 2;
	MOSolutionBase<Type>[] indArray;
	String functionType;
	int gen;
	int num_var;
	int num_obj;

	static String dataDirectory = "Weight";

	CrossoverOperator<Type, MOTask> cross;
	MutationOperator<Type, MOTask> mut;

	public MOEAD_DRA(CrossoverOperator crossover, MutationOperator mutation, int pop_size) {
		this.populationSize = pop_size;

		this.cross = crossover;
		this.mut = mutation;
		
		au = new Author("miha", "miha.ravber at gamil.com");
		ai = new AlgorithmInfo(
				"MOEAD_DRA",
				"\\bibitem{Zhang2009}\nQ.~Zhang, W.~Liu, H.~Li.\n\\newblock The Performance of a New Version of MOEA/D on CEC09 Unconstrained MOP Test Instances.\n\\newblock \\emph{IEEE Congress on Evolutionary Computation}, 203--208, 2009.\n",
				"MOEAD_DRA", "Multiobjective Evolutionary Algorithm Based on Decomposition");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
	}

	@Override
	public ParetoSolution<Type> run(T taskProblem) throws StopCriteriaException {
		task = taskProblem;
		num_var = task.getDimensions();
		num_obj = task.getNumberOfObjectives();
		
		if(optimalParam)
		{
			switch(num_obj){
			case 1:
			{
				populationSize = 100;
				break;
			}
			case 2:
			{
				populationSize = 100;
				break;
			}
			case 3:
			{
				populationSize = 300;
				break;
			}
			default:
			{
				populationSize = 500;
				break;
			}
			}
		}
			
		long initTime = System.currentTimeMillis();
		init();
		start();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: "+estimatedTime + "ms");

		ParetoSolution<Type> best = finalSelection(populationSize);
		
		if(display_data)
		{
			best.displayData(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemShortName(),task.getProblem());
			best.displayAllUnaryQulaityIndicators(task.getProblem());
		}
		if(save_data)
		{
			best.saveParetoImage(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemShortName());
			best.printFeasibleFUN("FUN_MOEAD_DRA");
			best.printVariablesToFile("VAR");
			best.printObjectivesToCSVFile("FUN");
		}
		
		return best;
	}

	protected void init() throws StopCriteriaException {

		population = new ParetoSolution<Type>(populationSize);
		savedValues = new MOSolutionBase[populationSize];
		utility = new double[populationSize];
		frequency = new int[populationSize];

		indArray = new MOSolutionBase[num_obj];

		neighborhood = new int[populationSize][T];

		z = new double[num_obj];
		// lambda_ = new Vector(problem_.getNumberOfObjectives()) ;
		lambda = new double[populationSize][num_obj];

		// STEP 1. Initialization
		// STEP 1.1. Compute euclidean distances between weight vectors and find T
		initUniformWeight();
		initNeighborhood();

		// STEP 1.2. Initialize population
		initPopulation();

		// STEP 1.3. Initialize z
		initIdealPoint();

		// STEP 1.4
		gen = 0;
		for (int i = 0; i < utility.length; i++) {
			utility[i] = 1.0;
			frequency[i] = 0;
		}
	}

	@Override
	public void resetDefaultsBeforNewRun() {
	}

	protected void start() throws StopCriteriaException {

		PolynomialMutation plm = new PolynomialMutation(1.0 / num_var, 20.0);
		DifferentialEvolutionCrossover dec = new DifferentialEvolutionCrossover();
		
		// STEP 2. Update
		do {
			int[] permutation = Util.randomPermutation(populationSize);
			//Utils.randomPermutation(permutation, populationSize);
			
			List<Integer> order = tour_selection(10);

			for (int i = 0; i < order.size(); i++) {
				// int n = permutation[i]; // or int n = i;
				int n = order.get(i); // or int n = i;
				frequency[n]++;

				int type;
				double rnd = Util.rnd.nextDouble();

				// STEP 2.1. Mating selection based on probability
				if (rnd < delta) // if (rnd < realb)
				{
					type = 1; // neighborhood
				} else {
					type = 2; // whole population
				}

				Vector<Integer> p = new Vector<Integer>();
				matingSelection(p, n, 2, type);

				// STEP 2.2. Reproduction
				MOSolutionBase<Type> child;
				MOSolutionBase<Type>[] parents = new MOSolutionBase[3];

				parents[0] = population.get(p.get(0));
				parents[1] = population.get(p.get(1));
				parents[2] = population.get(n);

				// Apply DE crossover
				cross.setCurrentSolution(population.get(n));
				child = cross.execute(parents, task)[0];

				// Apply mutation
				mut.execute(child, task);

				if (task.isStopCriteria())
					return;
				// Evaluation
				task.eval(child);

				// STEP 2.3. Repair. Not necessary

				// STEP 2.4. Update z_
				updateReference(child);

				// STEP 2.5. Update of solutions
				updateProblem(child, n, type);
			} // for

			gen++;
			if (gen % 30 == 0) {
				comp_utility();
			}

		} while (!task.isStopCriteria());
		System.out.println(gen);
	}

	public void initUniformWeight() {
		
		lambda = InitWeight.generate(num_obj, populationSize, true);
		/*
		if ((num_obj == 2) && (populationSize <= 300)) {
			for (int n = 0; n < populationSize; n++) {
				double a = 1.0 * n / (populationSize - 1);
				lambda[n][0] = a;
				lambda[n][1] = 1 - a;
			}
		} else {
			String dataFileName;
			dataFileName = "W" + num_obj + "D_" + populationSize + ".dat";

			File f = new File(dataDirectory + "/" + dataFileName);
			if(f.exists() && !f.isDirectory()) { 
				try {
					// Open the file
					FileInputStream fis = new FileInputStream(dataDirectory + "/" + dataFileName);
					InputStreamReader isr = new InputStreamReader(fis);
					BufferedReader br = new BufferedReader(isr);

					int numberOfObjectives = 0;
					int i = 0;
					int j = 0;
					String aux = br.readLine();
					while (aux != null) {
						StringTokenizer st = new StringTokenizer(aux);
						j = 0;
						numberOfObjectives = st.countTokens();
						while (st.hasMoreTokens()) {
							double value = (new Double(st.nextToken()))
									.doubleValue();
							lambda[i][j] = value;
							// System.out.println("lambda["+i+","+j+"] = " + value);
							j++;
						}
						aux = br.readLine();
						i++;
					}
					br.close();
				} catch (Exception e) {
					System.err.println("initUniformWeight: failed when reading for file: " + dataDirectory + "/" + dataFileName);
					e.printStackTrace();
				}
			}
			else
			{
				RandomGenerator rg = new RandomGenerator(num_obj, populationSize);
				lambda = rg.generate(); 
			}
		}*/
	}

	public void comp_utility() {
		double f1, f2, uti, delta;
		for (int n = 0; n < populationSize; n++) {
			f1 = fitnessFunction(population.get(n), lambda[n]);
			f2 = fitnessFunction(savedValues[n], lambda[n]);
			delta = f2 - f1;
			if (delta > 0.001)
				utility[n] = 1.0;
			else {
				uti = (0.95 + (0.05 * delta / 0.001)) * utility[n];
				utility[n] = uti < 1.0 ? uti : 1.0;
			}
			savedValues[n] = new MOSolutionBase<Type>(population.get(n));
		}
	}

	public void initNeighborhood() {
		double[] x = new double[populationSize];
		int[] idx = new int[populationSize];

		for (int i = 0; i < populationSize; i++) {
			// calculate the distances based on weight vectors
			for (int j = 0; j < populationSize; j++) {
				// save euclidian distance
				x[j] = Utils.distVector(lambda[i], lambda[j]);
				// x[j] = dist_vector(population[i].namda,population[j].namda);
				idx[j] = j;
				// System.out.println("x["+j+"]: "+x[j]+
				// ". idx["+j+"]: "+idx[j]) ;
			}

			// find 'niche' nearest neighboring subproblems
			Utils.minFastSort(x, idx, populationSize, T);
			// minfastsort(x,idx,population.size(),niche);
			System.arraycopy(idx, 0, neighborhood[i], 0, T);
		}
	}

	public void initPopulation() throws StopCriteriaException {
		for (int i = 0; i < populationSize; i++) {

			if (task.isStopCriteria())
				return;
			MOSolutionBase<Type> newSolution = new MOSolutionBase<Type>(task.getRandomMOIndividual());

			population.add(newSolution);
			savedValues[i] = new MOSolutionBase<Type>(newSolution);
		}
	}

	void initIdealPoint() throws StopCriteriaException {
		for (int i = 0; i < num_obj; i++) {
			z[i] = 1.0e+30;
			if (task.isStopCriteria())
				return;
			indArray[i] = new MOSolutionBase<Type>(task.getRandomMOIndividual());
		}

		for (int i = 0; i < populationSize; i++) {
			updateReference(population.get(i));
		}
	}

	public void matingSelection(Vector<Integer> list, int cid, int size, int type) {
		// list : the set of the indexes of selected mating parents
		// cid : the id of current subproblem
		// size : the number of selected mating parents
		// type : 1 - neighborhood; otherwise - whole population
		int ss;
		int r;
		int p;

		ss = neighborhood[cid].length;
		while (list.size() < size) {
			if (type == 1) {
				r = Util.rnd.nextInt(ss);
				p = neighborhood[cid][r];
				// p = population[cid].table[r];
			} else {
				p = Util.rnd.nextInt(populationSize);
			}
			boolean flag = true;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == p) // p is in the list
				{
					flag = false;
					break;
				}
			}

			// if (flag) list.push_back(p);
			if (flag)
				list.addElement(p);
		}
	}

	public List<Integer> tour_selection(int depth) {
		// selection based on utility
		List<Integer> selected = new ArrayList<Integer>();
		List<Integer> candidate = new ArrayList<Integer>();

		// vzamemo najboljše gled na uteži?
		for (int k = 0; k < num_obj; k++)
			selected.add(k); // WARNING! HERE YOU HAVE TO USE THE WEIGHT
								// PROVIDED BY QINGFU (NOT SORTED!!!!)

		// ostali, ki niso bili izbrani?
		for (int n = num_obj; n < populationSize; n++)
			candidate.add(n); // set of unselected weights

		while (selected.size() < (int) (populationSize / 5.0)) {
			// int best_idd = (int) (rnd_uni(&rnd_uni_init)*candidate.size()),
			// i2;
			// izberemo prvega kandidata
			int best_idd = (int) (Util.rnd.nextDouble() * candidate.size());
			// System.out.println(best_idd);
			int i2;
			int best_sub = candidate.get(best_idd);
			int s2;
			// izberemo naslednjih depth-1 kandidatov
			for (int i = 1; i < depth; i++) {
				i2 = (int) (Util.rnd.nextDouble() * candidate.size());
				s2 = candidate.get(i2);
				// System.out.println("Candidate: "+i2);
				if (utility[s2] > utility[best_sub]) {
					best_idd = i2;
					best_sub = s2;
				}
			}
			selected.add(best_sub);
			candidate.remove(best_idd);
		}
		return selected;
	}

	void updateReference(MOSolutionBase<Type> individual) {
		for (int n = 0; n < num_obj; n++) {
			if (individual.getObjective(n) < z[n]) {
				z[n] = individual.getObjective(n);

				indArray[n] = individual;
			}
		}
	}

	void updateProblem(MOSolutionBase<Type> indiv, int id, int type) {
		// indiv: child solution
		// id: the id of current subproblem
		// type: update solutions in - neighborhood (1) or whole population (otherwise)
		int size;
		int time;

		time = 0;

		if (type == 1) {
			size = neighborhood[id].length;
		} else {
			size = population.size();
		}
		int[] perm = Util.randomPermutation(size);

		//Utils.randomPermutation(perm, size);

		for (int i = 0; i < size; i++) {
			int k;
			if (type == 1) {
				k = neighborhood[id][perm[i]];
			} else {
				k = perm[i]; // calculate the values of objective function regarding the current subproblem
			}
			double f1, f2;

			f1 = fitnessFunction(population.get(k), lambda[k]);
			f2 = fitnessFunction(indiv, lambda[k]);

			if (f2 < f1) {
				population.replace(k, new MOSolutionBase<Type>(indiv));
				// population[k].indiv = indiv;
				time++;
			}
			// the maximal number of solutions updated is not allowed to exceed 'limit'
			if (time >= nr) {
				return;
			}
		}
	}

	double fitnessFunction(MOSolutionBase<Type> individual, double[] lambda) {

		double fitness;
		fitness = 0.0;
		double maxFun = -1.0e+30;

		for (int n = 0; n < num_obj; n++) {
			double diff = Math.abs(individual.getObjective(n) - z[n]);

			double feval;
			if (lambda[n] == 0) {
				//feval = 0.0001 * diff;
				feval = 1.0e-6 * diff;
			} else {
				feval = diff * lambda[n];
			}
			if (feval > maxFun) {
				maxFun = feval;
			}
		}
		
		fitness = maxFun;

		return fitness;
	}

	/**
	 * @author Juanjo 
	 * 		   This method selects N solutions from a set M, where N <= M
	 *         using the same method proposed by Qingfu Zhang, W. Liu, and Hui
	 *         Li in the paper describing MOEA/D-DRA (CEC 09 COMPTETITION) An
	 *         example is giving in that paper for two objectives. If N = 100,
	 *         then the best solutions attenting to the weights (0,1),
	 *         (1/99,98/99), ...,(98/99,1/99), (1,0) are selected.
	 * 
	 *         Using this method result in 101 solutions instead of 100. We will
	 *         just compute 100 even distributed weights and used them. The
	 *         result is the same
	 * 
	 *         In case of more than two objectives the procedure is: 1- Select a
	 *         solution at random 2- Select the solution from the population
	 *         which have maximum distance to it (whithout considering the
	 *         already included)
	 * 
	 * 
	 * 
	 * @param n : The number of solutions to return
	 * @return A solution set containing those elements
	 * 
	 */
	ParetoSolution<Type> finalSelection(int n) {
		ParetoSolution<Type> res = new ParetoSolution<Type>(n);
		if (num_obj == 2) { // subcase 1
			double[][] intern_lambda = new double[n][2];
			for (int i = 0; i < n; i++) {
				double a = 1.0 * i / (n - 1);
				intern_lambda[i][0] = a;
				intern_lambda[i][1] = 1 - a;
			}

			// we have now the weights, now select the best solution for each of them
			for (int i = 0; i < n; i++) {
				MOSolutionBase<Type> current_best = population.get(0);

				double value = fitnessFunction(current_best, intern_lambda[i]);
				for (int j = 1; j < n; j++) {
					double aux = fitnessFunction(population.get(j),intern_lambda[i]); // we are looking the best for the weight i

					if (aux < value) { // solution in position j is better!
						value = aux;
						current_best = population.get(j);
					}
				}
				res.add(new MOSolutionBase<Type>(current_best));
			}

		} else { // general case (more than two objectives)

			Distance<Type> distance_utility = new Distance<Type>();
			int random_index = Util.rnd.nextInt(population.size());

			// create a list containing all the solutions but the selected one (only references to them)
			List<MOSolutionBase<Type>> candidate = new LinkedList<MOSolutionBase<Type>>();
			candidate.add(population.get(random_index));

			for (int i = 0; i < population.size(); i++) {
				if (i != random_index)
					candidate.add(population.get(i));
			}

			while (res.size() < n) {
				int index = 0;
				MOSolutionBase<Type> selected = candidate.get(0); // it should be a next! (n <= population size!)
				double distance_value = distance_utility
						.distanceToSolutionSetInObjectiveSpace(selected, res);
				int i = 1;
				while (i < candidate.size()) {
					MOSolutionBase<Type> next_candidate = candidate.get(i);
					double aux = distance_value = distance_utility.distanceToSolutionSetInObjectiveSpace(next_candidate, res);
					if (aux > distance_value) {
						distance_value = aux;
						index = i;
					}
					i++;
				}

				// add the selected to res and remove from candidate list
				res.add(new MOSolutionBase<Type>(candidate.remove(index)));
			}
		}
		return res;
	}
}
