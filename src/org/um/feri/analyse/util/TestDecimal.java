package org.um.feri.analyse.util;

import java.text.DecimalFormat;

public class TestDecimal {
	public static DecimalFormat df1 = new DecimalFormat("0.#######E0");
	public static DecimalFormat df2 = new DecimalFormat("#.######");
	public static DecimalFormat df3 = new DecimalFormat("#.######");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double d1 = 0.000001234;
		double d2 = 1230.000001234;
		System.out.println(df1.format(d1));
		System.out.println(df2.format(d1));
		System.out.println(df3.format(d1));
		System.out.println(df1.format(d2));
		System.out.println(df2.format(d2));
		System.out.println(df3.format(d2));
	}

}
