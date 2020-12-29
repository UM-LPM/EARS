package org.um.feri.ears.qualityIndicator;

import org.um.feri.ears.problems.moo.ParetoSolution;

public class UniformDistribution<T extends Number> extends QualityIndicator<T>{

	public UniformDistribution(int num_obj) {
		super(num_obj);
		name = "Uniform Distribution";
	}
	
	@Override
	public double evaluate(ParetoSolution<T> paretoFrontApproximation) {
		
		double sigma_share;
		
		return 0;
	}

	@Override
	public boolean isMin() {

		return false;
	}

	@Override
	public IndicatorType getIndicatorType() {
		return QualityIndicator.IndicatorType.UNARY;
	}

	@Override
	public boolean requiresReferenceSet() {
		return false;
	}

	@Override
	public int compare(ParetoSolution<T> front1, ParetoSolution<T> front2, Double epsilon) {
		return 0;
	}

}
