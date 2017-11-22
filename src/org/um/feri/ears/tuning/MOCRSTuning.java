package org.um.feri.ears.tuning;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.benchmark.MOAlgorithmEvalResult;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.real_world.CITOProblem;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.rating.Game;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.FutureResult;
import org.um.feri.ears.util.Util;

public class MOCRSTuning {

	CRSSolution[] population;
	int numOfRuns = 8;
	int pop_size = 20; //30 for DE
	
	int D; //number of dimensions

    /* --------- jDE constants ----------- */
    final static double Finit = 0.5; // F INITIAL FACTOR VALUE
    final static double CRinit = 0.9; // CR INITIAL FACTOR VALUE
    
    final static double Fl = 0.1; // F in the range [0.1, 1.0]
    final static double Fu = 0.9; //

    final static double CRl = 0.0; // CR in the range [0.0, 1.0]
    final static double CRu = 1.0; //

    final static double tao1 = 0.1; // probability to adjust F
    final static double tao2 = 0.1; // probability to adjust CR
	
    double F, memF, CR, memCR; /* control variables of DE */
    double tmpF, tmpCR, tmp3;
    
    double tmp[];
    
    CRSSolution pold[]; // double pold[MAXPOP][MAXDIM]
    CRSSolution pnew[];
    CRSSolution pswap[];
    
    int r1, r2, r3; /* placeholders for random indexes */
    
	int num_eval = 0;
	int max_eval = 1000;
	
	CRSSolution best;
	CRSSolution bestit;
	
	Class<? extends AlgorithmBase> classAlg;
	String algName;
	ArrayList<ControlParameter> controlParameters;
	
	ArrayList<IntegerMOTask> tasks = new ArrayList<IntegerMOTask>();
	
	double draw_limit = 1e-7;
	
	
	public void tune(Class<? extends AlgorithmBase> classAlg, String algName, ArrayList<ControlParameter> controlParameters) {
		
		this.classAlg = classAlg;
		this.algName = algName;
		this.controlParameters = controlParameters;
		
		//Player algorithm = new Player("IBEA", new Rating(1500, 350, 0.06),0,0,0);
		
		tasks.add(new IntegerMOTask(EnumStopCriteria.EVALUATIONS, 300000, 5000, 3000, 1.0E-4, new CITOProblem("OA_AJHsqldb")));
		tasks.add(new IntegerMOTask(EnumStopCriteria.EVALUATIONS, 300000, 5000, 3000, 1.0E-4, new CITOProblem("OO_BCEL")));
		tasks.add(new IntegerMOTask(EnumStopCriteria.EVALUATIONS, 300000, 5000, 3000, 1.0E-4, new CITOProblem("OO_MyBatis")));

		execute();
		//remove significantly worse and replace with new ones?
		
		//JDE_rand_1_bin
	}
	
