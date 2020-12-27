//  DEMO.java
//
//  Author:
//       Miha Mlakar <miha.mlakar@ijs.si>
//       Dejan Petelein <dejan.petelin@ijs.si>
//
//  Copyright (c) 2011 Miha Mlakar, Dejan Petelin
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package org.um.feri.ears.algorithms.moo.demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.spea2.Spea2fitness;
import org.um.feri.ears.operators.CrossoverOperator;
import org.um.feri.ears.operators.DifferentialEvolutionCrossover;
import org.um.feri.ears.operators.DifferentialEvolutionSelection;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.CrowdingComparator;
import org.um.feri.ears.util.Distance;
import org.um.feri.ears.util.DominanceComparator;
import org.um.feri.ears.util.Ranking;

public class DEMO<T extends MOTask, Type extends Number> extends MOAlgorithm<T, Type> {

	ParetoSolution<Type> population          ;
	ParetoSolution<Type> offspringPopulation ;
	ParetoSolution<Type> union               ;

	int populationSize ;
    int selectionProcedure;
    Ranking<Type> ranking;
    int numberOfChildBetter;
    int numberOfParentBetter;
    int numberOfIncomparable;
    double kappa = 0.05;
    double rho = 2.0;

    Distance<Type>   distance  ;
    DominanceComparator<Type> dominance ;

    MOSolutionBase<Type> parents[] ;
    
	CrossoverOperator<Type, T, MOSolutionBase<Type>> cross;
    

