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
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.*;

public class RatingRPUOed2 extends RatingBenchmark {
    public static final String name="Real Parameter Unconstrained Optimization Problems with maximum evaluation condition";
    
    public boolean resultEqual(DoubleSolution a, DoubleSolution b) {
        if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if (Math.abs(a.getEval()-b.getEval())<draw_limit) return true;
        return false;
    }
    public RatingRPUOed2() {
        super();
        stopCriteria = EnumStopCriteria.EVALUATIONS;
        timeLimit = 10;
        maxEvaluations=10000;
        maxIterations = 500; 
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,"2");
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(maxEvaluations*2));
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
        registerTask(new Ackley1(2),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Bohachevsky1(),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Beale(),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Booth(),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Branin1(),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Easom(),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new GoldsteinPrice(),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Griewank(2),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new MartinAndGaddy(),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new PowellBadlyScaledFunction(),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Rastrigin(2),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new RosenbrockDeJong2(2),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Schwefel226(2),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new SchwefelRidge(2),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
        registerTask(new Sphere(2),stopCriteria, 2*maxEvaluations, timeLimit, maxIterations, 0.001);
    }
        
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getName()
     */
    @Override
    public String getName() {
        return name + "("+ getParameters().get(EnumBenchmarkInfoParameters.DIMENSION)+")";
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
