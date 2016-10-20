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
import org.um.feri.ears.problems.unconstrained.cec2015.*;


public class RatingCEC2015 extends RatingBenchmark{
    public static final String name="Benchmark CEC 2015";
    protected int evaluationsOnDimension=3000;
    protected int dimension=3;
    private double draw_limit=0.0000001;
    protected long timeLimit = 0;
    
    public boolean resultEqual(DoubleSolution a, DoubleSolution b) {
        if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if (Math.abs(a.getEval()-b.getEval())<draw_limit) return true;
        return false;
    }
    public RatingCEC2015(){
    	this(0.0000001);
    }
    public RatingCEC2015(double draw_limit) {
        super();
        this.draw_limit = draw_limit;
        evaluationsOnDimension=3000;
        dimension=10;
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,"3");
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(evaluationsOnDimension));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#registerTask(org.um.feri.ears.problems.Problem)
     */
    @Override
    protected void registerTask(Problem p, EnumStopCriteria sc, int eval, long time, double epsilon) {
        listOfProblems.add(new Task(sc, eval, time, epsilon, p));
    }
    
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#initFullProblemList()
     */
    @Override
    protected void initFullProblemList() {
    	
    	registerTask(new F1(dimension),stopCriteria,evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F2(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F3(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F4(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F5(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F6(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F7(dimension),stopCriteria,  evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F8(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F9(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F10(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F11(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F12(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F13(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F14(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);
    	registerTask(new F15(dimension),stopCriteria, evaluationsOnDimension, timeLimit, 0.001);

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
