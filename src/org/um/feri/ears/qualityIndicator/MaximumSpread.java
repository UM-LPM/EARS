package org.um.feri.ears.qualityIndicator;

import org.um.feri.ears.problems.moo.ParetoSolution;

/**
 * This class implements the maximum spread metric. 
 * The metric addresses the range of objective function values and takes into account the proximity to the
 * true Pareto front. This metric is applied to measure how well the PF_true is covered by the PF_known.
 * A higher value of MS reflects that a larger area of the PF_true is covered by the PF_known.
 * 
 * Reference:
 * E. Zitzler, L. Thiele, and K. Deb, "Comparison of multiobjective
 * evolutionary algorithms: Empirical results,"" IEEE Trans. Evol. Comput.,
 * vol. 8, no. 2, pp. 173-195, Jun. 2000.
 */
public class MaximumSpread<T extends Number> extends QualityIndicator<T> {

	static final double pow_ = 2.0;
	
	public MaximumSpread(int numObj, String fileName) {
		super(numObj, fileName, (ParetoSolution<T>) getReferenceSet(fileName));
		name = "Maximum Spread";
	}
	
	@Override
	public double evaluate(ParetoSolution<T> paretoFrontApproximation) {
		
		//double[][] front = population.writeObjectivesToMatrix();
		
		double[][] normalizedApproximation;

		normalizedApproximation = QualityIndicatorUtil.getNormalizedFront(paretoFrontApproximation.writeObjectivesToMatrix(), maximumValue, minimumValue);
		

		double sum = 0.0;
		double PFTrueMax, PFTrueMin, PFKnownMax, PFKnownMin;
		
		for (int i = 0; i < numberOfObjectives; i++) {
			PFTrueMax = PFKnownMax = Double.MIN_VALUE;
			PFTrueMin = PFKnownMin = Double.MAX_VALUE;
			
			for (double[] ds : normalizedReference) {
				if(ds[i] > PFTrueMax)
					PFTrueMax = ds[i];
				
				if(ds[i] < PFTrueMin)
					PFTrueMin = ds[i];
			}
			
			for (double[] ds : normalizedApproximation) {
				if(ds[i] > PFKnownMax)
					PFKnownMax = ds[i];
				
				if(ds[i] < PFKnownMin)
					PFKnownMin = ds[i];
			}
			
			sum += Math.pow((Math.min(PFKnownMax, PFTrueMax) - Math.max(PFKnownMin, PFTrueMin)) / (PFTrueMax - PFTrueMin), pow_);
		}
		return Math.sqrt((1.0 / numberOfObjectives) * sum);
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
		return true;
	}

	@Override
	public int compare(ParetoSolution<T> front1, ParetoSolution<T> front2, Double epsilon) {
		return 0;
	}

}
