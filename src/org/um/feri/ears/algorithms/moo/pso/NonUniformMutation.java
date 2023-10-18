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

import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.random.RNG;

/**
 * This class implements a non-uniform mutation operator.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @author Juan J. Durillo
 */
public class NonUniformMutation implements MutationOperator<DoubleProblem, NumberSolution<Double>> {
    private double perturbation;
    private int maxIterations;
    private double mutationProbability;
    private int currentIteration;


    /**
     * Constructor
     */
    public NonUniformMutation(double mutationProbability, double perturbation, int maxIterations) {
        this.perturbation = perturbation;
        this.mutationProbability = mutationProbability;
        this.maxIterations = maxIterations;
    }

    /* Getters */
    public double getPerturbation() {
        return perturbation;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public int getCurrentIteration() {
        return currentIteration;
    }

    /* Setter */
    public void setCurrentIteration(int currentIteration) {
        this.currentIteration = currentIteration;
    }

    /**
     * Perform the mutation operation
     *
     * @param probability Mutation setProbability
     * @param solution    The solution to mutate
     */
    public void doMutation(double probability, NumberSolution<Double> solution, DoubleProblem problem) {
        for (int i = 0; i < solution.numberOfVariables(); i++) {
            if (RNG.nextDouble() < probability) {
                double rand = RNG.nextDouble();
                double tmp;

                if (rand <= 0.5) {
                    tmp = delta(problem.getUpperLimit(i) - solution.getValue(i),
                            perturbation);
                    tmp += solution.getValue(i);
                } else {
                    tmp = delta(problem.getLowerLimit(i) - solution.getValue(i),
                            perturbation);
                    tmp += solution.getValue(i);
                }

                if (tmp < problem.getLowerLimit(i)) {
                    tmp = problem.getLowerLimit(i);
                } else if (tmp > problem.getUpperLimit(i)) {
                    tmp = problem.getUpperLimit(i);
                }
                solution.setValue(i, tmp);
            }
        }
    }


    /**
     * Calculates the delta value used in NonUniform mutation operator
     */
    private double delta(double y, double bMutationParameter) {
        double rand = RNG.nextDouble();
        int it, maxIt;
        it = currentIteration;
        maxIt = maxIterations;

        return (y * (1.0 -
                Math.pow(rand,
                        Math.pow((1.0 - it / (double) maxIt), bMutationParameter)
                )));
    }

    @Override
    public NumberSolution<Double> execute(NumberSolution<Double> solution, DoubleProblem problem) {

        doMutation(mutationProbability, solution, problem);
        return solution;
    }

    @Override
    public void setProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;

    }
}
