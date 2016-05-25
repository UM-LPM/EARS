package org.um.feri.ears.algorithms.tlbo;

public class TLBOConstraintsStatistic {
    private double max_constrains[];
    private double sum_constrains[];
    private double count_constrains[];
    private double min_constrains[];
    private int number; 
    public TLBOConstraintsStatistic(int nConstraints) {
        super();
        number = nConstraints;
        max_constrains = new double[number];
        sum_constrains = new double[number];
        count_constrains = new double[number];
        min_constrains = new double[number];
    
    }

}
