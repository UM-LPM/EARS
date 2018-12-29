package org.um.feri.ears.util.report;

public class Pair<T1,T2> {
  private T1 a;
  private T2 b;
  public Pair(T1 a, T2 b) {
    this.a = a;
    this.b = b;
  }
  public T1 getA() {
    return a;
  }
  public void setA(T1 a) {
    this.a = a;
  }
  public T2 getB() {
    return b;
  }
  public void setB(T2 b) {
    this.b = b;
  }
  
  
}
