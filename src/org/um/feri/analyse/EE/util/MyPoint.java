package org.um.feri.analyse.EE.util;

public class MyPoint {
	private int x,y;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public MyPoint(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public String toString() {
		return x+" "+y;
	}
	

}
