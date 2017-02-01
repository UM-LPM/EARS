/**
 * Insert data
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
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.concurrent.TimeUnit;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.export.data.EDBenchmark;
import org.um.feri.ears.export.data.EDTask;
import org.um.feri.ears.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.rating.Game;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

//TODO calculate CD for rating
public abstract class RatingBenchmark extends RatingBenchmarkBase<Task,Algorithm,AlgorithmEvalResult> {
    
    
    /**
     * TODO  this function can be done parallel - asynchrony
     * 
     * @param task
     * @param allSingleProblemRunResults 
     */
    protected void runOneProblem(Task task, BankOfResults allSingleProblemRunResults) {
    	long start=0;
    	long duration=0;
        for (Algorithm al: listOfAlgorithmsPlayers) {
        	reset(task); //number of evaluations  
            try {
                start = System.nanoTime();
         
                GraphDataRecorder.SetContext(al,task);
                task.startTimer();
                DoubleSolution bestByALg = al.execute(task); //check if result is fake!

                duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                al.addRunDuration(duration, duration - task.getEvaluationTimeMs());
                if (printSingleRunDuration) System.out.println(al.getID()+": "+duration/1000.0);
                reset(task); //for one eval!
                if (task.areDimensionsInFeasableInterval(bestByALg.getVariables())) {

                	results.add(new AlgorithmEvalResult(bestByALg, al, task.getNumberOfEvaluations())); 
                	allSingleProblemRunResults.add(task, bestByALg, al);

                }
                else {
                	System.err.println(al.getAlgorithmInfo().getVersionAcronym()+" result "+bestByALg+" is out of intervals! For task:"+task.getProblemName());
                	results.add(new AlgorithmEvalResult(null, al, task.getNumberOfEvaluations())); // this can be done parallel - asynchrony                    
                }
            } catch (StopCriteriaException e) {
            	System.err.println(al.getAlgorithmInfo().getVersionAcronym()+" StopCriteriaException for:"+task+"\n"+e);
            	results.add(new AlgorithmEvalResult(null, al, task.getNumberOfEvaluations()));
            }   
        }
    }
    
	@Override
	public void registerAlgorithm(Algorithm al) {
        listOfAlgorithmsPlayers.add(al);
    }
	
	
    
    @Override
	public void registerAlgorithms(ArrayList<Algorithm> algorithms) {
    	listOfAlgorithmsPlayers.addAll(algorithms);
		
	}



	class FitnessComparator implements Comparator<AlgorithmEvalResult> {
        Task t;
        public FitnessComparator(Task t) {
            this.t = t;
        }
        @Override
        public int compare(AlgorithmEvalResult arg0, AlgorithmEvalResult arg1) {
            if (arg0.getBest()!=null) {
                if (arg1.getBest()!=null){
                	
                    //if global optimum first check if draw the compare number of evaluations
                	if(stopCriteria == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS)
                	{
                		// if results are equal check number of evaluations
                		if(resultEqual(arg0.getBest(), arg1.getBest())){
                			if(arg0.numEvaluations < arg1.numEvaluations){
                				return -1;
                			}
                			if(arg0.numEvaluations > arg1.numEvaluations){
                				return 1;
                			}
                			return 0;
                		}
                	}
                    if (t.isFirstBetter(arg0.getBest(), arg1.getBest())) {
                    	return -1;
                    }
                    
                    else return 1;
                } else return -1; //second is null
            } else if (arg1.getBest()!= null) return 1; //first null
            return 0; //both equal
        }
    }
    
    protected void setWinLoseFromResultList(ResultArena arena, Task t) {
        AlgorithmEvalResult win;
        AlgorithmEvalResult lose;        
        FitnessComparator fc;
        fc = new FitnessComparator(t);
        Collections.sort(results, fc); //best first
        for (int i=0; i<results.size()-1; i++) {
            win = results.get(i);
            for (int j=i+1; j<results.size(); j++) {
                lose = results.get(j);
                if (resultEqual(win.best, lose.best)) { //Special for this benchmark
                    if (debugPrint) System.out.println("draw of "+win.getAl().getID()+" ("+Util.df3.format(win.getBest().getEval())+", feasable="+win.getBest().isFeasible()+") against "+lose.getAl().getID()+" ("+Util.df3.format(lose.getBest().getEval())+", feasable="+lose.getBest().isFeasible()+") for "+t.getProblemName());
                    arena.addGameResult(Game.DRAW, win.getAl().getAlgorithmInfo().getVersionAcronym(), lose.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemName());
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
                    if (debugPrint) System.out.println("win of "+win.getAl().getID()+" ("+Util.df3.format(win.getBest().getEval())+", feasable="+win.getBest().isFeasible()+") against "+lose.getAl().getID()+" ("+Util.df3.format(lose.getBest().getEval())+", feasable="+lose.getBest().isFeasible()+") for "+t.getProblemName());
                    arena.addGameResult(Game.WIN, win.getAl().getAlgorithmInfo().getVersionAcronym(), lose.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemName());
                }
                    
            }
        }
    }
    
    public boolean resultEqual(DoubleSolution a, DoubleSolution b) {
        if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if (!a.isFeasible()&&b.isFeasible()) return false;
        if (a.isFeasible()&&!b.isFeasible()) return false;
        if (!a.isFeasible()&&!b.isFeasible()) return true;
        if (Math.abs(a.getEval()-b.getEval())<draw_limit) return true;
        return false;
    }
    
    protected abstract void registerTask(Problem p, EnumStopCriteria sc, int eval, long time, int maxIterations, double epsilon);
    
    /**
     * Fill all data!
     *  
     * @param arena needs to be filed with players and their ratings
     * @param allSingleProblemRunResults 
     * @param repetition
     */
    public void run(ResultArena arena, BankOfResults allSingleProblemRunResults, int repetition) {
        duelNumber = repetition;
        addParameter(EnumBenchmarkInfoParameters.NUMBER_OF_DUELS, ""+repetition);
        long start = System.nanoTime();
        for (Task t:listOfProblems) {
        	System.out.println("Current problem: "+t.getProblemName());
            for (int i=0; i<repetition; i++) {
            	System.out.println("Current duel: "+ (i+1));
                runOneProblem(t,allSingleProblemRunResults);
                setWinLoseFromResultList(arena,t);
                results.clear();
            }
        }
        addParameter(EnumBenchmarkInfoParameters.BENCHMARK_RUN_DURATION, ""+TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        if(isCheating())
        {
        	System.out.println("The reset count does not match!");
        }
        
        // Recalculate ratings after tournament
        //arena.recalcRatings();

        
        if(displayRatingIntervalChart)
        {
        	arena.calculteRatings();
        	displayRatingIntervalsChart(arena.getPlayers());
        }
        
    }
    
}
