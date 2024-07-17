package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

/**
 * This class implements the depth based tree pruning operator for Genetic Programming.
 * The operator is used to prune the tree to the maximum allowed depth.
 */
public class GPDepthBasedTreePruningOperator extends GPOperator {

    private int maxTreeDepth;
    private ProgramProblem programProblem;

    @Override
    public ProgramSolution execute(ProgramSolution tProgramSolution, ProgramProblem tProgramProblem) {
        this.maxTreeDepth = tProgramProblem.getMaxTreeEndDepth();
        this.programProblem = tProgramProblem;
        pruneProgramDepth(tProgramSolution.getTree().getRootNode(), null, 1);
        return tProgramSolution;
    }

    private void pruneProgramDepth(Node current, Node parent, int currentDepth){
        if(currentDepth >= this.maxTreeDepth){
            if(current.getArity() > 0){
                ProgramSolution newSolution = programProblem.getProgramSolutionGenerator().generate(this.programProblem, currentDepth, "");
                if(parent != null){
                    try {
                        parent.replace(current, newSolution.getTree().getRootNode());
                    } catch (Exception e) {
                        throw new RuntimeException("Error replacing node");
                    }
                }

            }
        }else{
            for (Node child : current.getChildren()) {
                pruneProgramDepth(child, current, currentDepth + 1);
            }
        }
    }
}
