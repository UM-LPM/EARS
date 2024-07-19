package org.um.feri.ears.operators.gp;

import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

/****************************************************************************************
 * This class implements the node call frequency count based tree pruning operator for Genetic Programming.
 * The operator is used to prune the tree based on the frequency of node calls.
 ****************************************************************************************/
public class GPNodeCallFrequencyCountPruningOperator extends GPOperator {

    /***
     * The threshold for pruning the tree based on the frequency of node calls (in %).
     */
    private final int pruneThreshold;

    public GPNodeCallFrequencyCountPruningOperator() {
        this(0);
    }

    public GPNodeCallFrequencyCountPruningOperator(int pruneThreshold) {
        this.pruneThreshold = pruneThreshold;
    }

    @Override
    public ProgramSolution execute(ProgramSolution tProgramSolution, ProgramProblem tProgramProblem) {
        int[] nodeCallFrequencyCount = tProgramSolution.getNodeCallFrequencyCount();

        // 1. Find the node with the highest frequency of calls (root node contains the total number of calls)
        int maxNodeCallFrequency = nodeCallFrequencyCount[0];

        // 2. Calculate the threshold for pruning
        int threshold = (int) (maxNodeCallFrequency * pruneThreshold / 100.0);

        // 3. Prune the tree based on the threshold and the frequency of node calls (Remove all above the threshold)
        // Start from the end of the array to avoid index shifting
        for (int i = nodeCallFrequencyCount.length - 1; i >= 0; i--) {
            if (nodeCallFrequencyCount[i] < threshold) {
                tProgramSolution.getTree().removeAncestorAt(i);
            }
        }

        return tProgramSolution;
    }
}
