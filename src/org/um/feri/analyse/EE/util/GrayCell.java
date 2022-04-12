package org.um.feri.analyse.EE.util;

import java.util.ArrayList;

import org.um.feri.analyse.EE.tree_data.NodeEARS;


public class GrayCell {

	ArrayList<NodeEARS> elements;
	public static final int MUTATION=1; 
	public static final int CROSSOVER=2; 
	public static final int REPAIR=3;
	public static final int RANDOM=4; 
	public static final int EXPLOR=5; 
	public static final int EXPLIT=6; 
	public GrayCell() {
		super();
	}
	public void reset() {
	}
	public void add(NodeEARS n) {
		if (elements==null) elements = new ArrayList<NodeEARS>();
		elements.add(n);
		
	}
	/**
	 * @param args�
	 */
	public static void main(String[] args) {


	}

}
