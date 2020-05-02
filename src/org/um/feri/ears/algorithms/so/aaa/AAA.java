package org.um.feri.ears.algorithms.so.aaa;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.so.fdo.Bee;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.Collections;

public class AAA extends Algorithm {

    DoubleSolution best;
    Task task;
    int popSize;
    private Alga[] population;

    double cutting = 2.0;
    double energyLoss = 0.3;
    double adaptationConstant = 0.5;

    public AAA() {
        this(50);
    }

    public AAA(int popSize) {

        this.popSize = popSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("AAA",
                "@article{uymaz2015artificial, " +
                        "title={Artificial algae algorithm (AAA) for nonlinear global optimization}, " +
                        "author={Uymaz, Sait Ali and Tezel, Gulay and Yel, Esra}, " +
                        "journal={Applied Soft Computing}, " +
                        "volume={31}, " +
                        "pages={153--171}, " +
                        "year={2015}, " +
                        "publisher={Elsevier}}",
                "AAA", "Artificial Algae Algorithm");
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
        task = taskProblem;

        initPopulation();
        calculateGreatness();
        while (!task.isStopCriteria()) {

            calculateEnergy();
            frictionForce();
            for (int i = 0; i < popSize; i++) {
                int iStarve = 0;
                while (population[i].getEnergy() >= 0) {
                    int neighbor = tournamentMethod();
                    while (neighbor == i) {
                        neighbor = tournamentMethod();
                    }
                    int dim1 = (int) (task.getNumberOfDimensions() * Util.nextDouble());
                    int dim2 = (int) (task.getNumberOfDimensions() * Util.nextDouble());
                    int dim3 = (int) (task.getNumberOfDimensions() * Util.nextDouble());
                    while (dim1 == dim2 || dim1 == dim3 || dim2 == dim3) {
                        dim2 = (int) (task.getNumberOfDimensions() * Util.nextDouble());
                        dim3 = (int) (task.getNumberOfDimensions() * Util.nextDouble());
                    }
                    double[] newColony;
                    newColony = population[i].getDoubleVariables().clone();
                    double p = -1 + (2 * Util.nextDouble());
                    int degree1 = (int) (360 * Util.nextDouble());
                    int degree2 = (int) (360 * Util.nextDouble());
                    newColony[dim1] = newColony[dim1] + (population[neighbor].getValue(dim1) - newColony[dim1]) * (cutting - population[i].getFriction()) * p;
                    newColony[dim1] = task.setFeasible(newColony[dim1], dim1);
                    newColony[dim2] = newColony[dim2] + (population[neighbor].getValue(dim2) - newColony[dim2]) * (cutting - population[i].getFriction()) * Math.cos(Math.toRadians(degree1));
                    newColony[dim2] = task.setFeasible(newColony[dim2], dim2);
                    newColony[dim3] = newColony[dim3] + (population[neighbor].getValue(dim3) - newColony[dim3]) * (cutting - population[i].getFriction()) * Math.sin(Math.toRadians(degree2));
                    newColony[dim3] = task.setFeasible(newColony[dim3], dim3);

                    if (task.isStopCriteria())
                        break;

                    Alga newAlga = new Alga(task.eval(newColony));
                    population[i].setEnergy(population[i].getEnergy() - (energyLoss / 2));
                    if (task.isFirstBetter(newAlga, population[i])) {
                        newAlga.copyAttributes(population[i]);
                        population[i] = newAlga;
                        iStarve = 1;
                        if (task.isFirstBetter(newAlga, best))
                            best = new DoubleSolution(newAlga);
                    } else {
                        population[i].setEnergy(population[i].getEnergy() - (energyLoss / 2));
                    }
                }

                if (iStarve == 0) {
                    population[i].setColonyStarving(population[i].getColonyStarving() + 1);
                }
            }
            calculateGreatness();
            int dim = (int) (task.getNumberOfDimensions() * Util.nextDouble());
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

            if (Util.nextDouble() < adaptationConstant) {
                for (int i = 0; i < task.getNumberOfDimensions(); i++) {
                    population[maxStarving].setValue(i, population[maxStarving].getValue(i) + (best.getValue(i) - population[maxStarving].getValue(i)) * Util.nextDouble());
                }
            }
            task.incrementNumberOfIterations();
        }
        return best;
    }

    void calculateGreatness() {
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

    void calculateEnergy() {
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

    int[] minMaxIndex(double[] iArray) {
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

    void frictionForce() {
        double[] friction = new double[popSize];
        for (int i = 0; i < popSize; i++) {
            double r = (double) (population[i].getColonySize() * 3) / (double) (4 * Math.PI);
            r = Math.pow(r, (1.0 / 3));
            friction[i] = 2 * Math.PI * Math.pow(r, 2);
        }
        normalization(friction);
    }

    void normalization(double[] friction) {
        ArrayList<Double> sortedScores = new ArrayList<Double>();
        for (int i = 0; i < friction.length; i++) {
            sortedScores.add(friction[i]);
        }
        Collections.sort(sortedScores);
        double maxValue = sortedScores.get(sortedScores.size() - 1);
        double minValue = sortedScores.get(0);
        for (int i = 0; i < friction.length; i++) {
            population[i].setFriction((friction[i] - minValue) / (maxValue - minValue));
        }
    }

    int tournamentMethod() {
        int colonyOne = (int) ((popSize - 1) * Util.nextDouble());
        int colonyTwo = (int) ((popSize - 1) * Util.nextDouble());
        while (colonyOne == colonyTwo) {
            colonyTwo = (int) ((popSize - 1) * Util.nextDouble());
        }
        if (task.isFirstBetter(population[colonyOne], population[colonyTwo])) {
            return colonyOne;
        } else {
            return colonyTwo;
        }
    }

    private void initPopulation() throws StopCriteriaException {
        population = new Alga[popSize];

        best = task.getRandomSolution();
        population[0] = new Alga(best);
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriteria())
                break;
            population[i] = new Alga(task.getRandomSolution());
            if (task.isFirstBetter(population[i], best)) {
                best = new DoubleSolution(population[i]);
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
