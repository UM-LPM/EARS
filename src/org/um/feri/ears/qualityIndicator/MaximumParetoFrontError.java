package org.um.feri.ears.qualityIndicator;

import org.um.feri.ears.problems.moo.ParetoSolution;

/**
 * This class implements the Maximum Pareto Front Error indicator.
 * This indicator measures a worst
 * case scenario in term of the largest distance in the objective
 * space between any individual in the approximation front and
 * the corresponding closest vector in the true Pareto front.
 * 
 * Reference: D. A. Van Veldhuizen, "Multiobjective evolutionary algorithms: 
 * Classifications, analyses, and new innovations," Ph.D. dissertation, 
 * Air Force Inst. Technol., Wright-Patterson AFB, OH, 1999.
 */
public class MaximumParetoFrontError<T extends Number> extends QualityIndicator<T>{

	static final double pow_ = 2.0; // pow. This is the pow used for the distances

	public MaximumParetoFrontError(int num_obj, String file_name) {
		super(num_obj, file_name, getReferenceSet(file_name));
		name = "Maximum Pareto Front Error";
	}
	
	@Override
	public double evaluate(ParetoSolution<T> population) {

		/**
		 * Stores the normalized approximation set.
		 */
		double[][] normalizedApproximation;

		normalizedApproximation = MetricsUtil.getNormalizedFront(population.writeObjectivesToMatrix(), maximumValue, minimumValue);
		
		double max = 0.0;

		try {
			for (double[] aNormalizedAproximation : normalizedApproximation){

				max = Math.max(max,(MetricsUtil.distanceToNearestPoint(aNormalizedAproximation, normalizedReference)));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return max;
	}

	@Override
	public IndicatorType getIndicatorType() {
		return QualityIndicator.IndicatorType.UNARY;
	}

	@Override
	public boolean isMin() {
		return true;
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
