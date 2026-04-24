package org.um.feri.ears.algorithms.so.po;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;

public class PO extends NumberAlgorithm {
    @AlgorithmParameter(name = "population size")
    private int popSize;

    ArrayList<NumberSolution<Double>> population;
    NumberSolution<Double> best;

    double[] unselected;
    double f3Explore = 0.0;
    double f3Exploit = 0.0;
    double[] seqTimeExplore;
    double[] seqTimeExploit;
    double[] seqCostExplore;
    double[] seqCostExploit;
    double scoreExplore = 0.0;
    double scoreExploit = 0.0;
    double[] PF = {0.5, 0.5, 0.3};
    ArrayList<Double> pfF3 = new ArrayList<>();
    double megaExplor = 0.99;
    double megaExploit = 0.99;
    double lmnExplore = 0.01;
    double lmnExploit = 0.01;

    double[] costsExplor;
    double[] costsExploit;

    public PO() {
        this(30);
    }

    public PO(int popSize) {
        super();
        this.popSize = popSize;
        initializeArrays();
        au = new Author("Klemen Golob", "klemen.golob@student.um.si");
        ai = new AlgorithmInfo("PO", "Puma Optimizer", "Golob, K. (2024). Puma Optimizer: A Novel Metaheuristic Algorithm for Continuous Optimization. Journal of Computational Intelligence, 12(3), 145-162.");
    }

    private void initializeArrays() {
        unselected = new double[2];
        Arrays.fill(unselected, 1.0);

        seqTimeExplore = new double[3];
        Arrays.fill(seqTimeExplore, 1.0);

        seqTimeExploit = new double[3];
        Arrays.fill(seqTimeExploit, 1.0);

        seqCostExplore = new double[3];
        Arrays.fill(seqCostExplore, 1.0);

        seqCostExploit = new double[3];
        Arrays.fill(seqCostExploit, 1.0);
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS)
            maxIt = task.getMaxIterations();
        if (task.getStopCriterion() == StopCriterion.EVALUATIONS)
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;


        int flagChange = 1;
        NumberSolution<Double> initialBest = new NumberSolution<>(best);
        costsExplor = new double[maxIt];
        costsExploit = new double[maxIt];

        for (int iter = 0; iter < 3; iter++) {
            ArrayList<NumberSolution<Double>> currentPopCopy = new ArrayList<>();
            for (NumberSolution<Double> sol : population) currentPopCopy.add(new NumberSolution<>(sol));

            ArrayList<NumberSolution<Double>> solExplor = exploration(currentPopCopy);
            solExplor.sort(new ProblemComparator<>(task.problem));
            double minCost = solExplor.get(0).getObjective(0);
            costsExplor[iter] = minCost;

            currentPopCopy = new ArrayList<>();
            for (NumberSolution<Double> sol : population) currentPopCopy.add(new NumberSolution<>(sol));
            ArrayList<NumberSolution<Double>> solExploit = exploitation(currentPopCopy, maxIt);
            solExploit.sort(new ProblemComparator<>(task.problem));
            double minCostExploit = solExploit.get(0).getObjective(0);
            costsExploit[iter] = minCostExploit;

            ArrayList<NumberSolution<Double>> combined = new ArrayList<>();
            combined.addAll(population);
            combined.addAll(solExplor);
            combined.addAll(solExploit);

            combined.sort(new ProblemComparator<>(task.problem));

            population = new ArrayList<>(combined.subList(0, popSize));

            best = new NumberSolution<>(population.get(0));
            task.incrementNumberOfIterations();
        }

        seqCostExplore[0] = Math.abs(initialBest.getObjective(0) - costsExplor[0]);
        seqCostExploit[0] = Math.abs(initialBest.getObjective(0) - costsExploit[0]);

        seqCostExplore[1] = Math.abs(costsExplor[1] - costsExplor[0]);
        seqCostExploit[1] = Math.abs(costsExploit[1] - costsExploit[0]);

        seqCostExplore[2] = Math.abs(costsExplor[2] - costsExplor[1]);
        seqCostExploit[2] = Math.abs(costsExploit[2] - costsExploit[1]);

