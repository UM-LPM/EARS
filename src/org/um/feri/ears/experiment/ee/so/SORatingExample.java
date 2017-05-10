package org.um.feri.ears.experiment.ee;

import org.um.feri.ears.experiment.ee.so.DEAlgorithmLogging;
import org.um.feri.ears.experiment.ee.so.JADELogging;
import org.um.feri.ears.experiment.ee.so.PSOoriginalLogging;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.ProblemGriewank;
import org.um.feri.ears.problems.unconstrained.ProblemRastrigin;
import org.um.feri.ears.problems.unconstrained.ProblemRosenbrock;
import org.um.feri.ears.problems.unconstrained.ProblemSchwefel;
import org.um.feri.ears.problems.unconstrained.ProblemSphere;
import org.um.feri.ears.util.Util;

public class SORatingExample {
	
    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis());
        
        Problem[] problems = new Problem[5];
        int[] dimmensions = {10};
        PSOoriginalLogging psoLogging = new PSOoriginalLogging();
        JADELogging jadeLogging = new JADELogging();
        DEAlgorithmLogging deLogging = new DEAlgorithmLogging(DEAlgorithmLogging.DE_best_1_bin);
        
        for (int i = 0; i < dimmensions.length; ++i)
        {
        	problems[0] = new ProblemSphere(dimmensions[i]);
        	problems[1] = new ProblemGriewank(dimmensions[i]);
        	problems[2] = new ProblemRastrigin(dimmensions[i]);
        	problems[3] = new ProblemRosenbrock(dimmensions[i]);
        	problems[4] = new ProblemSchwefel(dimmensions[i]);
        	
        	for(int pr = 0; pr < problems.length; ++pr)
        	{
        		Problem p = problems[pr];
        		try {
                	Task t = new Task(EnumStopCriteria.EVALUATIONS, 5000*dimmensions[i], 0, 0, 0.001, p);
                	t.enableAncestorLogging();
        			psoLogging.execute(t);
        			t.saveAncestorLogging(psoLogging.getID()+"_"+p.getName());
        		} catch (StopCriteriaException e) {
        			e.printStackTrace();
        		}
        		
                Task.resetLoggingID();
                try {
                	Task t = new Task(EnumStopCriteria.EVALUATIONS, 5000*dimmensions[i], 0, 0, 0.001, p);
                	t.enableAncestorLogging();
                	jadeLogging.execute(t);
        			t.saveAncestorLogging(jadeLogging.getID()+"_"+p.getName());
        		} catch (StopCriteriaException e) {
        			e.printStackTrace();
        		}
                
                Task.resetLoggingID();
                try{
                	Task t = new Task(EnumStopCriteria.EVALUATIONS, 5000*dimmensions[i], 0, 0, 0.001, p);
                	t.enableAncestorLogging();
                	deLogging.execute(t);
                	t.saveAncestorLogging(deLogging.getID().replaceAll("/", "-")+"_"+p.getName());
                } catch (StopCriteriaException e){
                	e.printStackTrace();
                }
                Task.resetLoggingID();

        	}
                    	
        	
        }
          
    }
}
