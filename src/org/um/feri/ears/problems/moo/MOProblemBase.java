package org.um.feri.ears.problems.moo;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.ProblemBase;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.util.Util;

public abstract class MOProblemBase<Type extends Number> extends ProblemBase<Type>  {

	public MOProblemBase(int numberOfDimensions, int numberOfConstraints) {
		super(numberOfDimensions, numberOfConstraints);
	}
	
	protected int numberOfObjectives;
	protected String file_name;
	
	
	public String getVersion() {
		return version;
	}

	protected List<Objective> functions = new ArrayList<Objective>();
	
	public MOProblemBase(int numberOfDimensions, int numberOfConstraints, int numberOfObjectives)
	{
		super(numberOfDimensions, numberOfConstraints);
		this.numberOfObjectives = numberOfObjectives;
	}
	
	public void addObjective(Objective o) {
		functions.add(o);
    }

	public abstract void evaluate(MOSolutionBase<Type> solution);
	
	public Type getLowerLimit(int i) {
		return lowerLimit.get(i);
	}

	public Type getUpperLimit(int i) {
		return upperLimit.get(i);
	}
	
	public abstract boolean areDimensionsInFeasableInterval(ParetoSolution<Type> ps);
	
	/**
	 * Evaluates a solution
	 *
	 * @param variables The solution to evaluate
	 * @return a double [] with the evaluation results
	 */
	abstract public double[] evaluate(Type ds[]);
	
	abstract public MOSolutionBase<Type> getRandomSolution();
	
	public abstract void evaluateConstraints(MOSolutionBase<Type> solution);
	

	public int getNumberOfObjectives() {
		return numberOfObjectives;
	}

	public void setNumberOfObjectives(int numberOfObjectives) {
		this.numberOfObjectives = numberOfObjectives;
	}

	public String getFileName() {
		return file_name;
	}

	public boolean isFirstBetter(ParetoSolution<Type> x, ParetoSolution<Type> y, QualityIndicator<Type> qi) {
		return x.isFirstBetter(y, qi);
	}
	
	@Override
	public String toString() {

		return "Problem: "+name+ " version: "+version+" dimensions: "+ numberOfDimensions+" objectives: "+ numberOfObjectives + " constraints: "+numberOfConstraints;
	}

}