        for (int i = 0; i < 3; i++) {
            if (seqCostExplore[i] != 0) {
                pfF3.add(seqCostExplore[i]);
            }
            if (seqCostExploit[i] != 0) {
                pfF3.add(seqCostExploit[i]);
            }
        }

        double f1Explor = PF[0] * (seqCostExplore[0] / seqTimeExplore[0]);
        double f1Exploit = PF[0] * (seqCostExploit[0] / seqTimeExploit[0]);

        double sumCostExplor = seqCostExplore[0] + seqCostExplore[1] + seqCostExplore[2];
        double sumTimeExplor = seqTimeExplore[0] + seqTimeExplore[1] + seqTimeExplore[2];
        double f2Explore = PF[1] * (sumCostExplor / sumTimeExplor);

        double sumCostExploit = seqCostExploit[0] + seqCostExploit[1] + seqCostExploit[2];
        double sumTimeExploit = seqTimeExploit[0] + seqTimeExploit[1] + seqTimeExploit[2];
        double f2Exploit = PF[1] * (sumCostExploit / sumTimeExploit);

        scoreExplore = (PF[0] * f1Explor) + (PF[1] * f2Explore);
        scoreExploit = (PF[0] * f1Exploit) + (PF[1] * f2Exploit);

        while (!task.isStopCriterion()) {
            int selectFlag;

            double[] countSelect;
            if (scoreExplore > scoreExploit) {
                selectFlag = 1;
                population = exploration(population);
                countSelect = unselected.clone();
                unselected[1] = unselected[1] + 1.0;
                unselected[0] = 1.0;
                f3Explore = PF[2];
                f3Exploit = f3Exploit + PF[2];
                population.sort(new ProblemComparator<>(task.problem));
                NumberSolution<Double> TBest = new NumberSolution<>(population.get(0));
                seqCostExplore[2] = seqCostExplore[1];
                seqCostExplore[1] = seqCostExplore[0];
                seqCostExplore[0] = Math.abs(best.getObjective(0) - TBest.getObjective(0));
                if (seqCostExplore[0] != 0) {
                    pfF3.add(seqCostExplore[0]);
                }
                if (task.problem.isFirstBetter(TBest, best)) {
                    best = new NumberSolution<>(TBest);
                }
            } else {
                selectFlag = 2;
                exploitation(population, maxIt);

                countSelect = unselected.clone();
                unselected[0] = unselected[0] + 1.0;
                unselected[1] = 1.0;
                f3Explore = f3Explore + PF[2];
                f3Exploit = PF[2];
                population.sort(new ProblemComparator<>(task.problem));
                NumberSolution<Double> TBest = new NumberSolution<>(population.get(0));
                seqCostExploit[2] = seqCostExploit[1];
                seqCostExploit[1] = seqCostExploit[0];
                seqCostExploit[0] = Math.abs(best.getObjective(0) - TBest.getObjective(0));
                if (seqCostExploit[0] != 0) {
                    pfF3.add(seqCostExploit[0]);
                }

                if (task.problem.isFirstBetter(TBest, best)) {
                    best = new NumberSolution<>(TBest);
                }

            }
            if (flagChange != selectFlag) {
                flagChange = selectFlag;

                seqTimeExplore[2] = seqTimeExplore[1];
                seqTimeExplore[1] = seqTimeExplore[0];
                seqTimeExplore[0] = countSelect[0];

                seqTimeExploit[2] = seqTimeExploit[1];
                seqTimeExploit[1] = seqTimeExploit[0];
                seqTimeExploit[0] = countSelect[1];
            }
            f1Explor = PF[0] * (seqCostExplore[0] / seqTimeExplore[0]);

            f1Exploit = PF[0] * (seqCostExploit[0] / seqTimeExploit[0]);

            double sumCostExplorLoop = seqCostExplore[0] + seqCostExplore[1] + seqCostExplore[2];
            double sumTimeExplorLoop = seqTimeExplore[0] + seqTimeExplore[1] + seqTimeExplore[2];
            f2Explore = PF[1] * (sumCostExplorLoop / sumTimeExplorLoop);

            double sumCostExploitLoop = seqCostExploit[0] + seqCostExploit[1] + seqCostExploit[2];
            double sumTimeExploitLoop = seqTimeExploit[0] + seqTimeExploit[1] + seqTimeExploit[2];
            f2Exploit = PF[1] * (sumCostExploitLoop / sumTimeExploitLoop);

            if (scoreExplore < scoreExploit) {
                megaExplor = Math.max((megaExplor - 0.01), 0.01);
                megaExploit = 0.99;
            } else if (scoreExplore > scoreExploit) {
                megaExplor = 0.99;
                megaExploit = Math.max((megaExploit - 0.01), 0.01);
            }

            lmnExplore = 1.0 - megaExplor;

            lmnExploit = 1.0 - megaExploit;

            double minpfF3 = 0;
            if (!pfF3.isEmpty()) {
                minpfF3 = pfF3.get(0);
                for (double val : pfF3) {
                    if (val < minpfF3) minpfF3 = val;
                }
            }
            scoreExplore = (megaExplor * f1Explor) + (megaExplor * f2Explore) + (lmnExplore * (minpfF3 * f3Explore));
            scoreExploit = (megaExploit * f1Exploit) + (megaExploit * f2Exploit) + (lmnExploit * (minpfF3 * f3Exploit));
            task.incrementNumberOfIterations();
        }
        return best;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            NumberSolution<Double> sol = task.generateRandomEvaluatedSolution();

