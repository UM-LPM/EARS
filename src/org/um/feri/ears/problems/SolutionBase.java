package org.um.feri.ears.problems;

import java.util.ArrayList;
import java.util.List;

public abstract class SolutionBase <Type> {
	
	protected List<Type> variable;
	protected double[] constraints; //TODO refactor 2 types of individual for constrained optimization
	protected boolean feasible; //Feasible checks constrains
	protected double  overallConstraintViolation_ ;
	protected int  numberOfViolatedConstraints_ ;
	protected List<Type> upperLimit;
	protected List<Type> lowerLimit;

	public SolutionBase() {}

	public SolutionBase(SolutionBase<Type> s) {
		variable = new ArrayList<Type>(s.variable);
		
		this.feasible = s.feasible;
		if (s.constraints!=null) {
			constraints = new double[s.constraints.length];
			System.arraycopy(s.constraints, 0, constraints, 0, constraints.length);
		}
		overallConstraintViolation_  = s.getOverallConstraintViolation();
		numberOfViolatedConstraints_ = s.getNumberOfViolatedConstraint();
	}

	public boolean isFeasible() {
		return feasible;
	}

	public List<Type> getVariables() {
		return variable;
	}
	
	public abstract double getEval();
	
	public boolean isEqual(SolutionBase<Type> b, double draw_limit) {
		if (Math.abs(this.getEval()-b.getEval())<draw_limit) return true;
		return false;
	}
	
	public double[] getNewVariables() {
		double[] xx = new double[variable.size()];
		System.arraycopy(variable, 0, xx, 0, variable.size());
		return xx;
	}

	public int numberOfVariables() {
		return variable.size();
	}

	public void setValue(int i, Type c) {
		variable.set(i, c);
	}

	public Type getValue(int i) {
		return variable.get(i);
	}
	
	public List<Type> getUpperLimit() {
		return upperLimit;
	}

	public List<Type> getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * Sets all constraints of this solution.
	 * 
	 * @param constraints the new constraints for this solution
	 * @throws IllegalArgumentException if {@code constraints.length !=
	 *         getNumberOfConstraints()}
	 */
	public void setConstraints(double[] constraints) {
		if (constraints.length != this.constraints.length) {
			throw new IllegalArgumentException("invalid number of constraints");
		}

		for (int i = 0; i < constraints.length; i++) {
			this.constraints[i] = constraints[i];
		}
	}

	public double[] getConstraints() {
		return constraints;
	}

	/**
	 * Sets the overall constraints violated by the solution.
	 * @param value The overall constraints violated by the solution.
	 */
	public void setOverallConstraintViolation(double value) {
		this.overallConstraintViolation_ = value;
	} // setOverallConstraintViolation

	/**
	 * Gets the overall constraint violated by the solution.
	 * <b> REQUIRE </b>: This method has to be invoked after calling 
	 * <code>overallConstraintViolation</code>.
	 * @return the overall constraint violation by the solution.
	 */
	public double getOverallConstraintViolation() {
		return this.overallConstraintViolation_;
	}  //getOverallConstraintViolation


	/**
	 * Sets the number of constraints violated by the solution.
	 * @param value The number of constraints violated by the solution.
	 */
	public void setNumberOfViolatedConstraint(int value) {
		this.numberOfViolatedConstraints_ = value;
	} //setNumberOfViolatedConstraint

	/**
	 * Gets the number of constraint violated by the solution.
	 * <b> REQUIRE </b>: This method has to be invoked after calling
	 * <code>setNumberOfViolatedConstraint</code>.
	 * @return the number of constraints violated by the solution.
	 */
	public int getNumberOfViolatedConstraint() {
		return this.numberOfViolatedConstraints_;
	} // getNumberOfViolatedConstraint

	/**
	 * Returns {@code true} if any of the constraints are violated;
	 * {@code false} otherwise.
	 * 
	 * @return {@code true} if any of the constraints are violated;
	 *         {@code false} otherwise
	 */
	public boolean violatesConstraints() {

		if (constraints == null)
			return false;

		for (int i = 0; i < constraints.length; i++) {
			if (constraints[i] != 0.0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the number of constraints defined by this solution.
	 * 
	 * @return the number of constraints defined by this solution
	 */
	public int getNumberOfConstraints() {
		if(constraints == null)
			return 0;
		return constraints.length;
	}

	/**
	 * Returns the constraint at the specified index.
	 * 
	 * @param index index of the variable to be returned
	 * @return the constraint at the specified index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *         {@code (index < 0) || (index >= getNumberOfConstraints())}
	 */
	public double getConstraint(int index) {
		return constraints[index];
	}

}
