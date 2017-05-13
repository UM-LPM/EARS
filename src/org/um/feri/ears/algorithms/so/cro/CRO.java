package org.um.feri.ears.algorithms.so.cro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.operators.PolynomialMutationSO;
import org.um.feri.ears.operators.SBXCrossoverSO;
import org.um.feri.ears.operators.TournamentSelection;
import org.um.feri.ears.algorithms.so.cro.CoralSolution;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;
import org.um.feri.ears.util.Util;

public class CRO extends Algorithm {

	private int N, M; // Grid sizes
	private double rho; // Percentage of initial occupied reef
	private double Fbs, Fbr; // Percentage of broadcast spawners and brooders Fbr = (1 - Fbs)
	private double Fa, Fd; // Percentage of budders and depredated corals
	private double Pd; // Probability of depredation
	private int attemptsToSettle;
	int pop_size;
	Task task;

	TaskComparator comparator;
	protected TournamentSelection selectionOperator;
	protected SBXCrossoverSO crossoverOperator = new SBXCrossoverSO(0.9, 20.0);
	protected PolynomialMutationSO mutationOperator = new PolynomialMutationSO(1.0 / 10, 20.0);

	protected List<CoralSolution> coralReef;

	CoralSolution best;

	public CRO() {
		this(10, 10, 0.6, 0.9, 0.1, 0.1, 3);
	}

	/**
	 * Constructor
	 * 
	 * @param n
	 *            width of Coral Reef Grid
	 * @param m
	 *            height of Coral Reef Grid
	 * @param rho
	 *            Percentage of occupied reef
	 * @param fbs
	 *            Percentage of broadcast spawners
	 * @param fa
	 *            Percentage of budders
	 * @param pd
	 *            Probability of depredation
	 * @param attemptsToSettle
	 *            number of attempts a larvae has to try to settle reef
	 */
	public CRO(int n, int m, double rho, double fbs, double fa, double pd,
			int attemptsToSettle) {
		super();
		N = n;
		M = m;
		this.rho = rho;
		Fbs = fbs;
		Fbr =  1 - Fbs;
		Fa = fa;
		Fd = Fa;
		Pd = pd;
		this.attemptsToSettle = attemptsToSettle;
		pop_size = N * M;

		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("CRO",
				"@article{salcedo2014coral,"
				+ "title={The coral reefs optimization algorithm: a novel metaheuristic for efficiently solving optimization problems}, "
				+ "author={Salcedo-Sanz, S and Del Ser, J and Landa-Torres, I and Gil-L{\'o}pez, S and Portilla-Figueras, JA}, "
				+ "journal={The Scientific World Journal}, volume={2014},year={2014},publisher={Hindawi Publishing Corporation}}",
				"CRO", "Coral Reefs Optimization");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;

		List<DoubleSolution> broadcastSpawners;
		List<DoubleSolution> brooders;
		List<DoubleSolution> larvae;
		List<DoubleSolution> budders;

		comparator = new TaskComparator(task);
		selectionOperator = new TournamentSelection(2, comparator);

		createInitialPopulation();

		while (!task.isStopCriteria()) {
			
			int quantity = (int) (Fbs * coralReef.size());

			if ((quantity % 2) == 1) {
				quantity--;
			}
			broadcastSpawners = new ArrayList<DoubleSolution>(quantity);
			brooders = new ArrayList<DoubleSolution>(coralReef.size() - quantity);
			
			selectBroadcastSpawners(quantity, broadcastSpawners, brooders);

			if (task.isStopCriteria()) {
				break;
			}
			// External sexual reproduction (broadcast spawning)
			larvae = sexualReproduction(broadcastSpawners);

			if (task.isStopCriteria()) {
				break;
			}
			// Internal sexual reproduction (brooding)
			larvae.addAll(asexualReproduction(brooders));

			// Larvae settlement phase
			coralReef = larvaeSettlementPhase(larvae, coralReef);

			
			// Asexual reproduction (budding)
			Collections.sort(coralReef, comparator);
			budders = new ArrayList<DoubleSolution>((int) (Fa * coralReef.size()));
			for (int i = 0; i < budders.size(); i++) {
				budders.add(coralReef.get(i));
			}
			coralReef = larvaeSettlementPhase(budders, coralReef);

			// Depredation in Polyp Phase
			Collections.sort(coralReef, comparator);
			coralReef = depredation(coralReef);
			task.incrementNumberOfIterations();
		}
		Collections.sort(coralReef, comparator);
		return coralReef.get(0);
	}

