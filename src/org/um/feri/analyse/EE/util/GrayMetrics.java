package org.um.feri.analyse.EE.util;

import java.util.Collection;
import java.util.HashMap;

import org.um.feri.analyse.EE.tree_data.NodeEARS;


public class GrayMetrics {
	int size;
	int xsize;
	int ysize;
	GrayCell metrix[][];
	String monotoneGray[];

	public int getXLength() {
		// return this.size/2;
		return xsize;
	}

	public int getYLength() {
		// return this.size/2;
		return ysize;
	}

	public GrayMetrics(int size) {
		super();
		xsize = (int) Math.pow(2, size / 2);
		ysize = (int) Math.pow(2, size - (size / 2));
		this.size = size;
		metrix = new GrayCell[getXLength()][getYLength()];
		for (int i = 0; i < metrix.length; i++) {
			for (int j = 0; j < metrix[i].length; j++) {
				metrix[i][j] = new GrayCell();
			}
		}
		monotoneGray = GrayTestMz.monotonic_gray(size / 2);
	}

	public void add(NodeEARS n) {
		MyPoint m = MonotoneGrayCode.getPos(n.getChromo(), monotoneGray);
		metrix[m.getX()][m.getY()].add(n);
	}

	public int getNumberOfElements(int x, int y) {
		if (metrix[x][y].elements == null)
			return 0;
		return (int) Math.sqrt(metrix[x][y].elements.size());
	}

	public boolean isExplore(int x, int y, int X) {
		if (metrix[x][y].elements == null)
			return false;
		if (metrix[x][y].elements.get(0).getX() >= X)
			return true;
		if (metrix[x][y].elements.get(0).isRnd())
			return true;
		return false;
	}

	public boolean isExploit(int x, int y, int X) {
		if (metrix[x][y].elements == null)
			return false;
		if (metrix[x][y].elements.get(0).getX() < X)
			return true;
		return false;
	}

	/*
	 * Counts ferquency of revisited elements
	 */
	public Collection<Frequency> getMaxValues(int x, int type) {
		HashMap<Integer, Frequency> hm = new HashMap<Integer, Frequency>();
		Integer len = 0;
		for (int i = 0; i < metrix.length; i++) {
			for (int j = 0; j < metrix[i].length; j++) {
				if (metrix[i][j].elements == null)
					continue;
				len = 0;
				for (NodeEARS n : metrix[i][j].elements) {
					if (type == 1)
						len++; // all
					if (type == 2) {
						if (n.isRnd() || (n.getX() > x)) {
							len++;
						}
					}
					if (type == 3) {
						if ((n.getX() <= x)) {
							len++;
						}
					}

				}
				// len = metrix[i][j].elements.size();
				if (hm.containsKey(len)) {
					((Frequency) hm.get(len)).inc();
				} else
					hm.put(len, new Frequency(len));
			}
		}
		return hm.values();

	}

