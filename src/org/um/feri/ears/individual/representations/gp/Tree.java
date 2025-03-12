package org.um.feri.ears.individual.representations.gp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.um.feri.ears.individual.representations.gp.behaviour.tree.*;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.*;
import org.um.feri.ears.problems.gp.ProgramProblem;

import java.io.Serializable;
import java.util.*;

/****************************************************************************************
 * This class represents a tree structure used in Genetic Programming.
 * The tree is used to represent a solution to a problem.
 ****************************************************************************************/
public abstract class Tree implements Serializable {
    public enum TreeType {
        SYMBOLIC,
        BEHAVIOUR
    }

    protected String name;

    protected Node rootNode;

    public Tree(String name) {
        this(name, null);
    }

    public Tree(String name, Node rootNode) {
        this.rootNode = rootNode;
        this.name = name;
    }

    @JsonProperty("RootNode")
    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    @JsonProperty("RootNode")
    public Node getRootNode() {
        return rootNode;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    public String toJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int treeMaxDepth() {
        return rootNode.treeMaxDepth();
    }

    public int treeMinDepth() {
        return rootNode.treeMinDepth();
    }

    public int treeSize() {
        return rootNode.treeSize();
    }

    public int numberOfFunctions() {
        return rootNode.numberOfFunctions();
    }

    public int numberOfTerminals() {
        return rootNode.numberOfTerminals();
    }

    @JsonIgnore
    public List<Node> getFunctionNodes() {
        //return rootNode.getFunctionNodes();
        List<Node> nodes = new ArrayList<>();

        Queue<Node> nodesToCheck = new LinkedList<>();
        nodesToCheck.add(rootNode);

        while (!nodesToCheck.isEmpty()) {
            Node current = nodesToCheck.poll();
            if (current.arity > 0) {
                nodes.add(current);
                nodesToCheck.addAll(current.children);
            }
        }

        return nodes;
    }

    @JsonIgnore
    public List<Node> getTerminalNodes() {
        //return rootNode.getTerminalNodes();
        List<Node> nodes = new ArrayList<>();

        Queue<Node> nodesToCheck = new LinkedList<>();
        nodesToCheck.add(rootNode);

        while (!nodesToCheck.isEmpty()) {
            Node current = nodesToCheck.poll();

            if (current.arity == 0) {
                nodes.add(current);
            }
            if (current.children != null && !current.children.isEmpty()) {
                nodesToCheck.addAll(current.children);
            }
        }
        return nodes;
    }

    public abstract double evaluate(Map<String, Double> variables);

    public abstract Tree clone();

    public void removeAncestorAt(int index, boolean removeEmptyParent) {
        rootNode.removeAncestorAt(index, removeEmptyParent);
    }

    public void removeInvalidNodes(ProgramProblem tProgramProblem) {
        rootNode.removeInvalidNodes(tProgramProblem, 1);
    }

    public String displayTree(String filename, boolean show) {
        return rootNode.displayTree(filename, show);
    }

}
