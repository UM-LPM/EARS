package org.um.feri.ears.statistic.friedman;

/*
 * 
 * Class contains routines that order object by absolute value
 * 
 */

public class Pair implements Comparable<Object> {

  public double index;
  public double value;

  public Pair() {

  }

  public Pair(double i, double v) {
    index = i;
    value = v;
  }

  public int compareTo(Object o1) {
    if (this.value > ((Pair)o1).value)
      return -1;
    else if (Math.abs(this.value) < Math.abs(((Pair)o1).value))
      return 1;
    else return 0;
  }

}