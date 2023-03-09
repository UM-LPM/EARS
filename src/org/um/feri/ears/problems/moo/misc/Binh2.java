//  Binh2.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2012 Antonio J. Nebro
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
import org.um.feri.ears.problems.moo.functions.Binh2_F1;
import org.um.feri.ears.problems.moo.functions.Binh2_F2;
import org.um.feri.ears.util.Util;

/**
 * The Binh (2) problem.
 * <p>
 * Properties:
 * <ul>
 *   <li>Connected Pareto set
 *   <li>Convex Pareto front
 *   <li>Constrained
 * </ul>
 * <p>
 * References:
 * <ol>
 *   <li>Binh, T. T. and Korn, U (1997).  "MOBES: A Multiobjective Evolution
 *       Strategy for Constrained Optimization Problems."  Proceedings of the
 *       Third International Conference on Genetic Algorithms (Mendel 97),
 *       pp. 176-182.
 *   <li>Van Veldhuizen, D. A (1999).  "Multiobjective Evolutionary Algorithms:
 *       Classifications, Analyses, and New Innovations."  Air Force Institute
 *       of Technology, Ph.D. Thesis, Appendix B.
 * </ol>
 */
public class Binh2 extends DoubleProblem {


    public Binh2() {

        super("Binh2", 2, 1, 2, 2);

        referenceSetFileName = "Binh2";

        upperLimit = new ArrayList<>(numberOfDimensions);
        lowerLimit = new ArrayList<>(numberOfDimensions);

        lowerLimit.add(0.0);
        upperLimit.add(5.0);
        lowerLimit.add(0.0);
        upperLimit.add(3.0);

        addObjective(new Binh2_F1());
        addObjective(new Binh2_F2());
    }

    /**
     * Evaluates a solution.
     *
     * @param solution The solution to evaluate.
     */
    public void evaluate(NumberSolution<Double> solution) {

        double[] x = Util.toDoubleArray(solution.getVariables());

        double[] obj = new double[objectives.size()];
        for (int i = 0; i < obj.length; i++) {
            obj[i] = objectives.get(i).eval(x);
        }
        solution.setObjectives(obj);
    }

    public double[] evaluate(double[] x) {

        double[] obj = new double[objectives.size()];
        for (int i = 0; i < obj.length; i++) {
            obj[i] = objectives.get(i).eval(x);
        }
        return obj;
    }

    @Override
    public double[] calculateConstrains(NumberSolution<Double> solution) {

        double[] constraints = new double[numberOfConstraints];

        double[] x = Util.toDoubleArray(solution.getVariables());

        constraints[0] = -Math.pow(x[0] - 5.0, 2.0) - Math.pow(x[1], 2.0) + 25.0;
        constraints[1] = Math.pow(x[0] - 8.0, 2.0) + Math.pow(x[1] + 3.0, 2.0) - 7.7;

        return constraints;
    }
}
