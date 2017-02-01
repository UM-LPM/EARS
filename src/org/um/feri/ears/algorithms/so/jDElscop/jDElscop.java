package org.um.feri.ears.algorithms.so.jDElscop;


import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class jDElscop extends Algorithm {
	/*
	 * Recoded based on cpp code from Janez Brest jDElscop
	 * http://sci2s.ugr.es/EAMHCO#Software
	 */
	public enum StrategyDE {
		STRATEGY_jDEbest, STRATEGY_jDEbin, STRATEGY_jDEexp;
		public static int COUNT = StrategyDE.values().length;
	};

	/*
	 * public static final int NUMBER_OF_STRATEGYS=3; private static final int
	 * STRATEGY_jDEbest=0; private static final int STRATEGY_jjDEbin=1; private
	 * static final int STRATEGY_jDEexp=2;
	 */
	private int pop_size;
	jDElscopSolution pop_x[]; // population
	jDElscopSolution g; // global best
	Task task; // set it in run

	private void initPopulation() throws StopCriteriaException {
		pop_x = new jDElscopSolution[pop_size];
		for (int i = 0; i < pop_size; i++) {
			pop_x[i] = jDElscopSolution.setInitState(task
					.getRandomSolution());
			if (i == 0)
				g = pop_x[0];
			else if (task.isFirstBetter(pop_x[i], g))
				g = pop_x[i];
			if (task.isStopCriteria())
				break;
		}
	}

	public jDElscop() {
		this(100);
	}

	public jDElscop(int pop_size) {
		super();
		debug = false;
		this.pop_size = pop_size;

		setDebug(debug); // EARS prints some debug info
		ai = new AlgorithmInfo(
				"Janez Brest",
				"@Article{Brest2010,author=\"Brest, Janez and Mau{\\v{c}}ec, Mirjam Sepesy\",title=\"Self-adaptive differential evolution algorithm using population size reduction and three strategies\",journal=\"Soft Computing\",year=\"2010\",volume=\"15\",number=\"11\",pages=\"2157--2174\",issn=\"1433-7479\",doi=\"10.1007/s00500-010-0644-5\",url=\"http://dx.doi.org/10.1007/s00500-010-0644-5\"}",
				"jDElscop", "jDElscop"); // EARS add algorithm name
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
		// ai.addParameter(EnumAlgorithmParameters., F + "");
		au = new Author("Matej", "matej.crepinsek at um.si"); // EARS author
																// info
	}

	@Override
	public DoubleSolution execute(Task task) throws StopCriteriaException {
		this.task = task; // used in functions
		initPopulation();
		// int iteration=0;
		StrategyDE strategy; // =StrategyDE.STRATEGY_jDEbest;
		int MaxFES = task.getMaxEvaluations();
		int NumReduce = 3; // 7.2.2010 pmax := NumReduce+1 ==> pmax = 3+1 = 4
		int pop_size = this.pop_size; // variable population sizebased on
										// NumReduce
		int r1, r2, r3;
		double tau1 = 0.1;
		double tau2 = 0.1;
		double F, CR;
		int L, n;
		double tmp[];
		double tmp_par[];
		int odmik;
		int D = task.getNumberOfDimensions();
		jDElscopSolution pop_tmp[] = new jDElscopSolution[pop_size];
		while (!task.isStopCriteria()) {
			// iteration++;
			for (int i = 0; i < pop_size; i++) {
				if (Util.rnd.nextDouble() < 0.1
						&& task.getNumberOfEvaluations() > MaxFES / 2)
					strategy = StrategyDE.STRATEGY_jDEbest;
				else if (i < pop_size / 2)
					strategy = StrategyDE.STRATEGY_jDEbin;
				else
					strategy = StrategyDE.STRATEGY_jDEexp;
				// jDe
				tmp = pop_x[i].getNewVariables();
				tmp_par = pop_x[i].getNewPara();
				do {
					r1 = Util.rnd.nextInt(pop_size);
				} while (r1 == i);
				do {
					r2 = Util.rnd.nextInt(pop_size);
				} while (r2 == i || r2 == r1);
				do {
					r3 = Util.rnd.nextInt(pop_size);
				} while (r3 == i || r3 == r1 || r3 == r2);
				odmik = strategy.ordinal() * 2; // 0, 2, 4
				n = Util.rnd.nextInt(D);
				double F_l = 0.1 + Math.sqrt(1.0 / pop_size); // for small NP
				double CR_l = 0;
				double CR_u = 0.95;
				switch (strategy) {
				case STRATEGY_jDEbest:
					CR_l = 0.00;
					CR_u = 1.0;
					break;
				case STRATEGY_jDEexp:
					CR_l = 0.3;
					CR_u = 1.0;
					F_l = 0.5;
					break;
				case STRATEGY_jDEbin:
					F_l = 0.4;
					CR_l = 0.7;
				}// jDEbest
					// *** LOWER and UPPER values for strategies ***
				if (Util.rnd.nextDouble() < tau1) {
					tmp_par[odmik] = F = F_l + Util.rnd.nextDouble()
							* (1. - F_l);
				} else {
					F = tmp_par[odmik];
				}
				if (Util.rnd.nextDouble() < tau2) {
					tmp_par[1 + odmik] = CR = CR_l + Util.rnd.nextDouble()
							* (CR_u - CR_l);
				} else {
					CR = tmp_par[1 + odmik];
				}
				switch (strategy) {
				case STRATEGY_jDEbest:
					for (L = 0; L < D; L++) /* perform D binomial trials */
					{
						if ((Util.rnd.nextDouble() < CR) || L == (D - 1)) {
							tmp[n] = F
									* (g.getVariables().get(n))
									+ F
									* (pop_x[r2].getVariables().get(n) - pop_x[r3].getVariables().get(n));
						}
						n = (n + 1) % D;
					}
					break;
				case STRATEGY_jDEexp:
					if (Util.rnd.nextDouble() < 0.75
							&& task.isFirstBetter(pop_x[r3], pop_x[r2]))
						F = -F;

					L = 0;
					// F = 0.5; CR=0.9;
					do {
						tmp[n] = pop_x[r1].getVariables().get(n) + F
								* (pop_x[r2].getVariables().get(n) - pop_x[r3].getVariables().get(n));
						n = (n + 1) % D;
						L++;
					} while ((Util.rnd.nextDouble() < CR) && (L < D));

					break;
				case STRATEGY_jDEbin:
					if (Util.rnd.nextDouble() < 0.75
							&& task.isFirstBetter(pop_x[r3], pop_x[r2]))
						F = -F;

					for (L = 0; L < D; L++) /* perform D binomial trials */
					{
						if ((Util.rnd.nextDouble() < CR) || L == (D - 1)) {
							tmp[n] = pop_x[r1].getVariables().get(n)
									+ F
									* (pop_x[r2].getVariables().get(n) - pop_x[r3].getVariables().get(n));
						}
						n = (n + 1) % D;
					}

				}
				for (int j = 0; j < D; j++) {
					tmp[j] = task.setFeasible(tmp[j], j); // in bounds
				}
				DoubleSolution tmpI = task.eval(tmp);
				if (task.isFirstBetter(pop_x[i], tmpI)) { // old is better
					pop_tmp[i] = pop_x[i];
				} else {
					pop_tmp[i] = jDElscopSolution.setParamState(tmpI, tmp_par);
					if (task.isFirstBetter(pop_tmp[i], g)) {
						g = pop_tmp[i];
						if (debug) {
							System.out.println("time:"
									+ task.getNumberOfEvaluations() + " " + g);
						}
					}
				}
				if (task.isStopCriteria())
					break;
			}
			/*
			 * if (NumReduce > 0 && task.getNumberOfEvaluations() % (MaxFES /
			 * (NumReduce + 1)) == (MaxFES / (NumReduce + 1) - 1) && pop_size >
			 * 10 && task.getNumberOfEvaluations() < MaxFES - 1) {
			 */
			if (NumReduce > 0
					&& (task.getNumberOfEvaluations()
							% (MaxFES / (NumReduce + 1)) == 0) && pop_size > 10) {
				/*
				 * ASK Not used imin ASK NumReduce--? if (imin < pop_size / 2)
				 * imin = imin / 2; else imin = (imin - NP / 2) / 2;
				 */
				for (int ii = 0; ii < pop_size / 2; ii++) { // take best
															// half!
					if (task.isFirstBetter(pop_tmp[pop_size / 2 + ii],
							pop_tmp[ii])) {
						pop_x[ii] = pop_tmp[pop_size / 2 + ii];
					} else {
						pop_x[ii] = pop_tmp[ii];
					}

				}
				pop_size = (pop_size + 1) / 2; // NP = popReduceS2(newPop,
												// extDim, NP, cost);
				System.out.println("Reduce from:" + this.pop_size + " to "
						+ pop_size + "  time:" + task.getNumberOfEvaluations());

			} else {
				for (int ii = 0; ii < pop_size; ii++) { // take best half!
					pop_x[ii] = pop_tmp[ii];

				}
			}
			task.incrementNumberOfIterations();

		}
		return g;
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		// TODO Auto-generated method stub

	}

}
