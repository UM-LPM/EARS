package org.um.feri.ears.algorithms.so.de;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;


/* jDE[1] - Janez Brest */
/* =========================== */
/*
 [1] J. Brest, S. Greiner, B. Boskovic, M. Mernik, V. Zumer. 
 Self-adapting control parameters in differential evolution: a 
 comparative study on numerical benchmark problems. 
 IEEE Transactions on Evolutionary Computation, 2006, vol. 10, 
 no. 6, pp. 646-657. DOI 10.1109/TEVC.2006.872133

 More at: http://labraj.uni-mb.si/raj/en/index.php/Evolutionary_algorithm_jDE

 */
/***************************************************************
 **                                                            **
 **        D I F F E R E N T I A L     E V O L U T I O N       **
 **                                                            **
 ** Program: de.c                                              **
 ** Version: 3.6                                               **
 **                                                            **
 ** Authors: Dr. Rainer Storn                                  **
 **          c/o ICSI, 1947 Center Street, Suite 600           **
 **          Berkeley, CA 94707                                **
 **          Tel.:   510-642-4274 (extension 192)              **
 **          Fax.:   510-643-7684                              **
 **          E-mail: storn@icsi.berkeley.edu                   **
 **          WWW: http://http.icsi.berkeley.edu/~storn/        **
 **          on leave from                                     **
 **          Siemens AG, ZFE T SN 2, Otto-Hahn Ring 6          **
 **          D-81739 Muenchen, Germany                         **
 **          Tel:    636-40502                                 **
 **          Fax:    636-44577                                 **
 **          E-mail: rainer.storn@zfe.siemens.de               **
 **                                                            **
 **          Kenneth Price                                     **
 **          836 Owl Circle                                    **
 **          Vacaville, CA 95687                               **
 **          E-mail: kprice@solano.community.net               ** 
 **                                                            **
 ** This program implements some variants of Differential      **
 ** Evolution (DE) as described in part in the techreport      **
 ** tr-95-012.ps of ICSI. You can get this report either via   **
 ** ftp.icsi.berkeley.edu/pub/techreports/1995/tr-95-012.ps.Z  **
 ** or via WWW: http://http.icsi.berkeley.edu/~storn/litera.html*
 ** A more extended version of tr-95-012.ps is submitted for   **
 ** publication in the Journal Evolutionary Computation.       ** 
 **                                                            **
 ** You may use this program for any purpose, give it to any   **
 ** person or change it according to your needs as long as you **
 ** are referring to Rainer Storn and Ken Price as the origi-  **
 ** nators of the the DE idea.                                 **
 ** If you have questions concerning DE feel free to contact   **
 ** us. We also will be happy to know about your experiences   **
 ** with DE and your suggestions of improvement.               **
 **                                                            **
 ***************************************************************/

/**
 * H*O*C************************************************************** **
 * No.!Version! Date ! Request ! Modification ! Author **
 * ---+-------+------+---------+---------------------------+------- ** 1 + 3.1
 * +5/18/95+ - + strategy DE/rand-to-best/1+ Storn ** + + + + included + ** 1 +
 * 3.2 +6/06/95+C.Fleiner+ change loops into memcpy + Storn ** 2 + 3.2 +6/06/95+
 * - + update comments + Storn ** 1 + 3.3 +6/15/95+ K.Price + strategy DE/best/2
 * incl. + Storn ** 2 + 3.3 +6/16/95+ - + comments and beautifying + Storn ** 3
 * + 3.3 +7/13/95+ - + upper and lower bound for + Storn ** + + + +
 * initialization + ** 1 + 3.4 +2/12/96+ - + increased printout prec. + Storn **
 * 1 + 3.5 +5/28/96+ - + strategies revisited + Storn ** 2 + 3.5 +5/28/96+ - +
 * strategy DE/rand/2 incl. + Storn ** 1 + 3.6 +8/06/96+ K.Price + Binomial
 * Crossover added + Storn ** 2 + 3.6 +9/30/96+ K.Price + cost variance output +
 * Storn ** 3 + 3.6 +9/30/96+ - + alternative to ASSIGND + Storn ** 4 + 3.6
 * +10/1/96+ - + variable checking inserted + Storn ** 5 + 3.6 +10/1/96+ - +
 * strategy indic. improved + Storn ** ** jDE - J. Brest + Self-adaptive
 * strategy, s20 + Brest ** H*O*C*E
 ***********************************************************/

public class DEAlgorithm extends NumberAlgorithm {

