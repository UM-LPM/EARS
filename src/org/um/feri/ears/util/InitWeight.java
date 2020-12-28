package org.um.feri.ears.util;

import java.util.Arrays;

public class InitWeight {

    public static double[][] generate(int numberOfObjectives, int numberOfPoints, boolean isSwap) {
        Weight W = new Weight();


        int U = (int) Math.floor(Math.pow(numberOfPoints, (1.0 / (numberOfObjectives - 1)))) - 2;
        int M = 0;
        while (M < numberOfPoints) {
            U++;
            M = noweight(U, 0, numberOfObjectives);
        }
        W.W = new double[numberOfObjectives][M];

        double[] V = new double[numberOfObjectives];
        Arrays.fill(V, 0);

        W = setweight(W, V, U, 0, numberOfObjectives, numberOfObjectives);
        for (int i = 0; i < numberOfObjectives; i++) {
            for (int j = 0; j < numberOfPoints; j++) {
                W.W[i][j] = W.W[i][j] / (U + 0.0);
            }
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            for (int j = 0; j < numberOfPoints; j++) {
                if (W.W[i][j] < 1e-5)
                    W.W[i][j] = 1e-5;
            }
        }

        if (isSwap) {
            double[][] swap = new double[numberOfPoints][numberOfObjectives];
            for (int i = 0; i < numberOfObjectives; i++) {
                for (int j = 0; j < numberOfPoints; j++) {
                    swap[j][i] = W.W[i][j];
                }
            }

            return swap;
        }
        return W.W;
    }

    private static Weight setweight(Weight w, double[] vv, int unit, int sum, int objdim, int dim) {

        double[] v = Arrays.copyOf(vv, vv.length);

        if (dim == objdim) {
            v = new double[objdim];
            Arrays.fill(v, 0);
        }

        if (dim == 1) {
            v[0] = unit - sum;
            for (int i = 0; i < objdim; i++) {
                w.W[i][w.c] = v[i];
            }
            w.c++;
            return w;
        }

        for (int i = 0; i <= (unit - sum); i++) {
            v[dim - 1] = i;
            w = setweight(w, v, unit, sum + i, objdim, dim - 1);
        }
        return w;
    }

    private static int noweight(int unit, int sum, int dim) {
        int M = 0;

        if (dim == 1) {
            M = 1;
            return M;
        }

        for (int i = 0; i <= (unit - sum); i++) {
            M = M + noweight(unit, sum + i, dim - 1);
        }
        return M;
    }

    private static class Weight {
        public Weight() {
        }

        public double[][] W;
        public int c;
    }

}
