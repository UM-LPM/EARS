/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.um.feri.ears.problems.unconstrained.cec;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author QIN
 */
public final class Functions {

  
    //double ellips_func (double[] , double , int , double[] ,double[] ,int ,int) /* Ellipsoidal */
    //double bent_cigar_func (double[] , double , int , double[] ,double[] ,int ,int) /* Bent_Cigar */
    //double discus_func (double[] , double , int , double[] ,double[] ,int ,int) /* Discus */
    //double rosenbrock_func (double[] , double , int , double[] ,double[] ,int ,int) /* Rosenbrock's */
    //double ackley_func (double[] , double , int , double[] ,double[] ,int ,int) /* Ackley's  */
    //double rastrigin_func (double[] , double , int , double[] ,double[] ,int ,int) /* Rastrigin's  */
    //double weierstrass_func (double[] , double , int , double[] ,double[] ,int ,int) /* Weierstrass's  */
    //double griewank_func (double[] , double , int , double[] ,double[] ,int ,int) /* Griewank's  */
    //double schwefel_func (double[] , double , int , double[] ,double[] ,int ,int) /* Schwefel's  */
    //double katsuura_func (double[] , double , int , double[] ,double[] ,int ,int) /* Katsuura  */
    //double grie_rosen_func (double[] , double , int , double[] ,double[] ,int ,int) /* Griewank-Rosenbrock  */
    //double escaffer6_func (double[] , double , int , double[] ,double[] ,int ,int) /* Expanded Scaffer¡¯s
    //double happycat_func (double[] , double , int , double[] ,double[] ,int ,int) /* HappyCat */
    //double hgbat_func (double[] , double , int , double[] ,double[] ,int ,int) /* HGBat  */
	//double hf01 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 1 */
    //double hf02 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 2 */
    //double hf03 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 3 */
    //double hf04 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 4 */
    //double hf05 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 5 */
    //double hf06 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 6 */
	//double cf01 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 1 */
    //double cf02 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 2 */
    //double cf03 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 3 */
    //double cf04 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 4 */
    //double cf05 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 5 */
    //double cf06 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 6 */
    //double cf07 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 7 */
    //double cf08 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 8 */
	//void shiftfunc (double[] , double[] , int ,double[] )
    //void rotatefunc (double[] , double[] , int ,double[] )
    //void sr_func (double[] .double[] ,int ,double[] ,double[] ,double ,int ,int )/* shift and rotate*/
    //void asyfunc (double[] , double[] , int , double )
    //void oszfunc (double[] , double[] , int )
    //double cf_cal(double[] , double , int , double[] ,double[] ,double[] ,double[] , int )
    public final static double ellips_func(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Ellipsoidal */ {
        int i;
        double f = 0.0;
        double [] z =ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/
        for (i = 0; i < nx; i++) {
            f += Math.pow(10.0, 6.0 * i / (nx - 1)) * z[i] * z[i];
        }
        return f;
    }
    
	public static double ellips_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Ellipsoidal */ {
        int i;
        double f = 0.0;
        double [] z =ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/
        for (i = 0; i < nx; i++) {
            f += Math.pow(10.0, 6.0 * i / (nx - 1)) * z[i] * z[i];
        }
        return f;
    }
	
    public final static double sphere_func(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Sphere */ {
        int i;
        double f = 0.0;
        double [] z =ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

        for (i = 0; i < nx; i++) {
            f += z[i] * z[i];
        }
        return f;
    }
    
    public final static double sphere_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Sphere */ {
        int i;
        double f = 0.0;
        double [] z =ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

        for (i = 0; i < nx; i++) {
            f += z[i] * z[i];
        }
        return f;
    }

    public final static double bent_cigar_func(double[] x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Bent_Cigar */ {
        int i;
        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

        double f = z[0] * z[0];
        for (i = 1; i < nx; i++) {
            f += Math.pow(10.0, 6.0) * z[i] * z[i];
        }
        return f;
    }
    
	public static double bent_cigar_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Bent_Cigar */ {
		
		int i;
		double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

		double f = z[0] * z[0];
		for (i = 1; i < nx; i++) {
			f += Math.pow(10.0, 6.0) * z[i] * z[i];
		}
		return f;
	}

    public final static double discus_func(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Discus */ {
        int i;
        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

        double f = Math.pow(10.0, 6.0) * z[0] * z[0];
        for (i = 1; i < nx; i++) {
            f += z[i] * z[i];
        }

        return f;
    }
    
	public static double discus_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Discus */ {

        int i;
        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

        double f = Math.pow(10.0, 6.0) * z[0] * z[0];
        for (i = 1; i < nx; i++) {
            f += z[i] * z[i];
        }

        return f;
	}

    public final static double rosenbrock_func(double[] x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Rosenbrock's */ {
        int i;
        double tmp1, tmp2;
        double f = 0.0;
        double [] z = ShiftRotation.sr_func(x,  nx, Os, Mr, 2.048 / 100.0, s_flag, r_flag);/*shift and rotate*/

        z[0] += 1.0; //shift to origin
        for (i = 0; i < nx - 1; i++) {
            z[i + 1] += 1.0; //shift to orgin
            tmp1 = z[i] * z[i] - z[i + 1];
            tmp2 = z[i] - 1.0;
            f += 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
        }

        return f;
    }
    
    public static double rosenbrock_func(List<Double> x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Rosenbrock's */ {
        int i;
        double tmp1, tmp2;
        double f = 0.0;
        double [] z = ShiftRotation.sr_func(x,  nx, Os, Mr, 2.048 / 100.0, s_flag, r_flag);/*shift and rotate*/

        z[0] += 1.0; //shift to origin
        for (i = 0; i < nx - 1; i++) {
            z[i + 1] += 1.0; //shift to orgin
            tmp1 = z[i] * z[i] - z[i + 1];
            tmp2 = z[i] - 1.0;
            f += 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
        }

        return f;
    }

    public final static double ackley_func(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Ackley's  */ {
        int i;
        double sum1, sum2;
        sum1 = 0.0;
        sum2 = 0.0;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

        for (i = 0; i < nx; i++) {
            sum1 += z[i] * z[i];
            sum2 += Math.cos(2.0 * Constants.PI * z[i]);
        }
        sum1 = -0.2 * Math.sqrt(sum1 / nx);
        sum2 /= nx;
        return Constants.E - 20.0 * Math.exp(sum1) - Math.exp(sum2) + 20.0;

    }
    
	public static double ackley_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Ackley's  */ {
        int i;
        double sum1, sum2;
        sum1 = 0.0;
        sum2 = 0.0;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);/*shift and rotate*/

        for (i = 0; i < nx; i++) {
            sum1 += z[i] * z[i];
            sum2 += Math.cos(2.0 * Constants.PI * z[i]);
        }
        sum1 = -0.2 * Math.sqrt(sum1 / nx);
        sum2 /= nx;
        return Constants.E - 20.0 * Math.exp(sum1) - Math.exp(sum2) + 20.0;

    }

    public final static double weierstrass_func(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Weierstrass's  */ {
        int i, j, k_max;
        double sum, sum2 = 0, a, b;

        double [] z = ShiftRotation.sr_func(x,  nx, Os, Mr, 0.5 / 100.0, s_flag, r_flag);/*shift and rotate*/


        a = 0.5;
        b = 3.0;
        k_max = 20;
        double f = 0.0;
        for (i = 0; i < nx; i++) {
            sum = 0.0;
            sum2 = 0.0;
            for (j = 0; j <= k_max; j++) {
                sum += Math.pow(a, j) * Math.cos(2.0 * Constants.PI * Math.pow(b, j) * (z[i] + 0.5));
                sum2 += Math.pow(a, j) * Math.cos(2.0 * Constants.PI * Math.pow(b, j) * 0.5);
            }
            f += sum;
        }
        f -= nx * sum2;

        return f;
    }
    

	public static double weierstrass_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Weierstrass's  */ {
        int i, j, k_max;
        double sum, sum2 = 0, a, b;

        double [] z = ShiftRotation.sr_func(x,  nx, Os, Mr, 0.5 / 100.0, s_flag, r_flag);/*shift and rotate*/


        a = 0.5;
        b = 3.0;
        k_max = 20;
        double f = 0.0;
        for (i = 0; i < nx; i++) {
            sum = 0.0;
            sum2 = 0.0;
            for (j = 0; j <= k_max; j++) {
                sum += Math.pow(a, j) * Math.cos(2.0 * Constants.PI * Math.pow(b, j) * (z[i] + 0.5));
                sum2 += Math.pow(a, j) * Math.cos(2.0 * Constants.PI * Math.pow(b, j) * 0.5);
            }
            f += sum;
        }
        f -= nx * sum2;

        return f;
	}

    public final static double griewank_func(double[] x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Griewank's  */ {
        double s, p;

        double []z = ShiftRotation.sr_func(x, nx, Os, Mr, 600.0 / 100.0, s_flag, r_flag);/*shift and rotate*/

        s = 0.0;
        p = 1.0;
        for (int i = 0; i < nx; i++) {
            s += z[i] * z[i];
            p *= Math.cos(z[i] / Math.sqrt(1.0 + i));
        }
        return 1.0 + s / 4000.0 - p;
    }
    
	public static double griewank_func(List<Double> x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Griewank's  */ {
        double s, p;

        double []z = ShiftRotation.sr_func(x, nx, Os, Mr, 600.0 / 100.0, s_flag, r_flag);/*shift and rotate*/

        s = 0.0;
        p = 1.0;
        for (int i = 0; i < nx; i++) {
            s += z[i] * z[i];
            p *= Math.cos(z[i] / Math.sqrt(1.0 + i));
        }
        return 1.0 + s / 4000.0 - p;
    }

    public final static double rastrigin_func(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Rastrigin's  */ {
        double f = 0.0;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 5.12 / 100.0, s_flag, r_flag);/*shift and rotate*/ 

        for (int i = 0; i < nx; i++) {
            f += (z[i] * z[i] - 10.0 * Math.cos(2.0 * Constants.PI * z[i]) + 10.0);
        }

        return f;
    }
    
