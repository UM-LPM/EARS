package org.um.feri.ears.algorithms.so.fwa;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

public class FWA extends Algorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter(name = "max number of sparks")
    private int numMaxSparks;
    @AlgorithmParameter(name = "max amplitude")
    private double numMaxAmplitude;
    @AlgorithmParameter(name = "number of gaussian sparks")
    private int numGaussianSparks;
    @AlgorithmParameter(name = "bound a")
    private double numBoundA;
    @AlgorithmParameter(name = "bound b")
    private double numBoundB;
    private double eps;

    private Task task;
    private DoubleSolution bestSpark;

    private double[] maxBound;
    private double[] minBound;

    private DoubleSolution[] fireworks;
    private DoubleSolution[][] sparks;
    private DoubleSolution[] gaussianSparks;

    public FWA() {
        this(5, 50, 0.04, 0.8, 40, 5);
    }

    public FWA(int popSize, int numMaxSparks, double numBoundA, double numBoundB, double numMaxAmplitude, int numGaussianSparks) {
        super();
        this.popSize = popSize;
        this.numMaxSparks = numMaxSparks;
        this.numBoundA = numBoundA;
        this.numBoundB = numBoundB;
        this.numMaxAmplitude = numMaxAmplitude;
        this.numGaussianSparks = numGaussianSparks;

        eps = 1e-38;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("FWA", "Fireworks algorithm for optimization",
                "@inproceedings{tan2010fireworks,"
                        + "title={Fireworks algorithm for optimization},"
                        + "author={Tan, Ying and Zhu, Yuanchun},"
                        + "booktitle={International Conference in Swarm Intelligence},"
                        + "pages={355--364},"
                        + "year={2010},"
                        + "organization={Springer}}"
        );
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriterionException {
        task = taskProblem;
        initPopulation();

        maxBound = task.getUpperLimit();
        minBound = task.getLowerLimit();

        while (!task.isStopCriterion()) {

            //set off n fireworks
            setOff();
            if (task.isStopCriterion())
                break;
            //select n locations
            selectLocations();

            task.incrementNumberOfIterations();
        }
        return bestSpark;
    }

    //set off n fireworks
    private void setOff() throws StopCriterionException {

        //TODO minimization maximization
        //get max(worst) and min(best) value
        double maxvalue = fireworks[0].getEval();
        double minvalue = fireworks[0].getEval();
        int i;
        for (i = 1; i < popSize; i++) {
            if (fireworks[i].getEval() > maxvalue) {
                maxvalue = fireworks[i].getEval();
            }
            if (fireworks[i].getEval() < minvalue) {
                minvalue = fireworks[i].getEval();
            }
        }
        double summaxdiff = 0.0;
        double summindiff = 0.0;
        for (i = 0; i < popSize; i++) {
            summaxdiff += maxvalue - fireworks[i].getEval();
            summindiff += fireworks[i].getEval() - minvalue;
        }

        //get number of sparks for all fireworks
        int[] numSparks = new int[popSize];
        double tmpCoef;
        for (i = 0; i < popSize; i++) {
            tmpCoef = (maxvalue - fireworks[i].getEval() + eps) / (summaxdiff + eps);
            if (tmpCoef < numBoundA) {
                tmpCoef = numBoundA;
            }
            if (tmpCoef > numBoundB) {
                tmpCoef = numBoundB;
            }
            numSparks[i] = (int) (numMaxSparks * tmpCoef);
        }

        //get amplitude of explosion for all fireworks
        double[] ampExplosion = new double[popSize];
        for (i = 0; i < popSize; i++) {
            ampExplosion[i] = (fireworks[i].getEval() - minvalue + eps) / (summindiff + eps) * numMaxAmplitude;
        }

        //generate sparks for all fireworks
        sparks = new DoubleSolution[popSize][];
        //temporary position
        double[] tmpPos = new double[task.getNumberOfDimensions()];
        double[] fireworkPos;
        for (i = 0; i < popSize; i++) {
            sparks[i] = new DoubleSolution[numSparks[i]];
            fireworkPos = fireworks[i].getDoubleVariables();
            //get all sparks' position
            int k;
            for (k = 0; k < numSparks[i]; k++) {
                //sparks[i][k] = new DoubleSolution();
                //select z directions

                boolean[] randflag = new boolean[task.getNumberOfDimensions()];
                int j;
                for (j = 0; j < task.getNumberOfDimensions(); j++) {
                    randflag[j] = false;
                }
                int numExplosionDirections = (int) (task.getNumberOfDimensions() * Util.nextDouble());
                int randomCount = 0;
                int tmpRand;
                while (randomCount < numExplosionDirections) {
                    tmpRand = Util.nextInt(task.getNumberOfDimensions());
                    if (!randflag[tmpRand]) {
                        randflag[tmpRand] = true;
                        randomCount++;
                    }
                }
                //explode
                double displacement = ampExplosion[i] * (Util.nextDouble() - 0.5) * 2;
                for (j = 0; j < task.getNumberOfDimensions(); j++) {
                    if (randflag[j]) {
                        tmpPos[j] = fireworkPos[j] + displacement;
                        //out of bound
                        if (tmpPos[j] < minBound[j] || tmpPos[j] > maxBound[j]) {
                            double abspos = Math.abs(tmpPos[j]);
                            while (abspos >= 0) {
                                abspos -= (maxBound[j] - minBound[j]);
                            }
                            abspos += (maxBound[j] - minBound[j]);
                            tmpPos[j] = minBound[j] + abspos;
                        }
                    } else {
                        tmpPos[j] = fireworkPos[j];
                    }
                }
                //set position of the spark
                // Check bounds
                tmpPos = task.setFeasible(tmpPos);
                // Evaluate new solution
                if (task.isStopCriterion())
                    break;
                sparks[i][k] = task.eval(tmpPos);
            }
        }

        //gaussian explode
        gaussianSparks = new DoubleSolution[numGaussianSparks];
        int k;
        for (k = 0; k < numGaussianSparks; k++) {
            //gaussiansparks[k] = new DoubleSolution();
            //randomly select a firework
            i = Math.abs(Util.nextInt()) % popSize;
            fireworkPos = fireworks[i].getDoubleVariables();
            //select z directions
            boolean[] randFlag = new boolean[task.getNumberOfDimensions()];
            int j;
            for (j = 0; j < task.getNumberOfDimensions(); j++) {
                randFlag[j] = false;
            }
            int numExplosionDirections = (int) (task.getNumberOfDimensions() * Util.nextDouble());
            int randomcount = 0;
            int tmprand;
            while (randomcount < numExplosionDirections) {
                tmprand = Math.abs(Util.nextInt()) % task.getNumberOfDimensions();
                if (!randFlag[tmprand]) {
                    randFlag[tmprand] = true;
                    randomcount++;
                }
            }
            //explode
            double gaussianCoef = 1.0 + Util.nextGaussian();
            for (j = 0; j < task.getNumberOfDimensions(); j++) {
                if (randFlag[j]) {
                    tmpPos[j] = fireworkPos[j] * gaussianCoef;
                    //out of bound
                    if (tmpPos[j] < minBound[j] || tmpPos[j] > maxBound[j]) {
                        double abspos = Math.abs(tmpPos[j]);
                        while (abspos >= 0) {
                            abspos -= (maxBound[j] - minBound[j]);
                        }
                        abspos += (maxBound[j] - minBound[j]);
                        tmpPos[j] = minBound[j] + abspos;
                    }
                } else {
                    tmpPos[j] = fireworkPos[j];
                }
            }
            //set position of the spark
            // Check bounds
            tmpPos = task.setFeasible(tmpPos);
            // Evaluate new solution
            if (task.isStopCriterion())
                break;
            gaussianSparks[k] = task.eval(tmpPos);
        }
    }

    //select n locations
    private void selectLocations() {
        //select the best location
        int i, j, k;
        for (i = 0; i < popSize; i++) {
            if (task.isFirstBetter(fireworks[i], bestSpark)) {
                bestSpark = fireworks[i];
            }
        }
        for (i = 0; i < popSize; i++) {
            for (j = 0; j < sparks[i].length; j++) {
                if (task.isFirstBetter(sparks[i][j], bestSpark)) {
                    bestSpark = sparks[i][j];
                }
            }
        }
        for (i = 0; i < numGaussianSparks; i++) {
            if (task.isFirstBetter(gaussianSparks[i], bestSpark)) {
                bestSpark = gaussianSparks[i];
            }
        }
        //select the rest n-1 locations
        //count the number of fireworks and sparks
        int numFireworksSparks = popSize + numGaussianSparks;
        for (i = 0; i < popSize; i++) {
            for (j = 0; j < sparks[i].length; j++) {
                numFireworksSparks++;
            }
        }

        //put all the fireworks and sparks in an array
        double[][] fireworksPos = new double[numFireworksSparks][];
        int idx = 0;
        for (i = 0; i < popSize; i++) {
            fireworksPos[idx] = fireworks[i].getDoubleVariables();
            idx++;
        }
        for (i = 0; i < popSize; i++) {
            for (j = 0; j < sparks[i].length; j++) {
                fireworksPos[idx] = sparks[i][j].getDoubleVariables();
                idx++;
            }
        }
        for (i = 0; i < numGaussianSparks; i++) {
            fireworksPos[idx] = gaussianSparks[i].getDoubleVariables();
            idx++;
        }
        //calculate the selection probability of each location
        double[] selectionProbability = new double[numFireworksSparks];
        double sumProb = 0;
        for (i = 0; i < numFireworksSparks; i++) {
            selectionProbability[i] = 0;
            for (j = 0; j < numFireworksSparks; j++) {
                double tmpdis = 0;
                for (k = 0; k < task.getNumberOfDimensions(); k++) {
                    tmpdis += (fireworksPos[i][k] - fireworksPos[j][k]) * (fireworksPos[i][k] - fireworksPos[j][k]);
                }
                selectionProbability[i] += Math.sqrt(tmpdis);
            }
            sumProb += selectionProbability[i];
        }
        double[] cumulativeProbability = new double[numFireworksSparks];
        for (i = 0; i < numFireworksSparks; i++) {
            if (sumProb < eps) {
                selectionProbability[i] = 1.0 / numFireworksSparks;
            } else {
                selectionProbability[i] /= sumProb;
            }
            if (i == 0) {
                cumulativeProbability[i] = selectionProbability[i];
            } else {
                cumulativeProbability[i] = cumulativeProbability[i - 1] + selectionProbability[i];
            }
        }
        //select n-1 locations according to the selection probability
        int[] nextLocations = new int[popSize - 1];
        for (k = 0; k < popSize - 1; k++) {
            double randPointer = Util.nextDouble();
            for (i = 0; i < numFireworksSparks; i++) {
                if (randPointer <= cumulativeProbability[i]) {
                    break;
                }
            }
            nextLocations[k] = i;
        }
        //set next generations
        DoubleSolution[] nextFireworks = new DoubleSolution[popSize];
        nextFireworks[popSize - 1] = bestSpark;
        boolean breakFlag;
        for (k = 0; k < popSize - 1; k++) {
            idx = 0;
            breakFlag = false;
            for (i = 0; i < popSize; i++) {
                if (idx == nextLocations[k]) {
                    nextFireworks[k] = fireworks[i];
                    breakFlag = true;
                    break;
                }
                idx++;
            }
            if (breakFlag) {
                continue;
            }
            for (i = 0; i < popSize; i++) {
                for (j = 0; j < sparks[i].length; j++) {
                    if (idx == nextLocations[k]) {
                        nextFireworks[k] = sparks[i][j];
                        breakFlag = true;
                        break;
                    }
                    idx++;
                }
                if (breakFlag) {
                    break;
                }
            }
            if (breakFlag) {
                continue;
            }
            for (i = 0; i < numGaussianSparks; i++) {
                if (idx == nextLocations[k]) {
                    nextFireworks[k] = gaussianSparks[i];
                    breakFlag = true;
                    break;
                }
                idx++;
            }
        }
        fireworks = nextFireworks;
    }

    private void initPopulation() throws StopCriterionException {
        fireworks = new DoubleSolution[popSize];
        fireworks[0] = task.getRandomEvaluatedSolution();
        bestSpark = new DoubleSolution(fireworks[0]);

        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            DoubleSolution newSolution = task.getRandomEvaluatedSolution();
            fireworks[i] = newSolution;
            if (task.isFirstBetter(newSolution, bestSpark)) {
                bestSpark = new DoubleSolution(newSolution);
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
