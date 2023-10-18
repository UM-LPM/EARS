package org.um.feri.ears.algorithms.so.cro;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.operators.PolynomialMutationSO;
import org.um.feri.ears.operators.SBXCrossoverSO;
import org.um.feri.ears.operators.TournamentSelection;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.List;

public class CRO extends NumberAlgorithm {

	@AlgorithmParameter(name = "population size")
	private int popSize;
	@AlgorithmParameter(description = "width of Coral Reef Grid")
	private int n;
	@AlgorithmParameter(description = "height of Coral Reef Grid")
	private int m;
	@AlgorithmParameter(description = "percentage of initial occupied reef")
	private double rho;
	@AlgorithmParameter(description = "percentage of broadcast spawners")
	private double fbs;
	@AlgorithmParameter(description = "percentage of broadcast brooders fbr = (1 - fbs)", isTunable = false)
	private double fbr;
	@AlgorithmParameter(name = "percentage of budders")
	private double fa;
	@AlgorithmParameter(name = "percentage of depredated corals")
	private double fd;
	@AlgorithmParameter(name = "probability of depredation")
	private double pd;
	@AlgorithmParameter(name = "attempts to settle")
	private int attemptsToSettle;

	private ProblemComparator<NumberSolution<Double>> comparator;
	private TournamentSelection<NumberSolution<Double>, DoubleProblem> selectionOperator;
	private SBXCrossoverSO crossoverOperator = new SBXCrossoverSO(0.9, 20.0);
	private PolynomialMutationSO mutationOperator = new PolynomialMutationSO(1.0 / 10, 20.0);