    public static double rastrigin_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Rastrigin's  */ {
        double f = 0.0;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 5.12 / 100.0, s_flag, r_flag);/*shift and rotate*/

        for (int i = 0; i < nx; i++) {
            f += (z[i] * z[i] - 10.0 * Math.cos(2.0 * Constants.PI * z[i]) + 10.0);
        }

        return f;
    }

    public final static double schwefel_func(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Schwefel's  */ {
        double tmp;

        double [] z = ShiftRotation.sr_func(x,  nx, Os, Mr, 1000.0 / 100.0, s_flag, r_flag);/*shift and rotate*/


        double f = 0.0;
        for (int i = 0; i < nx; i++) {
            z[i] += 4.209687462275036e+002;
            if (z[i] > 500) {
                f -= (500.0 - (z[i] % 500)) * Math.sin(Math.pow(500.0 - (z[i] % 500), 0.5));
                tmp = (z[i] - 500.0) / 100;
                f += tmp * tmp / nx;
            } else if (z[i] < -500) {
                f -= (-500.0 + (Math.abs(z[i]) % 500)) * Math.sin(Math.pow(500.0 - (Math.abs(z[i]) % 500), 0.5));
                tmp = (z[i] + 500.0) / 100;
                f += tmp * tmp / nx;
            } else {
                f -= z[i] * Math.sin(Math.pow(Math.abs(z[i]), 0.5));
            }
        }
        f = 4.189828872724338e+002 * nx + f;

        return f;
    }
    
	public static double schwefel_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Schwefel's  */ {
        double tmp;

        double [] z = ShiftRotation.sr_func(x,  nx, Os, Mr, 1000.0 / 100.0, s_flag, r_flag);/*shift and rotate*/


        double f = 0.0;
        for (int i = 0; i < nx; i++) {
            z[i] += 4.209687462275036e+002;
            if (z[i] > 500) {
                f -= (500.0 - (z[i] % 500)) * Math.sin(Math.pow(500.0 - (z[i] % 500), 0.5));
                tmp = (z[i] - 500.0) / 100;
                f += tmp * tmp / nx;
            } else if (z[i] < -500) {
                f -= (-500.0 + (Math.abs(z[i]) % 500)) * Math.sin(Math.pow(500.0 - (Math.abs(z[i]) % 500), 0.5));
                tmp = (z[i] + 500.0) / 100;
                f += tmp * tmp / nx;
            } else {
                f -= z[i] * Math.sin(Math.pow(Math.abs(z[i]), 0.5));
            }
        }
        f = 4.189828872724338e+002 * nx + f;

        return f;
	}

    public final static double katsuura_func(double[] x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Katsuura  */ {
        double temp, tmp1, tmp2, tmp3;
        tmp3 = Math.pow(1.0 * nx, 1.2);

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 5 / 100.0, s_flag, r_flag);/*shift and rotate*/


        double f = 1.0;
        for (int i = 0; i < nx; i++) {
            temp = 0.0;
            for (int j = 1; j <= 32; j++) {
                tmp1 = Math.pow(2.0, j);
                tmp2 = tmp1 * z[i];
                temp += Math.abs(tmp2 - Math.floor(tmp2 + 0.5)) / tmp1;
            }
            f *= Math.pow(1.0 + (i + 1) * temp, 10.0 / tmp3);
        }
        tmp1 = 10.0 / nx / nx;
        f = f * tmp1 - tmp1;

        return f;

    }
    
	public static double katsuura_func(List<Double> x,  int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Katsuura  */ {
        double temp, tmp1, tmp2, tmp3;
        tmp3 = Math.pow(1.0 * nx, 1.2);

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 5 / 100.0, s_flag, r_flag);/*shift and rotate*/


        double f = 1.0;
        for (int i = 0; i < nx; i++) {
            temp = 0.0;
            for (int j = 1; j <= 32; j++) {
                tmp1 = Math.pow(2.0, j);
                tmp2 = tmp1 * z[i];
                temp += Math.abs(tmp2 - Math.floor(tmp2 + 0.5)) / tmp1;
            }
            f *= Math.pow(1.0 + (i + 1) * temp, 10.0 / tmp3);
        }
        tmp1 = 10.0 / nx / nx;
        f = f * tmp1 - tmp1;

        return f;
	}

    public final static double happycat_func(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /*HappyCat, probided by Hans-Georg Beyer (HGB)*/ /*original global optimum: [-1,-1,...,-1]*/ {
        int i;
        double alpha, r2, sum_z;
        alpha = 1.0 / 8.0;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 5 / 100.0, s_flag, r_flag);/*shift and rotate*/

        r2 = 0.0;
        sum_z = 0.0;
        for (i = 0; i < nx; i++) {
            z[i] = z[i] - 1.0; //shift to orgin
            r2 += z[i] * z[i];
            sum_z += z[i];

        }
       return Math.pow(Math.abs(r2 - nx), 2 * alpha) + (0.5 * r2 + sum_z) / nx + 0.5;

    }
    
	public static double happycat_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /*HappyCat, probided by Hans-Georg Beyer (HGB)*/ /*original global optimum: [-1,-1,...,-1]*/ {
        int i;
        double alpha, r2, sum_z;
        alpha = 1.0 / 8.0;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 5 / 100.0, s_flag, r_flag);/*shift and rotate*/

        r2 = 0.0;
        sum_z = 0.0;
        for (i = 0; i < nx; i++) {
            z[i] = z[i] - 1.0; //shift to orgin
            r2 += z[i] * z[i];
            sum_z += z[i];

        }
       return Math.pow(Math.abs(r2 - nx), 2 * alpha) + (0.5 * r2 + sum_z) / nx + 0.5;
	}

    public final static double hgbat_func(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /*HGBat, provided by Hans-Georg Beyer (HGB)*/ /*original global optimum: [-1,-1,...-1]*/ {
        double alpha, r2, sum_z;
        alpha = 1.0 / 4.0;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 5.0 / 100.0, s_flag, r_flag); /* shift and rotate */

        r2 = 0.0;
        sum_z = 0.0;
        for (int i = 0; i < nx; i++) {
            z[i] = z[i] - 1.0;//shift to orgin
            r2 += z[i] * z[i];
            sum_z += z[i];
        }
        return Math.pow(Math.abs(Math.pow(r2, 2.0) - Math.pow(sum_z, 2.0)), 2 * alpha) + (0.5 * r2 + sum_z) / nx + 0.5;
    }
    
	public static double hgbat_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /*HGBat, provided by Hans-Georg Beyer (HGB)*/ /*original global optimum: [-1,-1,...-1]*/ {
        double alpha, r2, sum_z;
        alpha = 1.0 / 4.0;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 5.0 / 100.0, s_flag, r_flag); /* shift and rotate */

        r2 = 0.0;
        sum_z = 0.0;
        for (int i = 0; i < nx; i++) {
            z[i] = z[i] - 1.0;//shift to orgin
            r2 += z[i] * z[i];
            sum_z += z[i];
        }
        return Math.pow(Math.abs(Math.pow(r2, 2.0) - Math.pow(sum_z, 2.0)), 2 * alpha) + (0.5 * r2 + sum_z) / nx + 0.5;
	}

    public final static double grie_rosen_func(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Griewank-Rosenbrock  */ {
        double temp, tmp1, tmp2;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 5.0 / 100.0, s_flag, r_flag); /* shift and rotate */


        double f = 0.0;

        z[0] += 1.0; //shift to orgin
        for (int i = 0; i < nx - 1; i++) {
            z[i + 1] += 1.0; //shift to orgin
            tmp1 = z[i] * z[i] - z[i + 1];
            tmp2 = z[i] - 1.0;
            temp = 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
            f += (temp * temp) / 4000.0 - Math.cos(temp) + 1.0;
        }
        tmp1 = z[nx - 1] * z[nx - 1] - z[0];
        tmp2 = z[nx - 1] - 1.0;
        temp = 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
        f += (temp * temp) / 4000.0 - Math.cos(temp) + 1.0;

        return f;
    }
    

	public static double grie_rosen_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Griewank-Rosenbrock  */ {
        double temp, tmp1, tmp2;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 5.0 / 100.0, s_flag, r_flag); /* shift and rotate */


        double f = 0.0;

        z[0] += 1.0; //shift to orgin
        for (int i = 0; i < nx - 1; i++) {
            z[i + 1] += 1.0; //shift to orgin
            tmp1 = z[i] * z[i] - z[i + 1];
            tmp2 = z[i] - 1.0;
            temp = 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
            f += (temp * temp) / 4000.0 - Math.cos(temp) + 1.0;
        }
        tmp1 = z[nx - 1] * z[nx - 1] - z[0];
        tmp2 = z[nx - 1] - 1.0;
        temp = 100.0 * tmp1 * tmp1 + tmp2 * tmp2;
        f += (temp * temp) / 4000.0 - Math.cos(temp) + 1.0;

        return f;
	}

    public final static double escaffer6_func(double[] x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Expanded Scaffer's F6  */ {
        double temp1, temp2;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */


        double f = 0.0;
        for (int i = 0; i < nx - 1; i++) {
            temp1 = Math.sin(Math.sqrt(z[i] * z[i] + z[i + 1] * z[i + 1]));
            temp1 = temp1 * temp1;
            temp2 = 1.0 + 0.001 * (z[i] * z[i] + z[i + 1] * z[i + 1]);
            f += 0.5 + (temp1 - 0.5) / (temp2 * temp2);
        }
        temp1 = Math.sin(Math.sqrt(z[nx - 1] * z[nx - 1] + z[0] * z[0]));
        temp1 = temp1 * temp1;
        temp2 = 1.0 + 0.001 * (z[nx - 1] * z[nx - 1] + z[0] * z[0]);
        f += 0.5 + (temp1 - 0.5) / (temp2 * temp2);

        return f;
    }
    
	public static double escaffer6_func(List<Double> x, int nx, double[] Os, double[] Mr, int s_flag, int r_flag) /* Expanded Scaffer's F6  */ {
        double temp1, temp2;

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */


        double f = 0.0;
        for (int i = 0; i < nx - 1; i++) {
            temp1 = Math.sin(Math.sqrt(z[i] * z[i] + z[i + 1] * z[i + 1]));
            temp1 = temp1 * temp1;
            temp2 = 1.0 + 0.001 * (z[i] * z[i] + z[i + 1] * z[i + 1]);
            f += 0.5 + (temp1 - 0.5) / (temp2 * temp2);
        }
        temp1 = Math.sin(Math.sqrt(z[nx - 1] * z[nx - 1] + z[0] * z[0]));
        temp1 = temp1 * temp1;
        temp2 = 1.0 + 0.001 * (z[nx - 1] * z[nx - 1] + z[0] * z[0]);
        f += 0.5 + (temp1 - 0.5) / (temp2 * temp2);

        return f;
	}

    public final static double hf01(double[] x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 1 */ {
        int i, tmp, cf_num = 3;
        double[] fit = new double[3];
        int[] G = new int[3];
        int[] G_nx = new int[3];
        double[] Gp = {0.3, 0.3, 0.4};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;
        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */
        double [] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }

        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = schwefel_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = rastrigin_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 2] + G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 2] + G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = ellips_func(ty, G_nx[i], tO, tM, 0, 0);

        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;
    }
    
	public static double hf01(List<Double> x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 1 */ {
		int i, tmp, cf_num = 3;
        double[] fit = new double[3];
        int[] G = new int[3];
        int[] G_nx = new int[3];
        double[] Gp = {0.3, 0.3, 0.4};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;
        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */
        double [] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }

        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = schwefel_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = rastrigin_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 2] + G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 2] + G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = ellips_func(ty, G_nx[i], tO, tM, 0, 0);

        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;
	}

    public final static double hf02(double[] x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 2 */ {
        int i, tmp, cf_num = 3;
        double[] fit = new double[3];
        int[] G = new int[3];
        int[] G_nx = new int[3];
        double[] Gp = {0.3, 0.3, 0.4};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

        double [] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }

        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = bent_cigar_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = hgbat_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 2] + G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = rastrigin_func(ty,  G_nx[i], tO, tM, 0, 0);

        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;

    }
    

	public static double hf02(List<Double> x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 2 */ {
        int i, tmp, cf_num = 3;
        double[] fit = new double[3];
        int[] G = new int[3];
        int[] G_nx = new int[3];
        double[] Gp = {0.3, 0.3, 0.4};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

        double [] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }

        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = bent_cigar_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = hgbat_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 2] + G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = rastrigin_func(ty,  G_nx[i], tO, tM, 0, 0);

        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;

    }

    public final static double hf03(double[] x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 3 */ {
        int i, tmp, cf_num = 4;
        double[] fit = new double[4];
        int[] G_nx = new int[4];
        int[] G = new int[4];
        double[] Gp = {0.2, 0.2, 0.3, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */
        double [] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }

        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = griewank_func(ty,  G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = weierstrass_func(ty,  G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = rosenbrock_func(ty,  G_nx[i], tO, tM, 0, 0);

        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = escaffer6_func(ty,  G_nx[i], tO, tM, 0, 0);

        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;

    }
    
	public static double hf03(List<Double> x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 3 */ {
        int i, tmp, cf_num = 4;
        double[] fit = new double[4];
        int[] G_nx = new int[4];
        int[] G = new int[4];
        double[] Gp = {0.2, 0.2, 0.3, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */
        double [] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }

        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = griewank_func(ty,  G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = weierstrass_func(ty,  G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = rosenbrock_func(ty,  G_nx[i], tO, tM, 0, 0);

        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = escaffer6_func(ty,  G_nx[i], tO, tM, 0, 0);

        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;
	}

    public final static double hf04(double[] x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 4 */ {
        int i, tmp, cf_num = 4;
        double[] fit = new double[4];
        int[] G = new int[4];
        int[] G_nx = new int[4];
        double[] Gp = {0.2, 0.2, 0.3, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */
        
        double [] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }

        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = hgbat_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = discus_func(ty,  G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = grie_rosen_func(ty,  G_nx[i], tO, tM, 0, 0);
        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = rastrigin_func(ty, G_nx[i], tO, tM, 0, 0);

        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;

    }
    
	public static double hf04(List<Double> x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 4 */ {
        int i, tmp, cf_num = 4;
        double[] fit = new double[4];
        int[] G = new int[4];
        int[] G_nx = new int[4];
        double[] Gp = {0.2, 0.2, 0.3, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */
        
        double [] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }

        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = hgbat_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = discus_func(ty,  G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = grie_rosen_func(ty,  G_nx[i], tO, tM, 0, 0);
        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = rastrigin_func(ty, G_nx[i], tO, tM, 0, 0);

        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;

    }

    public final static double hf05(double[] x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 5 */ {
        int i, tmp, cf_num = 5;
        double[] fit = new double[5];
        int[] G = new int[5];
        int[] G_nx = new int[5];
        double[] Gp = {0.1, 0.2, 0.2, 0.2, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        double[] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

        double[] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }

        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = escaffer6_func(ty,  G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = hgbat_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }

        fit[i] = rosenbrock_func(ty, G_nx[i], tO, tM, 0, 0);
        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = schwefel_func(ty, G_nx[i], tO, tM, 0, 0);
        i = 4;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = ellips_func(ty, G_nx[i], tO, tM, 0, 0);

		//for(i=0;i<cf_num;i++){
        //	System.out.println("fithf05["+i+"]"+"="+fit[i]);
        //}
        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;

    }
    
	public static double hf05(List<Double> x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) /* Hybrid Function 5 */ {
        int i, tmp, cf_num = 5;
        double[] fit = new double[5];
        int[] G = new int[5];
        int[] G_nx = new int[5];
        double[] Gp = {0.1, 0.2, 0.2, 0.2, 0.3};

        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;

        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }

        double[] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag); /* shift and rotate */

        double[] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }

        double[] ty, tO, tM;

        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = escaffer6_func(ty,  G_nx[i], tO, tM, 0, 0);

        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = hgbat_func(ty, G_nx[i], tO, tM, 0, 0);

        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }

        fit[i] = rosenbrock_func(ty, G_nx[i], tO, tM, 0, 0);
        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = schwefel_func(ty, G_nx[i], tO, tM, 0, 0);
        i = 4;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = ellips_func(ty, G_nx[i], tO, tM, 0, 0);

		//for(i=0;i<cf_num;i++){
        //	System.out.println("fithf05["+i+"]"+"="+fit[i]);
        //}
        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;

    }


     public final static double cf01(double[] x,  int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 1 */ {
        int i, j, cf_num = 5;
        double[] fit = new double[5];// fit[5];
        double[] delta = {10, 20, 30, 40, 50};
        double[] bias = {0, 100, 200, 300, 400};

        double[] tOs;// = new double[nx];
//        double[] tMr = new double[cf_num * nx * nx];
        double[] tMr;// = new double[nx * nx];


        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);   
        
        fit[i] = rosenbrock_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+4;

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);  
        
        fit[i] = ellips_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        
        fit[i] = bent_cigar_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+30;

        i = 3;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        
        fit[i] = discus_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;

        i = 4;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        
        fit[i] = ellips_func(x,  nx, tOs, tMr, 1, 0);
        fit[i] = 10000 * fit[i] / 1e+10;

        return cf_cal(x,nx, Os, delta, bias, fit, cf_num);

    }
     
 	public static double cf01(List<Double> x,  int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 1 */ {
        int i, j, cf_num = 5;
        double[] fit = new double[5];// fit[5];
        double[] delta = {10, 20, 30, 40, 50};
        double[] bias = {0, 100, 200, 300, 400};

        double[] tOs;// = new double[nx];
//        double[] tMr = new double[cf_num * nx * nx];
        double[] tMr;// = new double[nx * nx];


        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);   
        
        fit[i] = rosenbrock_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+4;

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);  
        
        fit[i] = ellips_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        
        fit[i] = bent_cigar_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+30;

        i = 3;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        
        fit[i] = discus_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;

        i = 4;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        
        fit[i] = ellips_func(x,  nx, tOs, tMr, 1, 0);
        fit[i] = 10000 * fit[i] / 1e+10;

        return cf_cal(x,nx, Os, delta, bias, fit, cf_num);

    }

	public final static double cf02(double[] x,  int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 2 */ {
        int i, j, cf_num = 3;
        double[] fit = new double[3];
        double[] delta = {20, 20, 20};
        double[] bias = {0, 100, 200};

        double[] tOs;// = new double[nx];
//        double[] tMr = new double[cf_num * nx * nx];
        double[] tMr;// = new double[nx * nx];

        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = schwefel_func(x, nx, tOs, tMr, 1, 0);

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = rastrigin_func(x,  nx, tOs, tMr, 1, r_flag);

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = hgbat_func(x, nx, tOs, tMr, 1, r_flag);

        return cf_cal(x, nx, Os, delta, bias, fit, cf_num);
    }
	

	public static double cf02(List<Double> x,  int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 2 */ {
        int i, j, cf_num = 3;
        double[] fit = new double[3];
        double[] delta = {20, 20, 20};
        double[] bias = {0, 100, 200};

        double[] tOs;// = new double[nx];
//        double[] tMr = new double[cf_num * nx * nx];
        double[] tMr;// = new double[nx * nx];

        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = schwefel_func(x, nx, tOs, tMr, 1, 0);

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = rastrigin_func(x,  nx, tOs, tMr, 1, r_flag);

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = hgbat_func(x, nx, tOs, tMr, 1, r_flag);

        return cf_cal(x, nx, Os, delta, bias, fit, cf_num);
    }

    public final static double cf03(double[] x,  int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 3 */ {
        int i, j, cf_num = 3;
        double[] fit = new double[3];
        double[] delta = {10, 30, 50};
        double[] bias = {0, 100, 200};

        double[] tOs;// = new double[nx];
//        double[] tMr = new double[cf_num * nx * nx];
        double[] tMr;// = new double[nx * nx];


        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = schwefel_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 4e+3;

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = rastrigin_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 1e+3;

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = ellips_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 1e+10;

        return cf_cal(x, nx, Os, delta, bias, fit, cf_num);
    }
    
	public static double cf03(List<Double> x,  int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 3 */ {
        int i, j, cf_num = 3;
        double[] fit = new double[3];
        double[] delta = {10, 30, 50};
        double[] bias = {0, 100, 200};

        double[] tOs;// = new double[nx];
//        double[] tMr = new double[cf_num * nx * nx];
        double[] tMr;// = new double[nx * nx];


        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = schwefel_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 4e+3;

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = rastrigin_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 1e+3;

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = ellips_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 1e+10;

        return cf_cal(x, nx, Os, delta, bias, fit, cf_num);
    }



	public final static double cf04(double[] x,  int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 4 */ {
        int i, j, cf_num = 5;
        double[] fit = new double[5];
        double[] delta = {10, 10, 10, 10, 10};
        double[] bias = {0, 100, 200, 300, 400};

        double[] tOs;// = new double[nx];
        double[] tMr;// = new double[ nx * nx];
//        double[] tMr = new double[cf_num * nx * nx];

//        System.out.println("nx = "+ nx + "\n" + 
//                "cf_num = " + cf_num + "\n" +
//                "Os.length = "+Os.length + "\n" + 
//                "Mr.length = " + Mr.length + "\n");
        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = schwefel_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / (4e+3);

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        // wrong
////        for (j = 0; j < cf_num * nx * nx; j++) {
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = happycat_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / (1e+3);

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = ellips_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 1e+10;

        i = 3;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = weierstrass_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 400;

        i = 4;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = griewank_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 100;

        return cf_cal(x,  nx, Os, delta, bias, fit, cf_num);
    }
	
	public static double cf04(List<Double> x,  int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 4 */ {
        int i, j, cf_num = 5;
        double[] fit = new double[5];
        double[] delta = {10, 10, 10, 10, 10};
        double[] bias = {0, 100, 200, 300, 400};

        double[] tOs;// = new double[nx];
        double[] tMr;// = new double[ nx * nx];
//        double[] tMr = new double[cf_num * nx * nx];

//        System.out.println("nx = "+ nx + "\n" + 
//                "cf_num = " + cf_num + "\n" +
//                "Os.length = "+Os.length + "\n" + 
//                "Mr.length = " + Mr.length + "\n");
        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = schwefel_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / (4e+3);

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        // wrong
////        for (j = 0; j < cf_num * nx * nx; j++) {
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = happycat_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / (1e+3);

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = ellips_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 1e+10;

        i = 3;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = weierstrass_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 400;

        i = 4;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = griewank_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 1000 * fit[i] / 100;

        return cf_cal(x,  nx, Os, delta, bias, fit, cf_num);
    }

    public final static double cf05(double[] x, int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 5 */ {
        int i, j, cf_num = 5;
        double[] fit = new double[5];
        double[] delta = {10, 10, 10, 20, 20};
        double[] bias = {0, 100, 200, 300, 400};

        double[] tOs;// = new double[nx];
        double[] tMr;// = new double[nx * nx];

        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = hgbat_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1000;
        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = rastrigin_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+3;
        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = schwefel_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 4e+3;
        i = 3;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = weierstrass_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 400;
        i = 4;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = ellips_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;

        return cf_cal(x,nx, Os, delta, bias, fit, cf_num);
    }
    
	public static double cf05(List<Double> x, int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 5 */ {
        int i, j, cf_num = 5;
        double[] fit = new double[5];
        double[] delta = {10, 10, 10, 20, 20};
        double[] bias = {0, 100, 200, 300, 400};

        double[] tOs;// = new double[nx];
        double[] tMr;// = new double[nx * nx];

        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = hgbat_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1000;
        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = rastrigin_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+3;
        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = schwefel_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 4e+3;
        i = 3;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = weierstrass_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 400;
        i = 4;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = ellips_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;

        return cf_cal(x,nx, Os, delta, bias, fit, cf_num);
    }

    public final static double cf06(double[] x,  int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 6 */ {
        int i, j, cf_num = 5;
        double[] fit = new double[5];
        double[] delta = {10, 20, 30, 40, 50};
        double[] bias = {0, 100, 200, 300, 400};

        double[] tOs;// = new double[nx];
        double[] tMr;// = new double[nx * nx];

        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = grie_rosen_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 4e+3;
        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = happycat_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+3;
        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j <  nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = schwefel_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 4e+3;
        i = 3;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = escaffer6_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 2e+7;
        i = 4;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = ellips_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;

        return cf_cal(x, nx, Os, delta, bias, fit, cf_num);
    }
    
	public static double cf06(List<Double> x,  int nx, double[] Os, double[] Mr, int r_flag) /* Composition Function 6 */ {
        int i, j, cf_num = 5;
        double[] fit = new double[5];
        double[] delta = {10, 20, 30, 40, 50};
        double[] bias = {0, 100, 200, 300, 400};

        double[] tOs;// = new double[nx];
        double[] tMr;// = new double[nx * nx];

        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = grie_rosen_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 4e+3;
        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = happycat_func(x,  nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+3;
        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j <  nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = schwefel_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 4e+3;
        i = 3;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = escaffer6_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 2e+7;
        i = 4;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        fit[i] = ellips_func(x, nx, tOs, tMr, 1, r_flag);
        fit[i] = 10000 * fit[i] / 1e+10;

        return cf_cal(x, nx, Os, delta, bias, fit, cf_num);
    }

    public final static double cf07(double[] x,  int nx, double[] Os, double[] Mr, int[] SS, int r_flag) /* Composition Function 7 */ {
        int i, j, cf_num = 3;
        double[] fit = new double[3];
        double[] delta = {10, 30, 50};
        double[] bias = {0, 100, 200};

        double[] tOs;// = new double[nx];
        double[] tMr;// = new double[nx * nx];
        int[] tSS;// = new int[nx];

        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        fit[i] = hf01(x,  nx, tOs, tMr, tSS, 1, r_flag);

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
        fit[i] = hf02(x, nx, tOs, tMr, tSS, 1, r_flag);

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
        fit[i] = hf03(x,nx, tOs, tMr, tSS, 1, r_flag);

        return cf_cal(x,  nx, Os, delta, bias, fit, cf_num);
    }
    
	public static double cf07(List<Double> x,  int nx, double[] Os, double[] Mr, int[] SS, int r_flag) /* Composition Function 7 */ {
        int i, j, cf_num = 3;
        double[] fit = new double[3];
        double[] delta = {10, 30, 50};
        double[] bias = {0, 100, 200};

        double[] tOs;// = new double[nx];
        double[] tMr;// = new double[nx * nx];
        int[] tSS;// = new int[nx];

        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        fit[i] = hf01(x,  nx, tOs, tMr, tSS, 1, r_flag);

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
        fit[i] = hf02(x, nx, tOs, tMr, tSS, 1, r_flag);

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
        fit[i] = hf03(x,nx, tOs, tMr, tSS, 1, r_flag);

        return cf_cal(x,  nx, Os, delta, bias, fit, cf_num);
    }

    public final static double cf08(double[] x,  int nx, double[] Os, double[] Mr, int[] SS, int r_flag) /* Composition Function 8 */ {
        int i, j, cf_num = 3;
        double[] fit = new double[3];
        double[] delta = {10, 30, 50};
        double[] bias = {0, 100, 200};

        double[] tOs;// = new double[nx];
        double[] tMr;// = new double[nx * nx];
        int[] tSS;// = new int[nx];

        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
        fit[i] = hf04(x, nx, tOs, tMr, tSS, 1, r_flag);

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
        fit[i] = hf05(x, nx, tOs, tMr, tSS, 1, r_flag);

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
        fit[i] = hf06(x, nx, tOs, tMr, tSS, 1, r_flag);

        return cf_cal(x, nx, Os, delta, bias, fit, cf_num);
    }
    
	public static double cf08(List<Double> x,  int nx, double[] Os, double[] Mr, int[] SS, int r_flag) /* Composition Function 8 */ {
        int i, j, cf_num = 3;
        double[] fit = new double[3];
        double[] delta = {10, 30, 50};
        double[] bias = {0, 100, 200};

        double[] tOs;// = new double[nx];
        double[] tMr;// = new double[nx * nx];
        int[] tSS;// = new int[nx];

        i = 0;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
        fit[i] = hf04(x, nx, tOs, tMr, tSS, 1, r_flag);

        i = 1;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
        fit[i] = hf05(x, nx, tOs, tMr, tSS, 1, r_flag);

        i = 2;
//        for (j = 0; j < nx; j++) {
//            tOs[j] = Os[i * nx + j];
//        }
//        for (j = 0; j < nx * nx; j++) {
//            tMr[j] = Mr[i * nx * nx + j];
//        }
//        for (j = 0; j < nx; j++) {
//            tSS[j] = SS[i * nx + j];
//        }
        tOs = Arrays.copyOfRange(Os, i*nx, (i+1)*nx);
        tMr = Arrays.copyOfRange(Mr, i*nx*nx, (i+1)*nx*nx);
        tSS = Arrays.copyOfRange(SS, i*nx, (i+1)*nx);
        fit[i] = hf06(x, nx, tOs, tMr, tSS, 1, r_flag);

        return cf_cal(x, nx, Os, delta, bias, fit, cf_num);
    }

    public final static double hf06(double[] x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) {
        int i;
        int tmp;
        int cf_num = 5;
        double[] fit = new double[5];
        int[] G = new int[5];
        int[] G_nx = new int[5];
        double[] Gp = {0.1, 0.2, 0.2, 0.2, 0.3};
        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;
        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }
        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);
        double [] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }
        double[] ty;
        double[] tO;
        double[] tM;
        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = katsuura_func(ty, G_nx[i], tO, tM, 0, 0);
        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = happycat_func(ty,  G_nx[i], tO, tM, 0, 0);
        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = grie_rosen_func(ty,  G_nx[i], tO, tM, 0, 0);
        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = schwefel_func(ty, G_nx[i], tO, tM, 0, 0);
        i = 4;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = ackley_func(ty,  G_nx[i], tO, tM, 0, 0);
        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;
    }
    
	public static double hf06(List<Double> x, int nx, double[] Os, double[] Mr, int[] S, int s_flag, int r_flag) {
        int i;
        int tmp;
        int cf_num = 5;
        double[] fit = new double[5];
        int[] G = new int[5];
        int[] G_nx = new int[5];
        double[] Gp = {0.1, 0.2, 0.2, 0.2, 0.3};
        tmp = 0;
        for (i = 0; i < cf_num - 1; i++) {
            G_nx[i] = (int) Math.ceil(Gp[i] * nx);
            tmp += G_nx[i];
        }
        G_nx[cf_num - 1] = nx - tmp;
        G[0] = 0;
        for (i = 1; i < cf_num; i++) {
            G[i] = G[i - 1] + G_nx[i - 1];
        }
        double [] z = ShiftRotation.sr_func(x, nx, Os, Mr, 1.0, s_flag, r_flag);
        double [] y = new double[nx];
        for (i = 0; i < nx; i++) {
            y[i] = z[S[i] - 1];
        }
        double[] ty;
        double[] tO;
        double[] tM;
        i = 0;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[ii];
            tO[ii] = Os[ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = katsuura_func(ty, G_nx[i], tO, tM, 0, 0);
        i = 1;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + ii];
            tO[ii] = Os[G_nx[i - 1] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = happycat_func(ty,  G_nx[i], tO, tM, 0, 0);
        i = 2;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = grie_rosen_func(ty,  G_nx[i], tO, tM, 0, 0);
        i = 3;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = schwefel_func(ty, G_nx[i], tO, tM, 0, 0);
        i = 4;
        ty = new double[G_nx[i]];
        tO = new double[G_nx[i]];
        tM = new double[G_nx[i]];
        for (int ii = 0; ii < G_nx[i]; ii++) {
            ty[ii] = y[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tO[ii] = Os[G_nx[i - 1] + G_nx[i - 2] + G_nx[i - 3] + G_nx[i - 4] + ii];
            tM[ii] = Mr[i * nx + ii];
        }
        fit[i] = ackley_func(ty,  G_nx[i], tO, tM, 0, 0);
        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f += fit[i];
        }
        return f;
    }
    
    /* not used
    public final static double[] asyfunc(double[] x, int nx, double beta) {
        int i;
        double[] xasy = new double[nx];
        for (i = 0; i < nx; i++) {
            if (x[i] > 0) {
                xasy[i] = Math.pow(x[i], 1.0 + beta * i / (nx - 1) * Math.pow(x[i], 0.5));
            }
        }
        return xasy;
    }
    */
    public final static double[]  oszfunc(double[] x,  int nx) {
        double[] xosz = new double[nx];
        int i, sx;
        double c1, c2, xx = 0;
        for (i = 0; i < nx; i++) {
            if (i == 0 || i == nx - 1) {
                if (x[i] != 0) {
                    xx = Math.log(Math.abs(x[i]));//xx=log(fabs(x[i]));
                }
                if (x[i] > 0) {
                    c1 = 10;
                    c2 = 7.9;
                } else {
                    c1 = 5.5;
                    c2 = 3.1;
                }
                if (x[i] > 0) {
                    sx = 1;
                } else if (x[i] == 0) {
                    sx = 0;
                } else {
                    sx = -1;
                }
                xosz[i] = sx * Math.exp(xx + 0.049 * (Math.sin(c1 * xx) + Math.sin(c2 * xx)));
            } else {
                xosz[i] = x[i];
            }
        }
        
        return xosz;
    }

    public final static double cf_cal(double[] x,  int nx, double[] Os, double[] delta, double[] bias, double[] fit, int cf_num) {
        int i, j;

        double[] w;
        double w_max = 0, w_sum = 0;
        w = new double[cf_num];
        for (i = 0; i < cf_num; i++) {
            fit[i] += bias[i];
            w[i] = 0;
            for (j = 0; j < nx; j++) {
                w[i] += Math.pow(x[j] - Os[i * nx + j], 2.0);
            }
            if (w[i] != 0) {
                w[i] = Math.pow(1.0 / w[i], 0.5) * Math.exp(-w[i] / 2.0 / nx / Math.pow(delta[i], 2.0));
            } else {
                w[i] = Constants.INF;
            }
            if (w[i] > w_max) {
                w_max = w[i];
            }
        }

        for (i = 0; i < cf_num; i++) {
            w_sum = w_sum + w[i];
        }
        if (w_max == 0) {
            for (i = 0; i < cf_num; i++) {
                w[i] = 1;
            }
            w_sum = cf_num;
        }
        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f = f + w[i] / w_sum * fit[i];
        }

        return f;

    }
    
	private static double cf_cal(List<Double> x,  int nx, double[] Os, double[] delta, double[] bias, double[] fit, int cf_num) {
        int i, j;

        double[] w;
        double w_max = 0, w_sum = 0;
        w = new double[cf_num];
        for (i = 0; i < cf_num; i++) {
            fit[i] += bias[i];
            w[i] = 0;
            for (j = 0; j < nx; j++) {
                w[i] += Math.pow(x.get(j) - Os[i * nx + j], 2.0);
            }
            if (w[i] != 0) {
                w[i] = Math.pow(1.0 / w[i], 0.5) * Math.exp(-w[i] / 2.0 / nx / Math.pow(delta[i], 2.0));
            } else {
                w[i] = Constants.INF;
            }
            if (w[i] > w_max) {
                w_max = w[i];
            }
        }

        for (i = 0; i < cf_num; i++) {
            w_sum = w_sum + w[i];
        }
        if (w_max == 0) {
            for (i = 0; i < cf_num; i++) {
                w[i] = 1;
            }
            w_sum = cf_num;
        }
        double f = 0.0;
        for (i = 0; i < cf_num; i++) {
            f = f + w[i] / w_sum * fit[i];
        }

        return f;

    }
}
