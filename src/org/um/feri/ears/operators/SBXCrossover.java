//  SBXCrossover.java
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

package org.um.feri.ears.operators;

import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.random.RNG;


/**
 * This class allows to apply a SBX crossover operator using two parent solutions.
 */
public class SBXCrossover implements CrossoverOperator<NumberProblem<Double>, NumberSolution<Double>>{
  /**
   * EPS defines the minimum difference allowed between real values
   */
	private static final double EPS = 1.0e-14;

	private static final double ETA_C_DEFAULT = 20.0;
	private Double crossoverProbability = 0.9;
	private double distributionIndex = ETA_C_DEFAULT;

	
	public SBXCrossover()
	{
		this(0.9, 20.0);
	}
  /** 
   * Constructor
   * Create a new SBX crossover operator with a default
   * index given by <code>DEFAULT_INDEX_CROSSOVER</code>
   */
	public SBXCrossover(Double crossoverProbability, double distributionIndex) {

		this.crossoverProbability = crossoverProbability;
		this.distributionIndex = distributionIndex;
	}
    
  /**
   * Perform the crossover operation. 
   * @param probability Crossover probability
   * @param parent1 The first parent
   * @param parent2 The second parent
   * @return An array containing the two offsprings
   */
	public NumberSolution<Double>[] doCrossover(double probability, NumberSolution<Double> parent1, NumberSolution<Double> parent2, NumberProblem<Double> problem) {

		NumberSolution<Double>[] offSpring = new NumberSolution[2];

		offSpring[0] = new NumberSolution<>(parent1);
		offSpring[1] = new NumberSolution<>(parent2);

		int i;
		double rand;
		double y1, y2, yL, yu;
		double c1, c2;
		double alpha, beta, betaq;
		double valueX1, valueX2;

		int numberOfVariables = parent1.getVariables().size();

		if (RNG.nextDouble() <= probability) {
			for (i = 0; i < numberOfVariables; i++) {
				valueX1 = parent1.getValue(i);
				valueX2 = parent2.getValue(i);
				if (RNG.nextDouble() <= 0.5) {
					if (java.lang.Math.abs(valueX1 - valueX2) > EPS) {

						if (valueX1 < valueX2) {
							y1 = valueX1;
							y2 = valueX2;
						} else {
							y1 = valueX2;
							y2 = valueX1;
						}

						yL = problem.getLowerLimit(i);
						yu = problem.getUpperLimit(i);
						rand = RNG.nextDouble();
						beta = 1.0 + (2.0 * (y1 - yL) / (y2 - y1));
						alpha = 2.0 - java.lang.Math.pow(beta, -(distributionIndex + 1.0));

						if (rand <= (1.0 / alpha)) {
							betaq = java.lang.Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
						} else {
							betaq = java.lang.Math.pow((1.0 / (2.0 - rand * alpha)), (1.0 / (distributionIndex + 1.0)));
						}

						c1 = 0.5 * ((y1 + y2) - betaq * (y2 - y1));
						beta = 1.0 + (2.0 * (yu - y2) / (y2 - y1));
						alpha = 2.0 - java.lang.Math.pow(beta,-(distributionIndex + 1.0));

						if (rand <= (1.0 / alpha)) {
							betaq = java.lang.Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
						} else {
							betaq = java.lang.Math.pow((1.0 / (2.0 - rand * alpha)), (1.0 / (distributionIndex + 1.0)));
						}

						c2 = 0.5 * ((y1 + y2) + betaq * (y2 - y1));

						if (c1 < yL)
							c1 = yL;

						if (c2 < yL)
							c2 = yL;

						if (c1 > yu)
							c1 = yu;

						if (c2 > yu)
							c2 = yu;

						if (RNG.nextDouble() <= 0.5) {
							offSpring[0].setValue(i, c2);
							offSpring[1].setValue(i, c1);
						} else {
							offSpring[0].setValue(i, c1);
							offSpring[1].setValue(i, c2);
						}
					} else {
						offSpring[0].setValue(i, valueX1);
						offSpring[1].setValue(i, valueX2);
					}
				} else {
					offSpring[0].setValue(i, valueX2);
					offSpring[1].setValue(i, valueX1);
				}
			}
		}

		return offSpring;
	}

	@Override
	public NumberSolution<Double>[] execute(NumberSolution<Double>[] parents, NumberProblem<Double> problem) {

		if (parents.length != 2) {
			return null;
		}

		NumberSolution<Double>[] offSpring;
		offSpring = doCrossover(crossoverProbability, parents[0], parents[1], problem);
		return offSpring;
	}
	@Override
	public void setCurrentSolution(NumberSolution<Double> current) {

	}
}
