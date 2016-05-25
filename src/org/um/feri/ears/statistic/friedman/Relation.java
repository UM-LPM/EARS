package org.um.feri.ears.statistic.friedman;

public class Relation {

	  public int i;
	  public int j;

	  public Relation() {

	  }

	  public Relation(int x, int y) {
	    i = x;
	    j = y;
	  }
	  
	  public String toString() {
		  return "("+i+","+j+")";	  
	  }

}
