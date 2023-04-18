package org.um.feri.ears.operators.gp;

import org.um.feri.ears.operators.MutationOperator;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.TreeAncestor;
import org.um.feri.ears.problems.gp.TreeNode;
import org.um.feri.ears.util.Util;

import java.util.Optional;

public class SingleTreeNodeMutation<T> extends GPMutation<T> {

    private Double mutationProbability = 0.5;

    public SingleTreeNodeMutation(Double mutationProbability){
        this.mutationProbability = mutationProbability;
    }

    @Override
    public void setProbability(double mutationProbability) {
        this.mutationProbability = 1.0 / mutationProbability;
    }

    private void doMutation(ProgramSolution<T> solution, ProgramProblem<T> problem) {
        int childCount = solution.getProgram().ancestors().getAncestorCount();
        int crossPoint = Util.rnd.nextInt(childCount + 1);

        // If root node is selected, whole program is randomly generated
        if (crossPoint == 0) {
            solution.setProgram(problem.getRandomSolution().getProgram());
            return;
        }

        TreeAncestor crossOverNodeAncestor = solution.getProgram().ancestorAt(crossPoint);
        TreeNode<T> crossOverNode = crossOverNodeAncestor.getTreeNode();

        Optional<TreeNode<T>> mutationNodeParent = crossOverNode.parent();

        int crossOverNodeIndex = mutationNodeParent.get().indexOf(crossOverNode);

        mutationNodeParent.get().replace(crossOverNodeIndex, problem.getRandomSolution(false, crossOverNodeAncestor.getTreeHeightPosition()).getProgram());

    }

    @Override
    public ProgramSolution<T> execute(ProgramSolution<T> tProgramSolution, ProgramProblem<T> problem) {
        if(Util.rnd.nextDouble() <= this.mutationProbability) {
            ProgramSolution<T> mutatedSolution = tProgramSolution.copy();
            doMutation(mutatedSolution, problem);
            return mutatedSolution;
        }
        else {
            return tProgramSolution;
        }
    }
}
