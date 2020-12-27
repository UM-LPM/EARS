package org.um.feri.ears.problems;

public class StopCriterionException extends Exception {
    public static String id="";
	public StopCriterionException(String msg) {
		super(msg);
		if (id.length()>0)
          System.err.println("Stop criterion! "+id);
    }
}