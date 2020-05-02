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
public class CEC2011_Problem_11_5_ELD_15 extends Problem {
  private double g_constrains[]; // internal

  private static double Power_Demand = 2630; // in MW
  // % % % ============= 6 unit system data ==========
  // % Data1= [Pmin Pmax a b c];
  private static double[][] Data1 = { { 150, 455, 0.000299, 10.1, 671 }, { 150, 455, 0.000183, 10.2, 574 }, { 20, 130, 0.001126, 8.8, 374 }, { 20, 130, 0.001126, 8.8, 374 }, { 150, 470, 0.000205, 10.4, 461 }, { 135, 460, 0.000301, 10.1, 630 }, { 135, 465, 0.000364, 9.8, 548 },
      { 60, 300, 0.000338, 11.2, 227 }, { 25, 162, 0.000807, 11.2, 173 }, { 25, 160, 0.001203, 10.7, 175 }, { 20, 80, 0.003586, 10.2, 186 }, { 20, 80, 0.005513, 9.9, 230 }, { 25, 85, 0.000371, 13.1, 225 }, { 15, 55, 0.001929, 12.1, 309 }, { 15, 55, 0.004447, 12.4, 323 } };
  private static double[][] Data2 = { { 400, 80, 120, 150, 150, 150, 150, 150, 150 }, { 300, 80, 120, 185, 255, 305, 335, 420, 450 }, { 105, 130, 130, 20, 20, 20, 20, 20, 20 }, { 100, 130, 130, 20, 20, 20, 20, 20, 20 }, { 90, 80, 120, 180, 200, 305, 335, 390, 420 },
      { 400, 80, 120, 230, 255, 365, 395, 430, 455 }, { 350, 80, 120, 135, 135, 135, 135, 135, 135 }, { 95, 65, 100, 60, 60, 60, 60, 60, 60 }, { 105, 60, 100, 25, 25, 25, 25, 25, 25 }, { 110, 60, 100, 25, 25, 25, 25, 25, 25 }, { 60, 80, 80, 20, 20, 20, 20, 20, 20 },
      { 40, 80, 80, 30, 40, 55, 65, 20, 20 }, { 30, 80, 80, 25, 25, 25, 25, 25, 25 }, { 20, 55, 55, 15, 15, 15, 15, 15, 15 }, { 20, 55, 55, 15, 15, 15, 15, 15, 15 } };
  // % Loss Co-efficients
  private static double[][] B1 = { { 1.4E-5, 1.2E-5, 0.7E-5, -0.1E-5, -0.3E-5, -0.1E-5, -0.1E-5, -0.1E-5, -0.3E-5, -0.5E-5, -0.3E-5, -0.2E-5, 0.4E-5, 0.3E-5, -0.1E-5 }, { 1.2E-5, 1.5E-5, 1.3E-5, 0.0E-5, -0.5E-5, -0.2E-5, 0.0E-5, 0.1E-5, -0.2E-5, -0.4E-5, -0.4E-5, 0.0E-5, 0.4E-5, 1E-5, -0.2E-5 },
      { 0.7E-5, 1.3E-5, 7.6E-5, -0.1E-5, -1.3E-5, -0.9E-5, -0.1E-5, 0.0E-5, -0.8E-5, -1.2E-5, -1.7E-5, 0.0E-5, -2.6E-5, 11.1E-5, -2.8E-5 }, { -0.1E-5, 0.0E-5, -0.1E-5, 3.4E-5, -0.7E-5, -0.4E-5, 1.1E-5, 5.0E-5, 2.9E-5, 3.2E-5, -1.1E-5, 0.0E-5, 0.1E-5, 0.1E-5, -2.6E-5 },
      { -0.3E-5, -0.5E-5, -1.3E-5, -0.7E-5, 9.0E-5, 1.4E-5, -0.3E-5, -1.2E-5, -1.0E-5, -1.3E-5, 0.7E-5, -0.2E-5, -0.2E-5, -2.4E-5, -0.3E-5 }, { -0.1E-5, -0.2E-5, -0.9E-5, -0.4E-5, 1.4E-5, 1.6E-5, 0.0E-5, -0.6E-5, -0.5E-5, -0.8E-5, 1.1E-5, -0.1E-5, -0.2E-5, -1.7E-5, 0.3E-5 },
      { -0.1E-5, 0.0E-5, -0.1E-5, 1.1E-5, -0.3E-5, 0.0E-5, 1.5E-5, 1.7E-5, 1.5E-5, 0.9E-5, -0.5E-5, 0.7E-5, 0.0E-5, -0.2E-5, -0.8E-5 }, { -0.1E-5, 0.1E-5, 0.0, 5.0E-5, -1.2E-5, -0.6E-5, 1.7E-5, 16.8E-5, 8.2E-5, 7.9E-5, -2.3E-5, -3.6E-5, 0.1E-5, 0.5E-5, -7.8E-5 },
      { -0.3E-5, -0.2E-5, -0.8E-5, 2.9E-5, -1.0E-5, -0.5E-5, 1.5E-5, 8.2E-5, 12.9E-5, 11.6E-5, -2.1E-5, -2.5E-5, 0.7E-5, -1.2E-5, -7.2E-5 }, { -0.5E-5, -0.4E-5, -1.2E-5, 3.2E-5, -1.3E-5, -0.8E-5, 0.9E-5, 7.9E-5, 11.6E-5, 20.0E-5, -2.7E-5, -3.4E-5, 0.9E-5, -1.1E-5, -8.8E-5 },
      { -0.3E-5, -0.4E-5, -1.7E-5, -1.1E-5, 0.7E-5, 1.1E-5, -0.5E-5, -2.3E-5, -2.1E-5, -2.7E-5, 14.0E-5, 0.1E-5, 0.4E-5, -3.8E-5, 16.8E-5 }, { -0.2E-5, 0.0E-5, 0.0, 0.0E-5, -0.2E-5, -0.1E-5, 0.7E-5, -3.6E-5, -2.5E-5, -3.4E-5, 0.1E-5, 5.4E-5, -0.1E-5, -0.4E-5, 2.8E-5 },
      { 0.4E-5, 0.4E-5, -2.6E-5, 0.1E-5, -0.2E-5, -0.2E-5, 0.0E-5, 0.1E-5, 0.7E-5, 0.9E-5, 0.4E-5, -0.1E-5, 10.3E-5, -10.1E-5, 2.8E-5 }, { 0.3E-5, 1.0E-5, 11.1E-5, 0.1E-5, -2.4E-5, -1.7E-5, -0.2E-5, 0.5E-5, -1.2E-5, -1.1E-5, -3.8E-5, -0.4E-5, -10.1E-5, 57.8E-5, -9.4E-5 },
      { -0.1E-5, -0.2E-5, -2.8E-5, -2.6E-5, -0.3E-5, 0.3E-5, -0.8E-5, -7.8E-5, -7.2E-5, -8.8E-5, 16.8E-5, 2.8E-5, 2.8E-5, -9.4E-5, 128.3E-5 } };
  private static double[] B2 = { -0.1E-5, -0.2E-5, 2.8E-5, -0.1E-5, 0.1E-5, -0.3E-5, -0.2E-5, -0.2E-5, 0.6E-5, 3.9E-5, -1.7E-5, 0.0E-5, -3.2E-5, 6.7E-5, -6.4E-5 };
  private static double B3 = 5.5E-5; //
  private static int Pmin_data1_col = 0;// = Data1(:,1)';
  private static int Pmax_data1_col = 1;// = Data1(:,2)';
  private static int a_data1_col = 2;// = Data1(:,3)';
  private static int b_data1_col = 3;// = Data1(:,4)';
  private static int c_data1_col = 4;// = Data1(:,5)';
  private static int Initial_Generations_data2_col = 0;// = Data2(:,1)';
  private static int Up_Ramp_data2_col = 1;// = Data2(:,2)';
  private static int Down_Ramp_data2_col = 2;// = Data2(:,3)';
  // Up_Ramp_Limit = min(Pmax,Initial_Generations+Up_Ramp);

