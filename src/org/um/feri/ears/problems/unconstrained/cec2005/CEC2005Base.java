package org.um.feri.ears.problems.unconstrained.cec2005;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.util.random.RNG;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

public abstract class CEC2005Base extends DoubleProblem {

    protected int funcNum;

    static final public int NUM_TEST_FUNC = 25;
    static final public String DEFAULT_FILE_BIAS = "fbias_data.txt";

    // For certain functions, some essential data can be calculated beforehand.
    // Hence, a maximum supported number of dimensions should be specified.
    // Specify the number of dimensions here if you need more.
    static final public int MAX_SUPPORT_DIM = 100;
    static final public double PIx2 = Math.PI * 2.0;

    // Formatter for the number representation
    static final public DecimalFormat scientificFormatter = new DecimalFormat("0.0000000000000000E00");
    static final public DecimalFormat numberFormatter = scientificFormatter;
    static final public DecimalFormat percentageFormatter = new DecimalFormat("0.0000000000");

    // Class variables
    static private double[] m_iSqrt;

    // Shifted global optimum
    protected double[] m_o;

    // Instance variables
    protected double[] m_biases;

    public CEC2005Base(String name, int d, int funcNum) {
        super(name, d,1,1,0);

        this.funcNum = funcNum;

        benchmarkName = "CEC20005";

        if ((funcNum < 1) || (funcNum > 25)) {
            System.err.println("Function number must be between 1 and 25!");
        }

        if (!((d == 2) || (d == 10) || (d == 30) || (d == 50))) {
            System.out.println("\nError: CEC2005 has predifend values only for D = 2, 10, 30, 50.");
        }


        m_biases = new double[NUM_TEST_FUNC];
        m_iSqrt = new double[MAX_SUPPORT_DIM];

        loadRowVectorFromFile(DEFAULT_FILE_BIAS, NUM_TEST_FUNC, m_biases);

        for (int i = 0; i < MAX_SUPPORT_DIM; i++) {
            m_iSqrt[i] = Math.sqrt(((double) i) + 1.0);
        }

        objectiveSpaceOptima[0] = m_biases[funcNum - 1];
    }

    //
    // Basic functions
    //

    // Sphere function
    static public double sphere(double[] x) {

        double sum = 0.0;

        for (int i = 0; i < x.length; i++) {
            sum += x[i] * x[i];
        }

        return (sum);
    }

    // Sphere function with noise
    static public double sphere_noise(double[] x) {

        double sum = 0.0;

        for (int i = 0; i < x.length; i++) {
            sum += x[i] * x[i];
        }

        // NOISE
        // Comment the next line to remove the noise
        sum *= (1.0 + 0.1 * Math.abs(RNG.nextGaussian()));

        return (sum);
    }

    // Schwefel's problem 1.2
    static public double schwefel_102(double[] x) {

        double prev_sum, curr_sum, outer_sum;

        curr_sum = x[0];
        outer_sum = (curr_sum * curr_sum);

        for (int i = 1; i < x.length; i++) {
            prev_sum = curr_sum;
            curr_sum = prev_sum + x[i];
            outer_sum += (curr_sum * curr_sum);
        }

        return (outer_sum);
    }

    // Rosenbrock's function
    static public double rosenbrock(double[] x) {

        double sum = 0.0;

        for (int i = 0; i < (x.length - 1); i++) {
            double temp1 = (x[i] * x[i]) - x[i + 1];
            double temp2 = x[i] - 1.0;
            sum += (100.0 * temp1 * temp1) + (temp2 * temp2);
        }

        return (sum);
    }

    // F2: Rosenbrock's Function -- 2D version
    static public double F2(double x, double y) {
        double temp1 = (x * x) - y;
        double temp2 = x - 1.0;
        return ((100.0 * temp1 * temp1) + (temp2 * temp2));
    }

    // Griewank's function
    static public double griewank(double[] x) {

        double sum = 0.0;
        double product = 1.0;

        for (int i = 0; i < x.length; i++) {
            sum += ((x[i] * x[i]) / 4000.0);
            product *= Math.cos(x[i] / m_iSqrt[i]);
        }

        return (sum - product + 1.0);
    }

    // F8: Griewank's Function -- 1D version
    static public double F8(double x) {
        return (((x * x) / 4000.0) - Math.cos(x) + 1.0);
    }

    // Ackley's function
    static public double ackley(double[] x) {

        double sum1 = 0.0;
        double sum2 = 0.0;

        for (int i = 0; i < x.length; i++) {
            sum1 += (x[i] * x[i]);
            sum2 += (Math.cos(PIx2 * x[i]));
        }

        return (-20.0 * Math.exp(-0.2 * Math.sqrt(sum1 / ((double) x.length))) - Math.exp(sum2 / ((double) x.length)) + 20.0 + Math.E);
    }

    // Round function
    // 0. Use the Matlab version for rounding numbers
    static public double myRound(double x) {
        return (Math.signum(x) * Math.round(Math.abs(x)));
    }

