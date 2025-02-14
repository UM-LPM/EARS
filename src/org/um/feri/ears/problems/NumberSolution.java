package org.um.feri.ears.problems;

import org.um.feri.ears.util.Point;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class NumberSolution<N extends Number> extends Solution {

    protected ArrayList<N> variables;

    public NumberSolution() {
        super();
    }

    /*
        Constructor for single-objective optimization
     */
    public NumberSolution(List<N> x, double eval, double[] constrains) {
        this(x, eval);
        checkConstraints(constrains);
    }

    /*
        Constructor for single-objective optimization
    */
    public NumberSolution(List<N> x, double eval) {
        super(1);
        setObjective(0, eval);
        variables = new ArrayList<>(x);
    }

    /*
        Constructor for single-objective optimization
    */
    public NumberSolution(List<N> x) {
        super(1);
        variables = new ArrayList<>(x);
    }

    public NumberSolution(N[] x) {
        this(new ArrayList<>(Arrays.asList(x)));
    }

    public NumberSolution(List<N> x, double[] objectives) {
        super(objectives.length);
        System.arraycopy(objectives, 0, this.objectives, 0, objectives.length);
        variables = new ArrayList<>(x);
        attributes = new HashMap<>();
    }

    public NumberSolution(int numberOfObjectives, List<N> x) {
        super(numberOfObjectives);
        variables = new ArrayList<>(x);
    }

    public NumberSolution(int numberOfObjectives) {
        super(numberOfObjectives);
    }

    public NumberSolution(NumberSolution<N> s) {
        super(s);
        variables = new ArrayList<>(s.variables);
    }

    @Override
    public NumberSolution<N> copy() {
        return new NumberSolution<>(this);
    }

    public ArrayList<N> getVariables() {
        return variables;
    }

    public Point<N> getVariablesAsPoint() {
        return new Point<N>(variables);
    }

    public int numberOfVariables() {
        return variables.size();
    }

    public void setValue(int i, N c) {
        variables.set(i, c);
    }

    public void setVariables(ArrayList<N> var) {
        this.variables = var;
    }

    public N getValue(int i) {
        return variables.get(i);
    }


    public String toString() {

        String objectivesString = Arrays.stream(getObjectives())
                .mapToObj(o -> Util.dfcshort.format(o) + " (" + o + ")")
                .collect(Collectors.joining(",", "[", "]"));

        String variablesString =  getVariables().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));

        if (constraints == null || constraints.length == 0) {
            return "objectives: " +objectivesString + " variables: " + variablesString;
        }
        else
            return "objectives: " +objectivesString + " variables: " + variablesString + " cons:" + Arrays.toString(constraints);
    }

    public String toStringCSV() {
        String aux = "";
        for (int i = 0; i < numberOfObjectives; i++)
            aux = aux + this.getObjective(i) + ";";
        aux = aux.replace(".", ",");
        return aux;
    }

    // This method is used to set existing object to original
    // used instead of clone.
    public void setClone(NumberSolution<N> org) {
        variables = new ArrayList<>(org.variables);
        super.setClone(org);
    }
}
