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
package org.um.feri.ears.algorithms.moo.moead_dra;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.moo.moead.Utils;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.random.RNG;

import java.util.LinkedList;
import java.util.Vector;

public class MOEAD_STM<N extends Number, P extends NumberProblem<N>> extends MOEAD_DRA<N, P> {

    // nadir point
    protected double[] nadirPoint;

    ParetoSolution<N> offspringPopulation;
    ParetoSolution<N> jointPopulation;


    public MOEAD_STM(CrossoverOperator<P, NumberSolution<N>> crossover, MutationOperator<P, NumberSolution<N>> mutation, int pop_size) {
        super(crossover, mutation, pop_size);

        au = new Author("miha", "miha.ravber at gamil.com");
        ai = new AlgorithmInfo(
                "MOEAD_STM", "Multiobjective Evolutionary Algorithm Based on Decomposition",
                "\\bibitem{Zhang2009}\nQ.~Zhang, W.~Liu, H.~Li.\n\\newblock The Performance of a New Version of MOEA/D on CEC09 Unconstrained MOP Test Instances.\n\\newblock \\emph{IEEE Congress on Evolutionary Computation}, 203--208, 2009."
        );
    }

    @Override
    protected void init() throws StopCriterionException {
        super.init();

        offspringPopulation = new ParetoSolution<N>(populationSize);
        jointPopulation = new ParetoSolution<N>(2 * populationSize);
        nadirPoint = new double[numObj];
        initializeNadirPoint();
    }

    @Override
    protected void start() throws StopCriterionException {

        // STEP 2. Update
        do {
            int[] permutation = RNG.randomPermutation(populationSize);
            //Utils.randomPermutation(permutation, populationSize);

            offspringPopulation.clear();

            for (int i = 0; i < populationSize; i++) {

                int n = permutation[i]; // or int n = i;
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
                updateNadirPoint(child);

                // STEP 2.5. Update of solutions
                //updateProblem(child, n, type);

                offspringPopulation.add(child);
            }

            // Combine the parent and the current offspring populations
            jointPopulation.clear();
            jointPopulation.addAll(population);
            jointPopulation.addAll(offspringPopulation);

            // selection process
            stmSelection();
			
			/*
			gen++;
			if (gen % 30 == 0) {
				comp_utility();
			}*/
            task.incrementNumberOfIterations();
        } while (!task.isStopCriterion());
        //System.out.println(gen);
        best = finalSelection(populationSize);
    }

    //initialize the nadir point
    protected void initializeNadirPoint() {
        for (int i = 0; i < numObj; i++)
            nadirPoint[i] = -1.0e+30;
        for (int i = 0; i < populationSize; i++)
            updateNadirPoint(population.get(i));
    }

    // update the current nadir point
    void updateNadirPoint(NumberSolution<N> individual) {
        for (int i = 0; i < numObj; i++) {
            if (individual.getObjective(i) > nadirPoint[i]) {
                nadirPoint[i] = individual.getObjective(i);
            }
        }
    }

