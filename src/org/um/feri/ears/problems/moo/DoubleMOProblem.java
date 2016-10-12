//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.problems.moo;

import java.util.ArrayList;
import java.util.List;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.ProblemBase;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.util.Util;

public abstract class DoubleMOProblem extends MOProblemBase<Double> {
	
	//protected List<Objective> functions = new ArrayList<Objective>();
	
	public DoubleMOProblem(int numberOfDimensions, int numberOfConstraints, int numberOfObjectives)
	{
		super(numberOfDimensions, numberOfConstraints, numberOfObjectives);
	}

	public void evaluate(MOSolutionBase<Double> solution) {
		List<Double> decisionVariables = solution.getVariables();
		double obj[] = evaluate(decisionVariables.toArray(new Double[decisionVariables.size()]));
		solution.setObjectives(obj);
	}
	
	@Override
	public boolean areDimensionsInFeasableInterval(ParetoSolution<Double> ps) {
		
		for(MOSolutionBase<Double> sol : ps)
		{
			for (int i=0; i<numberOfDimensions; i++) {
				if (sol.getValue(i) < lowerLimit.get(i))
					return false;
				if (sol.getValue(i) > upperLimit.get(i))
					return false;
			}
		}

		return true;
	}
	
	@Override
	public double[] evaluate(Double[] ds) {
		double[] x = new double[numberOfDimensions];
		for (int i = 0; i < numberOfDimensions; i++)
			x[i] = ds[i];
		double obj[] = new double[functions.size()];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = functions.get(i).eval(x);
		}
		return obj;
	}
	
	@Override
	public MOSolutionBase<Double> getRandomSolution() {

		//List<Double> var=new ArrayList<Double>(numberOfDimensions);
		Double[] var = new Double[numberOfDimensions];
		for (int j = 0; j < numberOfDimensions; j++) {
			var[j] = Util.nextDouble(lowerLimit.get(j), upperLimit.get(j));
		}
		MOSolutionBase<Double> sol = new MOSolutionBase<Double>(var, evaluate(var), upperLimit, lowerLimit);
		evaluateConstraints(sol);

		return sol;

	}
	
	public abstract void evaluateConstraints(MOSolutionBase<Double> solution);

	public boolean isFirstBetter(ParetoSolution<Double> x, ParetoSolution<Double> y, QualityIndicator<Double> qi) {
		return x.isFirstBetter(y, qi);
	}

}
