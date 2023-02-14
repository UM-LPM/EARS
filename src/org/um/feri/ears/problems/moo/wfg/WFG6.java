//  WFG6.java
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
 * This class implements the WFG6 problem
 * Reference: Simon Huband, Luigi Barone, Lyndon While, Phil Hingston
 * A Scalable Multi-objective Test Problem Toolkit.
 * Evolutionary Multi-Criterion Optimization:
 * Third International Conference, EMO 2005.
 * Proceedings, volume 3410 of Lecture Notes in Computer Science
 */
public class WFG6 extends WFG {
    /**
     * Constructor
     * Creates a default WFG1 instance with
     * 2 position-related parameters
     * 4 distance-related parameters
     * and 2 objectives
     */
    public WFG6() {
        this(2, 4, 2);
    }

    public WFG6(int obj) {
        this(obj == 2 ? 4 : (obj - 1) * 2, 20, obj);
    }

    /**
     * Creates a WFG1 problem instance
     *
     * @param k Number of position parameters
     * @param l Number of distance parameters
     * @param m Number of objective functions
     */
    public WFG6(int k, int l, int m) {
        super(k, l, m);

        referenceSetFileName = "WFG6." + m + "D";
        name = "WFG6";

        s = new int[m];
        for (int i = 0; i < m; i++) {
            s[i] = 2 * (i + 1);
        }

        a = new int[m - 1];
        for (int i = 0; i < m - 1; i++) {
            a[i] = 1;
        }
    }

    /**
     * Evaluate
     */
    public double[] evaluate(double[] z) {
        double[] y;

        y = normalise(z);
        y = t1(y, k);
        y = t2(y, k, m);

        double[] result = new double[m];
        double[] x = calculateX(y);
        for (int m = 1; m <= this.m; m++) {
            result[m - 1] = d * x[this.m - 1] + s[m - 1] * (new Shapes()).concave(x, m);
        }

        return result;
    }

    /**
     * WFG6 t1 transformation
     */
    public double[] t1(double[] z, int k) {
        double[] result = new double[z.length];

        System.arraycopy(z, 0, result, 0, k);

        for (int i = k; i < z.length; i++) {
            result[i] = (new Transformations()).sLinear(z[i], (double) 0.35);
        }

        return result;
    }

    /**
     * WFG6 t2 transformation
     */
    public double[] t2(double[] z, int k, int M) {
        double[] result = new double[M];

        for (int i = 1; i <= M - 1; i++) {
            int head = (i - 1) * k / (M - 1) + 1;
            int tail = i * k / (M - 1);
            double[] subZ = subVector(z, head - 1, tail - 1);

            result[i - 1] = (new Transformations()).rNonsep(subZ, k / (M - 1));
        }

        int head = k + 1;
        int tail = z.length;
        int l = z.length - k;

        double[] subZ = subVector(z, head - 1, tail - 1);
        result[M - 1] = (new Transformations()).rNonsep(subZ, l);

        return result;
    }
}