    /**
     * Select the next parent population, based on the stable matching criteria
     */
    public void stmSelection() {

        int[] idx = new int[populationSize];
        double[] nicheCount = new double[populationSize];

        int[][] solPref = new int[jointPopulation.size()][];
        double[][] solMatrix = new double[jointPopulation.size()][];
        double[][] distMatrix = new double[jointPopulation.size()][];
        double[][] fitnessMatrix = new double[jointPopulation.size()][];

        for (int i = 0; i < jointPopulation.size(); i++) {
            solPref[i] = new int[populationSize];
            solMatrix[i] = new double[populationSize];
            distMatrix[i] = new double[populationSize];
            fitnessMatrix[i] = new double[populationSize];
        }
        int[][] subpPref = new int[populationSize][];
        double[][] subpMatrix = new double[populationSize][];
        for (int i = 0; i < populationSize; i++) {
            subpPref[i] = new int[jointPopulation.size()];
            subpMatrix[i] = new double[jointPopulation.size()];
        }

        // Calculate the preference values of solution matrix
        for (int i = 0; i < jointPopulation.size(); i++) {
            int minIndex = 0;
            for (int j = 0; j < populationSize; j++) {
                fitnessMatrix[i][j] = fitnessFunction(jointPopulation.get(i), lambda[j]);
                distMatrix[i][j] = calculateDistance(jointPopulation.get(i), lambda[j]);
                if (distMatrix[i][j] < distMatrix[i][minIndex])
                    minIndex = j;
            }
            nicheCount[minIndex] = nicheCount[minIndex] + 1;
        }

        // calculate the preference values of subproblem matrix and solution matrix
        for (int i = 0; i < jointPopulation.size(); i++) {
            for (int j = 0; j < populationSize; j++) {
                subpMatrix[j][i] = fitnessFunction(jointPopulation.get(i), lambda[j]);
                solMatrix[i][j] = distMatrix[i][j] + nicheCount[j];
            }
        }

        // sort the preference value matrix to get the preference rank matrix
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < jointPopulation.size(); j++)
                subpPref[i][j] = j;
            Utils.quickSort(subpMatrix[i], subpPref[i], 0, jointPopulation.size() - 1);
        }
        for (int i = 0; i < jointPopulation.size(); i++) {
            for (int j = 0; j < populationSize; j++)
                solPref[i][j] = j;
            Utils.quickSort(solMatrix[i], solPref[i], 0, populationSize - 1);
        }

        idx = stableMatching(subpPref, solPref, populationSize, jointPopulation.size());

        population.clear();
        for (int i = 0; i < populationSize; i++)
            population.add(jointPopulation.get(idx[i]));

    }

    /**
     * Return the stable matching between 'subproblems' and 'solutions'
     * ('subproblems' propose first). It is worth noting that the number of
     * solutions is larger than that of the subproblems.
     */
    public int[] stableMatching(int[][] manPref, int[][] womanPref, int menSize, int womenSize) {

        // Indicates the mating status
        int[] statusMan = new int[menSize];
        int[] statusWoman = new int[womenSize];

        final int NOT_ENGAGED = -1;
        for (int i = 0; i < womenSize; i++)
            statusWoman[i] = NOT_ENGAGED;

        // List of men that are not currently engaged.
        LinkedList<Integer> freeMen = new LinkedList<Integer>();
        for (int i = 0; i < menSize; i++)
            freeMen.add(i);

        // next[i] is the next woman to whom i has not yet proposed.
        int[] next = new int[womenSize];

        while (!freeMen.isEmpty()) {
            int m = freeMen.remove();
            int w = manPref[m][next[m]];
            next[m]++;
            if (statusWoman[w] == NOT_ENGAGED) {
                statusMan[m] = w;
                statusWoman[w] = m;
            } else {
                int m1 = statusWoman[w];
                if (prefers(m, m1, womanPref[w], menSize)) {
                    statusMan[m] = w;
                    statusWoman[w] = m;
                    freeMen.add(m1);
                } else {
                    freeMen.add(m);
                }
            }
        }

        return statusMan;
    }

    /**
     * Returns true in case that a given woman prefers x to y.
     */
    public boolean prefers(int x, int y, int[] womanPref, int size) {

        for (int i = 0; i < size; i++) {
            int pref = womanPref[i];
            if (pref == x)
                return true;
            if (pref == y)
                return false;
        }
        // this should never happen.
        System.out.println("Error in womanPref list!");
        return false;
    }

    /**
     * Calculate the perpendicular distance between the solution and reference
     * line
     */
    public double calculateDistance(NumberSolution<N> individual, double[] lambda) {
        double scale;
        double distance;

        double[] vecInd = new double[numObj];
        double[] vecProj = new double[numObj];

        // vecInd has been normalized to the range [0,1]
        for (int i = 0; i < numObj; i++)
            vecInd[i] = (individual.getObjective(i) - z[i]) / (nadirPoint[i] - z[i]);

        scale = innerProduct(vecInd, lambda) / innerProduct(lambda, lambda);
        for (int i = 0; i < numObj; i++)
            vecProj[i] = vecInd[i] - scale * lambda[i];

        distance = norm_vector(vecProj);

        return distance;
    }

    /**
     * Calculate the norm of the vector
     */
    public double norm_vector(double[] z) {
        double sum = 0;

        for (int i = 0; i < numObj; i++)
            sum += z[i] * z[i];

        return Math.sqrt(sum);
    }

    /**
     * Calculate the dot product of two vectors
     */
    public double innerProduct(double[] vec1, double[] vec2) {
        double sum = 0;

        for (int i = 0; i < vec1.length; i++)
            sum += vec1[i] * vec2[i];

        return sum;
    }
}
