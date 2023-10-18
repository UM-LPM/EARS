package org.um.feri.ears.operators;

import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.TreeAncestor;
import org.um.feri.ears.problems.gp.TreeNode;
import org.um.feri.ears.util.random.RNG;

import java.util.Optional;

public class SinglePointCrossover<T extends Number> implements CrossoverOperator<ProgramProblem<T>, ProgramSolution<T>> {

    private Double crossoverProbability = 0.9;
    private ProgramSolution<T> currentSolution;

    public SinglePointCrossover(Double crossoverProbability){
        this.crossoverProbability = crossoverProbability;
    }

    @Override
    public void setCurrentSolution(ProgramSolution<T> current) {
        this.currentSolution = current ;
    }

    @Override
    public ProgramSolution<T>[] execute(ProgramSolution<T>[] programSolutions, ProgramProblem<T> problem) {
        if (programSolutions.length != 2) {
            return null;
        }

        ProgramSolution<T> parent1 = new ProgramSolution<>(programSolutions[0]);
        ProgramSolution<T> parent2 = new ProgramSolution<>(programSolutions[1]);

        if(RNG.nextDouble() <= this.crossoverProbability) {
            // Get lengths of both parents and select random crossover points
            int childCount1 = parent1.getProgram().ancestors().getAncestorCount();
            int childCount2 = parent2.getProgram().ancestors().getAncestorCount();

            //TODO check if indices not the same?
            int crossoverIndex1 = RNG.nextInt(childCount1) + 1;
            int crossoverIndex2 = RNG.nextInt(childCount2) + 1;

            //System.out.println("crossoverIndex1: " + crossoverIndex1);
            //System.out.println("crossoverIndex2: " + crossoverIndex2);

            // Select nodes at random indexes
            TreeAncestor<T> crossOverNodeTreeAncestor1 = parent1.getProgram().ancestorAt(crossoverIndex1);
            TreeAncestor<T> crossOverNodeTreeAncestor2 = parent2.getProgram().ancestorAt(crossoverIndex2);

            TreeNode<T> crossOverNode1 = crossOverNodeTreeAncestor1.getTreeNode();
            TreeNode<T> crossOverNode2 = crossOverNodeTreeAncestor2.getTreeNode();

            Optional<TreeNode<T>> mutationNodeParent1 = crossOverNode1.parent();
            Optional<TreeNode<T>> mutationNodeParent2 = crossOverNode2.parent();

            /*if (mutationNodeParent1.isEmpty() || mutationNodeParent2.isEmpty()) {
                throw new Exception("Crossover node doesn't contain a parent");
            }*/

            int crossOverNodeIndex1 = mutationNodeParent1.get().indexOf(crossOverNode1);
            int crossOverNodeIndex2 = mutationNodeParent2.get().indexOf(crossOverNode2);

            //Switch nodes and it's subtrees
            mutationNodeParent1.get().replace(crossOverNodeIndex1, crossOverNode2);
            mutationNodeParent2.get().replace(crossOverNodeIndex2, crossOverNode1);

            //TODO upoštevaj velikost drevesa.
            //Check if children are valid (treeHeight). If not valid, abort crossover
            if(problem.getMaxTreeHeight() <= parent1.getProgram().treeHeight()){
                return programSolutions;
            }
        }
        return new ProgramSolution[]{parent1, parent2};
    }
}
