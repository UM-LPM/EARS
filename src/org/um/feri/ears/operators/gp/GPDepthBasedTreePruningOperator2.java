package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.problems.gp.ProgramProblem2;
import org.um.feri.ears.problems.gp.ProgramSolution2;

public class GPDepthBasedTreePruningOperator2 extends GPOperator2 {

    private int maxTreeHeight;
    private ProgramProblem2 programProblem;

    @Override
    public ProgramSolution2 execute(ProgramSolution2 tProgramSolution, ProgramProblem2 tProgramProblem) {
        this.maxTreeHeight = tProgramProblem.getMaxTreeHeight();
        this.programProblem = tProgramProblem;
        pruneProgramHeight(tProgramSolution.getTree().getRootNode(), null, 1);
        return tProgramSolution;
    }

    private void pruneProgramHeight(Node current, Node parent, int currentHeight){
        if(currentHeight >= this.maxTreeHeight){
            if(current.getArity() > 0){
                ProgramSolution2 newSolution = programProblem.getProgramSolutionGenerator().generate(this.programProblem, currentHeight, "");
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
                pruneProgramHeight(child, current, currentHeight + 1);
            }
        }
    }
}
