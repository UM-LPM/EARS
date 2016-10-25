package org.um.feri.ears.algorithms.so.de;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.benchmark.EnumBenchmarkInfoParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

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

public class DEAlgorithm extends Algorithm {
    public static final int DE_best_1_exp = 1;
    public static final int DE_rand_1_exp = 2;
    public static final int DE_rand_to_best_1_exp = 3;
    public static final int DE_best_2_exp = 4;
    public static final int DE_rand_2_exp = 5;
    public static final int DE_best_1_bin = 6;
    public static final int DE_rand_1_bin = 7;
    public static final int DE_rand_to_best_1_bin = 8;
    public static final int DE_best_2_bin = 9;
    public static final int DE_rand_2_bin = 10;
    public static final int JDE_rand_1_bin = 20;
    IndividualSA c[], d[]; // double c[MAXPOP][MAXDIM], d[MAXPOP][MAXDIM];
    IndividualSA pold[]; // double pold[MAXPOP][MAXDIM]
    IndividualSA pnew[]; // pnew[MAXPOP][MAXDIM]
    IndividualSA pswap[]; // (*pswap)[MAXPOP][MAXDIM];
    double inibound_h[];
    double inibound_l[];
    Task task; // double extern evaluate(int D, double tmp[], long *nfeval); /*
               // obj. funct. */

    String strat[] = /* strategy-indicator */
    { "", "DE/best/1/exp", "DE/rand/1/exp", "DE/rand-to-best/1/exp", "DE/best/2/exp", "DE/rand/2/exp", "DE/best/1/bin", "DE/rand/1/bin",
            "DE/rand-to-best/1/bin", "DE/best/2/bin", "DE/rand/2/bin", "-11-", "-12-", "-13-", "-14-", "-15-", "-16-", "-17-", "-18-", "-19-",
            "JDE/rand/1/bin", /* s7+self-adaptive ==> s20,jDE */
            "-21-" };

    int i, j, L, n; /* counting variables */
    int r1, r2, r3, r4; /* placeholders for random indexes */
    int r5; /* placeholders for random indexes */
    int D; /* Dimension of parameter vector */
    int NP; /* number of population members */
    int imin; /* index to member with lowest energy */
    int strategy; /* choice parameter for screen output */
    int gen;
    // int genmax, seed;

    // long nfeval; /* number of function evaluations */

    double trial_cost; /* buffer variable */
    // double inibound_h; /* upper parameter bound */
    // double inibound_l; /* lower parameter bound */
    double tmp[]; // , best[], bestit[]; /* members MAXDIM*/
    double tmpF, tmpCR, tmp3;
    // double cost[]; /* obj. funct. valuesMAXPOP */
    double cvar; /* computes the cost variance */
    double cmean; /* mean cost */
    double F, memF, CR, memCR; /* control variables of DE */
    // double cmin; /* help variables */
    IndividualSA best, bestit, bestI;// best, best iteration, best I
    private boolean debug;
    /* --------- jDE constants ----------- */
    final static double Finit = 0.5; // F INITIAL FACTOR VALUE
    final static double CRinit = 0.9; // CR INITIAL FACTOR VALUE

    final static double Fl = 0.1; // F in the range [0.1, 1.0]
    final static double Fu = 0.9; //

    final static double CRl = 0.0; // CR in the range [0.0, 1.0]
    final static double CRu = 1.0; //

    final static double tao1 = 0.1; // probability to adjust F
    final static double tao2 = 0.1; // probability to adjust CR

    public DEAlgorithm(int strategy) {
        this(strategy, 30, Finit, CRinit);

    }

    public DEAlgorithm(int strategy, int NP) {
        this(strategy, NP, Finit, CRinit);

    }

