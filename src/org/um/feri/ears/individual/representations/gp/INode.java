package org.um.feri.ears.individual.representations.gp;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public interface INode<T extends INode<T>> extends Self<T>, Iterable<T> {


    /**Return the parent node of this INode node*/
    Optional<T> parent();

    /**Return the child node with the given index*/
    T childAt(final int index);

    /**Returns the ancestor node with the given index*/
    TreeAncestor ancestorAt(final int index);

    /**Returns the number of children of this Node*/
    int childCount();

    /**Returns the max tree depth*/
    int treeMaxDepth();
    /**Returns the min tree depth*/
    int treeMinDepth();
    /**Returns the number of nodes in a tree*/
    int treeSize();
    /**Returns the number of function nodes in a tree*/
    int numberOfFunctions();
    /**Returns the number of terminal nodes in a tree*/
    int numberOfTerminals();

    /**Validates if the given node is an ancestor of this node*/
    default boolean isAncestor(final INode<?> node) {
        requireNonNull(node);

        Optional<T> ancestor = Optional.of(self());
        boolean result;
        do {
            result = ancestor.filter(a -> a.identical(node)).isPresent();
        } while (!result &&
                (ancestor = ancestor.flatMap(INode::parent)).isPresent());

        return result;
    }

    /**Validates if the given node is a child of this node*/
    default boolean isChild(final INode<?> node) {
        requireNonNull(node);
        return childCount() != 0 &&
                node.parent().equals(Optional.of(self()));
    }

    /**Validates if one Node is identical to the other one*/
    default boolean identical(final INode<?> other) {
        return this == other;
    }

    /**Returns the index of the given child node*/
    default int indexOf(final INode<?> child) {
        int index = -1;
        for (int i = 0, n = childCount(); i < n && index == -1; ++i) {
            if (childAt(i).identical(child)) {
                index = i;
            }
        }

        return index;
    }
}
