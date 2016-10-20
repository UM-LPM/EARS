/**
 * Rating benchmark for unconstrained problems, small dimensions, evaluation is limited with maximum evaluations.
 * Results that are E-10 different are treated as same.
 * <p>
 * 
 * @author Matej Crepinsek
 * @version 1
 * 
 *          <h3>License</h3>
 * 
 *          Copyright (c) 2011 by Matej Crepinsek. <br>
 *          All rights reserved. <br>
 * 
 *          <p>
 *          Redistribution and use in source and binary forms, with or without
 *          modification, are permitted provided that the following conditions
 *          are met:
 *          <ul>
 *          <li>Redistributions of source code must retain the above copyright
 *          notice, this list of conditions and the following disclaimer.
 *          <li>Redistributions in binary form must reproduce the above
 *          copyright notice, this list of conditions and the following
 *          disclaimer in the documentation and/or other materials provided with
 *          the distribution.
 *          <li>Neither the name of the copyright owners, their employers, nor
 *          the names of its contributors may be used to endorse or promote
 *          products derived from this software without specific prior written
 *          permission.
 *          </ul>
 *          <p>
 *          THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *          "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *          LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *          FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *          COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *          INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *          BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *          LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *          CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *          LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *          ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *          POSSIBILITY OF SUCH DAMAGE.
 * 
 */
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
import org.um.feri.ears.problems.EnumStopCriteria;
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

public class RatingEnsemble extends MORatingBenchmark<Double, DoubleMOTask, DoubleMOProblem>{
    public static final String name="Rating Ensemble";
    protected int evaluationsOnDimension;
    private double draw_limit=0.0;
    private boolean random;
    
