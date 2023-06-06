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
package org.um.feri.ears.individual.representations.gp;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

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


    public void setParent(final TreeNode<T> parent) {
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
    public TreeAncestor ancestorAt(int index) {
        List<TreeAncestor> nodesToCheck = new ArrayList<>();
        nodesToCheck.add(new TreeAncestor(1, this));
        int currentInd = 0;
        while(!nodesToCheck.isEmpty()){
            TreeAncestor treeAncestor = nodesToCheck.get(0);
            TreeNode<T> current = treeAncestor.getTreeNode();
            for (Iterator<TreeNode<T>> it = current.getChildren().iterator(); it.hasNext();) {
                TreeNode<T> next = it.next();
                currentInd++;
                if(currentInd == index){
                    return new TreeAncestor<>(treeAncestor.getTreeHeightPosition() + 1, next);
                }
                nodesToCheck.add(new TreeAncestor<>(treeAncestor.getTreeHeightPosition() + 1, next));
            }
            nodesToCheck.remove(0);
        }

        throw new IndexOutOfBoundsException();
    }

    @Override
    public int childCount() {
        return children != null ? children.size() : 0;
    }

    @Override
    public TreeNode<T> copy() {
        return ofTree(this);
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

        child.setParent(this);
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

        child.setParent(this);

        return this;
    }

    public TreeNode<T> replace (final TreeNode<T> currentChild, final TreeNode<T> newChild){
        requireNonNull(currentChild);
        requireNonNull(newChild);

        return replace(this.getChildren().indexOf(currentChild), newChild);
    }

    public TreeNode<T> remove(final int index) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException(format(
                    "Child index is out of bounds: %s", index
            ));
        }

        final TreeNode<T> child = children.remove(index);
        assert child.parent == this;
        child.setParent(null);

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

    public void detach(){
        if (parent != null) {
            parent.remove(this);
        }
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

    // Tree copy
    public static <T, B> TreeNode<B> ofTree(
            final Tree<? extends T, ?> tree,
            final Function<? super T, ? extends B> mapper
    ) {
        final TreeNode<B> target = new TreeNode<>();
        target.operation = (Op<B>) tree.operation();
        target.coefficient = (B) tree.coeficient();
        copy(tree, target, mapper);
        return target;
    }

    private static <T, B> void copy(
            final Tree<? extends T, ?> source,
            final TreeNode<B> target,
            final Function<? super T, ? extends B> mapper
    ) {
        /*for (int i = 0; i < source.childCount(); ++i) {
            final Tree<? extends T, ?> child = source.childAt(i);
            final TreeNode<B> targetChild = of();
            targetChild.operation = (Op<B>) child.operation();
            targetChild.coefficient = (B) child.coeficient();
            target.attach(targetChild);
            copy(child, targetChild, mapper);
        }*/

        final Stack<Pair<Tree<? extends T, ?>, TreeNode<B>>> stack = new Stack<>();
        stack.push(new Pair<>(source, target));

        while (!stack.empty()) {
            final Pair<Tree<? extends T, ?>, TreeNode<B>> pair = stack.pop();
            final Tree<? extends T, ?> srcNode = pair.getFirst();
            final TreeNode<B> tgtNode = pair.getSecond();
            tgtNode.operation = (Op<B>) srcNode.operation();
            tgtNode.coefficient = mapper.apply(srcNode.coeficient());

            for (int i = 0; i < srcNode.childCount(); ++i) {
                final Tree<? extends T, ?> srcChild = srcNode.childAt(i);
                final TreeNode<B> tgtChild = new TreeNode<>();
                tgtNode.attach(tgtChild);
                stack.push(new Pair<>(srcChild, tgtChild));
            }
        }
    }

    public static <T> TreeNode<T> ofTree(final Tree<? extends T, ?> tree) {
        return ofTree(tree, Function.identity());
    }

    public TreeNode<T> attach(final TreeNode<T> child) {
        requireNonNull(child);

        if (child.parent == this) {
            insert(childCount() - 1, child);
        } else {
            insert(childCount(), child);
        }

        return this;
    }

    @SafeVarargs
    public final TreeNode<T> attach(final T... children) {
        for (T child : children) {
            attach(new TreeNode<>(child));
        }

        return this;
    }

    /**
     * Attaches the given {@code child} to {@code this} node.
     *
     * @param child the child to attach to {@code this} node
     * @return {@code this} tree-node, for method chaining
     */
    public TreeNode<T> attach(final T child) {
        return attach(new TreeNode<>(child));
    }

    @Override
    public Op<T> operation() {
        return operation;
    }

    @Override
    public T coeficient() {
        return coefficient;
    }
    //Tree copy END

    @Override
    public int treeHeight(){
        int maxHeight = 0;
        for (TreeNode<T> child : this.children) {
            int childHeight = child.treeHeight();
            maxHeight = Math.max(maxHeight, childHeight);
        }
        return maxHeight + 1;
    }

    @Override
    public int treeSize(){
        // Count all nodes from root to leaves
        int size = 1;
        for (TreeNode<T> child : this.children) {
            size += child.treeSize();
        }
        return size;
    }

    @Override
    public int numberOfFunctions(){
        if(this.operation.isComplex()){
            int size = 1;
            for (TreeNode<T> child : this.children) {
                size += child.numberOfFunctions();
            }
            return size;
        }
        return 0;
    }

    @Override
    public int numberOfTerminals(){
        int size = 0;
        if(this.operation.isSimple()){
            size++;
        }
        for (TreeNode<T> child : this.children) {
            size += child.numberOfTerminals();
        }

        return size;
    }

}

