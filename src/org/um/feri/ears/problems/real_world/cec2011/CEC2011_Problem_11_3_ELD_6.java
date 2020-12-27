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
public class CEC2011_Problem_11_3_ELD_6 extends Problem {
  private double g_constrains[]; // internal
  /*
   * 6 unit limits (in format [LB1, UB1; LB2, UB2; ....]): [100, 500;50, 200;80,
   * 300;50, 150; 50, 200;50, 120;];
   * 
   * Inequality constraints
   * 
   * ELD Instance 1
   * 
   * function [Total_Cost Cost Total_Penalty] = fn_ELD_6(Input_Population,Display)
   * %% DATA REQUIRED [Pop_Size No_of_Units] = size(Input_Population);
   * Power_Demand = 1263; %% in MW
   * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% %
   * % % ============= 6 unit system data ========== % Data1= [Pmin Pmax a b c];
   * Data1=[ 100 500 0.0070 7.0 240; 50 200 0.0095 10.0 200; 80 300 0.0090 8.5
   * 220; 50 150 0.0090 11.0 200; 50 200 0.0080 10.5 220; 50 120 0.0075 12.0
   * 190;]; % Data2=[Po UR DR Zone1min Zone1max Zone2min Zone2max]; Data2=[ 440 80
   * 120 210 240 350 380; 170 50 90 90 110 140 160; 200 65 100 150 170 210 240;
   * 150 50 90 80 90 110 120; 190 50 90 90 110 140 150; 150 50 90 75 85 100 105;];
   * % Loss Co-efficients B1=[ 1.7 1.2 0.7 -0.1 -0.5 -0.2; 1.2 1.4 0.9 0.1 -0.6
   * -0.1; 0.7 0.9 3.1 0.0 -1.0 -0.6; -0.1 0.1 0.0 0.24 -0.6 -0.8; -0.5 -0.6 -0.1
   * -0.6 12.9 -0.2; 0.2 -0.1 -0.6 -0.8 -0.2 15.0;]; B1=B1.*10^-5; B2=[-0.3908
   * -0.1297 0.7047 0.0591 0.2161 -0.6635].*10^-5; B3=0.0056*10^-2;
   * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% %%
   * INITIALIZATIONS Pmin = Data1(:,1)'; Pmax = Data1(:,2)'; a = Data1(:,3)'; b =
   * Data1(:,4)'; c = Data1(:,5)'; Initial_Generations = Data2(:,1)'; Up_Ramp =
   * Data2(:,2)'; Down_Ramp = Data2(:,3)'; Up_Ramp_Limit =
   * min(Pmax,Initial_Generations+Up_Ramp); Down_Ramp_Limit =
   * max(Pmin,Initial_Generations-Down_Ramp); Prohibited_Operating_Zones_POZ =
   * Data2(:,4:end)';
   * 
   * 440 80 120 210 240 350 380 170 50 90 90 110 140 160 200 65 100 150 170 210
   * 240 150 50 90 80 90 110 120 190 50 90 90 110 140 150 150 50 90 75 85 100 105
   * 
   * 210 90 150 80 90 75 240 110 170 90 110 85 350 140 210 110 140 100 380 160 240
   * 120 150 105
   * 
   * No_of_POZ_Limits = size(Prohibited_Operating_Zones_POZ,1); POZ_Lower_Limits =
   * Prohibited_Operating_Zones_POZ(1:2:No_of_POZ_Limits,:); 210 90 150 80 90 75
   * 350 140 210 110 140 100
   * 
   * POZ_Upper_Limits = Prohibited_Operating_Zones_POZ(2:2:No_of_POZ_Limits,:);
   * 240 110 170 90 110 85 380 160 240 120 150 105
   * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% %%
   * CALCULATIONS for i = 1:Pop_Size x = Input_Population(i,:); Power_Loss =
   * (x*B1*x') + (B2*x') + B3; Power_Loss = round(Power_Loss *10000)/10000; %%%
   * Power Balance Penalty Calculation Power_Balance_Penalty = abs(Power_Demand +
   * Power_Loss - sum(x)); %%% Capacity Limits Penalty Calculation
   * Capacity_Limits_Penalty = sum(abs(x-Pmin)-(x-Pmin)) +
   * sum(abs(Pmax-x)-(Pmax-x)); %%% Ramp Rate Limits Penalty Calculation
   * Ramp_Limits_Penalty = sum(abs(x-Down_Ramp_Limit)-(x-Down_Ramp_Limit)) +
   * sum(abs(Up_Ramp_Limit-x)-(Up_Ramp_Limit-x)); %%% Prohibited Operating Zones
   * Penalty Calculation temp_x = repmat(x,No_of_POZ_Limits/2,1); POZ_Penalty =
   * sum(sum((POZ_Lower_Limits<temp_x &
   * temp_x<POZ_Upper_Limits).*min(temp_x-POZ_Lower_Limits,POZ_Upper_Limits-temp_x
   * ))); %%% Total Penalty Calculation Total_Penalty(i,1) =
   * 1e3*Power_Balance_Penalty + 1e3*Capacity_Limits_Penalty +
   * 1e5*Ramp_Limits_Penalty + 1e5*POZ_Penalty; %%% Cost Calculation Cost(i,1) =
   * sum( a.*(x.^2) + b.*x + c ); Total_Cost(i,1) = Cost(i,1) +
   * Total_Penalty(i,1);
   * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% if
   * (nargin>1)
   * disp('-----------------------------------------------------------------------
   * -----'); disp(sprintf('6 UNIT SYSTEM')); disp(sprintf('Power_Loss : %17.8f
   * ',Power_Loss)); disp(sprintf('Total_Power_Generation : %17.8f ',sum(x)));
   * disp(sprintf('Power_Balance_Penalty : %17.8f ',Power_Balance_Penalty));
   * disp(sprintf('Capacity_Limits_Penalty : %17.8f ',Capacity_Limits_Penalty ));
   * disp(sprintf('Ramp_Limits_Penalty : %17.8f ',Ramp_Limits_Penalty));
   * disp(sprintf('POZ_Penalty : %17.8f ',POZ_Penalty)); disp(sprintf('Cost :
   * %17.8f ',Cost(i,1))); disp(sprintf('Total_Penalty : %17.8f
   * ',Total_Penalty(i,1))); disp(sprintf('Total_Objective_Value : %17.8f
   * ',Total_Cost(i,1)));
   * disp('-----------------------------------------------------------------------
   * -----'); end
   * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
   * end end
   * 
   */

