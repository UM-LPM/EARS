package org.um.feri.ears.individual.generations.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTree;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.SymbolicRegressionTree;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.Configuration;
import org.um.feri.ears.util.random.RNG;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class GPRampedHalfAndHalf extends GPProgramSolution {

    private ProgramProblem programProblem;

    public ProgramSolution generate(ProgramProblem programProblem, int startDepth, String treeName) {
        this.programProblem = programProblem;
        return generateSolution(startDepth, treeName);
    }


    public Node generateRandomTerminalNode(ProgramProblem programProblem) {
        return generateRandomNode(programProblem.getBaseTerminalNodeTypes());
    }

    public Configuration.InitPopGeneratorMethod getInitPopGeneratorMethod() {
        return Configuration.InitPopGeneratorMethod.RampedHalfAndHalfMethod;
    }

    public ProgramSolution generateSolution(int startDepth, String treeName) {
        ProgramSolution newSolution = new ProgramSolution(this.programProblem.getNumberOfObjectives());
        Tree tree;
        if(programProblem.getSolutionTreeType() == Tree.TreeType.SYMBOLIC)
            tree = new SymbolicRegressionTree(treeName);
        else
            tree = new BehaviourTree(treeName);

        // Pick random value withing the range of startDepth and maxTreeDepth
        int maxTreeDepth = startDepth == 1? programProblem.getMaxTreeStartDepth() : programProblem.getMaxTreeEndDepth();
        int finalDepth;
        if(startDepth < programProblem.getMinTreeDepth())
            finalDepth = RNG.nextInt(maxTreeDepth - programProblem.getMinTreeDepth() + 1) + programProblem.getMinTreeDepth();
        else
            finalDepth = RNG.nextInt(maxTreeDepth - startDepth + 1) + startDepth;

        RampedType rampedType = RNG.nextBoolean() ? RampedType.FULL : RampedType.GROWTH;

        if(rampedType == RampedType.FULL)
            tree.setRootNode(generateRampedTreeFull(startDepth, finalDepth));
        else
            tree.setRootNode(generateRampedTreeGrow(startDepth, finalDepth));

        newSolution.setTree(tree);

        return newSolution;
    }

    public Node generateRampedTreeFull(int depth, int finalDepth) {
        if(programProblem.getBaseFunctionNodeTypes().isEmpty() || programProblem.getBaseTerminalNodeTypes().isEmpty()) {
            throw new RuntimeException("Cannot generate a tree with no node types");
        }

        if(finalDepth == depth){
            return generateRandomNode(programProblem.getBaseTerminalNodeTypes());
        }
        else{
            // Get function node type
            Node node = generateRandomNode(programProblem.getBaseFunctionNodeTypes());

            int numOfChildren = node.getArity();
            for (int i = 0; i < numOfChildren; i++) {
                node.insert(i, generateRampedTreeFull(depth + 1, finalDepth));
            }
            return node;
        }
    }

    public Node generateRampedTreeGrow(int depth, int finalDepth) {
        if(programProblem.getBaseFunctionNodeTypes().isEmpty() || programProblem.getBaseTerminalNodeTypes().isEmpty()) {
            throw new RuntimeException("Cannot generate a tree with no node types");
        }

        if(finalDepth == depth){
            return generateRandomNode(programProblem.getBaseTerminalNodeTypes());
        }
        else{
            Node newNode;
            if(depth >= programProblem.getMinTreeDepth())
                newNode = generateRandomNode(programProblem.selectRandomNodeType());
            else
                newNode = generateRandomNode(programProblem.getBaseFunctionNodeTypes());

            // If the node is a function node, generate its children
            if(newNode.getArity() > 0){
                //int numOfChildren = newNode.getFixedNumOfChildren()? newNode.getArity() : (RNG.nextInt((newNode.getArity() - 1) + 1) + 1);
                int numOfChildren = newNode.getArity(); // TODO Use this instead ???
                for (int i = 0; i < numOfChildren; i++) {
                    newNode.insert(i, generateRampedTreeGrow(depth + 1, finalDepth));
                }
            }

            return newNode;
        }
    }

    private Node generateRandomNode(List<Class<? extends Node>> nodeTypes) {
        int index = RNG.nextInt(nodeTypes.size());
        try {
            // Create a new instance from base constructor of the randomly chosen Node type
            return nodeTypes.get(index).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Node generateRandomNode(Class<? extends Node> nodeType) {
        try {
            // Create a new instance from base constructor of the randomly chosen Node type
            //return nodeType.getDeclaredConstructor().newInstance();
            return assignEncapsulatedNodeToEncapsulatedNode(nodeType.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public enum RampedType {
        FULL,
        GROWTH
    }
}
