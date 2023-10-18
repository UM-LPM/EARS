package org.um.feri.ears.algorithms.so.jade;

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

public class JADE extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter(description = "CR mean - adaptive control parameter")
    double muCR = 0.5;
    @AlgorithmParameter(description = "location parameter - adaptive control parameter")
    double muF = 0.5;


    // private int arch_size;
    private int eliteSize; // calculated by p*pop_size pbest
    private double p; // % DE/Current-to-pbest
    private double c; // helps
    // private double F,CR;
    private ArrayList<JADESolution> elite; // pbest
    private JADESolution[] popX; // population
    private ArrayList<JADESolution> archX; // population
    private JADESolution g; // global best

    private ArrayList<Double> SCR; // list of successful F, CR in current gen
    private ArrayList<Double> SF; //

    public JADE() {
        this(30);
    }

    public JADE(int popSize) {
        this(popSize, .05, .1);
    }

    public JADE(int popSize, double p, double c) {
        super();
        // by paper p is in [0.05-0.2]
        // by paper c is in [0.05-0.2]
        // by paper pop_size (D < 10) = 30; (D=30) = 100; (D=100) = 400
        debug = false;
        this.popSize = popSize;
        this.c = c;
        this.p = p;
        eliteSize = (int) Math.round(popSize * p);
        elite = new ArrayList<JADESolution>();
        archX = new ArrayList<JADESolution>();
        SCR = new ArrayList<Double>();
        SF = new ArrayList<Double>();
        if (eliteSize == 0)
            eliteSize = 1;
        setDebug(debug); // EARS prints some debug info
        ai = new AlgorithmInfo(
                "JADE",
                "Adaptive Differential Evolution With Optional External Archive",
                "Jingqiao Zhang, Arthur C. Sanderson");
        // ai.addParameter(EnumAlgorithmParameters., F + "");
        au = new Author("Matej", "matej.crepinsek@um.si");
    }

    /*
     * Keep track of best individuals pbest! Also sets global best
     */
    private void updateEliteAndGlobalBest(JADESolution in) {
        boolean add = false;
        for (int i = 0; i < elite.size(); i++) {
            if (task.problem.isFirstBetter(in, elite.get(i))) {
                elite.add(i, in);
                add = true;
                break;
            }
        }
        if ((!add) && (eliteSize >= elite.size())) {
            elite.add(in);
        }

        if (g == null)
            g = in;
        else if (task.problem.isFirstBetter(in, g))
            g = in;
        if (eliteSize < elite.size())
            elite.remove(eliteSize);
    }

    private void initPopulation() throws StopCriterionException {
        popX = new JADESolution[popSize];
        for (int i = 0; i < popSize; i++) {
            popX[i] = new JADESolution(task.getRandomEvaluatedSolution(), 0.5, 0.5);
            updateEliteAndGlobalBest(popX[i]);
            if (task.isStopCriterion())
                break;
        }
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        g = null;
        this.task = task;
        elite.clear();
        archX.clear();
        JADESolution[] popNew = new JADESolution[popSize];
        double[] tmp;
        int jRand;
        int D = task.problem.getNumberOfDimensions();
        int r1, r2, pBest;
        double Fpom;
        JADESolution inR2, tmpIn;

        initPopulation();

        while (!task.isStopCriterion()) {
            SF.clear();
            SCR.clear();
            for (int i = 0; i < popSize; i++) {
                // Generate CRi
                popX[i].setCR(RNG.nextGaussian() * 0.1 + muCR);
                // http://introcs.cs.princeton.edu/java/stdlib/StdRandom.java.html
                // cauchy
                // Generate Fi
                do {
                    Fpom = 0.1
                            * Math.tan(Math.PI * (RNG.nextDouble() - 0.5))
                            + muF;
                } while (Fpom <= 0);
                popX[i].setF(Fpom);
                // System.out.print(
                // "("+pop_x[i].getCR()+", "+pop_x[i].getF()+") ");
                jRand = RNG.nextInt(D);
                tmp = Util.toDoubleArray(popX[i].getVariables());
                do {
                    r1 = RNG.nextInt(popSize);
                } while (r1 == i);
                do {
                    r2 = RNG.nextInt(popSize
                            + Math.min(archX.size(), popSize));
                } while (r2 == i || r2 == r1);
                if (r2 < popSize)
                    inR2 = popX[r2];
                else
                    inR2 = archX.get(r2 - popSize);
                pBest = RNG.nextInt(eliteSize);
                for (int d = 0; d < D; d++) {
                    if ((RNG.nextDouble() < popX[i].CR) || (d == jRand)) {
                        tmp[d] = task.problem.setFeasible(
                                        tmp[d]
                                                + popX[i].F
                                                * (elite.get(pBest).getValue(d) - tmp[d])
                                                + popX[i].F
                                                * (popX[r1].getValue(d) - inR2
                                                .getValue(d)), d);
                    }
                }

                NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(tmp));
                task.eval(newSolution);

                tmpIn = new JADESolution(newSolution, popX[i].CR,
                        popX[i].F);
                if (task.problem.isFirstBetter(tmpIn, popX[i])) {
                    SCR.add(tmpIn.CR); // save successful parameters
                    SF.add(tmpIn.F);
                    archX.add(popX[i]); // save old; good
                    popNew[i] = tmpIn;
                    updateEliteAndGlobalBest(tmpIn);
                } else {
                    popNew[i] = popX[i]; // old is in new population
                }
                if (task.isStopCriterion())
                    break;
            }
            // new generation
            if (popSize >= 0) System.arraycopy(popNew, 0, popX, 0, popSize);
            // empty archive if it is too big
            while (archX.size() > popSize)
                archX.remove(RNG.nextInt(archX.size())); // arch full
            // Update parameters
            if (SCR.size() > 0) {
                muCR = (1. - c) * muCR + c * (sum(SCR) / SCR.size());
                muF = (1. - c) * muF + c * (sum2(SF) / sum(SF)); // Lehmer mean
            } else { // not defined in paper
                muCR = RNG.nextDouble();
                if (muCR < 0.1)
                    muCR = 0.1;
                muF = RNG.nextDouble();
                if (muF < 0.1)
                    muF = 0.1;
            }
            // System.out.println("\nmuCR:" + muCR + " " + " muF:" +muF);
            task.incrementNumberOfIterations();
        }
        return g;
    }

    private double sum(ArrayList<Double> a) {
        double s = 0;
        for (Double aDouble : a) {
            s += aDouble;
        }
        return s;
    }

    private double sum2(ArrayList<Double> a) {
        double s = 0;
        for (Double aDouble : a) {
            s += aDouble * aDouble;
        }
        return s;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        // by paper p is in [0.05-0.2]
        // by paper c is in [0.05-0.2]
        // by paper pop_size (D < 10) = 30; (D=30) = 100; (D=100) = 400
    }

}
