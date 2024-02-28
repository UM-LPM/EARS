package org.um.feri.ears.algorithms.so.btlbo;


import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.Statistic;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BTLBO extends NumberAlgorithm {

    private Task<NumberSolution<Double>, DoubleProblem> task;

    private List<Double> lowerLimit;
    private List<Double> upperLimit;

    public Statistic stat;

    @AlgorithmParameter(name = "population size")
    private int popSize;
    private NumberSolution<Double>[] population;

    private int dimensions;
    public BTLBO() {
        this(20);
    }
    public BTLBO(int popSize) {
        super();
        this.popSize = popSize;
        au = new Author("Bojan", "bojan.orter@student.um.si");
        ai = new AlgorithmInfo("BTLBO", "Balanced Teaching-Learning-Based Optimization", "@article{TAHERI202168,\n" +
                "title = {An efficient Balanced Teaching-Learning-Based optimization algorithm with Individual restarting strategy for solving global optimization problems},\n" +
                "journal = {Information Sciences},\n" +
                "volume = {576},\n" +
                "pages = {68-104},\n" +
                "year = {2021},\n" +
                "author = {Ahmad Taheri and Keyvan RahimiZadeh and Ravipudi Venkata Rao}\n" +
                "}");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        lowerLimit = task.problem.getLowerLimit();
        upperLimit = task.problem.getUpperLimit();
        dimensions = task.problem.getNumberOfDimensions();
        stat = new Statistic();

        init();
        runAlgorithm();

        return stat.getCurrent_g().best;
    }

    private void init() throws StopCriterionException {
        population = new NumberSolution[popSize];

        if (task.isStopCriterion()) {
            return;
        }
        population[0] = task.getRandomEvaluatedSolution();
        if (stat.getCurrent_g().getBest() == null) {
            stat.getCurrent_g().setBest(population[0]);
        }

        if (task.problem.isFirstBetter(population[0], stat.getBest())) {
            stat.getCurrent_g().setBest(population[0]);
        }

        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion()) {
                return;
            }

            population[i] = (NumberSolution<Double>) task.getRandomEvaluatedSolution();
            if (task.problem.isFirstBetter(population[i], stat.getBest())) {
                stat.getCurrent_g().setBest(population[i]);
            }
        }
    }

    private void runAlgorithm() throws StopCriterionException {
        int maxFEs = task.getMaxEvaluations();

        int[] FC = new int[popSize];
        double lambda = Math.pow(((double) popSize / 100), 2);
        double maxFail = (maxFEs / (20 * (double) popSize)) * Math.exp(Math.pow((1 / (double) maxFEs), lambda));

        double rho = 0;

        NumberSolution<Double> teacherDS;
        double[] teacher;

        sortByFirstBetterCondition();

        while (!task.isStopCriterion()) {
            double alpha = 0;
            double[] mean = mean();

            teacherDS = population[getBestCostIndexInPopulation(population)];
            teacher = Util.toDoubleArray(population[getBestCostIndexInPopulation(population)].getVariables());

            for (int i = 0; i < popSize; i++) {
                alpha = (task.getNumberOfEvaluations() / (double) maxFEs) - rho;
                int indexJ = nextRandJ(i, popSize);
                double[] xi = Util.toDoubleArray(population[i].getVariables());
                double[] xj = Util.toDoubleArray(population[indexJ].getVariables());
                double[] xnew = new double[dimensions];

                int phase = nextIntMatlab(1, 3);
                if (phase == 1) { //teaching phase
                    int s = nextIntMatlab(0, 1);
                    int TF = nextIntMatlab(1, 2);

                    double[] mw = mw(mean, s, xi, xj);

                    xnew = xnewTeacherPhase(s, TF, mw, xi, teacher);
                } else if (phase == 2) { //learning
                    double[] step;

                    if (task.problem.isFirstBetter(population[indexJ], population[i])) {
                        step = subDoubleSolutions(xj, xi);
                    } else {
                        step = subDoubleSolutions(xi, xj);
                    }

                    xnew = xnewLearnerPhase(step, xi);
                } else { //Tutoring phase
                    double exp = Math.pow(Math.exp(-1 * (1 - alpha)), 2);
                    int ceilD = (int) Math.ceil(dimensions * Math.min((RNG.nextDouble() * exp), 1.0));
                    int[] indexes = randperm(dimensions, ceilD);
                    xnew = xnewTuturingPhase(task.getNumberOfEvaluations(), task.getMaxEvaluations(), indexes, Util.toDoubleArray(stat.getBest().getVariables()), xi, xj);
                }

                xnew = clipping(xnew, xi);
                NumberSolution<Double> xnewDS = new NumberSolution<>(Util.toDoubleArrayList(xnew));
                if (task.isStopCriterion()) {
                    return;
                }
                task.eval(xnewDS);

                if (task.problem.isFirstBetter(xnewDS, population[i])) {
                    population[i] = xnewDS;

                    FC[i] = 0;

                    if (task.problem.isFirstBetter(xnewDS, teacherDS)) {
                        teacherDS = xnewDS;
                        teacher = Util.toDoubleArray(xnewDS.getVariables());
                    }

                    if (task.problem.isFirstBetter(xnewDS, stat.getBest())) {
                        stat.getCurrent_g().setBest(xnewDS);
                    }
                } else {
                    FC[i] += 1;
                }
            }

            //reset phase
            double FT = (maxFail * (0.005 + (Math.pow(alpha, 2))));
            double threshold = (maxFail / 10) * Math.pow(
                    (1 + (task.getNumberOfEvaluations() / (double) task.getMaxEvaluations())),
                    (1 - (task.getNumberOfEvaluations() / (double) task.getMaxEvaluations()))
            );

            if (getFCOverThresholdCount(FC, threshold) > (popSize / 2)) {
                rho = task.getNumberOfEvaluations() / (double) task.getMaxEvaluations();

                //reset whole population
                init();
                if (task.isStopCriterion()) {
                    return;
                }
            } else {
                for (int i = 0; i < popSize; i++) {
                    if (FC[i] >= FT) {
                        double exp = Math.pow(Math.exp(-1 * (1 - alpha)), 2);
                        double rand = RNG.nextDouble();
                        double k = dimensions * (rand * exp);
                        int[] indexes = randperm(dimensions, (int) Math.ceil(k));

                        double[] xnew = resetIndexes(indexes, Util.toDoubleArray(population[i].getVariables()));
                        NumberSolution<Double> xnewDS = new NumberSolution<>(Util.toDoubleArrayList(xnew));
                        if (task.isStopCriterion()) {
                            return;
                        }
                        task.eval(xnewDS);
                        population[i] = xnewDS;
                        FC[i] = 0;

                        if (task.problem.isFirstBetter(population[i], stat.getBest())) {
                            stat.getCurrent_g().setBest(population[i]);
                        }
                    }
                }
            }
        }
    }

    private int nextIntMatlab(int lowerBound, int upperBound) {
        double rand = RNG.nextDouble();
        int factor = upperBound - (lowerBound - 1);

        return (lowerBound - 1) + ((int) (Math.ceil((double) factor * rand)));
    }


    //region phase xnew calculation helpers
    private double[] xnewTeacherPhase(int s, int TF, double[] mw, double[] xi, double[] teacher) {
        double[] xnew = new double[dimensions];

        double rand1 = RNG.nextDouble();
        double rand2 = RNG.nextDouble();

        for (int i = 0; i < dimensions; i++) {
            xnew[i] = xi[i]
                    + ((s) * rand1 * TF * (teacher[i] - mw[i]))
                    + ((1 - s) * rand2 * (teacher[i] - (TF * mw[i])));
        }

        return xnew;
    }

    private double[] xnewLearnerPhase(double[] step, double[] xi) {
        double[] xnew = new double[dimensions];

        for (int i = 0; i < dimensions; i++) {
            double rand = RNG.nextDouble();
            xnew[i] = xi[i] + (rand * step[i]);
        }

        return xnew;
    }

    private double[] xnewTuturingPhase(int FEs, int maxFEs, int[] indexes, double[] bestSolution, double[] xi, double[] xj) {
        double[] xnew = new double[dimensions];
        System.arraycopy(xi, 0, xnew, 0, dimensions);

        for (int i = 0; i < indexes.length; i++) {
            int index = indexes[i];
            double rand = RNG.nextDouble(-1, 1);
            xnew[index] = bestSolution[index] + Math.pow(1 - ((double) FEs / (double) maxFEs), 2) * rand * (xi[index] - xj[index]);
        }

        return xnew;
    }
    //endregion

    //region calculation helpers
    private double[] mean() {
        double[] d = new double[dimensions];
        int min = 0;
        int max = popSize - min;
        for (int i = min; i < max; i++) {
            for (int j = 0; j < dimensions; j++) {
                d[j] += population[i].getValue(j);
            }
        }

        for (int j = 0; j < dimensions; j++) {
            d[j] = d[j] / (max - min);
        }

        return d;
    }

    private double[] mw(double[] mean, int s, double[] xi, double[] xj) {
        double[] mw = new double[dimensions];

        for (int i = 0; i < dimensions; i++) {
            mw[i] = (mean[i] + (((s * xj[i]) + ((1 - s) * xi[i])))) / 2;
        }

        return mw;
    }

    private double[] clipping(double[] xnew, double[] xold) {
        ArrayList indexesUnderLB = new ArrayList<Integer>();
        ArrayList indexesOverUB = new ArrayList<Integer>();

        for (int i = 0; i < dimensions; i++) {
            if (xnew[i] < lowerLimit.get(i)) {
                indexesUnderLB.add(i);
            } else if (xnew[i] > upperLimit.get(i)) {
                indexesOverUB.add(i);
            }
        }

        for (int i = 0; i < indexesUnderLB.size(); i++) {
            double rand = RNG.nextDouble();
            int index = (int) indexesUnderLB.get(i);
            xnew[index] = xold[index] + rand * (lowerLimit.get(index) - xold[index]);
        }

        for (int i = 0; i < indexesOverUB.size(); i++) {
            double rand = RNG.nextDouble(-1, 1);
            int index = (int) indexesOverUB.get(i);
            xnew[index] = xold[index] + rand * (upperLimit.get(index) - xold[index]);
        }

        return xnew;
    }

    public double[] resetIndexes(int[] indexes, double[] x) {
        for (int i = 0; i < indexes.length; i++) {
            int index = indexes[i];
            double rand = RNG.nextDouble();
            x[index] = lowerLimit.get(index) + rand * (upperLimit.get(index) - lowerLimit.get(index));
        }

        return x;
    }

    private double[] subDoubleSolutions(double[] a, double[] b) {
        double[] sub = new double[dimensions];

        for (int i = 0; i < dimensions; i++) {
            sub[i] = a[i] - b[i];
        }

        return sub;
    }

    //endregion

    //region general helpers
    private void sortByFirstBetterCondition() {
        ProblemComparator<NumberSolution<Double>> s = new ProblemComparator<>(task.problem);
        Arrays.sort(population, s);
    }

    private int getBestCostIndexInPopulation(NumberSolution<Double>[] population) {
        int index = 0;
        NumberSolution best = population[index];
        for (int i = 1; i < popSize; i++) {
            if (task.problem.isFirstBetter(population[i], best)) {
                index = i;
                best = population[i];
            }
        }

        return index;
    }

    private int getFCOverThresholdCount(int[] FC, double threshold) {
        int count = 0;
        for (int i = 0; i < FC.length; i++) {
            if ((double) FC[i] >= threshold) {
                count++;
            }
        }
        return count;
    }
    //endregion

    // region Random helpers

    private int nextRandJ(int currentI, int arrayLen) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < arrayLen; i++) {
            indexes.add(i);
        }
        indexes.removeAll(Arrays.asList(currentI));

        int next = nextIntMatlab(1, indexes.size()) - 1;
        return indexes.get(next);
    }

    private int[] randperm(int n, int k) {
        ArrayList<Integer> permutation = new ArrayList<>();
        int[] result = new int[k];

        for (int i = 0; i < n; i++) {
            permutation.add(i);
        }

        int randIndex;
        for (int i = 0; i < k; i++) {
            randIndex = nextIntMatlab(1, permutation.size()) - 1;
            result[i] = permutation.get(randIndex);
            permutation.removeAll(Arrays.asList(result[i]));
        }

        /*int[] result = new int[k];
        int[] permuted = Util.randomPermutation(n);
        for(int i = 0; i < k; i++) {
            result[i] = permuted[i];
        }*/

        return result;
    }

    //endregion

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
