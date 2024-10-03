package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

public class GPTreeExpansionOperator extends FeasibilityGPOperator {

    private int minTreeDepth;
    private ProgramProblem programProblem;

    @Override
    public boolean isSolutionFeasible(ProgramSolution tProgramSolution, ProgramProblem tProgramProblem) {
        return tProgramSolution.getTree().treeMinDepth() >= tProgramProblem.getMinTreeDepth();
    }

    @Override
    public ProgramSolution execute(ProgramSolution tProgramSolution, ProgramProblem tProgramProblem) {
        this.minTreeDepth = tProgramProblem.getMinTreeDepth();
        this.programProblem = tProgramProblem;
        expandProgramDepth(tProgramSolution.getTree().getRootNode(), null, 1);
        return tProgramSolution;
    }

    private void expandProgramDepth(Node current, Node parent, int currentDepth){
        if(currentDepth < this.minTreeDepth){
            // Create new solution that will exceed minTreeDepth
            if(parent != null){
                ProgramSolution newSolution = this.programProblem.getProgramSolutionGenerator().generate(this.programProblem,  currentDepth, "");
                parent.replace(current, newSolution.getTree().getRootNode());
            }
            else if (current.getArity() > 0 && current.getChildren().isEmpty()){
                ProgramSolution newSolution = this.programProblem.getProgramSolutionGenerator().generate(this.programProblem,  currentDepth + 1, "");
                current.getChildren().add(newSolution.getTree().getRootNode());
            }

            if(current.getArity() > 0){
                for (Node child : current.getChildren()) {
                    expandProgramDepth(child, current, currentDepth + 1);
                }
            }
        }
    }

}