    public enum Strategy {
        DE_BEST_1_EXP("DE/best/1/exp"),
        DE_RAND_1_EXP("DE/rand/1/exp"),
        DE_RAND_TO_BEST_1_EXP("DE/rand-to-best/1/exp"),
        DE_BEST_2_EXP("DE/best/2/exp"),
        DE_RAND_2_EXP("DE/rand/2/exp"),
        DE_BEST_1_BIN("DE/best/1/bin"),
        DE_RAND_1_BIN("DE/rand/1/bin"),
        DE_RAND_TO_BEST_1_BIN("DE/rand-to-best/1/bin"),
        DE_BEST_2_BIN("DE/best/2/bin"),
        DE_RAND_2_BIN("DE/rand/2/bin"),
        JDE_RAND_1_BIN("JDE/rand/1/bin");

        public final String label;

        Strategy(String label) {
            this.label = label;
        }

    }

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter
    private Strategy strategy;
    /* --------- jDE constants ----------- */
    private final static double Finit = 0.5; // F INITIAL FACTOR VALUE
    private final static double CRinit = 0.9; // CR INITIAL FACTOR VALUE

    private final static double Fl = 0.1; // F in the range [0.1, 1.0]
    private final static double Fu = 0.9; //

    private final static double CRl = 0.0; // CR in the range [0.0, 1.0]
    private final static double CRu = 1.0; //

    private final static double tao1 = 0.1; // probability to adjust F
    private final static double tao2 = 0.1; // probability to adjust CR

    private DESolution[] c;
    private DESolution[] d; // double c[MAXPOP][MAXDIM], d[MAXPOP][MAXDIM];
    private DESolution[] pold; // double pold[MAXPOP][MAXDIM]
    private DESolution[] pnew; // pnew[MAXPOP][MAXDIM]
    private DESolution[] pswap; // (*pswap)[MAXPOP][MAXDIM];

    private int i, j, L, n; /* counting variables */
    private int r1, r2, r3, r4; /* placeholders for random indexes */
    private int r5; /* placeholders for random indexes */
    private int D; /* Dimension of parameter vector */


    private double[] tmp; // , best[], bestit[]; /* members MAXDIM*/
    private double tmpF, tmpCR, tmp3;
    private double cvar; /* computes the cost variance */
    private double cmean; /* mean cost */
    private double F, memF, CR, memCR; /* control variables of DE */
    private DESolution best, bestit, bestI;// best, best iteration, best I

    public DEAlgorithm() {
        this(Strategy.DE_RAND_1_BIN, 30, Finit, CRinit);
    }

    public DEAlgorithm(Strategy strategy) {
        this(strategy, 30, Finit, CRinit);
    }

    public DEAlgorithm(Strategy strategy, int popSize) {
        this(strategy, popSize, Finit, CRinit);
    }

    public DEAlgorithm(Strategy strategy, int popSize, double F, double CR) {
        this.strategy = strategy;
        memF = F;
        memCR = CR;
        this.F = F;
        this.CR = CR;
        this.popSize = popSize;

        assert (popSize > 5); // population size smaller than 6 will cause an infinite loop

        au = new Author("matej", "matej.crepinsek@um.si");
        ai = new AlgorithmInfo(strategy.label, strategy.label,
                "[1] J. Brest, S. Greiner, B. Boskovic, M. Mernik, V. Zumer. \n"
                + "Self-adapting control parameters in differential evolution: a\n" + "comparative study on numerical benchmark problems.\n"
                + "IEEE Transactions on Evolutionary Computation, 2006, vol. 10,\n" + "no. 6, pp. 646-657. DOI 10.1109/TEVC.2006.872133"
        );
    }

    private void assignd(int D, double[] a, double[] b) {
        System.arraycopy(b, 0, a, 0, D);
        // System.arraycopy(src, srcPos, dest, destPos, length)
    }

