package org.um.feri.ears.problems.gp;

import org.um.feri.ears.individual.representations.gp.Fitness;
import org.um.feri.ears.individual.representations.gp.Node;
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

    protected HashMap<String, Fitness> Fitnesses; // Detailed fitness values for analysis
    protected double ratingStandardDeviation; // Standard deviation of the rating when rating is used as fitness

    protected int[] NodeCallFrequencyCount;

    protected boolean isDirty; // Flag to indicate if the solution has been modified (GPOperators, etc.)

    public ProgramSolution(int numberOfObjectives) {
        super(numberOfObjectives);
        this.Fitnesses = new HashMap<>();
        this.NodeCallFrequencyCount = new int[]{};
        this.isDirty = false;
    }

    public ProgramSolution(ProgramSolution s) {
        super(s);
        tree = s.tree.clone();
        parents = new ArrayList<>();
        Fitnesses = new HashMap<>();
        Fitnesses.putAll(s.Fitnesses);
        NodeCallFrequencyCount = s.NodeCallFrequencyCount.clone();
        isDirty = s.isDirty;
        ratingStandardDeviation = s.ratingStandardDeviation;
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

    public void setFitnesses(HashMap<String, Fitness> values){
        this.Fitnesses = values;
    }

    public Map<String, Float> getFitnessesCombined(){
        HashMap<String, Float> fitnessesCombined = new HashMap<>();

        if(Fitnesses == null)
            return fitnessesCombined;

        for (Map.Entry<String, Fitness> entry : Fitnesses.entrySet()) {
            for(Map.Entry<String, Float> entry2 : entry.getValue().GetIndividualFitnessValues().entrySet()){
                if(fitnessesCombined.containsKey(entry2.getKey())){
                    fitnessesCombined.put(entry2.getKey(), fitnessesCombined.get(entry2.getKey()) + entry2.getValue());
                }
                else {
                    fitnessesCombined.put(entry2.getKey(), entry2.getValue());
                }
            }
        }

        return fitnessesCombined;
    }

    public void setNodeCallFrequencyCount(int[] nodeCallFrequencyCount){
        this.NodeCallFrequencyCount = nodeCallFrequencyCount;
    }

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

    public void setRatingStandardDeviation(double ratingStandardDeviation) {
        this.ratingStandardDeviation = ratingStandardDeviation;
    }

    public double getRatingStandardDeviation() {
        return ratingStandardDeviation;
    }
}