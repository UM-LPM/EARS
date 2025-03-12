package org.um.feri.ears.algorithms.so.mvo;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MVO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    private int dim;
    private List<Double> lb;
    private List<Double> ub;
    private ArrayList<NumberSolution<Double>> universes;
    private NumberSolution<Double> bestUniverse;

    public MVO() { this(60); }

    public MVO(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("Lovro Novak", "lovro.novak@student.um.si");
        ai = new AlgorithmInfo("MVO", "Multi-Verse Optimizer",
                "@article{mirjalili2016multi,"
                        + "  title={Multi-verse optimizer: a nature-inspired algorithm for global optimization},"
                        + "  author={Mirjalili, Seyedali and Mirjalili, Seyed Mohammad and Hatamlou, Ali},"
                        + "  journal={Neural Computing and Applications},"
                        + "  volume={27},"
                        + "  pages={495--513},"
                        + "  year={2016},"
                        + "  publisher={Springer}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        lb = task.problem.lowerLimit;
        ub = task.problem.upperLimit;
        dim = task.problem.getNumberOfDimensions();
        int maxIt = task.getMaxIterations();

        if (maxIt == 0)
            maxIt = (task.getMaxEvaluations()-popSize) / popSize;

        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        } else if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;
        } else {
            maxIt = 10000;
        }

        initPopulation();

        double wepMax = 1.0;
        double wepMin = 0.2;

        while (!task.isStopCriterion()){
            // WEP - wormhole existence probability
            double wep = wepMin + task.getNumberOfIterations() * ((wepMax - wepMin) / maxIt);
            // TDR - travel distance rate
            double tdr = 1 - Math.pow((double) task.getNumberOfIterations() / maxIt, 1.0 / 6.0);
            double[] inflationRates = new double[popSize]; // objectives
            for (int i = 0; i < popSize; i++){

                task.problem.makeFeasible(universes.get(i));

                if (task.isStopCriterion())
                    break;

                task.eval(universes.get(i));

                inflationRates[i] = universes.get(i).getObjective(0);

                if (task.problem.isFirstBetter(universes.get(i), bestUniverse)){
                    bestUniverse = universes.get(i);
                }
            }

            int[] sortedIndices;
            double[] sortedInflationRates = inflationRates.clone();
            int n = inflationRates.length;
            Integer[] indexes = new Integer[n];
            for (int i = 0; i < n; i++) {
                indexes[i] = i;
            }
            Arrays.sort(indexes, Comparator.comparingDouble(i -> inflationRates[i]));
            for (int i = 0; i < n; i++) {
                sortedInflationRates[i] = inflationRates[indexes[i]];
            }
            sortedIndices = Arrays.stream(indexes).mapToInt(Integer::intValue).toArray();

            double l2Norm = 0.0;
            for (double value : sortedInflationRates) {
                l2Norm += value * value;
            }
            l2Norm = Math.sqrt(l2Norm);
            double[] normalizedSortedInflationRates = new double[sortedInflationRates.length];
            for (int i = 0; i < sortedInflationRates.length; i++) {
                normalizedSortedInflationRates[i] = sortedInflationRates[i] / l2Norm;
            }

            ArrayList<NumberSolution<Double>> sortedUniverses = new ArrayList<>();
            for (int newindex = 0; newindex < popSize; newindex++) {
                sortedUniverses.add(universes.get(sortedIndices[newindex]));
            }

            for (int i = 1; i < popSize; i++){
                for (int j = 0; j < dim; j++){
                    double r1 = RNG.nextDouble();
                    if (r1 < normalizedSortedInflationRates[i]){
                        double[] negSortedInflationRates = new double[sortedInflationRates.length];
                        for (int rate_i = 0; rate_i < sortedInflationRates.length; rate_i++)
                            negSortedInflationRates[rate_i] = -sortedInflationRates[rate_i];

                        int whiteHoleIndex = rouletteWheelSelection(negSortedInflationRates);
                        if (whiteHoleIndex == -1)
                            whiteHoleIndex = 0;
                        universes.get(i).setValue(j, sortedUniverses.get(whiteHoleIndex).getValue(j));
                    }

                    double r2 = RNG.nextDouble();
                    if (r2 < wep){
                        double r3 = RNG.nextDouble();
                        if (r3 < 0.5){
                            double r_travel_dist = RNG.nextDouble(lb.get(j), ub.get(j));
                            universes.get(i).setValue(j, task.problem.makeFeasible(bestUniverse.getValue(j) + tdr * r_travel_dist, j));
                        }
                        if (r3 > 0.5) {
                            double r_travel_dist = RNG.nextDouble(lb.get(j), ub.get(j));
                            universes.get(i).setValue(j, task.problem.makeFeasible(bestUniverse.getValue(j) - tdr * r_travel_dist, j));
                        }
                    }
                }
            }
            task.incrementNumberOfIterations();
        }

        return bestUniverse;
    }

    private void initPopulation() throws StopCriterionException {
        universes = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            universes.add(task.generateRandomEvaluatedSolution());
        }
        bestUniverse = universes.get(0);
    }

    private int rouletteWheelSelection(double[] weights){
        double sum = Arrays.stream(weights).sum();
        double[] accumulation = new double[weights.length];
        double cumulativeSum = 0.0;
        for (int i = 0; i < weights.length; i++){
            cumulativeSum += weights[i];
            accumulation[i] = cumulativeSum;
        }
        double p = RNG.nextDouble() * sum;
        for (int i = 0; i < accumulation.length; i++){
            if (accumulation[i] > p){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {}

}