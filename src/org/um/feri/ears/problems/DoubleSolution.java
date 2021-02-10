package org.um.feri.ears.problems;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoubleSolution extends SolutionBase<Double> {
    private double eval;
    public List<DoubleSolution> parents;

    public DoubleSolution(DoubleSolution s) {
        super(s);

        eval = s.eval;
        parents = new ArrayList<DoubleSolution>();
    }

    public DoubleSolution() {
    }

    /**
     * !!!This constructor is for unconstrained optimization!
     *
     * @param x
     * @param eval
     * @deprecated
     */
    public DoubleSolution(List<Double> x, double eval) {

        //variable = Arrays.copyOf(x,x.length);
        variable = new ArrayList<Double>(x);
        this.eval = eval;
        constraintsMet = true;
    }

    /**
     * Use this constructor only in case of constrained optimization
     *
     * @param x
     * @param eval
     * @param constrains
     */
    public DoubleSolution(List<Double> x, double eval, double[] constrains) {
        variable = new ArrayList<Double>(x);
        checkConstraints(constrains);
        this.eval = eval;
    }

    public DoubleSolution(double[] ds, double eval, double[] constrains) {
        this(Arrays.asList(ArrayUtils.toObject(ds)), eval, constrains);
    }

    private void checkConstraints(double[] constrains) {
        constraintsMet = true;
        for (int i = 0; i < constrains.length; i++) {
            if (constrains[i] > 0) { //equal constrained needs to be solve in Problem (set 0 if<=0.001)
                constraintsMet = false;
                this.constraints = new double[constrains.length];
                System.arraycopy(constrains, 0, this.constraints, 0, constrains.length);
            }
        }
    }

    /**
     * Outboxes the array of variables to a primitive array.
     *
     * @return the variables in an array of primitives.
     */
    public double[] getDoubleVariables() {

        return variable.stream().mapToDouble(i -> i).toArray();
        //return  ArrayUtils.toPrimitive(variable);
    }

	/**
	 * Returns the evaluated fitness value.
	 * @return the evaluated fitness value.
	 */
	public double getEval() {
        return eval;
    }

    /**
     * Set the fitness value.
     */
    public void setEval(double eval) {
        this.eval = eval;
    }

    public String toString() {
        if (constraints == null || constraints.length == 0)
            return Util.dfcshort.format(eval) + " [" + Util.arrayToString(getDoubleVariables()) + "]";
        else
            return Util.dfcshort.format(eval) + " [" + Util.arrayToString(getDoubleVariables()) + "] cons:" + Arrays.toString(constraints);
    }
}
