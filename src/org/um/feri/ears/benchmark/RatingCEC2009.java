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
import org.um.feri.ears.problems.EnumStopCriterion;
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
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.FutureResult;


public class RatingCEC2009 extends MORatingBenchmark<Double, DoubleMOTask, DoubleMOProblem>{
    public static final String name="Benchmark CEC 2009";
    
	@Override
	public boolean resultEqual(ParetoSolution<Double> a, ParetoSolution<Double> b, QualityIndicator<Double> qi) {
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
	
    public RatingCEC2009(){
    	this(null, 0.0000001);
    	List<IndicatorName> indicators = new ArrayList<IndicatorName>();
        indicators.add(IndicatorName.IGD); //Default indicator
        this.indicators = indicators;
    }
    
    public RatingCEC2009(List<IndicatorName> indicators, double draw_limit) {
        super(indicators);
        this.drawLimit = draw_limit;
        maxEvaluations=30000;
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);

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
    		registerTask(stopCriterion, maxEvaluations, timeLimit, maxIterations, 0.001, moProblem);
		}
    }
    
    @Override
	protected void runOneProblem(DoubleMOTask task, BankOfResults allSingleProblemRunResults) {

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

        			results.add(new MOAlgorithmEvalResult(res.result, res.algorithm, res.task)); 
        			allSingleProblemRunResults.add(task, res.result, res.algorithm);
        		}
        		else {
        			System.err.println(res.algorithm.getAlgorithmInfo().getAcronym()+" result "+res.result+" is out of intervals! For task:"+task.getProblemName());
        			results.add(new MOAlgorithmEvalResult(null, res.algorithm, res.task)); // this can be done parallel - asynchrony                    
        		}
        		
        		//reset(task);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
        }
        
    	pool.shutdown();
    	
    	//TODO if not parallel call super
        
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
        return "CEC2009";
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getInfo()
     */
    @Override
    public String getInfo() {
        return "";
    }
}
