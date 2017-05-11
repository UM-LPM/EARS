package org.um.feri.analyse.EE.util;

import java.util.Arrays;

public class GrayTestMz {
	static String formatBinaryZero(int n, int l) {
		return addBinaryZero(Integer.toString(n, 2),l);
	}

	static String addBinaryZero(String n, int l) {
		String s="";
		for (int i=0; i<l-n.length();i++) s=s+"0";
		return s+n;
	}
	static void printBinary(int[] d,int l) {
		for (int i=0; i<d.length;i++)
		System.out.println(addBinaryZero(Integer.toString(d[i], 2),l)); 
	}
	
	static int[] delta2gray(int[] d, int ldn) {
		int g[] = new int[d.length];
		g[0] = 0;
		int n = (int) Math.pow(2, ldn);
		for (int k = 0; k < n - 1; ++k)
			g[k + 1] = g[k] ^ ((int) Math.pow(2, d[k]));
		//System.out.println(Arrays.toString(g));
		//printBinary(g, ldn);
		return g;
	}
	static String[] monotonic_gray(int lnd) {
		int[] d=monotonic_gray_delta(lnd);
		String[] tmp = new String[d.length];
		for (int i=0; i<tmp.length;i++) {
			tmp[i] = addBinaryZero(Integer.toString(d[i], 2),lnd);
		}
		return tmp;
		
	}
	static int[] monotonic_gray_delta(int ldn)
	// Write into the array d[] the delta sequence for the
	// Savage-Winkler monotonic Gray path.
	// Algorithm as given in Knuth/4.
	{
		int nn = (int) Math.pow(2, ldn);
		int d[] = new int[nn];
		int _goto = 2;
		if (1 >= ldn) {
			d[0] = 0;
			d[ldn] = 0;
			d=delta2gray(d, ldn);
			return d;
		}

		int[] p = new int[ldn];
		int[] pp = new int[ldn];

		int s[] = new int[nn];
		int t[] = new int[nn];
		int u[] = new int[nn];
		int tp[] = new int[nn];
		int up[] = new int[nn];

		// signed char *sp = (signed char *)d; // jjcast
		int sp[] = d;

		// R1:
		int n = 1;
		int n2 = (int) Math.pow(2, n);
		p[0] = 0;
		s[0] = t[0] = u[0] = 0;
		int j = 0, k = 0, l = 0;
		do {
			switch (_goto) {

			case 2:

				// S1:
				j = 0;
				k = 0;
				l = 0;
				u[n2 - 1] = -1;

			case 22:
				// S2:
				while (0 == u[j]) {
					sp[l] = s[j];
					up[l] = 0;
					++l;
					++j;
				}
				if (u[j] < 0) {
					_goto = 25;
					continue;
				}
				// S3:
				sp[l] = s[j];
				up[l] = 1;
				++l;
				++j;
				while (0 == u[j]) {
					sp[l] = s[j];
					up[l] = 0;
					++l;
					++j;
				}
				sp[l] = n;
				up[l] = 0;
				++l;
				while (0 == u[k]) {
					sp[l] = p[t[k]];
					up[l] = 0;
					++l;
					++k;
				}
				sp[l] = p[t[k]];
				up[l] = 1;
				++l;
				++k;
				while (0 == u[k]) {
					sp[l] = p[t[k]];
					up[l] = 0;
					++l;
					++k;
				}

				// S4:
				if (u[k] < 0) {
					_goto = 26;
					continue;
				}
				;
				sp[l] = n;
				up[l] = 0;
				++l;
				++k;
				++j;
				_goto = 22;
				continue;

			case 25:
				// S5:
				sp[l] = n;
				up[l] = 1;
				++l;
				while (0 == u[k]) {
					sp[l] = p[t[k]];
					up[l] = 0;
					++l;
					++k;
				}

			case 26:
				// S6:
				j = k = l = 0;
				while (0 == u[k]) {
					tp[l] = t[k];
					++l;
					++k;
				}
				tp[l] = n;
				++l;

			case 27:
				// S7:
				while (0 == u[j]) {
					tp[l] = p[s[j]];
					++l;
					++j;
				}
				if (u[j] < 0) {
					_goto = 999;
					continue;
				}
				tp[l] = n;
				++l;
				++j;
				++k;

				// S8:
				while (0 == u[k]) {
					tp[l] = t[k];
					++l;
					++k;
				}
				if (u[k] < 0) {
					_goto = 210;
					continue;
				}

				// S9:
				tp[l] = t[k];
				++l;
				++k;
				while (0 == u[k]) {
					tp[l] = t[k];
					++l;
					++k;
				}
				tp[l] = n;
				++l;
				while (0 == u[j]) {
					tp[l] = p[s[j]];
					++l;
					++j;
				}
				tp[l] = p[s[j]];
				++l;
				++j;
				_goto = 27;
				continue;

			case 210:
				// S10:
				tp[l] = n;
				++l;
				while (0 == u[j]) {
					tp[l] = p[s[j]];
					++l;
					++j;
				}

			case 999:
				++n;
				n2 = (int) Math.pow(2, n);

				// R3
				// here sp[1...2**n-1] contains the delta sequence
				if (n == ldn) {
					_goto = Integer.MAX_VALUE;
					continue;
				} // return;

				// R4:
				pp[0] = n - 1;
				for (int jj = 1; jj < n; ++jj)
					pp[jj] = p[p[jj - 1]];

				// R5:
				for (int jj = 0; jj < n; ++jj)
					p[jj] = pp[jj];
				for (int kk = 0; kk < n2 - 1; ++kk)
					s[kk] = sp[kk];
				for (int kk = 0; kk < n2 - 1; ++kk)
					t[kk] = tp[kk];
				for (int kk = 0; kk < n2 - 1; ++kk)
					u[kk] = up[kk];
				_goto = 2;
				continue; // start

			}
		} while (_goto != Integer.MAX_VALUE);
		// -------------------------
		//System.out.println(Arrays.toString(d));
		d = delta2gray(d, ldn);
		return d;
	}

