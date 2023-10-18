package org.um.feri.ears.operators;

import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.random.RNG;

public class PermutationSwapMutation implements MutationOperator<NumberProblem<Integer>, NumberSolution<Integer>> {

    private double mutationProbability;

    /**
     * Constructor
     */
    public PermutationSwapMutation(double mutationProbability) {
        if ((mutationProbability < 0) || (mutationProbability > 1)) {
            System.err.println("Mutation probability value invalid: " + mutationProbability);
        }
        this.mutationProbability = mutationProbability;
    }

    @Override
    public NumberSolution<Integer> execute(NumberSolution<Integer> solution, NumberProblem<Integer> problem) {
        doMutation(solution, problem);
        return solution;
    }

    /**
     * Performs the operation
     *
     * @param problem
     */
    public void doMutation(NumberSolution<Integer> solution, NumberProblem<Integer> problem) {
        int permutationLength;
        permutationLength = problem.getNumberOfDimensions();

        if ((permutationLength != 0) && (permutationLength != 1)) {
            if (RNG.nextDouble() < mutationProbability) {
                int pos1 = RNG.nextInt(0, permutationLength);
                int pos2 = RNG.nextInt(0, permutationLength);

                while (pos1 == pos2) {
                    if (pos1 == (permutationLength - 1))
                        pos2 = RNG.nextInt(0, permutationLength - 1);
                    else
                        pos2 = RNG.nextInt(pos1, permutationLength);
                }

                Integer temp = solution.getValue(pos1);
                solution.setValue(pos1, solution.getValue(pos2));
                solution.setValue(pos2, temp);
            }
        }
    }

    @Override
    public void setProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;

    }
}
