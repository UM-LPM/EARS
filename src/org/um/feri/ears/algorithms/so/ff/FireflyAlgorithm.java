package org.um.feri.ears.algorithms.so.ff;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.TaskComparator;
import org.um.feri.ears.util.Util;

public class FireflyAlgorithm extends Algorithm { //needs to me extended 
	//private DoubleSolution i; //EARS Individual includes solution vector and its fitness value
	private boolean debug = true;
	private Task task;
	FireflySolution best;
	ArrayList<FireflySolution> population;
	
	private double alpha = 0.5;		// alpha parameter. copy value from C++ and Matlab version
	private double betamin = 0.2;   // beta parameter. copy value from C++ and Matlab version
	private double gamma = 1.0;		// gamma parameter. copy value from C++ and Matlab version

	private int popSize;
	
    double [] ub;
    double [] lb;
	
	public FireflyAlgorithm(int popSize, int dimension) {
		
		super();
		setDebug(debug);  //EARS prints some debug info
		ai = new AlgorithmInfo("FA",
				"@article{fister2013,"
				+ "title={A comprehensive review of firefly algorithms},"
				+ "author={Fister, Iztok, Fister, Iztok Jr, Yang, X.S. and Brest Janez},"
				+ "journal={Swarm and Evolutionary Computation},"
				+ "volume={13},"
				+ "pages={34--46},"
				+ "year={2013},"
				+ "publisher={Elsevier}}",
				"FA", "Firefly algorithm");
		au =  new Author("alex", "shliu@mail.fresnostate.edu"); //EARS author info
		
		ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
		this.popSize = popSize;
	}
	
	public FireflyAlgorithm() { 
		this(20, 10);	
		//default value from C++ code of Fister. 1000 is dimension and 20 is pop_size
	}
	


	@Override  
	public DoubleSolution execute(Task taskProblem) throws StopCriteriaException{ //EARS main evaluation loop 
		task = taskProblem;
		ub = task.getUpperLimit();
		lb = task.getLowerLimit();
		//dimension = task.getNumberOfDimensions();
		
		initPopulation();
		sort_ffa(); // initial sort
		
		while (!task.isStopCriteria()) {
			
			alpha = alpha_new(alpha, task.getMaxIterations());
			move_ffa();
			//evaluate new solution
			update_eval();
			sort_ffa();
			// deep copy 
			best = new FireflySolution(population.get(0));
			System.out.println(best.getEval());
			task.incrementNumberOfIterations();
		}
		return best;

	}

	@Override
	public void resetToDefaultsBeforeNewRun() {
	}
	
	public void initPopulation() throws StopCriteriaException {
		population = new ArrayList<FireflySolution>();
		FireflySolution firefly = new FireflySolution(task.getRandomSolution());
		firefly.setAttractiveness(1.0);
		firefly.setIntensity(1.0);
		population.add(firefly);
		best = firefly;
		for (int i = 0; i < popSize - 1; i++) {
			FireflySolution newFirefly = new FireflySolution(task.getRandomSolution());
			newFirefly.setAttractiveness(1.0);
			newFirefly.setIntensity(1.0);
			population.add(newFirefly);
			if(task.isFirstBetter(newFirefly, best))
				best = new FireflySolution(newFirefly);
			if (task.isStopCriteria())
				break;
		}
	}
	
	// optionally recalculate the new alpha value
	public double alpha_new(double alpha, int NGen)
	{
	    double delta;			// delta parameter
	    delta = 1.0-Math.pow((Math.pow(10.0, -4.0)/0.9), 1.0/(double) NGen);
	    return (1-delta)*alpha;
	}
	
	//not available in C++ version
	public void update_eval() throws StopCriteriaException
	{
		for (int i = 0; i < popSize; i++)
		{
			if (task.isStopCriteria())
				break;
			FireflySolution fa = population.get(i);
			//fa.setIntensity();
			//fa.setAttractiveness();
			//the following invocation does not set newFirefly itself upper and lower limits
            FireflySolution newFirefly = new FireflySolution(task.eval(fa));
            population.set(i, newFirefly);
		}
		
	}
	
	// implementation of bubble sort and replace_ffa
	public void sort_ffa()
	{
	    int i, j;
	    
	    // initialization of indexes
	    for(i=0; i< popSize; i++)
	    	population.get(i).setIndex(i);
	    
	    // sort the population by fitness value
		population.sort(new TaskComparator(task));
	    
	    // Bubble sort. needs to improve to a better sorting algorithm
	    /*for(i=0;i<pop_size-1;i++)
	    {
	        for(j=i+1;j<pop_size;j++)
	        {
	        	//if Ii > Ij
	            if(population.get(i).getIntensity() > population.get(j).getIntensity())
	            {  
	            	FireflySolution temp = population.get(i);
	            	population.set(i, population.get(j));
	            	population.set(j, temp);
	            }
	        }
	    }*/
	}
	
	public void move_ffa()
	{
	    int i, j, k;
	    double scale;
	    double r, beta;
	    
	    for(i=0; i< popSize; i++)
	    {
	    
	        for(j=0; j< popSize; j++)
	        {
	            double distance = 0.0; //it was called r in C++ version
	            double [] ffa_i = population.get(i).getDoubleVariables();
	            double [] ffa_j = population.get(j).getDoubleVariables();
	            for(k=0;k<task.getNumberOfDimensions();k++)
	            {
	                //r += (ffa[i][k]-ffa[j][k])*(ffa[i][k]-ffa[j][k]);
	            	distance += ((ffa_i[k] - ffa_j[k])*(ffa_i[k] - ffa_j[k]));
	            }
	            distance = Math.sqrt(distance);
	            //alex: from the paper, it is be low intensity moves to high intensity. 
	            //because problems are "finding minimum" so lower intensity means *high* fitness.
	            //hence, it is correct to use Ii > Ij and move i to j 
	            //(since i has higher intensity (means lower fitness) i is the one moving to j.
	            //if(population.get(i).getIntensity() > population.get(j).getIntensity())
	            if(population.get(i).getEval() > population.get(j).getEval()) // changed to getEval
	            {
	                double beta0 = 1.0;
	                beta = (beta0-betamin)*Math.exp(-gamma*Math.pow(distance, 2.0))+betamin;
	                for(k=0;k<task.getNumberOfDimensions();k++)
	                {
	                    //alex: based on Yang's matlab code r is generated for each dimension
	                	scale = Math.abs(ub[k] - lb[k]); //alex: C++ version has scale bug!
	                	r = Util.nextDouble(lb[k], ub[k]);
	                    double tmpf = alpha*(r-0.5)*scale;
	                    //the formula below is C++ version
	                    //ffa[i][k] = ffa[i][k]*(1.0-beta)+ffa_tmp[j][k]*beta+tmpf;
	                    //alex: I think the above ffa_tmp is a bug -- different from matlab version
	                    //matlab's nso (ffa_tmp in C++) is from ns, which is updated result of ffa.
	                    ffa_i[k] = ffa_i[k]*(1.0-beta) + ffa_j[k]*beta + tmpf;
	                }
	                ffa_i = task.setFeasible(ffa_i); //check scope out of bound or not
                    population.get(i).setVariables(Arrays.asList(ArrayUtils.toObject(ffa_i))); //update solution i
	             
	            }
	        }
	    }
	}
}