  public static double Up_Ramp_Limit(int i) {
    // System.out.println("Pmax "+Data1[i][Pmax_data1_col]+"
    // Initial_Generations:"+Data2[i][Initial_Generations_data2_col]+"
    // Up_Ramp:"+Data2[i][Up_Ramp_data2_col]+"
    // "+Data2[i][Initial_Generations_data2_col]+Data2[i][Up_Ramp_data2_col]);
    return Math.min(Data1[i][Pmax_data1_col], Data2[i][Initial_Generations_data2_col] + Data2[i][Up_Ramp_data2_col]);
  }

  // Down_Ramp_Limit = max(Pmin,Initial_Generations-Down_Ramp);
  public static double Down_Ramp_Limit(int i) {
    return Math.max(Data1[i][Pmin_data1_col], Data2[i][Initial_Generations_data2_col] - Data2[i][Down_Ramp_data2_col]);
  }

  private static int D = 15;
  private static int D2 = Data2[0].length;

  public static double Prohibited_Operating_Zones_POZ(int i, int j) { // y,x
    // System.out.println("D2="+D2+" len:"+Data2.length);
    return Data2[j][3 + i];
  }

  public static int No_of_POZ_Limits() {
    // System.out.println(Data2[0].length-4+1);
    return Data2[0].length - 4 + 1;
  }

