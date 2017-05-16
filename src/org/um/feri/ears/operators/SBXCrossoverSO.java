package org.um.feri.ears.operators;

import java.util.EnumMap;

import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.util.Util;

public class SBXCrossoverSO implements CrossoverOperator<Double, Task, DoubleSolution>{


	/**
	 * EPS defines the minimum difference allowed between real values
	 */
	private static final double EPS = 1.0e-14;

	private static final double ETA_C_DEFAULT = 20.0;
	private Double crossoverProbability = 0.9;
	private double distributionIndex = ETA_C_DEFAULT;


	public SBXCrossoverSO()
	{
		this(0.9, 20.0);
	}
	/** 
	 * Constructor
	 * Create a new SBX crossover operator whit a default
	 * index given by <code>DEFAULT_INDEX_CROSSOVER</code>
	 */
	public SBXCrossoverSO(Double crossoverProbability, double distributionIndex) {

		this.crossoverProbability = crossoverProbability;
		this.distributionIndex = distributionIndex;
	}

	@Override
	public DoubleSolution[] execute(DoubleSolution[] parents, Task task) {
		if (parents.length != 2) {
			return null;
		}

		DoubleSolution[] offSpring;
		offSpring = doCrossover(crossoverProbability, parents[0], parents[1], task);
		return offSpring;
	}

	private DoubleSolution[] doCrossover(Double probability, DoubleSolution parent1,
			DoubleSolution parent2, Task task) {
		DoubleSolution[] offSpring = new DoubleSolution[2];

		offSpring[0] = new DoubleSolution(parent1);
		offSpring[1] = new DoubleSolution(parent2);

		int i;
		double rand;
		double y1, y2, yL, yu;
		double c1, c2;
		double alpha, beta, betaq;
		double valueX1, valueX2;

		int numberOfVariables = parent1.getVariables().size();

		if (Util.rnd.nextDouble() <= probability) {
			for (i = 0; i < numberOfVariables; i++) {
				valueX1 = parent1.getValue(i);
				valueX2 = parent2.getValue(i);
				if (Util.rnd.nextDouble() <= 0.5) {
					if (java.lang.Math.abs(valueX1 - valueX2) > EPS) {

						if (valueX1 < valueX2) {
							y1 = valueX1;
							y2 = valueX2;
						} else {
							y1 = valueX2;
							y2 = valueX1;
						}

						yL = task.getLowerLimit()[i];
						yu = task.getUpperLimit()[i];
						rand = Util.rnd.nextDouble();
						beta = 1.0 + (2.0 * (y1 - yL) / (y2 - y1));
						alpha = 2.0 - java.lang.Math.pow(beta, -(distributionIndex + 1.0));

						if (rand <= (1.0 / alpha)) {
							betaq = java.lang.Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
						} else {
							betaq = java.lang.Math.pow((1.0 / (2.0 - rand * alpha)), (1.0 / (distributionIndex + 1.0)));
						}

						c1 = 0.5 * ((y1 + y2) - betaq * (y2 - y1));
						beta = 1.0 + (2.0 * (yu - y2) / (y2 - y1));
						alpha = 2.0 - java.lang.Math.pow(beta,-(distributionIndex + 1.0));

						if (rand <= (1.0 / alpha)) {
							betaq = java.lang.Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
						} else {
							betaq = java.lang.Math.pow((1.0 / (2.0 - rand * alpha)), (1.0 / (distributionIndex + 1.0)));
						}

						c2 = 0.5 * ((y1 + y2) + betaq * (y2 - y1));

						if (c1 < yL)
							c1 = yL;

						if (c2 < yL)
							c2 = yL;

						if (c1 > yu)
							c1 = yu;

						if (c2 > yu)
							c2 = yu;

						if (Util.rnd.nextDouble() <= 0.5) {
							offSpring[0].setValue(i, c2);
							offSpring[1].setValue(i, c1);
						} else {
							offSpring[0].setValue(i, c1);
							offSpring[1].setValue(i, c2);
						}
					} else {
						offSpring[0].setValue(i, valueX1);
						offSpring[1].setValue(i, valueX2);
					}
				} else {
					offSpring[0].setValue(i, valueX2);
					offSpring[1].setValue(i, valueX1);
				}
			}
		}

		return offSpring;
	}
	@Override
	public void setCurrentSolution(MOSolutionBase<Double> current) {
		
	}
	
	@Override
	public EnumMap<EnumAlgorithmParameters, String> getOperatorParameters() {
		EnumMap<EnumAlgorithmParameters, String> para = new EnumMap<EnumAlgorithmParameters, String>(EnumAlgorithmParameters.class);
		para.put(EnumAlgorithmParameters.CR, crossoverProbability+"");
		para.put(EnumAlgorithmParameters.ETA, distributionIndex+"");
		return para;
	}

}
