package org.um.feri.analyse.EE.tree_data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class NodesEARS {
	private Hashtable<String, NodeEARS> ht;
	private ArrayList<NodeEARS> initTrees;
	private ArrayList<NodeEARS> paretoList;
	public static final int SCENARIO_NORMAL = 0;
	public static final int SCENARIO_OPTIMISTIC = 1;
	public static final int SCENARIO_SEMI_OPTIMISTIC = 2;
	public Hashtable<String, NodeEARS> getAllNodesEARS() {
		return ht;
	}
	public boolean containsKey(Object key) {
		return ht.containsKey(key);
	}

	public ArrayList<NodeEARS> getInitTrees() {
		return initTrees;
	}

	public NodeEARS put(NodeEARS value) {
		if (value.getParent() == null)
			initTrees.add(value);
		return ht.put(value.getID(), value);
	}

	public NodeEARS get(String key) {
		return ht.get(key);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (NodeEARS n : ht.values()) {
			sb.append(n.toString()).append("\n");
		}
		return sb.toString();
	}

	public NodesEARS() {
		super();
		ht = new Hashtable<String, NodeEARS>();
		initTrees = new ArrayList<NodeEARS>();
		paretoList = new ArrayList<NodeEARS>();
	}

	/*
	 * Only pareto parents and pareto inniduals survy 
	 */
	public void transformInOptimisticParetoTree() {
		for (NodeEARS n:paretoList) { //set all relavant
			while (n!=null) {
				n.setTmp(true);
				n=n.getParent();
			}
		}
		//Remove from initTrees
		for (int i=initTrees.size()-1;i>-1;i--) {
			if (!initTrees.get(i).isTmp()) initTrees.remove(i);
		}
		//Remove from tree
		for (NodeEARS n:paretoList) { //set all relavant
			while (n!=null) {
				for (int j=n.getChildrens().size()-1;j>=0;j--) {
					if (!n.getChildrens().get(j).isTmp()) {
						n.getChildrens().remove(j);
					}
				}
				n=n.getParent();
			}
		}
	}
	/*
	 * Only pareto  
	 */
	public void transformInOptimisticPlusParetoTree() {
		for (NodeEARS n:paretoList) { //set all relavant
			while (n!=null) {
				n.setTmp(true);
				n=n.getParent();
				if (n!=null) {
					for (NodeEARS nn:n.getChildrens()) {
						nn.setTmp(true);
					}
				}
			}
		}
		//Remove from initTrees
		for (int i=initTrees.size()-1;i>-1;i--) {
			if (!initTrees.get(i).isTmp()) initTrees.remove(i);
		}
		//Remove from tree
		for (NodeEARS n:paretoList) { //set all relavant
			while (n!=null) {
				for (int j=n.getChildrens().size()-1;j>=0;j--) {
					if (!n.getChildrens().get(j).isTmp()) {
						n.getChildrens().remove(j);
					}
				}
				n=n.getParent();
			}
		}
	}

	/**
	 * Skips comment or blank line
	 * 
	 * @param br
	 * @return
	 * @throws IOException 
	 */
	private String getNextLine(BufferedReader br) throws IOException {
		boolean comment=false;
		String s="";
		while ((s=br.readLine()) != null) {
			if (s.trim().length()==0) continue;
			
			break;
		}
		return s;
	}
	/**
	 * EARS format
	 * id;fitness;chromo[,,,];parents[,,,]
	 * ...
	 *  
	 * @param file
	 * @param maxgen
	 * @param useMaxGen calculate statistic for only first maxgen
	 */
	public void createAll_EARS(String file, int maxgen, double epsilon[], boolean useMaxGen) {
		FileReader fr;
		int currGen=0;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while(null != (s = br.readLine())) {
				//new individual create NodeEARS
				put(NodeEARS.convert4String(s, this, epsilon));	
				
			}
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * EARS format
	 * id;fitness;chromo[,,,];parents[,,,]
	 * 
	 * Calculates the EE statistics with sum of diff on dimensions
	 *  
	 * @param file
	 * @param maxgen
	 * @param useMaxGen calculate statistic for only first maxgen
	 */
	public void createAll_EARS(String file, int maxgen, double epsilon, boolean useMaxGen) 
	{
		FileReader fr;
		int currGen=0;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while(null != (s = br.readLine())) 
			{
				put(NodeEARS.convert4String(s, this, epsilon));
			}
			fr.close();
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
