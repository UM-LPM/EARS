/* Copyright 2009-2015 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.um.feri.ears.algorithms.moo.dbea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.ObjectiveComparator;
import org.um.feri.ears.util.Util;

/* The original Matlab version of I-DBEA was written by Md. Asafuddoula,
 * Tapabrata Ray and Ruhul Sarker.  This class has been tested against their
 * Matlab version to ensure it produces identical results.  See the
 * DBEATest.java class for more information about the testing procedure.
 *
 * A Java version of I-DBEA written by Md Asafuddoula was also cross-referenced
 * when developing this class.  The Java version was released under the GNU
 * LGPL, version 3 or later, and is copyright 2015 Md Asafuddoula.
 *
 * Note: There are some differences between Md Asafuddoula's newer Java version
 * and their older Matlab version, including the removal of corner sort.
 * Experimental tests on their Java version indicate performance between the
 * two versions differ, becoming more substantial with more objectives, with
 * the Matlab version appearing superior.  For this reason, we have replicated
 * the Matlab version within the MOEA Framework.
 */

/**
 * Implementation of the Improved Decomposition-Based Evolutionary Algorithm
 * (I-DBEA).  This implementation is based on the Matlab version published
 * by the original authors.
 * <p>
 * References:
 * <ol>
 *   <li>Asafuddoula, M., T. Ray, and R. Sarker (2015).  "A Decomposition-
 *       Based Evolutionary Algorithm for Many-Objective Optimization."
 *       IEEE Transaction on Evolutionary Computation, 19(3):445-460.
 *   <li><a href="http://seit.unsw.adfa.edu.au/research/sites/mdo/Ray/Research-Data/Matlab-DBEA.rar">
 *       Matlab-DBEA.rar</a>
 * </ol>
 */
public class DBEA<T extends MOTask, Type extends Number> extends MOAlgorithm<T, Type> {

    int populationSize;

    /**
     * Set to {@code true} to remove random permutations to make unit testing
     * easier.
     */
    static boolean TESTING_MODE = false;

    /**
     * The ideal point used for scaling the objectives.
     */
    double[] idealPoint;

    /**
     * The intercepts used for scaling the objectives.
     */
    double[] intercepts;

    /**
     * The reference points (weights).
     */
    List<double[]> weights;

    /**
     * Stores the population
     */
    ParetoSolution<Type> population;

    /**
     * The solutions that define the corners.
     */
    ParetoSolution<Type> corner;

    /**
     * The number of outer divisions for generating reference points.
     */
    private final int divisionsOuter;

    /**
     * The number of inner divisions for generating reference points.
     */
    private final int divisionsInner;

    CrossoverOperator<Type, T, MOSolutionBase<Type>> cross;
    MutationOperator<Type, T, MOSolutionBase<Type>> mut;


    public DBEA(CrossoverOperator crossover, MutationOperator mutation, MOTask problem) {

        this.cross = crossover;
        this.mut = mutation;
        int divisionsOuter = 4;
        int divisionsInner = 0;

        if (problem.getNumberOfObjectives() == 1) {
            divisionsOuter = 100;
        } else if (problem.getNumberOfObjectives() == 2) {
            divisionsOuter = 20;
        } else if (problem.getNumberOfObjectives() == 3) {
            divisionsOuter = 12;
        } else if (problem.getNumberOfObjectives() == 4) {
            divisionsOuter = 8;
        } else if (problem.getNumberOfObjectives() == 5) {
            divisionsOuter = 6;
        } else if (problem.getNumberOfObjectives() == 6) {
            divisionsOuter = 5;
        } else if (problem.getNumberOfObjectives() == 7) {
            divisionsOuter = 3;
            divisionsInner = 2;
        } else if (problem.getNumberOfObjectives() == 8) {
            divisionsOuter = 3;
            divisionsInner = 2;
        } else if (problem.getNumberOfObjectives() == 9) {
            divisionsOuter = 3;
            divisionsInner = 2;
        } else if (problem.getNumberOfObjectives() == 10) {
            divisionsOuter = 3;
            divisionsInner = 2;
        } else {
            divisionsOuter = 2;
            divisionsInner = 1;
        }

        // compute number of reference points
        int populationSize = (int) (CombinatoricsUtils.binomialCoefficient(problem.getNumberOfObjectives() + divisionsOuter - 1, divisionsOuter)
                + (divisionsInner == 0 ? 0 : CombinatoricsUtils.binomialCoefficient(problem.getNumberOfObjectives() + divisionsInner - 1, divisionsInner)));

        this.populationSize = populationSize;

        au = new Author("miha", "miha.ravber at gamil.com");
        ai = new AlgorithmInfo(
                "DBEA",
                "\\bibitem{Asafuddoula2015}\nQ.~Zhang, W.~Liu, H.~Li.\n\\newblock A Decomposition-Based Evolutionary Algorithm for Many-Objective Optimization.\n\\newblock \\emph{IEEE Transaction on Evolutionary Computation}, 19(3):445--460, 2015.\n",
                "DBEA", "Decomposition-Based Evolutionary Algorithm");
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");

        this.divisionsOuter = divisionsOuter;
        this.divisionsInner = divisionsInner;
    }


