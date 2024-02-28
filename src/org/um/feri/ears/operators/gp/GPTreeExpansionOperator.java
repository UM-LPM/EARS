package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

public class GPTreeExpansionOperator extends GPOperator {

    private int minTreeDepth;
    private ProgramProblem programProblem;

    @Override
    public ProgramSolution execute(ProgramSolution tProgramSolution, ProgramProblem tProgramProblem) {
        this.minTreeDepth = tProgramProblem.getMinTreeDepth();
        this.programProblem = tProgramProblem;
        expandProgramHeight(tProgramSolution.getTree().getRootNode(), null, 1);
        return tProgramSolution;
    }

    private void expandProgramHeight(Node current, Node parent, int currentHeight){
        if(currentHeight < this.minTreeDepth){
            // Create new solution that will exceed minTreeHeight
            ProgramSolution newSolution = this.programProblem.getProgramSolutionGenerator().generate(this.programProblem,  currentHeight, "");
            if(parent != null){
                parent.replace(current, newSolution.getTree().getRootNode());
            }
            for (Node child : current.getChildren()) {
                expandProgramHeight(child, current, currentHeight + 1);
            }
        }
    }

}
