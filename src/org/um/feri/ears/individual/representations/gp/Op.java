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


import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

public interface Op<T> extends Function<T[], T>, Supplier<Op<T>> {

    String name();
    OperationType type();

    int arity();

    default boolean isTerminal() {
        return type() == OperationType.TERMINAL;
    }

    default boolean isVariable() {
        return type() == OperationType.VARIABLE;
    }
    default boolean isConstant() {
        return type() == OperationType.CONSTANT;
    }

    default boolean isComplex() {
        return arity() > 0;
    }

    default boolean isSimple() {
        return arity() < 0;
    }

    @Override
    default Op<T> get() {
        return this;
    }

    static <T> Op<T> define(
            final String name,
            final OperationType type
    ) {
        return new Operation<>(name, type, 0, v -> null);
    }

    static <T> Op<T> define(
            final String name,
            final int arity,
            final Function<T[], T> function
    ) {
        return new Operation<>(name, OperationType.FUNCTION, arity, function);
    }

    static <T> Op<T> define(
            final String name,
            final OperationType type,
            final int arity,
            final Function<T[], T> function
    ) {
        return new Operation<>(name, type, arity, function);
    }

    static <T> Op<T> define(
            final String name,
            final UnaryOperator<T> function
    ) {
        requireNonNull(function);
        return define(name, OperationType.FUNCTION, 1, v -> function.apply(v[0]));
    }

    static <T> Op<T> define(
            final String name,
            final BinaryOperator<T> function
    ) {
        requireNonNull(function);
        return define(name, OperationType.FUNCTION,2, v -> function.apply(v[0], v[1]));
    }
}