	private void execute() {
		
		init();
		
		pold = population; // old population (generation G)
        pnew = new CRSSolution[pop_size]; // new population (generation G+1)
        tmp = new double[D];
		
        if(pop_size < 4) {
        	System.err.println("Population must contain at least 4 members");
        	return;
        }
        int gen = 1;
		while(num_eval < max_eval){
			
            System.out.println("Current generation: "+gen);
            System.out.println("Evaulations used: "+num_eval+"/"+max_eval);
			
            for (int i = 0; i < pop_size; i++) /* Start of loop through ensemble */
            {
                if (num_eval >= max_eval)
                    break;
			
	            do {
	                r1 = Util.rnd.nextInt(pop_size); // (int) (Util.rnd.nextDouble() * NP);
	            } while (r1 == i);
	
	            do {
	                r2 = Util.rnd.nextInt(pop_size); // (int) (Util.rnd.nextDouble() * NP);
	            } while ((r2 == i) || (r2 == r1));
	
	            do {
	                r3 = Util.rnd.nextInt(pop_size); // (int) (Util.rnd.nextDouble() * NP);
	            } while ((r3 == i) || (r3 == r1) || (r3 == r2));
			
			
	            assignd(D, tmp, pold[i].params);
	            tmpF = pold[i].getF();
	            tmpCR = pold[i].getCR();
	            int n = (int) (Util.rnd.nextDouble() * D);
	            // SELF-ADAPTATION OF CONTROL PARAMETERS
	            if (Util.rnd.nextDouble() < tao1) { // F
	                F = Fl + Util.rnd.nextDouble() * Fu;
	                tmpF = F;
	            } else
	                F = tmpF;
	            if (Util.rnd.nextDouble() < tao2) { // CR
	                CR = CRl + Util.rnd.nextDouble() * CRu;
	                tmpCR = CR;
	            } else
	                CR = tmpCR;
	
	            for (int L = 0; L < D; L++) // perform D binomial trials 
	            {
	                if ((Util.rnd.nextDouble() < CR) || L == (D - 1)) {
	                    tmp[n] = pold[r1].params[n] + F * (pold[r2].params[n] - pold[r3].params[n]);
	                }
	                n = (n + 1) % D;
	            }
            
            
	            for (int kk = 0; kk < D; kk++) {
	                tmp[kk] = setFeasible(tmp[kk], kk);
	            }
	            
	            CRSSolution br = evaluate(tmp, pold, i);
	            CRSSolution trial_cost = new CRSSolution(br, tmpF, tmpCR);
	            if (isFirstBetter(trial_cost, pold[i])) {
	                pnew[i] = trial_cost;
	                if (isFirstBetter(trial_cost, best)) {
	                    best = new CRSSolution(trial_cost);
	                    System.out.println("New best solution:");
	                    System.out.println(best.name);
	                    System.out.println("Rating: "+best.p.getRatingData().getRating());
	                }
	
	            } else {
	                pnew[i] = new CRSSolution(pold[i]);
	            }
            
            }/* End mutation loop through pop. */

            bestit = new CRSSolution(best);
            pswap = pold;
            pold = pnew;
            pnew = pswap;
            gen++;
		}
		
        System.out.println("Best solution found:");
        System.out.println(best.name);
        System.out.println("Rating: "+best.p.getRatingData().getRating());
	}
	
    private CRSSolution evaluate(double[] newValue, CRSSolution[] pop, int index) {
     	
    	String newName = algName+"_";
    	HashMap<String, Double> params = new HashMap<String, Double>();
    	for (int i = 0; i < controlParameters.size(); i++){
    		ControlParameter cp = controlParameters.get(i);
     		newName += controlParameters.get(i).name+"("+newValue[i]+")";
    		params.put(cp.name, newValue[i]);
    	}
    	CRSSolution newSolution = createSoluton(newName, params, newValue);
    	createPlayers(pop, newSolution, index);
		return newSolution;
	}

	private boolean isFirstBetter(CRSSolution first, CRSSolution second) {
		
		return first.p.getRatingData().getRating() > second.p.getRatingData().getRating();
	}

	private double setFeasible(double value, int paramIndex) {
		
    	ControlParameter cp = controlParameters.get(paramIndex);
    	return cp.correctValue(value);
	}

	void assignd(int D, double a[], double b[]) {
        System.arraycopy(b, 0, a, 0, D);
        // System.arraycopy(src, srcPos, dest, destPos, length)
    }

	private void init() {
		
		population = new CRSSolution[pop_size];
		D = controlParameters.size();
		for (int i = 0; i < pop_size; i++) {

			String newName = algName+"_";
        	HashMap<String, Double> params = new HashMap<String, Double>();
        	double[] solParams = new double[controlParameters.size()];
        	int ind = 0;
        	for (ControlParameter cp : controlParameters){
        		double value = cp.randomValue();
         		newName+=cp.name+"("+value+")";
        		params.put(cp.name, value);
        		solParams[ind++] = value;
        	}
        	
        	CRSSolution newSolution = createSoluton(newName, params, solParams);   	
        	//add to population
        	population[i] = newSolution; 
			
		}
		createPlayers(population, null, -1);
		
		//save best solution
		CRSSolution currentBest = population[0];
		for (int i = 1; i < population.length; i++) {
			if(population[i].p.getRatingData().getRating() > currentBest.p.getRatingData().getRating()){
				currentBest = population[i];
			}
		}
		best = new CRSSolution(currentBest);
		bestit = new CRSSolution(currentBest);
	}
	
	
	private CRSSolution createSoluton(String name, HashMap<String, Double> params, double[] solParams) {
		  	
    	//fill player game list       	
    	CRSSolution newSolution = fillPlayerGameList(name, params);
    	
    	newSolution.params = solParams;
    	newSolution.name = name;
    	//set initial CR and F
    	newSolution.setCR(CRinit);
    	newSolution.setF(Finit);
    	
    	return newSolution;
	}
	