    public DBEA(CrossoverOperator crossover, MutationOperator mutation, int pop_size, int divisionsOuter, int divisionsInner) {
        this.populationSize = pop_size;
        this.cross = crossover;
        this.mut = mutation;

        au = new Author("miha", "miha.ravber at gamil.com");
        ai = new AlgorithmInfo(
                "DBEA",
                "\\bibitem{Asafuddoula2015}\nQ.~Zhang, W.~Liu, H.~Li.\n\\newblock A Decomposition-Based Evolutionary Algorithm for Many-Objective Optimization.\n\\newblock \\emph{IEEE Transaction on Evolutionary Computation}, 19(3):445--460, 2015.\n",
                "DBEA", "Decomposition-Based Evolutionary Algorithm");
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");

        this.divisionsOuter = divisionsOuter;
        this.divisionsInner = divisionsInner;
    }

    @Override
    protected void init() throws StopCriterionException {
        initPopulation();
        generateWeights();
        preserveCorner();
        initializeIdealPointAndIntercepts();
    }

    @Override
    protected void start() throws StopCriterionException {

        do {
            int[] permutation = Util.randomPermutation(population.size());

            for (int i = 0; i < population.size(); i++) {
                int n = permutation[i];

                MOSolutionBase<Type>[] parents = new MOSolutionBase[2];
                parents[0] = population.get(i);
                parents[1] = population.get(n);

                MOSolutionBase<Type>[] offSpring = cross.execute(parents, task);
                mut.execute(offSpring[0], task);

                if (task.isStopCriterion()) {
                    best = population;
                    return;
                }
                // Evaluation
                task.eval(offSpring[0]);

                if (!checkDomination(offSpring[0])) {
                    updateIdealPointAndIntercepts(offSpring[0]);
                    updatePopulation(offSpring[0]);
                }
            }

            // this call is likely not necessary, but is included in the Matlab
            // version
            preserveCorner();
            task.incrementNumberOfIterations();
        } while (!task.isStopCriterion());
        best = population;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }

    public void initPopulation() throws StopCriterionException {

        population = new ParetoSolution<Type>(populationSize);
        for (int i = 0; i < populationSize; i++) {

            if (task.isStopCriterion())
                return;
            MOSolutionBase<Type> newSolution = new MOSolutionBase<Type>(task.getRandomMOSolution());

            population.add(newSolution);
        }
    }

    /**
     * Return the sum of absolute constraint violations for the given solution.
     *
     * @param solution the solution
     * @return the constraint violation
     */
    private double sumOfConstraintViolations(MOSolutionBase<Type> solution) {
        double result = 0.0;

        for (int i = 0; i < solution.getNumberOfConstraints(); i++) {
            result += Math.abs(solution.getConstraint(i));
        }

        return result;
    }

