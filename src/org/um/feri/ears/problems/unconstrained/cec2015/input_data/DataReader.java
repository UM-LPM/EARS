/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.um.feri.ears.problems.unconstrained.cec2015.input_data;

//~--- JDK imports ------------------------------------------------------------

import java.io.InputStream;

import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * 
 * @author QIN
 */
public final class DataReader {
	private static double[] readDouble(Scanner input, int maxSize) {
		double[] ret = new double[maxSize];
		int actualSize = 0;
		input.useLocale(Locale.US); // Added to work on any PC 
		try {

			// Scanner input = new Scanner(dataFile);
			for (int i = 0; i < ret.length; i++) {
				ret[i] = input.nextDouble();
				actualSize++;
			}
		} catch (NoSuchElementException e) {
		}

		if (actualSize != maxSize) {
			return Arrays.copyOf(ret, actualSize);
		} else {
			return ret;
		}
	}

	private static int[] readInt(Scanner input, int maxSize) {
		int[] ret = new int[maxSize];
		int actualSize = 0;

		try {

			// Scanner input = new Scanner(dataFile);
			for (int i = 0; i < ret.length; i++) {
				ret[i] = input.nextInt();
				actualSize++;
			}
		} catch (NoSuchElementException e) {
		}

		if (actualSize != maxSize) {
			return Arrays.copyOf(ret, actualSize);
		} else {
			return ret;
		}
	}

	public static int[] readShuffleData(int func_num, int nx) {
		int size = nx;

		if (size != -1) {
			String fn = "shuffle_data_" + func_num + "_D" + nx + ".txt";
			InputStream dataFile = DataReader.class.getResourceAsStream(fn);

			return readInt(new Scanner(dataFile), size);
		}

		return null;
	}

	public static double[] readRotation(int func_num, int nx, int cf_num) {
		int size = (func_num < 13) ? nx * nx : cf_num * nx * nx;
		//String p = DataReader.class.getResource("org.um.feri.ears.problems.unconstrained.cec2015.input_data").getPath();

		String fn = "M_" + func_num + "_D" + nx + ".txt";
		InputStream dataFile = DataReader.class.getResourceAsStream(fn);
		return readDouble(new Scanner(dataFile), size);
	}

	public static double[] readShiftData(int func_num, int nx, int cf_num) {
		int size = (func_num < 13) ? nx : nx * cf_num;
		String fn = "shift_data_" + func_num + "_D" + nx + ".txt";
		InputStream dataFile = DataReader.class.getResourceAsStream(fn);

		return readDouble(new Scanner(dataFile), size);
	}

	// double ellips_func (double[] , double , int , double[] ,double[] ,int ,int) /* Ellipsoidal */
	// double bent_cigar_func (double[] , double , int , double[] ,double[] ,int ,int) /* Bent_Cigar */
	// double discus_func (double[] , double , int , double[] ,double[] ,int ,int) /* Discus */
	// double rosenbrock_func (double[] , double , int , double[] ,double[] ,int ,int) /* Rosenbrock's */
	// double ackley_func (double[] , double , int , double[] ,double[] ,int ,int) /* Ackley's */
	// double rastrigin_func (double[] , double , int , double[] ,double[] ,int ,int) /* Rastrigin's */
	// double weierstrass_func (double[] , double , int , double[] ,double[] ,int ,int) /* Weierstrass's */
	// double griewank_func (double[] , double , int , double[] ,double[] ,int ,int) /* Griewank's */
	// double schwefel_func (double[] , double , int , double[] ,double[] ,int ,int) /* Schwefel's */
	// double katsuura_func (double[] , double , int , double[] ,double[] ,int ,int) /* Katsuura */
	// double grie_rosen_func (double[] , double , int , double[] ,double[] ,int ,int) /* Griewank-Rosenbrock */
	// double escaffer6_func (double[] , double , int , double[] ,double[] ,int ,int) /* Expanded Scaffer��s
	// double happycat_func (double[] , double , int , double[] ,double[] ,int ,int) /* HappyCat */
	// double hgbat_func (double[] , double , int , double[] ,double[] ,int ,int) /* HGBat */
	// double hf01 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 1 */
	// double hf02 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 2 */
	// double hf03 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 3 */
	// double hf04 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 4 */
	// double hf05 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 5 */
	// double hf06 (double[] , double[] , int , double[] ,double[] , int[] ,int ,int) /* Composition Function 6 */
	// double cf01 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 1 */
	// double cf02 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 2 */
	// double cf03 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 3 */
	// double cf04 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 4 */
	// double cf05 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 5 */
	// double cf06 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 6 */
	// double cf07 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 7 */
	// double cf08 (double[] , double[] , int , double[] ,double[] ,int ) /* Composition Function 8 */
	// void shiftfunc (double[] , double[] , int ,double[] )
	// void rotatefunc (double[] , double[] , int ,double[] )
	// void sr_func (double[] .double[] ,int ,double[] ,double[] ,double ,int ,int )/* shift and rotate*/
	// void asyfunc (double[] , double[] , int , double )
	// void oszfunc (double[] , double[] , int )
	// double cf_cal(double[] , double , int , double[] ,double[] ,double[] ,double[] , int )
}

// ~ Formatted by Jindent --- http://www.jindent.com