    // 1. "o" is provided
    static public double myXRound(double x, double o) {
        return ((Math.abs(x - o) < 0.5) ? x : (myRound(2.0 * x) / 2.0));
    }

    // 2. "o" is not provided
    static public double myXRound(double x) {
        return ((Math.abs(x) < 0.5) ? x : (myRound(2.0 * x) / 2.0));
    }

    // Rastrigin's function
    static public double rastrigin(double[] x) {

        double sum = 0.0;

        for (int i = 0; i < x.length; i++) {
            sum += (x[i] * x[i]) - (10.0 * Math.cos(PIx2 * x[i])) + 10.0;
        }

        return (sum);
    }

    // Non-Continuous Rastrigin's function
    static public double rastriginNonCont(double[] x) {

        double sum = 0.0;
        double currX;

        for (int i = 0; i < x.length; i++) {
            currX = myXRound(x[i]);
            sum += (currX * currX) - (10.0 * Math.cos(PIx2 * currX)) + 10.0;
        }

        return (sum);
    }

    // Weierstrass function
    static public double weierstrass(double[] x) {
        return (weierstrass(x, 0.5, 3.0, 20));
    }

    static public double weierstrass(double[] x, double a, double b, int Kmax) {

        double sum1 = 0.0;
        for (int i = 0; i < x.length; i++) {
            for (int k = 0; k <= Kmax; k++) {
                sum1 += Math.pow(a, k) * Math.cos(PIx2 * Math.pow(b, k) * (x[i] + 0.5));
            }
        }

        double sum2 = 0.0;
        for (int k = 0; k <= Kmax; k++) {
            sum2 += Math.pow(a, k) * Math.cos(PIx2 * Math.pow(b, k) * (0.5));
        }

        return (sum1 - sum2 * ((double) (x.length)));
    }

    // F8F2
    static public double F8F2(double[] x) {

        double sum = 0.0;

        for (int i = 1; i < x.length; i++) {
            sum += F8(F2(x[i - 1], x[i]));
        }
        sum += F8(F2(x[x.length - 1], x[0]));

        return (sum);
    }

    // Scaffer's F6 function
    static public double ScafferF6(double x, double y) {
        double temp1 = x * x + y * y;
        double temp2 = Math.sin(Math.sqrt(temp1));
        double temp3 = 1.0 + 0.001 * temp1;
        return (0.5 + ((temp2 * temp2 - 0.5) / (temp3 * temp3)));
    }

    // Expanded Scaffer's F6 function
    static public double EScafferF6(double[] x) {

        double sum = 0.0;

        for (int i = 1; i < x.length; i++) {
            sum += ScafferF6(x[i - 1], x[i]);
        }
        sum += ScafferF6(x[x.length - 1], x[0]);

        return (sum);
    }

    // Non-Continuous Expanded Scaffer's F6 function
    static public double EScafferF6NonCont(double[] x) {

        double sum = 0.0;
        double prevX, currX;

        currX = myXRound(x[0]);
        for (int i = 1; i < x.length; i++) {
            prevX = currX;
            currX = myXRound(x[i]);
            sum += ScafferF6(prevX, currX);
        }
        prevX = currX;
        currX = myXRound(x[0]);
        sum += ScafferF6(prevX, currX);

        return (sum);
    }

    // Elliptic
    static public double elliptic(double[] x) {

        double sum = 0.0;
        double a = 1e6;

        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(a, (((double) i) / ((double) (x.length - 1)))) * x[i] * x[i];
        }

