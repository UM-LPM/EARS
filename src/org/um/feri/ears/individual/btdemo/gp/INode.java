package org.um.feri.ears.individual.btdemo.gp;

import org.um.feri.ears.individual.representations.gp.Op;
import org.um.feri.ears.individual.representations.gp.Self;
import org.um.feri.ears.individual.representations.gp.TreeAncestor;
import org.um.feri.ears.individual.representations.gp.TreeAncestor2;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public interface INode<T extends INode<T>> extends Self<T>, Iterable<T> {


    /*Return the parent node of this INode node*/
    Optional<T> parent();

    /*Return the child node with the given index*/
    T childAt(final int index);

    /*Returns the ancestor node with the given index*/
    TreeAncestor2 ancestorAt(final int index);

    /*Returns the number of children this INode consists of*/
    int childCount();

    /*Returns the depth of the INode*/
    int treeHeight();
    int treeSize();

    int numberOfFunctions();
    int numberOfTerminals();

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

    default boolean isChild(final INode<?> node) {
        requireNonNull(node);
        return childCount() != 0 &&
                node.parent().equals(Optional.of(self()));
    }

    default boolean identical(final INode<?> other) {
        return this == other;
    }

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
