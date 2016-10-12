package org.um.feri.ears.algorithms.so.jade;
import java.util.ArrayList;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class JADE extends Algorithm {
	private int pop_size;
	// private int arch_size;
	private int elite_size; // calculated by p*pop_size pbest
	// private double F,CR;
	private ArrayList<JADEIndividual> elite; // pbest
	private JADEIndividual pop_x[]; // population
	private ArrayList<JADEIndividual> arch_x; // population
	private JADEIndividual g; // global best

	private ArrayList<Double> SCR; // list of successful F, CR in current gen
	private ArrayList<Double> SF; //

	Task task; // set it in run
	double p; // % DE/Current-to-pbest
	double c; // helps

	/*
	 * Keep track of best individuals pbest! Also sets global best
	 */
	private void updateEliteAndGlobalBest(JADEIndividual in) {
		boolean add = false;
		for (int i = 0; i < elite.size(); i++) {
			if (task.isFirstBetter(in, elite.get(i))) {
				elite.add(i, in);
				add = true;
				break;
			}
		}
		if ((!add) && (elite_size >= elite.size())) {
			elite.add(in);
		}

		if (g == null) 
			g = in;
		else if (task.isFirstBetter(in, g))
			g = in;
		if (elite_size < elite.size())
			elite.remove(elite_size);
	}

	private void initPopulation() throws StopCriteriaException {
		pop_x = new JADEIndividual[pop_size];
		for (int i = 0; i < pop_size; i++) {
			pop_x[i] = new JADEIndividual(task.getRandomSolution(), 0.5, 0.5);
			updateEliteAndGlobalBest(pop_x[i]);
			if (task.isStopCriteria())
				break;
		}
	}

	public JADE(int pop_size, double p, double c) {
		super();
		// by paper p is in [0.05-0.2]
		// by paper c is in [0.05-0.2]
		// by paper pop_size (D < 10) = 30; (D=30) = 100; (D=100) = 400
		debug = false;
		this.pop_size = pop_size;
		this.c = c;
		this.p = p;
		elite_size = (int) Math.round(pop_size * p);
		elite = new ArrayList<JADEIndividual>();
		arch_x = new ArrayList<JADEIndividual>();
		SCR = new ArrayList<Double>();
		SF = new ArrayList<Double>();
		if (elite_size == 0)
			elite_size = 1;
		setDebug(debug); // EARS prints some debug info
		ai = new AlgorithmInfo(
				"Jingqiao Zhang, Arthur C. Sanderson",
				"JADE: Adaptive Differential Evolution With Optional External Archive",
				"JADE", "JADE"); // EARS add algorithm name
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
		// ai.addParameter(EnumAlgorithmParameters., F + "");
		au = new Author("Matej", "matej.crepinsek at um.si"); // EARS author
																// info
	}

	public JADE() {
		this(30, .05, .1); 
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		g=null;
		task = taskProblem;
		elite.clear();
		arch_x.clear();
		JADEIndividual pop_new[] = new JADEIndividual[pop_size];
		double tmp[];
		int j_rand;
		int D = task.getDimensions();
		int r1, r2, pBest;
		double Fpom;
		JADEIndividual in_r2, tmpIn;

		double muCR = 0.5; // adaptive control parameters
		double muF = 0.5;

		initPopulation();

		while (!task.isStopCriteria()) {
			SF.clear();
			SCR.clear();
			for (int i = 0; i < pop_size; i++) {
				// Generate CRi
				pop_x[i].setCR(Util.rnd.nextGaussian() * 0.1 + muCR);
				// http://introcs.cs.princeton.edu/java/stdlib/StdRandom.java.html
				// cauchy
				// Generate Fi
				do {
					Fpom = 0.1
							* Math.tan(Math.PI * (Util.rnd.nextDouble() - 0.5))
							+ muF;
				} while (Fpom <= 0);
				pop_x[i].setF(Fpom);
				// System.out.print(
				// "("+pop_x[i].getCR()+", "+pop_x[i].getF()+") ");
				j_rand = Util.rnd.nextInt(D);
				tmp = pop_x[i].getNewVariables();
				do {
					r1 = Util.rnd.nextInt(pop_size);
				} while (r1 == i);
				do {
					r2 = Util.rnd.nextInt(pop_size
							+ Math.min(arch_x.size(), pop_size));
				} while (r2 == i || r2 == r1);
				if (r2 < pop_size)
					in_r2 = pop_x[r2];
				else
					in_r2 = arch_x.get(r2 - pop_size);
				pBest = Util.rnd.nextInt(elite_size);
				for (int d = 0; d < D; d++) {
					if ((Util.rnd.nextDouble() < pop_x[i].CR) || (d == j_rand)) {
						tmp[d] = task
								.feasible(
										tmp[d]
												+ pop_x[i].F
												* (elite.get(pBest).getVariables().get(d) - tmp[d])
												+ pop_x[i].F
												* (pop_x[r1].getVariables().get(d) - in_r2
														.getVariables().get(d)), d);
					}
				}
				tmpIn = new JADEIndividual(task.eval(tmp), pop_x[i].CR,
						pop_x[i].F);
				if (task.isFirstBetter(tmpIn, pop_x[i])) {
					SCR.add(tmpIn.CR); // save successful parameters
					SF.add(tmpIn.F);
					arch_x.add(pop_x[i]); // save old; good
					pop_new[i] = tmpIn;
					updateEliteAndGlobalBest(tmpIn);
				} else {
					pop_new[i] = pop_x[i]; // old is in new population
				}
				if (task.isStopCriteria())
					break;
			}
			for (int i = 0; i < pop_size; i++) { // new generation
				pop_x[i] = pop_new[i];
			}
			// empty archive if it is too big
			while (arch_x.size() > pop_size)
				arch_x.remove(Util.rnd.nextInt(arch_x.size())); // arch full
			// Update parameters
			if (SCR.size() > 0) {
				muCR = (1. - c) * muCR + c * (sum(SCR) / SCR.size());
				muF = (1. - c) * muF + c * (sum2(SF) / sum(SF)); // Lehmer mean
			} else { // not defined in paper
				muCR = Util.rnd.nextDouble();
				if (muCR < 0.1)
					muCR = 0.1;
				muF = Util.rnd.nextDouble();
				if (muF < 0.1)
					muF = 0.1;
			}
			// System.out.println("\nmuCR:" + muCR + " " + " muF:" +muF);
		}
		return g;
	}

	private final double sum(ArrayList<Double> a) {
		double s = 0;
		for (int i = 0; i < a.size(); i++) {
			s += a.get(i);
		}
		return s;
	}

	private final double sum2(ArrayList<Double> a) {
		double s = 0;
		for (int i = 0; i < a.size(); i++) {
			s += a.get(i) * a.get(i);
		}
		return s;
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		// TODO Auto-generated method stub
		// by paper p is in [0.05-0.2]
		// by paper c is in [0.05-0.2]
		// by paper pop_size (D < 10) = 30; (D=30) = 100; (D=100) = 400
	}

}
