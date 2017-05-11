package org.um.feri.analyse.EE.util;

public class StartEnd {
	private int start, end;

	public StartEnd(int start, int end) {
		super();
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}
	public boolean isIn(int i) {
		if ((i>=start) && (i<=end)){
			return true;
		}
		return false;
	}

}
