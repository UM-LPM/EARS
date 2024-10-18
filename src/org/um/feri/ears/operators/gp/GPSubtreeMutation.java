package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.TreeAncestor;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.random.RNG;

import java.util.Optional;

public class GPSubtreeMutation extends GPMutation {

    private Double mutationProbability = 0.5;

    public GPSubtreeMutation(Double mutationProbability){
        this.mutationProbability = mutationProbability;
    }

    @Override
    public void setProbability(double mutationProbability) {
        this.mutationProbability = 1.0 / mutationProbability;
    }

    private void doMutation(ProgramSolution solution, ProgramProblem problem) {
        int childCount = solution.getTree().getRootNode().ancestors().getAncestorCount();
        int crossPoint = RNG.nextInt(childCount + 1);

        // If root node is selected, whole program is randomly generated
        if (crossPoint == 0) {
            solution.getTree().setRootNode(problem.getRandomSolution().getTree().getRootNode());
            return;
        }

        TreeAncestor crossOverNodeAncestor = solution.getTree().getRootNode().ancestorAt(crossPoint);
        Node crossOverNode = crossOverNodeAncestor.getTreeNode();

        Optional<Node> mutationNodeParent = crossOverNode.parent();

        int crossOverNodeIndex = mutationNodeParent.get().indexOf(crossOverNode);

        mutationNodeParent.get().replace(crossOverNodeIndex, problem.getProgramSolutionGenerator().generate(problem, crossOverNodeAncestor.getTreeDepthPosition(), "").getTree().getRootNode());

    }

    @Override
    public ProgramSolution execute(ProgramSolution tProgramSolution, ProgramProblem problem) {
        if(RNG.nextDouble() <= this.mutationProbability) {
            ProgramSolution mutatedSolution = tProgramSolution.copy();
            doMutation(mutatedSolution, problem);

            // Check if new programs are feasible. If not, make them feasible (makeFeasible does all that)
            problem.makeFeasible(mutatedSolution);

            return mutatedSolution;
        }
        else {
            return tProgramSolution;
        }
    }
}
