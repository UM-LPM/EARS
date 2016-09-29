//  ZDT6.java
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
package org.um.feri.ears.problems.moo.zdt;

import java.util.ArrayList;

import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.Objective;

public class ZDT6  extends ZDT{
	
	public ZDT6() {
		this(10); 
	}
	public ZDT6(Integer numberOfVariables) {
	     
		super(numberOfVariables,0,2);

		file_name = "ZDT6";
		name = "ZDT6";

		upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);


		for (int i = 0; i < numberOfDimensions; i++) {
			lowerLimit.add(0.0);
			upperLimit.add(1.0);
		}


		this.addObjective(new ZDT6_F1());
		this.addObjective(new ZDT6_F2());
	}

	@Override
	public void evaluateConstraints(MOSolutionBase<Double> solution) {
	}
	
	public class ZDT6_F1 extends Objective{

		@Override
		public double eval(double[] ds) {
			
			double f = 1.0 - Math.exp(-4.0 * ds[0])
					* Math.pow(Math.sin(6.0 * Math.PI * ds[0]), 6.0);
			
			return f;
		}
	}
	
	public class ZDT6_F2 extends Objective{

		@Override
		public double eval(double[] ds) {
			
			double f = 1.0 - Math.exp(-4.0 * ds[0])
					* Math.pow(Math.sin(6.0 * Math.PI * ds[0]), 6.0);

			double g = 0.0;
			for (int i = 1; i < numberOfDimensions; i++) {
				g += ds[i];
			}
			g = 1.0 + 9.0 * Math.pow(g / (numberOfDimensions - 1), 0.25);

			double h = 1.0 - Math.pow(f / g, 2.0);
			
			return g * h;
		}
	}

}