    public void init() throws StopCriterionException {
        this.D = task.problem.getNumberOfDimensions();
        // this.NP = D * 10; Set by constructor
        this.F = memF;
        this.CR = memCR;
        tmp = new double[D];
        c = new DESolution[popSize];
        d = new DESolution[popSize];
        for (i = 0; i < popSize; i++) {
            c[i] = new DESolution(task.getRandomEvaluatedSolution(), Finit, CRinit);
            // System.out.println(i+". "+c[i]);
        }
        
		/*double x7[] = {
		29.6,
	    11.4,
	    3.0,
	    10.8,
	    8.0};
        c[0] = new IndividualSA(task.eval(x7), Finit, CRinit);
        */
        bestI = c[0];
        for (i = 0; i < popSize; i++) {
            if (task.problem.isFirstBetter(c[i], bestI))
                bestI = c[i];
        }
        // if (strategy == 20) System.out.println("Start 0I:"+bestI);
        best = new DESolution(bestI);
        bestit = new DESolution(bestI);
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        init(); // referesh all data
        pold = c; /* old population (generation G) */
        pnew = d; /* new population (generation G+1) */
        // if (strategy == 20) System.out.println("NP="+NP+" Start:"+best);
        while (!task.isStopCriterion()) {

            for (i = 0; i < popSize; i++) /* Start of loop through ensemble */ {
                if (task.isStopCriterion())
                    break;
                do /* Pick a random population member */ { /* Endless loop for NP < 2 !!! */
                    r1 = RNG.nextInt(popSize); // (int) (RNG.nextDouble() *
                    // NP);
                } while (r1 == i);

                do /* Pick a random population member */ { /* Endless loop for NP < 3 !!! */
                    r2 = RNG.nextInt(popSize); // (int) (RNG.nextDouble() *
                    // NP);
                } while ((r2 == i) || (r2 == r1));

                do /* Pick a random population member */ { /* Endless loop for NP < 4 !!! */
                    r3 = RNG.nextInt(popSize); // (int) (RNG.nextDouble() *
                    // NP);
                } while ((r3 == i) || (r3 == r1) || (r3 == r2));

                do /* Pick a random population member */ { /* Endless loop for NP < 5 !!! */
                    r4 = RNG.nextInt(popSize); // (int) (RNG.nextDouble() *
                    // NP);
                } while ((r4 == i) || (r4 == r1) || (r4 == r2) || (r4 == r3));

                do /* Pick a random population member */ { /* Endless loop for NP < 6 !!! */
                    r5 = RNG.nextInt(popSize); // (int) (RNG.nextDouble() *
                    // NP);
                } while ((r5 == i) || (r5 == r1) || (r5 == r2) || (r5 == r3) || (r5 == r4));

                if (strategy == Strategy.DE_BEST_1_EXP) /* strategy DE0 (not in our paper) */ {
                    assignd(D, tmp, Util.toDoubleArray(pold[i].getVariables()));
                    n = (int) (RNG.nextDouble() * D);
                    L = 0;
                    do {
                        tmp[n] = bestit.getValue(n) + F * (pold[r2].getValue(n) - pold[r3].getValue(n));
                        n = (n + 1) % D;
                        L++;
                    } while ((RNG.nextDouble() < CR) && (L < D));
                }

                /*-------DE/rand/1/exp-------------------------------------------------------------------*/
                /*-------This is one of my favourite strategies. It works especially well when the-------*/
                /*-------"bestit[]"-schemes experience misconvergence. Try e.g. F=0.7 and CR=0.5---------*/
                /*-------as a first guess.---------------------------------------------------------------*/
                else if (strategy == Strategy.DE_RAND_1_EXP) /* strategy DE1 in the techreport */ {
                    assignd(D, tmp, Util.toDoubleArray(pold[i].getVariables()));
                    n = (int) (RNG.nextDouble() * D);
                    L = 0;
                    do {
                        tmp[n] = pold[r1].getValue(n) + F * (pold[r2].getValue(n) - pold[r3].getValue(n));
                        n = (n + 1) % D;
                        L++;
                    } while ((RNG.nextDouble() < CR) && (L < D));
                }

                /*-------DE/rand-to-best/1/exp-----------------------------------------------------------*/
                /*-------This strategy seems to be one of the best strategies. Try F=0.85 and CR=1.------*/
                /*-------If you get misconvergence try to increase NP. If this doesn't help you----------*/
                /*-------should play around with all three control variables.----------------------------*/
                else if (strategy == Strategy.DE_RAND_TO_BEST_1_EXP) /* similiar to DE2 but generally better */ {
                    assignd(D, tmp, Util.toDoubleArray(pold[i].getVariables()));
                    n = (int) (RNG.nextDouble() * D);
                    L = 0;
                    do {
                        tmp[n] = tmp[n] + F * (bestit.getValue(n) - tmp[n]) + F * (pold[r1].getValue(n) - pold[r2].getValue(n));
                        n = (n + 1) % D;
                        L++;
                    } while ((RNG.nextDouble() < CR) && (L < D));
                }

                /*-------DE/best/2/exp is another powerful strategy worth trying--------------------------*/
                else if (strategy == Strategy.DE_BEST_2_EXP) {
                    assignd(D, tmp, Util.toDoubleArray(pold[i].getVariables()));
                    n = (int) (RNG.nextDouble() * D);
                    L = 0;
                    do {
                        tmp[n] = bestit.getValue(n) + (pold[r1].getValue(n) + pold[r2].getValue(n) - pold[r3].getValue(n) - pold[r4].getValue(n)) * F;
                        n = (n + 1) % D;
                        L++;
                    } while ((RNG.nextDouble() < CR) && (L < D));
                }
                /*-------DE/rand/2/exp seems to be a robust optimizer for many functions-------------------*/
                else if (strategy == Strategy.DE_RAND_2_EXP) {
                    assignd(D, tmp, Util.toDoubleArray(pold[i].getVariables()));
                    n = (int) (RNG.nextDouble() * D);
                    L = 0;
                    do {
                        tmp[n] = pold[r5].getValue(n) + (pold[r1].getValue(n) + pold[r2].getValue(n) - pold[r3].getValue(n) - pold[r4].getValue(n)) * F;
                        n = (n + 1) % D;
                        L++;
                    } while ((RNG.nextDouble() < CR) && (L < D));
                }

                /*-------DE/best/1/bin--------------------------------------------------------------------*/
                else if (strategy == Strategy.DE_BEST_1_BIN) {
                    assignd(D, tmp, Util.toDoubleArray(pold[i].getVariables()));
                    n = (int) (RNG.nextDouble() * D);
                    for (L = 0; L < D; L++) /* perform D binomial trials */ {
                        if ((RNG.nextDouble() < CR) || L == (D - 1)) /*
                         * change
                         * at
                         * least
                         * one
                         * parameter
                         */ {
                            tmp[n] = bestit.getValue(n) + F * (pold[r2].getValue(n) - pold[r3].getValue(n));
                        }
                        n = (n + 1) % D;
                    }
                }
                /*-------DE/rand/1/bin-------------------------------------------------------------------*/
                else if (strategy == Strategy.DE_RAND_1_BIN) {
                    assignd(D, tmp, Util.toDoubleArray(pold[i].getVariables()));
                    n = (int) (RNG.nextDouble() * D);
                    for (L = 0; L < D; L++) /* perform D binomial trials */ {
                        if ((RNG.nextDouble() < CR) || L == (D - 1)) {
                            tmp[n] = pold[r1].getValue(n) + F * (pold[r2].getValue(n) - pold[r3].getValue(n));
                            /*
                             * if (Math.abs(tmp[n])>100) {
                             * System.out.println(F+" "
                             * +CR+" "+Arrays.toString(tmp
                             * )+" moj "+pold[i]+" "+Arrays
                             * .toString(pold[i].getX()));
                             * System.out.println(pold[r1]);
                             * System.out.println(pold[r2]);
                             * System.out.println(pold[r3]);
                             *
                             * }
                             */
                        }
                        n = (n + 1) % D;
                    }
                }
                /*-- s20 -----JAN-DE/rand/1/bin-------------------------------------------------------------------*/
                else if (strategy == Strategy.JDE_RAND_1_BIN) {
                    assignd(D, tmp, Util.toDoubleArray(pold[i].getVariables()));
                    tmpF = pold[i].getF();
                    tmpCR = pold[i].getCR();
                    n = (int) (RNG.nextDouble() * D);
                    // SELF-ADAPTATION OF CONTROL PARAMETERS
                    if (RNG.nextDouble() < tao1) { // F
                        F = Fl + RNG.nextDouble() * Fu;
                        tmpF = F;
                    } else
                        F = tmpF;
                    if (RNG.nextDouble() < tao2) { // CR
                        CR = CRl + RNG.nextDouble() * CRu;
                        tmpCR = CR;
                    } else
                        CR = tmpCR;

                    for (L = 0; L < D; L++) /* perform D binomial trials */ {
                        if ((RNG.nextDouble() < CR) || L == D) {
                            tmp[n] = pold[r1].getValue(n) + F * (pold[r2].getValue(n) - pold[r3].getValue(n));
                        }
                        n = (n + 1) % D;
                    }
                }

                /*-------DE/rand-to-best/1/bin-----------------------------------------------------------*/
                else if (strategy == Strategy.DE_RAND_TO_BEST_1_BIN) {
                    assignd(D, tmp, Util.toDoubleArray(pold[i].getVariables()));
                    n = (int) (RNG.nextDouble() * D);
                    for (L = 0; L < D; L++) /* perform D binomial trials */ {
                        if ((RNG.nextDouble() < CR) || L == (D - 1)) /*
                         * change
                         * at
                         * least
                         * one
                         * parameter
                         */ {
                            tmp[n] = tmp[n] + F * (bestit.getValue(n) - tmp[n]) + F * (pold[r1].getValue(n) - pold[r2].getValue(n));
                        }
                        n = (n + 1) % D;
                    }
                }
                /*-------DE/best/2/bin--------------------------------------------------------------------*/
                else if (strategy == Strategy.DE_BEST_2_BIN) {
                    assignd(D, tmp, Util.toDoubleArray(pold[i].getVariables()));
                    n = (int) (RNG.nextDouble() * D);
                    for (L = 0; L < D; L++) /* perform D binomial trials */ {
                        if ((RNG.nextDouble() < CR) || L == (D - 1)) /*
                         * change
                         * at
                         * least
                         * one
                         * parameter
                         */ {
                            tmp[n] = bestit.getValue(n) + (pold[r1].getValue(n) + pold[r2].getValue(n) - pold[r3].getValue(n) - pold[r4].getValue(n)) * F;
                        }
                        n = (n + 1) % D;
                    }
                }
                /*-------DE/rand/2/bin--------------------------------------------------------------------*/
                else {
                    assignd(D, tmp, Util.toDoubleArray(pold[i].getVariables()));
                    n = (int) (RNG.nextDouble() * D);
                    for (L = 0; L < D; L++) /* perform D binomial trials */ {
                        if ((RNG.nextDouble() < CR) || L == (D - 1)) /*
                         * change
                         * at
                         * least
                         * one
                         * parameter
                         */ {
                            tmp[n] = pold[r5].getValue(n) + (pold[r1].getValue(n) + pold[r2].getValue(n) - pold[r3].getValue(n) - pold[r4].getValue(n)) * F;
                        }
                        n = (n + 1) % D;
                    }
                }
                // CM???
                for (int kk = 0; kk < D; kk++) {
                    tmp[kk] = task.problem.setFeasible(tmp[kk], kk);
                }
                NumberSolution<Double> br = new NumberSolution<>(Util.toDoubleArrayList(tmp));
                task.eval(br);

                DESolution trial_cost = new DESolution(br, tmpF, tmpCR);
                // if (strategy == 20) System.out.println(pnew[i]+
                // " new best "+trial_cost);
                if (task.problem.isFirstBetter(trial_cost, pold[i])) {
                    // if (strategy == 20) System.out.println(pnew[i]+
                    // "  best "+trial_cost);
                    pnew[i] = trial_cost;
                    if (task.problem.isFirstBetter(trial_cost, best)) {
                        best = new DESolution(trial_cost);
                    }

                } else {
                    pnew[i] = new DESolution(pold[i]);
                    // System.out.println(pold[i]+" vs "+pnew[i]);
                }

            } /* End mutation loop through pop. */
            // System.out.println(task.getNumberOfEvaluations());
            bestit = new DESolution(best);
            // if (strategy == 20) System.out.println(best);
            pswap = pold;
            pold = pnew;
            pnew = pswap;
            /*
             * IndividualSA swap; for (int i=0; i<NP;i++) { swap = pold[i];
             * pold[i] = new IndividualSA(pnew[i]); pnew[i] = new
             * IndividualSA(swap); }
             */
            task.incrementNumberOfIterations();

        }
        return best;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        this.F = memF;
        this.CR = memCR;
    }


