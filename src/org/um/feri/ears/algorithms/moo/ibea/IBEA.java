//  IBEA.java
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
package org.um.feri.ears.algorithms.moo.ibea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.BinaryTournament2;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.SolutionDominanceComparator;
import org.um.feri.ears.util.Ranking;

public class IBEA<N extends Number, P extends NumberProblem<N>> extends MOAlgorithm<N, NumberSolution<N>, P> {

    int populationSize;
    int archiveSize;

    ParetoSolution<N> population;
    ParetoSolution<N> archive;

    CrossoverOperator<P, NumberSolution<N>> cross;
    MutationOperator<P, NumberSolution<N>> mut;

    /**
     * Defines the number of tournaments for creating the mating pool
     */
    public static final int TOURNAMENTS_ROUNDS = 1;

    /**
     * Stores the value of the indicator between each pair of solutions into
     * the solution set
     */
    private List<List<Double>> indicatorValues_;

    /**
     *
     */
    private double maxIndicatorValue_;

    public IBEA(CrossoverOperator<P, NumberSolution<N>> crossover, MutationOperator<P, NumberSolution<N>> mutation, int populationSize, int archiveSize) {

        this.cross = crossover;
        this.mut = mutation;

        this.populationSize = populationSize;
        this.archiveSize = archiveSize;

        au = new Author("miha", "miha.ravber at gamil.com");
        ai = new AlgorithmInfo(
                "IBEA", "Indicator-based evolutionary algorithm",
                "\\bibitem{Zitzler2004}\nE.~Zitzler, S.~Kunzli\n\\newblock Indicator-Based Selection in Multiobjective Search.\n\\newblock \\emph{Parallel Problem Solving from Nature (PPSN VIII)}, 832--842, 2004."
        );
    }

    @Override
    protected void start() throws StopCriterionException {

        ParetoSolution<N> offSpringSolutionSet;

        BinaryTournament2<N> bt2 = new BinaryTournament2<>();

        // Create the initial solutionSet
        NumberSolution<N> newSolution;
        for (int i = 0; i < populationSize; i++) {
            if (task.isStopCriterion())
                return;
            newSolution = new NumberSolution<N>(task.generateRandomEvaluatedSolution());
            // problem.evaluateConstraints(newSolution);
            population.add(newSolution);
        }

        while (!task.isStopCriterion()) {
            ParetoSolution<N> union = population.union(archive);
            calculateFitness(union);
            archive = union;

            while (archive.size() > populationSize) {
                removeWorst(archive);
            }

            // Create a new offspringPopulation
            offSpringSolutionSet = new ParetoSolution<N>(populationSize);
            NumberSolution<N>[] parents = new NumberSolution[2];
            while (offSpringSolutionSet.size() < populationSize) {
                int j = 0;
                do {
                    j++;
                    parents[0] = bt2.execute(archive);
                } while (j < IBEA.TOURNAMENTS_ROUNDS); // do-while
                int k = 0;
                do {
                    k++;
                    parents[1] = bt2.execute(archive);
                } while (k < IBEA.TOURNAMENTS_ROUNDS); // do-while

                //make the crossover
                NumberSolution<N>[] offSpring = cross.execute(parents, task.problem);
                mut.execute(offSpring[0], task.problem);
                if (task.isStopCriterion())
                    break;
                task.eval(offSpring[0]);
                offSpringSolutionSet.add(offSpring[0]);
            } // while
            // End Create a offSpring solutionSet
            population = offSpringSolutionSet;
            task.incrementNumberOfIterations();
        } // while

        // Return the first non-dominated front
        Ranking<N> ranking = new Ranking<N>(archive);
        best = ranking.getSubfront(0);

    }

    @Override
    protected void init() {

        if (optimalParam) {
            switch (numObj) {
                case 1: {
                    populationSize = 100;
                    archiveSize = 100;
                    break;
                }
                case 2: {
                    populationSize = 100;
                    archiveSize = 100;
                    break;
                }
                case 3: {
                    populationSize = 300;
                    archiveSize = 300;
                    break;
                }
                default: {
                    populationSize = 500;
                    archiveSize = 500;
                    break;
                }
            }
        }
        population = new ParetoSolution<N>(populationSize);
        archive = new ParetoSolution<N>(archiveSize);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }

    /**
     * calculates the hypervolume of that portion of the objective space that
     * is dominated by individual a but not by individual b
     */
    double calcHypervolumeIndicator(NumberSolution<N> p_ind_a,
									NumberSolution<N> p_ind_b,
									int d,
									double[] maximumValues,
									double[] minimumValues) {
        double a, b, r, max;
        double volume = 0;
        double rho = 2.0;

        r = rho * (maximumValues[d - 1] - minimumValues[d - 1]);
        max = minimumValues[d - 1] + r;


        a = p_ind_a.getObjective(d - 1);
        if (p_ind_b == null)
            b = max;
        else
            b = p_ind_b.getObjective(d - 1);

        if (d == 1) {
            if (a < b)
                volume = (b - a) / r;
            else
                volume = 0;
        } else {
            if (a < b) {
                volume = calcHypervolumeIndicator(p_ind_a, null, d - 1, maximumValues, minimumValues) *
                        (b - a) / r;
                volume += calcHypervolumeIndicator(p_ind_a, p_ind_b, d - 1, maximumValues, minimumValues) *
                        (max - b) / r;
            } else {
                volume = calcHypervolumeIndicator(p_ind_a, p_ind_b, d - 1, maximumValues, minimumValues) *
                        (max - a) / r;
            }
        }

        return (volume);
    }

