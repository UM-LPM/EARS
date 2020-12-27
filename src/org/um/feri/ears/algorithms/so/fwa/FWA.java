package org.um.feri.ears.algorithms.so.fwa;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class FWA extends Algorithm {

    private DoubleSolution bestSpark;

    private int popSize;
    private Task task;

    private double numMaxAmplitude;
    private int numGaussianSparks;
    private double numBoundA;
    private double numBoundB;
    private double eps;
    private int numMaxSparks;

    private double[] maxBound;
    private double[] minBound;

    private DoubleSolution[] fireworks;
    private DoubleSolution[][] sparks;
    private DoubleSolution[] gaussiansparks;

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
        ai = new AlgorithmInfo("FWA",
                "@inproceedings{tan2010fireworks,"
                        + "title={Fireworks algorithm for optimization},"
                        + "author={Tan, Ying and Zhu, Yuanchun},"
                        + "booktitle={International Conference in Swarm Intelligence},"
                        + "pages={355--364},"
                        + "year={2010},"
                        + "organization={Springer}}",
                "FWA", "Flower Pollination Algorithm");
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriterionException {
        task = taskProblem;
        initPopulation();

        maxBound = task.getUpperLimit();
        minBound = task.getLowerLimit();

        while (!task.isStopCriterion()) {

            //set off n fireworks
            setoff();
            if (task.isStopCriterion())
                break;
            //select n locations
            selectlocations();

            task.incrementNumberOfIterations();
        }
        return bestSpark;
    }

    //set off n fireworks
    private void setoff() throws StopCriterionException {

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
        double tmpcoef;
        for (i = 0; i < popSize; i++) {
            tmpcoef = (maxvalue - fireworks[i].getEval() + eps) / (summaxdiff + eps);
            if (tmpcoef < numBoundA) {
                tmpcoef = numBoundA;
            }
            if (tmpcoef > numBoundB) {
                tmpcoef = numBoundB;
            }
            numSparks[i] = (int) (numMaxSparks * tmpcoef);
        }

        //get amplitude of explosion for all fireworks
        double[] ampExplosion = new double[popSize];
        for (i = 0; i < popSize; i++) {
            ampExplosion[i] = (fireworks[i].getEval() - minvalue + eps) / (summindiff + eps) * numMaxAmplitude;
        }

        //generate sparks for all fireworks
        sparks = new DoubleSolution[popSize][];
        //temporary position
        double[] tmppos = new double[task.getNumberOfDimensions()];
        double[] fireworkpos;
        for (i = 0; i < popSize; i++) {
            sparks[i] = new DoubleSolution[numSparks[i]];
            fireworkpos = fireworks[i].getDoubleVariables();
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
                int randomcount = 0;
                int tmprand;
                while (randomcount < numExplosionDirections) {
                    tmprand = Util.nextInt(task.getNumberOfDimensions());
                    if (!randflag[tmprand]) {
                        randflag[tmprand] = true;
                        randomcount++;
                    }
                }
                //explode
                double displacement = ampExplosion[i] * (Util.nextDouble() - 0.5) * 2;
                for (j = 0; j < task.getNumberOfDimensions(); j++) {
                    if (randflag[j]) {
                        tmppos[j] = fireworkpos[j] + displacement;
                        //out of bound
                        if (tmppos[j] < minBound[j] || tmppos[j] > maxBound[j]) {
                            double abspos = Math.abs(tmppos[j]);
                            while (abspos >= 0) {
                                abspos -= (maxBound[j] - minBound[j]);
                            }
                            abspos += (maxBound[j] - minBound[j]);
                            tmppos[j] = minBound[j] + abspos;
                        }
                    } else {
                        tmppos[j] = fireworkpos[j];
                    }
                }
                //set position of the spark
                // Check bounds
                tmppos = task.setFeasible(tmppos);
                // Evaluate new solution
                if (task.isStopCriterion())
                    break;
                sparks[i][k] = task.eval(tmppos);
            }
        }

        //gaussian explode
        gaussiansparks = new DoubleSolution[numGaussianSparks];
        int k;
        for (k = 0; k < numGaussianSparks; k++) {
            //gaussiansparks[k] = new DoubleSolution();
            //randomly select a firework
            i = Math.abs(Util.nextInt()) % popSize;
            fireworkpos = fireworks[i].getDoubleVariables();
            //select z directions
            boolean[] randflag = new boolean[task.getNumberOfDimensions()];
            int j;
            for (j = 0; j < task.getNumberOfDimensions(); j++) {
                randflag[j] = false;
            }
            int numExplosionDirections = (int) (task.getNumberOfDimensions() * Util.nextDouble());
            int randomcount = 0;
            int tmprand;
            while (randomcount < numExplosionDirections) {
                tmprand = Math.abs(Util.nextInt()) % task.getNumberOfDimensions();
                if (!randflag[tmprand]) {
                    randflag[tmprand] = true;
                    randomcount++;
                }
            }
            //explode
            double gaussianCoef = 1.0 + Util.nextGaussian();
            for (j = 0; j < task.getNumberOfDimensions(); j++) {
                if (randflag[j]) {
                    tmppos[j] = fireworkpos[j] * gaussianCoef;
                    //out of bound
                    if (tmppos[j] < minBound[j] || tmppos[j] > maxBound[j]) {
                        double abspos = Math.abs(tmppos[j]);
                        while (abspos >= 0) {
                            abspos -= (maxBound[j] - minBound[j]);
                        }
                        abspos += (maxBound[j] - minBound[j]);
                        tmppos[j] = minBound[j] + abspos;
                    }
                } else {
                    tmppos[j] = fireworkpos[j];
                }
            }
            //set position of the spark
            // Check bounds
            tmppos = task.setFeasible(tmppos);
            // Evaluate new solution
            if (task.isStopCriterion())
                break;
            gaussiansparks[k] = task.eval(tmppos);
        }
    }

    //select n locations
    private void selectlocations() {
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
            if (task.isFirstBetter(gaussiansparks[i], bestSpark)) {
                bestSpark = gaussiansparks[i];
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
        double[][] fireworkspos = new double[numFireworksSparks][];
        int idx = 0;
        for (i = 0; i < popSize; i++) {
            fireworkspos[idx] = fireworks[i].getDoubleVariables();
            idx++;
        }
        for (i = 0; i < popSize; i++) {
            for (j = 0; j < sparks[i].length; j++) {
                fireworkspos[idx] = sparks[i][j].getDoubleVariables();
                idx++;
            }
        }
        for (i = 0; i < numGaussianSparks; i++) {
            fireworkspos[idx] = gaussiansparks[i].getDoubleVariables();
            idx++;
        }
        //calculate the selection probability of each location
        double[] selectionprobability = new double[numFireworksSparks];
        double sumprob = 0;
        for (i = 0; i < numFireworksSparks; i++) {
            selectionprobability[i] = 0;
            for (j = 0; j < numFireworksSparks; j++) {
                double tmpdis = 0;
                for (k = 0; k < task.getNumberOfDimensions(); k++) {
                    tmpdis += (fireworkspos[i][k] - fireworkspos[j][k]) * (fireworkspos[i][k] - fireworkspos[j][k]);
                }
                selectionprobability[i] += Math.sqrt(tmpdis);
            }
            sumprob += selectionprobability[i];
        }
        double[] cumulativeprobability = new double[numFireworksSparks];
        for (i = 0; i < numFireworksSparks; i++) {
            if (sumprob < eps) {
                selectionprobability[i] = 1.0 / numFireworksSparks;
            } else {
                selectionprobability[i] /= sumprob;
            }
            if (i == 0) {
                cumulativeprobability[i] = selectionprobability[i];
            } else {
                cumulativeprobability[i] = cumulativeprobability[i - 1] + selectionprobability[i];
            }
        }
        //select n-1 locations according to the selection probability
        int[] nextlocations = new int[popSize - 1];
        for (k = 0; k < popSize - 1; k++) {
            double randpointer = Util.nextDouble();
            for (i = 0; i < numFireworksSparks; i++) {
                if (randpointer <= cumulativeprobability[i]) {
                    break;
                }
            }
            nextlocations[k] = i;
        }
        //set next generations
        DoubleSolution[] nextfireworks = new DoubleSolution[popSize];
        nextfireworks[popSize - 1] = bestSpark;
        boolean breakflag;
        for (k = 0; k < popSize - 1; k++) {
            idx = 0;
            breakflag = false;
            for (i = 0; i < popSize; i++) {
                if (idx == nextlocations[k]) {
                    nextfireworks[k] = fireworks[i];
                    breakflag = true;
                    break;
                }
                idx++;
            }
            if (breakflag) {
                continue;
            }
            for (i = 0; i < popSize; i++) {
                for (j = 0; j < sparks[i].length; j++) {
                    if (idx == nextlocations[k]) {
                        nextfireworks[k] = sparks[i][j];
                        breakflag = true;
                        break;
                    }
                    idx++;
                }
                if (breakflag) {
                    break;
                }
            }
            if (breakflag) {
                continue;
            }
            for (i = 0; i < numGaussianSparks; i++) {
                if (idx == nextlocations[k]) {
                    nextfireworks[k] = gaussiansparks[i];
                    breakflag = true;
                    break;
                }
                idx++;
            }
        }
        fireworks = nextfireworks;
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