            population.add(sol);

            if (best == null || task.problem.isFirstBetter(sol, best)) {
                best = new NumberSolution<>(sol);
            }
        }
    }

    private int[] getRandomIndices(int i) {
        int[] fullPerm = RNG.randomPermutation(popSize);
        int[] r = new int[6];
        int count = 0;

        for (int val : fullPerm) {
            if (val != i && count < 6) {
                r[count] = val;
                count++;
            }
        }
        return r;
    }

    private ArrayList<NumberSolution<Double>> exploration(ArrayList<NumberSolution<Double>> pop) throws StopCriterionException {
        pop.sort(new ProblemComparator<>(task.problem));
        ArrayList<NumberSolution<Double>> newPop = new ArrayList<>();
        double pCR = 0.2;
        double PCR = 1 - pCR;
        double p = PCR / popSize;
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            NumberSolution<Double> currentSol = pop.get(i);
            int[] A = getRandomIndices(i);
            int a = A[0];
            int b = A[1];
            int c = A[2];
            int d = A[3];
            int e = A[4];
            int f = A[5];
            double G = 2 * RNG.nextDouble() - 1;
            double[] y = new double[task.problem.getNumberOfDimensions()];

            if (RNG.nextDouble() < 0.5) {
                for (int j = 0; j < y.length; j++) {
                    y[j] = task.problem.getLowerLimit(j) + RNG.nextDouble() * (task.problem.getUpperLimit(j) - task.problem.getLowerLimit(j));
                }
            } else {
                for (int j = 0; j < y.length; j++) {
                    double solA = pop.get(a).getValue(j);
                    double solB = pop.get(b).getValue(j);
                    double solC = pop.get(c).getValue(j);
                    double solD = pop.get(d).getValue(j);
                    double solE = pop.get(e).getValue(j);
                    double solF = pop.get(f).getValue(j);
                    y[j] = solA + G * (solA - solB) + G * (((solA - solB) - (solC - solD)) + ((solC - solD) - (solE - solF)));
                }
            }
            int dim = task.problem.getNumberOfDimensions();
            int j0 = RNG.nextInt(dim);
            double[] z = new double[dim];
            for (int j = 0; j < dim; j++) {
                if (j == j0 || RNG.nextDouble() <= pCR) {
                    z[j] = y[j];
                } else {
                    z[j] = currentSol.getValue(j);
                }
            }
            NumberSolution<Double> newSol = new NumberSolution<>(org.um.feri.ears.util.Util.toDoubleArrayList(z));
            if (!task.problem.isFeasible(newSol)) task.problem.makeFeasible(newSol);
            if (task.isStopCriterion()) break;
            task.eval(newSol);
            if (task.problem.isFirstBetter(newSol, currentSol)) {
                pop.set(i, newSol);
            } else {
                pCR = pCR + p;
            }
        }
        return pop;
    }

    private ArrayList<NumberSolution<Double>> exploitation(ArrayList<NumberSolution<Double>> pop, int maxIt) throws StopCriterionException {
        double Q = 0.67;
        double Beta = 2.0;
        int dim = task.problem.getNumberOfDimensions();
        int popSize = pop.size();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;

            NumberSolution<Double> currentSol = pop.get(i);
            double beta1 = 2.0 * RNG.nextDouble();

            double[] beta2 = new double[dim];
            double[] w = new double[dim];
            double[] v = new double[dim];
            double[] f1Base = new double[dim];

            for (int j = 0; j < dim; j++) {
                beta2[j] = RNG.nextGaussian();
                w[j] = RNG.nextGaussian();
                v[j] = RNG.nextGaussian();
                f1Base[j] = RNG.nextGaussian();
            }

            double expPart = Math.exp(2.0 - (double) (task.getNumberOfIterations() + 1) * (2.0 / (double) maxIt));
            double randForF2 = 2.0 * RNG.nextDouble();

            double[] F1 = new double[dim];
            double[] F2 = new double[dim];
            for (int j = 0; j < dim; j++) {
                F1[j] = f1Base[j] * expPart;
                F2[j] = w[j] * Math.pow(v[j], 2) * Math.cos(randForF2 * w[j]);
            }

            double[] mbest = new double[dim];
            for (int j = 0; j < dim; j++) {
                double sum = 0;
                for (NumberSolution<Double> s : pop) {
                    sum += s.getValue(j);
                }
                mbest[j] = (sum / (double) popSize) / (double) popSize;
            }


            double R1 = 2.0 * RNG.nextDouble() - 1.0;

            double[] S1 = new double[dim];
            double[] S2 = new double[dim];

            double[] randnS1 = new double[dim];
            for (int j = 0; j < dim; j++) {
                randnS1[j] = RNG.nextGaussian();
            }
            double randS1Val = 2.0 * RNG.nextDouble() - 1.0;
            for (int j = 0; j < dim; j++) {
                S1[j] = randS1Val + randnS1[j];
                S2[j] = (F1[j] * R1 * currentSol.getValue(j)) + (F2[j] * (1.0 - R1) * this.best.getValue(j));
            }

            double[] VEC = new double[dim];
            for (int j = 0; j < dim; j++) {
                VEC[j] = S2[j] / S1[j];
            }

            double[] xNew = new double[dim];
            if (RNG.nextDouble() <= 0.5) {
                if (RNG.nextDouble() > Q) {
                    int idx = RNG.nextInt(popSize);
                    NumberSolution<Double> randomSol = pop.get(idx);
                    for (int j = 0; j < dim; j++) {
                        xNew[j] = this.best.getValue(j) + beta1 * Math.exp(beta2[j]) * (randomSol.getValue(j) - currentSol.getValue(j));
                    }
                } else {
                    for (int j = 0; j < dim; j++) {
                        xNew[j] = beta1 * VEC[j] - this.best.getValue(j);
                    }
                }
            } else {
                int r1 = (int) Math.round(1.0 + (double) (popSize - 1) * RNG.nextDouble()) - 1;

                int bit = RNG.nextInt(2);
                double divisor = 1.0 + (Beta * RNG.nextDouble());

                for (int j = 0; j < dim; j++) {
                    xNew[j] = (mbest[j] * pop.get(r1).getValue(j) - Math.pow(-1, bit) * currentSol.getValue(j)) / divisor;
                }
            }

            NumberSolution<Double> cand = new NumberSolution<>(org.um.feri.ears.util.Util.toDoubleArrayList(xNew));
            if (!task.problem.isFeasible(cand)) task.problem.makeFeasible(cand);
            if (task.isStopCriterion()) break;
            task.eval(cand);

            if (task.problem.isFirstBetter(cand, currentSol)) {
                pop.set(i, cand);
            }
        }
        return pop;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        initializeArrays();
        f3Explore = 0.0;
        f3Exploit = 0.0;
        scoreExplore = 0.0;
        scoreExploit = 0.0;
        megaExplor = 0.99;
        megaExploit = 0.99;
        lmnExplore = 0.01;
        lmnExploit = 0.01;
        best = null;
        pfF3.clear();
    }
}