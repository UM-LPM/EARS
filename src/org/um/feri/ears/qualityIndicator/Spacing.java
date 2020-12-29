package org.um.feri.ears.qualityIndicator;

import org.apache.commons.math3.stat.StatUtils;
import org.um.feri.ears.problems.moo.ParetoSolution;

/**
 * This class implements the Spacing metric.
 * This metric is a value measuring how evenly the nondominated solutions are distributed along the approximation front.
 * S = 0 indicates that all members of the approximation front are equidistantly spaced.
 * <p>
 * Reference:
 * J. R. Schott. Fault Tolerant Design Using Single
 * and Multicriteria Genetic Algorithm Optimization.
 * Master Thesis, Boston, MA: Department of
 * Aeronautics and Astronautics, Massachusetts Institute
 * of Technology, 1995.
 */
public class Spacing<T extends Number> extends QualityIndicator<T> {

    /**
     * Constructor. Creates a new instance of the spacing metric.
     */
    public Spacing(int numObj, String fileName) {
        super(numObj, fileName, (ParetoSolution<T>) getReferenceSet(fileName));
        name = "Spacing";
    }

    @Override
    public double evaluate(ParetoSolution<T> paretoFrontApproximation) {

        double S = 0.0;
        double sum = 0.0;

        /*
         * Stores the normalized approximation set.
         */
        double[][] normalizedApproximation;
        // TODO remove constraint violations
        normalizedApproximation = QualityIndicatorUtil.getNormalizedFront(paretoFrontApproximation.writeObjectivesToMatrix(), maximumValue, minimumValue);

        double[] d = new double[paretoFrontApproximation.size()];


        for (int i = 0; i < paretoFrontApproximation.size(); i++) {
            double min = Double.POSITIVE_INFINITY;
            double[] solutionI = normalizedApproximation[i];
			
			/*if (solutionI.violatesConstraints()) {
				continue;
			}*/

            for (int j = 0; j < paretoFrontApproximation.size(); j++) {
                if (i != j) {
                    double[] solutionJ = normalizedApproximation[j];
					
					/*if (solutionJ.violatesConstraints()) {
						continue;
					}*/

                    try {
                        min = Math.min(min, QualityIndicatorUtil.distance(solutionI, solutionJ));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            d[i] = min;
        }

        double dbar = StatUtils.sum(d) / paretoFrontApproximation.size();

        for (int i = 0; i < paretoFrontApproximation.size(); i++) {
            if (paretoFrontApproximation.get(i).violatesConstraints()) {
                continue;
            }
            sum += Math.pow(d[i] - dbar, 2.0);
        }

        return Math.sqrt(sum / (paretoFrontApproximation.size() - 1));
		
		/*try {
			for (int i = 0; i < population.size(); i++)
			{
				averageDistance += MetricsUtil.distanceToNearestPoint(i, normalizedApproximation);
			}
			averageDistance = averageDistance / population.size();
			for (int i = 0; i < population.size(); i++)
			{
				closestpoint = MetricsUtil.distanceToNearestPoint(i, normalizedApproximation);
				sum+= Math.pow(averageDistance - closestpoint, pow_);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		S = Math.sqrt((1.0/(population.size()-1.0)) * sum);
		return S;*/
    }

    @Override
    public boolean isMin() {
        return true;
    }

    @Override
    public IndicatorType getIndicatorType() {
        return QualityIndicator.IndicatorType.UNARY;
    }

    @Override
    public boolean requiresReferenceSet() {
        return true; // for normalization
    }

    @Override
    public int compare(ParetoSolution<T> front1, ParetoSolution<T> front2, Double epsilon) {
        return 0;
    }

}
