package org.um.feri.ears.memory;

import java.util.Arrays;

import org.um.feri.ears.problems.Task;

public class DuplicationRemovalStrategyLeaveIt extends DuplicationRemovalStrategy {
	//This is strategy without strategy. It doesn't use knowledge about it
	public DuplicationRemovalStrategyLeaveIt() {
	}
	
	@Override
	public void changeSolution(double[] x) {
		//do not change it, it will take additional evaluation
	}
	@Override
	public boolean criteria4Change(int hits) {
		return true; //always
	}
	public String toString( ) {
		StringBuffer sb = new StringBuffer();
		sb.append(Class.class.getSimpleName());
		return sb.toString();
	}
}