    public DEAlgorithm(int strategy, int NP, double F, double CR) {
        this.strategy = strategy;
        memF = F;
        memCR = CR;
        this.F = F;
        this.CR = CR;
        this.NP = NP;

        if (strategy == 20) {
            au = new Author("Brest", "brest at uni");
            ai = new AlgorithmInfo("DE", "[1] J. Brest, S. Greiner, B. Boskovic, M. Mernik, V. Zumer. \n"
                    + "Self-adapting control parameters in differential evolution: a\n" + "comparative study on numerical benchmark problems.\n"
                    + "IEEE Transactions on Evolutionary Computation, 2006, vol. 10,\n" + "no. 6, pp. 646-657. DOI 10.1109/TEVC.2006.872133", strat[strategy],
                    "JDE-SalfAdaptiv Differental Evolution");
        } else {
            au = new Author("Storn", " storn at icsi.berkeley.edu");
            ai = new AlgorithmInfo("DE", "Rainer Storn", strat[strategy], "Differential evolution");

            ai.addParameter(EnumAlgorithmParameters.F, F + "");
            ai.addParameter(EnumAlgorithmParameters.CR, CR + "");
        }
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, NP + "");

    }

    void assignd(int D, double a[], double b[]) {
        System.arraycopy(b, 0, a, 0, D);
        // System.arraycopy(src, srcPos, dest, destPos, length)
    }

    public void init() throws StopCriteriaException {
        this.D = task.getDimensions();
        inibound_h = task.getUpperLimit();
        inibound_l = task.getLowerLimit();
        // this.NP = D * 10; Set by constructor
        this.F = memF;
        this.CR = memCR;
        tmp = new double[D];
        c = new IndividualSA[NP];
        d = new IndividualSA[NP];
        for (i = 0; i < NP; i++) {
            c[i] = new IndividualSA(task.getRandomSolution(), Finit, CRinit);
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
        for (i = 0; i < NP; i++) {
            if (task.isFirstBetter(c[i], bestI))
                bestI = c[i];
        }
        // if (strategy == 20) System.out.println("Start 0I:"+bestI);
        best = new IndividualSA(bestI);
        bestit = new IndividualSA(bestI);
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
        this.task = taskProblem;
        init(); // referesh all data
        pold = c; /* old population (generation G) */
        pnew = d; /* new population (generation G+1) */
        // if (strategy == 20) System.out.println("NP="+NP+" Start:"+best);
        while (!task.isStopCriteria()) {
            gen++;
            imin = 0;

            for (i = 0; i < NP; i++) /* Start of loop through ensemble */
            {
                if (task.isStopCriteria())
                    break;
                do /* Pick a random population member */
                { /* Endless loop for NP < 2 !!! */
                    r1 = Util.rnd.nextInt(NP); // (int) (Util.rnd.nextDouble() *
                                               // NP);
                } while (r1 == i);

                do /* Pick a random population member */
                { /* Endless loop for NP < 3 !!! */
                    r2 = Util.rnd.nextInt(NP); // (int) (Util.rnd.nextDouble() *
                                               // NP);
                } while ((r2 == i) || (r2 == r1));

                do /* Pick a random population member */
                { /* Endless loop for NP < 4 !!! */
                    r3 = Util.rnd.nextInt(NP); // (int) (Util.rnd.nextDouble() *
                                               // NP);
                } while ((r3 == i) || (r3 == r1) || (r3 == r2));

                do /* Pick a random population member */
                { /* Endless loop for NP < 5 !!! */
                    r4 = Util.rnd.nextInt(NP); // (int) (Util.rnd.nextDouble() *
                                               // NP);
                } while ((r4 == i) || (r4 == r1) || (r4 == r2) || (r4 == r3));

                do /* Pick a random population member */
                { /* Endless loop for NP < 6 !!! */
                    r5 = Util.rnd.nextInt(NP); // (int) (Util.rnd.nextDouble() *
                                               // NP);
                } while ((r5 == i) || (r5 == r1) || (r5 == r2) || (r5 == r3) || (r5 == r4));

                if (strategy == 1) /* strategy DE0 (not in our paper) */
                {
                    assignd(D, tmp, pold[i].getDoubleVariables());
                    n = (int) (Util.rnd.nextDouble() * D);
                    L = 0;
                    do {
                        tmp[n] = bestit.getVariables().get(n) + F * (pold[r2].getVariables().get(n) - pold[r3].getVariables().get(n));
                        n = (n + 1) % D;
                        L++;
                    } while ((Util.rnd.nextDouble() < CR) && (L < D));
                }

                /*-------DE/rand/1/exp-------------------------------------------------------------------*/
                /*-------This is one of my favourite strategies. It works especially well when the-------*/
                /*-------"bestit[]"-schemes experience misconvergence. Try e.g. F=0.7 and CR=0.5---------*/
                /*-------as a first guess.---------------------------------------------------------------*/
                else if (strategy == 2) /* strategy DE1 in the techreport */
                {
                    assignd(D, tmp, pold[i].getDoubleVariables());
                    n = (int) (Util.rnd.nextDouble() * D);
                    L = 0;
                    do {
                        tmp[n] = pold[r1].getVariables().get(n) + F * (pold[r2].getVariables().get(n) - pold[r3].getVariables().get(n));
                        n = (n + 1) % D;
                        L++;
                    } while ((Util.rnd.nextDouble() < CR) && (L < D));
                }

                /*-------DE/rand-to-best/1/exp-----------------------------------------------------------*/
                /*-------This strategy seems to be one of the best strategies. Try F=0.85 and CR=1.------*/
                /*-------If you get misconvergence try to increase NP. If this doesn't help you----------*/
                /*-------should play around with all three control variables.----------------------------*/
                else if (strategy == 3) /* similiar to DE2 but generally better */
                {
                    assignd(D, tmp, pold[i].getDoubleVariables());
                    n = (int) (Util.rnd.nextDouble() * D);
                    L = 0;
                    do {
                        tmp[n] = tmp[n] + F * (bestit.getVariables().get(n) - tmp[n]) + F * (pold[r1].getVariables().get(n) - pold[r2].getVariables().get(n));
                        n = (n + 1) % D;
                        L++;
                    } while ((Util.rnd.nextDouble() < CR) && (L < D));
                }

                /*-------DE/best/2/exp is another powerful strategy worth trying--------------------------*/
                else if (strategy == 4) {
                    assignd(D, tmp, pold[i].getDoubleVariables());
                    n = (int) (Util.rnd.nextDouble() * D);
                    L = 0;
                    do {
                        tmp[n] = bestit.getVariables().get(n) + (pold[r1].getVariables().get(n) + pold[r2].getVariables().get(n) - pold[r3].getVariables().get(n) - pold[r4].getVariables().get(n)) * F;
                        n = (n + 1) % D;
                        L++;
                    } while ((Util.rnd.nextDouble() < CR) && (L < D));
                }
                /*-------DE/rand/2/exp seems to be a robust optimizer for many functions-------------------*/
                else if (strategy == 5) {
                    assignd(D, tmp, pold[i].getDoubleVariables());
                    n = (int) (Util.rnd.nextDouble() * D);
                    L = 0;
                    do {
                        tmp[n] = pold[r5].getVariables().get(n) + (pold[r1].getVariables().get(n) + pold[r2].getVariables().get(n) - pold[r3].getVariables().get(n) - pold[r4].getVariables().get(n)) * F;
                        n = (n + 1) % D;
                        L++;
                    } while ((Util.rnd.nextDouble() < CR) && (L < D));
                }

                /*-------DE/best/1/bin--------------------------------------------------------------------*/
                else if (strategy == 6) {
                    assignd(D, tmp, pold[i].getDoubleVariables());
                    n = (int) (Util.rnd.nextDouble() * D);
                    for (L = 0; L < D; L++) /* perform D binomial trials */
                    {
                        if ((Util.rnd.nextDouble() < CR) || L == (D - 1)) /*
                                                                           * change
                                                                           * at
                                                                           * least
                                                                           * one
                                                                           * parameter
                                                                           */
                        {
                            tmp[n] = bestit.getVariables().get(n) + F * (pold[r2].getVariables().get(n) - pold[r3].getVariables().get(n));
                        }
                        n = (n + 1) % D;
                    }
                }
                /*-------DE/rand/1/bin-------------------------------------------------------------------*/
                else if (strategy == 7) {
                    assignd(D, tmp, pold[i].getDoubleVariables());
                    n = (int) (Util.rnd.nextDouble() * D);
                    for (L = 0; L < D; L++) /* perform D binomial trials */
                    {
                        if ((Util.rnd.nextDouble() < CR) || L == (D - 1)) {
                            tmp[n] = pold[r1].getVariables().get(n) + F * (pold[r2].getVariables().get(n) - pold[r3].getVariables().get(n));
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
                else if (strategy == 20) {
                    assignd(D, tmp, pold[i].getDoubleVariables());
                    tmpF = pold[i].getF();
                    tmpCR = pold[i].getCR();
                    n = (int) (Util.rnd.nextDouble() * D);
                    // SELF-ADAPTATION OF CONTROL PARAMETERS
                    if (Util.rnd.nextDouble() < tao1) { // F
                        F = Fl + Util.rnd.nextDouble() * Fu;
                        tmpF = F;
                    } else
                        F = tmpF;
                    if (Util.rnd.nextDouble() < tao2) { // CR
                        CR = CRl + Util.rnd.nextDouble() * CRu;
                        tmpCR = CR;
                    } else
                        CR = tmpCR;

                    for (L = 0; L < D; L++) /* perform D binomial trials */
                    {
                        if ((Util.rnd.nextDouble() < CR) || L == (D - 1)) {
                            tmp[n] = pold[r1].getVariables().get(n) + F * (pold[r2].getVariables().get(n) - pold[r3].getVariables().get(n));
                        }
                        n = (n + 1) % D;
                    }
                }

                /*-------DE/rand-to-best/1/bin-----------------------------------------------------------*/
                else if (strategy == 8) {
                    assignd(D, tmp, pold[i].getDoubleVariables());
                    n = (int) (Util.rnd.nextDouble() * D);
                    for (L = 0; L < D; L++) /* perform D binomial trials */
                    {
                        if ((Util.rnd.nextDouble() < CR) || L == (D - 1)) /*
                                                                           * change
                                                                           * at
                                                                           * least
                                                                           * one
                                                                           * parameter
                                                                           */
                        {
                            tmp[n] = tmp[n] + F * (bestit.getVariables().get(n) - tmp[n]) + F * (pold[r1].getVariables().get(n) - pold[r2].getVariables().get(n));
                        }
                        n = (n + 1) % D;
                    }
                }
                /*-------DE/best/2/bin--------------------------------------------------------------------*/
                else if (strategy == 9) {
                    assignd(D, tmp, pold[i].getDoubleVariables());
                    n = (int) (Util.rnd.nextDouble() * D);
                    for (L = 0; L < D; L++) /* perform D binomial trials */
                    {
                        if ((Util.rnd.nextDouble() < CR) || L == (D - 1)) /*
                                                                           * change
                                                                           * at
                                                                           * least
                                                                           * one
                                                                           * parameter
                                                                           */
                        {
                            tmp[n] = bestit.getVariables().get(n) + (pold[r1].getVariables().get(n) + pold[r2].getVariables().get(n) - pold[r3].getVariables().get(n) - pold[r4].getVariables().get(n)) * F;
                        }
                        n = (n + 1) % D;
                    }
                }
                /*-------DE/rand/2/bin--------------------------------------------------------------------*/
                else {
                    assignd(D, tmp, pold[i].getDoubleVariables());
                    n = (int) (Util.rnd.nextDouble() * D);
                    for (L = 0; L < D; L++) /* perform D binomial trials */
                    {
                        if ((Util.rnd.nextDouble() < CR) || L == (D - 1)) /*
                                                                           * change
                                                                           * at
                                                                           * least
                                                                           * one
                                                                           * parameter
                                                                           */
                        {
                            tmp[n] = pold[r5].getVariables().get(n) + (pold[r1].getVariables().get(n) + pold[r2].getVariables().get(n) - pold[r3].getVariables().get(n) - pold[r4].getVariables().get(n)) * F;
                        }
                        n = (n + 1) % D;
                    }
                }
                // CM???
                for (int kk = 0; kk < D; kk++) {
                    tmp[kk] = task.feasible(tmp[kk], kk);
                }
                DoubleSolution br = task.eval(tmp);
                IndividualSA trial_cost = new IndividualSA(br, tmpF, tmpCR);
                // if (strategy == 20) System.out.println(pnew[i]+
                // " new best "+trial_cost);
                if (task.isFirstBetter(trial_cost, pold[i])) {
                    // if (strategy == 20) System.out.println(pnew[i]+
                    // "  best "+trial_cost);
                    pnew[i] = trial_cost;
                    if (task.isFirstBetter(trial_cost, best)) {
                        best = new IndividualSA(trial_cost);
                    }

                } else {
                    pnew[i] = new IndividualSA(pold[i]);
                    // System.out.println(pold[i]+" vs "+pnew[i]);
                }

            } /* End mutation loop through pop. */
            // System.out.println(task.getNumberOfEvaluations());
            bestit = new IndividualSA(best);
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
    public void resetDefaultsBeforNewRun() {
        this.F = memF;
        this.CR = memCR;
    }


    @Override
    public List<AlgorithmBase> getAlgorithmParameterTest(EnumMap<EnumBenchmarkInfoParameters, String> param, int maxCombinations) {
        List<AlgorithmBase> alternative = new ArrayList<AlgorithmBase>();
        if (maxCombinations == 1) {
            alternative.add(this);
        } else {
            String sdim=param.get(EnumBenchmarkInfoParameters.DIMENSION);
            int dim=10;
            if (sdim!=null) {
                dim = Integer.parseInt(sdim);
            }
            int counter = 0;
            if (strategy == JDE_rand_1_bin) { //self adaptive no need for CR and F parameter
                int paramCombinations[] = { 25,50,15,75,100, 10, 30, 40};
                for (int i = 0; (i < paramCombinations.length) && (counter < maxCombinations); i++) {
                    alternative.add(new DEAlgorithm(strategy, paramCombinations[i]));
                    counter++;

                }

            } else
            {
                double paramCombinations[][] = { // {k, c}
                        {10 * dim, 0.5, 0.9}, {10 * dim, 0.5, 0.85}, {25, 0.5, 0.9},  {50, 0.5, 0.9 }, 
                        {10, 0.5, 0.9},  {50, 0.5, 0.9 } , {25, 0.5, 0.8},  {25, 0.5, 0.9 }};
                
                for (int i = 0; (i < paramCombinations.length) && (counter < maxCombinations); i++) {
                    alternative.add(new DEAlgorithm(strategy,(int) paramCombinations[i][0], paramCombinations[i][1], paramCombinations[i][2]));
                    counter++;

                }

            };
            
        }
        return alternative;
    }

    /*-----------End of main()------------------------------------------*/

}
