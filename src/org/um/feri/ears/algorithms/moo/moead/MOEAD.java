//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.moead;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.InitWeight;
import org.um.feri.ears.util.Ranking;
import org.um.feri.ears.util.random.RNG;

import java.util.List;
import java.util.Vector;

import static java.util.Arrays.asList;

/**
 * Implementation of MOEA/D, the multiobjective evolutionary algorithm with
 * decomposition.
 * <p>
 * References:
 * <ol>
 * <li>Li, H. and Zhang, Q. "Multiobjective Optimization problems with
 * Complicated Pareto Sets, MOEA/D and NSGA-II." IEEE Transactions on
 * Evolutionary Computation, 13(2):284-302, 2009.
 * <li>Zhang, Q., et al.  "The Performance of a New Version of MOEA/D on
 * CEC09 Unconstrained MOP Test Instances."  IEEE Congress on Evolutionary
 * Computation, 2009.
 * </ol>
 */
public class MOEAD<N extends Number, P extends NumberProblem<N>> extends MOAlgorithm<N, NumberSolution<N>, P> {

    List<Integer> twoDimfiles = asList(100, 300, 400, 500, 600, 800, 1000);
    List<Integer> threeDimfiles = asList(500, 600, 800, 1000, 1200);
    List<Integer> fiveDimfiles = asList(1000, 1200, 1500, 1800, 2000, 2500);

    int populationSize;
    /**
     * Stores the population
     */
    ParetoSolution<N> population;
    /**
     * Stores the values of the individuals
     */
    NumberSolution<N>[] savedValues;

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
    NumberSolution<N>[] indArray;
    String functionType;
    int gen;

    CrossoverOperator<P, NumberSolution<N>> cross;
    MutationOperator<P, NumberSolution<N>> mut;

    static String dataDirectory = "Weight";

    public MOEAD(CrossoverOperator<P, NumberSolution<N>> crossover, MutationOperator<P, NumberSolution<N>> mutation, int pop_size) {
        this.populationSize = pop_size;
        this.cross = crossover;
        this.mut = mutation;

        au = new Author("miha", "miha.ravber at gamil.com");
        ai = new AlgorithmInfo(
                "MOEAD", "Multiobjective Evolutionary Algorithm Based on Decomposition",
                "\\bibitem{Zhang2009}\nQ.~Zhang, W.~Liu, H.~Li.\n\\newblock The Performance of a New Version of MOEA/D on CEC09 Unconstrained MOP Test Instances.\n\\newblock \\emph{IEEE Congress on Evolutionary Computation}, 203--208, 2009."
        );
    }

