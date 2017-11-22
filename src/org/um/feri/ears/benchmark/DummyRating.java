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

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.DummyProblem;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;

public class DummyRating extends RatingBenchmark{
    public static final String name="Dummy rating";
    protected int dimension=3;
    
    public boolean resultEqual(DoubleSolution a, DoubleSolution b) {
        if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if (Math.abs(a.getEval()-b.getEval())<draw_limit) return true;
        return false;
    }
    public DummyRating(){
    	this(0.000001);
    }
    public DummyRating(double draw_limit) {
        super();
        this.draw_limit = 1e-6;
        maxEvaluations=3000;
        dimension=3;
        maxIterations = 0;
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,"3");
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#registerTask(org.um.feri.ears.problems.Problem)
     */
    @Override
    protected void registerTask(Problem p, EnumStopCriteria sc, int eval, long time,int maxIterations, double epsilon) {
        listOfProblems.add(new Task(sc, eval, time, maxIterations, epsilon, p));
    }
    
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#initFullProblemList()
     */
    
    public void addDummyTask(String name){
    	registerTask(new DummyProblem(name),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    }
    
    @Override
    protected void initFullProblemList() {
    	
    	
    	/*registerTask(new DummyProblem("i3_te1"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("i3_te2"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("i3_te3"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);*/
    	
    	/*registerTask(new DummyProblem("f1"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f2"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f3"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f4"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f5"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f6"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f7"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f8"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f9"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f10"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f11"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f12"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f13"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f14"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f15"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f16"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f17"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f18"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f19"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f20"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f21"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("f22"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);*/
    	
    	//Grouped by method
    	/*
    	registerTask(new DummyProblem("en300_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("en300_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("en300_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("en300_c4"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c4"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c4"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c4"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c4"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c4"),stopCriteria, evaluationsOnDimension, 0.001);*/
    	/*
    	registerTask(new DummyProblem("en300_c5"),stopCriteria,  maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("1HZ_c5"),stopCriteria, maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("50HZ_c5"),stopCriteria, maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("300HZ_c5"),stopCriteria, maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("750HZ_c5"),stopCriteria, maxEvaluations, 0, maxIterations, 0.001);
    	registerTask(new DummyProblem("9s20_c5"),stopCriteria, maxEvaluations, 0, maxIterations, 0.001);*/
    	
    	
    	// Grouped by material
    	/*
    	registerTask(new DummyProblem("en300_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("en300_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("en300_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("en300_c4"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("en300_c5"),stopCriteria, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("1HZ_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c4"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("1HZ_c5"),stopCriteria, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("50HZ_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c4"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("50HZ_c5"),stopCriteria, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("300HZ_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c4"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("300HZ_c5"),stopCriteria, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("750HZ_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c4"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("750HZ_c5"),stopCriteria, evaluationsOnDimension, 0.001);
    	
    	registerTask(new DummyProblem("9s20_c1"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c2"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c3"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c4"),stopCriteria, evaluationsOnDimension, 0.001);
    	registerTask(new DummyProblem("9s20_c5"),stopCriteria, evaluationsOnDimension, 0.001);*/
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
        return "DummyRating";
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getInfo()
     */
    @Override
    public String getInfo() {
        return "";
    }
    
}
