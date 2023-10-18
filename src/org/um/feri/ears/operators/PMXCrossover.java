package org.um.feri.ears.operators;

import org.um.feri.ears.problems.NumberProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.random.RNG;

public class PMXCrossover implements CrossoverOperator<NumberProblem<Integer>, NumberSolution<Integer>> {

	private double crossoverProbability = 1.0;

	public PMXCrossover()
	{
		this(1.0);
	}
	
	
	public PMXCrossover(double crossoverProbability) {
		if ((crossoverProbability < 0) || (crossoverProbability > 1)) {
			crossoverProbability = 1.0;
		}
		this.crossoverProbability = crossoverProbability;
	}
	
	@Override
	public NumberSolution<Integer>[] execute(NumberSolution<Integer>[] source, NumberProblem<Integer> problem) {
		if (source.length < 2) {
			return null;
		}
		return doCrossover(crossoverProbability, source) ;
	}
	
	/**
	 * Perform the crossover operation
	 *
	 * @param probability Crossover probability
	 * @param parents     Parents
	 * @return An array containing the two offspring
	 */
	public NumberSolution<Integer>[] doCrossover(double probability, NumberSolution<Integer>[] parents) {
		NumberSolution<Integer>[] offspring = new NumberSolution[2];

		offspring[0] = parents[0].copy();
		offspring[1] = parents[1].copy();

		int permutationLength = parents[0].numberOfVariables();

		if (RNG.nextDouble() < probability) {
			int cuttingPoint1;
			int cuttingPoint2;

			// STEP 1: Get two cutting points
			cuttingPoint1 = RNG.nextInt(0, permutationLength);
			cuttingPoint2 = RNG.nextInt(0, permutationLength);
			while (cuttingPoint2 == cuttingPoint1)
				cuttingPoint2 = RNG.nextInt(0, permutationLength);

			if (cuttingPoint1 > cuttingPoint2) {
				int swap;
				swap = cuttingPoint1;
				cuttingPoint1 = cuttingPoint2;
				cuttingPoint2 = swap;
			}

			// STEP 2: Get the subchains to interchange
			int replacement1[] = new int[permutationLength];
			int replacement2[] = new int[permutationLength];
			for (int i = 0; i < permutationLength; i++)
				replacement1[i] = replacement2[i] = -1;

			// STEP 3: Interchange
			for (int i = cuttingPoint1; i <= cuttingPoint2; i++) {
				offspring[0].setValue(i, parents[1].getValue(i));
				offspring[1].setValue(i, parents[0].getValue(i));

				replacement1[parents[1].getValue(i)] = parents[0].getValue(i) ;
				replacement2[parents[0].getValue(i)] = parents[1].getValue(i) ;
			}

			// STEP 4: Repair offspring
			for (int i = 0; i < permutationLength; i++) {
				if ((i >= cuttingPoint1) && (i <= cuttingPoint2))
					continue;

				int n1 = parents[0].getValue(i);
				int m1 = replacement1[n1];

				int n2 = parents[1].getValue(i);
				int m2 = replacement2[n2];

				while (m1 != -1) {
					n1 = m1;
					m1 = replacement1[m1];
				}

				while (m2 != -1) {
					n2 = m2;
					m2 = replacement2[m2];
				}

				offspring[0].setValue(i, n1);
				offspring[1].setValue(i, n2);
			}
		}

		return offspring;
	}


	@Override
	public void setCurrentSolution(NumberSolution<Integer> current) {
		
		
	}
}
