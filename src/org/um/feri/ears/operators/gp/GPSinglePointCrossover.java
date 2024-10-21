package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.TreeAncestor;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.random.RNG;

import java.util.Optional;

public class GPSinglePointCrossover extends GPCrossover {

    private Double crossoverProbability = 0.9;

    public GPSinglePointCrossover(Double crossoverProbability){
        this.crossoverProbability = crossoverProbability;
    }

    @Override
    public void setCurrentSolution(ProgramSolution current) {

    }

    @Override
    public ProgramSolution[] execute(ProgramSolution[] programSolutions, ProgramProblem problem) {
        if (programSolutions.length != 2) {
            return null;
        }

        ProgramSolution parent1 = programSolutions[0];
        ProgramSolution parent2 = programSolutions[1];

        if(RNG.nextDouble() <= this.crossoverProbability) {
            //System.out.println("Performing crossover");

            // Get lengths of both parents and select random crossover points
            int childCount1 = parent1.getTree().getRootNode().ancestors().getAncestorCount();
            int childCount2 = parent2.getTree().getRootNode().ancestors().getAncestorCount();

            int crossoverIndex1 = RNG.nextInt(childCount1) + 1;
            int crossoverIndex2 = RNG.nextInt(childCount2) + 1;

            //System.out.println("Crossover index 1: " + crossoverIndex1);
            //System.out.println("Crossover index 2: " + crossoverIndex2);

            // Select nodes at random indexes
            TreeAncestor crossOverNodeTreeAncestor1 = parent1.getTree().getRootNode().ancestorAt(crossoverIndex1);
            TreeAncestor crossOverNodeTreeAncestor = parent2.getTree().getRootNode().ancestorAt(crossoverIndex2);

            Node crossOverNode1 = crossOverNodeTreeAncestor1.getTreeNode();
            Node crossOverNode2 = crossOverNodeTreeAncestor.getTreeNode();

            Optional<Node> mutationNodeParent1 = crossOverNode1.parent();
            Optional<Node> mutationNodeParent2 = crossOverNode2.parent();

            if (mutationNodeParent1.isEmpty() || mutationNodeParent2.isEmpty()) {
                throw new RuntimeException("MutationNodeParent1 or mutationNodeParent2 is empty");
            }

            int crossOverNodeIndex1 = mutationNodeParent1.get().indexOf(crossOverNode1);
            int crossOverNodeIndex2 = mutationNodeParent2.get().indexOf(crossOverNode2);

            //Switch nodes and it's subtrees
            mutationNodeParent1.get().replace(crossOverNodeIndex1, crossOverNode2);
            mutationNodeParent2.get().replace(crossOverNodeIndex2, crossOverNode1);

            // Check if new programs are feasible. If not, make them feasible
            problem.makeFeasible(parent1);
            problem.makeFeasible(parent2);

        }
        return new ProgramSolution[]{parent1, parent2};
    }
}
