package org.um.feri.ears.qualityIndicator;

import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.DoubleMOProblem;

public class UniformDistribution extends QualityIndicator{

	public UniformDistribution(DoubleMOProblem problem) {
		super(problem);
		name = "Uniform Distribution";
	}
	
	@Override
	public double evaluate(ParetoSolution front) {
		
		double sigma_share;
		
		return 0;
	}

	@Override
	public boolean isMin() {

		return false;
	}

	@Override
	public IndicatorType getIndicatorType() {
		return QualityIndicator.IndicatorType.Unary;
	}

	@Override
	public boolean requiresReferenceSet() {
		return false;
	}

	@Override
	public int compare(ParetoSolution front1, ParetoSolution front2, Double epsilon) {
		return 0;
	}

}