  public static double POZ_Lower_Limits(int i, int j) {
    return Prohibited_Operating_Zones_POZ(i * 2, j);
  }

  public static double POZ_Upper_Limits(int i, int j) {
    return Prohibited_Operating_Zones_POZ(i * 2 + 1, j);
  }

  public CEC2011_Problem_11_5_ELD_15() {
    super(15, 4);

    lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
    upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));

    for (int z = 0; z < numberOfDimensions; z++) {
      lowerLimit.set(z, Data1[z][0]);
      upperLimit.set(z, Data1[z][1]);
    }

    name = "RWP_11_5_ELD_15";
    description = "RWP_11_5_ELD_15 Static Economic Load Dispatch (ELD) Problem ";
  }

  private double produkt(double x[], double y[]) {
    double t = 0;
    for (int i = 0; i < x.length; i++) {
      t += x[i] * y[i];
    }
    return t;
  }

  private double[] produkt2(double x[], double y[][]) {
    double t[] = new double[numberOfDimensions]; // dim??
    for (int i = 0; i < x.length; i++) {
      double tt = 0;
      for (int ii = 0; ii < x.length; ii++) {
        tt += x[i] * y[ii][i];
      }
      t[i] = tt;
    }
    return t;
  }

  protected double sum(double x[]) {
    double tt = 0;
    for (int ii = 0; ii < x.length; ii++) {
      tt += x[ii];
    }
    return tt;
  }

  @Override
  public double[] calc_constrains(List<Double> x) {
    return calc_constrains(x.stream().mapToDouble(i -> i).toArray());
  }

  public double[] calc_constrains(Double[] ds) {
    return calc_constrains(ArrayUtils.toPrimitive(ds));
  }

  public double[] calc_constrains(double x[]) {
    g_constrains = new double[numberOfConstraints];
    double Power_Loss = produkt(produkt2(x, B1), x) + produkt(B2, x) + B3;
    Power_Loss = Math.round(Power_Loss * 10000) / 10000;
    double Power_Balance_Penalty = Math.abs(Power_Demand + Power_Loss - sum(x));
    double Capacity_Limits_Penalty = 0;
    for (int i = 0; i < numberOfDimensions; i++) { // Capacity_Limits_Penalty = sum(abs(x-Pmin)-(x-Pmin)) +
                                                   // sum(abs(Pmax-x)-(Pmax-x));
      Capacity_Limits_Penalty += Math.abs(x[i] - Data1[i][Pmin_data1_col]) - (x[i] - Data1[i][Pmin_data1_col]); // sum(abs(x-Pmin)-(x-Pmin))
      Capacity_Limits_Penalty += Math.abs(Data1[i][Pmax_data1_col] - x[i]) - (Data1[i][Pmax_data1_col] - x[i]); // sum(abs(Pmax-x)-(Pmax-x))
    }

    double Ramp_Limits_Penalty = 0;
    for (int i = 0; i < numberOfDimensions; i++) { // Ramp_Limits_Penalty = sum(abs(x-Down_Ramp_Limit)-(x-Down_Ramp_Limit)) +
                                                   // sum(abs(Up_Ramp_Limit-x)-(Up_Ramp_Limit-x));
      Ramp_Limits_Penalty += Math.abs(x[i] - Down_Ramp_Limit(i)) - (x[i] - Down_Ramp_Limit(i)); // sum(abs(x-Down_Ramp_Limit)-(x-Down_Ramp_Limit))
      Ramp_Limits_Penalty += Math.abs(Up_Ramp_Limit(i) - x[i]) - (Up_Ramp_Limit(i) - x[i]); // sum(abs(Up_Ramp_Limit-x)-(Up_Ramp_Limit-x));
    }
    // System.out.println("Ramp_Limits_Penalty="+Ramp_Limits_Penalty);
    double POZ_Penalty = 0;
    // temp_x = repmat(x,No_of_POZ_Limits/2,1);
    // POZ_Penalty = sum(sum((POZ_Lower_Limits<temp_x &
    // temp_x<POZ_Upper_Limits).*min(temp_x-POZ_Lower_Limits,POZ_Upper_Limits-temp_x)));
    for (int j = 0; j < No_of_POZ_Limits() / 2; j++) {
      for (int i = 0; i < numberOfDimensions; i++) {
        if ((POZ_Lower_Limits(j, i) < x[i]) && (x[i] < POZ_Upper_Limits(j, i))) {
          POZ_Penalty += Math.min(x[i] - POZ_Lower_Limits(j, i), POZ_Upper_Limits(j, i) - x[i]);
        }
      }
    }
    // System.out.println("POZ_Penalty 14="+POZ_Penalty);
    double Total_Penalty = 1e3 * Power_Balance_Penalty + 1e3 * Capacity_Limits_Penalty + 1e5 * Ramp_Limits_Penalty + 1e5 * POZ_Penalty;
    g_constrains[0] = 1e3 * Power_Balance_Penalty;
    g_constrains[1] = 1e3 * Capacity_Limits_Penalty;
    g_constrains[2] = 1e5 * Ramp_Limits_Penalty;
    g_constrains[3] = 1e5 * POZ_Penalty;

    return g_constrains; // do I need deep copy?
  }

  public double eval(double x[]) {
    /*
     * double Power_Loss = produkt(produkt2(x, B1),x) + produkt(B2, x) + B3;
     * Power_Loss = Math.round(Power_Loss *10000)/10000; double
     * Power_Balance_Penalty = Math.abs(Power_Demand + Power_Loss - sum(x)); double
     * Capacity_Limits_Penalty=0; for (int i=0; i<dim; i++) {
     * //Capacity_Limits_Penalty = sum(abs(x-Pmin)-(x-Pmin)) +
     * sum(abs(Pmax-x)-(Pmax-x));
     * Capacity_Limits_Penalty+=Math.abs(x[i]-Data1[i][Pmin_data1_col])-(x[i]-Data1[
     * i][Pmin_data1_col]); //sum(abs(x-Pmin)-(x-Pmin))
     * Capacity_Limits_Penalty+=Math.abs(Data1[i][Pmax_data1_col]-x[i])-(Data1[i][
     * Pmax_data1_col]-x[i]); //sum(abs(Pmax-x)-(Pmax-x)) } //
     * System.out.println("Capacity_Limits_Penalty ="+Capacity_Limits_Penalty);
     * 
     * //%%% Ramp Rate Limits Penalty Calculation
     * 
     * double Ramp_Limits_Penalty=0; for (int i=0; i<dim; i++) { //
     * Ramp_Limits_Penalty = sum(abs(x-Down_Ramp_Limit)-(x-Down_Ramp_Limit)) +
     * sum(abs(Up_Ramp_Limit-x)-(Up_Ramp_Limit-x));
     * Ramp_Limits_Penalty+=Math.abs(x[i]-Down_Ramp_Limit(i))-(x[i]-Down_Ramp_Limit(
     * i)); //sum(abs(x-Down_Ramp_Limit)-(x-Down_Ramp_Limit))
     * Ramp_Limits_Penalty+=Math.abs(Up_Ramp_Limit(i)-x[i])-(Up_Ramp_Limit(i)-x[i]);
     * //sum(abs(Up_Ramp_Limit-x)-(Up_Ramp_Limit-x)); } //
     * System.out.println("Ramp_Limits_Penalty="+Ramp_Limits_Penalty); double
     * POZ_Penalty=0; //temp_x = repmat(x,No_of_POZ_Limits/2,1); //POZ_Penalty =
     * sum(sum((POZ_Lower_Limits<temp_x &
     * temp_x<POZ_Upper_Limits).*min(temp_x-POZ_Lower_Limits,POZ_Upper_Limits-temp_x
     * ))); for (int j=0; j<No_of_POZ_Limits()/2; j++) { for (int i=0; i<dim; i++) {
     * if ((POZ_Lower_Limits(j,i)<x[i]) && (x[i]<POZ_Upper_Limits(j,i))) {
     * POZ_Penalty+=Math.min(x[i]-POZ_Lower_Limits(j, i), POZ_Upper_Limits(j,
     * i)-x[i]); } } } // System.out.println("POZ_Penalty 14="+POZ_Penalty); double
     * Total_Penalty = 1e3*Power_Balance_Penalty + 1e3*Capacity_Limits_Penalty +
     * 1e5*Ramp_Limits_Penalty + 1e5*POZ_Penalty; g_constrains[0] =
     * 1e3*Power_Balance_Penalty; g_constrains[1] = 1e3*Capacity_Limits_Penalty;
     * g_constrains[2] = 1e5*Ramp_Limits_Penalty ; g_constrains[3] = 1e5*POZ_Penalty
     * ;
     */
    // Cost(i,1) = sum( a.*(x.^2) + b.*x + c );
    double f = 0;
    for (int i = 0; i < numberOfDimensions; i++) {
      f += Data1[i][a_data1_col] * x[i] * x[i] + Data1[i][b_data1_col] * x[i] + Data1[i][c_data1_col];
    }
    // System.out.println("f:"+f+" Total_Penalty:"+Total_Penalty);

    return f;
  }
}