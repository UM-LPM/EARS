package org.um.feri.ears.individual.generations.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTree;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.SymbolicRegressionTree;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class GPRandomProgramSolution extends GPProgramSolution {

    private ProgramProblem programProblem;

    public ProgramSolution generate(ProgramProblem programProblem, int startDepth, String treeName) {
        this.programProblem = programProblem;
        return getRandomSolution(startDepth, treeName);
    }

    public Node generateRandomTerminalNode(ProgramProblem programProblem) {
        return generateRandomNode(programProblem.getBaseTerminalNodeTypes());
    }

    public ProgramSolution getRandomSolution(int startDepth, String treeName) {
        ProgramSolution newSolution = new ProgramSolution(this.programProblem.getNumberOfObjectives());
        Tree tree;
        if(programProblem.getSolutionTreeType() == Tree.TreeType.SYMBOLIC)
            tree = new SymbolicRegressionTree(treeName);
        else
            tree = new BehaviourTree(treeName);

        tree.setRootNode(generateRandomTree(startDepth));
        newSolution.setTree(tree);

        // TODO Remove when add support for treeSize check in generator
        if(!programProblem.isFeasible(newSolution))
            programProblem.makeFeasible(newSolution);

        return newSolution;
    }

    public Node generateRandomTree(int depth) {
        if(programProblem.getBaseFunctionNodeTypes().isEmpty() || programProblem.getBaseTerminalNodeTypes().isEmpty()) {
            throw new RuntimeException("Cannot generate a tree with no node types");
        }
        if (programProblem.getMaxTreeDepth() == depth || (depth >= programProblem.getMinTreeDepth() && Util.rnd.nextBoolean())) {
            // Base case: return a terminal node
            return generateRandomNode(programProblem.getBaseTerminalNodeTypes());
        } else {
            // Recursive case: return a function node
            Node node = generateRandomNode(programProblem.getBaseFunctionNodeTypes());
            int numOfChildren = node.getFixedNumOfChildren()? node.getArity() : (Util.rnd.nextInt((node.getArity() - 1) + 1) + 1);
            for (int i = 0; i < numOfChildren; i++) {
                node.insert(i, generateRandomTree(depth + 1)); // TODO check!!!
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