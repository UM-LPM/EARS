package org.um.feri.ears.operators.gp;

import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.TreeAncestor;
import org.um.feri.ears.problems.gp.TreeNode;
import org.um.feri.ears.util.Util;

import java.util.Optional;

public class SinglePointCrossover<T> extends GPCrossover<T> {

    private Double crossoverProbability = 0.9;

    public SinglePointCrossover(Double crossoverProbability){
        this.crossoverProbability = crossoverProbability;
    }

    @Override
    public void setCurrentSolution(ProgramSolution<T> current) {

    }

    @Override
    public ProgramSolution<T>[] execute(ProgramSolution<T>[] programSolutions, ProgramProblem<T> problem) {
        if (programSolutions.length != 2) {
            return null;
        }

        if(Util.rnd.nextDouble() <= this.crossoverProbability) {
            ProgramSolution<T> parent1 = programSolutions[0].copy();
            ProgramSolution<T> parent2 = programSolutions[1].copy();

            // Perform crossover so long that it's inside boundaries
            do {
                // Get lengths of both parents and select random crossover points
                int childCount1 = parent1.getProgram().ancestors().getAncestorCount();
                int childCount2 = parent2.getProgram().ancestors().getAncestorCount();

                int crossoverIndex1 = Util.rnd.nextInt(childCount1) + 1;
                int crossoverIndex2 = Util.rnd.nextInt(childCount2) + 1;

                // Select nodes at random indexes
                TreeAncestor<T> crossOverNodeTreeAncestor1 = parent1.getProgram().ancestorAt(crossoverIndex1);
                TreeAncestor<T> crossOverNodeTreeAncestor2 = parent2.getProgram().ancestorAt(crossoverIndex2);

                TreeNode<T> crossOverNode1 = crossOverNodeTreeAncestor1.getTreeNode();
                TreeNode<T> crossOverNode2 = crossOverNodeTreeAncestor2.getTreeNode();

                Optional<TreeNode<T>> mutationNodeParent1 = crossOverNode1.parent();
                Optional<TreeNode<T>> mutationNodeParent2 = crossOverNode2.parent();

                int crossOverNodeIndex1 = mutationNodeParent1.get().indexOf(crossOverNode1);
                int crossOverNodeIndex2 = mutationNodeParent2.get().indexOf(crossOverNode2);

                //Switch nodes and it's subtrees
                mutationNodeParent1.get().replace(crossOverNodeIndex1, crossOverNode2);
                mutationNodeParent2.get().replace(crossOverNodeIndex2, crossOverNode1);
            } while (!problem.isFeasible(parent1) || !problem.isFeasible(parent2));
        }
        return programSolutions;
    }
}
