//  EpsilonBin.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.um.feri.ears.qualityIndicator;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

/**
 * This class implements the binary epsilon additive indicator as proposed in
 * E. Zitzler, E. Thiele, L. Laummanns, M., Fonseca, C., and Grunert da Fonseca.
 * V (2003): Performance Assesment of Multiobjective Optimizers: An Analysis and
 * Review. The code is the a Java version of the original metric implementation
 * by Eckart Zitzler.
 * It can be used also as a command line program just by typing
 * $java jmetal.qualityIndicator.Epsilon <solutionFrontFile> <trueFrontFile> <numberOfObjectives>
 */
public class EpsilonBin<T extends Number> extends QualityIndicator<T>{
	
	public EpsilonBin(int num_obj) {
		super(num_obj);
		name = "EpsilonBin";
	}

	@Override
	public double evaluate(ParetoSolution<T> population) {
		return 0;
	}

	@Override
	public IndicatorType getIndicatorType() {
		return IndicatorType.Binary;
	}

	@Override
	public boolean isMin() {
		return false;
	}

	@Override
	public boolean requiresReferenceSet() {
		return false;
	}

	@Override
	public int compare(ParetoSolution<T> front1, ParetoSolution<T> front2, Double epsilon) {
		//normalize both fronts
		
		//TODO add +1 to all normalized values if additive method [1,2]
		double[][] front = ArrayUtils.addAll(front1.writeObjectivesToMatrix(), front2.writeObjectivesToMatrix());
		
		maximumValue = MetricsUtil.getMaximumValues(front, numberOfObjectives);
		minimumValue = MetricsUtil.getMinimumValues(front, numberOfObjectives);
		
		double[][] normalizedFront1 = MetricsUtil.getNormalizedFront(front1.writeObjectivesToMatrix(), maximumValue, minimumValue);
		double[][] normalizedFront2 = MetricsUtil.getNormalizedFront(front2.writeObjectivesToMatrix(), maximumValue, minimumValue);
		
		double eps1,eps2;
		
		int method = 0; // 0 - additive, 1 - multiplicative 
		
		eps1 = getEpsilon(normalizedFront1, normalizedFront2, method);
		eps2 = getEpsilon(normalizedFront2, normalizedFront1, method);
		
		// if equal or incomparable
		if((eps1 == method && eps2 == method) || (eps1 > method && eps2 > method))
			return 0;
		if(eps1 <= method && eps2 > method)
			return -1;
		else
			return 1;
	}

	private double getEpsilon(double[][] normalizedFront1, double[][] normalizedFront2, int method) {
		
		int i, j, k;
		double eps, eps_j = 0.0, eps_k = 0.0, eps_temp;

		if (method == 0)
			eps = Double.MIN_VALUE;
		else
			eps = 0;

		for (i = 0; i < normalizedFront2.length; i++) {
			for (j = 0; j < normalizedFront1.length; j++) {
				for (k = 0; k < numberOfObjectives; k++) {
					switch (method) {
					case 0:
						eps_temp = normalizedFront1[j][k] - normalizedFront2[i][k];
						break;
					default:
						if ((normalizedFront2[i][k] < 0 && normalizedFront1[j][k] > 0)
								|| (normalizedFront2[i][k] > 0 && normalizedFront1[j][k] < 0)
								|| (normalizedFront2[i][k] == 0 || normalizedFront1[j][k] == 0)) {
							System.err.println("error in data file");
							System.exit(0);
						}
						eps_temp = normalizedFront1[j][k] / normalizedFront2[i][k];
						break;
					}
					if (k == 0)
						eps_k = eps_temp;
					else if (eps_k < eps_temp)
						eps_k = eps_temp;
				}
				if (j == 0)
					eps_j = eps_k;
				else if (eps_j > eps_k)
					eps_j = eps_k;
			}
			if (i == 0)
				eps = eps_j;
			else if (eps < eps_j)
				eps = eps_j;
		}
		return eps;
	}

}
