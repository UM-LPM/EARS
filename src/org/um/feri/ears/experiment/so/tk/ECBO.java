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

// V �lanku se za�ne ta algoritem v poglavju 
// 2.2. Enhanced Colliding Bodies Optimization (ECBO)

//ECBO
public class ECBO extends Algorithm {
	// Size of the population
	private int pop_size;

	// Colliding memory size
	private int cMS;

	// 0.25 ali 0.3
	private double Pro;

	// Population of colliding bodies
	private ArrayList<ECBOSolution> CB;

	// Colliding Memory
	private ArrayList<ECBOSolution> CM;

	// Best CB by far
	private ECBOSolution best = null;

	private TaskComparator comparator;

	public ECBO(int pop, double PRO) {
		super();
		this.cMS = pop / 10;
		this.pop_size = pop;
		this.Pro = PRO;

		au = new Author("Tadej Klakocer", "tadej.klakocer@student.um.si"); // EARS
		ai = new AlgorithmInfo("Enhanced Coliding body optimisation ",
				"A.Kaveh, M.Ilchi Ghazaan, Enhanced colliding bodies optimization for design problems with continuous and discrete variables, Advances in Engineering Software 77, 2014",
				"ECBO", "Physics-based metaheuristic algorithm");

		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED1, cMS + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED2, Pro + "");

	}

	// Parametri kot v �lanku.
	public ECBO() {
		this(20, 0.25);
	}

	//Glavna zanka algoritma (zagon)
	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		comparator = new TaskComparator(taskProblem);

		// Celotna populacija
		CB = new ArrayList<>();

		// Collision memory
		CM = new ArrayList<>();

		best = null;

		double iter = 0;
		double max_iter = (taskProblem.getMaxEvaluations() - pop_size) / pop_size;

		initPop(taskProblem);

		// Glavna zanka algoritma.
		while (!taskProblem.isStopCriteria()) {

			// Coefficient of restitution
			double cor;

			DefineMass(taskProblem);

			Saving(iter, taskProblem);

			// Sortiranje po masi(ve�ja masa , bolj�i fitnes)
			Collections.sort(CB, comparator);

			// pre-update velocity
			for (int i = 0; i < pop_size / 2; i++) // Stacionarni delci v CB
			{
				CB.get(i).v = new double[taskProblem.getNumberOfDimensions()];
			}

			for (int i = pop_size / 2; i < pop_size; i++) // Premikajoci se delci v CB, enacba 2
			{
				for (int j = 0; j < taskProblem.getNumberOfDimensions(); j++)
					CB.get(i).v[j] = CB.get(i - pop_size / 2).getValue(j) - CB.get(i).getValue(j);
			}

			// enacba 6
			cor = 1.0 - (iter / max_iter);

			Collision(cor, taskProblem);

			UpdateCBs(taskProblem);

			iter++;
			
			if (taskProblem.isStopCriteria())
				break;

		}

		// Poi��i najbolj�o najdeno re�itev, preden jo vrne�.
		for (int i = 0; i < pop_size; i++) {
			if (taskProblem.isFirstBetter(CB.get(i), best)) {
				best = new ECBOSolution(CB.get(i));
			}
		}

		return best; // Vrni najbolj�o najdeno re�itev.
	}

	@Override
	public void resetDefaultsBeforNewRun() {

	}

	
	
	//Poglavje 2.2 Step1
	// Ustvarjanje za�etne populacije.
	private void initPop(Task taskProb) throws StopCriteriaException {
		for (int i = 0; i < pop_size; i++) {
			CB.add(new ECBOSolution(taskProb));

			// best set
			if (i == 0)
				best = CB.get(0);
			else if (taskProb.isFirstBetter(CB.get(i), best))
				best = CB.get(i);

			if (taskProb.isStopCriteria())
				break;
		}
	}

	//Poglavj 2.2 Step2
	//2.2 Step2 Defining mass
	private void DefineMass(Task taskprob){
		// Ra�unanje mase delcev.
		double sum_spodaj = 0;

		for (int i = 0; i < pop_size; i++) {
			sum_spodaj = sum_spodaj + (1.0 / CB.get(i).getEval());
		}

		sum_spodaj = 1.0 / sum_spodaj;

		for (int i = 0; i < pop_size; i++) {
			CB.get(i).masa = (1.0 / CB.get(i).getEval()) / sum_spodaj;
		}

	}

	//Poglavje 2.2 Step3
	//2.2 Step3 Saving - CM memory
	private void Saving(double iter, Task taskProb){
		// CM - coliding memory
		// nekaj najbolj�ih CB se shrani
		// v naslednji iteraciji da� iz CM v glavno populacijo
		// iz glavne jih toliko odstrani� kot si jih dal noter

		Collections.sort(CB, comparator);

		if (iter == 0) // Prva iteracija samo shrani.
		{
			// Shranimo za naslednjo iteracijo.
			for (int i = 0; i < cMS; i++)
				CM.add(CB.get(i));
		} else // Ostale iteracije med izvajanjem.
		{
			// Prvo dodaj iz CM v CB populacijo.
			for (int i = 0; i < cMS; i++)
				CB.add(CM.get(i));

			Collections.sort(CB, comparator);

			// Izbri�i toliko zadnjih najslab�ih iz CB populacije
			for (int i = 0; i < cMS; i++)
				CB.remove(CB.size() - 1);

			// shranimo za naslednjo iteracijo (zamenjava ce je boljsi)
			// - zgodovinsko najbolj�i so v CM (skozi celotni proces)

			Collections.sort(CM, comparator);

			for (int j = 0; j < cMS; j++) {
				// �e �e isti noter presko�i.
				if (CM.contains(CB.get(j)))
					continue;

				if (taskProb.isFirstBetter(CB.get(j), CM.get(j)))
					CM.set(j, CB.get(j));
			}
		}

	}

	//Poglavje 2.2 Step4,5,6
	private void Collision(double cor, Task taskProb){
		// Trki med delci.
		for (int i = 0; i < pop_size / 2; i++) // Stacionarni delci v CB, enacba 4
		{
			for (int j = 0; j < taskProb.getNumberOfDimensions(); j++) {
				double zgoraj = (CB.get(i + pop_size / 2).masa + cor * CB.get(i + pop_size / 2).masa)
						* CB.get(i + pop_size / 2).v[j];
				double spodaj = CB.get(i).masa + CB.get(i + pop_size / 2).masa;

				CB.get(i).v_after[j] = zgoraj / spodaj;
			}
		}

		for (int i = pop_size / 2; i < pop_size; i++) // Premikajoci se delci v CB, enacba 5
		{
			for (int j = 0; j < taskProb.getNumberOfDimensions(); j++) {
				double zgoraj = (CB.get(i).masa - cor * CB.get(i - pop_size / 2).masa) * CB.get(i).v[j];
				double spodaj = CB.get(i).masa + CB.get(i - pop_size / 2).masa;

				CB.get(i).v_after[j] = zgoraj / spodaj;
			}
		}

	}

	//Poglavje 2.2 Step7, 8
	private void UpdateCBs(Task taskProb) throws StopCriteriaException {
		// Posodobi pozicijo delcev (premik).
		for (int i = 0; i < pop_size / 2; i++) // Stacionarni delci v CB, enacba 7
		{
			CB.get(i).new_x = new double[taskProb.getNumberOfDimensions()];

			for (int j = 0; j < taskProb.getNumberOfDimensions(); j++) {
				double rand = Util.nextDouble() * 2 - 1; // med -1 in 1
				double new_value = CB.get(i).getValue(j) + rand * CB.get(i).v_after[j];

				CB.get(i).new_x[j] = taskProb.setFeasible(new_value, j);
			}

			// Dodatno za izognitev lokalnega optimuma, enacba 10
			if (Util.nextDouble() < Pro) {
				int rand_dimenzija = Util.nextInt(taskProb.getNumberOfDimensions());
				CB.get(i).new_x[rand_dimenzija] = taskProb
						.setFeasible(taskProb.getLowerLimit()[rand_dimenzija]
								+ Util.nextDouble() * (taskProb.getUpperLimit()[rand_dimenzija]
										- taskProb.getLowerLimit()[rand_dimenzija]),
								rand_dimenzija);
			}
		}

		for (int i = pop_size / 2; i < pop_size; i++) // Premikajoci se delci v CB, enacba 8
		{
			CB.get(i).new_x = new double[taskProb.getNumberOfDimensions()];

			for (int j = 0; j < taskProb.getNumberOfDimensions(); j++) {
				double rand = Util.nextDouble() * 2 - 1; // med -1 in 1

				double new_value = CB.get(i - pop_size / 2).getValue(j) + rand * CB.get(i).v_after[j];

				CB.get(i).new_x[j] = taskProb.setFeasible(new_value, j);
			}

			// Dodatno za izognitev lokalnega optimuma, enacba 10
			if (Util.nextDouble() < Pro) {
				int rand_dimenzija = Util.nextInt(taskProb.getNumberOfDimensions());

				CB.get(i).new_x[rand_dimenzija] = taskProb
						.setFeasible(taskProb.getLowerLimit()[rand_dimenzija]
								+ Util.nextDouble() * (taskProb.getUpperLimit()[rand_dimenzija]
										- taskProb.getLowerLimit()[rand_dimenzija]),
								rand_dimenzija);
			}
		}

		// Uporabi new_x za fitnes oceno CB delca (ocenitev re�itve)
		for (int i = 0; i < pop_size; i++) {
			ECBOSolution tmp = new ECBOSolution(taskProb.eval(CB.get(i).getNoviX()));

			// Najdi bolj�ega od trenutno najbolj�ega delca.
			if (taskProb.isFirstBetter(tmp, best)) {
				best = new ECBOSolution(tmp);
			}

			CB.set(i, tmp);

			if (taskProb.isStopCriteria())
				break;
		}
	}

}