	private MOAlgorithm createObject(String name) {
    	//create constructor
    	Constructor<?> ctor = null;
		try {
			//classAlg = Class.forName(classAlg);
			ctor = classAlg.getConstructor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Create new algorithm instance
    	MOAlgorithm algorithmObject = null;
    	try {
    		algorithmObject = (MOAlgorithm) ctor.newInstance();
    	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
    			| InvocationTargetException e) {
    		e.printStackTrace();
    	}

    	//Set algorithm name
    	AlgorithmInfo ai = algorithmObject.getAlgorithmInfo();
    	ai.setVersionAcronym(name);
    	ai.setPublishedAcronym(name);
    	ai.setVersionDescription(name);
    	algorithmObject.setAlgorithmInfo(ai);
    	
    	return algorithmObject;
	}

	private void setParameters(Object object, HashMap<String, Double> params) {

		Class<?> current = object.getClass();
		while(current.getSuperclass()!=null){ // we don't want to process Object.class
			if(current.getSimpleName().equals("MOAlgorithm") || current.getSimpleName().equals("Algorithm")){
				return;
			}
			Field[] fields = current.getDeclaredFields();
			if (fields != null && fields.length > 0) {
				for (Field field : fields) {
					if(!field.getType().isPrimitive()){
						try {
							field.setAccessible(true);
							Object objectMember = field.get(object);
							if(objectMember != null){
								setParameters(objectMember, params);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						for (Entry<String, Double> cursor : params.entrySet()) {
							if(cursor.getKey().equals(field.getName())){
								field.setAccessible(true);
								try {
									if(field.getType() == int.class || field.getType() == Integer.class)
										field.setInt(object, cursor.getValue().intValue());
									else
										field.setDouble(object, cursor.getValue());
								} catch (IllegalArgumentException | IllegalAccessException e) {
									e.printStackTrace();
								}
								break;
							}
						}
					}
				}
			}
			current = current.getSuperclass();
		}
	}

	private CRSSolution fillPlayerGameList(String name, HashMap<String, Double> params) {
		
		//create default object
		MOAlgorithm defaultObject = createObject(name);
    	setParameters(defaultObject, params);

		System.out.println("Fill player game list for: "+name);
		int threads = Runtime.getRuntime().availableProcessors();
		CRSSolution sol = new CRSSolution();
		for(IntegerMOTask task : tasks){
			task.resetCounter();
			System.out.println("Current task: "+task.getProblemName());
			try {
				ExecutorService service = Executors.newFixedThreadPool(threads);

				Set<Future<FutureResult<IntegerMOTask, Integer>>> set = new HashSet<Future<FutureResult<IntegerMOTask, Integer>>>();
				for (int i = 0; i < numOfRuns; i++) {
					
					//create new object for each thread
					MOAlgorithm object = createObject(name);
			    	setParameters(object, params);
					
					Future<FutureResult<IntegerMOTask, Integer>> future = service.submit(object.createRunnable(object, new IntegerMOTask(task)));
					set.add(future);
				}

				for (Future<FutureResult<IntegerMOTask, Integer>> future : set) {

					FutureResult<IntegerMOTask, Integer> res = future.get();

					sol.allGamesPlayed.add(new MOAlgorithmEvalResult(res.result, defaultObject));
				}

				/*for (int i = 0; i < numOfRuns; i++) {
					try {
						ParetoSolution bestByALg;
						bestByALg = object.execute(task);
						sol.allGamesPlayed.add(new MOAlgorithmEvalResult(bestByALg, object));
					} catch (StopCriteriaException e) {
						e.printStackTrace();
					}
				}*/

				service.shutdown();
				service.awaitTermination(10, TimeUnit.HOURS);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return sol;
	}
	
	// Creates evaluated player for provided solution, if you want to create players for the whole population
	// set solution to null and inedx to -1
	private void createPlayers(CRSSolution[] population, CRSSolution newSolution, int solIndex){
		
		if(newSolution != null)
			System.out.println("Creating player for: "+newSolution.name);
		
		ResultArena arena = new ResultArena(100);
    	//add players to the arena
		for(int i = 0; i < population.length; i++){
			
			if(i == solIndex)
				arena.addPlayer(new Player(newSolution.name, new Rating(1500, 350, 0.06),0,0,0));
			else
				arena.addPlayer(new Player(population[i].name, new Rating(1500, 350, 0.06),0,0,0));
		}

		int index = 0;
		for(IntegerMOTask task : tasks){

			for (int k = 0; k < numOfRuns; k++) {

				ArrayList<MOAlgorithmEvalResult> sameGameResults = new ArrayList<MOAlgorithmEvalResult>(); 
				for(int i = 0; i < population.length; i++){
					if(i == solIndex)
						sameGameResults.add(newSolution.allGamesPlayed.get(index));
					else
						sameGameResults.add(population[i].allGamesPlayed.get(index));
				}

				FitnessComparator fc;
				QualityIndicator qi = IndicatorFactory.createIndicator(IndicatorName.IGD, task.getNumberOfObjectives(), task.getProblemFileName());
				fc = new FitnessComparator(task, qi);
				
				MOAlgorithmEvalResult win;
				MOAlgorithmEvalResult lose;
				Collections.sort(sameGameResults, fc); //best first
				for (int i=0; i < sameGameResults.size()-1; i++) {
					win = sameGameResults.get(i);
					for (int j=i+1; j<sameGameResults.size(); j++) {
						lose = sameGameResults.get(j);
						if (resultEqual(win.getBest(), lose.getBest(), qi)) {
							arena.addGameResult(Game.DRAW, win.getAl().getAlgorithmInfo().getVersionAcronym(), lose.getAl().getAlgorithmInfo().getVersionAcronym(), task.getProblemName(), qi.getName());
						} else {
							if (win.getAl()==null) {
								System.out.println("NULL ID "+win.getClass().getName());
							}
							if (win.getBest()==null) {
								System.out.println(win.getAl().getID()+" NULL");
							}                    
							if (lose.getAl()==null) {
								System.out.println("NULL ID "+lose.getClass().getName());
							}
							if (lose.getBest()==null) {
								System.out.println(lose.getAl().getID()+" NULL");
							}                     
							arena.addGameResult(Game.WIN, win.getAl().getAlgorithmInfo().getVersionAcronym(), lose.getAl().getAlgorithmInfo().getVersionAcronym(), task.getProblemName(), qi.getName());
						}
					}
				}
				
				index++;
			}
		}
		
		arena.calculteRatings();

		//set players

		if(solIndex == -1) { //update all players
			for(CRSSolution sol : population){
				num_eval++;
				sol.p = arena.getPlayer(sol.name);
			}

		} else {
			newSolution.p = arena.getPlayer(newSolution.name);
			num_eval++;
		}
	}
	
	public boolean resultEqual(ParetoSolution<Integer> a, ParetoSolution<Integer> b, QualityIndicator<Integer> qi) {
		if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if(qi.getIndicatorType() == IndicatorType.Unary)
        	return a.isEqual(b,draw_limit); 
        else if(qi.getIndicatorType() == IndicatorType.Binary)
        {
			if(qi.compare(a, b, draw_limit) == 0)
			{
				return true;
			}
        }
        return false;
	}
	
	class FitnessComparator implements Comparator<MOAlgorithmEvalResult> {
		IntegerMOTask t;
        QualityIndicator qi;
        public FitnessComparator(IntegerMOTask t, QualityIndicator qi) {
            this.t = t;
            this.qi = qi;
        }
        @Override
        public int compare(MOAlgorithmEvalResult arg0, MOAlgorithmEvalResult arg1) {
            if (arg0.getBest()!=null) {
                if (arg1.getBest()!=null){
                   // if (resultEqual(arg0.getBest(), arg1.getBest())) return 0; Normal sor later!
                	if(qi.getIndicatorType() == IndicatorType.Unary)
                	{
                		try {
							arg0.getBest().evaluate(qi);
							arg1.getBest().evaluate(qi);
						} catch (Exception e) {
							e.printStackTrace();
						}
                	}
                    try {
						if (t.isFirstBetter(arg0.getBest(),arg1.getBest(), qi)) return -1;
						else return 1;
					} catch (Exception e) {
						e.printStackTrace();
					}
                } else return -1; //second is null
            } else
                if (arg1.getBest()!= null) return 1; //first null
            return 0; //both equal
        }
    }
}
