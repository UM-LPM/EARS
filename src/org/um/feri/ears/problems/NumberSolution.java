package org.um.feri.ears.problems;

import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class NumberSolution<Type extends Number> extends Solution {

    protected ArrayList<Type> variables;

    /*
        Constructor for single-objective optimization
     */
    public NumberSolution(List<Type> x, double eval, double[] constrains) {
        this(x, eval);
        checkConstraints(constrains);
    }

    /*
        Constructor for single-objective optimization
    */
    public NumberSolution(List<Type> x, double eval) {
        super(1);
        setObjective(0, eval);
        variables = new ArrayList<>(x);
    }

    /*
        Constructor for single-objective optimization
    */
    public NumberSolution(List<Type> x) {
        super(1);
        variables = new ArrayList<>(x);
    }

    public NumberSolution(List<Type> x, double[] objectives) {
        super(objectives.length);
        System.arraycopy(objectives, 0, this.objectives, 0, objectives.length);
        variables = new ArrayList<>(x);
        attributes = new HashMap<>();
    }

    public NumberSolution(int numberOfObjectives, List<Type> x) {
        super(numberOfObjectives);
        variables = new ArrayList<>(x);
    }

    public NumberSolution(int numberOfObjectives) {
        super(numberOfObjectives);
    }

    public NumberSolution(NumberSolution<Type> s) {
        super(s);
        variables = new ArrayList<>(s.variables);
    }

    @Override
    public NumberSolution<Type> copy() {
        return new NumberSolution<>(this);
    }

    public ArrayList<Type> getVariables() {
        return variables;
    }

    public int numberOfVariables() {
        return variables.size();
    }

    public void setValue(int i, Type c) {
        variables.set(i, c);
    }

    public void setVariables(ArrayList<Type> var) {
        this.variables = var;
    }

    public Type getValue(int i) {
        return variables.get(i);
    }


    public String toString() {

        String objectivesString = Arrays.stream(getObjectives()).mapToObj(o -> Util.dfcshort.format(o)).collect(Collectors.joining(",", "[", "]"));

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
}
