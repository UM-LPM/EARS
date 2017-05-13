package org.um.feri.ears.experiment.so.pso;

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
	ArrayList<PSOCBCWSolution> population;
	Task task;
	PSOCBCWSolution PgBest;
	double w;
	PSOCBCWSolution gBest[];
	PSOCBCWSolution gWorst[];
	PSOCBCWSolution _gBest;
	PSOCBCWSolution _gWorst;
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
		this.gBest = new PSOCBCWSolution[gsize];
		this.gWorst = new PSOCBCWSolution[wsize];
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
		task = taskProblem;
		initPopulation();

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
				if (task.isFirstBetter(population.get(j), gBest[i])) {
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
				if (task.isFirstBetter(gWorst[i], population.get(j))) {
					gWorst[i] = population.get(j);
					indeksiWorst[i] = j;
				}
			}
		}

		_gWorst = gWorst[0];

		int iterCount = 0;
		double prag = 0.5;
		double prob = 0.0;

		while (!task.isStopCriteria()) {
			for (int i = 0; i < populationSize; i++) {
				v = new double[task.getNumberOfDimensions()];

				double _x[] = population.get(i).getDoubleVariables();
				double _pBestx[] = population.get(i).getP().getDoubleVariables();
				double _gBestx[] = _gBest.getDoubleVariables();

				for (int d = 0; d < task.getNumberOfDimensions(); d++) {
					double xi = _x[d] + v[d];

					v[d] = v[d] + phi1 * Util.rnd.nextDouble() * (_pBestx[d] - xi)
							+ phi2 * Util.rnd.nextDouble() * (_gBestx[d] - xi);
				}
				
				if(task.isStopCriteria())
					break;
				//population.set(i, update(population.get(i),v));
				population.set(i, population.get(i).update(task, v));
				//update(population.get(i),v);
				
				if (task.isFirstBetter(population.get(i), _gBest)) {
					_gBest = population.get(i);
				} else if (task.isFirstBetter(_gWorst, population.get(i))) {
					_gWorst = population.get(i);
				}

				if (task.isStopCriteria()) {
					break;
				}

			}

			PSOCBCWSolution worst = gBest[0];
			int index = 0;

			for (int k = 1; k < gBest.length; k++) {
				if (task.isFirstBetter(worst, gBest[k])) {
					worst = gBest[k];
					index = k;
				}
			}

			gBest[index] = _gBest;

			PSOCBCWSolution best = gWorst[0];
			index = 0;

			for (int k = 1; k < gWorst.length; k++) {
				if (task.isFirstBetter(gWorst[k], best)) {
					best = gWorst[k];
					index = k;
				}
			}

			gWorst[index] = _gWorst;

			if (iterCount > Math.max(gBest.length, gWorst.length)) {
				int stevec = 0;

				for (PSOCBCWSolution p : population) {
					PSOCBCWSolution CB = Closests(p, gBest);
					PSOCBCWSolution CW = Closests(p, gWorst);

					CWx = CW.getDoubleVariables();
					CBx = CB.getDoubleVariables();
					x = p.getDoubleVariables();

					c1sum = 0.0;
					c2sum = 0.0;

					tt1 = 0.0;

					xu = task.getUpperLimit();
					xl = task.getLowerLimit();

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

					for (int d = 0; d < task.getNumberOfDimensions(); d++) {
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
					
					if(task.isStopCriteria())
						break;
					//population.set(stevec, update(population.get(stevec),v));
					population.set(stevec, population.get(stevec).update(taskProblem, v));
					//update(population.get(stevec),v);

					for (int dd = 0; dd < task.getNumberOfDimensions(); dd++) {
						if (inertia[dd] >= task.getLowerLimit()[dd] + epsilon) {
							inertia[dd] = inertia[dd] - dw - epsilon;
						}

						else {
							inertia[dd] = task.getUpperLimit()[dd] - epsilon;
						}
					}
					if (task.isStopCriteria())
						break;
				}
			}
			iterCount++;
			task.incrementNumberOfIterations();
		}

		return _gBest;
	}

	private PSOCBCWSolution Closests(PSOCBCWSolution p1, PSOCBCWSolution ps[]) {
		double xp1[] = p1.getDoubleVariables();

		double distances[] = new double[ps.length];

		for (int i = 0; i < ps.length; i++) {
			distances[i] = Distance(xp1, ps[i].getDoubleVariables());
		}

		double maxDistance = distances[0];
		int indeks = 0;

		for (int i = 1; i < distances.length; i++) {
			if (distances[i] > maxDistance) {
				maxDistance = distances[i];
				indeks = i;
			}
		}

		return ps[indeks];
	}
	
	public void update(PSOCBCWSolution sol, double v[]) throws StopCriteriaException
	{
		double x[] = sol.getDoubleVariables();
		
		for (int i=0; i<x.length; i++)
		{
			x[i]=task.setFeasible(x[i]+v[i],i);
		}
		
		
		PSOCBCWSolution tmp = new PSOCBCWSolution(task.eval(x));
		
		if (task.isFirstBetter(tmp, sol.p))
		{
			tmp.p = tmp;
		}
		else
		{
			tmp.p = sol.p;
		}
		
		tmp.v = v;
		
		sol = tmp;

	}
	
	/*
	public PSOCBCWIndividual update(Task t, double v[]) throws StopCriteriaException
	{
		double x[] = getNewVariables();
		
		for (int i=0; i<x.length; i++)
		{
			x[i]=t.feasible(x[i]+v[i],i);
		}
		
		PSOCBCWIndividual tmp = new PSOCBCWIndividual(t.eval(x));
		
		if (t.isFirstBetter(tmp, p))
		{
			tmp.p = tmp;
		} 
		else
		{
			tmp.p = p;
		}
		
		tmp.v = v;
		
		return tmp;		
	}*/
	

	private double Distance(double p1[], double p2[]) {
		double sum = 0.0;

		for (int i = 0; i < p1.length; i++) {
			sum += Math.pow(p1[i] - p2[i], 2);
		}

		return Math.sqrt(sum);
	}

	private void initPopulation() throws StopCriteriaException {
		population = new ArrayList<>();

		for (int i = 0; i < populationSize; i++) {
			population.add(new PSOCBCWSolution(task));
		}

		inertia = new double[task.getNumberOfDimensions()];

		for (int i = 0; i < task.getNumberOfDimensions(); i++) {
			inertia[i] = w;
		}
	}

	@Override
	public void resetDefaultsBeforNewRun() {

	}

	@Override
	public List<AlgorithmBase> getAlgorithmParameterTest(EnumMap<EnumBenchmarkInfoParameters, String> parameters,
			int maxCombinations) {
		List<AlgorithmBase> algortihms = new ArrayList<AlgorithmBase>();

		algortihms.add(new PSOCBCW(30, 0.99, 0.8, 0.8, 2, 4, 0.00001, 0.000001));
		algortihms.add(new PSOCBCW(50, 0.99, 0.8, 0.8, 2, 4, 0.00001, 0.000001));
		algortihms.add(new PSOCBCW(30, 0.99, 0.7, 0.8, 2, 4, 0.00001, 0.000001));
		algortihms.add(new PSOCBCW(30, 0.99, 0.9, 0.9, 2, 4, 0.00001, 0.000001));
		algortihms.add(new PSOCBCW(30, 0.99, 0.8, 0.8, 4, 4, 0.00001, 0.000001));
		algortihms.add(new PSOCBCW(30, 0.99, 0.8, 0.8, 4, 4, 0.000001, 0.0000001));
		algortihms.add(new PSOCBCW(30, 0.99, 0.6, 0.9, 4, 4, 0.000001, 0.0000001));
		algortihms.add(new PSOCBCW(35, 0.6, 0.6, 0.9, 4, 4, 0.000001, 0.0000001));

		if (maxCombinations <= 8) {
			return algortihms.subList(0, maxCombinations - 1);
		}
		return algortihms;
	}
}