    /**
     * Returns the allowed constraint violation.
     *
     * @param population the population
     * @return the allowed constraint violation
     */
    double constraintApproach(ParetoSolution<Type> population) {
        double feasible = 1;
        double violation = 0.0;

        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).violatesConstraints()) {
                violation = violation + sumOfConstraintViolations(population.get(i));
            } else {
                feasible++;
            }
        }

        return (feasible / population.size()) * (violation / population.size());
    }

    /**
     * Updates the population with the child solution.
     *
     * @param child the child solution
     */
    void updatePopulation(MOSolutionBase<Type> child) {
        double eps = 0; // unused in I-DBEA
        double eps_con = constraintApproach(population);
        boolean success = false;

        // update the corners if necessary
        if (corner != null && !child.violatesConstraints()) {
            corner.add(child);
            corner = corner_sort(corner);
        }

        double[] f2 = normalizedObjectives(child);
        int[] order = Util.randomPermutation(population.size());

        if (TESTING_MODE) {
            for (int i = 0; i < order.length; i++) {
                order[i] = i;
            }
        }

        for (int i = 0; i < population.size(); i++) {
            int j = order[i];
            double[] weight = weights.get(j);
            double[] f1 = normalizedObjectives(population.get(j));

            double d1_parent = distanceD1(f1, weight);
            double d1_child = distanceD1(f2, weight);
            double d2_parent = distanceD2(f1, d1_parent);
            double d2_child = distanceD2(f2, d1_child);
            double cv_parent = sumOfConstraintViolations(population.get(j));
            double cv_child = sumOfConstraintViolations(child);

            if (cv_child < eps_con && cv_parent < eps_con || (cv_child == cv_parent)) {
                if (compareSolution(d1_child, d2_child, d1_parent, d2_parent, eps)) {
                    population.replace(j, child);
                    success = true;
                }
            }

            if (cv_child < cv_parent) {
                population.replace(j, child);
                success = true;
            }

            if (success) {
                break;
            }
        }
    }

    /**
     * Initializes the ideal point and intercepts based on the bounds of the
     * initial population.
     */
    void initializeIdealPointAndIntercepts() {
        idealPoint = new double[num_obj];
        intercepts = new double[num_obj];

        for (int i = 0; i < num_obj; i++) {
            idealPoint[i] = Double.POSITIVE_INFINITY;
            intercepts[i] = Double.NEGATIVE_INFINITY;
        }

        ParetoSolution<Type> feasibleSolutions = getFeasibleSolutions(population);

        if (!feasibleSolutions.isEmpty()) {
            for (int i = 0; i < feasibleSolutions.size(); i++) {
                for (int j = 0; j < num_obj; j++) {
                    idealPoint[j] = Math.min(idealPoint[j],
                            feasibleSolutions.get(i).getObjective(j));
                    intercepts[j] = Math.max(intercepts[j],
                            feasibleSolutions.get(i).getObjective(j));
                }
            }
        }
    }

    /**
     * Returns {@code true} if this solution is dominated by any member of the
     * population.
     *
     * @param solution the solution
     * @return {@code true} if the solution is dominated; {@code false}
     * otherwise
     */
    boolean checkDomination(MOSolutionBase<Type> solution) {
        if (solution.violatesConstraints()) {
            return false;
        }

        // include the corner solutions
        ParetoSolution<Type> combinedPopulation = new ParetoSolution<Type>();
        combinedPopulation.addAll(population);

        if (corner != null) {
            combinedPopulation.addAll(corner);
        }

        // check for dominance
        for (MOSolutionBase<Type> otherSolution : getFeasibleSolutions(combinedPopulation).solutions) {
            int count = 0;

            for (int i = 0; i < num_obj; i++) {
                if (otherSolution.getObjective(i) < solution.getObjective(i)) {
                    count++;
                }
            }

            if (count == num_obj) {
                return true;
            }

        }

        return false;
    }

    /**
     * Preserve the solutions that comprise the corners of the Pareto front.
     */
    void preserveCorner() {
        ParetoSolution<Type> feasibleSolutions = getFeasibleSolutions(population);

        if (feasibleSolutions.size() >= 2 * num_obj) {
            corner = corner_sort(feasibleSolutions);
        }
    }

    /**
     * Performs corner sort to identify 2*M solutions, where M is the number of
     * objectives, that comprise the corners of the population.
     *
     * @param population the population
     * @return the 2*M corner solutions
     */
    ParetoSolution<Type> corner_sort(ParetoSolution<Type> population) {
        ParetoSolution<Type> unique = new ParetoSolution<Type>();
        ParetoSolution<Type> duplicates = new ParetoSolution<Type>();

        // remove duplicate solutions
        for (int i = 0; i < population.size(); i++) {
            if (unique.contains(population.get(i))) {
                duplicates.add(population.get(i));
            } else {
                boolean isDuplicate = false;

                for (int j = 0; j < unique.size(); j++) {
                    if (Arrays.equals(unique.get(j).getObjectives(), population.get(i).getObjectives())) {
                        duplicates.add(population.get(i));
                        isDuplicate = true;
                        break;
                    }
                }

                if (!isDuplicate) {
                    unique.add(population.get(i));
                }
            }
        }

        // sort the solutions
        List<ParetoSolution<Type>> sortedSets = new ArrayList<ParetoSolution<Type>>();

        for (int i = 0; i < num_obj; i++) {
            sortedSets.add(orderBySmallestObjective(i, unique));
        }

        for (int i = 0; i < num_obj; i++) {
            sortedSets.add(orderBySmallestSquaredValue(i, unique));
        }

        // identify the corners
        ParetoSolution<Type> result = new ParetoSolution<Type>();
        int current_id = 0;
        int current_f = 0;

        while (result.size() < unique.size()) {
            MOSolutionBase<Type> r = sortedSets.get(current_f).get(current_id);

            if (!result.contains(r)) {
                result.add(r);
            }

            current_f++;

            if (current_f >= 2 * num_obj) {
                current_f = 0;
                current_id++;
            }
        }

        result.addAll(duplicates);

        // reduce the set to 2*M solutions
        ParetoSolution<Type> prunedSet = new ParetoSolution<Type>();

        for (int i = 0; i < 2 * num_obj; i++) {
            prunedSet.add(result.get(i));
        }

        return prunedSet;
    }

    /**
     * Returns a copy of the population sorted by the objective value in
     * ascending order.
     *
     * @param objective  the objective
     * @param population the population
     * @return a copy of the population ordered by the objective value
     */
    private ParetoSolution<Type> orderBySmallestObjective(int objective,
                                                          ParetoSolution<Type> population) {
        ParetoSolution<Type> result = new ParetoSolution<Type>();
        result.addAll(population);
        result.sort(new ObjectiveComparator(objective));
        return result;
    }

    /**
     * Returns a copy of the population sorted by the sum-of-squares of all
     * but one objective.
     *
     * @param objective  the ignored objective
     * @param population the population
     * @return a copy of the population ordered by the sum-of-squares of all
     * but one objective
     */
    private ParetoSolution<Type> orderBySmallestSquaredValue(final int objective,
                                                             ParetoSolution<Type> population) {
        ParetoSolution<Type> result = new ParetoSolution<Type>();
        result.addAll(population);

        result.sort(new Comparator<MOSolutionBase<Type>>() {

            @Override
            public int compare(MOSolutionBase<Type> s1, MOSolutionBase<Type> s2) {
                double sum1 = 0.0;
                double sum2 = 0.0;

                for (int i = 0; i < num_obj; i++) {
                    if (i != objective) {
                        sum1 += Math.pow(s1.getObjective(i), 2.0);
                        sum2 += Math.pow(s2.getObjective(i), 2.0);
                    }
                }
                return Double.compare(sum1, sum2);
            }

        });

        return result;
    }

    /**
     * Returns the feasible solutions.
     *
     * @param population the entire population containing feasible and
     *                   infeasible solutions
     * @return the feasible solutions in the population
     */
    private ParetoSolution<Type> getFeasibleSolutions(ParetoSolution<Type> population) {
        ParetoSolution<Type> feasibleSolutions = new ParetoSolution<Type>();

        for (MOSolutionBase<Type> solution : population.solutions) {
            if (!solution.violatesConstraints()) {
                feasibleSolutions.add(solution);
            }
        }

        return feasibleSolutions;
    }

    /**
     * Updates the ideal point and intercepts given the new solution.
     *
     * @param solution the new solution
     */
    void updateIdealPointAndIntercepts(MOSolutionBase<Type> solution) {
        if (!solution.violatesConstraints()) {
            // update the ideal point
            for (int j = 0; j < num_obj; j++) {
                idealPoint[j] = Math.min(idealPoint[j], solution.getObjective(j));
                intercepts[j] = Math.max(intercepts[j], solution.getObjective(j));
            }

            // compute the axis intercepts
            ParetoSolution<Type> feasibleSolutions = getFeasibleSolutions(population);
            feasibleSolutions.add(solution);

            ParetoSolution<Type> nondominatedSolutions = getNondominatedFront(feasibleSolutions);

            if (!nondominatedSolutions.isEmpty()) {
                // find the points with the largest value in each objective
                ParetoSolution<Type> extremePoints = new ParetoSolution<Type>();

                for (int i = 0; i < num_obj; i++) {
                    extremePoints.add(largestObjectiveValue(i, nondominatedSolutions));
                }

                if (numberOfUniqueSolutions(extremePoints) != num_obj) {
                    for (int i = 0; i < num_obj; i++) {
                        intercepts[i] = extremePoints.get(i).getObjective(i);
                    }
                } else {
                    try {
                        RealMatrix b = new Array2DRowRealMatrix(num_obj, 1);
                        RealMatrix A = new Array2DRowRealMatrix(num_obj, num_obj);

                        for (int i = 0; i < num_obj; i++) {
                            b.setEntry(i, 0, 1.0);

                            for (int j = 0; j < num_obj; j++) {
                                A.setEntry(i, j, extremePoints.get(i).getObjective(j));
                            }
                        }

                        double numerator = new LUDecomposition(A).getDeterminant();
                        b.scalarMultiply(numerator);
                        RealMatrix normal = MatrixUtils.inverse(A).multiply(b);

                        for (int i = 0; i < num_obj; i++) {
                            intercepts[i] = numerator / normal.getEntry(i, 0);

                            if (intercepts[i] <= 0 || Double.isNaN(intercepts[i]) || Double.isInfinite(intercepts[i])) {
                                intercepts[i] = extremePoints.get(i).getObjective(i);
                            }
                        }
                    } catch (RuntimeException e) {
                        for (int i = 0; i < num_obj; i++) {
                            intercepts[i] = extremePoints.get(i).getObjective(i);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the non-dominated (rank 0) front.
     *
     * @param population the entire population
     * @return the solutions that are non-dominated
     */
    private ParetoSolution<Type> getNondominatedFront(ParetoSolution<Type> population) {
        NondominatedPopulation<Type> front = new NondominatedPopulation<Type>();
        front.addAll(population);
        return front;
    }

    /**
     * Returns the solution with the largest objective value for the given
     * objective.
     *
     * @param objective  the objective
     * @param population the population of solutions
     * @return the solution with the largest objective value
     */
    private MOSolutionBase<Type> largestObjectiveValue(int objective,
                                                       ParetoSolution<Type> population) {
        MOSolutionBase<Type> largest = null;
        double value = Double.NEGATIVE_INFINITY;

        for (MOSolutionBase<Type> solution : population.solutions) {
            if (solution.getObjective(objective) > value) {
                largest = solution;
                value = solution.getObjective(objective);
            }
        }

        return largest;
    }

    /**
     * Counts the number of unique solutions in the population.  This method
     * checks both the identify of the solutions (if they are the same Java
     * object) and the value of the individual objectives.
     *
     * @param population the population
     * @return the number of unique solutions in the population
     */
    private int numberOfUniqueSolutions(ParetoSolution<Type> population) {
        int count = 0;

        for (int i = 0; i < population.size(); i++) {
            boolean isDuplicate = false;

            for (int j = 0; j < i; j++) {
                if ((population.get(j) == population.get(i)) ||
                        Arrays.equals(population.get(j).getObjectives(),
                                population.get(i).getObjectives())) {
                    isDuplicate = true;
                    break;
                }

                if (!isDuplicate) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Generates the reference directions (weights) based on the number of
     * outer and inner divisions.
     */
    void generateWeights() {
        if (divisionsInner > 0) {
            if (divisionsOuter >= num_obj) {
                System.err.println("The specified number of outer divisions produces intermediate reference points, recommend setting divisionsOuter < numberOfObjectives.");
            }

            weights = generateWeights(divisionsOuter);

            // offset the inner weights
            List<double[]> inner = generateWeights(divisionsInner);

            for (int i = 0; i < inner.size(); i++) {
                double[] weight = inner.get(i);

                for (int j = 0; j < weight.length; j++) {
                    weight[j] = (1.0 / num_obj + weight[j]) / 2;
                }
            }

            weights.addAll(inner);
        } else {
            if (divisionsOuter < num_obj) {
                System.err.println("No intermediate reference points will be generated for the specified number of divisions, recommend increasing divisions");
            }

            weights = generateWeights(divisionsOuter);
        }
    }

    /**
     * Computes the distance away from the ideal point along the reference
     * direction.
     *
     * @param f the normalized objective values for the point
     * @param w the reference direction
     * @return the distance
     */
    private double distanceD1(double[] f, double[] w) {
        double dn = normVector(w);

        for (int j = 0; j < num_obj; j++) {
            w[j] = w[j] / dn;
        }

        return innerproduct(f, w);
    }

    /**
     * Computes the perpendicular distance to the reference direction.
     *
     * @param f  the normalized objective values for the point
     * @param d1 the reference direction
     * @return the perpendicular distance
     */
    private double distanceD2(double[] f, double d1) {
        return Math.sqrt(Math.pow(normVector(f), 2) - Math.pow(d1, 2));
    }

    /**
     * Computes the norm of a vector.
     *
     * @param z the vector
     * @return the norm value
     */
    private double normVector(double[] z) {
        double sum = 0;

        for (int i = 0; i < num_obj; i++) {
            sum += z[i] * z[i];
        }

        return Math.sqrt(sum);
    }

    /**
     * Computes the inner product of two vectors.
     *
     * @param vec1 the first vector
     * @param vec2 the second vector
     * @return the inner product value
     */
    private double innerproduct(double[] vec1, double[] vec2) {
        double sum = 0;

        for (int i = 0; i < vec1.length; i++) {
            sum += vec1[i] * vec2[i];
        }

        return sum;
    }

    /**
     * Returns {@code true} if the child should replace the parent;
     * {@code false} otherwise.
     *
     * @param d1_child  the D1 length of the child
     * @param d2_child  the D2 length of the child
     * @param d1_parent the D1 length of the parent
     * @param d2_parent the D2 length of the parent
     * @param eps       the objective alignment option, set to 0 in I-DBEA
     * @return {@code true} if the child should replace the parent;
     * {@code false} otherwise
     */
    private boolean compareSolution(double d1_child, double d2_child,
                                    double d1_parent, double d2_parent, double eps) {
        if ((d2_child == d2_parent) || ((d2_child < eps) && (d2_parent < eps))) {
            if (d1_child < d1_parent) {
                return true;
            }
        } else if (d2_child < d2_parent) {
            return true;
        }

        return false;
    }

    /**
     * Returns the normalized objective values for the given solution.
     *
     * @param solution the solution
     * @return the normalized objective values
     */
    private double[] normalizedObjectives(MOSolutionBase<Type> solution) {
        double[] objectiveValues = new double[num_obj];

        for (int j = 0; j < num_obj; j++) {
            objectiveValues[j] = (solution.getObjective(j) - idealPoint[j]) /
                    (intercepts[j] - idealPoint[j]);
        }

        return objectiveValues;
    }

    /**
     * Generates the reference points (weights) for the given number of
     * divisions.
     *
     * @param divisions the number of divisions
     * @return the list of reference points
     */
    private List<double[]> generateWeights(int divisions) {
        List<double[]> result = new ArrayList<double[]>();
        double[] weight = new double[num_obj];

        generateRecursive(result, weight, num_obj, divisions, divisions, 0);

        return result;
    }

    /**
     * Generate reference points (weights) recursively.
     *
     * @param weights            list storing the generated reference points
     * @param weight             the partial reference point being recursively generated
     * @param numberOfObjectives the number of objectives
     * @param left               the number of remaining divisions
     * @param total              the total number of divisions
     * @param index              the current index being generated
     */
    private void generateRecursive(List<double[]> weights,
                                   double[] weight, int numberOfObjectives, int left, int total, int index) {
        if (index == (numberOfObjectives - 1)) {
            weight[index] = (double) left / total;
            weights.add(weight.clone());
        } else {
            for (int i = 0; i <= left; i += 1) {
                weight[index] = (double) i / total;
                generateRecursive(weights, weight, numberOfObjectives, left - i, total, index + 1);
            }
        }
    }

}
