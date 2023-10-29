package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.btdemo.gp.Node;
import org.um.feri.ears.individual.representations.gp.TreeAncestor;
import org.um.feri.ears.individual.representations.gp.TreeAncestor2;
import org.um.feri.ears.individual.representations.gp.TreeNode;
import org.um.feri.ears.problems.gp.ProgramProblem2;
import org.um.feri.ears.problems.gp.ProgramSolution2;
import org.um.feri.ears.util.Util;

import java.util.Optional;

public class SubtreeMutation2 extends GPMutation2 {

    private Double mutationProbability = 0.5;

    public SubtreeMutation2(Double mutationProbability){
        this.mutationProbability = mutationProbability;
    }

    @Override
    public void setProbability(double mutationProbability) {
        this.mutationProbability = 1.0 / mutationProbability;
    }

    private void doMutation(ProgramSolution2 solution, ProgramProblem2 problem) {
        int childCount = solution.getTree().getRootNode().ancestors().getAncestorCount();
        int crossPoint = Util.rnd.nextInt(childCount + 1);

        // If root node is selected, whole program is randomly generated
        if (crossPoint == 0) {
            solution.getTree().setRootNode(problem.getRandomSolution().getTree().getRootNode());
            return;
        }

        TreeAncestor2 crossOverNodeAncestor = solution.getTree().getRootNode().ancestorAt(crossPoint);
        Node crossOverNode = crossOverNodeAncestor.getTreeNode();

        Optional<Node> mutationNodeParent = crossOverNode.parent();

        int crossOverNodeIndex = mutationNodeParent.get().indexOf(crossOverNode);

        mutationNodeParent.get().replace(crossOverNodeIndex, problem.getProgramSolutionGenerator().generate(problem, crossOverNodeAncestor.getTreeHeightPosition(), "").getTree().getRootNode());

    }

    @Override
    public ProgramSolution2 execute(ProgramSolution2 tProgramSolution, ProgramProblem2 problem) {
        if(Util.rnd.nextDouble() <= this.mutationProbability) {
            ProgramSolution2 mutatedSolution = tProgramSolution.copy();
            doMutation(mutatedSolution, problem);
            return mutatedSolution;
        }
        else {
            return tProgramSolution;
        }
    }
}
