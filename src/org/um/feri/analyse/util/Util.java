package org.um.feri.analyse.util;

import java.text.DecimalFormat;

public class Util {
	public static DecimalFormat df1 = new DecimalFormat("#,###.#");
	public static DecimalFormat df = new DecimalFormat("#,###.##");
	public static DecimalFormat df3 = new DecimalFormat("#,###.###");
	public static DecimalFormat df6 = new DecimalFormat("#,###.######");
	public static DecimalFormat dfc1 = new DecimalFormat("#,##0.#######E0");
	public static DecimalFormat dfc2 = new DecimalFormat("#,##0.##E0"); //change **
	public static DecimalFormat dfcShort = new DecimalFormat("0.##E0"); //change **
	public static DecimalFormat intf = new DecimalFormat("###,###,###");
	public static String arrayToString(double d[]) {
		String s = "";
		for (int i = 0; i < d.length; i++) {
			s = s + df.format(d[i]);
			if (i < d.length - 1)
				s = s + ",";
		}
		return s;
	}

	public static double divide(double a, double b) {
		if (b == 0)
			return 0;
		return a / b;
	}
}