	protected void createInitialPopulation() throws StopCriteriaException {
		// At inizialiazation populate only part of the coral reef (rho)
		int quantity = (int) (rho * N * M);
		coralReef = new ArrayList<>(N * M);
		CoralSolution newCoral = new CoralSolution(task.getRandomSolution());
		Coordinate co = new Coordinate(Util.nextInt(0, N - 1), Util.nextInt(0, M - 1));
		newCoral.setCoralPosition(co);
		coralReef.add(newCoral);
		
		for (int i = 1; i < quantity; i++) {
			newCoral = new CoralSolution(task.getRandomSolution());
			co = new Coordinate(Util.nextInt(0, N - 1), Util.nextInt(0, M - 1));
			while(getCoralFromPosition(co) != null)
			{
				co = new Coordinate(Util.nextInt(0, N - 1), Util.nextInt(0, M - 1));
			}
			newCoral.setCoralPosition(co);
			coralReef.add(newCoral);
		}

	}


	private CoralSolution getCoralFromPosition(Coordinate co) {
		
		for(CoralSolution coral: coralReef)
		{
			if(coral.getCoralPosition() != null && coral.getCoralPosition().equals(co))
				return coral;
		}
		
		return null;
	}

	protected void selectBroadcastSpawners(int quantity, List<DoubleSolution> spawners, List<DoubleSolution> brooders) {

		int[] per = Util.randomPermutation(coralReef.size());

		for (int i = 0; i < coralReef.size(); i++) {
			
			if(i < quantity)
			{
				spawners.add(coralReef.get(per[i]));
			}
			else // The remaining corals in the reef are selected as brooders
			{
				brooders.add(coralReef.get(per[i]));
			}
		}
	}

	protected List<DoubleSolution> sexualReproduction(List<DoubleSolution> broadcastSpawners)
			throws StopCriteriaException {
		DoubleSolution[] parents = new DoubleSolution[2];
		List<DoubleSolution> larvae = new ArrayList<DoubleSolution>(broadcastSpawners.size() / 2);

		while (broadcastSpawners.size() > 0) {
			parents[0] = selectionOperator.execute(broadcastSpawners, task);
			parents[1] = selectionOperator.execute(broadcastSpawners, task);

			broadcastSpawners.remove(parents[0]);
			// If the parents are not the same
			if (broadcastSpawners.contains(parents[1])) {
				broadcastSpawners.remove(parents[1]);
			}

			DoubleSolution newSolution = crossoverOperator.execute(parents, task)[0];
			if (task.isStopCriteria()) {
				break;
			}

			newSolution = task.eval(newSolution);

			larvae.add(newSolution);

			parents = new CoralSolution[2];

		}

		return larvae;
	}

	protected List<DoubleSolution> asexualReproduction(List<DoubleSolution> brooders) throws StopCriteriaException {
		int sz = brooders.size();

		List<DoubleSolution> larvae = new ArrayList<DoubleSolution>(sz);

		for (int i = 0; i < sz; i++) {
			DoubleSolution newSolution = mutationOperator.execute(brooders.get(i), task);
			if (task.isStopCriteria()) {
				break;
			}
			newSolution = task.eval(newSolution);

			larvae.add(newSolution);
		}
		return larvae;
	}

	protected List<CoralSolution> larvaeSettlementPhase(List<DoubleSolution> larvae, List<CoralSolution> population) {

		int attempts = attemptsToSettle;
		for (DoubleSolution larva : larvae) {

 			for (int attempt = 0; attempt < attempts; attempt++) {
				Coordinate C = new Coordinate(Util.nextInt(0, N - 1), Util.nextInt(0, M - 1));

				// Add larva to the reef
				CoralSolution coral = getCoralFromPosition(C);
				if (coral == null) {
					CoralSolution newSolution = new CoralSolution(larva);
					newSolution.setCoralPosition(C);
					population.add(newSolution);
					break;
				}
				
				// Replace the existing coral with the larva if it is better
				if (task.isFirstBetter(larva, coral)) {
					CoralSolution newSolution = new CoralSolution(larva);
					newSolution.setCoralPosition(C);
					population.add(newSolution);
					population.remove(coral); // remove coral from current position
					break;
				}
			}
		}

		return population;
	}

	protected List<CoralSolution> depredation(List<CoralSolution> population) {
		int popSize = population.size();
		int quantity = (int) (Fd * popSize);

		quantity = popSize - quantity;

		double coin;
		for (int i = popSize - 1; i > quantity; i--) {
			coin = Util.rnd.nextDouble();

			if (coin < Pd) {
				population.remove(population.size() - 1);
			}

		}

		return population;
	}

	@Override
	public void resetDefaultsBeforNewRun() {

	}

}
