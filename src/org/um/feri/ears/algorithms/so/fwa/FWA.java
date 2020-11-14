package org.um.feri.ears.algorithms.so.fwa;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

public class FWA extends Algorithm{
	
	DoubleSolution bestspark;
	
	int pop_size;
	Task task;
	
	private double numMaxAmplitude;
	private int numGaussianSparks;
	private double numBoundA;
	private double numBoundB;
	private double eps;
	private int numMaxSparks;

	private double [] maxbound;
	private double [] minbound;
	
	private DoubleSolution [] fireworks;
	private DoubleSolution [][] sparks;
	private DoubleSolution [] gaussiansparks;
	
	public FWA()
	{
		this(5,50,0.04,0.8,40,5);
	}
	
	public FWA(int pop_size, int numMaxSparks, double numBoundA, double numBoundB, double numMaxAmplitude, int numGaussianSparks)
	{
		super();
		this.pop_size = pop_size;
		this.numMaxSparks = numMaxSparks;
		this.numBoundA = numBoundA;
		this.numBoundB = numBoundB;
		this.numMaxAmplitude = numMaxAmplitude;
		this.numGaussianSparks = numGaussianSparks;
		
		eps = 1e-38;
		
		au = new Author("miha", "miha.ravber@um.si");
		ai = new AlgorithmInfo("FWA",
				"@inproceedings{tan2010fireworks,"
				+ "title={Fireworks algorithm for optimization},"
				+ "author={Tan, Ying and Zhu, Yuanchun},"
				+ "booktitle={International Conference in Swarm Intelligence},"
				+ "pages={355--364},"
				+ "year={2010},"
				+ "organization={Springer}}",
				"FWA", "Flower Pollination Algorithm");
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, pop_size + "");
	}

	@Override
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
		task = taskProblem;
		initPopulation();
		
		maxbound = task.getUpperLimit();
		minbound = task.getLowerLimit();
		
		while (!task.isStopCriteria()) {
			
			//set off n fireworks
			setoff();
			if(task.isStopCriteria())
				break;
			//select n locations
			selectlocations();
					
			task.incrementNumberOfIterations();
		}
		return bestspark;
	}
	
	//set off n fireworks
	private void setoff() throws StopCriteriaException {

		//get max(worst) and min(best) value
		double maxvalue = fireworks[0].getEval();
		double minvalue = fireworks[0].getEval();
		int i;
		for(i = 1;i < pop_size;i ++) {
			if(fireworks[i].getEval() > maxvalue) {
				maxvalue = fireworks[i].getEval();
			}
			if(fireworks[i].getEval() < minvalue) {
				minvalue = fireworks[i].getEval();
			}
		}
		double summaxdiff = 0.0;
		double summindiff = 0.0;
		for(i = 0;i < pop_size;i ++) {
			summaxdiff += maxvalue - fireworks[i].getEval();
			summindiff += fireworks[i].getEval() - minvalue;
		}

		//get number of sparks for all fireworks
		int [] numSparks = new int [pop_size];
		double tmpcoef;
		for(i = 0;i < pop_size;i ++) {
			tmpcoef = (maxvalue - fireworks[i].getEval() + eps) / (summaxdiff + eps);
			if(tmpcoef < numBoundA) {
				tmpcoef = numBoundA;
			}
			if(tmpcoef > numBoundB) {
				tmpcoef = numBoundB;
			}
			numSparks[i] = (int) (numMaxSparks * tmpcoef);
		}

		//get amplitude of explosion for all fireworks
		double[] ampExplosion = new double [pop_size];
		for(i = 0;i < pop_size;i++) {
			ampExplosion[i] =(fireworks[i].getEval() - minvalue + eps) / (summindiff + eps) * numMaxAmplitude;
		}

		//generate sparks for all fireworks
		sparks = new DoubleSolution [pop_size] [];
		//temporary position
		double [] tmppos = new double[task.getNumberOfDimensions()];
		double [] fireworkpos;
		for(i = 0;i < pop_size;i++) {
			sparks[i] = new DoubleSolution [numSparks[i]];
			fireworkpos = fireworks[i].getDoubleVariables();
			//get all sparks' position
			int k;
			for(k = 0 ;k < numSparks[i];k ++) {
				//sparks[i][k] = new DoubleSolution();
				//select z directions

				boolean [] randflag = new boolean [task.getNumberOfDimensions()];
				int j;
				for(j = 0;j < task.getNumberOfDimensions();j ++) {
					randflag[j] = false;
				}
				int numExplosionDirections = (int) (task.getNumberOfDimensions() * Util.nextDouble());
				int randomcount = 0;
				int tmprand;
				while(randomcount < numExplosionDirections) {
					tmprand = Util.nextInt(task.getNumberOfDimensions());
					if(!randflag[tmprand]) {
						randflag[tmprand] = true;
						randomcount ++;
					}
				}
				//explode
				double displacement = ampExplosion[i] * (Util.nextDouble() - 0.5) * 2;
				for(j = 0 ;j < task.getNumberOfDimensions();j ++) {
					if(randflag[j]) {
						tmppos[j] = fireworkpos[j] + displacement;
						//out of bound
						if(tmppos[j] < minbound[j] || tmppos[j] > maxbound[j]) {
							double abspos = Math.abs(tmppos[j]);
							while(abspos >= 0) {
								abspos -= (maxbound[j] - minbound[j]);
							}
							abspos += (maxbound[j] - minbound[j]);
							tmppos[j] = minbound[j] + abspos;
						}
					}
					else {
						tmppos[j] = fireworkpos[j];
					}
				}
				//set position of the spark
				// Check bounds
				tmppos = task.setFeasible(tmppos);
				// Evaluate new solution
				if(task.isStopCriteria())
					break;
				sparks[i][k] = task.eval(tmppos);
			}
		}

		//gaussian explode
		gaussiansparks = new DoubleSolution [numGaussianSparks];
		int k;
		for(k = 0;k < numGaussianSparks;k ++) {
			//gaussiansparks[k] = new DoubleSolution();
			//randomly select a firework
			i = Math.abs(Util.nextInt()) % pop_size;
			fireworkpos = fireworks[i].getDoubleVariables();
			//select z directions
			boolean [] randflag = new boolean [task.getNumberOfDimensions()];
			int j;
			for(j = 0;j < task.getNumberOfDimensions();j ++) {
				randflag[j] = false;
			}
			int numExplosionDirections = (int) (task.getNumberOfDimensions() * Util.nextDouble());
			int randomcount = 0;
			int tmprand;
			while(randomcount < numExplosionDirections) {
				tmprand = Math.abs(Util.nextInt()) % task.getNumberOfDimensions();
				if(!randflag[tmprand]) {
					randflag[tmprand] = true;
					randomcount ++;
				}
			}
			//explode
			double gaussianCoef = 1.0 + Util.nextGaussian();
			for(j = 0 ;j < task.getNumberOfDimensions();j ++) {
				if(randflag[j]) {
					tmppos[j] = fireworkpos[j] * gaussianCoef;
					//out of bound
					if(tmppos[j] < minbound[j] || tmppos[j] > maxbound[j]) {
						double abspos = Math.abs(tmppos[j]);
						while(abspos >= 0) {
							abspos -= (maxbound[j] - minbound[j]);
						}
						abspos += (maxbound[j] - minbound[j]);
						tmppos[j] = minbound[j] + abspos;
					}
				}
				else {
					tmppos[j] = fireworkpos[j];
				}
			}
			//set position of the spark
			// Check bounds
			tmppos = task.setFeasible(tmppos);
			// Evaluate new solution
			if(task.isStopCriteria())
				break;
			gaussiansparks[k] = task.eval(tmppos);
		}
	}
	
	//select n locations
	private void selectlocations() {
		//select the best location
		int i,j,k;
		for(i = 0;i < pop_size;i ++) {
			if(task.isFirstBetter(fireworks[i], bestspark)) {
				bestspark = fireworks[i];
			}
		}
		for(i = 0;i < pop_size;i ++) {
			for(j = 0;j < sparks[i].length;j ++) {
				if(task.isFirstBetter(sparks[i][j], bestspark)) {
					bestspark = sparks[i][j];
				}
			}
		}
		for(i = 0;i < numGaussianSparks;i ++) {
			if(task.isFirstBetter(gaussiansparks[i], bestspark)) {
				bestspark = gaussiansparks[i];
			}
		}
		//select the rest n-1 locations
		//count the number of fireworks and sparks
		int numFireworksSparks = pop_size + numGaussianSparks;
		for(i = 0;i < pop_size;i ++) {
			for(j = 0;j < sparks[i].length;j ++) {
				numFireworksSparks ++;
			}
		}

		//put all the fireworks and sparks in an array
		double [][] fireworkspos = new double [numFireworksSparks][];
		int idx = 0;
		for(i = 0;i < pop_size;i ++) {
			fireworkspos[idx] = fireworks[i].getDoubleVariables();
			idx ++;
		}
		for(i = 0;i < pop_size;i ++) {
			for(j = 0;j < sparks[i].length;j ++) {
				fireworkspos[idx] = sparks[i][j].getDoubleVariables();
				idx ++;
			}
		}
		for(i = 0;i < numGaussianSparks;i ++) {
			fireworkspos[idx] = gaussiansparks[i].getDoubleVariables();
			idx ++;
		}
		//calculate the selection probability of each location
		double [] selectionprobability = new double [numFireworksSparks];
		double sumprob = 0;
		for(i = 0;i < numFireworksSparks;i ++) {
			selectionprobability[i] = 0;
			for(j = 0;j < numFireworksSparks;j ++) {
				double tmpdis = 0;
				for(k = 0;k < task.getNumberOfDimensions();k ++) {
					tmpdis += (fireworkspos[i][k] - fireworkspos[j][k]) * (fireworkspos[i][k] - fireworkspos[j][k]);
				}
				selectionprobability[i] += Math.sqrt(tmpdis);
			}
			sumprob += selectionprobability[i];
		}
		double [] cumulativeprobability = new double [numFireworksSparks];
		for(i = 0;i < numFireworksSparks;i ++) {
			if(sumprob < eps) {
				selectionprobability[i] = 1.0 / numFireworksSparks;
			}
			else {
				selectionprobability[i] /= sumprob;
			}
			if(i == 0) {
				cumulativeprobability[i] =selectionprobability[i];
			}
			else {
				cumulativeprobability[i] = cumulativeprobability[i - 1] + selectionprobability[i];
			}
		}
		//select n-1 locations according to the selection probability
		int [] nextlocations = new int [pop_size - 1];
		for(k = 0;k < pop_size - 1;k ++) {
			double randpointer = Util.nextDouble();
			for(i = 0;i < numFireworksSparks;i ++) {
				if(randpointer <= cumulativeprobability[i]) {
					break;
				}
			}
			nextlocations[k] = i;
		}
		//set next generations
		DoubleSolution[] nextfireworks = new DoubleSolution [pop_size];
		nextfireworks[pop_size - 1] = bestspark;
		boolean breakflag;
		for(k = 0;k < pop_size - 1;k ++) {
			idx = 0;
			breakflag = false;
			for(i = 0;i < pop_size;i ++) {
				if(idx == nextlocations[k]) {
					nextfireworks[k] = fireworks[i];
					breakflag = true;
					break;
				}
				idx ++;
			}
			if(breakflag) {
				continue;
			}
			for(i = 0;i < pop_size;i ++) {
				for(j = 0;j < sparks[i].length;j ++) {
					if(idx == nextlocations[k]) {
						nextfireworks[k] = sparks[i][j];
						breakflag = true;
						break;
					}
					idx ++;
				}
				if(breakflag) {
					break;
				}
			}
			if(breakflag) {
				continue;
			}
			for(i = 0;i < numGaussianSparks;i ++) {
				if(idx == nextlocations[k]) {
					nextfireworks[k] = gaussiansparks[i];
					breakflag = true;
					break;
				}
				idx ++;
			}
		}
		fireworks = nextfireworks;
	}


	private void initPopulation() throws StopCriteriaException {
		fireworks = new DoubleSolution[pop_size];
		fireworks[0] = task.getRandomSolution();
		bestspark = new DoubleSolution(fireworks[0]);

		for (int i = 1; i < pop_size; i++) {
			if (task.isStopCriteria())
				break;
			DoubleSolution newSolution = task.getRandomSolution();
			fireworks[i] = newSolution;
			if(task.isFirstBetter(newSolution, bestspark))
			{
				bestspark = new DoubleSolution(newSolution);
			}
		}
	}
	

	@Override
	public void resetToDefaultsBeforeNewRun() {
	}

}
