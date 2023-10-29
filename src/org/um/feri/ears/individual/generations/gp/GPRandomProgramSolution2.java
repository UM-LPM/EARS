package org.um.feri.ears.individual.generations.gp;

import org.um.feri.ears.individual.btdemo.gp.Node;
import org.um.feri.ears.individual.btdemo.gp.behaviour.BehaviourTree;
import org.um.feri.ears.individual.btdemo.gp.behaviour.SymbolicRegressionTree;
import org.um.feri.ears.individual.btdemo.gp.behaviour.Tree;
import org.um.feri.ears.individual.representations.gp.Op;
import org.um.feri.ears.individual.representations.gp.OperationType;
import org.um.feri.ears.individual.representations.gp.TreeNode;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramProblem2;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.ProgramSolution2;
import org.um.feri.ears.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class GPRandomProgramSolution2 extends GPProgramSolution2 {

    private ProgramProblem2 programProblem;

    public ProgramSolution2 generate(ProgramProblem2 programProblem, int startHeight, String treeName) {
        this.programProblem = programProblem;
        return getRandomSolution(startHeight, treeName);
    }

    public ProgramSolution2 getRandomSolution(int startHeight, String treeName) {
        ProgramSolution2 newSolution = new ProgramSolution2(this.programProblem.getNumberOfObjectives());
        Tree tree;
        if(programProblem.getSolutionTreeType() == Tree.TreeType.SYMBOLIC)
            tree = new SymbolicRegressionTree(treeName);
        else
            tree = new BehaviourTree(treeName);

        tree.setRootNode(generateRandomTree(startHeight));
        newSolution.setTree(tree);
        return newSolution;
    }

    public Node generateRandomTree(int height) {
        if(programProblem.getBaseFunctionNodeTypes().isEmpty() || programProblem.getBaseTerminalNodeTypes().isEmpty()) {
            throw new RuntimeException("Cannot generate a tree with no node types");
        }
        if (programProblem.getMaxTreeHeight() == height || (height >= programProblem.getMinTreeHeight() && Util.rnd.nextBoolean())) {
            // Base case: return a terminal node
            return generateRandomNode(programProblem.getBaseTerminalNodeTypes());
        } else {
            // Recursive case: return a function node
            Node node = generateRandomNode(programProblem.getBaseFunctionNodeTypes());
            int numOfChildren = node.getFixedNumOfChildren()? node.getArity() : Util.rnd.nextInt(1, node.getArity() + 1);
            for (int i = 0; i < numOfChildren; i++) {
                node.insert(i, generateRandomTree(height + 1)); // TODO check!!!
            }
            return node;
        }
    }

    private Node generateRandomNode(List<Class<? extends Node>> nodeTypes) {
        int index = Util.rnd.nextInt(nodeTypes.size());
        try {
            // Create a new instance from base constructor of the randomly chosen Node type
            return nodeTypes.get(index).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
