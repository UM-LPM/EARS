package org.um.feri.ears.algorithms.so.pso.variations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.benchmark.EnumBenchmarkInfoParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class PSOCBCW extends Algorithm {

	int populationSize;
	ArrayList<PSOCBCWIndividual> population;
	PSOCBCWIndividual PgBest;
	double w;
	PSOCBCWIndividual gBest[];
	PSOCBCWIndividual gWorst[];
	PSOCBCWIndividual _gBest;
	PSOCBCWIndividual _gWorst;
	double dw;
	double epsilon;
	double phi1, phi2;

	double inertia[];

	public PSOCBCW() {
		this(20, 0.723, 1.49445, 1.49445, 2, 4, 0.00001, 0.000001);
	}

	public PSOCBCW(int populationSize, double w, double p1, double p2, int gsize, int wsize, double dw, double eps) {
		super();
		this.populationSize = populationSize;
		this.w = w;
		this.gBest = new PSOCBCWIndividual[gsize];
		this.gWorst = new PSOCBCWIndividual[wsize];
		this.dw = dw;
		this.epsilon = eps;
		this.phi1 = p1;
		this.phi2 = p2;

		setDebug(debug);
		ai = new AlgorithmInfo("PSOCBCW", "PSOCBCW", "PSOCBCW", "PSOCBCW");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize + "");
		ai.addParameter(EnumAlgorithmParameters.C1, p1 + "");
		ai.addParameter(EnumAlgorithmParameters.C2, p2 + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED1, w + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED2, gsize + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED3, wsize + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED4, dw + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED5, eps + "");
		au = new Author("Robnik", "aleksander.robnik@student.um.si");
	}
