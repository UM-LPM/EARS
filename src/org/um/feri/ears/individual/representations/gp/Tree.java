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

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public interface Tree<V, T extends Tree<V, T>> extends Self<T>, Iterable<T> {

    Op<V> operation();
    V coeficient();

    /*Return the parent node of this tree node*/
    Optional<T> parent();

    /*Return the child node with the given index*/
    T childAt(final int index);

    /*Returns the ancestor node with the given index*/
    TreeAncestor<T> ancestorAt(final int index);

    /*Returns the number of children this tree consists of*/
    int childCount();

    /*Returns the depth of the tree*/
    int treeHeight();

    int treeSize();

    int numberOfFunctions();
    int numberOfTerminals();

    default boolean isAncestor(final Tree<?, ?> node) {
        requireNonNull(node);

        Optional<T> ancestor = Optional.of(self());
        boolean result;
        do {
            result = ancestor.filter(a -> a.identical(node)).isPresent();
        } while (!result &&
                (ancestor = ancestor.flatMap(Tree::parent)).isPresent());

        return result;
    }

    default boolean isChild(final Tree<?, ?> node) {
        requireNonNull(node);
        return childCount() != 0 &&
                node.parent().equals(Optional.of(self()));
    }

    default boolean identical(final Tree<?, ?> other) {
        return this == other;
    }

    default int indexOf(final Tree<?, ?> child) {
        int index = -1;
        for (int i = 0, n = childCount(); i < n && index == -1; ++i) {
            if (childAt(i).identical(child)) {
                index = i;
            }
        }

        return index;
    }

}
