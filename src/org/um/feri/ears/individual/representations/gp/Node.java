package org.um.feri.ears.individual.representations.gp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
        this(0, "");
    }
    public Node(String name) {
        this(0, name);
    }

    public Node(int arity, String name) {
        this(arity, new ArrayList<>(), false, name);
    }

    public Node(int arity, List<Node> children, boolean fixedNumOfChildren, String name) {
        this.id = Node.CURRENT_ID++;
        this.name = name;
        this.arity = arity;
        this.children = children;
        this.fixedNumOfChildren = fixedNumOfChildren;
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
            if(hasChildren()){
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
    public int treeHeight(){
        int maxHeight = 0;
        for (Node child : this.children) {
            int childHeight = child.treeHeight();
            maxHeight = Math.max(maxHeight, childHeight);
        }
        return maxHeight + 1;
    }

    @Override
    public int treeSize(){
        // Count all nodes from root to leaves
        int size = 1;
        for (Node child : this.children) {
            size += child.treeSize();
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
        if(this.arity > 0){
            size++;
        }
        for (Node child : this.children) {
            size += child.numberOfTerminals();
        }

        return size;
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
    public TreeAncestor2 ancestorAt(int index) {
        List<TreeAncestor2> nodesToCheck = new ArrayList<>();
        nodesToCheck.add(new TreeAncestor2(1, this));
        int currentInd = 0;
        while(!nodesToCheck.isEmpty()){
            TreeAncestor2 treeAncestor = nodesToCheck.get(0);
            Node current = treeAncestor.getTreeNode();
            for (Iterator<Node> it = current.getChildren().iterator(); it.hasNext();) {
                Node next = it.next();
                currentInd++;
                if(currentInd == index){
                    return new TreeAncestor2(treeAncestor.getTreeHeightPosition() + 1, next);
                }
                nodesToCheck.add(new TreeAncestor2(treeAncestor.getTreeHeightPosition() + 1, next));
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

    public String getName() {
        return name;
    }

    public abstract double evaluate(Map<String, Double> variables);

    //public abstract int getRandomNumberOfAllowedChildren();

    // abstract void addChild(Node children);

    public boolean hasChildren(){
        return children != null && !children.isEmpty();
    }

    public Node insert(final int index, final Node child) {
        requireNonNull(child);
        if (isAncestor(child)) {
            throw new IllegalArgumentException("The new child is an ancestor.");
        }

        if (child.parent != null) {
            child.parent.remove(child);
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

    public Node remove(final int index) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException(format(
                    "Child index is out of bounds: %s", index
            ));
        }

        final Node child = children.remove(index);
        assert child.parent == this;
        child.setParent(null);

        if (children.isEmpty()) {
            children = null;
        }

        return this;
    }

    public void remove(final INode<?> child) {
        requireNonNull(child);

        if (!isChild(child)) {
            throw new IllegalArgumentException("The given child is not a child.");
        }
        remove(indexOf(child));
    }

    public void detach(){
        if (parent != null) {
            parent.remove(this);
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
        ancestors.addAncestors(this.getChildren());

        for (Iterator<Node> it = children.iterator(); it.hasNext();) {
            Node next = it.next();
            next.ancestorCountSub(ancestors);
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

    /*
     * Define nodes
     * */
    private void setTreeNodes(GraphPrinter gp) {
        if(this.parent == null){
            gp.addln(id + " [label=\"" + this.name +"\", " + this.setNodeStyle() + "]");
        }
        for (Node next : children) {
            gp.addln(next.id + " [label=\"" + next.name + "\", " + next.setNodeStyle() + "]");
            next.setTreeNodes(gp);
        }
    }

    /*
     * Write node connections to org.um.feri.gpf.GraphPrinter
     * */
    private void displaySubTree(GraphPrinter gp){
        for (Iterator<Node> it = children.iterator(); it.hasNext();) {
            Node next = it.next();
            gp.addln(id + " -> " + next.id);
            next.displaySubTree(gp);
        }
    }

    private String setNodeStyle(){
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