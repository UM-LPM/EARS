package org.um.feri.ears.experiment.so.tk;

import java.util.ArrayList;
import java.util.Arrays;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

///Izvorna koda spremenjena v Javo
//Vir1: https://www.mathworks.com/matlabcentral/fileexchange/47064-a-multilevel-thresholding-algorithm-using-electromagnetism-optimization/content/MTEMO/EMO_KAPUR/EMO.m
//Vir2: http://www.codeforge.com/read/212138/EM.cpp__html

//klasi�ni EML po prvotnem �lanku
public class EM extends Algorithm {
	// Dimenzija problema
	private int N;

	// Velikost populacije
	private int M;

	// �tevilo lokalnih iskanj (koliko ovrednotenj porabi za lokalno iskanje).
	private int LS;

	// Parameter lokalnega iskanja.
	private double DELTA;

	// Trenutno najbolj�i ion.
	private int best_idx;

	// Populacija ionov.
	private ArrayList<EMSolution> ions;

	public EM(int pop_size, int lsiter, double delta) {
		super();
		this.M = pop_size;
		this.LS = lsiter;
		this.DELTA = delta;

		ai = new AlgorithmInfo(
				"An Electromagnetism-like Mechanism",
				"S.Ilker Birbil, Shu-Cherng Fang, An Electromagnetism-like Mechanism for Global Optimization, Journal of Global Optimization 25, 2003 ",
				"EM",
				"Physics-based metaheuristic algorithm");
		au = new Author("Tadej Klakocer", "tadej.klakocer@student.um.si");

		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, M + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED1, LS + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED2, DELTA + "");

	}

	public EM() {
		this(30, 10, 0.001);
	}

	//V �lanku 3.1. GENERAL SCHEME FOR EM
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		N = taskProblem.getNumberOfDimensions();
		Initialize(taskProblem);

		while (!taskProblem.isStopCriteria()) {
			if (LS != 0)
				Local(taskProblem);

			CalcF(taskProblem);
			Move(taskProblem);

		}

		// Najde najbolj�ega preden vrne najbolj�o re�itev.
		for (int i = 0; i < this.M; i++) {
			if (taskProblem.isFirstBetter(ions.get(i), ions.get(best_idx))) {
				//best = ions.get(i);
				//best.index = i;
				best_idx = i;
				//System.out.println("Novi best1");
				//best.count = taskProblem.getNumberOfEvaluations();
			}
		}

		return ions.get(best_idx);//best;
	}

	@Override
	public void resetDefaultsBeforNewRun() {

	}

	// V �lanku 3.2. INITIALIZATION
	void Initialize(Task t) throws StopCriteriaException {
		ions = null;
		ions = new ArrayList<>();

		for (int i = 0; i < M; i++) {
			ions.add(new EMSolution(t));

			if (t.isStopCriteria())
				break;
		}

		best_idx = 0;

		// Find best and worst
		for (int i = 0; i < M; i++) {

			if (t.isFirstBetter(ions.get(i), ions.get(best_idx))) {
				best_idx = i;
			}
		}
	}

	// V �lanku 3.3. LOCAL SEARCH (�e to�no kot v �lanku, potem deluje slabo, sem uporabil local search od Vir1)
	// Simple random local search algorithm.
	void Local(Task t) throws StopCriteriaException {
		int count;

		double ThresHold = 0;
		for (int k = 0; k < N; k++)
			if (t.getInterval()[k] > ThresHold)
				ThresHold = t.getInterval()[k];

		ThresHold = ThresHold * DELTA;

		// The current best point is assigned to be point i.
		// Local search is applied only to the current best point.
		//i = best.index;

		for (int j = 0; j < N; j++) {
			count = 0;
			double Tiny1 = Util.nextDouble();

			while (count < LS) {
				double[] y = ions.get(best_idx).getDoubleVariables();
				double Tiny = Util.nextDouble();
				// FLAG = 1;

				// Make a tiny increment in the corresponding dimension.
				if (Tiny1 > 0.5) {
					y[j] = t.setFeasible(y[j] + Tiny * ThresHold, j);
				}
				// Make a tiny decrement in the corresponding dimension.
				else {
					y[j] = t.setFeasible(y[j] - Tiny * ThresHold, j);
				}

				if (t.isStopCriteria())
					break;

				// Evaluate new temp ion.
				EMSolution temp = new EMSolution(t.eval(y));

				// Update the best values.
				if (t.isFirstBetter(temp, ions.get(best_idx))) {
					temp.Q = ions.get(best_idx).Q;
					System.arraycopy(ions.get(best_idx).Fi, 0, temp.Fi, 0, ions.get(best_idx).Fi.length);

					ions.set(best_idx, temp);
					//System.out.println("Novi local best");
					count = LS - 1;
				}

				count++;
			}
		}
	}

	// V �lanku 3.4. CALCULATION OF TOTAL FORCE VECTOR
	void CalcF(Task t) {
		double dist, temp;
		double totdif = 0.0;

		for (int k = 0; k < M; k++) {
			// The total deviation from the current best objective
			// function value. This is used in the calculation of
			// charges below.
			totdif = totdif + (ions.get(k).getEval() - ions.get(best_idx).getEval());
		}

		for (int k = 0; k < M; k++) {
			// The charge of each point is calculated.
			ions.get(k).Q = Math.exp((-N * (ions.get(k).getEval() -  ions.get(best_idx).getEval()) / totdif));

			// Total force on each point is initialized.
			Arrays.fill(ions.get(k).Fi, 0.0);
		}

		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				// Attraction
				if (t.isFirstBetter(ions.get(j), ions.get(i)) && (i != j)) {
					double[] aij = VECSUB(ions.get(j).getDoubleVariables(), ions.get(i).getDoubleVariables(), N);
					dist = VECNORM(aij, N);
					temp = (ions.get(i).Q * ions.get(j).Q) / (Math.pow(dist, 2.0));

					aij = SCAMUL(temp, aij, N);

					// doda� k force
					ions.get(i).Fi = VECADD(ions.get(i).Fi, aij, N);
				}
				// Repulsion
				else if ((i != best_idx) && (i != j)) {
					double[] aij = VECSUB(ions.get(j).getDoubleVariables(), ions.get(i).getDoubleVariables(), N);

					dist = VECNORM(aij, N);
					temp = (ions.get(i).Q * ions.get(j).Q) / (Math.pow(dist, 2.0));

					aij = SCAMUL(temp, aij, N);

					// od�teje force
					ions.get(i).Fi = VECSUB(ions.get(i).Fi, aij, N);
				}
			} // end for j
		} // end for i
	}

	// V �lanku 3.5. MOVEMENT ACCORDING TO THE TOTAL FORCE
	void Move(Task t) throws StopCriteriaException {
		int i, j;
		double norm, Tiny;

		for (i = 0; i < M; i++) {
			if (i != best_idx){ //best.index) {
				Tiny = Util.nextDouble();

				// Normalize the total force on each point
				// to maintain the feasibility.
				norm = VECNORM(ions.get(i).Fi, N);			
				ions.get(i).Fi = SCAMUL((1.0 / norm), ions.get(i).Fi, N);

				double[] y = ions.get(i).getDoubleVariables();

				for (j = 0; j < N; j++) {
					if (ions.get(i).Fi[j] > 0) {
						y[j] = y[j] + Tiny * ions.get(i).Fi[j] * (t.getUpperLimit()[j] - y[j]);
					} else {
						y[j] = y[j] +  Tiny * ions.get(i).Fi[j] * (y[j] - t.getLowerLimit()[j]);
					}
				} // end for j

				if (t.isStopCriteria())
					break;

				// Evaluate new position of ion i
				EMSolution novi = new EMSolution(t.eval(y));

				ions.set(i, novi);

			}

		} // end for i

		// Find best
		for (i = 0; i < M; i++) {
			if (t.isFirstBetter(ions.get(i), ions.get(best_idx))) {
				best_idx = i;				
			}
		}
	}

	/* HELPER METODE */

	//Helper: Vector Addition
	double[] VECADD(double[] A, double[] B, int D) {
		double[] ret = new double[D];

		for (int i = 0; i < D; i++)
			ret[i] = B[i] + B[i];

		return ret;
	}

	//Helper: Vector Subtraction
	double[] VECSUB(double[] A, double[] B, int D) {
		double[] aij = new double[D];

		for (int i = 0; i < D; i++)
			aij[i] = A[i] - B[i];

		return aij;
	}

	//Helper: L2 norm (euclidean norm) of a vector.
	double VECNORM(double[] vec, int D) {
		double dist = 0.0;
		for (int i = 0; i < D; i++)
			dist += Math.pow(vec[i], 2);

		dist = Math.sqrt(dist);

		return dist;
	}

	//Helper: Scalar multiplication.
	double[] SCAMUL(double a, double[] A, int D) {
		// A = a*A

		double[] ret = new double[D];
		for (int i = 0; i < D; i++)
			ret[i] = a * A[i];

		return ret;
	}

}