        return (sum);
    }

    // Hybrid composition
    static public double hybrid_composition(double[] x, HybridCompositionData job) {

        int num_func = job.num_func;
        int num_dim = job.num_dim;

        // Get the raw weights
        double wMax = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < num_func; i++) {
            double sumSqr = 0.0;
            shift(job.z[i], x, job.o[i]);
            for (int j = 0; j < num_dim; j++) {
                sumSqr += (job.z[i][j] * job.z[i][j]);
            }
            job.w[i] = Math.exp(-1.0 * sumSqr / (2.0 * num_dim * job.sigma[i] * job.sigma[i]));
            if (wMax < job.w[i])
                wMax = job.w[i];
        }

        // Modify the weights
        double wSum = 0.0;
        double w1mMaxPow = 1.0 - Math.pow(wMax, 10.0);
        for (int i = 0; i < num_func; i++) {
            if (job.w[i] != wMax) {
                job.w[i] *= w1mMaxPow;
            }
            wSum += job.w[i];
        }

        // Normalize the weights
        for (int i = 0; i < num_func; i++) {
            job.w[i] /= wSum;
        }

        double sumF = 0.0;
        for (int i = 0; i < num_func; i++) {
            for (int j = 0; j < num_dim; j++) {
                job.z[i][j] /= job.lambda[i];
            }
            rotate(job.zM[i], job.z[i], job.M[i]);
            sumF +=
                    job.w[i] *
                            (
                                    job.C * job.basicFunc(i, job.zM[i]) / job.fmax[i] +
                                            job.biases[i]
                            );
        }
        return (sumF);
    }

    //
    // Elementary operations
    //

    // Shift
    static public void shift(double[] results, double[] x, double[] o) {
        for (int i = 0; i < x.length; i++) {
            results[i] = x[i] - o[i];
        }
    }

    // Rotate
    static public void rotate(double[] results, double[] x, double[][] matrix) {
        xA(results, x, matrix);
    }

    //
    // Matrix & vector operations
    //

    // (1xD) row vector * (Dx1) column vector = (1) scalar
    static public double xy(double[] x, double[] y) {
        double result = 0.0;
        for (int i = 0; i < x.length; i++) {
            result += (x[i] * y[i]);
        }

        return (result);
    }

    // (1xD) row vector * (DxD) matrix = (1xD) row vector
    static public void xA(double[] result, double[] x, double[][] A) {
        for (int i = 0; i < result.length; i++) {
            result[i] = 0.0;
            for (int j = 0; j < result.length; j++) {
                result[i] += (x[j] * A[j][i]);
            }
        }
    }

    // (DxD) matrix * (Dx1) column vector = (Dx1) column vector
    static public void Ax(double[] result, double[][] A, double[] x) {
        for (int i = 0; i < result.length; i++) {
            result[i] = 0.0;
            for (int j = 0; j < result.length; j++) {
                result[i] += (A[i][j] * x[j]);
            }
        }
    }

    static public void loadRowVectorFromFile(String file, int columns, double[] row) {
        try {
            BufferedReader brSrc = new BufferedReader(new InputStreamReader(CEC2005Base.class.getResourceAsStream("/org/um/feri/ears/problems/unconstrained/cec2005/input_data/" + file)));
            //BufferedReader brSrc = new BufferedReader(new FileReader(file));
            loadRowVector(brSrc, columns, row);
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    static public void loadRowVector(BufferedReader brSrc, int columns, double[] row) throws Exception {
        String stToken;
        StringTokenizer stTokenizer = new StringTokenizer(brSrc.readLine());
        for (int i = 0; i < columns; i++) {
            stToken = stTokenizer.nextToken();
            row[i] = Double.parseDouble(stToken);
        }
    }

    static public void loadColumnVectorFromFile(String file, int rows, double[] column) {
        try {
            BufferedReader brSrc = new BufferedReader(new InputStreamReader(CEC2005Base.class.getResourceAsStream("/org/um/feri/ears/problems/unconstrained/cec2005/input_data/" + file)));
            //BufferedReader brSrc = new BufferedReader(new FileReader(file));
            loadColumnVector(brSrc, rows, column);
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    static public void loadColumnVector(BufferedReader brSrc, int rows, double[] column) throws Exception {
        String stToken;
        for (int i = 0; i < rows; i++) {
            StringTokenizer stTokenizer = new StringTokenizer(brSrc.readLine());
            stToken = stTokenizer.nextToken();
            column[i] = Double.parseDouble(stToken);
        }
    }

    static public void loadNMatrixFromFile(String file, int N, int rows, int columns, double[][][] matrix) {
        try {
            BufferedReader brSrc = new BufferedReader(new InputStreamReader(CEC2005Base.class.getResourceAsStream("/org/um/feri/ears/problems/unconstrained/cec2005/input_data/" + file)));
            //BufferedReader brSrc = new BufferedReader(new FileReader(file));
            for (int i = 0; i < N; i++) {
                loadMatrix(brSrc, rows, columns, matrix[i]);
            }
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    static public void loadMatrixFromFile(String file, int rows, int columns, double[][] matrix) {
        try {

            BufferedReader brSrc = new BufferedReader(new InputStreamReader(CEC2005Base.class.getResourceAsStream("/org/um/feri/ears/problems/unconstrained/cec2005/input_data/" + file)));
            //BufferedReader brSrc = new BufferedReader(new FileReader(file));
            loadMatrix(brSrc, rows, columns, matrix);
            brSrc.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    static public void loadMatrix(BufferedReader brSrc, int rows, int columns, double[][] matrix) throws Exception {
        for (int i = 0; i < rows; i++) {
            loadRowVector(brSrc, columns, matrix[i]);
        }
    }

    public abstract class HybridCompositionData {

        // Number of basic functions
        public int num_func;
        // Number of dimensions
        public int num_dim;

        // Predefined constant
        public double C;
        // Coverage range for each basic function
        public double[] sigma;
        // Biases for each basic function
        public double[] biases;
        // Stretch / compress each basic function
        public double[] lambda;
        // Estimated fmax
        public double[] fmax;
        // Shift global optimum for each basic function
        public double[][] o;
        // Linear transformation matrix for each basic function
        public double[][][] M;

        // Working areas to avoid memory allocation operations
        public double[] w;
        public double[][] z;
        public double[][] zM;

        public HybridCompositionData() {
            // Nothing
            // This class is just a place holder.
        }

        public abstract double basicFunc(int func_no, double[] x);
    }

}
