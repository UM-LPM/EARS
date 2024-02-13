package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.problems.gp.ProgramProblem2;
import org.um.feri.ears.problems.gp.ProgramSolution2;

import java.util.ArrayList;
import java.util.List;

public class GPTreeSizePruningOperator2 extends  GPOperator2 {
    public enum OperatorType {
        NEW_SOLUTION,
        CLOSEST_TO_MAX_TREE_NODES
    }
    private OperatorType operatorType;

    public GPTreeSizePruningOperator2() {
        this(OperatorType.CLOSEST_TO_MAX_TREE_NODES);
    }


    public GPTreeSizePruningOperator2(OperatorType operatorType) {
        this.operatorType = operatorType;
    }
    public ProgramSolution2 execute(ProgramSolution2 tProgramSolution, ProgramProblem2 tProgramProblem) {
        do {
            int numberOfNodes = tProgramSolution.getTree().numberOfNodes();
            int maxTreeNodes = tProgramProblem.getMaxTreeNodes();

            // 0. If the tree size is already bellow the maximum number of nodes, return the current solution
            if(numberOfNodes <= maxTreeNodes){
                break;
            }

            // 1. Get the list of function nodes
            List<Node> functionNodes = tProgramSolution.getTree().getFunctionNodes();
            functionNodes.remove(0); // Remove the root node because we cannot replace it

            // 2. Find the node that has the most appropriate number of ancestors, so the number of ancestors will be equal or bellow the maximum number of nodes after this subtree is removed
            int[] descendantsCount = new int[functionNodes.size()];

            // 2.1
            for (int i = 0; i < functionNodes.size(); i++) {
                // Must substract -1 because the root node is not a descendant of itself and it will be replaced with terminal node
                descendantsCount[i] = functionNodes.get(i).numberOfNodes() - 1;
            }

            // 2.2
            int[] treeNodesDiffs = new int[descendantsCount.length];

            for (int i = 0; i < descendantsCount.length; i++) {
                treeNodesDiffs[i] = numberOfNodes - descendantsCount[i];
            }

            // 2.3
            int[] maxTreeNodesDiffs = new int[treeNodesDiffs.length];

            int minDiffIndex = -1;

            for (int i = 0; i < treeNodesDiffs.length; i++) {
                maxTreeNodesDiffs[i] = maxTreeNodes - treeNodesDiffs[i];
                if ((maxTreeNodesDiffs[i] >= 0 && minDiffIndex < 0) || (maxTreeNodesDiffs[i] >= 0 && maxTreeNodesDiffs[i] < maxTreeNodesDiffs[minDiffIndex])) {
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
                    maxTreeNodesDiffs2[0] = maxTreeNodes - treeNodesDiffs[0];

                    for (int i = 1; i < treeNodesDiffs.length; i++) {
                        maxTreeNodesDiffs2[i] = maxTreeNodes - treeNodesDiffs[i];
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
