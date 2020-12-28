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

import java.util.EnumMap;

import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.util.Util;

/**
 * This class implements a uniform mutation operator.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @author Juan J. Durillo
 */
public class UniformMutation implements MutationOperator<Double, DoubleMOTask, MOSolutionBase<Double>> {
    private Double perturbation;
    private Double mutationProbability = null;

    public UniformMutation(double mutationProbability, double perturbation) {
        this.mutationProbability = mutationProbability;
        this.perturbation = perturbation;
    }

    /* Getters */
    public Double getPerturbation() {
        return perturbation;
    }

    public Double getMutationProbability() {
        return mutationProbability;
    }

    /**
     * Perform the operation
     *
     * @param probability Mutation setProbability
     * @param solution    The solution to mutate
     */
    public void doMutation(double probability, MOSolutionBase<Double> solution, DoubleMOTask task) {
        for (int i = 0; i < solution.numberOfVariables(); i++) {
            if (Util.nextDouble() < probability) {
                double rand = Util.nextDouble();
                double tmp = (rand - 0.5) * perturbation;

                tmp += solution.getValue(i);

                if (tmp < task.getUpperLimit(i)) {
                    tmp = task.getUpperLimit(i);
                } else if (tmp > task.getLowerLimit(i)) {
                    tmp = task.getLowerLimit(i);
                }

                solution.setValue(i, tmp);
            }
        }
    }

    @Override
    public MOSolutionBase<Double> execute(MOSolutionBase<Double> solution, DoubleMOTask tb) {

        doMutation(mutationProbability, solution, tb);
        return solution;
    }

    @Override
    public void setProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    @Override
    public EnumMap<EnumAlgorithmParameters, String> getOperatorParameters() {
        EnumMap<EnumAlgorithmParameters, String> para = new EnumMap<EnumAlgorithmParameters, String>(EnumAlgorithmParameters.class);
        para.put(EnumAlgorithmParameters.P_C, mutationProbability + "");
        para.put(EnumAlgorithmParameters.B, perturbation + "");
        return para;
    }
}