    @Override
    public List<Algorithm> getAlgorithmParameterTest(int dimension, int maxCombinations) {
        List<Algorithm> alternative = new ArrayList<Algorithm>();
        if (maxCombinations == 1) {
            alternative.add(this);
        } else {
            int counter = 0;
            if (strategy == Strategy.JDE_RAND_1_BIN) { //self adaptive no need for CR and F parameter
                int[] paramCombinations = {25, 50, 15, 75, 100, 10, 30, 40};
                for (int i = 0; (i < paramCombinations.length) && (counter < maxCombinations); i++) {
                    alternative.add(new DEAlgorithm(strategy, paramCombinations[i]));
                    counter++;

                }

            } else {
                double[][] paramCombinations = { // {k, c}
                        {10 * dimension, 0.5, 0.9}, {10 * dimension, 0.5, 0.85}, {25, 0.5, 0.9}, {50, 0.5, 0.9},
                        {10, 0.5, 0.9}, {50, 0.5, 0.9}, {25, 0.5, 0.8}, {25, 0.5, 0.9}};

                for (int i = 0; (i < paramCombinations.length) && (counter < maxCombinations); i++) {
                    alternative.add(new DEAlgorithm(strategy, (int) paramCombinations[i][0], paramCombinations[i][1], paramCombinations[i][2]));
                    counter++;

                }

            }
        }
        return alternative;
    }
    /*-----------End of main()------------------------------------------*/
}
