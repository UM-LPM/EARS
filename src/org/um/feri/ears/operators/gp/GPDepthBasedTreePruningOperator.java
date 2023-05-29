package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.representations.gp.TreeNode;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;

public class GPDepthBasedTreePruningOperator<T> extends GPOperator<T> {

    private int maxTreeHeight;
    private ProgramProblem<T> programProblem;

    @Override
    public ProgramSolution<T> execute(ProgramSolution<T> tProgramSolution, ProgramProblem<T> tProgramProblem) {
        this.maxTreeHeight = tProgramProblem.getMaxTreeHeight();
        this.programProblem = tProgramProblem;
        pruneProgramHeight(tProgramSolution.getProgram(), null, 1);
        return tProgramSolution;
    }

    private void pruneProgramHeight(TreeNode<T> current, TreeNode<T> parent, int currentHeight){
        if(currentHeight >= this.maxTreeHeight){
            if(current.operation().isComplex()){
                ProgramSolution<T> newSolution = programProblem.getProgramSolutionGenerator().generate(this.programProblem, false, currentHeight);
                if(parent != null){
                    try {
                        parent.replace(current, newSolution.getProgram());
                    } catch (Exception e) {
                        throw new RuntimeException("Error replacing node");
                    }
                }

            }
        }else{
            for (TreeNode<T> child : current.getChildren()) {
                pruneProgramHeight(child, current, currentHeight + 1);
            }
        }
    }
}