    @Override
    protected void init() throws StopCriterionException {

        if (optimalParam) {
            switch (numObj) {
                case 1: {
                    populationSize = 100;
                    break;
                }
                case 2: {
                    populationSize = 100;
                    break;
                }
                case 3: {
                    populationSize = 300;
                    break;
                }
                default: {
                    populationSize = 500;
                    break;
                }
            }
        }

        population = new ParetoSolution<N>(populationSize);
        savedValues = new NumberSolution[populationSize];

        indArray = new NumberSolution[numObj];

        neighborhood = new int[populationSize][T];

        z = new double[numObj];
        // lambda_ = new Vector(problem_.getNumberOfObjectives()) ;
        lambda = new double[populationSize][numObj];

        // STEP 1. Initialization
        // STEP 1.1. Compute euclidean distances between weight vectors and find T
        initUniformWeight();
        initNeighborhood();

        // STEP 1.2. Initialize population
        initPopulation();

        // STEP 1.3. Initialize z
        initIdealPoint();
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

    @Override
    protected void start() throws StopCriterionException {

        // STEP 2. Update
        do {
            int[] permutation = RNG.randomPermutation(populationSize);

            for (int i = 0; i < populationSize; i++) {
                int n = permutation[i]; // or int n = i;

                int type;
                double rnd = RNG.nextDouble();

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
                NumberSolution<N> child;
                NumberSolution<N>[] parents = new NumberSolution[3];

                parents[0] = population.get(p.get(0));
                parents[1] = population.get(p.get(1));
                parents[2] = population.get(n);

                // Apply DE crossover
                cross.setCurrentSolution(population.get(n));
                child = cross.execute(parents, task.problem)[0];

                // Apply mutation
                mut.execute(child, task.problem);

                if (task.isStopCriterion()) {
                    best = population;
                    return;
                }
                // Evaluation
                task.eval(child);

                // STEP 2.3. Repair. Not necessary

                // STEP 2.4. Update z_
                updateReference(child);

                // STEP 2.5. Update of solutions
                updateProblem(child, n, type);
            } // for
            task.incrementNumberOfIterations();
        } while (!task.isStopCriterion());
        //System.out.println(gen);
        Ranking<N> ranking = new Ranking<>(population);
        best = ranking.getSubfront(0);
    }

    public void initUniformWeight() {

        lambda = InitWeight.generate(numObj, populationSize, true);
		/*
		if ((num_obj == 2) && (populationSize <= 100)) {
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
							double value = (Double.parseDouble(st.nextToken()))
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

    public void initPopulation() throws StopCriterionException {
        for (int i = 0; i < populationSize; i++) {

            if (task.isStopCriterion())
                return;
            NumberSolution<N> newSolution = task.getRandomEvaluatedSolution();

            population.add(newSolution);
            savedValues[i] = new NumberSolution<N>(newSolution);
        }
    }

    void initIdealPoint() throws StopCriterionException {
        for (int i = 0; i < numObj; i++) {
            z[i] = 1.0e+30;
            if (task.isStopCriterion())
                return;
            indArray[i] = task.getRandomEvaluatedSolution();
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
                r = RNG.nextInt(ss);
                p = neighborhood[cid][r];
                // p = population[cid].table[r];
            } else {
                p = RNG.nextInt(populationSize);
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

    void updateReference(NumberSolution<N> individual) {
        for (int n = 0; n < numObj; n++) {
            if (individual.getObjective(n) < z[n]) {
                z[n] = individual.getObjective(n);

                indArray[n] = individual;
            }
        }
    }

    void updateProblem(NumberSolution<N> indiv, int id, int type) {
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
        int[] perm = RNG.randomPermutation(size);

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
                population.replace(k, new NumberSolution<N>(indiv));
                // population[k].indiv = indiv;
                time++;
            }
            // the maximal number of solutions updated is not allowed to exceed 'limit'
            if (time >= nr) {
                return;
            }
        }
    }

    double fitnessFunction(NumberSolution<N> individual, double[] lambda) {

        double fitness;
        fitness = 0.0;
        double maxFun = -1.0e+30;

        for (int n = 0; n < numObj; n++) {
            double diff = Math.abs(individual.getObjective(n) - z[n]);

            double feval;
            if (lambda[n] == 0) {
                feval = 0.0001 * diff;
            } else {
                feval = diff * lambda[n];
            }
            if (feval > maxFun) {
                maxFun = feval;
            }
        }

        fitness = maxFun;
		/*if (individual.violatesConstraints()) {
			fitness += 10000.0;
		}*/
        return fitness;
    }
	
/*
	@Override
	public void run() {
		
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
		
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize+"");
		
		if(caching != Cache.None && caching != Cache.Save)
		{
			ParetoSolution<Type> next = returnNext(task.taskInfo());
			if(next != null)
				return next;
			else
				System.out.println("No solution found in chache for algorithm: "+ai.getPublishedAcronym()+" on problem: "+task.getProblemName());
		}
			
		long initTime = System.currentTimeMillis();
		init();
		start();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: "+estimatedTime + "ms");

		ParetoSolution<Type> best = population;
		
		if(display_data)
		{
			best.displayData(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemName());
			best.displayAllUnaryQulaityIndicators(task.getNumberOfObjectives(), task.getProblemFileName());
		}
		if(save_data)
		{
			best.saveParetoImage(this.getAlgorithmInfo().getPublishedAcronym(),task.getProblemName());
			best.printFeasibleFUN("FUN_MOEAD_DRA");
			best.printVariablesToFile("VAR");
			best.printObjectivesToCSVFile("FUN");
		}
		
		if(caching == Cache.Save)
		{
			Util.<Type>addParetoToJSON(getCacheKey(task.taskInfo()),ai.getPublishedAcronym(), best);
		}
		
	}*/
}
