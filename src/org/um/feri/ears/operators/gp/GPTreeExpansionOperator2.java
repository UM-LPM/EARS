package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.btdemo.gp.Node;
import org.um.feri.ears.individual.representations.gp.TreeNode;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramProblem2;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.ProgramSolution2;

public class GPTreeExpansionOperator2  extends GPOperator2  {

    private int minTreeHeight;
    private ProgramProblem2 programProblem;

    @Override
    public ProgramSolution2 execute(ProgramSolution2 tProgramSolution, ProgramProblem2 tProgramProblem) {
        this.minTreeHeight = tProgramProblem.getMinTreeHeight();
        this.programProblem = tProgramProblem;
        expandProgramHeight(tProgramSolution.getTree().getRootNode(), null, 1);
        return tProgramSolution;
    }

    private void expandProgramHeight(Node current, Node parent, int currentHeight){
        if(currentHeight < this.minTreeHeight){
            // Create new solution that will exceed minTreeHeight
            ProgramSolution2 newSolution = this.programProblem.getProgramSolutionGenerator().generate(this.programProblem,  currentHeight, "");
            if(parent != null){
                parent.replace(current, newSolution.getTree().getRootNode());
            }
            for (Node child : current.getChildren()) {
                expandProgramHeight(child, current, currentHeight + 1);
            }
        }
    }

}
