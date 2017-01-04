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

//klasièni EML po prvotnem èlanku
public class EML extends Algorithm {
	// Dimension of the function
	int N;
	// Population size
	int M;
	// Number of local search iterations.
	int LS;
	// Local Search parameter.
	double DELTA;

	// Current best ion
	EMSolution best;

	// Population of ions
	ArrayList<EMSolution> ions;

	public EML(int pop_size, int lsiter, double delta) {
		super();
		this.M = pop_size;
		this.LS = lsiter;
		this.DELTA = delta;

		ai = new AlgorithmInfo("An Electromagnetism-like Mechanism ", "clanek", "EML", "EML");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, M + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED1, LS + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED2, DELTA + "");

		au = new Author("Tadej Klakocer", "tadej.klakocer@student.um.si"); // EARS
		// author
		// info
	}

	public EML() {
		this(30, 20, 0.001); // èlanek nastavitve
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		N = taskProblem.getDimensions();
		Initialize(taskProblem);

		//int iter = 0;
		//int iter_max = 100;

		while (!taskProblem.isStopCriteria()) {
			if (LS != 0)
				Local(taskProblem);

			CalcF(taskProblem);
			Move(taskProblem);

			//if (taskProblem.isStopCriteria())
		//		break;

		//	iter++;
/*
			if (iter == iter_max) {
				System.out.println("iteracija full");
				break;
			}
*/
		}

		// find best last minute
		for (int i = 0; i < this.M; i++) {
			if (taskProblem.isFirstBetter(ions.get(i), best)) {
				best = ions.get(i);
				best.index = i;
				best.count = taskProblem.getNumberOfEvaluations();
				
				System.out.println("last minute");
			}
		}

		return best;
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		// TODO Auto-generated method stub

	}

	// Initialize
	// m points are generated randomly from the feasible region.
	void Initialize(Task t) throws StopCriteriaException {
		ions = null;
		ions = new ArrayList<>();

		for (int i = 0; i < M; i++) {
			ions.add(new EMSolution(t));

			if (t.isStopCriteria())
				break;
		}

		best = new EMSolution(ions.get(0));
		best.count = 0;
		best.index = 0;

		// Find best and worst
		for (int i = 0; i < M; i++) {

			if (t.isFirstBetter(ions.get(i), best)) {
				best = new EMSolution(ions.get(i));
				best.index = i;
				best.count = t.getNumberOfEvaluations();
			}
		}
	}

	// Local
	// Simple random local search algorithm.
	void Local(Task t) throws StopCriteriaException {
		int count;

		double ThresHold = 0;
		for (int k = 0; k < N; k++)
			if (t.getInterval()[k] > ThresHold)
				ThresHold = t.getInterval()[k];

		ThresHold = ThresHold * DELTA;

		int i, j;

		// The current best point is assigned to be point i.
		// Local search is applied only to the current best point.
		i = best.index;

		for (j = 0; j < N; j++) {
			count = 0;
			double Tiny1 = Util.nextDouble();

			while (count < LS) {
				double[] y = best.getNewVariables();
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
				if (t.isFirstBetter(temp, best)) {
					temp.Q = ions.get(i).Q;
					System.arraycopy(ions.get(i).Fi, 0, temp.Fi, 0, ions.get(i).Fi.length);

					ions.set(i, temp);

					best = new EMSolution(temp);
					best.index = i;
					best.count = t.getNumberOfEvaluations();

				} else {
					count = LS - 1;
				}

				count++;
			}
		}
	}

	// CalcF
	// The total force on each point is calculated here.
	void CalcF(Task t) {
		double dist, temp;
		double totdif = 0.0;

		for (int k = 0; k < M; k++) {
			// The total deviation from the current best objective
			// function value. This is used in the calculation of
			// charges below.
			totdif = totdif + (ions.get(k).getEval() - best.getEval());
		}

		for (int k = 0; k < M; k++) {
			// The charge of each point is calculated.
			ions.get(k).Q = Math.exp(-1.0 * (N * (ions.get(k).getEval() - best.getEval()) / totdif));

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

					// dodaš k force
					ions.get(i).Fi = VECADD(ions.get(i).Fi, aij, N);
				}
				// Repulsion
				else if ((i != best.index) && (i != j)) {
					double[] aij = VECSUB(ions.get(j).getDoubleVariables(), ions.get(i).getDoubleVariables(), N);

					dist = VECNORM(aij, N);
					temp = (ions.get(i).Q * ions.get(j).Q) / (Math.pow(dist, 2.0));

					aij = SCAMUL(temp, aij, N);

					// odšteje force
					ions.get(i).Fi = VECSUB(ions.get(i).Fi, aij, N);
				}
			} // end for j
		} // end for i
	}

	// Move
	// Each point except the current best point
	// is moved in the direction of total force.
	void Move(Task t) throws StopCriteriaException {
		int i, j;
		double norm, Tiny;

		for (i = 0; i < M; i++) {
			if (i != best.index) {
				Tiny = Util.nextDouble();

				// Normalize the total force on each point
				// to maintain the feasibility.
				norm = VECNORM(ions.get(i).Fi, N);
				ions.get(i).Fi = SCAMUL((1.0 / norm), ions.get(i).Fi, N);

				double[] y = ions.get(i).getNewVariables();

				for (j = 0; j < N; j++) {
					if (ions.get(i).Fi[j] >= 0) {
						y[j] = y[j] + Tiny * ions.get(i).Fi[j] * (t.getUpperLimit()[j] - y[j]);
					} else {
						y[j] = y[j] + Tiny * ions.get(i).Fi[j] * (y[j] - t.getLowerLimit()[j]);
					}
				} // end for j

				if (t.isStopCriteria())
					break;

				// Evaluate new position of ion i
				EMSolution novi = new EMSolution(t.eval(y));

				// if(t.isFirstBetter(novi, ions.get(i)))
				ions.set(i, novi);

			}

		} // end for i

		// Find best
		for (i = 0; i < M; i++) {
			if (t.isFirstBetter(ions.get(i), best)) {
				best = new EMSolution(ions.get(i));
				best.index = i;
				best.count = t.getNumberOfEvaluations();
				
				//System.out.println(t.getNumberOfEvaluations() + "; " + best.getEval());
			}
		}
	}

	// Vector Addition
	double[] VECADD(double[] A, double[] B, int D) {
		double[] ret = new double[D];

		for (int i = 0; i < D; i++)
			ret[i] = B[i] + B[i];

		return ret;
	}

	// VECSUB
	// Vector Subtraction
	double[] VECSUB(double[] A, double[] B, int D) {
		double[] aij = new double[D];

		for (int i = 0; i < D; i++)
			aij[i] = A[i] - B[i];

		return aij;
	}

	// VECNORM
	// L2 norm (euclidean norm) of a vector.
	double VECNORM(double[] vec, int D) {
		double dist = 0.0;
		for (int i = 0; i < D; i++)
			dist += Math.pow(vec[i], 2);

		dist = Math.sqrt(dist);

		return dist;
	}

	// SCAMUL
	// Scalar multiplication.
	double[] SCAMUL(double a, double[] A, int D) {
		// A = a*A

		double[] ret = new double[D];
		for (int i = 0; i < D; i++)
			ret[i] = a * A[i];

		return ret;
	}

}
