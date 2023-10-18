package org.um.feri.ears.operators;

import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.TreeAncestor;
import org.um.feri.ears.problems.gp.TreeNode;
import org.um.feri.ears.util.random.RNG;

import java.util.Optional;

public class SingleTreeNodeMutation<T> implements MutationOperator<ProgramProblem<T>, ProgramSolution<T>> {

    private Double mutationProbability = null;

    public SingleTreeNodeMutation(Double mutationProbability){
        this.mutationProbability = mutationProbability;
    }

    @Override
    public void setProbability(double mutationProbability) {
        this.mutationProbability = 1.0 / mutationProbability;
    }

    private void doMutation(double probability, ProgramSolution<T> solution, ProgramProblem<T> problem) {
        if (RNG.nextDouble() <= probability) {
            //System.out.println("Tree is in order to be mutated");
            int childCount = solution.getProgram().ancestors().getAncestorCount();
            int crossPoint = RNG.nextInt(childCount) + 1;

            TreeAncestor crossOverNodeAncestor = solution.getProgram().ancestorAt(crossPoint);
            TreeNode<T> crossOverNode = crossOverNodeAncestor.getTreeNode();

            Optional<TreeNode<T>> mutationNodeParent = crossOverNode.parent();

            /*if (mutationNodeParent.isEmpty()) {
                throw new Exception("Mutation node doesn't contain a parent");
            }*/

            int crossOverNodeIndex = mutationNodeParent.get().indexOf(crossOverNode);

            mutationNodeParent.get().replace(crossOverNodeIndex, problem.getRandomSolution(false, crossOverNodeAncestor.getTreeHeightPosition()).getProgram());
        }
    }

    @Override
    public ProgramSolution<T> execute(ProgramSolution<T> tProgramSolution, ProgramProblem<T> problem) {
        ProgramSolution<T> mutatedSolution = new ProgramSolution<>(tProgramSolution);
        doMutation(mutationProbability, mutatedSolution, problem);
        return mutatedSolution;
    }
}
