package org.um.feri.ears.benchmark;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.moo.IntegerMOProblem;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.real_world.CITOProblem;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.rating.Game;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.FutureResult;
import org.um.feri.ears.util.Util;

public class CITOBenchmark extends MORatingBenchmark<Integer, IntegerMOTask, IntegerMOProblem>{
    public static final String name="CITO Benchmark";
    private boolean random;
    
	@Override
	public boolean resultEqual(ParetoSolution<Integer> a, ParetoSolution<Integer> b, QualityIndicator<Integer> qi) {
		if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if(qi.getIndicatorType() == IndicatorType.UNARY)
        	return a.isEqual(b, drawLimit);
        else if(qi.getIndicatorType() == IndicatorType.BINARY)
        {
			if(qi.compare(a, b, drawLimit) == 0)
			{
				return true;
			}
        }
        return false;
	}
	
    public CITOBenchmark(){
    	this(null, 0.0000001, true);
    	List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD); //Default indicator
        this.indicators = indicators;
    }
    
    public CITOBenchmark(List<IndicatorName> indicators, double drawLimit, boolean random) {
        super(indicators);
        this.random = random;
        this.drawLimit = drawLimit;
        maxEvaluations=60000;
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+drawLimit);

    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#registerTask(org.um.feri.ears.problems.Problem)
     */
    @Override
    protected void registerTask(EnumStopCriterion sc, int eval, long allowedTime, int maxIterations, double epsilon, IntegerMOProblem p) {
        listOfProblems.add(new IntegerMOTask(sc, eval, allowedTime, maxIterations, epsilon, p));
    }
    
    @Override
    protected void setWinLoseFromResultList(ResultArena arena, IntegerMOTask t) {
    	
    	if(random)
    	{
			AlgorithmRunResult<ParetoSolution<Integer>, MOAlgorithm<IntegerMOTask, Integer>, IntegerMOTask> first;
			AlgorithmRunResult<ParetoSolution<Integer>, MOAlgorithm<IntegerMOTask, Integer>, IntegerMOTask> second;
			QualityIndicator<Integer> qi;
			IndicatorName indicatorName;
    		for (int i=0; i<results.size(); i++) {
    			first = results.get(i);
    			for (int j=i+1; j<results.size(); j++) {
    				second = results.get(j);
    				indicatorName = indicators.get(Util.nextInt(indicators.size()));
    				qi = IndicatorFactory.createIndicator(indicatorName, t.getNumberOfObjectives(), t.getProblemFileName());
    				
    				try {
    					if(qi.getIndicatorType() == IndicatorType.UNARY)
    					{
    						first.getSolution().evaluate(qi);
    						second.getSolution().evaluate(qi);
    					}
					} catch (Exception e) {
						e.printStackTrace();
					}
    				if (resultEqual(first.getSolution(), second.getSolution(), qi)) {
						arena.addGameResult(Game.DRAW, first.getAlgorithm().getID(), second.getAlgorithm().getID(), t.getProblemName(), indicatorName.toString());
					} 
    				else 
    				{
    					if (t.isFirstBetter(first.getSolution(),second.getSolution(), qi))
    					{
    						arena.addGameResult(Game.WIN, first.getAlgorithm().getID(), second.getAlgorithm().getID(), t.getProblemName(), indicatorName.toString());
    					}
    					else
    					{
    						arena.addGameResult(Game.WIN, second.getAlgorithm().getID(), first.getAlgorithm().getID(), t.getProblemName(), indicatorName.toString());
    					}
    				}
        		}
    		}	
    	}
    	else
    		super.setWinLoseFromResultList(arena, t);
    	
    }
    
    @Override
	protected void runOneProblem(IntegerMOTask task, BankOfResults allSingleProblemRunResults) {

    	reset(task);
    	ExecutorService pool = Executors.newFixedThreadPool(5);//Executors.newFixedThreadPool(listOfAlgorithmsPlayers.size());
        Set<Future<FutureResult>> set = new HashSet<Future<FutureResult>>();
        for (MOAlgorithm<IntegerMOTask, Integer> al: listOfAlgorithmsPlayers) {
          Future<FutureResult> future = pool.submit(al.createRunnable(al, new IntegerMOTask(task)));
          set.add(future);
        }

        for (Future<FutureResult> future : set) {
        	try {
        		FutureResult res = future.get();

        		if (printSingleRunDuration) System.out.println("Total execution time for "+ res.algorithm.getAlgorithmInfo().getAcronym()+": "+res.algorithm.getLastRunDuration());
        		//reset(task); //for one eval!
        		if ((MOAlgorithm.getCaching() == Cache.NONE && task.areDimensionsInFeasibleInterval(res.result)) || MOAlgorithm.getCaching() != Cache.NONE) {

        			results.add(new AlgorithmRunResult(res.result, res.algorithm, res.task));
        			allSingleProblemRunResults.add(task, res.result, res.algorithm);
        		}
        		else {
        			System.err.println(res.algorithm.getAlgorithmInfo().getAcronym()+" result "+res.result+" is out of intervals! For task:"+task.getProblemName());
        			results.add(new AlgorithmRunResult(null, res.algorithm, res.task)); // this can be done parallel - asynchrony
        		}
        		
        		//reset(task);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
        }
        
    	pool.shutdown();
        
	}

	/* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#initFullProblemList()
     */
    @Override
    protected void initFullProblemList() {
    	
    	ArrayList<IntegerMOProblem> problems = new ArrayList<IntegerMOProblem>();
   	
//    	problems.add(new CITOProblem(CITOProblem.Problems.OA_AJHotDraw.name()));
    	problems.add(new CITOProblem(CITOProblem.Problems.OA_AJ_HSQLDB.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OA_HealthWatcher.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OA_TollSystems.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OO_BCEL.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OO_JBoss.name()));
//    	problems.add(new CITOProblem(CITOProblem.Problems.OO_JHotDraw.name()));
//		problems.add(new CITOProblem(CITOProblem.Problems.OO_MyBatis.name()));
    	
    	for (IntegerMOProblem moProblem : problems) {
    		registerTask(stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001, moProblem);
		}
    }
        
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getAcronym()
     */
    @Override
    public String getAcronym() {
        return "CITOBenchmark";
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getInfo()
     */
    @Override
    public String getInfo() {
        return "";
    }
}
