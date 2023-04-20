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

import java.util.Random;
import java.util.function.Function;

public enum MathOp implements Op<Double> {

    ADD("+", 2, v -> v[0] + v[1]),
    SUB("–", 2, v -> v[0] - v[1]),
    MUL("*",2, v -> v[0]*v[1]),

    DIV("/",2, v -> Math.abs(v[1]) < 0.000000001? 1: v[0]/v[1]),
    PI("π", OperationType.TERMINAL, v -> Math.PI),
    CONST("const", OperationType.CONSTANT, v -> MathOp.generateRandomConstant());

    private static final Double minVal = -20.0;
    private static final Double maxVal = 20.0;

    public static Double generateRandomConstant(){
        return minVal + (maxVal - minVal) * new Random().nextDouble();
    }

    @Override
    public int arity() {
        return _arity;
    }

    @Override
    public Double apply(Double[] args) {
        return _function.apply(args);
    }

    @Override
    public String toString() {
        return _name;
    }

    @Override
    public OperationType type() {
        return _type;
    }

    private final String _name;
    private final OperationType _type;
    private final int _arity;
    private final Function<Double[], Double> _function;

    MathOp(final String name, final OperationType type, final Function<Double[], Double> function) {
        assert name != null;
        assert type != null;
        assert function != null;

        _name = name;
        _type = type;
        _function = function;
        _arity = -1;
    }

    MathOp(final String name, final int arity, final Function<Double[], Double> function) {
        assert name != null;
        assert arity >= 0;
        assert function != null;

        _name = name;
        _type = OperationType.FUNCTION;
        _function = function;
        _arity = arity;
    }

    MathOp(final String name, final OperationType type, final int arity, final Function<Double[], Double> function) {
        assert name != null;
        assert type != null;
        assert arity >= 0;
        assert function != null;

        _name = name;
        _type = type;
        _function = function;
        _arity = arity;
    }
}