	@Override
	public boolean resultEqual(ParetoSolution<Double> a, ParetoSolution<Double> b, QualityIndicator<Double> qi) {
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
    
    public RatingEnsemble(List<IndicatorName> indicators, double draw_limit, boolean random, boolean runInParalel) {
        super(indicators);
        this.random = random;
        this.runInParalel = runInParalel;
        this.draw_limit = draw_limit;
        evaluationsOnDimension=300000;
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(evaluationsOnDimension));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);

    }
    public RatingEnsemble(ArrayList<IndicatorName> indicators, double[] weights, double draw_limit, boolean random, boolean runInParalel) {
        super(indicators, weights);
        this.random = random;
        this.runInParalel = runInParalel;
        this.draw_limit = draw_limit;
        evaluationsOnDimension=300000;
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(evaluationsOnDimension));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);
	}

	/* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#registerTask(org.um.feri.ears.problems.Problem)
     */
    @Override
    protected void registerTask(EnumStopCriteria sc, int eval, double epsilon, DoubleMOProblem p) {
        listOfProblems.add(new DoubleMOTask(sc, eval, epsilon, p));
    }
    
    @Override
    protected void setWinLoseFromResultList(ResultArena arena, DoubleMOTask t) {

    	if(random)
    	{
    		MOAlgorithmEvalResult first;
			MOAlgorithmEvalResult second;
			QualityIndicator<Double> qi;
			IndicatorName indicatorName;
    		for (int i=0; i<results.size(); i++) {
    			first = results.get(i);
    			for (int j=i+1; j<results.size(); j++) {
    				second = results.get(j);
    				indicatorName = getRandomIndicator();
    				qi = IndicatorFactory.createIndicator(indicatorName, t.getNumberOfObjectives(), t.getProblemFileName());
    				
    				try {
    					if(qi.getIndicatorType() == IndicatorType.Unary)
    					{
    						first.getBest().evaluate(qi, true); //TODO paralel
    						second.getBest().evaluate(qi, true);
    					}
					} catch (Exception e) {
						e.printStackTrace();
					}
    				if (resultEqual(first.getBest(), second.getBest(), qi)) { 
						arena.addGameResult(Game.DRAW, first.getAl().getAlgorithmInfo().getVersionAcronym(), second.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemName(), indicatorName.toString());
					} 
    				else 
    				{
    					if (t.isFirstBetter(first.getBest(),second.getBest(), qi))
    					{
    						arena.addGameResult(Game.WIN, first.getAl().getAlgorithmInfo().getVersionAcronym(), second.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemName(), indicatorName.toString());
    					}
    					else
    					{
    						arena.addGameResult(Game.WIN, second.getAl().getAlgorithmInfo().getVersionAcronym(), first.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemName(), indicatorName.toString());
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

    	if(MOAlgorithm.getCaching() == Cache.None && runInParalel)
    	{
    		reset(task);
    		ExecutorService pool = Executors.newFixedThreadPool(listOfAlgorithmsPlayers.size());
    		Set<Future<FutureResult<DoubleMOTask, Double>>> set = new HashSet<Future<FutureResult<DoubleMOTask, Double>>>();
    		for (MOAlgorithm<DoubleMOTask, Double> al: listOfAlgorithmsPlayers) {
    			Future<FutureResult<DoubleMOTask, Double>> future = pool.submit(al.createRunnable(al, new DoubleMOTask(task)));
    			set.add(future);
    		}

    		for (Future<FutureResult<DoubleMOTask, Double>> future : set) {
    			try {
    				FutureResult<DoubleMOTask, Double> res = future.get();

    				if (printSingleRunDuration) System.out.println("Total execution time for "+ res.algorithm.getAlgorithmInfo().getVersionAcronym()+": "+res.algorithm.getLastRunDuration());
    				//reset(task); //for one eval!
    				if ((MOAlgorithm.getCaching() == Cache.None && task.areDimensionsInFeasableInterval(res.result)) || MOAlgorithm.getCaching() != Cache.None) {

    					results.add(new MOAlgorithmEvalResult(res.result, res.algorithm)); 
    					allSingleProblemRunResults.add(task, res.result, res.algorithm);
    				}
    				else {
    					System.err.println(res.algorithm.getAlgorithmInfo().getVersionAcronym()+" result "+res.result+" is out of intervals! For task:"+task.getProblemName());
    					results.add(new MOAlgorithmEvalResult(null, res.algorithm)); // this can be done parallel - asynchrony                    
    				}

    				//reset(task);
    			} catch (InterruptedException | ExecutionException e) {
    				e.printStackTrace();
    			}
    		}

    		pool.shutdown();
    	}
    	else
    	{
    		super.runOneProblem(task, allSingleProblemRunResults);
    	}
    }

	/* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#initFullProblemList()
     */
    @Override
    protected void initFullProblemList() {
    	
    	ArrayList<DoubleMOProblem> problems = new ArrayList<DoubleMOProblem>();
    	
    	/*problems.add(new ZDT1());
    	problems.add(new ZDT2());
    	problems.add(new ZDT3());
    	problems.add(new ZDT4());
    	problems.add(new ZDT6());
    	problems.add(new DTLZ2(3));
    	problems.add(new WFG1(5));
    	problems.add(new WFG2(5));
    	problems.add(new DTLZ1(10));*/
    	
    	/*problems.add(new ZDT1());
    	problems.add(new ZDT2());
    	problems.add(new ZDT3());
    	problems.add(new ZDT4());
    	problems.add(new ZDT6());*/
    	    	
    	
    	/*problems.add(new WFG1(2));
    	problems.add(new WFG2(2));
    	problems.add(new WFG3(2));
    	problems.add(new WFG4(2));
    	problems.add(new WFG5(2));
    	problems.add(new WFG6(2));
    	problems.add(new WFG7(2));
    	problems.add(new WFG8(2));
    	problems.add(new WFG9(2));*/
    	
    	
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

    	
    	
    	/*problems.add(new DTLZ1(3));
    	problems.add(new DTLZ2(3));
    	problems.add(new DTLZ3(3));
    	problems.add(new DTLZ4(3));
    	problems.add(new DTLZ5(3)); // ?
    	problems.add(new DTLZ6(3));
    	problems.add(new DTLZ7(3));*/
    	
    	
    	for (DoubleMOProblem moProblem : problems) {
    		registerTask(stopCriteria, evaluationsOnDimension, 0.0001, moProblem);
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
        return "RatingEnsemble";
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getInfo()
     */
    @Override
    public String getInfo() {
        return "";
    }
}
