package org.um.feri.ears.problems.moo;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.util.Util;

public abstract class IntegerMOProblem extends MOProblemBase<Integer>{

	public IntegerMOProblem(int numberOfDimensions, int numberOfConstraints, int numberOfObjectives) {
		super(numberOfDimensions, numberOfConstraints, numberOfObjectives);

	}
	
	public void evaluate(MOSolutionBase<Integer> solution) {
		double obj[] = evaluate(solution.getVariables());
		solution.setObjectives(obj);
	}
	
	@Override
	public double[] evaluate(Integer[] ds) {
		
		double[] x = new double[numberOfDimensions];
		for (int i = 0; i < numberOfDimensions; i++)
			x[i] = ds[i];
		double obj[] = new double[functions.size()];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = functions.get(i).eval(x);
		}

		return obj;
	}
	
	public double[] evaluate(List<Integer> ds) {
		
		double[] x = new double[numberOfDimensions];
		for (int i = 0; i < numberOfDimensions; i++)
			x[i] = ds.get(i);
		double obj[] = new double[functions.size()];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = functions.get(i).eval(x);
		}

		return obj;
	}

	@Override
	public void evaluateConstraints(MOSolutionBase<Integer> solution) {
	}

	@Override
	public MOSolutionBase<Integer> getRandomSolution() {

		List<Integer> var = new ArrayList<Integer>(numberOfDimensions);
		//Integer[] var = new Integer[numberOfDimensions];
		List<Integer> randomSequence = new ArrayList<>(numberOfDimensions);

		for (int j = 0; j < numberOfDimensions; j++) {
			randomSequence.add(j);
		}

		Util.shuffle(randomSequence);

		for (int i = 0; i < numberOfDimensions; i++) {
			//var[i]= randomSequence.get(i);
			var.add(randomSequence.get(i));
		}

		MOSolutionBase<Integer> sol = new MOSolutionBase<Integer>(var, evaluate(var), upperLimit, lowerLimit);
		evaluateConstraints(sol);

		return sol;

	}

	@Override
	public boolean areDimensionsInFeasableInterval(ParetoSolution<Integer> ps) {
		
		for(MOSolutionBase<Integer> sol : ps)
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

}
