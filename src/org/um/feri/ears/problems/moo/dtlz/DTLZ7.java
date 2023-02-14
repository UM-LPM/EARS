//  DTLZ7.java
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

import java.util.ArrayList;


public class DTLZ7 extends DTLZ {

    public DTLZ7(int numberOfObjectives) {
        this(numberOfObjectives + 19, numberOfObjectives);
    }

    public DTLZ7(int numberOfVariables, int numberOfObjectives) {

        super("DTLZ7", numberOfVariables, 0, numberOfObjectives);

        referenceSetFileName = "DTLZ7." + numberOfObjectives + "D";

        upperLimit = new ArrayList<>(numberOfDimensions);
        lowerLimit = new ArrayList<>(numberOfDimensions);


        for (int i = 0; i < numberOfDimensions; i++) {
            lowerLimit.add(0.0);
            upperLimit.add(1.0);
        }

    }

    @Override
    public double[] evaluate(double[] x) {

        double[] f = new double[numberOfObjectives];

        int k = numberOfDimensions - numberOfObjectives + 1;

        double g = 0.0;
        for (int i = numberOfDimensions - k; i < numberOfDimensions; i++) {
            g += x[i];
        }

        g = 1 + (9.0 * g) / k;

        System.arraycopy(x, 0, f, 0, numberOfObjectives - 1);

        double h = 0.0;
        for (int i = 0; i < numberOfObjectives - 1; i++) {
            h += (f[i] / (1.0 + g)) * (1 + Math.sin(3.0 * Math.PI * f[i]));
        }

        h = numberOfObjectives - h;

        f[numberOfObjectives - 1] = (1 + g) * h;

        return f;
    }

}
