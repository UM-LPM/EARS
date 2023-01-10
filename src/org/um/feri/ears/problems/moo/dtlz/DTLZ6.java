//  DTLZ6.java
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
package org.um.feri.ears.problems.moo.dtlz;

import org.um.feri.ears.problems.NumberSolution;

import java.util.ArrayList;


public class DTLZ6 extends DTLZ{
	
	public DTLZ6(int numberOfObjectives) {
		this(numberOfObjectives + 9, numberOfObjectives);
	}
	
	public DTLZ6(int numberOfVariables, int numberOfObjectives) {
	     
		super(numberOfVariables,0,numberOfObjectives);

		fileName = "DTLZ6."+numberOfObjectives+"D";
		name = "DTLZ6";

		upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);


		for (int i = 0; i < numberOfDimensions; i++) {
			lowerLimit.add(0.0);
			upperLimit.add(1.0);
		}

	}

	@Override
	public void evaluateConstraints(NumberSolution<Double> solution) {
	}
	
	@Override
	public double[] evaluate(Double ds[]) {

		double[] theta = new double[numberOfObjectives - 1];

		double[] f = new double[numberOfObjectives];
		double[] x = new double[numberOfDimensions];

		int k = numberOfDimensions - numberOfObjectives + 1;

		for (int i = 0; i < numberOfDimensions; i++) {
			x[i] = ds[i];
		}

		double g = 0.0;
		for (int i = numberOfDimensions - k; i < numberOfDimensions; i++) {
			g += java.lang.Math.pow(x[i], 0.1);
		}

		double t = java.lang.Math.PI / (4.0 * (1.0 + g));
		theta[0] = x[0] * java.lang.Math.PI / 2;
		for (int i = 1; i < (numberOfObjectives - 1); i++) {
			theta[i] = t * (1.0 + 2.0 * g * x[i]);
		}

		for (int i = 0; i < numberOfObjectives; i++) {
			f[i] = 1.0 + g;
		}

		for (int i = 0; i < numberOfObjectives; i++) {
			for (int j = 0; j < numberOfObjectives - (i + 1); j++) {
				f[i] *= java.lang.Math.cos(theta[j]);
			}
			if (i != 0) {
				int aux = numberOfObjectives - (i + 1);
				f[i] *= java.lang.Math.sin(theta[aux]);
			}
		}

		return f;
	}

}