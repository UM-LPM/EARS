package org.um.feri.ears.experiment.ee;

import java.util.Locale;

import org.um.feri.ears.experiment.ee.so.JADELogging;
import org.um.feri.ears.experiment.ee.so.PSOoriginalLogging;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.ProblemSphere;
import org.um.feri.ears.util.Util;

public class SORatingExample {
	
    public static void main(String[] args) {
        Util.rnd.setSeed(System.currentTimeMillis());
        
        
        PSOoriginalLogging psoLogging = new PSOoriginalLogging();
        JADELogging algorithm = new JADELogging();
        
        try {
        	Problem p = new ProblemSphere(2);
        	Task t = new Task(EnumStopCriteria.EVALUATIONS, 3000, 0, 0, 0.001,p);
        	t.enableAncestorLogging();
			psoLogging.execute(t);
			t.saveAncestorLogging(psoLogging.getID()+"_"+p.getName());
		} catch (StopCriteriaException e) {
			e.printStackTrace();
		}
        Task.resetLoggingID();
        try {
        	
        	Problem p = new ProblemSphere(2);
        	Task t = new Task(EnumStopCriteria.EVALUATIONS, 3000, 0, 0, 0.001,p);
        	t.enableAncestorLogging();
        	algorithm.execute(t);
			t.saveAncestorLogging(algorithm.getID()+"_"+p.getName());
		} catch (StopCriteriaException e) {
			e.printStackTrace();
		}
        
    }
}
