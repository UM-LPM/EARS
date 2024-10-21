package org.um.feri.ears.operators.gp;

import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.random.RNG;

/****************************************************************************************
 * This class implements the node call frequency count based tree pruning operator for Genetic Programming.
 * The operator is used to prune the tree based on the frequency of node calls.
 ****************************************************************************************/
public class GPNodeCallFrequencyCountPruningOperator extends GPOperator {

    /***
     * The threshold for pruning the tree based on the frequency of node calls (in %).
     */
    private final int pruneThreshold;
    /***
     * The probability of pruning the tree if the prune threshold is met.
     */
    private final double pruneProbability; // TODO Test if this parameter improves the overall performance

    public GPNodeCallFrequencyCountPruningOperator() {
        this(0, 0.0);
    }

    public GPNodeCallFrequencyCountPruningOperator(int pruneThreshold, double pruneProbability) {
        this.pruneThreshold = pruneThreshold;
        this.pruneProbability = pruneProbability;
    }

    @Override
    public ProgramSolution execute(ProgramSolution tProgramSolution, ProgramProblem tProgramProblem) {
        int[] nodeCallFrequencyCount = tProgramSolution.getNodeCallFrequencyCount();

        // 1. Find the node with the highest frequency of calls (root node contains the total number of calls)
        int maxNodeCallFrequency = nodeCallFrequencyCount[0];

        // 2. Calculate the threshold for pruning
        int threshold = (int) (maxNodeCallFrequency * this.pruneThreshold / 100.0);

        // 3. Prune the tree based on the threshold and the frequency of node calls (Remove all above the threshold)
        // Start from the end of the array to avoid index shifting
        for (int i = nodeCallFrequencyCount.length - 1; i >= 0; i--) {
            if (nodeCallFrequencyCount[i] < threshold && RNG.nextDouble() < this.pruneProbability) {
                tProgramSolution.getTree().removeAncestorAt(i, false);
            }
        }

        tProgramSolution.getTree().removeInvalidNodes(tProgramProblem);

        tProgramProblem.makeFeasible(tProgramSolution);

        return tProgramSolution;
    }
}
