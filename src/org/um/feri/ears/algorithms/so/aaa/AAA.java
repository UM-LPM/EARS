package org.um.feri.ears.algorithms.so.aaa;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Collections;

public class AAA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter(name = "shear force", description = "determines the boundaries of the search space within which each algal colony generate a new candidate solution which is inversely proportional to the size of the colony")
    private double shearForce = 2.0;
    @AlgorithmParameter(name = "loss of energy", description = "determines the number of new candidate solutions of algal colonies produced at each iteration")
    private double energyLoss = 0.3;
    @AlgorithmParameter(name = "adaptation probability constant", description = "determines the speed of the process in which algal colony, which could not find good solutions, tries to resemble itself to the biggest algal colony in the environment")
    private double adaptationConstant = 0.5;

    private NumberSolution<Double> best;
    private Alga[] population;

    public AAA() {
        this(50);
    }

    public AAA(int popSize) {

        this.popSize = popSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("AAA", "Artificial Algae Algorithm",
                "@article{uymaz2015artificial, " +
                        "title={Artificial algae algorithm (AAA) for nonlinear global optimization}, " +
                        "author={Uymaz, Sait Ali and Tezel, Gulay and Yel, Esra}, " +
                        "journal={Applied Soft Computing}, " +
                        "volume={31}, " +
                        "pages={153--171}, " +
                        "year={2015}, " +
                        "publisher={Elsevier}}"
                );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        initPopulation();
        calculateGreatness();
        while (!task.isStopCriterion()) {

            calculateEnergy();
            frictionForce();
            for (int i = 0; i < popSize; i++) {
                int iStarve = 0;
                while (population[i].getEnergy() >= 0) {
                    int neighbor = tournamentMethod();
                    while (neighbor == i) {
                        neighbor = tournamentMethod();
                    }
                    double[] newColony = Util.toDoubleArray(population[i].getVariables()).clone(); //clone necessary?

                    int dim1 = RNG.nextInt(task.problem.getNumberOfDimensions());
                    int dim2 = RNG.nextInt(task.problem.getNumberOfDimensions());
                    while (dim1 == dim2 ) {
                        dim2 = RNG.nextInt(task.problem.getNumberOfDimensions());
                    }

                    //In two-dimensional problems, algal movement is sinusoidal
                    double p = RNG.nextDouble(-1,1);
                    newColony[dim1] = newColony[dim1] + (population[neighbor].getValue(dim1) - newColony[dim1]) * (shearForce - population[i].getFriction()) * p;
                    int degree = (int) (360 * RNG.nextDouble());
                    newColony[dim2] = newColony[dim2] + (population[neighbor].getValue(dim2) - newColony[dim2]) * (shearForce - population[i].getFriction()) * Math.sin(Math.toRadians(degree));

                    //In case of three or more dimensions, algal movement is helical
                    if(task.problem.getNumberOfDimensions() > 2) {
                        int dim3 = RNG.nextInt(task.problem.getNumberOfDimensions());
                        while (dim1 == dim3 || dim2 == dim3) {
                            dim3 = RNG.nextInt(task.problem.getNumberOfDimensions());
                        }
                        degree = (int) (360 * RNG.nextDouble());
                        newColony[dim3] = newColony[dim3] + (population[neighbor].getValue(dim3) - newColony[dim3]) * (shearForce - population[i].getFriction()) * Math.cos(Math.toRadians(degree));
                    }

                    task.problem.setFeasible(newColony);

                    if (task.isStopCriterion())
                        break;

                    NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newColony));
                    task.eval(newSolution);
                    Alga newAlga = new Alga(newSolution);
                    population[i].setEnergy(population[i].getEnergy() - (energyLoss / 2));
                    if (task.problem.isFirstBetter(newAlga, population[i])) {
                        newAlga.copyAttributes(population[i]);
                        population[i] = newAlga;
                        iStarve = 1;
                        if (task.problem.isFirstBetter(newAlga, best))
                            best = new NumberSolution<>(newAlga);
                    } else {
                        population[i].setEnergy(population[i].getEnergy() - (energyLoss / 2));
                    }
                }

                if (iStarve == 0) {
                    population[i].setColonyStarving(population[i].getColonyStarving() + 1);
                }
            }
            calculateGreatness();
            int dim = (int) (task.problem.getNumberOfDimensions() * RNG.nextDouble());
            int minColony = 0, maxColony = 0;
            for (int i = 1; i < popSize; i++) {
                if (population[i].getColonySize() > population[maxColony].getColonySize())
                    maxColony = i;
                if (population[i].getColonySize() < population[minColony].getColonySize())
                    minColony = i;
            }
            population[minColony].setValue(dim, population[maxColony].getValue(dim));

            int maxStarving = 0;
            for (int i = 1; i < popSize; i++) {
                if (population[i].getColonyStarving() > population[maxStarving].getColonyStarving())
                    maxStarving = i;
            }

            if (RNG.nextDouble() < adaptationConstant) {
                for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                    population[maxStarving].setValue(i, population[maxStarving].getValue(i) + (best.getValue(i) - population[maxStarving].getValue(i)) * RNG.nextDouble());
                }
            }
            task.incrementNumberOfIterations();
        }
        return best;
    }

    private void calculateGreatness() {
        ArrayList<Double> sortedScores = new ArrayList<Double>();
        for (int i = 0; i < popSize; i++) {
            sortedScores.add(population[i].getEval());
        }
        Collections.sort(sortedScores);
        double maxValue = sortedScores.get(sortedScores.size() - 1);
        double minValue = sortedScores.get(0);
        double[] normalScore = new double[popSize];
        for (int i = 0; i < popSize; i++) {
            normalScore[i] = (population[i].getEval() - minValue) / (maxValue - minValue);
            normalScore[i] = 1 - normalScore[i];
        }
        for (int i = 0; i < popSize; i++) {
            double fKs = Math.abs(population[i].getColonySize() / 2); //half saturation constant
            double M = normalScore[i] / (fKs + normalScore[i]);
            double dX = M * population[i].getColonySize(); //greatness rate
            population[i].setColonySize(population[i].getColonySize() + dX);
        }
    }

    private void calculateEnergy() {
        int[] sort = new int[popSize];
        double[] fGreatSurface = new double[popSize];
        for (int i = 0; i < popSize; i++) {
            sort[i] = i;
        }
        for (int i = 0; i < (popSize - 1); i++) {
            for (int j = (i + 1); j < popSize; j++) {
                if (population[sort[i]].getColonySize() > population[sort[j]].getColonySize()) {
                    int value;
                    value = sort[i];
                    sort[i] = sort[j];
                    sort[j] = value;
                }
            }
            fGreatSurface[sort[i]] = Math.pow(i, 2);
        }
        fGreatSurface[sort[(popSize - 1)]] = Math.pow((popSize - 1), 2);
        int[] indis = minMaxIndex(fGreatSurface);
        double minValue = fGreatSurface[indis[0]];
        double maxValue = fGreatSurface[indis[1]];
        for (int i = 0; i < fGreatSurface.length; i++) {
            population[i].setEnergy((fGreatSurface[i] - minValue) / (maxValue - minValue));
        }
    }

    private int[] minMaxIndex(double[] iArray) {
        int maxIndex = 0;
        int minIndex = 0;
        for (int i = 1; i < iArray.length; i++) {
            if (iArray[maxIndex] < iArray[i]) {
                maxIndex = i;
            }
            if (iArray[minIndex] > iArray[i]) {
                minIndex = i;
            }
        }
        return new int[]{minIndex, maxIndex};
    }

    private void frictionForce() {
        double[] friction = new double[popSize];
        for (int i = 0; i < popSize; i++) {
            double r = (double) (population[i].getColonySize() * 3) / (double) (4 * Math.PI);
            r = Math.pow(r, (1.0 / 3));
            friction[i] = 2 * Math.PI * Math.pow(r, 2);
        }
        normalization(friction);
    }

    private void normalization(double[] friction) {
        ArrayList<Double> sortedScores = new ArrayList<Double>();
        for (double f : friction) {
            sortedScores.add(f);
        }
        Collections.sort(sortedScores);
        double maxValue = sortedScores.get(sortedScores.size() - 1);
        double minValue = sortedScores.get(0);
        for (int i = 0; i < friction.length; i++) {
            population[i].setFriction((friction[i] - minValue) / (maxValue - minValue));
        }
    }

    private int tournamentMethod() {
        int colonyOne = (int) ((popSize - 1) * RNG.nextDouble());
        int colonyTwo = (int) ((popSize - 1) * RNG.nextDouble());
        while (colonyOne == colonyTwo) {
            colonyTwo = (int) ((popSize - 1) * RNG.nextDouble());
        }
        if (task.problem.isFirstBetter(population[colonyOne], population[colonyTwo])) {
            return colonyOne;
        } else {
            return colonyTwo;
        }
    }

    private void initPopulation() throws StopCriterionException {
        population = new Alga[popSize];

        best = task.getRandomEvaluatedSolution();
        population[0] = new Alga(best);
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population[i] = new Alga(task.getRandomEvaluatedSolution());
            if (task.problem.isFirstBetter(population[i], best)) {
                best = new NumberSolution(population[i]);
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