	private List<CoralSolution> coralReef;

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
		this.n = n;
		this.m = m;
		this.rho = rho;
		this.fbs = fbs;
		this.fbr =  1 - this.fbs;
		this.fa = fa;
		this.fd = this.fa;
		this.pd = pd;
		this.attemptsToSettle = attemptsToSettle;
		popSize = this.n * this.m;

		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("CRO", "Coral Reefs Optimization",
				"@article{salcedo2014coral,"
				+ "title={The coral reefs optimization algorithm: a novel metaheuristic for efficiently solving optimization problems}, "
				+ "author={Salcedo-Sanz, S and Del Ser, J and Landa-Torres, I and Gil-L{'o}pez, S and Portilla-Figueras, JA}, "
				+ "journal={The Scientific World Journal}, volume={2014},year={2014},publisher={Hindawi Publishing Corporation}}"
		);
	}

	@Override
	public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
		this.task = task;

		List<NumberSolution<Double>> broadcastSpawners;
		List<NumberSolution<Double>> brooders;
		List<NumberSolution<Double>> larvae;
		List<NumberSolution<Double>> budders;

		comparator = new ProblemComparator<>(task.problem);
		selectionOperator = new TournamentSelection(2, comparator);

		createInitialPopulation();

		while (!task.isStopCriterion()) {
			
			int quantity = (int) (fbs * coralReef.size());

			if ((quantity % 2) == 1) {
				quantity--;
			}
			broadcastSpawners = new ArrayList<>(quantity);
			brooders = new ArrayList<>(coralReef.size() - quantity);
			
			selectBroadcastSpawners(quantity, broadcastSpawners, brooders);

			if (task.isStopCriterion()) {
				break;
			}
			// External sexual reproduction (broadcast spawning)
			larvae = sexualReproduction(broadcastSpawners);

			if (task.isStopCriterion()) {
				break;
			}
			// Internal sexual reproduction (brooding)
			larvae.addAll(asexualReproduction(brooders));

			// Larvae settlement phase
			coralReef = larvaeSettlementPhase(larvae, coralReef);
			
			// Asexual reproduction (budding)
			coralReef.sort(comparator);
			budders = new ArrayList<>((int) (fa * coralReef.size()));
			for (int i = 0; i < budders.size(); i++) {
				budders.add(coralReef.get(i));
			}
			coralReef = larvaeSettlementPhase(budders, coralReef);

			// Depredation in Polyp Phase
			coralReef.sort(comparator);
			coralReef = depredation(coralReef);
			task.incrementNumberOfIterations();
		}
		coralReef.sort(comparator);
		return coralReef.get(0);
	}

	private void createInitialPopulation() throws StopCriterionException {
		// At inizialiazation populate only part of the coral reef (rho)
		int quantity = (int) (rho * n * m);
		coralReef = new ArrayList<>(n * m);
		CoralSolution newCoral = new CoralSolution(task.getRandomEvaluatedSolution());
		Coordinate co = new Coordinate(RNG.nextInt(0, n), RNG.nextInt(0, m));
		newCoral.setCoralPosition(co);
		coralReef.add(newCoral);
		
		for (int i = 1; i < quantity; i++) {
			if(task.isStopCriterion())
				break;
			newCoral = new CoralSolution(task.getRandomEvaluatedSolution());
			co = new Coordinate(RNG.nextInt(0, n), RNG.nextInt(0, m));
			while(getCoralFromPosition(co) != null)
			{
				co = new Coordinate(RNG.nextInt(0, n), RNG.nextInt(0, m));
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

	private void selectBroadcastSpawners(int quantity, List<NumberSolution<Double>> spawners, List<NumberSolution<Double>> brooders) {

		int[] per = RNG.randomPermutation(coralReef.size());

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

	private List<NumberSolution<Double>> sexualReproduction(List<NumberSolution<Double>> broadcastSpawners)
			throws StopCriterionException {
		NumberSolution<Double>[] parents = new NumberSolution[2];
		List<NumberSolution<Double>> larvae = new ArrayList<>(broadcastSpawners.size() / 2);

		while (broadcastSpawners.size() > 0) {
			parents[0] = selectionOperator.execute(broadcastSpawners, task.problem);
			parents[1] = selectionOperator.execute(broadcastSpawners, task.problem);

			broadcastSpawners.remove(parents[0]);
			// If the parents are not the same
			if (broadcastSpawners.contains(parents[1])) {
				broadcastSpawners.remove(parents[1]);
			}

			NumberSolution<Double> newSolution = crossoverOperator.execute(parents, task.problem)[0];
			if (task.isStopCriterion()) {
				break;
			}

			task.eval(newSolution);

			larvae.add(newSolution);

			parents = new CoralSolution[2];
		}
		return larvae;
	}

	private List<NumberSolution<Double>> asexualReproduction(List<NumberSolution<Double>> brooders) throws StopCriterionException {
		int sz = brooders.size();

		List<NumberSolution<Double>> larvae = new ArrayList<>(sz);

		for (int i = 0; i < sz; i++) {
			NumberSolution<Double> newSolution = mutationOperator.execute(brooders.get(i), task.problem);
			if (task.isStopCriterion()) {
				break;
			}
			task.eval(newSolution);

			larvae.add(newSolution);
		}
		return larvae;
	}

	private List<CoralSolution> larvaeSettlementPhase(List<NumberSolution<Double>> larvae, List<CoralSolution> population) {

		int attempts = attemptsToSettle;
		for (NumberSolution<Double> larva : larvae) {

 			for (int attempt = 0; attempt < attempts; attempt++) {
				Coordinate C = new Coordinate(RNG.nextInt(0, n), RNG.nextInt(0, m));

				// Add larva to the reef
				CoralSolution coral = getCoralFromPosition(C);
				if (coral == null) {
					CoralSolution newSolution = new CoralSolution(larva);
					newSolution.setCoralPosition(C);
					population.add(newSolution);
					break;
				}
				
				// Replace the existing coral with the larva if it is better
				if (task.problem.isFirstBetter(larva, coral)) {
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

	private List<CoralSolution> depredation(List<CoralSolution> population) {
		int popSize = population.size();
		int quantity = (int) (fd * popSize);

		quantity = popSize - quantity;

		double coin;
		for (int i = popSize - 1; i > quantity; i--) {
			coin = RNG.nextDouble();

			if (coin < pd) {
				population.remove(population.size() - 1);
			}
		}
		return population;
	}

	@Override
	public void resetToDefaultsBeforeNewRun() {
	}

	/**
	 * Represents a Coordinate in Coral Reef Grid
	 *
	 * @author inacio-medeiros
	 *
	 */
	public class Coordinate implements Comparable<Coordinate> {
		private int x, y;

		/**
		 * Constructor
		 *
		 * @param x
		 *            Coordinate's x-position
		 * @param y
		 *            Coordinate's y-position
		 */
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Retrieves Coordinate's x-position
		 *
		 * @return Coordinate's x-position
		 */
		public int getX() {
			return x;
		}

		/**
		 * Retrieves Coordinate's y-position
		 *
		 * @return Coordinate's y-position
		 */
		public int getY() {
			return y;
		}

		/**
		 * Sets Coordinate's x-position to a new value
		 *
		 * @param x
		 *            new value for Coordinate's x-position
		 */
		public void setX(int x) {
			this.x = x;
		}

		/**
		 * Sets Coordinate's y-position to a new value
		 *
		 * @param y
		 *            new value for Coordinate's y-position
		 */
		public void setY(int y) {
			this.y = y;
		}

		@Override
		public int compareTo(Coordinate arg0) {
			int diffX = Math.abs(arg0.x - this.x);
			int diffY = Math.abs(arg0.y - this.y);
			double result = Math.sqrt((diffX * diffX) + (diffY * diffY));

			return Integer.parseInt(Double.toString(result));
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Coordinate other = (Coordinate) obj;

			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

	}

	public class CoralSolution extends NumberSolution<Double> {

		/**
		 * The position of the coral on the coral reef
		 */
		private Coordinate coralPosition;

		public CoralSolution(CoralSolution coralSolution) {
			super(coralSolution);
			this.coralPosition = coralSolution.coralPosition;
		}

		public CoralSolution(NumberSolution<Double> solution) {
			super(solution);
		}

		public Coordinate getCoralPosition() {
			return coralPosition;
		}

		public void setCoralPosition(Coordinate coralPosition) {
			this.coralPosition = coralPosition;
		}

	}
}


