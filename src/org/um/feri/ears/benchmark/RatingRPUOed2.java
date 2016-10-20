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
import org.um.feri.ears.problems.unconstrained.ProblemAckley;
import org.um.feri.ears.problems.unconstrained.ProblemB2;
import org.um.feri.ears.problems.unconstrained.ProblemBeale;
import org.um.feri.ears.problems.unconstrained.ProblemBooth;
import org.um.feri.ears.problems.unconstrained.ProblemBranin;
import org.um.feri.ears.problems.unconstrained.ProblemDeJong;
import org.um.feri.ears.problems.unconstrained.ProblemEasom;
import org.um.feri.ears.problems.unconstrained.ProblemGoldSteinAndPrice;
import org.um.feri.ears.problems.unconstrained.ProblemGriewank;
import org.um.feri.ears.problems.unconstrained.ProblemMartinAndGaddy;
import org.um.feri.ears.problems.unconstrained.ProblemPowellBadlyScaledFunction;
import org.um.feri.ears.problems.unconstrained.ProblemRastrigin;
import org.um.feri.ears.problems.unconstrained.ProblemRosenbrock;
import org.um.feri.ears.problems.unconstrained.ProblemSchwefel;
import org.um.feri.ears.problems.unconstrained.ProblemSchwefelRidge;
import org.um.feri.ears.problems.unconstrained.ProblemSphere;

//TODO calculate CD for rating
public class RatingRPUOed2 extends RatingBenchmark {
    public static final String name="Real Parameter Unconstrained Optimization Problems with maximum evaluation condition";
    protected int evaluationsOnDimension=1500;
    protected long timeLimit = 5 * 1000;
    public static final double DRAW_LIMIT=0.000000001;
    
    public boolean resultEqual(DoubleSolution a, DoubleSolution b) {
        if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if (Math.abs(a.getEval()-b.getEval())<DRAW_LIMIT) return true;
        return false;
    }
    public RatingRPUOed2() {
        super();
        //stopCriteria = EnumStopCriteria.CPU_TIME;
        
        evaluationsOnDimension=1500;
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,"2");
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(evaluationsOnDimension*2));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+DRAW_LIMIT);
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
        registerTask(new ProblemAckley(2),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemB2(),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemBeale(),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemBooth(),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemBranin(),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemDeJong(),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemEasom(),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemGoldSteinAndPrice(),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemGriewank(2),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemMartinAndGaddy(),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemPowellBadlyScaledFunction(),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemRastrigin(2),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemRosenbrock(2),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemSchwefel(2),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemSchwefelRidge(2),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
        registerTask(new ProblemSphere(2),stopCriteria, 2*evaluationsOnDimension, timeLimit, 0.001);
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
        return "RPUOed2";
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getInfo()
     */
    @Override
    public String getInfo() {
        return "";
    }
    
}
