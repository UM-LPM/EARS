package org.um.feri.ears.operators.gp;

import org.um.feri.ears.individual.btdemo.gp.Node;
import org.um.feri.ears.individual.representations.gp.TreeAncestor;
import org.um.feri.ears.individual.representations.gp.TreeAncestor2;
import org.um.feri.ears.individual.representations.gp.TreeNode;
import org.um.feri.ears.problems.gp.ProgramProblem2;
import org.um.feri.ears.problems.gp.ProgramSolution2;
import org.um.feri.ears.util.Util;

import java.util.Optional;

public class SinglePointCrossover2 extends GPCrossover2 {

    private Double crossoverProbability = 0.9;

    public SinglePointCrossover2(Double crossoverProbability){
        this.crossoverProbability = crossoverProbability;
    }

    @Override
    public void setCurrentSolution(ProgramSolution2 current) {

    }

    @Override
    public ProgramSolution2[] execute(ProgramSolution2[] programSolutions, ProgramProblem2 problem) {
        if (programSolutions.length != 2) {
            return null;
        }

        if(Util.rnd.nextDouble() <= this.crossoverProbability) {
            ProgramSolution2 parent1;
            ProgramSolution2 parent2;
            // Perform crossover so long that it's inside boundaries
            //do {
            parent1 = programSolutions[0].copy();
            parent2 = programSolutions[1].copy();

            // Get lengths of both parents and select random crossover points
            int childCount1 = parent1.getTree().getRootNode().ancestors().getAncestorCount();
            int childCount2 = parent2.getTree().getRootNode().ancestors().getAncestorCount();

            int crossoverIndex1 = Util.rnd.nextInt(childCount1) + 1;
            int crossoverIndex2 = Util.rnd.nextInt(childCount2) + 1;

            // Select nodes at random indexes
            TreeAncestor2 crossOverNodeTreeAncestor1 = parent1.getTree().getRootNode().ancestorAt(crossoverIndex1);
            TreeAncestor2 crossOverNodeTreeAncestor2 = parent2.getTree().getRootNode().ancestorAt(crossoverIndex2);

            Node crossOverNode1 = crossOverNodeTreeAncestor1.getTreeNode();
            Node crossOverNode2 = crossOverNodeTreeAncestor2.getTreeNode();

            Optional<Node> mutationNodeParent1 = crossOverNode1.parent();
            Optional<Node> mutationNodeParent2 = crossOverNode2.parent();

            int crossOverNodeIndex1 = mutationNodeParent1.get().indexOf(crossOverNode1);
            int crossOverNodeIndex2 = mutationNodeParent2.get().indexOf(crossOverNode2);

            if (crossOverNode2.parent().isEmpty() || crossOverNode1.parent().isEmpty()) {
                throw new RuntimeException("Error replacing node");
            }
            //Switch nodes and it's subtrees
            mutationNodeParent1.get().replace(crossOverNodeIndex1, crossOverNode2);
            mutationNodeParent2.get().replace(crossOverNodeIndex2, crossOverNode1);

            // Check if new programs are feasible. If not, make them feasible
            if (!problem.isFeasible(parent1)) {
                problem.makeFeasible(parent1);
            }
            if (!problem.isFeasible(parent2)) {
                problem.makeFeasible(parent2);
            }
            //} while (!problem.isFeasible(parent1) || !problem.isFeasible(parent2));

            return new ProgramSolution2[]{parent1, parent2};
        }
        return programSolutions;
    }
}
