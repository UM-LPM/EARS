package org.um.feri.ears.experiment.so.tk;

import java.util.ArrayList;
import java.util.Collections;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;
import org.um.feri.ears.util.Util;

//Opis algoritma se zaène v poglavju 2.2 Presentation of charged search system
public class CSS extends Algorithm {
	//int counter_fail = 0;

	// matlab eps
	private double eps = 2.2204e-16;

	// Size of the population
	private int pop_size; 

	// Numbers of CP in charge memory
	private int CMS;

	// Pitch Adjusting Rate
	private double PAR;

	// The Charged Memory Considering Rate (CMCR)
	private double CMCR; 

	//Max interval med zgornjo in spodnjo mejo (razpon)
	//private double max;

	private TaskComparator comparator;

	// Glavna populacija nabojev
	private ArrayList<CSSSolution> CPs;

	// Charged memory - spominska populacija
	private ArrayList<CSSSolution> CM;

	// Najbojša rešitev in najslabša rešitev v celotnem zagonu
	private CSSSolution best, worst;

	public CSS(int pop, double par, double cmcr) {	
		this.pop_size = pop;
		this.PAR = par;
		this.CMCR = cmcr;
		this.CMS = pop_size / 4;

		ai = new AlgorithmInfo("Charged system search",
				"A. Kaveh, S. Talatahari, A novel heuristic optimization method: charged system search, Acta Mech 213, 2010",
				"CSS",
				"Physics-based metaheuristic algorithm");
		au = new Author("Tadej Klakoèer", "tadej.klakocer@student.um.si");			

		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED1, CMS + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED2, PAR + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED2, CMCR + "");																																// info
	}

	public CSS(){
		this(20, 0.1, 0.95);
	}


	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		comparator = new TaskComparator(taskProblem);

		int iter = 0;
		int itermax = (taskProblem.getMaxEvaluations() - pop_size) / pop_size;


		//Enaèba 21
		/*
		max = taskProblem.getUpperLimit()[0] - taskProblem.getLowerLimit()[0];
		for (int d = 0; d < taskProblem.getDimensions(); d++) {
			double tmp = taskProblem.getUpperLimit()[0] - taskProblem.getLowerLimit()[0];

			if(max < tmp)
				max = tmp;
		}*/

		//System.out.println("max: "+ max);

		// LEVEL 1: Initialization
		// STEP1 + STEP2 + STEP3
		initPopulation(taskProblem);
		calcMagnitude();

		while (!taskProblem.isStopCriteria()) {
			// LEVEL2 : SEARCH

			// Razdalja do ostalih nabojev "dist_sep(i,j)"
			double[][] rij = calcSepDistance(taskProblem);

			// RULE 3
			// Probability of moving
			double[][] Pij = calcProb(taskProblem);

			// Magnitude of charge
			calcMagnitude();

			// RULE 4
			// Electrical force acting on a CP
			// po vseh dimenzijah ista dolžina intervala
			calcForce(taskProblem, Pij, rij);

			// Rule 5, new positions and velocity of CPs
			double kv = 0.5 * (1.0 - iter / itermax); // eksploracija
			double ka = 0.5 * (1.0 + iter / itermax); // exploitacija

			// Premakne delce, èe so izven mej jih popravi z HS
			// Rule 5, 6 , 7 
			moveParticles(taskProblem, kv, ka);

			// Poisce novi best, poisce novi worst
			findBestWorst(taskProblem);

			// Update CM (èe boljši v CPs kot v CM, zamenjaj delce v CM)
			refreshMemory(taskProblem);

			iter++;

			if (taskProblem.isStopCriteria())
				break;

		}

		// Reset worst
		worst = CPs.get(0);

		// Poici najboljsega preden vrnes
		for (int i = 0; i < pop_size; i++) {

			if (taskProblem.isFirstBetter(CPs.get(i), best)) {
				best = CPs.get(i);
			}

			if (taskProblem.isFirstBetter(worst, CPs.get(i))) {
				worst = CPs.get(i);
			}
		}

		// Vrne najboljši najden cp
		return best; 
	}

	// Dolzina vektorja, Euclidean norm ||x||
	public double vectorLength(double[] x) {
		double res = 0;

		for (int d = 0; d < x.length; d++)
			res = res + Math.pow(x[d], 2);

		return Math.sqrt(res);
	}

	//Level1: STEP1 STEP2
	private void initPopulation(Task problem) throws StopCriteriaException {
		// reset
		CPs = null;
		CPs = new ArrayList<>(pop_size);
		best = null;
		worst = null;

		// STEP1
		// Ustvari pop_size posameznikov
		for (int i = 0; i < pop_size; i++) 
		{
			CPs.add(new CSSSolution(problem));

			// Find best cp
			if (i == 0)
				best = CPs.get(0);
			else if (problem.isFirstBetter(CPs.get(i), best))
				best = CPs.get(i);

			// Find worst cp
			if (i == 0)
				worst = CPs.get(0);
			else if (problem.isFirstBetter(worst, CPs.get(i)))
				worst = CPs.get(i);

			if (problem.isStopCriteria())
				break;
		}

		// STEP2
		// Sort cps in the increasing order po fitnes funkciji
		Collections.sort(CPs, comparator);

		initChargedMemory();
	}

	//Level1: STEP3
	private void initChargedMemory() {
		// Generate the initial charged memory [some of the best solutions] as many as CMS
		CM = null;
		CM = new ArrayList<>(CMS);

		// So ze po vrsti v cm populaciji (en sort manj)
		for (int i = 0; i < CMS; i++) {
			CM.add(CPs.get(i));
		}
	}


	// Izraèuna magnitude of charge
	private void calcMagnitude() {
		// magnitude of charge
		for (int i = 0; i < pop_size; i++) {
			// Pride do deljenje z niè, ker sta worst in best enaka, zato + eps
			CPs.get(i).Q = (CPs.get(i).getEval() - worst.getEval()) 
					/ 
					(best.getEval() - worst.getEval() + eps);
		}	
	}

	// Izraèuna separation distance med delci
	private double[][] calcSepDistance(Task taskProblem) {
		double rij[][] = new double[pop_size][pop_size];

		for (int i = 0; i < pop_size; i++)
			for (int j = 0; j < pop_size; j++) {
				double[] temp_x1 = new double[taskProblem.getNumberOfDimensions()]; // zgoraj
				double[] temp_x2 = new double[taskProblem.getNumberOfDimensions()]; // spodaj

				for (int d = 0; d < taskProblem.getNumberOfDimensions(); d++) {
					temp_x1[d] = CPs.get(i).getVariables()[d] - CPs.get(j).getVariables()[d]; // minus
					temp_x2[d] = CPs.get(i).getVariables()[d] + CPs.get(j).getVariables()[d]; // plus

					temp_x2[d] = (temp_x2[d] / 2.0) - best.getVariables()[d];
				}

				// eps - small positive number to avoid singularity (matlab vrednost eps)
				rij[i][j] = vectorLength(temp_x1) / (vectorLength(temp_x2) + eps);
			}

		return rij;
	}

	// Izraèuna verjetnosti da se privlaèita
	private double[][] calcProb(Task taskProblem) {
		double[][] Pij = new double[pop_size][pop_size];

		for (int i = 0; i < pop_size; i++)
			for (int j = 0; j < pop_size; j++) {
				double pogoj = (CPs.get(i).getEval() - best.getEval())
						/ (CPs.get(j).getEval() - CPs.get(i).getEval() + eps);

				// Ali ima j delec boljši fitnes od delca i ( f(j) > f(i))
				if ((pogoj > Util.nextDouble()) || taskProblem.isFirstBetter(CPs.get(j), CPs.get(i))) {
					Pij[i][j] = 1.0;
				} else
					Pij[i][j] = 0.0;
			}

		return Pij;
	}

	// Izraèuna total force, ki deluje na delec
	private void calcForce(Task taskProblem, double[][] Pij, double[][] rij) {

		for (int d = 0; d < taskProblem.getNumberOfDimensions(); d++) 
		{
			//Enaèba 21, ampak deluje boljše èe je radij vsakega delca 1.0
			double a = 1.0;

			for (int j = 0; j < pop_size; j++) {
				double suma_dim = 0.0;

				for (int i = 0; i < pop_size; i++) {
					if (i == j) // brez samega sebe
						continue;

					double desno = Pij[j][i] * (CPs.get(i).getVariables()[d] - CPs.get(j).getVariables()[d]);
					double i1, i2;

					if (rij[j][i] < a) {
						i1 = 1.0;
						i2 = 0.0;
					} else {
						i1 = 0.0;
						i2 = 1.0;
					}

					double part1 = (CPs.get(i).Q / Math.pow(a, 3)) * rij[j][i] * i1;

					// +eps, kr pride do deljenja z niè in posledièno potem NaN
					double part2 = (CPs.get(i).Q / (Math.pow(rij[j][i], 2) + eps)) * i2;

					suma_dim = suma_dim + ((part1 + part2) * desno);
				}

				// Konèna vrednost za tega j-delca po d-dimenziji
				CPs.get(j).F[d] = suma_dim;//* CPs.get(j).Q;
			}
		}
	}

	// Premakne delce , novi velocity, HS based algorithm èe izven meja
	private void moveParticles(Task taskProblem, double kv, double ka) throws StopCriteriaException {
		boolean[] harmonized = new boolean[pop_size];

		for (int j = 0; j < pop_size; j++) {

			double y[] = CPs.get(j).getDoubleVariables();
			double yv[] = new double[taskProblem.getNumberOfDimensions()];

			harmonized[j] = false;

			for (int d = 0; d < taskProblem.getNumberOfDimensions(); d++) {
				double rand1j = Util.nextDouble();
				double rand2j = Util.nextDouble();

				double xj_new = (ka * rand1j * CPs.get(j).F[d]) + (kv * rand2j * CPs.get(j).v[d] + CPs.get(j).getVariables()[d]);

				// Èe je dimenzija izven meja problema, Harmony search varjanta za novo pozicijo na tej dimenziji
				// RULE 7
				if (taskProblem.isFeasible(xj_new, d) == false)
				{
					harmonized[j] = true;

					if (Util.nextDouble() < CMCR) {
						// Memory considering: randomly select a note stored in
						// HM
						xj_new = CM.get(Util.nextInt(CMS)).getVariables()[d];

						// z vrjetnostjo PAR, pozicijo malo natunaj
						if (Util.nextDouble() < PAR) {
							double b = taskProblem.getInterval()[d] / 1000.0;

							// Pitch adjusting, within +/- b
							xj_new = taskProblem.setFeasible(xj_new + (2.0 * Util.nextDouble() - 1.0) * b, d);
						}
					} 
					else //Randomly generate new position
					{
						// je feasible ker je generirano iz mej problema
						xj_new = taskProblem.getLowerLimit()[d] + Util.nextDouble()
						* (taskProblem.getUpperLimit()[d] - taskProblem.getLowerLimit()[d]);
					}
				}

				// Nova pozicija - stara pozicija = novi velocity
				yv[d] = xj_new - CPs.get(j).getVariables()[d];
				y[d] = xj_new;
			}
			
			if (taskProblem.isStopCriteria())
				break;

			//edino ovrednotenje v kodi
			CSSSolution tmp2 = new CSSSolution(taskProblem.eval(y), yv);

			//A hybrid HS-CSS algorithm forsimultaneous analysis, design andoptimization of trusses via forcemethod
			//Ali Kaveh / Omid Khadem Hosseini
			if (harmonized[j]) 
			{
				// Sortiranje ce je prislo do zamenjave
				Collections.sort(CM, comparator);

				// Zamenjava najslabšega v HM oz. CM.
				if (taskProblem.isFirstBetter(tmp2, CM.get(CM.size() - 1))) 
				{	
					// Najslabšega zbriše.
					CM.remove(CM.size() - 1);

					// Doda novega boljšega noter.
					CM.add(tmp2);
				}			
			}

			// Shrani nazaj v populacijo
			CPs.set(j, tmp2);	
		}
	}

	// Poišèe novega najboljšega in najslabšega.
	private void findBestWorst(Task taskProblem)
	{
		// reset worst
		worst = CPs.get(0);

		for (int j = 0; j < pop_size; j++) 
		{
			// najdi best
			if (taskProblem.isFirstBetter(CPs.get(j), best)) {
				best = CPs.get(j);
			}

			// najdi worst
			if (taskProblem.isFirstBetter(worst, CPs.get(j))) {
				worst = CPs.get(j);
			}
		}

	}

	// Posodobi charge memory populacijo.
	private void refreshMemory(Task taskProblem) {
		for (int i = 0; i < CMS; i++) {
			// update CM (èe boljši v CPs kot v CM, zamenjaj delce v CM)
			Collections.sort(CPs, comparator);

			Collections.sort(CM, comparator);

			if (taskProblem.isFirstBetter(CPs.get(i), CM.get(CMS - 1))) {
				// po èlanku, èe sem dobro razumel
				CM.remove(CMS - 1); // slabšega ven
				CM.add(CPs.get(i)); // boljšega noter
			}
		}

		Collections.sort(CM, comparator);
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		// TODO Auto-generated method stub

	}

}
