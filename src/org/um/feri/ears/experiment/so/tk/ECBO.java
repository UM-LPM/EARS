package org.um.feri.ears.experiment.so.tk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;
import org.um.feri.ears.util.Util;

//ECBO
public class ECBO extends Algorithm {
	// Size of the population
	int pop_size;

	// Colliding memory size
	int cMS;

	// 0.25 ali 0.3
	double Pro;

	// Population of colliding bodies
	ArrayList<CBOIndividual> CB;

	// Colliding Memory
	ArrayList<CBOIndividual> CM;

	// Best CB by far
	CBOIndividual best = null;
	
	TaskComparator comparator;


	public ECBO(int pop, double PRO) {
		super();
		this.cMS = pop / 10;
		this.pop_size = pop;
		this.Pro = PRO;

		ai = new AlgorithmInfo("Enhanced Coliding body optimisation ", "clanek", "ECBO", "ECBO");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED1, cMS + "");
		ai.addParameter(EnumAlgorithmParameters.UNNAMED2, Pro + "");

		au = new Author("Tadej Klakocer", "tadej.klakocer@student.um.si"); // EARS
		// author
		// info
	}

	// Parametri kot v èlanku.
	public ECBO() {
		this(20, 0.25);
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		comparator = new TaskComparator(taskProblem);
		// celotna populacija
		CB = new ArrayList<>();

		// collison memory
		CM = new ArrayList<>();

		best = null;

		// iteracije se dobro raèunajo
		double iter = 0;
		// double max_iter = 200;//(taskProblem.getMaxEvaluations() - pop_size)
		// / pop_size; //-initpopulation!
		// double max_iter = 10000;
		double max_iter = (taskProblem.getMaxEvaluations() - pop_size) / pop_size; // -initpopulation!

		initPop(taskProblem);

		// glavna zanka algoritma
		while (!taskProblem.isStopCriteria()) {

			// coefficient of restitution
			double cor;

			// calculate maso delcev
			double sum_spodaj = 0;

			for (int i = 0; i < pop_size; i++) {
				sum_spodaj = sum_spodaj + (1.0 / CB.get(i).getEval());
			}

			sum_spodaj = 1.0 / sum_spodaj;

			for (int i = 0; i < pop_size; i++) {
				CB.get(i).masa = (1.0 / CB.get(i).getEval()) / sum_spodaj;
			}

			// Reversed - padajoèe - descending
			// normalno - narašèujoèe - ascending

			// CM - coliding memory
			// nekaj najboljših CB se shrani
			// v naslednji iteraciji daš iz CM v glavno populacijo
			// iz glavne jih toliko odstraniš kot si jih dal noter

			// oèitno vsi problemi minimizirajo
			Collections.sort(CB, comparator);

			if (iter == 0) // prva iteracija samo shrani
			{
				// shranimo za naslednjo iteracijo
				for (int i = 0; i < cMS; i++)
					CM.add(CB.get(i));
			} else // ostale iteracije se
			{
				// prvo dodaj iz CM
				for (int i = 0; i < cMS; i++)
					CB.add(CM.get(i));

				// oèitno vsi problemi minimizirajo
				Collections.sort(CB, comparator);

				// zbriši toliko zadnjih najslabših
				for (int i = 0; i < cMS; i++)
					CB.remove(CB.size() - 1);

				// shranimo za naslednjo iteracijo (zamenjava ce je boljsi)
				// - zgodovinsko najboljši so v CM (skozi celotni proces)

				// oèitno vsi problemi minimizirajo
				Collections.sort(CM, comparator);

				for (int j = 0; j < cMS; j++) {
					// èe že isti noter preskoèi
					if (CM.contains(CB.get(j)))
						continue;

					if (taskProblem.isFirstBetter(CB.get(j), CM.get(j)))
						CM.set(j, CB.get(j));
				}
			}

			// sort po masi(veèja masa , boljši fitnes)
			Collections.sort(CB, comparator);

			// pre-update velocity
			for (int i = 0; i < pop_size / 2; i++) // stacionarni CB
			{
				CB.get(i).v = new double[taskProblem.getDimensions()]; // vse na
				// nulo
			}

			for (int i = pop_size / 2; i < pop_size; i++) // premikajoci se CB,
				// enacba 2
			{
				for (int j = 0; j < taskProblem.getDimensions(); j++)
					CB.get(i).v[j] = CB.get(i - pop_size / 2).getVariables().get(j) - CB.get(i).getVariables().get(j);
			}

			// 1- ( iter / max_iter) enacba 6
			cor = 1.0 - (iter / max_iter);

			// kolizija delcev
			for (int i = 0; i < pop_size / 2; i++) // stacionarni CB enacba 4
			{
				for (int j = 0; j < taskProblem.getDimensions(); j++) {
					double zgoraj = (CB.get(i + pop_size / 2).masa + cor * CB.get(i + pop_size / 2).masa)
							* CB.get(i + pop_size / 2).v[j];
					double spodaj = CB.get(i).masa + CB.get(i + pop_size / 2).masa;

					CB.get(i).v_after[j] = zgoraj / spodaj;
				}
			}

			for (int i = pop_size / 2; i < pop_size; i++) // premikajoci se CB
				// enacba 5
			{
				for (int j = 0; j < taskProblem.getDimensions(); j++) {
					double zgoraj = (CB.get(i).masa - cor * CB.get(i - pop_size / 2).masa) * CB.get(i).v[j];
					double spodaj = CB.get(i).masa + CB.get(i - pop_size / 2).masa;

					CB.get(i).v_after[j] = zgoraj / spodaj;
				}
			}

			// posodobi pozicijo delcev
			for (int i = 0; i < pop_size / 2; i++) // stacionarni CB enacba 7
			{
				CB.get(i).new_x = new double[taskProblem.getDimensions()];

				for (int j = 0; j < taskProblem.getDimensions(); j++) {
					double rand = Util.nextDouble() * 2 - 1; // med -1 in 1
					double new_value = CB.get(i).getVariables().get(j) + rand * CB.get(i).v_after[j];

					CB.get(i).new_x[j] = taskProblem.setFeasible(new_value, j);
				}

				// dodatno za izognitev lokalnega optimuma, ENACBA 10
				if (Util.nextDouble() < Pro) {
					int rand_dimenzija = Util.nextInt(taskProblem.getDimensions());
					CB.get(i).new_x[rand_dimenzija] = taskProblem.setFeasible(taskProblem.getLowerLimit()[rand_dimenzija]
							+ Util.nextDouble() * (taskProblem.getUpperLimit()[rand_dimenzija]
									- taskProblem.getLowerLimit()[rand_dimenzija]),
							rand_dimenzija);
				}
			}

			for (int i = pop_size / 2; i < pop_size; i++) // premikajoci se CB
				// enacba 8
			{
				CB.get(i).new_x = new double[taskProblem.getDimensions()];

				for (int j = 0; j < taskProblem.getDimensions(); j++) {
					double rand = Util.nextDouble() * 2 - 1; // med -1 in 1

					double new_value = CB.get(i - pop_size / 2).getVariables().get(j) + rand * CB.get(i).v_after[j];

					CB.get(i).new_x[j] = taskProblem.setFeasible(new_value, j);
				}

				// enacba 10, izognitev lokalnega optimuma
				if (Util.nextDouble() < Pro) {
					int rand_dimenzija = Util.nextInt(taskProblem.getDimensions());

					CB.get(i).new_x[rand_dimenzija] = taskProblem.setFeasible(taskProblem.getLowerLimit()[rand_dimenzija]
							+ Util.nextDouble() * (taskProblem.getUpperLimit()[rand_dimenzija]
									- taskProblem.getLowerLimit()[rand_dimenzija]),
							rand_dimenzija);
				}
			}

			// uporabi new_x za fitnes oceno CB delca, ocenitev delca
			for (int i = 0; i < pop_size; i++) // premikajoci se CB enacba 8
			{
				CBOIndividual tmp = new CBOIndividual(taskProblem.eval(CB.get(i).getNoviX()));

				// najdi najboljšega za izpis
				if (taskProblem.isFirstBetter(tmp, best)) {
					best = new CBOIndividual(tmp);
				}

				CB.set(i, tmp);

				if (taskProblem.isStopCriteria())
					break;
			}

			iter++;
			if (taskProblem.isStopCriteria())
				break;
			/*
			if (iter == max_iter) {
				System.out.println("iteracija full");
				break;
			}
			 */
		}

		// poici najboljsega preden vrnes
		for (int i = 0; i < pop_size; i++) {
			if (taskProblem.isFirstBetter(CB.get(i), best)) {
				best = new CBOIndividual(CB.get(i));
			}
		}

		return best; //vrni best CB
	}

	@Override
	public void resetDefaultsBeforNewRun() {
		// TODO Auto-generated method stub

	}

	// kreiranje zaèetne populacije
	private void initPop(Task taskProb) throws StopCriteriaException {
		for (int i = 0; i < pop_size; i++) {
			CB.add(new CBOIndividual(taskProb));

			// best set
			if (i == 0)
				best = CB.get(0);
			else if (taskProb.isFirstBetter(CB.get(i), best))
				best = CB.get(i);

			if (taskProb.isStopCriteria())
				break;
		}
	}
}
