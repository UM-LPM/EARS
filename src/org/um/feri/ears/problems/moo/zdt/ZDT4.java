//  ZDT4.java
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

import org.um.feri.ears.problems.moo.Objective;

public class ZDT4 extends ZDT{
	
	public ZDT4() {
		this(30); 
	}
	public ZDT4(Integer numberOfVariables) {
	     
		super(numberOfVariables,0,2);

		referenceSetFileName = "ZDT4";
		name = "ZDT4";
		
		upperLimit = new ArrayList<>(numberOfDimensions);
		lowerLimit = new ArrayList<>(numberOfDimensions);


		lowerLimit.add(0.0);
		upperLimit.add(1.0);
		
		for (int i = 1; i < numberOfDimensions; i++) {
			lowerLimit.add(-5.0);
			upperLimit.add(5.0);
		}


		this.addObjective(new ZDT4_F1());
		this.addObjective(new ZDT4_F2());
	}
	
	public class ZDT4_F1 extends Objective{

		@Override
		public double eval(double[] ds) {
			return ds[0];
		}
	}
	
	public class ZDT4_F2 extends Objective{

		@Override
		public double eval(double[] ds) {
			
			double g = 0.0;
			for (int i = 1; i < numberOfDimensions; i++) {
				g += Math.pow(ds[i], 2.0) - 10.0 * Math.cos(4.0 * Math.PI * ds[i]);
			}
			g += 1.0 + 10.0 * (numberOfDimensions - 1);

			double h = 1.0 - Math.sqrt(ds[0] / g);
			
			return g * h;
		}
	}

}