	/**
	 * GNUPlot type data
	 * 
	 * @return
	 */
	public String getDataTable() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < metrix.length; i++) {
			for (int j = 0; j < metrix[i].length; j++) {
				sb.append(i).append(" ").append(j).append(" ").append(
						getNumberOfElements(i, j)).append("\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * GNUPlot type data
	 * 
	 * @return
	 */
	public String getDataTableArea(int x) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < metrix.length; i++) {
			for (int j = 0; j < metrix[i].length; j++) {
				if (metrix[i][j].elements != null)
					if (x <= metrix[i][j].elements.size())
						sb.append(i).append(" ").append(j).append(" ").append(
								"0.7 0.7").append("\n");
			}
			// sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * GNUPlot type data
	 * 
	 * @return
	 */
	public String getExplore(int x) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < metrix.length; i++) {
			for (int j = 0; j < metrix[i].length; j++) {
				if (isExplore(i, j, x))
					sb.append(i).append(" ").append(j).append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * GNUPlot type data
	 * 
	 * @return
	 */
	public String getExploreCell(int x) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < metrix.length; i++) {
			for (int j = 0; j < metrix[i].length; j++) {
				if (isExplore(i, j, x))
					sb.append(i).append(" ").append(j).append("\n");
			}
		}
		return sb.toString();
	}

	public String getExploit(int x) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < metrix.length; i++) {
			for (int j = 0; j < metrix[i].length; j++) {
				if (isExploit(i, j, x))
					sb.append(i).append(" ").append(j).append("\n");
			}
		}
		return sb.toString();
	}

	/*
	 * public int findArea(boolean[][] mP, int i, int j, int x) { int countx=0;
	 * for (int jj=j+1; i<mP[i].length;j++) { if ((metrix[i][jj].elements !=
	 * null) && (mP[i][jj] == false)) { if (x <= metrix[i][j].elements.size()) {
	 * mP[i][j] = true; countx++; } } } }
	 */
	/**
	 * GNUPlot type data
	 * 
	 * @return
	 */
	public StringBuffer getOptimizedDataTableArea(int x) {
		StringBuffer sb = new StringBuffer();
		int countx = 0;
		long all = 0;
		boolean[][] mP = new boolean[metrix.length][];
		for (int i = 0; i < metrix.length; i++) {
			mP[i] = new boolean[metrix.length];
		}
		for (int i = 0; i < metrix.length; i++) {
			if (countx > 0)
				System.out.println("countx=0;=0;?????");
			for (int j = 0; j < metrix[i].length; j++) {
				if ((metrix[i][j].elements != null) && (mP[i][j] == false)) {
					if (x <= metrix[i][j].elements.size()) {
						mP[i][j] = true;
						countx++;
					}
				}
				if ((!mP[i][j] && (countx > 0))) { // write prejsnjega scan line
					sb.append(i).append(" ").append(
							(j - 1) - ((double) countx / 2)).append(" ")
							.append("1 " + countx).append("\n");
					all += countx;
					countx = 0;
				} else {
					if (j == (metrix[i].length - 1)) { // end of the line
						sb.append(i).append(" ").append(
								(j) - ((double) countx / 2)).append(" ")
								.append("1 " + countx).append("\n");
						countx = 0;
						all += countx;
					}

				}
			}
			// sb.append("\n");
		}
		System.out.println("all:" + all);
		return sb;
	}

	public static int setC(int i, int max) {
		if (i < 0)
			return 0;
		if (i >= max)
			return max - 1;
		return i;
	}

	// if it is colored dont set it
	public static void setShadowN(int[][] shadowP, int i, int j, int v) {
		if (shadowP[i][j] < v)
			shadowP[i][j] = v;
	}

	public static void setShadow(int[][] shadowP, int i, int j) {
		int maxI = shadowP.length;
		int maxJ = shadowP[0].length;
		setShadowN(shadowP, setC(i, maxI), setC(j, maxJ), 2); // set main pos
		setShadowN(shadowP, setC(i - 1, maxI), setC(j, maxJ), 1);
		setShadowN(shadowP, setC(i + 1, maxI), setC(j, maxJ), 1);
		setShadowN(shadowP, setC(i, maxI), setC(j - 1, maxJ), 1);
		setShadowN(shadowP, setC(i, maxI), setC(j + 1, maxJ), 1);

		setShadowN(shadowP, setC(i - 1, maxI), setC(j - 1, maxJ), 1);
		setShadowN(shadowP, setC(i - 1, maxI), setC(j + 1, maxJ), 1);
		setShadowN(shadowP, setC(i + 1, maxI), setC(j - 1, maxJ), 1);
		setShadowN(shadowP, setC(i + 1, maxI), setC(j + 1, maxJ), 1);

		setShadowN(shadowP, setC(i - 2, maxI), setC(j, maxJ), 1);
		setShadowN(shadowP, setC(i + 2, maxI), setC(j, maxJ), 1);
		setShadowN(shadowP, setC(i, maxI), setC(j - 2, maxJ), 1);
		setShadowN(shadowP, setC(i, maxI), setC(j + 2, maxJ), 1);

	}

	/**
	 * GNUPlot type data
	 * 
	 * Shadow means that we calculate
	 * 
	 * @return
	 */
	public StringBuffer getOptimizedEEDataTableArea(int x, boolean explore,
			boolean shadow) {
		StringBuffer sb = new StringBuffer();
		double countx = 0;
		long all = 0;
		boolean[][] mP = new boolean[metrix.length][];
		int[][] shadowP = new int[metrix.length][];
		for (int i = 0; i < metrix.length; i++) {
			mP[i] = new boolean[metrix.length];
			shadowP[i] = new int[metrix.length];
		}
		for (int i = 0; i < metrix.length; i++) {
			if (countx > 0)
				System.out.println("countx=0;=0;?????");
			for (int j = 0; j < metrix[i].length; j++) {
				if ((metrix[i][j].elements != null) && (mP[i][j] == false)) {
					if (explore) {
						if (metrix[i][j].elements.get(0).isRnd()
								|| (metrix[i][j].elements.get(0).getX() > x)) {

							mP[i][j] = true;
							countx++;
							setShadow(shadowP, i, j);

						}

					} else { // exploit
						if ((!metrix[i][j].elements.get(0).isRnd())
								&& (metrix[i][j].elements.get(0).getX() <= x)) {
							mP[i][j] = true;
							setShadow(shadowP, i, j);
							countx++;
						}
					}
				}
				if ((!mP[i][j] && (countx > 0))) { // write prejsnjega scan line
					if (explore)
						sb.append(i).append(" ").append(
								(j - 1) - ((double) countx / 2)).append(" ")
								.append("0.5 " + (countx / 2)).append("\n");
					else
						sb.append(i).append(" ").append(
								(j - 1) - ((double) countx / 2)).append(" ")
								.append("0.5 " + (countx / 2)).append("\n");
					all += countx;
					countx = 0;
				} else {
					if (j == (metrix[i].length - 1)) { // end of the line
						if (explore)
							sb.append(i).append(" ").append(
									(j) - ((double) countx / 2)).append(" ")
									.append("0.5 " + (countx / 2)).append("\n");
						else
							sb.append(i).append(" ").append(
									(j) - ((double) countx / 2)).append(" ")
									.append("0.5 " + (countx / 2)).append("\n");
						countx = 0;
						all += countx;
					}

				}
			}
			// sb.append("\n");
		}
		System.out.println("all:" + all);
		if (shadow == false) {
			return sb;
		}
		// Shadow case
		StringBuffer sbsh = new StringBuffer();
		for (int i = 0; i < metrix.length; i++) { // clear where are already
			// positions
			for (int j = 0; j < metrix[i].length; j++) {
				if ((metrix[i][j].elements != null)) {
					setShadowN(shadowP, i, j, 2); // explore and exploit
				}
			}
		}
		countx = 0;
		for (int i = 0; i < shadowP.length; i++) { // print shadow
			for (int j = 0; j < shadowP[i].length; j++) {
				if (shadowP[i][j] == 1) {
					countx++;
				}
				if ((!(shadowP[i][j] == 1) && (countx > 0))) { // write
					// prejsnjega
					// scan line
					sbsh.append(i).append(" ").append(
							(j - 1) - ((double) countx / 2)).append(" ")
							.append("0.5 " + (countx / 2)).append("\n");
					countx = 0;
				} else {
					if (j == (shadowP[i].length - 1)) { // end of the line
						sbsh.append(i).append(" ").append(
								(j) - ((double) countx / 2)).append(" ")
								.append("0.5 " + (countx / 2)).append("\n");
						countx = 0;
					}

				}
			}
			// sb.append("\n");
		}
		return sbsh;
	}

	/*
	 * type 1-All, 2-Explore, 3-Exploit
	 */
	public int[][] getOptimizedEEFrequencyShadow(int x, StartEnd startEnd,
			int type) {
		int[][] shadowP = new int[metrix.length][];
		for (int i = 0; i < metrix.length; i++) {
			shadowP[i] = new int[metrix.length];
		}
		for (int i = 0; i < metrix.length; i++) {
			for (int j = 0; j < metrix[i].length; j++) {
				if ((metrix[i][j].elements != null) && (shadowP[i][j] < 2)) {
					if (startEnd.isIn(metrix[i][j].elements.size())) {
						if (type == 1) {
							setShadow(shadowP, i, j);
						}
						if (type == 2) {
							if (metrix[i][j].elements.get(0).isRnd()
									|| (metrix[i][j].elements.get(0).getX() > x)) {
								setShadow(shadowP, i, j);
							}
						}

						if (type == 3) {
							if ((!metrix[i][j].elements.get(0).isRnd())
									&& (metrix[i][j].elements.get(0).getX() <= x)) {
								setShadow(shadowP, i, j);
							}
						}

					}
				}
			}
		}
		return shadowP;
	}

	/*
	 * type 1-All, 2-Explore, 3-Exploit
	 */
	public StringBuffer getOptimizedEEFrequency(int x, StartEnd startEnd,
			int type) {
		StringBuffer sb = new StringBuffer();
		double countx = 0;
		long all = 0;
		boolean[][] mP = new boolean[metrix.length][];
		for (int i = 0; i < metrix.length; i++) {
			mP[i] = new boolean[metrix.length];
		}
		for (int i = 0; i < metrix.length; i++) {
			if (countx > 0)
				System.out.println("countx=0;=0;?????");
			for (int j = 0; j < metrix[i].length; j++) {
				if ((metrix[i][j].elements != null)) { //&& (mP[i][j] == false)
					if (startEnd.isIn(metrix[i][j].elements.size())) {
						if (type == 1) {
							mP[i][j] = true;
							countx++;
						}

						if (type == 2) {
							if (metrix[i][j].elements.get(0).isRnd()
									|| (metrix[i][j].elements.get(0).getX() > x)) {
								mP[i][j] = true;
								countx++;
							}
						}

						if (type == 3) {
							if ((!metrix[i][j].elements.get(0).isRnd())
									&& (metrix[i][j].elements.get(0).getX() <= x)) {
								mP[i][j] = true;
								countx++;
							}
						}

					}
				}
				if (!mP[i][j] && (countx > 0)) { // write
					// prejsnjega
					// scan
					// line
					sb.append(i).append(" ").append(
							(j - 1) - ((double) countx / 2)).append(" ")
							.append("0.5 " + ((double) countx / 2)).append(
									"\n");
					all += countx;
					countx = 0;
				} else {
					if ((j == (metrix[i].length - 1)) && (countx > 0)) { // end
																			// of
																			// the
																			// line
						sb.append(i).append(" ").append(
								(j) - ((double) countx / 2)).append(" ")
								.append("0.5 " + ((double) countx / 2))
								.append("\n");
						countx = 0;
						all += countx;
					}

				}
			}
			// sb.append("\n");
		}
		System.out.println("all:" + all);
		return sb;
	}

	public static void cleanShadow4Points(int allByParts[][][]) {
		boolean b[] = new boolean[allByParts.length];
		// Redistribute info for full
		for (int i = 0; i < allByParts.length; i++) {
			for (int j = 0; j < allByParts[i].length; j++) {
				for (int k = 0; k < allByParts[i][j].length; k++) {
					if (allByParts[i][j][k] > 1) {
						for (int ii = 0; ii < allByParts.length; ii++) {
							allByParts[ii][j][k] = allByParts[i][j][k];
						}
					}
				}
			}
		}
		boolean full = false;
		int selected;
		StackFix sf = new StackFix(allByParts.length);
		for (int i = 0; i < allByParts[0].length; i++) {
			for (int j = 0; j < allByParts[0][i].length; j++) {
				// is full (if top works only one need check not all loop
				full = false;
				for (int k = 0; k < allByParts.length; k++) {
					if (allByParts[k][i][j] > 1) {
						full = true;
						break;
					}
				}
				if (!full) {
					for (int k = 0; k < allByParts.length; k++) {
						if (allByParts[k][i][j] == 1) {
							b[k] = true;
						} else
							b[k] = false;
					}
					selected = sf.selectIndex(b); // select one
					if (selected > -1) {
						for (int k = 0; k < allByParts.length; k++) {
							if (allByParts[k][i][j] == 1) {
								if (k != selected) { // erase others
									allByParts[k][i][j] = 0;
								}
							}
						}

					}
				}
			}
		}
	}

	/*
	 * type 1-All, 2-Explore, 3-Exploit
	 */
	public static StringBuffer getOptimizedEEFrequencyShadow(int allByParts[][]) {
		StringBuffer sb = new StringBuffer();
		double countx = 0;
		for (int i = 0; i < allByParts.length; i++) {
			for (int j = 0; j < allByParts[i].length; j++) {
				if (allByParts[i][j] == 1) {
					countx++;
				}

				if ((allByParts[i][j] != 1) && (countx > 0)) { // write
					// prejsnjega
					// scan
					// line ali konec vrstice
					sb.append(i).append(" ").append(
							(j - 1) - ((double) countx / 2)).append(" ")
							.append("0.5 " + ((double) countx / 2))
							.append("\n");
					countx = 0;
				}

				else {
					if ((j == (allByParts[i].length - 1)) && (countx > 0)) { //
						// end of the line
						sb.append(i).append(" ").append(
								(j) - ((double) countx / 2)).append(" ")
								.append("0.5 " + ((double) countx / 2)).append(
										"\n");
						countx = 0;
					}

				}
			}

			// sb.append("\n");
		}
		return sb;
	}

}
