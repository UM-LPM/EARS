//  Tanaka.java
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
package org.um.feri.ears.problems.moo.misc;

import java.util.ArrayList;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.moo.functions.Tanaka_F1;
import org.um.feri.ears.problems.moo.functions.Tanaka_F2;
import org.um.feri.ears.util.Util;

public class Tanaka extends DoubleProblem {

    public Tanaka() {

        super("Tanaka", 2, 1, 2, 2);

        referenceSetFileName = "Tanaka";

        upperLimit = new ArrayList<>(numberOfDimensions);
        lowerLimit = new ArrayList<>(numberOfDimensions);

        for (int var = 0; var < numberOfDimensions; var++) {
            lowerLimit.add(10e-5);
            upperLimit.add(Math.PI);
        }

        addObjective(new Tanaka_F1());
        addObjective(new Tanaka_F2());
    }

    @Override
    public void evaluate(NumberSolution<Double> solution) {

        double[] x = Util.toDoubleArray(solution.getVariables());

        double[] obj = new double[objectives.size()];
        for (int i = 0; i < obj.length; i++) {
            obj[i] = objectives.get(i).eval(x);
        }
        solution.setObjectives(obj);

    }

    @Override
    public double[] calculateConstrains(NumberSolution<Double> solution) {
        double[] constraints = new double[numberOfConstraints];

        double[] dv = Util.toDoubleArray(solution.getVariables());

        double x1 = dv[0];
        double x2 = dv[1];

        constraints[0] = (x1 * x1 + x2 * x2 - 1.0 - 0.1 * Math.cos(16.0 * Math.atan(x1 / x2)));
        constraints[1] = -2.0 * ((x1 - 0.5) * (x1 - 0.5) + (x2 - 0.5) * (x2 - 0.5) - 0.5);

        return constraints;
    }

    public double[] evaluate(double[] x) {

        double[] obj = new double[objectives.size()];
        for (int i = 0; i < obj.length; i++) {
            obj[i] = objectives.get(i).eval(x);
        }
        return obj;
    }
}
