package org.um.feri.ears.problems;

import java.util.List;

import org.um.feri.ears.util.Util;

public abstract class ProblemBase <Type> {
	
	public List<Type> upperLimit;
	public List<Type> lowerLimit;
	protected int numberOfDimensions;
	protected boolean minimum = true;
	public int numberOfConstraints = 0;
	public Type max_constraints[]; 
	public Type min_constraints[]; 
	public Type count_constraints[]; 
	public Type sum_constraints[]; 
	public Type normalization_constraints_factor[]; // used for normalization
	protected String name;
	protected String shortName;
	protected String benchmarkName;
	protected String description;
	
	protected String version = "1.0";
	public static final int CONSTRAINED_TYPE_COUNT=1;
	public static final int CONSTRAINED_TYPE_SUM=2;
	public static final int CONSTRAINED_TYPE_NORMALIZATION=3;
	public static int constrained_type = CONSTRAINED_TYPE_SUM;
	public static final int NOT_SHIFTED=0;
	public static final int SHIFTED=1;

	public ProblemBase(int numberOfDimensions, int numberOfConstraints)
	{
		this.numberOfDimensions = numberOfDimensions;
		this.numberOfConstraints = numberOfConstraints;
	}
	/**
	 * Allows to set different name. That can be used in report generating process.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
		
	public boolean isMinimum() {
		return minimum;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public int getNumberOfConstraints() {
		return numberOfConstraints;
	}

	public void setNumberOfConstraints(int numberOfConstraints) {
		this.numberOfConstraints = numberOfConstraints;
	}

	public int countConstraintViolation(double tmp_constrains[]) {
		int c = 0;

		for (int i = 0; i < numberOfConstraints; i++) {
			if (tmp_constrains[i] > 0) {
				c++;
			}
		}
		return c;
	}

	public int getNumberOfDimensions() {
		return numberOfDimensions;
	}

	public void setNumberOfDimensions(int dim) {
		this.numberOfDimensions = dim;
	}
	
	public String getProblemInfoCSV() {
		return "name:"+name+";numberOfDimensions:"+numberOfDimensions+";numberOfConstraints:"+numberOfConstraints+";version:"+version+";";
	}
	
	
	/**
	 * Important! Do not use this function for constrained problems,
	 * if fitness is not reflecting feasibility of the solution.
	 * 
	 * @param a first fitness
	 * @param b second fitness
	 * @return
	 */
	public boolean isFirstBetter(double a, double b) {
		if (minimum)
			return a < b;
		return a > b;
	}

	public String getName() {
		return name;
	}
	
	public String getBenchmarkName() {
		return benchmarkName;
	}
	public void setBenchmarkName(String benchmarkName) {
		this.benchmarkName = benchmarkName;
	}
	@Override
	public abstract String toString();
}
