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

import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.cec2010.*;

public class RatingCEC2010 extends RatingBenchmark{
    public static final String name="Benchmark CEC 2010";
    protected int dimension=1000; //recommended
   
    public boolean resultEqual(DoubleSolution a, DoubleSolution b) {
        if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if (Math.abs(a.getEval()-b.getEval())< drawLimit) return true;
        return false;
    }
    public RatingCEC2010(){
    	this(1e-7);
    }
    public RatingCEC2010(double draw_limit) {
        super();
        this.drawLimit = draw_limit;
        stopCriteria = EnumStopCriteria.EVALUATIONS;
        maxEvaluations= 10000; //(int) (1 * 1e6);
        maxIterations = 0; 
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,""+dimension);
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#registerTask(org.um.feri.ears.problems.Problem)
     */
    @Override
    protected void registerTask(Problem p, EnumStopCriteria sc, int eval, long time, int maxIterations, double epsilon) {
        listOfProblems.add(new Task(sc, eval, time, maxIterations, epsilon, p));
    }
    
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#initFullProblemList()
     */
    @Override
    protected void initFullProblemList() {
    	
    	registerTask(new F1(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F2(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F3(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F4(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F5(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F6(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F7(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F8(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F9(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F10(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F11(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F12(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F13(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F14(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F15(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F16(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F17(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F18(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F19(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
    	registerTask(new F20(dimension),stopCriteria, maxEvaluations, timeLimit, maxIterations, 0.001);
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
        return "CEC2010";
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getInfo()
     */
    @Override
    public String getInfo() {
        return "";
    }
    
}
