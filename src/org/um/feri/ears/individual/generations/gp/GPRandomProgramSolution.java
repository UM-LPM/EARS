package org.um.feri.ears.individual.generations.gp;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.BehaviourTree;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.GoalNode;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.GoalNodeDefinition;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.SymbolicRegressionTree;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.util.Configuration;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class GPRandomProgramSolution extends GPProgramSolution {

    public static final long serialVersionUID = 7095521811400218978L;

    private ProgramProblem programProblem;

    private List<GoalNodeDefinition> goalNodeDefinitions;

    public GPRandomProgramSolution() {
        super();
        this.goalNodeDefinitions = new ArrayList<>();
    }

    public GPRandomProgramSolution(List<GoalNodeDefinition> goalNodeDefinitions){
        this.goalNodeDefinitions = goalNodeDefinitions;
    }

    public ProgramSolution generate(ProgramProblem programProblem, int startDepth, String treeName) {
        this.programProblem = programProblem;
        return generateSolution(startDepth, treeName);
    }

    public Node generateRandomTerminalNode(ProgramProblem programProblem) {
        return generateRandomNode(programProblem.getBaseTerminalNodeTypes());
    }

    public Configuration.InitPopGeneratorMethod getInitPopGeneratorMethod() {
        return Configuration.InitPopGeneratorMethod.Random;
    }

    public ProgramSolution generateSolution(int startDepth, String treeName) {
        ProgramSolution newSolution = new ProgramSolution(this.programProblem.getNumberOfObjectives());
        Tree tree;
        if(programProblem.getSolutionTreeType() == Tree.TreeType.SYMBOLIC)
            tree = new SymbolicRegressionTree(treeName);
        else
            tree = new BehaviourTree(treeName);

        tree.setRootNode(generateRandomTree(startDepth, startDepth));
        newSolution.setTree(tree);

        return newSolution;
    }

    public Node generateRandomTree(int startDepth, int depth) {
        // TODO Test this !!!!
        if(programProblem.getBaseFunctionNodeTypes().isEmpty() || programProblem.getBaseTerminalNodeTypes().isEmpty()) {
            throw new RuntimeException("Cannot generate a tree with no node types");
        }

        if((startDepth == 1? programProblem.getMaxTreeStartDepth() :  programProblem.getMaxTreeEndDepth()) == depth){
            return generateRandomNode(programProblem.getBaseTerminalNodeTypes());
        }
        else {
            Node newNode;
            if(depth >= programProblem.getMinTreeDepth())
                newNode = generateRandomNode(programProblem.getRandomNodeType());
            else
                newNode = generateRandomNode(programProblem.getBaseFunctionNodeTypes());

            // If the node is a function node, generate its children
            if(newNode.getArity() > 0){
                //int numOfChildren = newNode.getFixedNumOfChildren()? newNode.getArity() : (RNG.nextInt((newNode.getArity() - 1) + 1) + 1);
                int numOfChildren = newNode.getArity(); // TODO Use this instead ???
                for (int i = 0; i < numOfChildren; i++) {
                    newNode.insert(i, generateRandomTree(startDepth, depth + 1));
                }
            }

            return newNode;
        }

        /*if(programProblem.getBaseFunctionNodeTypes().isEmpty() || programProblem.getBaseTerminalNodeTypes().isEmpty()) {
            throw new RuntimeException("Cannot generate a tree with no node types");
        }
        if (programProblem.getMaxTreeDepth() == depth || (depth >= programProblem.getMinTreeDepth() && RNG.nextBoolean())) {
            // Base case: return a terminal node
            return generateRandomNode(programProblem.getBaseTerminalNodeTypes());
        } else {
            // Recursive case: return a function node
            Node node = generateRandomNode(programProblem.getBaseFunctionNodeTypes());
            int numOfChildren = node.getFixedNumOfChildren()? node.getArity() : (RNG.nextInt((node.getArity() - 1) + 1) + 1);
            for (int i = 0; i < numOfChildren; i++) {
                node.insert(i, generateRandomTree(depth + 1)); // TODO check!!!
            }
            return node;
        }*/
    }

    private Node generateRandomNode(List<Class<? extends Node>> nodeTypes) {
        int index = RNG.nextInt(nodeTypes.size());
        try {
            // Create a new instance from base constructor of the randomly chosen Node type
            return assignGoalToGoalNode(nodeTypes.get(index).getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Node generateRandomNode(Class<? extends Node> nodeType) {
        try {
            // Create a new instance from base constructor of the randomly chosen Node type
            return assignGoalToGoalNode(nodeType.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Assigns a random GoalNodeDefinition to a GoalNode (If passed Node is not a GoalNode, it will be returned as is)
     * @param node Node to assign GoalNodeDefinition to
     * @return Modified Node
     */
    private Node assignGoalToGoalNode(Node node) {
        if(node instanceof GoalNode goalNode){
            if(goalNodeDefinitions.isEmpty()){
                throw new RuntimeException("GoalNodeDefinitions are empty");
            }
            // Select a random GoalNodeDefinition and set it to GoalNode
            GoalNodeDefinition goalNodeDefinition = goalNodeDefinitions.get(RNG.nextInt(goalNodeDefinitions.size()));
            goalNode.setGoal(goalNodeDefinition.getGoalName(), goalNodeDefinition.getGoalNodeBehaviour().clone());
        }
        return node;
    }

    public void setGoalNodeDefinitions(List<GoalNodeDefinition> goalNodeDefinitions) {
        this.goalNodeDefinitions = goalNodeDefinitions;
    }

}
