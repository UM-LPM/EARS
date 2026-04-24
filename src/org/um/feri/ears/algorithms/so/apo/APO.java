package org.um.feri.ears.algorithms.so.apo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class APO extends NumberAlgorithm {
    @AlgorithmParameter(name = "population size")
    private int popSize;

    @AlgorithmParameter(name = "np (neighbor pairs)")
    private int np = 1;

    @AlgorithmParameter(name = "pf_max (proportion fraction max)")
    private double pfMax = 0.1;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> bestSolution;

    private int dim;
    private double LB;
    private double UB;
    private double eps = 2.220446049250313e-16;
    private double bestFit;
    private double[] bestProtozoa;

    public APO() {
        this(30);
    }

    public APO(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("Matevz", "matevz.sladic@student.um.si");
        ai = new AlgorithmInfo("APO", "Artificial Protozoa Optimizer",
                "@article{wang2024apo," +
                        "  title={Artificial Protozoa Optimizer (APO): A novel bio-inspired metaheuristic algorithm for engineering optimization}," +
                        "  author={Wang, Xiaopeng and Snášel, Václav and Mirjalili, Seyedali and Pan, Jeng-Shyang and Kong, Lingping and Shehadeh, Hisham A.}," +
                        "  journal={Knowledge-Based Systems}," +
                        "  volume={295}," +
                        "  number={C}," +
                        "  pages={111737}," +
                        "  year={2024}," +
                        "  publisher={Elsevier}," +
                        "  doi={10.1016/j.knosys.2024.111737}" +
                        "}"
        );

    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        dim = task.problem.getNumberOfDimensions();
        LB = task.problem.lowerLimit.get(0);
        UB = task.problem.upperLimit.get(0);

        int maxIterations = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIterations = task.getMaxIterations();
        }
        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIterations = (task.getMaxEvaluations() - popSize) / popSize;
        }

        double[][] protozoa = new double[popSize][dim];
        double[][] newProtozoa = new double[popSize][dim];
        double[][] epn = new double[np][dim];
        double[] protozoaFit = new double[popSize];
        bestFit = Double.MAX_VALUE;
        bestProtozoa = new double[dim];

        for (int i = 0; i < popSize; i++) {

            NumberSolution<Double> sol = task.generateRandomEvaluatedSolution();
            protozoa[i] = Util.toDoubleArray(sol.getVariables());

            protozoaFit[i] = sol.getObjective(0);
            if (protozoaFit[i] < bestFit) {
                bestFit = protozoaFit[i];
                bestProtozoa = protozoa[i].clone();
            }
        }

        while (!task.isStopCriterion()) {
            int[] order = argSort(protozoaFit);
            protozoa = reorder(protozoa, order);
            protozoaFit = reorder(protozoaFit, order);

            double pf = pfMax * RNG.nextDouble();
            int numRi = (int) Math.ceil(popSize * pf);
            int[] ri = randPermIndices(popSize, numRi);

            for (int i = 0; i < popSize; i++) {
                if (contains(ri, i)) {
                    // dormancy or reproduction
                    double pdr = 0.5 * (1 + Math.cos((1.0 - (double) (i + 1) / popSize) * Math.PI));

                    if (RNG.nextDouble() < pdr) {
                        newProtozoa[i] = task.problem.generateRandomVariables();
                    } else {
                        int flag = RNG.nextDouble() <= 0.5 ? 1 : -1;
                        int[] Mr = new int[dim];
                        int[] idxs = randPermIndices(dim, (int) Math.ceil(RNG.nextDouble() * dim));
                        for (int id : idxs) Mr[id] = 1;
                        for (int d = 0; d < dim; d++) {
                            double randVec = LB + RNG.nextDouble() * (UB - LB);
                            newProtozoa[i][d] = protozoa[i][d] + flag * RNG.nextDouble() * randVec * Mr[d];
                        }
                    }
                } else {
                    // foraging form
                    int it = task.getNumberOfIterations() + 2;
                    double f = RNG.nextDouble() * (1 + Math.cos((double) it / maxIterations * Math.PI));
                    int[] Mf = new int[dim];
                    int[] mfIdxs = randPermIndices(dim, (int) Math.ceil((double) dim * (i + 1) / popSize));
                    for (int id : mfIdxs) {
                        Mf[id] = 1;
                    }
                    double pah = 0.5 * (1 + Math.cos((double) it / maxIterations * Math.PI));

                    if (RNG.nextDouble() < pah) {
                        // autotroph form
                        int j = RNG.nextInt(popSize);
                        for (int k = 0; k < np; k++) {
                            int km, kp;
                            if (i == 0) {
                                km = i;
                                kp = i + (RNG.nextInt(popSize - i - 1) + 1);
                            } else if (i == popSize - 1) {
                                km = RNG.nextInt(popSize - 1);
                                kp = i;
                            } else {
                                km = RNG.nextInt(i);
                                kp = i + (RNG.nextInt(popSize - (i + 1)) + 1);
                            }
                            double wa = Math.exp(-Math.abs(protozoaFit[km] / (protozoaFit[kp] + eps)));
                            for (int d = 0; d < dim; d++) {
                                epn[k][d] = wa * (protozoa[km][d] - protozoa[kp][d]);
                            }
                        }
                        for (int d = 0; d < dim; d++) {
                            double sumEpn = 0.0;
                            for (int k = 0; k < np; k++) sumEpn += epn[k][d];
                            newProtozoa[i][d] = protozoa[i][d] + f * (protozoa[j][d] - protozoa[i][d] + (1.0 / np) * sumEpn) * Mf[d];
                        }
                    } else {
                        // heterotroph form
                        for (int k = 0; k < np; k++) {
                            int imk, ipk;
                            if (i == 0) {
                                imk = i;
                                ipk = i + (k + 1);
                            } else if (i == popSize - 1) {
                                imk = (popSize - 1) - (k + 1);
                                ipk = i;
                            } else {
                                imk = i - (k + 1);
                                ipk = i + (k + 1);
                            }
                            if (imk < 0) imk = 0;
                            if (ipk > popSize - 1) ipk = popSize - 1;
                            double wh = Math.exp(-Math.abs(protozoaFit[imk] / (protozoaFit[ipk] + eps)));
                            for (int d = 0; d < dim; d++) {
                                epn[k][d] = wh * (protozoa[imk][d] - protozoa[ipk][d]);
                            }
                        }
                        int flag = RNG.nextDouble() <= 0.5 ? 1 : -1;
                        double[] Xnear = new double[dim];
                        for (int d = 0; d < dim; d++) {
                            Xnear[d] = (1 + flag * RNG.nextDouble() * (1.0 - (double) it / maxIterations)) * protozoa[i][d];
                        }
                        for (int d = 0; d < dim; d++) {
                            double sumEpn = 0.0;
                            for (int k = 0; k < np; k++) sumEpn += epn[k][d];
                            newProtozoa[i][d] = protozoa[i][d] + f * (Xnear[d] - protozoa[i][d] + (1.0 / np) * sumEpn) * Mf[d];
                        }
                    }
                }
                if (task.isStopCriterion()) break;
            }

            for (int i = 0; i < popSize; i++) {
                task.problem.makeFeasible(newProtozoa[i]);
                NumberSolution<Double> newSol = new NumberSolution<>(Util.toDoubleArrayList(newProtozoa[i]));
                if (task.isStopCriterion()) break;
                task.eval(newSol);

                if (task.problem.isFirstBetter(newSol.getObjective(0), protozoaFit[i], 0)) {
                    protozoa[i] = newProtozoa[i].clone();
                    protozoaFit[i] = newSol.getObjective(0);
                }
                if (protozoaFit[i] < bestFit) {
                    bestFit = protozoaFit[i];
                    bestProtozoa = protozoa[i].clone();
                }
            }
            task.incrementNumberOfIterations();
        }
        bestSolution = new NumberSolution<>(Util.toDoubleArrayList(bestProtozoa));
        bestSolution.setObjective(0, bestFit);

        return bestSolution;
    }

    private boolean contains(int[] arr, int val) {
        for (int v : arr)
            if (v == val) return true;
        return false;
    }

    private int[] argSort(double[] a) {
        Integer[] idx = new Integer[a.length];
        for (int i = 0; i < a.length; i++) idx[i] = i;
        java.util.Arrays.sort(idx, (i, j) -> Double.compare(a[i], a[j]));
        int[] out = new int[a.length];
        for (int i = 0; i < a.length; i++) out[i] = idx[i];
        return out;
    }

    private double[][] reorder(double[][] mat, int[] order) {
        double[][] out = new double[mat.length][mat[0].length];
        for (int i = 0; i < order.length; i++) out[i] = mat[order[i]].clone();
        return out;
    }

    private double[] reorder(double[] vec, int[] order) {
        double[] out = new double[vec.length];
        for (int i = 0; i < order.length; i++) out[i] = vec[order[i]];
        return out;
    }

    public int[] randPermIndices(int n, int k) {
        ArrayList<Integer> pool = new ArrayList<>();
        for (int i = 0; i < n; i++)
            pool.add(i);
        int[] arr = new int[k];
        for (int i = 0; i < k; i++) {
            int idx = RNG.nextInt(pool.size());
            arr[i] = pool.get(idx);
            pool.remove(idx);
        }
        return arr;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
