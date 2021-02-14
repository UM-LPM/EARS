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
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriterion;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem1;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem10;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem2;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem3;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem4;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem5;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem6;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem7;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem8;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem9;
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

public class BIOMABenchmark extends MORatingBenchmark<Double, DoubleMOTask, DoubleMOProblem>{
    public static final String name="BIOMA Benchmark";
    private boolean random;
    
	@Override
	public boolean resultEqual(ParetoSolution<Double> a, ParetoSolution<Double> b, QualityIndicator<Double> qi) {
		if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if(qi.getIndicatorType() == IndicatorType.UNARY)
        	return a.isEqual(b, drawLimit); //TODO Quality indicator get eps
        else if(qi.getIndicatorType() == IndicatorType.BINARY)
        {
			if(qi.compare(a, b, drawLimit) == 0)
			{
				return true;
			}
        }
        return false;
	}
	
    public BIOMABenchmark(){
    	this(null, 0.0000001, true);
    	List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD); //Default indicator
        this.indicators = indicators;
    }
    
    public BIOMABenchmark(List<IndicatorName> indicators, double draw_limit, boolean random) {
        super(indicators);
        this.random = random;
        this.drawLimit = draw_limit;
        maxEvaluations=300000;
        stopCriterion = EnumStopCriterion.EVALUATIONS;
        maxIterations = 500;
        timeLimit = 5000; //millisecnods
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);
    }
    
    
    @Override
    protected void setWinLoseFromResultList(ResultArena arena, DoubleMOTask t) {

    	if(random)
    	{
			AlgorithmRunResult<ParetoSolution<Double>, MOAlgorithm<DoubleMOTask, Double>, DoubleMOTask> first;
			AlgorithmRunResult<ParetoSolution<Double>, MOAlgorithm<DoubleMOTask, Double>, DoubleMOTask> second;
			QualityIndicator<Double> qi;
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
	protected void runOneProblem(DoubleMOTask task, BankOfResults allSingleProblemRunResults) {
    	
    	//super.runOneProblem(task, allSingleProblemRunResults);
    	reset(task);
    	ExecutorService pool = Executors.newFixedThreadPool(listOfAlgorithmsPlayers.size());
        Set<Future<FutureResult>> set = new HashSet<Future<FutureResult>>();
        for (MOAlgorithm<DoubleMOTask, Double> al: listOfAlgorithmsPlayers) {
          Future<FutureResult> future = pool.submit(al.createRunnable(al, new DoubleMOTask(task)));
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
     * @see org.um.feri.ears.benchmark.RatingBenchmark#registerTask(org.um.feri.ears.problems.Problem)
     */
    @Override
    protected void registerTask(EnumStopCriterion sc, int eval, long allowedTime, int maxIterations, double epsilon, DoubleMOProblem p) {
        listOfProblems.add(new DoubleMOTask(sc, eval, allowedTime, maxIterations, epsilon, p));
    }
    
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#initFullProblemList()
     */
    @Override
    protected void initFullProblemList() {
    	
    	ArrayList<DoubleMOProblem> problems = new ArrayList<DoubleMOProblem>();
    	
    	
    	problems.add(new UnconstrainedProblem1());
    	problems.add(new UnconstrainedProblem2());
    	problems.add(new UnconstrainedProblem3());
    	problems.add(new UnconstrainedProblem4());
    	problems.add(new UnconstrainedProblem5());
    	problems.add(new UnconstrainedProblem6());
    	problems.add(new UnconstrainedProblem7());
    	problems.add(new UnconstrainedProblem8());
    	problems.add(new UnconstrainedProblem9());
    	problems.add(new UnconstrainedProblem10());

    	
    	for (DoubleMOProblem moProblem : problems) {
    		registerTask(stopCriterion, maxEvaluations, timeLimit, maxIterations,  1.0E-4, moProblem);
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
        return "BIOMA";
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getInfo()
     */
    @Override
    public String getInfo() {
        return "";
    }
}
