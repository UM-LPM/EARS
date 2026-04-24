package org.um.feri.ears.algorithms.so.gjo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;


public class GJO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    @AlgorithmParameter(name = "printDecimals")
    private int printDecimals = 4;

    private ArrayList<double[]> positions;
    private double[] malePos;
    private double maleScore;

    private double[] femalePos;
    private double femaleScore;

    public GJO() {
        this(30);
    }

    public GJO(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("Dejan", "dejan.tominc@student.um.si");
        ai = new AlgorithmInfo(
                "GJO",
                "Golden Jackal Optimization",
                "@article{chopra2022gjo,"
                        + "  title={Golden Jackal Optimization: A Novel Nature-Inspired Optimizer for Engineering Applications},"
                        + "  author={Chopra, Nitish and Ansari, Muhammad Mohsin},"
                        + "  journal={Expert Systems with Applications},"
                        + "  year={2022},"
                        + "  pages={116924},"
                        + "  publisher={Elsevier}"
                        + "}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        final int dim = task.problem.getNumberOfDimensions();

        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        } else if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = task.getMaxEvaluations() / popSize;
        }

        initPopulation(dim);

        NumberSolution<Double> bestSol = null;

        while (!task.isStopCriterion()) {

            // 1) update Male/Female
            for (int i = 0; i < popSize; i++) {
                if (task.isStopCriterion()) break;

                double[] x = positions.get(i);
                task.problem.makeFeasible(x);
                NumberSolution<Double> sol = new NumberSolution<>(Util.toDoubleArrayList(x));

                task.eval(sol);

                double fit = sol.getEval();
                //TODO: replace with isFirstBetter
                if (fit < maleScore) {
                    maleScore = fit;
                    malePos = x.clone();
                    bestSol = sol;

                } else if (fit > maleScore && fit < femaleScore) {
                    femaleScore = fit;
                    femalePos = x.clone();
                }
            }

            if (task.isStopCriterion()) break;

            // 2) Compute E1 and RL (Levy)
            double E1 = 1.5 * (1.0 - ((double) task.getNumberOfIterations() / (double) maxIt));
            double[][] RL = levyMatrix(popSize, dim, 1.5);

            for (int i = 0; i < popSize; i++) {
                for (int j = 0; j < dim; j++) {
                    RL[i][j] *= 0.05;
                }
            }

            // 3) Update positions
            for (int i = 0; i < popSize; i++) {
                double[] x = positions.get(i);
                double[] newX = new double[dim];

                for (int j = 0; j < dim; j++) {
                    double r1 = RNG.nextDouble();
                    double E0 = 2.0 * r1 - 1.0;
                    double E = E1 * E0;

                    double maleCandidate;
                    double femaleCandidate;

                    if (Math.abs(E) < 1.0) {
                        double Dm = Math.abs((RL[i][j] * malePos[j]) - x[j]);
                        maleCandidate = malePos[j] - E * Dm;

                        double Df = Math.abs((RL[i][j] * femalePos[j]) - x[j]);
                        femaleCandidate = femalePos[j] - E * Df;
                    } else {
                        double Dm = Math.abs(malePos[j] - (RL[i][j] * x[j]));
                        maleCandidate = malePos[j] - E * Dm;

                        double Df = Math.abs(femalePos[j] - (RL[i][j] * x[j]));
                        femaleCandidate = femalePos[j] - E * Df;
                    }

                    newX[j] = (maleCandidate + femaleCandidate) / 2.0;
                }

                positions.set(i, newX);
            }
            task.incrementNumberOfIterations();
        }

        if (bestSol != null) {
            return bestSol;
        }

        return new NumberSolution<>(Util.toDoubleArrayList(malePos));
    }

    private void initPopulation(int dim) {
        positions = new ArrayList<>(popSize);
        for (int i = 0; i < popSize; i++) {
            positions.add(new double[dim]);
        }

        for (int d = 0; d < dim; d++) {
            double lb = task.problem.getLowerLimit(d);
            double ub = task.problem.getUpperLimit(d);
            double range = ub - lb;

            for (int i = 0; i < popSize; i++) {
                double u = RNG.nextDouble();
                positions.get(i)[d] = u * range + lb;
            }
        }

        malePos = new double[dim];
        femalePos = new double[dim];
        maleScore = Double.POSITIVE_INFINITY;
        femaleScore = Double.POSITIVE_INFINITY;
    }

    private double[][] levyMatrix(int n, int m, double beta) {
        double num = gamma(1.0 + beta) * Math.sin(Math.PI * beta / 2.0);
        double den = gamma((1.0 + beta) / 2.0) * beta * Math.pow(2.0, (beta - 1.0) / 2.0);
        double sigmaU = Math.pow(num / den, 1.0 / beta);

        double[][] z = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double u = RNG.nextGaussian(0.0, sigmaU);
                double v = RNG.nextGaussian(0.0, 1.0);
                z[i][j] = u / Math.pow(Math.abs(v), 1.0 / beta);
            }
        }
        return z;
    }

    private double gamma(double z) {
        if (z < 0.5) {
            return Math.PI / (Math.sin(Math.PI * z) * gamma(1.0 - z));
        }

        double[] p = {
                0.99999999999980993,
                676.5203681218851,
                -1259.1392167224028,
                771.32342877765313,
                -176.61502916214059,
                12.507343278686905,
                -0.13857109526572012,
                9.9843695780195716e-6,
                1.5056327351493116e-7
        };

        z -= 1.0;
        double x = p[0];
        for (int i = 1; i < p.length; i++) x += p[i] / (z + i);

        double g = 7.0;
        double t = z + g + 0.5;
        return Math.sqrt(2.0 * Math.PI) * Math.pow(t, z + 0.5) * Math.exp(-t) * x;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        positions = null;
        malePos = null;
        femalePos = null;
        maleScore = Double.POSITIVE_INFINITY;
        femaleScore = Double.POSITIVE_INFINITY;
    }
}
