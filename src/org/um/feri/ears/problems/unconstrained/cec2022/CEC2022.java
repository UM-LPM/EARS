package org.um.feri.ears.problems.unconstrained.cec2022;

import org.um.feri.ears.problems.DoubleProblem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

/**
 * Base class for all CEC2022 benchmark problems.
 *
 * Based on the C++ code from:
 * CEC2022 GitHub: <a href="https://github.com/P-N-Suganthan/2022-SO-BO">CEC2022-BoundConstrained</a>
 *
 * Works for dimensions 10, 20.
 */
public abstract class CEC2022 extends DoubleProblem {

    private static final double E  = Math.E;
    private static final double PI = Math.PI;

    protected int funcNum;

    private double[][] rotation; // Rotation matrix
    private double[][] oShift;   // Optimum shift vectors
    private int[][]    shuffle;  // Shuffle indices (hybrid functions)
    private double[]   y, z;    // Temporary decision vectors

    // Component counts for composition functions
    //   F9  → cf01 (5 components)
    //   F10 → cf02 (3 components)
    //   F11 → cf06 (5 components)
    //   F12 → cf07 (6 components)
    private static final int[] CF_COMP_NUM = {0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 3, 5, 6};

    private static long[] seeds;

    /**
     * CEC2022 base class constructor.
     *
     * @param name    Short name of the function (e.g. "F01")
     * @param d       Number of dimensions (10 or 20)
     * @param funcNum Function number 1–12
     */
    public CEC2022(String name, int d, int funcNum) {
        super("CEC2022" + name, d, 1, 1, 0);

        this.funcNum = funcNum;
        if (funcNum < 1 || funcNum > 12) {
            System.err.println("Function number must be between 1 and 12!");
        }

        benchmarkName = "CEC2022";
        shortName = "F" + funcNum;

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions,  100.0));

        if (d != 10 && d != 20) {
            System.err.println("Error: CEC2022 test functions are only defined for D = 10, 20.");
        }

        this.y = new double[numberOfDimensions];
        this.z = new double[numberOfDimensions];

        loadData();

        decisionSpaceOptima[0] = oShift[0];

        switch (funcNum) {
            case 1: objectiveSpaceOptima[0] = 300; break;
            case 2: objectiveSpaceOptima[0] = 400; break;
            case 3: objectiveSpaceOptima[0] = 600; break;
            case 4: objectiveSpaceOptima[0] = 800; break;
            case 5: objectiveSpaceOptima[0] = 900; break;
            case 6: objectiveSpaceOptima[0] = 1800; break;
            case 7: objectiveSpaceOptima[0] = 2000; break;
            case 8: objectiveSpaceOptima[0] = 2200; break;
            case 9: objectiveSpaceOptima[0] = 2300; break;
            case 10: objectiveSpaceOptima[0] = 2400; break;
            case 11: objectiveSpaceOptima[0] = 2600; break;
            case 12: objectiveSpaceOptima[0] = 2700; break;
        }
    }

    public static void main(String[] args) {

        int[] dims = {10, 20};

        for (int d : dims) {
            System.out.println("========================================");
            System.out.printf("  Testing CEC2022 benchmark  (D = %d)%n", d);
            System.out.println("========================================");
            System.out.printf("%-6s  %-12s  %-12s  %-10s%n",
                    "Func", "Expected", "Got", "Error");
            System.out.println("----------------------------------------");

            CEC2022[] problems = {
                    new F1(d), new F2(d), new F3(d),  new F4(d),
                    new F5(d), new F6(d), new F7(d),  new F8(d),
                    new F9(d), new F10(d), new F11(d), new F12(d)
            };

            double[] expected = {
                    300, 400, 600, 800,
                    900, 1800, 2000, 2200,
                    2300, 2400, 2600, 2700
            };

            for (int i = 0; i < problems.length; i++) {
                CEC2022 p = problems[i];
                // The global optimum is the shift vector stored in decisionSpaceOptima[0]
                double[] optimum = p.getDecisionSpaceOptima()[0];
                double result    = p.eval(optimum);
                double error     = Math.abs(result - expected[i]);

                System.out.printf("%-6s  %-12.4f  %-12.4f  %-10.2e%n",
                        p.getShortName(), expected[i], result, error);
            }
            System.out.println();
        }
    }


    private void loadData() {
        // Hybrid functions (6,7,8) need shuffle data; composition functions
        // (9-12) are purely rotation+shift.
        switch (funcNum) {
            case 1: case 2: case 3: case 4: case 5:   // Basic functions
            case 9: case 10: case 11: case 12:          // Composition functions
                loadRotationData();
                loadShiftData();
                break;
            case 6: case 7: case 8:                    // Hybrid functions
                loadRotationData();
                loadShiftData();
                loadShuffleData();
                break;
        }

        loadSeedData();
    }

    private void loadSeedData() {
        try {
            seeds = new long[1000];

            String fn = "input_data/Rand_Seeds.txt";
            InputStream dataFile = CEC2022.class.getResourceAsStream(fn);
            Scanner fileReader = new Scanner(dataFile);
            fileReader.useLocale(Locale.US);

            for (int i = 0; i < seeds.length; i++)
                seeds[i] = (long) fileReader.nextDouble();

            fileReader.close();
        } catch (Exception ex) {
            System.err.println("Error loading seed data: " + ex.getMessage());
        }
    }

    /**
     * Returns the seed for a given function, dimension, and run.
     *
     * Mirrors the MATLAB formula from the CEC2022 benchmark:
     *   seed_ind = (problem_size/10 * func_no * Runs + run_id) - Runs
     *   seed_ind = mod(seed_ind, 1000) + 1        (1-based in MATLAB)
     *   run_seed = Rand_Seeds(seed_ind)
     *
     * @param funcNo      Function number (1–12)
     * @param numberOfDimensions Number of dimensions (10 or 20)
     * @param runId       Current run index, 1-based (1–Runs)
     * @param totalRuns   Total number of runs
     * @return            The seed for this function/dimension/run combination
     */
    public static long getSeed(int funcNo, int numberOfDimensions, int runId, int totalRuns) {
        int seedInd = (numberOfDimensions / 10 * funcNo * totalRuns + (runId + 1)) - totalRuns;
        seedInd = seedInd % 1000;
        return seeds[seedInd];
    }

    private void loadRotationData() {
        try {
            int compNum = (funcNum >= 9) ? CF_COMP_NUM[funcNum] : 1;
            rotation = new double[compNum * numberOfDimensions][numberOfDimensions];

            String fn = "input_data/M_" + funcNum + "_D" + numberOfDimensions + ".txt";
            InputStream dataFile = CEC2022.class.getResourceAsStream(fn);
            Scanner fileReader = new Scanner(dataFile);
            fileReader.useLocale(Locale.US);

            for (int i = 0; i < rotation.length; i++)
                for (int j = 0; j < rotation[i].length; j++)
                    rotation[i][j] = fileReader.nextDouble();
            fileReader.close();
        } catch (Exception ex) {
            System.err.println("Error loading rotation data for F" + funcNum + ": " + ex.getMessage());
        }
    }

    private void loadShiftData() {
        try {
            int compNum = (funcNum >= 9) ? CF_COMP_NUM[funcNum] : 1;
            oShift = new double[compNum][numberOfDimensions];

            String fn = "input_data/shift_data_" + funcNum + ".txt";
            InputStream dataFile = CEC2022.class.getResourceAsStream(fn);
            Scanner fileReader = new Scanner(dataFile);
            fileReader.useLocale(Locale.US);

            for (int i = 0; i < oShift.length; i++) {
                for (int j = 0; j < oShift[i].length; j++)
                    oShift[i][j] = fileReader.nextDouble();
                if (fileReader.hasNextLine()) fileReader.nextLine();
            }
            fileReader.close();
        } catch (Exception ex) {
            System.err.println("Error loading shift data for F" + funcNum + ": " + ex.getMessage());
        }
    }

    private void loadShuffleData() {
        try {
            shuffle = new int[1][numberOfDimensions];

            String fn = "input_data/shuffle_data_" + funcNum + "_D" + numberOfDimensions + ".txt";
            InputStream dataFile = CEC2022.class.getResourceAsStream(fn);
            Scanner fileReader = new Scanner(dataFile);

            for (int j = 0; j < shuffle[0].length; j++)
                shuffle[0][j] = fileReader.nextInt();
            fileReader.close();
        } catch (Exception ex) {
            System.err.println("Error loading shuffle data for F" + funcNum + ": " + ex.getMessage());
        }
    }

    public int getFuncNum() {
        return funcNum;
    }

    /** Shifts x by the optimum stored at row funcPos of oShift into xShift. */
    private void shiftFunc(double[] x, double[] xShift, int funcPos) {
        for (int i = 0; i < x.length; i++)
            xShift[i] = x[i] - oShift[funcPos][i];
    }

    /** Rotates x using the rotation block at row funcPos*nx of rotation into xRotate. */
    private void rotateFunc(double[] x, double[] xRotate, int funcPos) {
        int nx = x.length;
        for (int i = 0; i < nx; i++) {
            xRotate[i] = 0.0;
            for (int j = 0; j < nx; j++)
                xRotate[i] += x[j] * rotation[i + nx * funcPos][j];
        }
    }

    /**
     * Shift-and-rotate transformation (sr_func in the C++ reference).
     * Results are written into {@code xShiftRotate}.
     */
    private void shiftRotateFunc(double[] x, double[] xShiftRotate,
                                 double shiftRate, int sFlag, int rFlag, int funcPos) {
        if (sFlag == 1) {
            if (rFlag == 1) {
                shiftFunc(x, y, funcPos);
                for (int i = 0; i < x.length; i++) y[i] *= shiftRate;
                rotateFunc(y, xShiftRotate, funcPos);
            } else {
                shiftFunc(x, xShiftRotate, funcPos);
                for (int i = 0; i < x.length; i++) xShiftRotate[i] *= shiftRate;
            }
        } else {
            if (rFlag == 1) {
                for (int i = 0; i < x.length; i++) y[i] = x[i] * shiftRate;
                rotateFunc(y, xShiftRotate, funcPos);
            } else {
                for (int i = 0; i < x.length; i++) xShiftRotate[i] = x[i] * shiftRate;
            }
        }
    }

    /**
     * Computes the weighted combination of component fitness values for a
     * composition function.
     *
     * @param x      Decision vector
     * @param nx     Number of dimensions
     * @param delta  Spread / range parameters (one per component)
     * @param bias   Bias values (one per component)
     * @param fit    Pre-computed raw fitness from each component
     * @param cfNum  Number of components
     * @return Composite objective value
     */
    private double cfCal(double[] x, int nx, double[] delta, double[] bias,
                         double[] fit, int cfNum) {
        final double INF = 1.0e99;
        double[] w = new double[cfNum];
        double wMax = 0.0, wSum = 0.0;

        for (int i = 0; i < cfNum; i++) {
            fit[i] += bias[i];
            w[i] = 0.0;
            for (int j = 0; j < nx; j++)
                w[i] += Math.pow(x[j] - oShift[i][j], 2.0);
            if (w[i] != 0.0)
                w[i] = Math.pow(1.0 / w[i], 0.5) *
                        Math.exp(-w[i] / 2.0 / nx / Math.pow(delta[i], 2.0));
            else
                w[i] = INF;
            if (w[i] > wMax) wMax = w[i];
        }

        for (int i = 0; i < cfNum; i++) wSum += w[i];

        if (wMax == 0.0) {
            for (int i = 0; i < cfNum; i++) w[i] = 1.0;
            wSum = cfNum;
        }

        double result = 0.0;
        for (int i = 0; i < cfNum; i++)
            result += w[i] / wSum * fit[i];
        return result;
    }

    /** High Conditioned Elliptic Function. */
    protected double ellipsFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 1.0, sFlag, rFlag, funcPos);
        double result = 0.0;
        for (int i = 0; i < nx; i++)
            result += Math.pow(10.0, 6.0 * i / (nx - 1)) * z[i] * z[i];
        return result;
    }

    /** Bent Cigar Function. */
    protected double bentCigarFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 1.0, sFlag, rFlag, funcPos);
        double result = z[0] * z[0];
        for (int i = 1; i < nx; i++)
            result += 1.0e6 * z[i] * z[i];
        return result;
    }

    /** Discus Function. */
    protected double discusFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 1.0, sFlag, rFlag, funcPos);
        double result = 1.0e6 * z[0] * z[0];
        for (int i = 1; i < nx; i++)
            result += z[i] * z[i];
        return result;
    }

    /** Rosenbrock's Function. */
    protected double rosenbrockFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 2.048 / 100.0, sFlag, rFlag, funcPos);
        z[0] += 1.0;
        double result = 0.0;
        for (int i = 0; i < nx - 1; i++) {
            z[i + 1] += 1.0;
            double tmp1 = z[i] * z[i] - z[i + 1];
            double tmp2 = z[i] - 1.0;
            result += 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
        }
        return result;
    }

    /** Ackley's Function. */
    protected double ackleyFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 1.0, sFlag, rFlag, funcPos);
        double sum1 = 0.0, sum2 = 0.0;
        for (int i = 0; i < nx; i++) {
            sum1 += z[i] * z[i];
            sum2 += Math.cos(2.0 * PI * z[i]);
        }
        sum1 = -0.2 * Math.sqrt(sum1 / nx);
        sum2 /= nx;
        return E - 20.0 * Math.exp(sum1) - Math.exp(sum2) + 20.0;
    }

    /** Griewank's Function. */
    protected double griewankFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 600.0 / 100.0, sFlag, rFlag, funcPos);
        double s = 0.0, p = 1.0;
        for (int i = 0; i < nx; i++) {
            s += z[i] * z[i];
            p *= Math.cos(z[i] / Math.sqrt(1.0 + i));
        }
        return 1.0 + s / 4000.0 - p;
    }

    /** Rastrigin's Function. */
    protected double rastriginFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 5.12 / 100.0, sFlag, rFlag, funcPos);
        double result = 0.0;
        for (int i = 0; i < nx; i++)
            result += z[i] * z[i] - 10.0 * Math.cos(2.0 * PI * z[i]) + 10.0;
        return result;
    }

    /** Modified Schwefel's Function. */
    protected double schwefelFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 1000.0 / 100.0, sFlag, rFlag, funcPos);
        double result = 0.0;
        for (int i = 0; i < nx; i++) {
            z[i] += 4.209687462275036e+002;
            if (z[i] > 500.0) {
                double mod = z[i] % 500.0;
                result -= (500.0 - mod) * Math.sin(Math.sqrt(500.0 - mod));
                double tmp = (z[i] - 500.0) / 100.0;
                result += tmp * tmp / nx;
            } else if (z[i] < -500.0) {
                double mod = Math.abs(z[i]) % 500.0;
                result -= (-500.0 + mod) * Math.sin(Math.sqrt(500.0 - mod));
                double tmp = (z[i] + 500.0) / 100.0;
                result += tmp * tmp / nx;
            } else {
                result -= z[i] * Math.sin(Math.sqrt(Math.abs(z[i])));
            }
        }
        result += 4.189828872724338e+002 * nx;
        return result;
    }

    /** Griewank-Rosenbrock Function (f8f2). */
    protected double grieRosenFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 5.0 / 100.0, sFlag, rFlag, funcPos);
        z[0] += 1.0;
        double result = 0.0;
        for (int i = 0; i < nx - 1; i++) {
            z[i + 1] += 1.0;
            double tmp1 = z[i] * z[i] - z[i + 1];
            double tmp2 = z[i] - 1.0;
            double temp = 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
            result += temp * temp / 4000.0 - Math.cos(temp) + 1.0;
        }
        double tmp1 = z[nx - 1] * z[nx - 1] - z[0];
        double tmp2 = z[nx - 1] - 1.0;
        double temp = 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
        result += temp * temp / 4000.0 - Math.cos(temp) + 1.0;
        return result;
    }

    /** Expanded Scaffer's F6 Function. */
    protected double escaffer6Func(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 1.0, sFlag, rFlag, funcPos);
        double result = 0.0;
        for (int i = 0; i < nx - 1; i++) {
            double temp1 = Math.sin(Math.sqrt(z[i] * z[i] + z[i + 1] * z[i + 1]));
            temp1 = temp1 * temp1;
            double temp2 = 1.0 + 0.001 * (z[i] * z[i] + z[i + 1] * z[i + 1]);
            result += 0.5 + (temp1 - 0.5) / (temp2 * temp2);
        }
        double temp1 = Math.sin(Math.sqrt(z[nx - 1] * z[nx - 1] + z[0] * z[0]));
        temp1 = temp1 * temp1;
        double temp2 = 1.0 + 0.001 * (z[nx - 1] * z[nx - 1] + z[0] * z[0]);
        result += 0.5 + (temp1 - 0.5) / (temp2 * temp2);
        return result;
    }

    /** HappyCat Function. Original global optimum: [-1, -1, ..., -1]. */
    protected double happycatFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 5.0 / 100.0, sFlag, rFlag, funcPos);
        double alpha = 1.0 / 8.0;
        double r2 = 0.0, sumZ = 0.0;
        for (int i = 0; i < nx; i++) {
            z[i] -= 1.0;
            r2   += z[i] * z[i];
            sumZ += z[i];
        }
        return Math.pow(Math.abs(r2 - nx), 2.0 * alpha) + (0.5 * r2 + sumZ) / nx + 0.5;
    }

    /** HGBat Function. Original global optimum: [-1, -1, ..., -1]. */
    protected double hgbatFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 5.0 / 100.0, sFlag, rFlag, funcPos);
        double alpha = 1.0 / 4.0;
        double r2 = 0.0, sumZ = 0.0;
        for (int i = 0; i < nx; i++) {
            z[i] -= 1.0;
            r2   += z[i] * z[i];
            sumZ += z[i];
        }
        return Math.pow(Math.abs(Math.pow(r2, 2.0) - Math.pow(sumZ, 2.0)), 2.0 * alpha)
                + (0.5 * r2 + sumZ) / nx + 0.5;
    }

    /** Schaffer's F7 Function. */
    protected double schafferF7Func(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 1.0, sFlag, rFlag, funcPos);
        double result = 0.0;
        for (int i = 0; i < nx - 1; i++) {
            // Note: uses the pre-shift y[] values as per the C++ reference
            double si  = Math.pow(y[i] * y[i] + y[i + 1] * y[i + 1], 0.5);
            double tmp = Math.sin(50.0 * Math.pow(si, 0.2));
            result += Math.pow(si, 0.5) + Math.pow(si, 0.5) * tmp * tmp;
        }
        return result * result / (nx - 1) / (nx - 1);
    }

    /**
     * Non-Continuous Rotated Rastrigin's Function (step_rastrigin).
     * Applies a discretisation step to y before shift-rotating.
     */
    protected double stepRastriginFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        // Discretise using the pre-shifted y[] (populated by any previous sr_func call,
        // but here we mirror the C++ logic which reads y[] before calling sr_func again).
        for (int i = 0; i < nx; i++) {
            if (Math.abs(y[i] - oShift[0][i]) > 0.5)
                y[i] = oShift[0][i] + Math.floor(2.0 * (y[i] - oShift[0][i]) + 0.5) / 2.0;
        }
        shiftRotateFunc(x, z, 5.12 / 100.0, sFlag, rFlag, funcPos);
        double result = 0.0;
        for (int i = 0; i < nx; i++)
            result += z[i] * z[i] - 10.0 * Math.cos(2.0 * PI * z[i]) + 10.0;
        return result;
    }

    /** Levy Function. */
    protected double levyFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 1.0, sFlag, rFlag, funcPos);
        double[] w = new double[nx];
        for (int i = 0; i < nx; i++)
            w[i] = 1.0 + (z[i] - 0.0) / 4.0;

        double term1 = Math.pow(Math.sin(PI * w[0]), 2.0);
        double term3 = Math.pow(w[nx - 1] - 1.0, 2.0) *
                (1.0 + Math.pow(Math.sin(2.0 * PI * w[nx - 1]), 2.0));
        double sum = 0.0;
        for (int i = 0; i < nx - 1; i++) {
            double wi = w[i];
            sum += Math.pow(wi - 1.0, 2.0) * (1.0 + 10.0 * Math.pow(Math.sin(PI * wi + 1.0), 2.0));
        }
        return term1 + sum + term3;
    }

    /** Zakharov Function. */
    protected double zakharovFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 1.0, sFlag, rFlag, funcPos);
        double sum1 = 0.0, sum2 = 0.0;
        for (int i = 0; i < nx; i++) {
            sum1 += z[i] * z[i];
            sum2 += 0.5 * (i + 1) * z[i];
        }
        return sum1 + Math.pow(sum2, 2.0) + Math.pow(sum2, 4.0);
    }

    /** Katsuura Function. */
    protected double katsuuraFunc(double[] x, int nx, int sFlag, int rFlag, int funcPos) {
        shiftRotateFunc(x, z, 5.0 / 100.0, sFlag, rFlag, funcPos);
        double result = 1.0;
        double tmp3 = Math.pow(nx, 1.2);
        for (int i = 0; i < nx; i++) {
            double temp = 0.0;
            for (int j = 1; j <= 32; j++) {
                double tmp1 = Math.pow(2.0, j);
                double tmp2 = tmp1 * z[i];
                temp += Math.abs(tmp2 - Math.floor(tmp2 + 0.5)) / tmp1;
            }
            result *= Math.pow(1.0 + (i + 1) * temp, 10.0 / tmp3);
        }
        double tmp1 = 10.0 / nx / nx;
        return result * tmp1 - tmp1;
    }

    /**
     * Hybrid Function 1 – hf02 (N=3): BentCigar + HGBat + Rastrigin
     * Proportions: 0.4 | 0.4 | 0.2
     */
    protected double hf02(double[] x, int nx, int sFlag, int rFlag) {
        double[] Gp  = {0.4, 0.4, 0.2};
        int      cfNum = 3;
        int[]    G    = new int[cfNum];
        int[]    Gnx  = computeGroupSizes(Gp, nx, cfNum, G);
        double[] fit  = new double[cfNum];

        shiftRotateFunc(x, z, 1.0, sFlag, rFlag, 0);
        double[] hy = applyShuffleToZ(nx);

        fit[0] = bentCigarFunc(subArray(hy, G[0], Gnx[0]), Gnx[0], 0, 0, 0);
        fit[1] = hgbatFunc    (subArray(hy, G[1], Gnx[1]), Gnx[1], 0, 0, 0);
        fit[2] = rastriginFunc(subArray(hy, G[2], Gnx[2]), Gnx[2], 0, 0, 0);

        double result = 0.0;
        for (double f : fit) result += f;
        return result;
    }

    /**
     * Hybrid Function 2 – hf10 (N=6): HGBat + Katsuura + Ackley + Rastrigin + Schwefel + SchafferF7
     * Proportions: 0.1 | 0.2 | 0.2 | 0.2 | 0.1 | 0.2
     */
    protected double hf10(double[] x, int nx, int sFlag, int rFlag) {
        double[] Gp  = {0.1, 0.2, 0.2, 0.2, 0.1, 0.2};
        int      cfNum = 6;
        int[]    G    = new int[cfNum];
        int[]    Gnx  = computeGroupSizes(Gp, nx, cfNum, G);
        double[] fit  = new double[cfNum];

        shiftRotateFunc(x, z, 1.0, sFlag, rFlag, 0);
        double[] hy = applyShuffleToZ(nx);

        fit[0] = hgbatFunc    (subArray(hy, G[0], Gnx[0]), Gnx[0], 0, 0, 0);
        fit[1] = katsuuraFunc (subArray(hy, G[1], Gnx[1]), Gnx[1], 0, 0, 0);
        fit[2] = ackleyFunc   (subArray(hy, G[2], Gnx[2]), Gnx[2], 0, 0, 0);
        fit[3] = rastriginFunc(subArray(hy, G[3], Gnx[3]), Gnx[3], 0, 0, 0);
        fit[4] = schwefelFunc (subArray(hy, G[4], Gnx[4]), Gnx[4], 0, 0, 0);
        fit[5] = schafferF7Func(subArray(hy, G[5], Gnx[5]), Gnx[5], 0, 0, 0);

        double result = 0.0;
        for (double f : fit) result += f;
        return result;
    }

    /**
     * Hybrid Function 3 – hf06 (N=5): Katsuura + HappyCat + GrieRosen + Schwefel + Ackley
     * Proportions: 0.3 | 0.2 | 0.2 | 0.1 | 0.2
     */
    protected double hf06(double[] x, int nx, int sFlag, int rFlag) {
        double[] Gp  = {0.3, 0.2, 0.2, 0.1, 0.2};
        int      cfNum = 5;
        int[]    G    = new int[cfNum];
        int[]    Gnx  = computeGroupSizes(Gp, nx, cfNum, G);
        double[] fit  = new double[cfNum];

        shiftRotateFunc(x, z, 1.0, sFlag, rFlag, 0);
        double[] hy = applyShuffleToZ(nx);

        fit[0] = katsuuraFunc (subArray(hy, G[0], Gnx[0]), Gnx[0], 0, 0, 0);
        fit[1] = happycatFunc (subArray(hy, G[1], Gnx[1]), Gnx[1], 0, 0, 0);
        fit[2] = grieRosenFunc(subArray(hy, G[2], Gnx[2]), Gnx[2], 0, 0, 0);
        fit[3] = schwefelFunc (subArray(hy, G[3], Gnx[3]), Gnx[3], 0, 0, 0);
        fit[4] = ackleyFunc   (subArray(hy, G[4], Gnx[4]), Gnx[4], 0, 0, 0);

        double result = 0.0;
        for (double f : fit) result += f;
        return result;
    }

    /**
     * Composition Function 1 – cf01 (N=5):
     * Rosenbrock | Elliptic | BentCigar | Discus | Elliptic (no rotation)
     * delta: {10,20,30,40,50}  bias: {0,200,300,100,400}
     */
    protected double cf01(double[] x, int nx, int rFlag) {
        int      cfNum = 5;
        double[] delta = {10, 20, 30, 40, 50};
        double[] bias  = {0, 200, 300, 100, 400};
        double[] fit   = new double[cfNum];

        fit[0] = rosenbrockFunc(x, nx, 1, rFlag, 0); fit[0] = 10000 * fit[0] / 1e4;
        fit[1] = ellipsFunc    (x, nx, 1, rFlag, 1); fit[1] = 10000 * fit[1] / 1e10;
        fit[2] = bentCigarFunc (x, nx, 1, rFlag, 2); fit[2] = 10000 * fit[2] / 1e30;
        fit[3] = discusFunc    (x, nx, 1, rFlag, 3); fit[3] = 10000 * fit[3] / 1e10;
        fit[4] = ellipsFunc    (x, nx, 1, 0,     4); fit[4] = 10000 * fit[4] / 1e10;

        return cfCal(x, nx, delta, bias, fit, cfNum);
    }

    /**
     * Composition Function 2 – cf02 (N=3):
     * Schwefel (no rotation) | Rastrigin | HGBat
     * delta: {20,10,10}  bias: {0,200,100}
     */
    protected double cf02(double[] x, int nx, int rFlag) {
        int      cfNum = 3;
        double[] delta = {20, 10, 10};
        double[] bias  = {0, 200, 100};
        double[] fit   = new double[cfNum];

        fit[0] = schwefelFunc (x, nx, 1, 0,     0);
        fit[1] = rastriginFunc(x, nx, 1, rFlag, 1);
        fit[2] = hgbatFunc    (x, nx, 1, rFlag, 2);

        return cfCal(x, nx, delta, bias, fit, cfNum);
    }

    /**
     * Composition Function 3 – cf06 (N=5):
     * EScaffer6 | Schwefel | Griewank | Rosenbrock | Rastrigin
     * delta: {20,20,30,30,20}  bias: {0,200,300,400,200}
     */
    protected double cf06(double[] x, int nx, int rFlag) {
        int      cfNum = 5;
        double[] delta = {20, 20, 30, 30, 20};
        double[] bias  = {0, 200, 300, 400, 200};
        double[] fit   = new double[cfNum];

        fit[0] = escaffer6Func (x, nx, 1, rFlag, 0); fit[0] = 10000 * fit[0] / 2e7;
        fit[1] = schwefelFunc  (x, nx, 1, rFlag, 1);
        fit[2] = griewankFunc  (x, nx, 1, rFlag, 2); fit[2] = 1000  * fit[2] / 100;
        fit[3] = rosenbrockFunc(x, nx, 1, rFlag, 3);
        fit[4] = rastriginFunc (x, nx, 1, rFlag, 4); fit[4] = 10000 * fit[4] / 1e3;

        return cfCal(x, nx, delta, bias, fit, cfNum);
    }

    /**
     * Composition Function 4 – cf07 (N=6):
     * HGBat | Rastrigin | Schwefel | BentCigar | Elliptic | EScaffer6
     * delta: {10,20,30,40,50,60}  bias: {0,300,500,100,400,200}
     */
    protected double cf07(double[] x, int nx, int rFlag) {
        int      cfNum = 6;
        double[] delta = {10, 20, 30, 40, 50, 60};
        double[] bias  = {0, 300, 500, 100, 400, 200};
        double[] fit   = new double[cfNum];

        fit[0] = hgbatFunc     (x, nx, 1, rFlag, 0); fit[0] = 10000 * fit[0] / 1000;
        fit[1] = rastriginFunc (x, nx, 1, rFlag, 1); fit[1] = 10000 * fit[1] / 1e3;
        fit[2] = schwefelFunc  (x, nx, 1, rFlag, 2); fit[2] = 10000 * fit[2] / 4e3;
        fit[3] = bentCigarFunc (x, nx, 1, rFlag, 3); fit[3] = 10000 * fit[3] / 1e30;
        fit[4] = ellipsFunc    (x, nx, 1, rFlag, 4); fit[4] = 10000 * fit[4] / 1e10;
        fit[5] = escaffer6Func (x, nx, 1, rFlag, 5); fit[5] = 10000 * fit[5] / 2e7;

        return cfCal(x, nx, delta, bias, fit, cfNum);
    }

    /**
     * Computes group sizes from proportions and fills the starting-index array G.
     *
     * @param Gp     Proportion of nx assigned to each group
     * @param nx     Total number of dimensions
     * @param cfNum  Number of groups
     * @param G      Output: start index of each group (length cfNum)
     * @return       Array of group sizes (length cfNum)
     */
    private int[] computeGroupSizes(double[] Gp, int nx, int cfNum, int[] G) {
        int[] Gnx = new int[cfNum];
        int   tmp = 0;
        for (int i = 0; i < cfNum - 1; i++) {
            Gnx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += Gnx[i];
        }
        Gnx[cfNum - 1] = nx - tmp;
        G[0] = 0;
        for (int i = 1; i < cfNum; i++)
            G[i] = G[i - 1] + Gnx[i - 1];
        return Gnx;
    }

    /**
     * Reorders z[] according to shuffle[0] (1-based indices) and returns the result.
     * Mirrors the C++ loop: y[i] = z[S[i]-1].
     */
    private double[] applyShuffleToZ(int nx) {
        double[] hy = new double[nx];
        for (int i = 0; i < nx; i++)
            hy[i] = z[shuffle[0][i] - 1];
        return hy;
    }

    /**
     * Returns a sub-array of src starting at offset with the given length.
     * Used to pass sub-problems to component functions.
     */
    private double[] subArray(double[] src, int offset, int length) {
        double[] result = new double[length];
        System.arraycopy(src, offset, result, 0, length);
        return result;
    }
}