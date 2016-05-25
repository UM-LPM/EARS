package org.um.feri.ears.util;

public class TrapezoidalRule {

   public static double integrate(int size, double[] x, double[] y)
   {  
	   double sum = 0.0, increment;

	   for ( int k = 1; k < size; k++ )
	   {//Trapezoid rule:  1/2 h * (f0 + f1)
		   increment = 0.5 * (x[k]-x[k-1]) * (y[k]+y[k-1]);
		   sum += increment;
	   }
	   return sum;
   }
}
