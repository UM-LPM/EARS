package org.um.feri.ears.operators;

import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.Util;

public class PermutationSwapMutation implements MutationOperator<Integer, MOTask<Integer>, NumberSolution<Integer>> {

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
    public NumberSolution<Integer> execute(NumberSolution<Integer> solution, MOTask<Integer> tb) {
        doMutation(solution, tb);
        return solution;
    }

    /**
     * Performs the operation
     *
     * @param tb
     */
    public void doMutation(NumberSolution<Integer> solution, MOTask<Integer> tb) {
        int permutationLength;
        permutationLength = tb.problem.getNumberOfDimensions();

        if ((permutationLength != 0) && (permutationLength != 1)) {
            if (Util.rnd.nextDouble() < mutationProbability) {
                int pos1 = Util.nextInt(0, permutationLength);
                int pos2 = Util.nextInt(0, permutationLength);

                while (pos1 == pos2) {
                    if (pos1 == (permutationLength - 1))
                        pos2 = Util.nextInt(0, permutationLength - 1);
                    else
                        pos2 = Util.nextInt(pos1, permutationLength);
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