    /**
     * This structure store the indicator values of each pair of elements
     */
    public void computeIndicatorValuesHD(ParetoSolution<N> solutionSet,
                                         double[] maximumValues,
                                         double[] minimumValues) {
        ParetoSolution<N> A, B;
        // Initialize the structures
        indicatorValues_ = new ArrayList<List<Double>>();
        maxIndicatorValue_ = -Double.MAX_VALUE;

        for (int j = 0; j < solutionSet.size(); j++) {
            A = new ParetoSolution<N>(1);
            A.add(solutionSet.get(j));

            List<Double> aux = new ArrayList<Double>();
            for (int i = 0; i < solutionSet.size(); i++) {
                B = new ParetoSolution<N>(1);
                B.add(solutionSet.get(i));

                int flag = (new SolutionDominanceComparator()).compare(A.get(0), B.get(0));

                double value = 0.0;
                if (flag == -1) {
                    value = -calcHypervolumeIndicator(A.get(0), B.get(0), numObj, maximumValues, minimumValues);
                } else {
                    value = calcHypervolumeIndicator(B.get(0), A.get(0), numObj, maximumValues, minimumValues);
                }
                //double value = epsilon.epsilon(matrixA,matrixB,problem_.getNumberOfObjectives());


                //Update the max value of the indicator
                if (Math.abs(value) > maxIndicatorValue_)
                    maxIndicatorValue_ = Math.abs(value);
                aux.add(value);
            }
            indicatorValues_.add(aux);
        }
    } // computeIndicatorValues


    /**
     * Calculate the fitness for the individual at position pos
     */
    public void fitness(ParetoSolution<N> solutionSet, int pos) {
        double fitness = 0.0;
        double kappa = 0.05;

        for (int i = 0; i < solutionSet.size(); i++) {
            if (i != pos) {
                fitness += Math.exp((-1 * indicatorValues_.get(i).get(pos) / maxIndicatorValue_) / kappa);
            }
        }
        solutionSet.get(pos).setParetoFitness(fitness);
    }


    /**
     * Calculate the fitness for the entire population.
     **/
    public void calculateFitness(ParetoSolution<N> solutionSet) {
        // Obtains the lower and upper bounds of the population
        double[] maximumValues = new double[numObj];
        double[] minimumValues = new double[numObj];

        for (int i = 0; i < numObj; i++) {
            maximumValues[i] = -Double.MAX_VALUE; // i.e., the minus maxium value
            minimumValues[i] = Double.MAX_VALUE; // i.e., the maximum value
        }

        for (int pos = 0; pos < solutionSet.size(); pos++) {
            for (int obj = 0; obj < numObj; obj++) {
                double value = solutionSet.get(pos).getObjective(obj);
                if (value > maximumValues[obj])
                    maximumValues[obj] = value;
                if (value < minimumValues[obj])
                    minimumValues[obj] = value;
            }
        }

        computeIndicatorValuesHD(solutionSet, maximumValues, minimumValues);
        for (int pos = 0; pos < solutionSet.size(); pos++) {
            fitness(solutionSet, pos);
        }
    }


    /**
     * Update the fitness before removing an individual
     */
    public void removeWorst(ParetoSolution<N> solutionSet) {

        // Find the worst;
        double worst = solutionSet.get(0).getParetoFitness();
        int worstIndex = 0;
        double kappa = 0.05;

        for (int i = 1; i < solutionSet.size(); i++) {
            if (solutionSet.get(i).getParetoFitness() > worst) {
                worst = solutionSet.get(i).getParetoFitness();
                worstIndex = i;
            }
        }

        //if (worstIndex == -1) {
        //    System.out.println("Yes " + worst);
        //}
        //System.out.println("Solution Size "+solutionSet.size());
        //System.out.println(worstIndex);

        // Update the population
        for (int i = 0; i < solutionSet.size(); i++) {
            if (i != worstIndex) {
                double fitness = solutionSet.get(i).getParetoFitness();
                fitness -= Math.exp((-indicatorValues_.get(worstIndex).get(i) / maxIndicatorValue_) / kappa);
                solutionSet.get(i).setParetoFitness(fitness);
            }
        }

        // remove worst from the indicatorValues list
        indicatorValues_.remove(worstIndex); // Remove its own list
        Iterator<List<Double>> it = indicatorValues_.iterator();
        while (it.hasNext())
            it.next().remove(worstIndex);

        // remove the worst individual from the population
        solutionSet.remove(worstIndex);
    } // removeWorst

}
