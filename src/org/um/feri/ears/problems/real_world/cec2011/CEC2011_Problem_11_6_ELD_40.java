package org.um.feri.ears.problems.real_world.cec2011;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.util.Util;

/**
 * Problem function!
 * 
 * @author Matej Črepinšek
 * @version 1
 * 
 **/
public class CEC2011_Problem_11_6_ELD_40 extends Problem {
  private double g_constrains[]; // internal
  /*
  */

  // % % % ============= 6 unit system data ==========
  // % Data1= [Pmin Pmax a b c];
  private static double[][] Data1 = { { 36, 114, 0.0069, 6.73, 94.705, 100, 0.084 }, { 36, 114, 0.0069, 6.73, 94.705, 100, 0.084 }, { 60, 120, 0.02028, 7.07, 309.54, 100, 0.084 }, { 80, 190, 0.00942, 8.18, 369.03, 150, 0.063 }, { 47, 97, 0.0114, 5.35, 148.89, 120, 0.077 },
      { 68, 140, 0.01142, 8.05, 222.33, 100, 0.084 }, { 110, 300, 0.00357, 8.03, 287.71, 200, 0.042 }, { 135, 300, 0.00492, 6.99, 391.98, 200, 0.042 }, { 135, 300, 0.00573, 6.6, 455.76, 200, 0.042 }, { 130, 300, 0.00605, 12.9, 722.82, 200, 0.042 }, { 94, 375, 0.00515, 12.9, 635.2, 200, 0.042 },
      { 94, 375, 0.00569, 12.8, 654.69, 200, 0.042 }, { 125, 500, 0.00421, 12.5, 913.4, 300, 0.035 }, { 125, 500, 0.00752, 8.84, 1760.4, 300, 0.035 }, { 125, 500, 0.00708, 9.15, 1728.3, 300, 0.035 }, { 125, 500, 0.00708, 9.15, 1728.3, 300, 0.035 }, { 220, 500, 0.00313, 7.97, 647.85, 300, 0.035 },
      { 220, 500, 0.00313, 7.95, 649.69, 300, 0.035 }, { 242, 550, 0.00313, 7.97, 647.83, 300, 0.035 }, { 242, 550, 0.00313, 7.97, 647.81, 300, 0.035 }, { 254, 550, 0.00298, 6.63, 785.96, 300, 0.035 }, { 254, 550, 0.00298, 6.63, 785.96, 300, 0.035 }, { 254, 550, 0.00284, 6.66, 794.53, 300, 0.035 },
      { 254, 550, 0.00284, 6.66, 794.53, 300, 0.035 }, { 254, 550, 0.00277, 7.1, 801.32, 300, 0.035 }, { 254, 550, 0.00277, 7.1, 801.32, 300, 0.035 }, { 10, 150, 0.52124, 3.33, 1055.1, 120, 0.077 }, { 10, 150, 0.52124, 3.33, 1055.1, 120, 0.077 }, { 10, 150, 0.52124, 3.33, 1055.1, 120, 0.077 },
      { 47, 97, 0.0114, 5.35, 148.89, 120, 0.077 }, { 60, 190, 0.0016, 6.43, 222.92, 150, 0.063 }, { 60, 190, 0.0016, 6.43, 222.92, 150, 0.063 }, { 60, 190, 0.0016, 6.43, 222.92, 150, 0.063 }, { 90, 200, 0.0001, 8.95, 107.87, 200, 0.042 }, { 90, 200, 0.0001, 8.62, 116.58, 200, 0.042 },
      { 90, 200, 0.0001, 8.62, 116.58, 200, 0.042 }, { 25, 110, 0.0161, 5.88, 307.45, 80, 0.098 }, { 25, 110, 0.0161, 5.88, 307.45, 80, 0.098 }, { 25, 110, 0.0161, 5.88, 307.45, 80, 0.098 }, { 242, 550, 0.00313, 7.97, 647.83, 300, 0.035 } };
  // % Data2=[Po UR DR Zone1min Zone1max Zone2min Zone2max];
  private static double[][] Data2 = {};

  // % Loss Co-efficients
  private static double[][] B1 = {};
  private static double[] B2 = {};
  private static double B3 = 0;
  private static double Power_Demand = 10500;; // in MW

  private static int Pmin_data1_col = 0;// = Data1(:,1)';
  private static int Pmax_data1_col = 1;// = Data1(:,2)';
  private static int a_data1_col = 2;// = Data1(:,3)';
  private static int b_data1_col = 3;// = Data1(:,4)';
  private static int c_data1_col = 4;// = Data1(:,5)';
  private static int e_data1_col = 5;// = Data1(:,6)';
  private static int f_data1_col = 6;// = Data1(:,7)';

  public CEC2011_Problem_11_6_ELD_40() {
    super(40, 2);

    lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
    upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));

    for (int z = 0; z < numberOfDimensions; z++) {
      lowerLimit.set(z, Data1[z][0]);
      upperLimit.set(z, Data1[z][1]);
    }

    name = "RWP_11_5_ELD_40";
    description = "RWP_11_5_ELD_40 Static Economic Load Dispatch (ELD) Problem ";
  }

  protected double sum(double x[]) {
    double tt = 0;
    for (int ii = 0; ii < x.length; ii++) {
      tt += x[ii];
    }
    return tt;
  }

  public double[] evaluateConstrains(double x[]) {
    g_constrains = new double[numberOfConstraints];
    double Power_Loss = 0;
    double Power_Balance_Penalty = Math.abs(Power_Demand + Power_Loss - sum(x));
    double Capacity_Limits_Penalty = 0;
    for (int i = 0; i < numberOfDimensions; i++) { // Capacity_Limits_Penalty = sum(abs(x-Pmin)-(x-Pmin)) +
                                                   // sum(abs(Pmax-x)-(Pmax-x));
      Capacity_Limits_Penalty += Math.abs(x[i] - Data1[i][Pmin_data1_col]) - (x[i] - Data1[i][Pmin_data1_col]); // sum(abs(x-Pmin)-(x-Pmin))
      Capacity_Limits_Penalty += Math.abs(Data1[i][Pmax_data1_col] - x[i]) - (Data1[i][Pmax_data1_col] - x[i]); // sum(abs(Pmax-x)-(Pmax-x))
    }
    g_constrains[0] = 1e5 * Power_Balance_Penalty;
    g_constrains[1] = 1e3 * Capacity_Limits_Penalty;

    return g_constrains; // do I need deep copy?
  }

  public double eval(double x[]) {

    // Cost(i,1) = sum( a.*(x.^2) + b.*x + c + abs(e.*sin(f.*(Pmin-x))) );
    double f = 0;
    for (int i = 0; i < numberOfDimensions; i++) {
      f += Data1[i][a_data1_col] * x[i] * x[i] + Data1[i][b_data1_col] * x[i] + Data1[i][c_data1_col] + Math.abs(Data1[i][e_data1_col] * Math.sin(Data1[i][e_data1_col] * (Data1[i][Pmin_data1_col] - x[i])));
    }
    // System.out.println("f:"+f+" Total_Penalty:"+Total_Penalty);

    return f;
  }
}