//Algoritem PSO z uporabo zunanjega pomnilnika
	
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		initPopulation(taskProblem);

		double v[];
		double CWx[];
		double CBx[];
		double x[];
		double c1sum;
		double c2sum;
		double tt1;
		double xu[];
		double xl[];
		double c1;
		double c2;
		double C;

		gBest[0] = population.get(0);
		int indeksiBest[] = new int[gBest.length];
		java.util.Arrays.fill(indeksiBest, -1);
		indeksiBest[0] = 0;

		for (int i = 1; i < gBest.length; i++) {
			for (int j = 0; j < gBest.length; j++) {
				if (!Arrays.asList(indeksiBest).contains(j)) {
					gBest[i] = population.get(i);
					indeksiBest[i] = j;
					break;
				}
			}

			for (int j = 0; j < population.size(); j++) {
				if (taskProblem.isFirstBetter(population.get(j), gBest[i])) {
					gBest[i] = population.get(j);
					indeksiBest[i] = j;
				}
			}
		}

		_gBest = gBest[0];

		gWorst[0] = population.get(0);
		int indeksiWorst[] = new int[gWorst.length];
		java.util.Arrays.fill(indeksiWorst, -1);
		indeksiWorst[0] = 0;

		for (int i = 1; i < gWorst.length; i++) {
			for (int j = 0; j < gWorst.length; j++) {
				if (!Arrays.asList(indeksiWorst).contains(j)) {
					gWorst[i] = population.get(i);
					indeksiWorst[i] = j;
					break;
				}
			}

			for (int j = 0; j < population.size(); j++) {
				if (taskProblem.isFirstBetter(gWorst[i], population.get(j))) {
					gWorst[i] = population.get(j);
					indeksiWorst[i] = j;
				}
			}
		}

		_gWorst = gWorst[0];

		int iterCount = 0;
		double prag = 0.5;
		double prob = 0.0;

		while (!taskProblem.isStopCriteria()) {
			for (int i = 0; i < populationSize; i++) {
				v = new double[taskProblem.getDimensions()];

				double _x[] = population.get(i).getDoubleVariables();
				double _pBestx[] = population.get(i).getP().getDoubleVariables();
				double _gBestx[] = _gBest.getDoubleVariables();

				for (int d = 0; d < taskProblem.getDimensions(); d++) {
					double xi = _x[d] + v[d];

					v[d] = v[d] + phi1 * Util.rnd.nextDouble() * (_pBestx[d] - xi)
							+ phi2 * Util.rnd.nextDouble() * (_gBestx[d] - xi);
				}

				population.set(i, population.get(i).update(taskProblem, v));

				if (taskProblem.isFirstBetter(population.get(i), _gBest)) {
					_gBest = population.get(i);
				} else if (taskProblem.isFirstBetter(_gWorst, population.get(i))) {
					_gWorst = population.get(i);
				}

				if (taskProblem.isStopCriteria()) {
					break;
				}

			}

			PSOCBCWIndividual worst = gBest[0];
			int indeks = 0;

			for (int k = 1; k < gBest.length; k++) {
				if (taskProblem.isFirstBetter(worst, gBest[k])) {
					worst = gBest[k];
					indeks = k;
				}
			}

			gBest[indeks] = _gBest;

			PSOCBCWIndividual best = gWorst[0];
			indeks = 0;

			for (int k = 1; k < gWorst.length; k++) {
				if (taskProblem.isFirstBetter(gWorst[k], best)) {
					best = gWorst[k];
					indeks = k;
				}
			}

			gWorst[indeks] = _gWorst;

			if (iterCount > Math.max(gBest.length, gWorst.length)) {
				int stevec = 0;

				for (PSOCBCWIndividual p : population) {
					PSOCBCWIndividual CB = Najblizji(p, gBest);
					PSOCBCWIndividual CW = Najblizji(p, gWorst);

					CWx = CW.getDoubleVariables();
					CBx = CB.getDoubleVariables();
					x = p.getDoubleVariables();

					c1sum = 0.0;
					c2sum = 0.0;

					tt1 = 0.0;

					xu = taskProblem.getUpperLimit();
					xl = taskProblem.getLowerLimit();

					for (int k = 0; k < x.length; k++) {
						c1sum += Math.pow(CBx[k] - x[k], 2);
						c2sum += Math.pow(CWx[k] - x[k], 2);

						tt1 += (xu[k] - xl[k]);
					}

					c1 = Math.sqrt(c1sum) / tt1;
					c2 = Math.sqrt(c2sum) / tt1;

					C = Math.abs(c1 - c2) / (c1 + c2);

					v = p.getV();
					double pBestx[] = p.getP().getDoubleVariables();
					double gBestx[] = _gBest.getDoubleVariables();
					double px[] = p.getDoubleVariables();

					double phi = phi1 + phi2;
					double K = 2 / (Math.abs(2 - phi - Math.sqrt(phi * phi - 4 * phi)));

					for (int d = 0; d < taskProblem.getDimensions(); d++) {
						prob = Util.rnd.nextDouble();

						double xi = px[d] + v[d];

						if (prob <= prag) {
							v[d] = 0.99 * v[d] + phi1 * Util.rnd.nextDouble() * (pBestx[d] - xi)
									+ phi2 * Util.rnd.nextDouble() * (gBestx[d] - xi);

							v[d] = K * (v[d] + phi1 * Util.rnd.nextDouble() * (pBestx[d] - xi)
									+ phi2 * Util.rnd.nextDouble() * (gBestx[d] - xi));
						}

						else {
							v[d] = inertia[d] * (v[d] + C) + phi1 * Util.rnd.nextDouble() * (pBestx[d] - xi)
									+ phi2 * Util.rnd.nextDouble() * (gBestx[d] - xi);

							v[d] = K * ((v[d] + C) + phi1 * Util.rnd.nextDouble() * (pBestx[d] - xi)
									+ phi2 * Util.rnd.nextDouble() * (gBestx[d] - xi));
						}
					}

					population.set(stevec, population.get(stevec).update(taskProblem, v));

					for (int dd = 0; dd < taskProblem.getDimensions(); dd++) {
						if (inertia[dd] >= taskProblem.getLowerLimit()[dd] + epsilon) {
							inertia[dd] = inertia[dd] - dw - epsilon;
						}

						else {
							inertia[dd] = taskProblem.getUpperLimit()[dd] - epsilon;
						}
					}
					if (taskProblem.isStopCriteria())
						break;
				}
			}
			iterCount++;
		}

		return _gBest;
	}

	private PSOCBCWIndividual Najblizji(PSOCBCWIndividual p1, PSOCBCWIndividual ps[]) {
		double xp1[] = p1.getDoubleVariables();

		double razdalje[] = new double[ps.length];

		for (int i = 0; i < ps.length; i++) {
			razdalje[i] = Razdalja(xp1, ps[i].getDoubleVariables());
		}

		double maxRazdalja = razdalje[0];
		int indeks = 0;

		for (int i = 1; i < razdalje.length; i++) {
			if (razdalje[i] > maxRazdalja) {
				maxRazdalja = razdalje[i];
				indeks = i;
			}
		}

		return ps[indeks];
	}

	private double Razdalja(double p1[], double p2[]) {
		double sum = 0.0;

		for (int i = 0; i < p1.length; i++) {
			sum += Math.pow(p1[i] - p2[i], 2);
		}

		return Math.sqrt(sum);
	}

	private void initPopulation(Task taskProblem) throws StopCriteriaException {
		population = new ArrayList<>();

		for (int i = 0; i < populationSize; i++) {
			population.add(new PSOCBCWIndividual(taskProblem));
		}

		inertia = new double[taskProblem.getDimensions()];

		for (int i = 0; i < taskProblem.getDimensions(); i++) {
			inertia[i] = w;
		}
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<AlgorithmBase> getAlgorithmParameterTest(EnumMap<EnumBenchmarkInfoParameters, String> parameters,
			int maxCombinations) {
		List<AlgorithmBase> seznam = new ArrayList<AlgorithmBase>();

		seznam.add(new PSOCBCW(30, 0.99, 0.8, 0.8, 2, 4, 0.00001, 0.000001));
		seznam.add(new PSOCBCW(50, 0.99, 0.8, 0.8, 2, 4, 0.00001, 0.000001));
		seznam.add(new PSOCBCW(30, 0.99, 0.7, 0.8, 2, 4, 0.00001, 0.000001));
		seznam.add(new PSOCBCW(30, 0.99, 0.9, 0.9, 2, 4, 0.00001, 0.000001));
		seznam.add(new PSOCBCW(30, 0.99, 0.8, 0.8, 4, 4, 0.00001, 0.000001));
		seznam.add(new PSOCBCW(30, 0.99, 0.8, 0.8, 4, 4, 0.000001, 0.0000001));
		seznam.add(new PSOCBCW(30, 0.99, 0.6, 0.9, 4, 4, 0.000001, 0.0000001));
		seznam.add(new PSOCBCW(35, 0.6, 0.6, 0.9, 4, 4, 0.000001, 0.0000001));

		if (maxCombinations <= 8) {
			return seznam.subList(0, maxCombinations - 1);
		}
		return seznam;
	}
}
