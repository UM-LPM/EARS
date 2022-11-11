/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package org.um.feri.ears.problems.gp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class TreeNode<T> implements Tree<T, TreeNode<T>>, Iterable<TreeNode<T>>, Copyable<TreeNode<T>>, Serializable {

    public static int CURRENT_ID = 1;

    private int id;

    private TreeNode<T> parent;
    private List<TreeNode<T>> children;

    private Op<T> operation;
    private T coefficient;

    /* Constructors */
    public TreeNode() {
        this.children = new ArrayList<>();
        this.id = TreeNode.CURRENT_ID;
        TreeNode.CURRENT_ID += 1;
    }
    public TreeNode(Op<T> operation) {
        this.operation = operation;
        this.children = new ArrayList<>();
        this.id = TreeNode.CURRENT_ID;
        TreeNode.CURRENT_ID += 1;
    }

    public TreeNode(T coefficient) {
        this.coefficient = coefficient;
        this.children = new ArrayList<>();
        this.id = TreeNode.CURRENT_ID;
        TreeNode.CURRENT_ID += 1;
    }

    public Op<T> getOperation() {
        return operation;
    }

    public void setOperation(Op<T> operation) {
        this.operation = operation;
    }

    public T getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(T coefficient) {
        this.coefficient = coefficient;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }

    @Override
    public Optional<TreeNode<T>> parent() {
        return Optional.ofNullable(parent);
    }


    public void parent(final TreeNode<T> parent) {
        this.parent = parent;
    }

    @Override
    public TreeNode<T> childAt(int index) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException(format(
                    "Child index is out of bounds: %s", index
            ));
        }

        return children.get(index);
    }

    @Override
    public int childCount() {
        return children != null ? children.size() : 0;
    }

    @Override
    public TreeNode<T> copy() {
        return null;
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        return null;
    }

    public TreeNode<T> insert(final int index, final TreeNode<T> child) {
        requireNonNull(child);
        if (isAncestor(child)) {
            throw new IllegalArgumentException("The new child is an ancestor.");
        }

        if (child.parent != null) {
            child.parent.remove(child);
        }

        child.parent(this);
        createChildrenIfMissing();
        children.add(index, child);

        return this;
    }

    public TreeNode<T> replace(final int index, final TreeNode<T> child) {
        requireNonNull(child);
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException(format(
                    "Child index is out of bounds: %s", index
            ));
        }
        if (isAncestor(child)) {
            throw new IllegalArgumentException("The new child is an ancestor.");
        }

        final TreeNode<T> oldChild = children.set(index, child);
        assert oldChild != null;
        assert oldChild.parent == this;

        oldChild.parent(null);
        child.parent(this);

        return this;
    }

    public TreeNode<T> remove(final int index) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException(format(
                    "Child index is out of bounds: %s", index
            ));
        }

        final TreeNode<T> child = children.remove(index);
        assert child.parent == this;
        child.parent(null);

        if (children.isEmpty()) {
            children = null;
        }

        return this;
    }

    public void remove(final Tree<?, ?> child) {
        requireNonNull(child);

        if (!isChild(child)) {
            throw new IllegalArgumentException("The given child is not a child.");
        }
        remove(indexOf(child));
    }

    private void createChildrenIfMissing() {
        if (children == null) {
            children = new ArrayList<>(2);
        }
    }

    public Ancestor ancestors() {
        Ancestor ancestors = new Ancestor();
        ancestorCountSub(ancestors);
        return  ancestors;
    }

    private void ancestorCountSub(Ancestor ancestors) {
        ancestors.addAncestors(this.getChildren());

        for (Iterator<TreeNode<T>> it = children.iterator(); it.hasNext();) {
            TreeNode next = it.next();
            next.ancestorCountSub(ancestors);
        }
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(500);
        print(buffer, "", "");
        return buffer.toString();
    }

    /*
     * Text representation of a tree
     * */
    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        //TODO
        /*buffer.append(prefix);
        buffer.append(value());
        buffer.append('\n');

        for (Iterator<TreeNode<T>> it = children.iterator(); it.hasNext();) {
            TreeNode next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }*/
    }

    /*
     * Creates .dot file and png image with visual representation of a tree
     * */
    public void displayTree(String filename){
        GraphPrinter gp = new GraphPrinter(filename);
        setTreeNodes(gp);
        displaySubTree(gp);
        gp.print();
    }

    /*
     * Define nodes
     * */
    private void setTreeNodes(GraphPrinter gp) {
        if(this.parent == null){
            gp.addln(id + " [label=\"" + this.operation +"\", " + this.setNodeStyle() + "]");
        }
        for (TreeNode<T> next : children) {
            gp.addln(next.id + " [label=\"" + next.operation + "\", " + next.setNodeStyle() + "]");
            next.setTreeNodes(gp);
        }
    }

    /*
     * Set node properties depending on nodeType
     * */
    private String setNodeStyle(){
        String nodeStyle = "";

        //TODO
        //nodeStyle = "shape=circle";

        return  nodeStyle;
    }

    /*
     * Write node connections to org.um.feri.gpf.GraphPrinter
     * */
    private void displaySubTree(GraphPrinter gp){
        for (Iterator<TreeNode<T>> it = children.iterator(); it.hasNext();) {
            TreeNode next = it.next();
            gp.addln(id + " -> " + next.id);
            next.displaySubTree(gp);
        }
    }

    public class Ancestor {
        protected int ancestorCount;
        protected List<TreeNode<T>> ancestors;

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

        public void addAncestors(List<TreeNode<T>> nodes) {
            this.ancestorCount += nodes.size();
            this.ancestors.addAll(nodes);
        }

        public List<TreeNode<T>> getAncestors() {
            return ancestors;
        }

        public void setAncestors(List<TreeNode<T>> ancestors) {
            this.ancestors = ancestors;
        }
    }

}
