package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

import java.util.List;

/**
 * This class implements the tree size pruning operator for Genetic Programming.
 * The operator is used to prune the tree to the maximum number of nodes.
 * The operator can be used in two modes:
 * 1. NEW_SOLUTION - If the tree size is above the maximum number of nodes, the whole tree is replaced with a new one
 * 2. CLOSEST_TO_MAX_TREE_NODES - If the tree size is above the maximum number of nodes, the node that has the most appropriate number of descendants is replaced with a terminal node
 */
public class GPTreeSizePruningOperator extends FeasibilityGPOperator {
    public enum OperatorType {
        NEW_SOLUTION,
        CLOSEST_TO_MAX_TREE_NODES
    }
    private OperatorType operatorType;

    public GPTreeSizePruningOperator() {
        this(OperatorType.CLOSEST_TO_MAX_TREE_NODES);
    }


    public GPTreeSizePruningOperator(OperatorType operatorType) {
        this.operatorType = operatorType;
    }

    @Override
    public boolean isSolutionFeasible(ProgramSolution tProgramSolution, ProgramProblem tProgramProblem) {
        return tProgramSolution.getTree().treeSize() <= tProgramProblem.getMaxTreeSize();
    }

    @Override
    public ProgramSolution execute(ProgramSolution tProgramSolution, ProgramProblem tProgramProblem) {
        do {
            int treeSize = tProgramSolution.getTree().treeSize();
            int maxTreeSize = tProgramProblem.getMaxTreeSize();

            // 0. If the tree size is already bellow the maximum number of nodes, return the current solution
            if(treeSize <= maxTreeSize){
                break;
            }
            System.out.println("Performing tree size pruning");

            // 1. Get the list of function nodes
            List<Node> functionNodes = tProgramSolution.getTree().getFunctionNodes();
            functionNodes.remove(0); // Remove the root node because we cannot replace it

            // 2. Find the node that has the most appropriate number of ancestors, so the number of ancestors will be equal or bellow the maximum number of nodes after this subtree is removed
            int[] descendantsCount = new int[functionNodes.size()];

            // 2.1
            for (int i = 0; i < functionNodes.size(); i++) {
                // Must substract -1 because the root node is not a descendant of itself and it will be replaced with terminal node
                descendantsCount[i] = functionNodes.get(i).treeSize() - 1;
            }

            // 2.2
            int[] treeNodesDiffs = new int[descendantsCount.length];

            for (int i = 0; i < descendantsCount.length; i++) {
                treeNodesDiffs[i] = treeSize - descendantsCount[i];
            }

            // 2.3
            int[] maxTreeSizeDiffs = new int[treeNodesDiffs.length];

            int minDiffIndex = -1;

            for (int i = 0; i < treeNodesDiffs.length; i++) {
                maxTreeSizeDiffs[i] = maxTreeSize - treeNodesDiffs[i];
                if ((maxTreeSizeDiffs[i] >= 0 && minDiffIndex < 0) || (maxTreeSizeDiffs[i] >= 0 && maxTreeSizeDiffs[i] < maxTreeSizeDiffs[minDiffIndex])) {
                    minDiffIndex = i;
                }
            }

            // 2.4
            if(minDiffIndex == -1 && operatorType == OperatorType.NEW_SOLUTION){
                // 3. b) Replace the whole tree with a new one
                tProgramSolution = tProgramProblem.getRandomSolution();
                break;
            }
            else {
                if(minDiffIndex == -1 && operatorType == OperatorType.CLOSEST_TO_MAX_TREE_NODES) {
                    // 3. a) If no such node is found, find the one that has the most appropriate number of descendants
                    int[] maxTreeNodesDiffs2 = new int[descendantsCount.length];

                    minDiffIndex = 0;
                    maxTreeNodesDiffs2[0] = maxTreeSize - treeNodesDiffs[0];

                    for (int i = 1; i < treeNodesDiffs.length; i++) {
                        maxTreeNodesDiffs2[i] = maxTreeSize - treeNodesDiffs[i];
                        if (maxTreeNodesDiffs2[i] > maxTreeNodesDiffs2[minDiffIndex]) {
                            minDiffIndex = i;
                        }
                    }
                }

                // 4. Replace the selected node with a terminal node
                // Parent is always present because we are not replacing the root node
                Node parent = functionNodes.get(minDiffIndex).parent().get();
                int childIndex = parent.childIndex(functionNodes.get(minDiffIndex));
                parent.replace(childIndex, tProgramProblem.getRandomTerminalNode());

            }

        } while (true);

        // 5. Return the modified tree
        return tProgramSolution;
    }
}
