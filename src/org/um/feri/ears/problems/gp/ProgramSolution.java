package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.representations.gp.FinalIndividualFitness;
import org.um.feri.ears.individual.representations.gp.IndividualMatchResult;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.*;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.*;
import org.um.feri.ears.problems.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramSolution extends Solution {

    protected Tree tree;

    protected List<ProgramSolution> parents;

    protected FinalIndividualFitness Fitness;

    protected int[] NodeCallFrequencyCount; // TODO Remove this when moved to FinalIndividualFitness

    protected boolean isDirty; // Flag to indicate if the solution has been modified (GPOperators, etc.)

    protected int changesCount; // Number of changes made to the solution

    public ProgramSolution(int numberOfObjectives) {
        super(numberOfObjectives);
        this.Fitness = new FinalIndividualFitness();
        this.NodeCallFrequencyCount = new int[]{};
        this.isDirty = false;
        this.changesCount = 0;
    }

    public ProgramSolution(ProgramSolution s) {
        super(s);
        tree = s.tree.clone();
        parents = new ArrayList<>();
        Fitness = new FinalIndividualFitness(s.Fitness);
        NodeCallFrequencyCount = s.NodeCallFrequencyCount.clone(); // TODO Remove this when moved to FinalIndividualFitness
        isDirty = s.isDirty;
        changesCount = s.changesCount;
    }

    @Override
    public ProgramSolution copy() {
        return new ProgramSolution(this);
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Tree getTree() {
        return tree;
    }

    public FinalIndividualFitness getFitness(){
        return Fitness;
    @Override
    public String toString() {
        //TODO add treeType to tree and add check
        return buildEquation(tree.getRootNode());
    }

    private String buildEquation(Node node) {
        if (node instanceof ConstNode) {
            return String.valueOf(node.evaluate(null));
        } else if (node instanceof VarNode) {
            return node.getName();
        } else if (node instanceof OperatorNode) {
            String operator = "";
            List<Node> children = node.getChildren();

            switch (node.getArity()) {
                case 1:
                    if (node instanceof SqrtNode) {
                        return "sqrt(" + buildEquation(children.get(0)) + ")";
                    } else if (node instanceof CosNode) {
                        return "cos(" + buildEquation(children.get(0)) + ")";
                    } else if (node instanceof SinNode) {
                        return "sin(" + buildEquation(children.get(0)) + ")";
                    } else if (node instanceof TanNode) {
                        return "tan(" + buildEquation(children.get(0)) + ")";
                    } else if (node instanceof TanhNode) {
                        return "tanh(" + buildEquation(children.get(0)) + ")";
                    } else if (node instanceof LogNode) {
                        return "log(" + buildEquation(children.get(0)) + ")";
                    } else if (node instanceof Log10Node) {
                        return "log10(" + buildEquation(children.get(0)) + ")";
                    } else if (node instanceof AbsNode) {
                        return "abs(" + buildEquation(children.get(0)) + ")";
                    }
                    break;
                case 2:
                    if (node instanceof AddNode) {
                        operator = "+";
                    } else if (node instanceof SubNode) {
                        operator = "-";
                    } else if (node instanceof MulNode) {
                        operator = "*";
                    } else if (node instanceof DivNode) {
                        operator = "/";
                    } else if (node instanceof PowNode) {
                        operator = "^";
                    } else if (node instanceof ModNode) {
                        operator = "%";
                    }
                    String left = buildEquation(children.get(0));
                    String right = buildEquation(children.get(1));
                    return "(" + left + " " + operator + " " + right + ")";
            }
        }
        return "";
    }

    public void printTree() {
        printTree(tree.getRootNode(), "");
    }

    public void printTree(Node node, String indent) {
        if (node instanceof ConstNode) {
            System.out.println(indent + "ConstNode(" + ((ConstNode) node).evaluate(null) + ")");
        } else if (node instanceof VarNode) {
            System.out.println(indent + "VarNode(" + ((VarNode) node).getName() + ")");
        } else if (node instanceof OperatorNode) {
            if (node instanceof AddNode) {
                System.out.println(indent + "AddNode");
            } else if (node instanceof SubNode) {
                System.out.println(indent + "SubNode");
            } else if (node instanceof MulNode) {
                System.out.println(indent + "MulNode");
            } else if (node instanceof DivNode) {
                System.out.println(indent + "DivNode");
            }
            for (Node child : ((OperatorNode) node).getChildren()) {
                printTree((Node) child, indent + "  ");
            }
        } else if (node instanceof RootNode) {
            System.out.println(indent + "RootNode");
            for (Node child : ((RootNode) node).getChildren()) {
                printTree((Node) child, indent + "  ");
            }
        } else if (node instanceof ActionNode) {
            System.out.println(indent + "ActionNode(" + ((BehaviourTreeNode) node).getNodeTypeString() + ")");
        } else if (node instanceof ConditionNode) {
            System.out.println(indent + "ConditionNode(" + ((BehaviourTreeNode) node).getNodeTypeString() + ")");
        } else if (node instanceof CompositeNode) {
            System.out.println(indent + "CompositeNode(" + ((BehaviourTreeNode) node).getNodeTypeString() + ")");
            for (Node child : ((CompositeNode) node).getChildren()) {
                printTree((Node) child, indent + "  ");
            }
        } else if (node instanceof DecoratorNode) {
            System.out.println(indent + "DecoratorNode(" + ((BehaviourTreeNode) node).getNodeTypeString() + ")");
            for (Node child : ((DecoratorNode) node).getChildren()) {
                printTree((Node) child, indent + "  ");
            }
        }
    }

    public HashMap<String, Fitness> getFitnesses(){
        return Fitnesses;
    }

    public void setFitness(FinalIndividualFitness fitness){
        this.Fitness = fitness;
    }

    public Map<String, Double> getFitnessesCombined(){
        HashMap<String, Double> fitnessesCombined = new HashMap<>();

        for(IndividualMatchResult individualMatchResult : Fitness.getIndividualMatchResults()){
            for(Map.Entry<String, Double> entry : individualMatchResult.individualValues.entrySet()){
                if(fitnessesCombined.containsKey(entry.getKey())){
                    fitnessesCombined.put(entry.getKey(), fitnessesCombined.get(entry.getKey()) + entry.getValue());
                }
                else {
                    fitnessesCombined.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return fitnessesCombined;
    }

    // TODO Remove this (when moved to FinalIndividualFitness)
    public void setNodeCallFrequencyCount(int[] nodeCallFrequencyCount){
        this.NodeCallFrequencyCount = nodeCallFrequencyCount;
    }

    // TODO Remove this (when moved to FinalIndividualFitness)
    public int[] getNodeCallFrequencyCount(){
        return NodeCallFrequencyCount;
    }

    public void resetIsDirty(){
        isDirty = false;
    }

    public void setIsDirty(boolean isDirty){
        this.isDirty = isDirty;
    }

    public boolean isDirty(){
        return isDirty;
    }

    public void increaseChangesCount(){
        changesCount++;
    }

    public int getChangesCount(){
        return changesCount;
    }
}