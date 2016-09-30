package org.um.feri.ears.benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.rating.Game;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.ParetoSolutionCache;
import org.um.feri.ears.util.Util;

public abstract class MORatingBenchmark<T extends Number, Task extends MOTask<T, P>, P extends MOProblemBase<T>> extends RatingBenchmarkBase<Task, MOAlgorithm<Task, T>, MOAlgorithmEvalResult> {
	
	protected List<IndicatorName> indicators;
    private double indicatorWeights[];
    protected boolean runInParalel = false;
    
	
	/**
	 * This indicator is only used when comparing two approximation sets
	 */
	
	public MORatingBenchmark(List<IndicatorName> indicators){
		super();
		this.indicators = indicators;
	}
	
	public MORatingBenchmark(ArrayList<IndicatorName> indicators, double[] weights) {
		super();
		this.indicators = indicators;
		indicatorWeights = weights;
	}

	public abstract boolean resultEqual(ParetoSolution<T> a, ParetoSolution<T> b, QualityIndicator<T> qi);
    protected abstract void registerTask(EnumStopCriteria sc, int eval, double epsilon, P p);
	
	@Override
	public void registerAlgorithm(MOAlgorithm<Task, T> al) {
        listOfAlgorithmsPlayers.add(al);
    }
    
	protected IndicatorName getRandomIndicator()
	{
		if(indicatorWeights != null)
		{
			double rand = Util.rnd.nextDouble();
			for (int i = 0; i < indicatorWeights.length; i++) {
				if(rand < indicatorWeights[i])
					return indicators.get(i);
			}
			
		}
		return indicators.get(Util.nextInt(indicators.size()));
	}

	class FitnessComparator implements Comparator<MOAlgorithmEvalResult> {
        MOTask<T, P> t;
        QualityIndicator<T> qi;
        public FitnessComparator(MOTask<T, P> t, QualityIndicator<T> qi) {
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
    
	@Override
    protected void setWinLoseFromResultList(ResultArena arena, Task t) {
		
		for (IndicatorName indicatorName : indicators) {

			MOAlgorithmEvalResult win;
			MOAlgorithmEvalResult lose;        
			FitnessComparator fc;
			QualityIndicator<T> qi = IndicatorFactory.<T>createIndicator(indicatorName, t.getNumberOfObjectives(), t.getProblemFileName());
			fc = new FitnessComparator(t, qi);
			Collections.sort(results, fc); //best first
			for (int i=0; i<results.size()-1; i++) {
				win = results.get(i);
				for (int j=i+1; j<results.size(); j++) {
					lose = results.get(j);
					if (resultEqual(win.getBest(), lose.getBest(), qi)) {
						arena.addGameResult(Game.DRAW, win.getAl().getAlgorithmInfo().getVersionAcronym(), lose.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemName(), indicatorName.toString());
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
						arena.addGameResult(Game.WIN, win.getAl().getAlgorithmInfo().getVersionAcronym(), lose.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemName(), indicatorName.toString());
					}

				}
			}
		}
    }
    
    /**
     * Fill all data!
     *  
     * @param arena needs to be filed with players and their ratings
     * @param allSingleProblemRunResults 
     * @param repetition
     */
	@Override
    public void run(ResultArena arena, BankOfResults allSingleProblemRunResults, int repetition) {
        duelNumber = repetition;
        parameters.put(EnumBenchmarkInfoParameters.NUMBER_OF_DEULS, ""+repetition);
        for (Task t:listOfProblems) {
        	System.out.println("Current problem: "+t.getProblemName());
            for (int i=0; i<repetition; i++) {
            	System.out.println("Current repetition: "+ (i+1));
                runOneProblem(t,allSingleProblemRunResults);
                setWinLoseFromResultList(arena,t);
                results.clear();
            }
        }
        
        if(isCheating())
        {
        	System.out.println("The reset count does not match!");
        }
        
    }

	@Override
	protected void runOneProblem(Task task, BankOfResults allSingleProblemRunResults) {

			long start=0;
			long duration=0;
			for (MOAlgorithm<Task, T> al: listOfAlgorithmsPlayers) {

				reset(task); //number of evaluations  
				try {
					start = System.currentTimeMillis();
					
					GraphDataRecorder.SetContext(al,task);
					
					ParetoSolution<T> bestByALg = al.execute(task); //check if result is fake!
					
	                GraphDataRecorder.SetParetoSolution(bestByALg);

					duration = System.currentTimeMillis()-start;
					if (printSingleRunDuration) {
						System.out.println(al.getID()+": "+(duration/1000));
					}
					al.addRunDuration(duration);

					reset(task); //for one eval!
					if ((MOAlgorithm.getCaching() == Cache.None && task.areDimensionsInFeasableInterval(bestByALg)) || MOAlgorithm.getCaching() != Cache.None) {

						results.add(new MOAlgorithmEvalResult(bestByALg, al)); 
						allSingleProblemRunResults.add(task, bestByALg, al);
					}
					else {
						System.err.println(al.getAlgorithmInfo().getVersionAcronym()+" result "+bestByALg+" is out of intervals! For task:"+task.getProblemName());
						results.add(new MOAlgorithmEvalResult(null, al)); // this can be done parallel - asynchrony                    
					}
				} catch (StopCriteriaException e) {
					System.err.println(al.getAlgorithmInfo().getVersionAcronym()+" StopCriteriaException for:"+task+"\n"+e);
					results.add(new MOAlgorithmEvalResult(null, al));
				}
			}

	}
	
}
