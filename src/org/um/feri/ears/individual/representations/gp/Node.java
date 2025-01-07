package org.um.feri.ears.individual.representations.gp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.NotImplementedException;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.util.GraphPrinter;

import java.io.Serializable;
import java.util.*;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public abstract class Node implements INode<Node>, Iterable<Node>, Cloneable, Serializable {

    public static int CURRENT_ID = 1;

    protected int id;

    protected String name;

    protected Node parent;

    protected List<Node> children;

    // Defines the number of children this node can have
    protected int arity;

    // Parameter that defined if children count must be equal to arity
    protected boolean fixedNumOfChildren;

    public Node() {
        this("", 0);
    }
    public Node(String name) {
        this(name, 0);
    }

    public Node(String name, int arity) {
        this(name, arity, new ArrayList<>(), false);
    }

    public Node(String name, int arity, List<Node> children, boolean fixedNumOfChildren) {
        this.id = Node.CURRENT_ID++;
        this.name = name;
        this.arity = arity;
        this.children = children;
        this.fixedNumOfChildren = fixedNumOfChildren;
    }

    @JsonIgnore
    public int getId() {
        return id;
    }

    @JsonProperty("Children")
    public List<Node> getChildren() {
        return children;
    }

    @JsonProperty("Children")
    public void setChildren(List<Node> children) {
        this.children = children;
    }

    @JsonIgnore
    public int getArity() {
        return arity;
    }

    @JsonIgnore
    public void setArity(int arity) {
        this.arity = arity;
    }

    @JsonIgnore
    public boolean getFixedNumOfChildren() {
        return fixedNumOfChildren;
    }

    @Override
    public Node clone() {
        try {
            Node node = (Node) super.clone();
            node.id = Node.CURRENT_ID++;
            node.children = new ArrayList<>();
            if(children != null && !children.isEmpty()){
                for (Node child : this.children) {
                    Node cloned = child.clone();
                    cloned.parent = node;
                    node.children.add(cloned);
                }
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen
        }
    }

    @Override
    public int treeMaxDepth(){
        int maxDepth = 0;
        if(arity > 0) {
            for (Node child : this.children) {
                int childHeight = child.treeMaxDepth();
                maxDepth = Math.max(maxDepth, childHeight);
            }
        }
        return maxDepth + 1;
    }

    @Override
    public int treeMinDepth(){
        // Returns the minimum end depth (Terminals) of the tree
        if(arity == 0 || children.isEmpty()){
            return 1;
        }

        return treeMinDepthHelper(this, 1);
    }

    private int treeMinDepthHelper(Node node, int depth) {
        if (node.arity == 0) {
            return depth;
        }

        int minDepth = Integer.MAX_VALUE;
        for (Node child : node.children) {
            minDepth = Math.min(minDepth, treeMinDepthHelper(child, depth + 1));
        }
        return minDepth;
    }

    @Override
    public int treeSize(){
        // Count all nodes from root to leaves
        int size = 1;
        if(arity > 0) {
            for (Node child : this.children) {
                size += child.treeSize();
            }
        }
        return size;
    }

    @Override
    public int numberOfFunctions(){
        if(this.arity > 0){
            int size = 1;
            for (Node child : this.children) {
                size += child.numberOfFunctions();
            }
            return size;
        }
        return 0;
    }

    @Override
    public int numberOfTerminals(){
        int size = 0;
        if(this.arity == 0){
            size++;
        }
        else{
            for (Node child : this.children) {
                size += child.numberOfTerminals();
            }
        }

        return size;
    }

    @Override
    public HashMap<String, Integer> nodeCounts() {
        HashMap<String, Integer> nodeCounts = new HashMap<>();

        nodeCounts.put(this.name, 1);

        if(arity > 0){ // TODO Fix: This doesn't work for Encapsulator nodes (their arity is 0)
            for (Node child : this.children) {
                HashMap<String, Integer> childNodeCounts = child.nodeCounts();
                for (Map.Entry<String, Integer> entry : childNodeCounts.entrySet()) {
                    if(nodeCounts.containsKey(entry.getKey())){
                        nodeCounts.put(entry.getKey(), nodeCounts.get(entry.getKey()) + entry.getValue());
                    }
                    else{
                        nodeCounts.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        return nodeCounts;
    }

    public int childIndex(Node child) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException(
                    "Children is null"
            );
        }

        return children.indexOf(child);
    }

    @Override
    public int childCount() {
        return children != null ? children.size() : 0;
    }

    @Override
    public Node childAt(int index) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException(format(
                    "Child index is out of bounds: %s", index
            ));
        }

        return children.get(index);
    }

    @Override
    public Iterator<Node> iterator() {
        return null;
    }

    @Override
    public TreeAncestor ancestorAt(int index) {
        List<TreeAncestor> nodesToCheck = new ArrayList<>();
        nodesToCheck.add(new TreeAncestor(1, this));
        int currentInd = 0;
        while(!nodesToCheck.isEmpty()){
            TreeAncestor treeAncestor = nodesToCheck.get(0);
            Node current = treeAncestor.getTreeNode();
            if(current.arity > 0){
                for (Iterator<Node> it = current.getChildren().iterator(); it.hasNext();) {
                    Node next = it.next();
                    currentInd++;
                    if(currentInd == index){
                        return new TreeAncestor(treeAncestor.getTreeDepthPosition() + 1, next);
                    }
                    nodesToCheck.add(new TreeAncestor(treeAncestor.getTreeDepthPosition() + 1, next));
                }
            }
            nodesToCheck.remove(0);
        }

        throw new IndexOutOfBoundsException();
    }

    public Optional<Node> parent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(final Node parent) {
        this.parent = parent;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    public abstract double evaluate(Map<String, Double> variables);

    //public abstract int getRandomNumberOfAllowedChildren();

    // abstract void addChild(Node children);

    public boolean hasChildren(){
        return arity > 0 && children != null && !children.isEmpty();
    }

    public Node insert(final int index, final Node child) {
        requireNonNull(child);
        if (isAncestor(child)) {
            throw new IllegalArgumentException("The new child is an ancestor.");
        }

        if (child.parent != null) {
            child.parent.remove(child, false);
        }

        child.setParent(this);
        createChildrenIfMissing();
        children.add(index, child);

        return this;
    }

    public Node replace(final int index, final Node child) {
        requireNonNull(child);
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException(format(
                    "Child index is out of bounds: %s", index
            ));
        }
        if (isAncestor(child)) {
            throw new IllegalArgumentException("The new child is an ancestor.");
        }

        final Node oldChild = children.set(index, child);
        assert oldChild != null;
        assert oldChild.parent == this;

        child.setParent(this);

        return this;
    }

    public Node replace (final Node currentChild, final Node newChild){
        requireNonNull(currentChild);
        requireNonNull(newChild);

        return replace(this.getChildren().indexOf(currentChild), newChild);
    }

    public Node remove(final int index, boolean removeEmptyParent) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException(format(
                    "Child index is out of bounds: %s", index
            ));
        }

        final Node child = children.remove(index);
        assert child.parent == this;
        child.setParent(null);

        if (children.isEmpty()) {
            //children = null;
            if(removeEmptyParent)
                detach(true);
        }

        return this;
    }

    public void remove(final INode<?> child, boolean removeEmptyParent) {
        requireNonNull(child);

        if (!isChild(child)) {
            throw new IllegalArgumentException("The given child is not a child.");
        }
        remove(indexOf(child), removeEmptyParent);
    }

    public void removeAncestorAt(int index, boolean removeEmptyParent){
        TreeAncestor treeAncestor = ancestorAt(index);
        treeAncestor.getTreeNode().detach(removeEmptyParent);
    }

    /***
     * Removes the the node from the tree
     * @param removeEmptyParent if true, the parent is detached if the parent node has no children after this node is removed
     */
    public void detach(boolean removeEmptyParent){
        if (parent != null) {
            parent.remove(this,removeEmptyParent);
        }
    }

    public Node attach(final Node child) {
        requireNonNull(child);

        if (child.parent == this) {
            insert(childCount() - 1, child);
        } else {
            insert(childCount(), child);
        }

        return this;
    }

    public void removeInvalidNodes(ProgramProblem tProgramProblem, int currentDepth){
        if (arity > 0 && children != null) {
            currentDepth++;
            Iterator<Node> iterator = children.iterator();
            // If rootNode doesn't have children, add a random terminal node
            if(!iterator.hasNext() && currentDepth == 2){
                //System.out.println("Adding random terminal to as a child to root node.");
                Node newNode = tProgramProblem.getProgramSolutionGenerator().generateRandomTerminalNode(tProgramProblem);
                children.add(newNode);
                return;
            }

            while (iterator.hasNext()) {
                Node child = iterator.next();
                child.removeInvalidNodes(tProgramProblem, currentDepth); // Recursively clean up child nodes

                if (child.arity > 0 && (child.children == null || child.children.isEmpty())) {
                    if(currentDepth == tProgramProblem.getMinTreeDepth()){
                        //System.out.println("Replacing node: " + name + " with a random terminal node");
                        Node newNode = tProgramProblem.getProgramSolutionGenerator().generateRandomTerminalNode(tProgramProblem);
                        replace(child, newNode);
                    }
                    else {
                        //System.out.println("Removing invalid node: " + name);
                        iterator.remove();
                    }
                }
            }
        }
    }

    private void createChildrenIfMissing() {
        if (children == null) {
            children = new ArrayList<>(2);
        }
    }

    public Node.Ancestor ancestors() {
        Node.Ancestor ancestors = new Node.Ancestor();
        ancestorCountSub(ancestors);
        return  ancestors;
    }

    private void ancestorCountSub(Node.Ancestor ancestors) {
        if(arity > 0){
            ancestors.addAncestors(this.getChildren());

            for (Iterator<Node> it = children.iterator(); it.hasNext();) {
                Node next = it.next();
                next.ancestorCountSub(ancestors);
            }
        }
    }

    /*
     * Creates .dot file and png image with visual representation of a tree
     * */
    public String displayTree(String filename, boolean show){
        GraphPrinter gp = new GraphPrinter(filename);
        setTreeNodes(gp);
        displaySubTree(gp);
        return gp.print(show);
    }

    public String toDotString(){
        GraphPrinter gp = new GraphPrinter();
        gp.addln("digraph G {");
        setTreeNodes(gp);
        displaySubTree(gp);
        gp.addln("}");

        return gp.toString();
    }

    /*
     * Define nodes
     * */
    public void setTreeNodes(GraphPrinter gp) {
        gp.addln(id + " [label=\"" + this.name +"\", " + this.setNodeStyle() + "]");
        //gp.addln(id + " [<label=\"" + this.name +"<BR /><FONT POINT-SIZE=\"10\">"+ "this.propertiesToString()" +"</FONT>>\", " + this.setNodeStyle() + "]");
        //gp.addln(id + "[label=<"+ this.name +"<BR /><FONT POINT-SIZE=\"10\">" + "this.propertiesToString()" + "</FONT>"+ this.setNodeStyle() +">]");

        for (Node next : children) {
            next.setTreeNodes(gp);
        }
    }

    /*
     * Write node connections to org.um.feri.gpf.GraphPrinter
     * */
    public void displaySubTree(GraphPrinter gp){
        for (Iterator<Node> it = children.iterator(); it.hasNext();) {
            Node next = it.next();
            gp.addln(id + " -> " + next.id);
            next.displaySubTree(gp);
        }
    }

    public String setNodeStyle(){
        String nodeStyle = "";

        //TODO
        //nodeStyle = "shape=circle";

        return  nodeStyle;
    }


    public class Ancestor {
        protected int ancestorCount;
        protected List<Node> ancestors;

        public Ancestor() {
            this.ancestorCount = 0;
            this.ancestors = new ArrayList<>();
        }

        public int getAncestorCount() {
            return ancestorCount;
        }

        public void setAncestorCount(int ancestorCount) {
            this.ancestorCount = ancestorCount;
        }

        public void addAncestors(List<Node> nodes) {
            this.ancestorCount += nodes.size();
            this.ancestors.addAll(nodes);
        }

        public List<Node> getAncestors() {
            return ancestors;
        }

        public void setAncestors(List<Node> ancestors) {
            this.ancestors = ancestors;
        }
    }

}