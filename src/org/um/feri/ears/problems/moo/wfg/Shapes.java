//  Shapes.java
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

package org.um.feri.ears.problems.moo.wfg;

/**
 * Class implementing shape functions for wfg benchmark
 * Reference: Simon Huband, Luigi Barone, Lyndon While, Phil Hingston
 * A Scalable Multi-objective Test Problem Toolkit.
 * Evolutionary Multi-Criterion Optimization:
 * Third International Conference, EMO 2005.
 * Proceedings, volume 3410 of Lecture Notes in Computer Science
 */
public class Shapes {

    /**
     * Calculate a linear shape
     */
    public double linear(double[] x, int m) {
        double result = (double) 1.0;
        int M = x.length;

        for (int i = 1; i <= M - m; i++) {
            result *= x[i - 1];
        }

        if (m != 1) {
            result *= (1 - x[M - m]);
        }

        return result;
    }

    /**
     * Calculate a convex shape
     */
    public double convex(double[] x, int m) {
        double result = (double) 1.0;
        int M = x.length;

        for (int i = 1; i <= M - m; i++) {
            result *= (1 - Math.cos(x[i - 1] * Math.PI * 0.5));
        }

        if (m != 1) {
            result *= (1 - Math.sin(x[M - m] * Math.PI * 0.5));
        }

        return result;
    }

    /**
     * Calculate a concave shape
     */
    public double concave(double[] x, int m) {
        double result = (double) 1.0;
        int M = x.length;

        for (int i = 1; i <= M - m; i++) {
            result *= Math.sin(x[i - 1] * Math.PI * 0.5);
        }

        if (m != 1) {
            result *= Math.cos(x[M - m] * Math.PI * 0.5);
        }

        return result;
    }

    /**
     * Calculate a mixed shape
     */
    public double mixed(double[] x, int A, double alpha) {
        double tmp;
        tmp =
                (double) Math.cos((double) 2.0 * A * (double) Math.PI * x[0] + (double) Math.PI * (double) 0.5);
        tmp /= (2.0 * (double) A * Math.PI);

        return (double) Math.pow(((double) 1.0 - x[0] - tmp), alpha);
    }

    /**
     * Calculate a disc shape
     */
    public double disc(double[] x, int A, double alpha, double beta) {
        double tmp;
        tmp = (double) Math.cos((double) A * Math.pow(x[0], beta) * Math.PI);

        return (double) 1.0 - (double) Math.pow(x[0], alpha) * (double) Math.pow(tmp, 2.0);
    }
}