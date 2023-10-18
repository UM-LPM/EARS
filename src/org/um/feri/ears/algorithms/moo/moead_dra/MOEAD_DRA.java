//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.algorithms.moo.moead_dra;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.moead.Utils;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Distance;
import org.um.feri.ears.util.InitWeight;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import static java.util.Arrays.asList;

/**
 * Reference: Q. Zhang,  W. Liu,  and H Li, The Performance of a New Version of
 * MOEA/D on CEC09 Unconstrained MOP Test Instances, Working Report CES-491,
 * School of CS & EE, University of Essex, 02/2009
 */
public class MOEAD_DRA<N extends Number, P extends NumberProblem<N>> extends MOAlgorithm<N, NumberSolution<N>, P> {

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
     * T: neighbor size
     */
    int T = 20;
    /**
     * Neighborhood
     */
    int[][] neighborhood;
    /**
     * delta: probability that parent solutions are selected from neighborhood
     */
    double delta = 0.9;
    /**
     * nr: maximal number of solutions replaced by each child solution
     */
    int nr = 2;
    NumberSolution<N>[] indArray;
    String functionType;
    int gen;

    static String dataDirectory = "Weight";

    CrossoverOperator<P, NumberSolution<N>> cross;
    MutationOperator<P, NumberSolution<N>> mut;

    public MOEAD_DRA(CrossoverOperator<P, NumberSolution<N>> crossover, MutationOperator<P, NumberSolution<N>> mutation, int pop_size) {
        this.populationSize = pop_size;

        this.cross = crossover;
        this.mut = mutation;

        au = new Author("miha", "miha.ravber at gamil.com");
        ai = new AlgorithmInfo(
                "MOEAD_DRA", "Multiobjective Evolutionary Algorithm Based on Decomposition",
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
        utility = new double[populationSize];
        frequency = new int[populationSize];

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

        // STEP 1.4
        gen = 0;
        for (int i = 0; i < utility.length; i++) {
            utility[i] = 1.0;
            frequency[i] = 0;
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

    protected void start() throws StopCriterionException {

        // STEP 2. Update
        do {
            int[] permutation = RNG.randomPermutation(populationSize);
            //Utils.randomPermutation(permutation, populationSize);

            List<Integer> order = tour_selection(10);

            for (int i = 0; i < order.size(); i++) {
                // int n = permutation[i]; // or int n = i;
                int n = order.get(i); // or int n = i;
                frequency[n]++;

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
                    best = finalSelection(populationSize);
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

            gen++;
            if (gen % 30 == 0) {
                comp_utility();
            }
            task.incrementNumberOfIterations();
        } while (!task.isStopCriterion());
        System.out.println(gen);

        best = finalSelection(populationSize);
    }

    public void initUniformWeight() {

        lambda = InitWeight.generate(numObj, populationSize, true);
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
            savedValues[n] = new NumberSolution<N>(population.get(n));
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

    public void initPopulation() throws StopCriterionException {
        for (int i = 0; i < populationSize; i++) {

            if (task.isStopCriterion())
                return;
            NumberSolution<N> newSolution = new NumberSolution<N>(task.problem.getRandomEvaluatedSolution());

            population.add(newSolution);
            savedValues[i] = new NumberSolution<N>(newSolution);
        }
    }

    void initIdealPoint() throws StopCriterionException {
        for (int i = 0; i < numObj; i++) {
            z[i] = 1.0e+30;
            if (task.isStopCriterion())
                return;
            indArray[i] = new NumberSolution<N>(task.problem.getRandomEvaluatedSolution());
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

    public List<Integer> tour_selection(int depth) {
        // selection based on utility
        List<Integer> selected = new ArrayList<Integer>();
        List<Integer> candidate = new ArrayList<Integer>();

        for (int k = 0; k < numObj; k++)
            selected.add(k); // WARNING! HERE YOU HAVE TO USE THE WEIGHT
        // PROVIDED BY QINGFU (NOT SORTED!!!!)


        for (int n = numObj; n < populationSize; n++)
            candidate.add(n); // set of unselected weights

        while (selected.size() < (int) (populationSize / 5.0)) {
            // int best_idd = (int) (rnd_uni(&rnd_uni_init)*candidate.size()),
            // i2;
            // izberemo prvega kandidata
            int best_idd = (int) (RNG.nextDouble() * candidate.size());
            // System.out.println(best_idd);
            int i2;
            int best_sub = candidate.get(best_idd);
            int s2;
            // izberemo naslednjih depth-1 kandidatov
            for (int i = 1; i < depth; i++) {
                i2 = (int) (RNG.nextDouble() * candidate.size());
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
     * @param n : The number of solutions to return
     * @return A solution set containing those elements
     * @author Juanjo
     * This method selects N solutions from a set M, where N <= M
     * using the same method proposed by Qingfu Zhang, W. Liu, and Hui
     * Li in the paper describing MOEA/D-DRA (CEC 09 COMPTETITION) An
     * example is giving in that paper for two objectives. If N = 100,
     * then the best solutions attenting to the weights (0,1),
     * (1/99,98/99), ...,(98/99,1/99), (1,0) are selected.
     * <p>
     * Using this method result in 101 solutions instead of 100. We will
     * just compute 100 even distributed weights and used them. The
     * result is the same
     * <p>
     * In case of more than two objectives the procedure is: 1- Select a
     * solution at random 2- Select the solution from the population
     * which have maximum distance to it (whithout considering the
     * already included)
     */
    ParetoSolution<N> finalSelection(int n) {
        ParetoSolution<N> res = new ParetoSolution<N>(n);
        if (numObj == 2) { // subcase 1
            double[][] intern_lambda = new double[n][2];
            for (int i = 0; i < n; i++) {
                double a = 1.0 * i / (n - 1);
                intern_lambda[i][0] = a;
                intern_lambda[i][1] = 1 - a;
            }

            // we have now the weights, now select the best solution for each of them
            for (int i = 0; i < n; i++) {
                NumberSolution<N> current_best = population.get(0);

                double value = fitnessFunction(current_best, intern_lambda[i]);
                for (int j = 1; j < n; j++) {
                    double aux = fitnessFunction(population.get(j), intern_lambda[i]); // we are looking the best for the weight i

                    if (aux < value) { // solution in position j is better!
                        value = aux;
                        current_best = population.get(j);
                    }
                }
                res.add(new NumberSolution<N>(current_best));
            }

        } else { // general case (more than two objectives)

            Distance<N> distance_utility = new Distance<N>();
            int random_index = RNG.nextInt(population.size());

            // create a list containing all the solutions but the selected one (only references to them)
            List<NumberSolution<N>> candidate = new LinkedList<NumberSolution<N>>();
            candidate.add(population.get(random_index));

            for (int i = 0; i < population.size(); i++) {
                if (i != random_index)
                    candidate.add(population.get(i));
            }

            while (res.size() < n) {
                int index = 0;
                NumberSolution<N> selected = candidate.get(0); // it should be a next! (n <= population size!)
                double distance_value = distance_utility
                        .distanceToSolutionSetInObjectiveSpace(selected, res);
                int i = 1;
                while (i < candidate.size()) {
                    NumberSolution<N> next_candidate = candidate.get(i);
                    double aux = distance_value = distance_utility.distanceToSolutionSetInObjectiveSpace(next_candidate, res);
                    if (aux > distance_value) {
                        distance_value = aux;
                        index = i;
                    }
                    i++;
                }

                // add the selected to res and remove from candidate list
                res.add(new NumberSolution<N>(candidate.remove(index)));
            }
        }
        return res;
    }
}