  private static double Power_Demand = 1263; // in MW
  // % % % ============= 6 unit system data ==========
  // % Data1= [Pmin Pmax a b c];
  private static double[][] Data1 = { { 100, 500, 0.0070, 7.0, 240 }, { 50, 200, 0.0095, 10.0, 200 }, { 80, 300, 0.0090, 8.5, 220 }, { 50, 150, 0.0090, 11.0, 200 }, { 50, 200, 0.0080, 10.5, 220 }, { 50, 120, 0.0075, 12.0, 190 } };
  // % Data2=[Po UR DR Zone1min Zone1max Zone2min Zone2max];
  private static double[][] Data2 = { { 440, 80, 120, 210, 240, 350, 380 }, { 170, 50, 90, 90, 110, 140, 160 }, { 200, 65, 100, 150, 170, 210, 240 }, { 150, 50, 90, 80, 90, 110, 120 }, { 190, 50, 90, 90, 110, 140, 150 }, { 150, 50, 90, 75, 85, 100, 105 } };

  // % Loss Co-efficients
  private static double[][] B1 = { { 1.7E-5, 1.2E-5, 0.7E-5, -0.1E-5, -0.5E-5, -0.2E-5 }, { 1.2E-5, 1.4E-5, 0.9E-5, 0.1E-5, -0.6E-5, -0.1E-5 }, { 0.7E-5, 0.9E-5, 3.1E-5, 0.0E-5, -1.0E-5, -0.6E-5 }, { -0.1E-5, 0.1E-5, 0.0E-5, 0.24E-5, -0.6E-5, -0.8E-5 },
      { -0.5E-5, -0.6E-5, -0.1E-5, -0.6E-5, 12.9E-5, -0.2E-5 }, { 0.2E-5, -0.1E-5, -0.6E-5, -0.8E-5, -0.2E-5, 15.0E-5 } };
  private static double[] B2 = { -0.3908E-5, -0.1297E-5, 0.7047E-5, 0.0591E-5, 0.2161E-5, -0.6635E-5 };
  // B3=0.0056*10^-2; ??? != 0.0056E-2
  private static double B3 = 0.56 * 10E-5; // 5.6E-5!
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

  private static int D = 6;

  /*
   * Prohibited_Operating_Zones_POZ = Data2(:,4:end)';
   * 
   * 440 80 120 210 240 350 380 170 50 90 90 110 140 160 200 65 100 150 170 210
   * 240 150 50 90 80 90 110 120 190 50 90 90 110 140 150 150 50 90 75 85 100 105
   * 
   * 210 90 150 80 90 75 240 110 170 90 110 85 350 140 210 110 140 100 380 160 240
   * 120 150 105
   */
  public static double Prohibited_Operating_Zones_POZ(int i, int j) {
    // System.out.println(i+","+j+"->"+(D-4+j)+","+i);
    // return Data2[j][D-(4-1)+i];
    return Data2[j][3 + i];
  }

  public static int No_of_POZ_Limits() {
    return Data2[0].length - 4 + 1;
  }

  /*
   * POZ_Lower_Limits = Prohibited_Operating_Zones_POZ(1:2:No_of_POZ_Limits,:);
   * 210 90 150 80 90 75 350 140 210 110 140 100
   */
  public static double POZ_Lower_Limits(int i, int j) {
    return Prohibited_Operating_Zones_POZ(i * 2, j);
  }

  /*
   * POZ_Upper_Limits = Prohibited_Operating_Zones_POZ(2:2:No_of_POZ_Limits,:);
   * 240 110 170 90 110 85 380 160 240 120 150 105
   */
  public static double POZ_Upper_Limits(int i, int j) {
    return Prohibited_Operating_Zones_POZ(i * 2 + 1, j);
  }

  public CEC2011_Problem_11_3_ELD_6() {
    super(6, 4);

    g_constrains = new double[numberOfConstraints];

    lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
    upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));

    for (int z = 0; z < numberOfDimensions; z++) {
      lowerLimit.set(z, Data1[z][0]);
      upperLimit.set(z, Data1[z][1]);
    }

    // [100, 500;50, 200;80, 300;50, 150; 50, 200;50, 120;];

    name = "RWP_11_3_ELD_6";
    description = "RWP_11_3_ELD_6 Static Economic Load Dispatch (ELD) Problem ";
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

  public double[] evaluateConstrains(double x[]) {
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
    // System.out.println("Capacity_Limits_Penalty ="+Capacity_Limits_Penalty);

    // %%% Ramp Rate Limits Penalty Calculation

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