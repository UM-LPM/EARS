package org.um.feri.ears.experiment.ee;

import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.hc.HillClimbing;
import org.um.feri.ears.algorithms.so.jDElscop.jDElscop;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.cec2014.*;

public class SoCEC2014Benchmark {
	
    public static void main(String[] args) {
        DoubleProblem[] problems = new DoubleProblem[16];
        int dimm;
        try
        {
        	dimm = Integer.parseInt(args[0]);
        }catch(Exception e)
        {
        	dimm = 2;
        }
        
        RandomWalkAlgorithm randomLog = new RandomWalkAlgorithm();
        HillClimbing hillClimb = new HillClimbing(HillClimbing.HillClimbingStrategy.STEEPEST_ASCENT,0.001);
        JADE jadeLogging = new JADE();
        DEAlgorithm deLogging = new DEAlgorithm(DEAlgorithm.Strategy.DE_BEST_1_BIN);
        jDElscop jDElscopLog = new jDElscop();
        TLBOAlgorithm TLBOLog = new TLBOAlgorithm();
        
        System.out.println(randomLog.getId()+ " "+ hillClimb.getId()+" "+jadeLogging.getId()+" "+
        		          deLogging.getId()+" "+ jDElscopLog.getId() +" "+TLBOLog.getId());
        
        for(int run = 0; run < 10; ++run)
        {
			problems[0] = new F1(dimm);
			problems[1] = new F2(dimm);
			problems[2] = new F3(dimm);
			problems[3] = new F4(dimm);
			problems[4] = new F5(dimm);
			problems[5] = new F6(dimm);
			problems[6] = new F7(dimm);
			problems[7] = new F8(dimm);
			problems[8] = new F9(dimm);
			problems[9] = new F10(dimm);
			problems[10] = new F11(dimm);
			problems[11] = new F12(dimm);
			problems[12] = new F13(dimm);
			problems[13] = new F14(dimm);
			problems[14] = new F15(dimm);
			problems[15] = new F16(dimm);
        	
        	for(int pr = 0; pr < problems.length; ++pr)
        	{
        	/*	Problem p = problems[pr];
        		try {
                	Task t = new Task(EnumStopCriterion.EVALUATIONS, 1000*dimm, 0, 0, 0.001, p);
                	t.enableAncestorLogging();
                	randomLog.execute(t);
        			t.saveAncestorLogging(randomLog.getID()+"_"+p.getName()+"D"+dimm+"R"+run);
        		} catch (StopCriterionException e) {
        			e.printStackTrace();
        		}
        		
                Task.resetLoggingID();
                try {
                	Task t = new Task(EnumStopCriterion.EVALUATIONS, 1000*dimm, 0, 0, 0.001, p);
                	t.enableAncestorLogging();
                	hillClimb.execute(t);
        			t.saveAncestorLogging(hillClimb.getID()+"_"+p.getName()+"D"+dimm+"R"+run);
        		} catch (StopCriterionException e) {
        			e.printStackTrace();
        		}
        		
              /*  Task.resetLoggingID();
                try {
                	Task t = new Task(EnumStopCriterion.EVALUATIONS, 1000*dimm, 0, 0, 0.001, p);
                	t.enableAncestorLogging();
                	jadeLogging.execute(t);
        			t.saveAncestorLogging(jadeLogging.getID()+"_"+p.getName()+"D"+dimm+"R"+run);
        		} catch (StopCriterionException e) {
        			e.printStackTrace();
        		}
                
                Task.resetLoggingID();
                try{
                	Task t = new Task(EnumStopCriterion.EVALUATIONS, 1000*dimm, 0, 0, 0.001, p);
                	t.enableAncestorLogging();
                	deLogging.execute(t);
                	t.saveAncestorLogging(deLogging.getID().replaceAll("/", "-")+"_"+p.getName()+"D"+dimm+"R"+run);
                } catch (StopCriterionException e){
                	e.printStackTrace();
                }
                Task.resetLoggingID();

                try{
                	Task t = new Task(EnumStopCriterion.EVALUATIONS, 1000*dimm, 0, 0, 0.001, p);
                	t.enableAncestorLogging();
                	jDElscopLog.execute(t);
                	t.saveAncestorLogging(jDElscopLog.getID().replaceAll("/", "-")+"_"+p.getName()+"D"+dimm+"R"+run);
                } catch (StopCriterionException e){
                	e.printStackTrace();
                }
                Task.resetLoggingID();

                try{
                	Task t = new Task(EnumStopCriterion.EVALUATIONS, 1000*dimm, 0, 0, 0.001, p);
                	t.enableAncestorLogging();
                	TLBOLog.execute(t);
                	t.saveAncestorLogging(TLBOLog.getID().replaceAll("/", "-")+"_"+p.getName()+"D"+dimm+"R"+run);
                } catch (StopCriterionException e){
                	e.printStackTrace();
                }*/
                Task.resetLoggingID();

        	}
        }
          
    }
}