	public DEMO(CrossoverOperator crossover, int populationSize, int selectionProcedure) {
		
		this.cross = crossover;
		this.populationSize = populationSize;
		this.selectionProcedure = selectionProcedure;

		au = new Author("miha", "miha.ravber at gamil.com");
		ai = new AlgorithmInfo(
				"DEMO",
				"\\bibitem{Robic2005}\nT.~Robi�, B.~Filipi�\n\\newblock DEMO: Differential Evolution for Multiobjective Optimization.\n\\newblock \\emph{Evolutionary Multi-Criterion Optimization}, 520-533, 2005.\n",
				"DEMO", "Differential Evolution for Multiobjective Optimization");
		
		ai.addParameters(crossover.getOperatorParameters());
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, populationSize+"");
		
	}
	
	@Override
	protected void init() {
		population = new ParetoSolution<Type>(populationSize * 2);
	}
	
	@Override
	protected void start() throws StopCriterionException {
		
		distance = new Distance<Type>();
		dominance = new DominanceComparator<Type>();
		DifferentialEvolutionCrossover dec = new DifferentialEvolutionCrossover();
		DifferentialEvolutionSelection<Type> des = new DifferentialEvolutionSelection<Type>();

		// Create the initial solutionSet
		MOSolutionBase<Type> newSolution;
		for (int i = 0; i < populationSize; i++) {
			if (task.isStopCriterion())
				return;
			newSolution = new MOSolutionBase<Type>(task.getRandomMOSolution());
			// problem.evaluateConstraints(newSolution);
			population.add(newSolution);
		}

		while (!task.isStopCriterion()) {
            // Create the offSpring solutionSet (empty)      
            //offspringPopulation  = new SolutionSet(populationSize*2);        

			numberOfChildBetter = 0;
			numberOfParentBetter = 0;
			numberOfIncomparable = 0;	
			for (int i = 0; i < populationSize; i++){   
				// Obtain parents. Two parameters are required: the population and the index of the current individual
				try {
					des.setCurrentIndex(i);
					parents = des.execute(population, task);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("The population has less than four solutions");
					break;
				}

				MOSolutionBase<Type> child;
				// Crossover. Two parameters are required: the current
				// individual and the array of parents
				cross.setCurrentSolution(population.get(i));
				child = cross.execute(parents, task)[0];

				if (task.isStopCriterion())
					break;
				task.eval(child);

				// Dominance test
				int result  ;
				if(areSolutionsTheSame(population.get(i), child) == false)
				{
					result = dominance.compare(population.get(i), child) ;
					if (result == -1) { // Solution i dominates child
						numberOfParentBetter ++;                    
					} // if
					else if (result == 1) { // child dominates
						population.replace(i, child);
						numberOfChildBetter ++;                    	                      
					} // else if
					else { // the two solutions are non-dominated
						population.add(child) ;
						numberOfIncomparable ++;
					} // else
				}

				else // the two solutions are the same so we do not add the child
				{
					numberOfIncomparable ++;
				}

			} // for           

        
            /* Environmental selection
             * Demo modifikacija: PARETO OPTIMALNE FRONTE + METRIKE NAKOPI�ENOSTI
             * Demo mora omogo�at, da to delamo na 3 na�ine in sicer: - NSGA 2
             *                                                        - IBEA
             *                                                        - SPEA 2                                                        
             */
            
			if(selectionProcedure == 0){         //NSGA 2
				// Ranking the offspring population
				ranking = new Ranking<Type>(population); 
				int remain = populationSize;
				int index  = 0;
				ParetoSolution<Type> front = null;
				population = environmentalSelectionNSGAII(ranking, distance, front, remain, index);
			}
			else if(selectionProcedure == 1){    //IBEA
				while (population.size() > populationSize) {		//offspringPopulation
					Object[] result = calculateFitness(population);
					ArrayList<List<Double>> indicatorValues_ = (ArrayList<List<Double>> )result[0];
					double maxIndicatorValue_ = Double.valueOf(result[1].toString());
					removeWorst(population, indicatorValues_, maxIndicatorValue_);
				}   
			}else if(selectionProcedure == 2){   //SPEA 2
				population = environmentalSelectionSPEA2(population, populationSize);	//offspringPopulation
			}              

            /* we get population of reduced (half) size after the environmental selection               
             * so we double it back. 
             */
            //population.setMaxSize(populationSize*2);
			task.incrementNumberOfIterations();
          }

          // Return the first non-dominated front
          ranking = new Ranking(population);
          best = ranking.getSubfront(0);
	}
	
	private boolean areSolutionsTheSame(MOSolutionBase<Type> parent, MOSolutionBase<Type> child) {

		for(int i=0; i< num_var; i++)
		{
			if(parent.getValue(i) != child.getValue(i))
				return false;
		}
		return true;
	}
	
	public void removeWorst(ParetoSolution solutionSet, ArrayList<List<Double>> indicatorValues_, double maxIndicatorValue_) {
		// Find the worst;
		double worst = solutionSet.get(0).getFitness();
		int worstIndex = 0;

		for (int i = 1; i < solutionSet.size(); i++) {
			if (solutionSet.get(i).getFitness() > worst) {
				worst = solutionSet.get(i).getFitness();
				worstIndex = i;
			}
		}

		// Update the population
		for (int i = 0; i < solutionSet.size(); i++) {
			if (i != worstIndex) {
				double fitness = solutionSet.get(i).getFitness();
				fitness -= Math.exp((-indicatorValues_.get(worstIndex).get(i) / maxIndicatorValue_) / kappa);
				solutionSet.get(i).setFitness(fitness);
			}
		}

		// remove worst from the indicatorValues list
		indicatorValues_.remove(worstIndex); // Remove its own list
		Iterator<List<Double>> it = indicatorValues_.iterator();
		while (it.hasNext()) {
			it.next().remove(worstIndex);
		}

		// remove the worst individual from the population
		solutionSet.remove(worstIndex);
	}

	public Object[] calculateFitness(ParetoSolution solutionSet) {
		// Obtains the lower and upper bounds of the population
		double[] maximumValues = new double[num_obj];
		double[] minimumValues = new double[num_obj];

		for (int i = 0; i < num_obj; i++) {
			maximumValues[i] = -Double.MAX_VALUE; // i.e., the minus maxium value
			minimumValues[i] = Double.MAX_VALUE; // i.e., the maximum value
		}

		for (int pos = 0; pos < solutionSet.size(); pos++) {
			for (int obj = 0; obj < num_obj; obj++) {
				double value = solutionSet.get(pos).getObjective(obj);
				if (value > maximumValues[obj]) {
					maximumValues[obj] = value;
				}
				if (value < minimumValues[obj]) {
					minimumValues[obj] = value;
				}
			}
		}

		Object[] result = computeIndicatorValuesHD(solutionSet, maximumValues, minimumValues);
		for (int pos = 0; pos < solutionSet.size(); pos++) {
			fitness(solutionSet, pos, (ArrayList<List<Double>>)result[0], Double.valueOf(result[1].toString()));
		}

		return result;
	}
	
	/**
	 * This structure store the indicator values of each pair of elements
	 */
	public Object[] computeIndicatorValuesHD(ParetoSolution<Type> solutionSet, double[] maximumValues, double[] minimumValues) {
		ParetoSolution<Type> A, B;
		// Initialize the structures
		ArrayList<List<Double>> indicatorValues_ = new ArrayList<List<Double>>();
		double maxIndicatorValue_ = -Double.MAX_VALUE;

		for (int j = 0; j < solutionSet.size(); j++) {
			A = new ParetoSolution<Type>(1);
			A.add(solutionSet.get(j));

			List<Double> aux = new ArrayList<Double>();
			for (int i = 0; i < solutionSet.size(); i++) {
				B = new ParetoSolution<Type>(1);
				B.add(solutionSet.get(i));

				int flag = (new DominanceComparator()).compare(A.get(0), B.get(0));

				double value = 0.0;
				if (flag == -1) {
					value = -calcHypervolumeIndicator(A.get(0), B.get(0), num_obj, maximumValues, minimumValues);
				} else {
					value = calcHypervolumeIndicator(B.get(0), A.get(0), num_obj, maximumValues, minimumValues);
				}
				//double value = epsilon.epsilon(matrixA,matrixB,problem_.getNumberOfObjectives());

				//Update the max value of the indicator
				if (Math.abs(value) > maxIndicatorValue_) {
					maxIndicatorValue_ = Math.abs(value);
				}
				aux.add(value);
			}
			indicatorValues_.add(aux);
		}
		return new Object[]{indicatorValues_, maxIndicatorValue_};
	}

	double calcHypervolumeIndicator(MOSolutionBase<Type> p_ind_a, MOSolutionBase<Type> p_ind_b, int d, double maximumValues[], double minimumValues[]) {
		double a, b, r, max;
		double volume = 0;

		r = rho * (maximumValues[d - 1] - minimumValues[d - 1]);
		max = minimumValues[d - 1] + r;

		a = p_ind_a.getObjective(d - 1);
		if (p_ind_b == null) {
			b = max;
		} else {
			b = p_ind_b.getObjective(d - 1);
		}

		if (d == 1) {
			if (a < b) {
				volume = (b - a) / r;
			}
			else {
				volume = 0;
			}
		} else {
			if (a < b) {
				volume = calcHypervolumeIndicator(p_ind_a, null, d - 1, maximumValues, minimumValues) * (b - a) / r;
				volume += calcHypervolumeIndicator(p_ind_a, p_ind_b, d - 1, maximumValues, minimumValues) * (max - b) / r;
			}
			else {
				volume = calcHypervolumeIndicator(p_ind_a, p_ind_b, d - 1, maximumValues, minimumValues) * (max - b) / r;
			}
		}

		return (volume);
	}

	/**
	 * Calculate the fitness for the individual at position pos
	 */
	public void fitness(ParetoSolution<Type> solutionSet, int pos, ArrayList<List<Double>> indicatorValues_, double maxIndicatorValue_) {
		double fitness = 0.0;

		for (int i = 0; i < solutionSet.size(); i++) {
			if (i != pos) {
				fitness += Math.exp((-1 * indicatorValues_.get(i).get(pos) / maxIndicatorValue_) / kappa);
			}
		}
		solutionSet.get(pos).setFitness(fitness);
	}
	
	public ParetoSolution<Type> environmentalSelectionSPEA2(ParetoSolution<Type> union, int archiveSize){      
		ParetoSolution<Type> res;
		/*
		 * Racunanje razdalj med osebki. Razdalje so shranjene v spremenljivki distance objekta Spa2Fitness
		 */
		Spea2fitness spea = new Spea2fitness(union);

		/*
		 * Calculating:   strength(moc)
		 *                R(i) - rawFitness(groba uspesnost)
		 *                D(i) - distance (gostota)
		 *                F(i) = R(i) + D(i) - (prava uspesnost)
		 */
		spea.fitnessAssign();

		/*
		 * Ce je nedominiranih osebkov vec kot je velikost novega arhiva, izloci osebke, ki so blizu drugim osebkom (4.3)
		 * Sicer pa, ce nedominirani osebki ne zapolnijo arhiva, arhiv dopolni z najboljsimi dominiranimi osebki iz P(t-1) in A(t-1) (4.4)
		 * Ce je zaustavitveni kritrij izpolnjen, koncaj (4.5)
		 */    
		res = spea.environmentalSelection(archiveSize);

		return res;
	}

	public ParetoSolution<Type> environmentalSelectionNSGAII(Ranking<Type> ranking, Distance<Type> distance, ParetoSolution<Type> front, int remain, int index){
		ParetoSolution<Type> finalPop = new ParetoSolution<Type>(remain);

		// Obtain the next front
		front=ranking.getSubfront(index);

		while ((remain > 0) && (remain >= front.size())){ 
			//Assign crowding distance to individuals
			distance.crowdingDistanceAssignment(front,num_obj);

			for (int k = 0; k < front.size(); k++ ) {
				finalPop.add(front.get(k));
			} // for

			//Decrement remain
			remain = remain - front.size();

			//Obtain the next front
			index++;
			if (remain > 0) {
				front = ranking.getSubfront(index);
			} // if
		}//end while

		// remain is less than front(index).size, insert only the best one
		if (remain > 0) {  // front contains individuals to insert                        
			while (front.size() > remain) {
				distance.crowdingDistanceAssignment(front,num_obj);
				front.remove(front.indexWorst(new CrowdingComparator()));
			}
			for (int k = 0; k < front.size(); k++) {
				finalPop.add(front.get(k));
			}
			remain = 0; 
		} // if    

		return finalPop;    
	}

	@Override
	public void resetToDefaultsBeforeNewRun() {
		
	}

}