	// -------------------------

	public static int END = Integer.MAX_VALUE;
/*
 * set xtics   ("00000" 1 -1, "00001" 2 -1, "00011" 3 -1, "00010" 4 -1, "00110" 5 -1, "00100" 6 -1, "01100" 7 -1, "01000" 8 -1, "11000" 9 -1, "10000" 10 -1, "10001" 11 -1, "10101" 12 -1, "10100" 13 -1, "10110" 14 -1, "10010" 15 -1, "11010" 16 -1, "01010" 17 -1, "01011" 18 -1, "01001" 19 -1, "01101" 20 -1, "00101" 21 -1, "00111" 22 -1, "01111" 23 -1, "01110" 24 -1, "11110" 25 -1, "11100" 26 -1, "11101" 27 -1, "11001" 28 -1, "11011" 29 -1, "10011" 30 -1, "10111" 31 -1, "11111" 32 -1)
 * 
 */
	public static String gnuXTic(int[] list, int n, int skip, int skipLine) {
		StringBuffer sb = new StringBuffer();
		sb.append("set xtics   (");
		boolean first=true;
		for (int i=0; i<list.length; i++) {
			if (((i%skip)==0) || (i==(list.length-1))) {
				if (!first) {
					sb.append(", ");
				} else first = false;
				sb.append("\"").append(formatBinaryZero(list[i],n)).append("\"");
				sb.append(" ").append(""+(i+1)).append(" -1");
			} else {
				if ((i%skipLine)==0) {
					if (!first) {
						sb.append(", ");
					} else first = false;
					sb.append("\"\""); //no label
					sb.append(" ").append(""+(i+1)).append(" -1");					
				}
			}
		}
		sb.append(")");
		return sb.toString();
		
	}
    
	public static void main(String[] args) {
		printBinary(monotonic_gray_delta(9),9);
		System.out.println(gnuXTic(monotonic_gray_delta(9),9,16,4));
	}
}
