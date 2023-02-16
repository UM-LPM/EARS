//  DTLZ2.java
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


public class DTLZ2 extends DTLZ {


    public DTLZ2(int numberOfObjectives) {
        this(numberOfObjectives + 9, numberOfObjectives);
    }

    public DTLZ2(int numberOfVariables, int numberOfObjectives) {

        super("DTLZ2", numberOfVariables, numberOfObjectives, 0);

        referenceSetFileName = "DTLZ2." + numberOfObjectives + "D";

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
        for (int i = numberOfDimensions - k; i < numberOfDimensions; i++)
            g += (x[i] - 0.5) * (x[i] - 0.5);

        for (int i = 0; i < numberOfObjectives; i++)
            f[i] = 1.0 + g;

        for (int i = 0; i < numberOfObjectives; i++) {
            for (int j = 0; j < numberOfObjectives - (i + 1); j++)
                f[i] *= Math.cos(x[j] * 0.5 * Math.PI);
            if (i != 0) {
                int aux = numberOfObjectives - (i + 1);
                f[i] *= Math.sin(x[aux] * 0.5 * Math.PI);
            }
        }
        return f;
    }
}
