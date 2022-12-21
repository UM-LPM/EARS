package org.um.feri.ears.problems;

import java.util.ArrayList;
import java.util.List;

public class NumberSolution<Type extends Number> extends SolutionBase {

    protected List<Type> variables;

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
        this(eval);
        variables = new ArrayList<>(x);
    }
    public NumberSolution(int numberOfObjectives, List<Type> x) {
        variables = new ArrayList<>(x);
        this.numberOfObjectives = numberOfObjectives; //TODO move to super
        objectives = new double[numberOfObjectives];
    }
    public NumberSolution(double eval) {
        objectives = new double[1];
        objectives[0] = eval;
    }
    public NumberSolution(NumberSolution<Type> s) {
        super(s);
        variables = new ArrayList<>(s.variables);
        if(objectives == null)
            objectives = new double[s.objectives.length];
        System.arraycopy(s.objectives, 0, objectives, 0, s.objectives.length);
        parents = new ArrayList<>();
    }

    public List<Type> getVariables() {
        return variables;
    }

    public int numberOfVariables() {
        return variables.size();
    }

    public void setValue(int i, Type c) {
        variables.set(i, c);
    }

    public void setVariables(List<Type> var) {
        this.variables = var;
    }

    public Type getValue(int i) {
        return variables.get(i);
    }


    public String toString() {
        return "NumberSolution - TODO " + getEval(); //TODO return based on number of objectives
        /*if (constraints == null || constraints.length == 0)
            return Util.dfcshort.format(eval) + " [" + Util.arrayToString(Util.toDoubleArray(getVariables())) + "]";
        else
            return Util.dfcshort.format(eval) + " [" + Util.arrayToString(Util.toDoubleArray(getVariables())) + "] cons:" + Arrays.toString(constraints);*/
    }
}
