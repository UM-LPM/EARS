package org.um.feri.ears.problems;

public class DummySolution extends NumberSolution<Double> {

    public DummySolution(double value) {
        super(value);
    }

    @Override
    public String toString() {
        return "DummySolution eval " + getEval();
    }
}
