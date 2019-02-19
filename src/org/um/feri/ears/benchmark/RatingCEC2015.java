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
import java.util.concurrent.TimeUnit;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.cec2015.F1;
import org.um.feri.ears.problems.unconstrained.cec2015.F10;
import org.um.feri.ears.problems.unconstrained.cec2015.F11;
import org.um.feri.ears.problems.unconstrained.cec2015.F12;
import org.um.feri.ears.problems.unconstrained.cec2015.F13;
import org.um.feri.ears.problems.unconstrained.cec2015.F14;
import org.um.feri.ears.problems.unconstrained.cec2015.F15;
import org.um.feri.ears.problems.unconstrained.cec2015.F2;
import org.um.feri.ears.problems.unconstrained.cec2015.F3;
import org.um.feri.ears.problems.unconstrained.cec2015.F4;
import org.um.feri.ears.problems.unconstrained.cec2015.F5;
import org.um.feri.ears.problems.unconstrained.cec2015.F6;
import org.um.feri.ears.problems.unconstrained.cec2015.F7;
import org.um.feri.ears.problems.unconstrained.cec2015.F8;
import org.um.feri.ears.problems.unconstrained.cec2015.F9;


public class RatingCEC2015 extends RatingBenchmark{
    public static final String name="Benchmark CEC 2015";
    protected boolean calculateTime = false;
    protected int warmupIterations = 10000;

    public RatingCEC2015(){
    	this(0.0000001);
    }
    public RatingCEC2015(double draw_limit) {
        super();
        this.draw_limit = draw_limit;
        maxEvaluations= 300000; // 1500 exact evaluations
        dimension=30;
        timeLimit = 2500;
        maxIterations = 2500;
        stopCriteria = EnumStopCriteria.EVALUATIONS;
        initFullProblemList();
        /*addParameter(EnumBenchmarkInfoParameters.STOPPING_CRITERIA,""+stopCriteria);
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,""+dimension);
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);*/
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#registerTask(org.um.feri.ears.problems.Problem)
     */
    @Override
    protected void registerTask(Problem p, EnumStopCriteria sc, int eval, long time, int maxIterations, double epsilon) {
        listOfProblems.add(new Task(sc, eval, time, maxIterations, epsilon, p));
    }
    
    @Override
	public int getNumberOfRuns() {
		//number of runs set to 8 to reduce server execution time
		return 10;
	}
	/* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#initFullProblemList()
     */
    @Override
    protected void initFullProblemList() {
    	
    	ArrayList<Problem> problems = new ArrayList<Problem>();
    	
    	problems.add(new F1(dimension));
    	problems.add(new F2(dimension));
    	problems.add(new F3(dimension));
    	problems.add(new F4(dimension));
    	problems.add(new F5(dimension));
    	problems.add(new F6(dimension));
    	problems.add(new F7(dimension));
    	problems.add(new F8(dimension));
    	problems.add(new F9(dimension));
    	problems.add(new F10(dimension));
    	problems.add(new F11(dimension));
    	problems.add(new F12(dimension));
    	problems.add(new F13(dimension));
    	problems.add(new F14(dimension));
    	problems.add(new F15(dimension));
    	
    	for(Problem p : problems)
    	{
    		/*if(stopCriteria == EnumStopCriteria.CPU_TIME && calculateTime)
    		{
    			System.out.println("Calculating time for problem: "+p.getName());
    			timeLimit = calculateTime(p);
    		}*/
    		
    		if(stopCriteria == EnumStopCriteria.CPU_TIME)
    		{
    			for(int i = 0; i < warmupIterations; i++)
    			{
    				p.getRandomSolution();
    			}
    		}
    		
    		registerTask(p, stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	}
    }
        
    private long calculateTime(Problem p) {
		
    	long start = System.nanoTime();
		long duration;
		for(int i = 0; i < maxEvaluations; i++)
		{
			p.getRandomSolution();
		}
		duration = System.nanoTime() - start;
		// add algorithm runtime
		duration += (int)(duration*(10.0f/100.0f));
		
		return TimeUnit.NANOSECONDS.toMillis(duration);
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
        return "CEC2015";
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getInfo()
     */
    @Override
    public String getInfo() {
        return "";
    }
    
}
