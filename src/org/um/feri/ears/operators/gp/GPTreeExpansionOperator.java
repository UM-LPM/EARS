package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.representations.gp.TreeNode;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

public class GPTreeExpansionOperator<T>  extends GPOperator<T>  {

    private int minTreeHeight;
    private ProgramProblem<T> programProblem;

    @Override
    public ProgramSolution<T> execute(ProgramSolution<T> tProgramSolution, ProgramProblem<T> tProgramProblem) {
        this.minTreeHeight = tProgramProblem.getMinTreeHeight();
        this.programProblem = tProgramProblem;
        expandProgramHeight(tProgramSolution.getProgram(), null, 1);
        return tProgramSolution;
    }

    private void expandProgramHeight(TreeNode<T> current, TreeNode<T> parent, int currentHeight){
        if(currentHeight < this.minTreeHeight){
            if(!current.getOperation().isComplex()){
                ProgramSolution<T> newSolution = this.programProblem.getProgramSolutionGenerator().generate(this.programProblem, false, currentHeight);
                if(parent != null){
                    parent.replace(current, newSolution.getProgram());
                }
            }else {
                for (TreeNode<T> child : current.getChildren()) {
                    expandProgramHeight(child, current, currentHeight + 1);
                }
            }
        }
    }

}
