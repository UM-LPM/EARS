package org.um.feri.ears.statistic.friedman;

public class CDF_Normal extends Object {

   public static double xnormi(double p) {

      double arg,t,t2,t3,xnum,xden,qinvp,x,pc;

      final double c[] = {2.515517,
      .802853,
      .010328};

      final double d[] = {1.432788,
      .189269,
      .001308};

      if (p <= .5) {
    	  
         arg = -2.0*Math.log(p);
         t = Math.sqrt(arg);
         t2 = t*t;
         t3 = t2*t;

         xnum = c[0] + c[1]*t + c[2]*t2;
         xden = 1.0 + d[0]*t + d[1]*t2 + d[2]*t3;
         qinvp = t - xnum/xden;
         x = -qinvp;

         return x;
         
      }else{
    	  
         pc = 1.0 - p;
         arg = -2.0*Math.log(pc);
         t = Math.sqrt(arg);
         t2 = t*t;
         t3 = t2*t;

         xnum = c[0] + c[1]*t + c[2]*t2;
         xden = 1.0 + d[0]*t + d[1]*t2 + d[2]*t3;
         x = t - xnum/xden;

         return x;

      }

   }

   public static double normp(double z) {

      double zabs;
      double p;
      double expntl,pdf;

      final double p0 = 220.2068679123761;
      final double p1 = 221.2135961699311;
      final double p2 = 112.0792914978709;
      final double p3 = 33.91286607838300;
      final double p4 = 6.373962203531650;
      final double p5 = .7003830644436881;
      final double p6 = .3526249659989109E-01;

      final double q0 = 440.4137358247522;
      final double q1 = 793.8265125199484;
      final double q2 = 637.3336333788311;
      final double q3 = 296.5642487796737;
      final double q4 = 86.78073220294608;
      final double q5 = 16.06417757920695;
      final double q6 = 1.755667163182642;
      final double q7 = .8838834764831844E-1;

      final double cutoff = 7.071;
      final double root2pi = 2.506628274631001;

      zabs = Math.abs(z);

//  |z| > 37

      if (z > 37.0) {
         p = 1.0;
         return p;
      }
      if (z < -37.0) {
         p = 0.0;
         return p;
      }

//  |z| <= 37.

      expntl = Math.exp(-.5*zabs*zabs);
      pdf = expntl/root2pi;

//  |z| < cutoff = 10/sqrt(2).

      if (zabs < cutoff) {
         p = expntl*((((((p6*zabs + p5)*zabs + p4)*zabs + p3)*zabs +
             p2)*zabs + p1)*zabs + p0)/(((((((q7*zabs + q6)*zabs +
             q5)*zabs + q4)*zabs + q3)*zabs + q2)*zabs + q1)*zabs +
             q0);
      }else{
         p = pdf/(zabs + 1.0/(zabs + 2.0/(zabs + 3.0/(zabs + 4.0/
             (zabs + 0.65)))));
      }

      if (z < 0.0) {
         return p;
      }else{
         p = 1.0 - p;
         return p;
      }
   }
}
