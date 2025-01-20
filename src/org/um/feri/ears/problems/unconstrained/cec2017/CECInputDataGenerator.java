package org.um.feri.ears.problems.unconstrained.cec2017;

import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

public class CECInputDataGenerator {

    public static double[] generateShiftVector(int dimension, double lowerBound, double upperBound) {
        double[] shiftVector = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            shiftVector[i] = lowerBound + (upperBound - lowerBound) * RNG.nextDouble();
        }
        return shiftVector;
    }

    public static int[] generateShuffleArray(int dimension) {
        int[] shuffleArray = new int[dimension];
        for (int i = 0; i < dimension; i++) {
            shuffleArray[i] = i + 1;
        }
        RNG.shuffle(shuffleArray);
        return shuffleArray;
    }

    /**
     * The rotation matrix for each subcomponent is generated from standard normally distributed entries by Gram-Schmidt ortho-normalization with condition number c that is equal to 1 or 2.
     * @param dimension
     * @param conditionNumber
     * @return
     */
    public static double[][] generateRotationMatrix(int dimension, double conditionNumber) {
        if (dimension <= 1) {
            return new double[][]{{1.0}};
        }

        // Step 1: Generate orthonormal matrices P and Q using Gram-Schmidt
        double[][] A = generateRandomMatrix(dimension);
        double[][] P = gramSchmidt(A);

        A = generateRandomMatrix(dimension);
        double[][] Q = gramSchmidt(A);

        // Step 2: Create diagonal matrix D
        double[] u = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            u[i] = RNG.nextDouble(); // Random values in [0, 1)
        }
        double minU = min(u);
        double maxU = max(u);
        double[] d = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            d[i] = Math.pow(conditionNumber, (u[i] - minU) / (maxU - minU));
        }
        double[][] D = createDiagonalMatrix(d);

        // Step 3: Combine P, D, and Q to form the final matrix M
        double[][] temp = multiplyMatrices(P, D);
        double[][] M = multiplyMatrices(temp, Q);

        return M;
    }

    private static double[][] generateRandomMatrix(int dimension) {
        double[][] matrix = new double[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                matrix[i][j] = RNG.nextGaussian();
            }
        }
        return matrix;
    }

    private static double[][] gramSchmidt(double[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        double[][] q = new double[n][m];
        double[][] r = new double[m][m];

        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                q[i][j] = matrix[i][j];
            }
            for (int i = 0; i < j; i++) {
                r[i][j] = dotProduct(column(q, i), column(matrix, j));
                for (int k = 0; k < n; k++) {
                    q[k][j] -= r[i][j] * q[k][i];
                }
            }
            double norm = Math.sqrt(dotProduct(column(q, j), column(q, j)));
            for (int i = 0; i < n; i++) {
                q[i][j] /= norm;
            }
            r[j][j] = norm;
        }
        return q;
    }

    private static double dotProduct(double[] vec1, double[] vec2) {
        double sum = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            sum += vec1[i] * vec2[i];
        }
        return sum;
    }

    private static double[] column(double[][] matrix, int col) {
        double[] column = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            column[i] = matrix[i][col];
        }
        return column;
    }

    private static double[][] createDiagonalMatrix(double[] diag) {
        int n = diag.length;
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i][i] = diag[i];
        }
        return matrix;
    }

    private static double min(double[] array) {
        double min = Double.MAX_VALUE;
        for (double v : array) {
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    private static double max(double[] array) {
        double max = Double.MIN_VALUE;
        for (double v : array) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    private static double[][] multiplyMatrices(double[][] A, double[][] B) {
        int n = A.length;
        int m = B[0].length;
        int p = B.length;
        double[][] result = new double[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < p; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        RNG.setSeed(123);
        int numberOfDimensions = 5;
        //TODO store data directly in input_data
        generateShuffleFiles(numberOfDimensions);
        generateRotationData(numberOfDimensions);
    }

    private static void generateRotationData(int numberOfDimensions) {
        for (int funcNum = 1; funcNum <= 29; funcNum++) {
            if(funcNum == 12 || funcNum == 13 || funcNum == 17 || funcNum == 29) {
                continue;
            }

            int compNum = 10;
            double [][] rotation;

            if (funcNum <= 19) {
                rotation = new double [numberOfDimensions][numberOfDimensions];
                rotation = generateRotationMatrix(numberOfDimensions, 1.0);
            }
            else {
                rotation = new double [compNum * numberOfDimensions][numberOfDimensions];

                for (int i = 0; i < compNum; i++) {

                    double[][] currentRotation = generateRotationMatrix(numberOfDimensions, 1.0);

                    for (int row = 0; row < numberOfDimensions; row++) {
                        rotation[i * numberOfDimensions + row] = currentRotation[row];
                    }
                }
            }

            String filename = "M_" + funcNum + "_D" + numberOfDimensions + ".txt";

            //convert array to string
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < rotation.length; i++) {
                for (int j = 0; j < rotation[i].length; j++) {
                    sb.append(rotation[i][j]);
                    if (j < rotation[i].length - 1) {
                        sb.append(" ");
                    }
                }
                if (i < rotation.length - 1) {
                    sb.append("\n");
                }
            }

            String currentDir = System.getProperty("user.dir");
            String path = currentDir + "/input_data/" + filename;

            Util.writeToFile(path, sb.toString());
        }
    }

    private static void generateShuffleFiles(int dimensions) {
        for (int funcNum = 1; funcNum <= 29; funcNum++) {
            if(funcNum == 12 || funcNum == 13 || funcNum == 17 || funcNum == 29) {
                continue;
            }
            int[] shuffleArray = generateShuffleArray(dimensions);
            String filename = "shuffle_data_" + funcNum + "_D" + dimensions + ".txt";

            //convert array to string
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < shuffleArray.length; j++) {
                sb.append(shuffleArray[j]);
                if (j < shuffleArray.length - 1) {
                    sb.append(" ");
                }
            }

            String currentDir = System.getProperty("user.dir");
            String path = currentDir + "/input_data/" + filename;

            Util.writeToFile(path, sb.toString());
        }
    }
}
