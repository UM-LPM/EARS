package org.um.feri.analyse.EE.metrics;

import java.util.ArrayList;

import org.um.feri.analyse.EE.treeData.NodeEARS;
import org.um.feri.analyse.EE.util.MeanStDev;


public class ATMetrics {
	private ArrayList<NodeEARS> initTrees;
	private ArrayList<NodeEARS> splitTrees; // only tree root nodes
	private ArrayList<NodeEARS> allNodes; 
	private double x;
	private long count;
	private long differentSolutions;

	
	public ATMetrics(ArrayList<NodeEARS> initTrees, double x) {
		super();
		this.initTrees = initTrees;
		this.x = x;
		splitTrees = new ArrayList<NodeEARS>();
		allNodes = new ArrayList<NodeEARS>();
		count = 0;
		differentSolutions=0;
		fillRootLeafsAndCount();
		setRevisitedAll();
	}
    /**
     * TODO implement and test epsilon revisited criteria
     * 
     * @param epsilons
     */
	private void setRevisitedAll(double epsilons[]) {
		NodeEARS tmp, tmp2;
		int k;
		int max=allNodes.get(0).chromo.length();
		String s1; String s2;
		//ArrayList<NodeEARS> allNodesCopy = new ArrayList<NodeEARS>(); 
		for (int i=0; i < allNodes.size(); i++) {
			tmp = allNodes.get(i);
			if (!tmp.isRevisited()) {
				differentSolutions++;
				for (int j = i+1; j < allNodes.size(); j++) {
			//	for (int j = allNodes.size()-1; j > i; j--) {
					tmp2=allNodes.get(j);
					if (tmp.ones1==tmp2.ones1) 
						if (tmp.ones2==tmp2.ones2)
							if (tmp.ones3==tmp2.ones3){
								s1=tmp.chromo;
								s2=tmp2.chromo;
								max = s1.length();
								if (max!=s2.length()) {
									break;	
								}
								for (k=0;k<max;k++) {
									if (s1.charAt(k)!=s2.charAt(k)) {
										break;
									}
								}
								if (k==max) {
									tmp.addRevisited(); //same solution!
									tmp2.setRevisited(true);	
								}
		//			  if (tmp.getChromo().equals(tmp2.getChromo())) { SLOW
		//				tmp.addRevisited(); //same solution!
		//				tmp2.setRevisited(true);
		//			  } 
					}
				}
			}
		}
	}
 
	private void setRevisitedAll() {
		NodeEARS tmp, tmp2;
		int k;
		int max=allNodes.get(0).chromo.length();
		String s1; String s2;
		//ArrayList<NodeEARS> allNodesCopy = new ArrayList<NodeEARS>(); 
		for (int i=0; i < allNodes.size(); i++) {
			tmp = allNodes.get(i);
			if (!tmp.isRevisited()) {
				differentSolutions++;
				for (int j = i+1; j < allNodes.size(); j++) {
			//	for (int j = allNodes.size()-1; j > i; j--) {
					tmp2=allNodes.get(j);
					if (tmp.ones1==tmp2.ones1) 
						if (tmp.ones2==tmp2.ones2)
							if (tmp.ones3==tmp2.ones3){
								s1=tmp.chromo;
								s2=tmp2.chromo;
								max = s1.length();
								if (max!=s2.length()) {
									break;	
								}
								for (k=0;k<max;k++) {
									if (s1.charAt(k)!=s2.charAt(k)) {
										break;
									}
								}
								if (k==max) {
									tmp.addRevisited(); //same solution!
									tmp2.setRevisited(true);	
								}
		//			  if (tmp.getChromo().equals(tmp2.getChromo())) { SLOW
		//				tmp.addRevisited(); //same solution!
		//				tmp2.setRevisited(true);
		//			  } 
					}
				}
			}
		}
	}
	public double nonRevisitedRatio() {
		// System.out.println(splitTrees.size()+"/"+count);
		return Util.divide(differentSolutions,count);
	}
	
	public long getCount() {
		return count;
	}

	private void fillRootLeafsAndCount() {
		splitTrees.addAll(initTrees);
		for (NodeEARS n : initTrees) {
			count++;
			allNodes.add(n);
			for (NodeEARS e : n.getChildrens()) {
				recursiveFillRootTopDown(e);
			}
		}
	}

	private void recursiveFillRootTopDown(NodeEARS e) {
		count++;
		allNodes.add(e);
		if (e.getX() >= x) {
			splitTrees.add(e);
		}
		for (NodeEARS n : e.getChildrens()) {
			recursiveFillRootTopDown(n);
		}
	}

	public double explorRatio() {
		// System.out.println(splitTrees.size()+"/"+count);
		return Util.divide(splitTrees.size(),count);
	}
	public double exploitRatio() {
		// System.out.println(splitTrees.size()+"/"+count);
		return (double) 1-explorRatio();
	}

    // First parent A 
	private NodeEARS getParentATree(NodeEARS e) {
		if (e.getParent()==null) return null; //first no parent
		NodeEARS p=e.getParent();
		while (p.getX()<=x) {
			if (p.getParent()==null) return p; //Last is root
			p=p.getParent();
		}
		return p;	
	}
    // TreeDepth A 
	private int getTreeDepth(NodeEARS e) {
		int i=0;
		do {
		  NodeEARS t = getParentATree(e);
		  if (t!=null) i++; 
		  else break;
		  e=t;
		  
		} while (true);
		return i;
		
	}
	public MeanStDev explorGap() {
	  ArrayList<Double> population = new ArrayList<Double>();
		for (NodeEARS t : splitTrees) {
			if (t.getParent()!=null) {
				population.add((double) (t.getIdGen() - getParentATree(t).getIdGen()));
			}
		}
	  return new MeanStDev(population);
	}

	public MeanStDev explorProgressiveness() {
		  ArrayList<Double> population = new ArrayList<Double>();
			for (NodeEARS t : splitTrees) {
				if (t.getParent()!=null) {
					population.add((double) getTreeDepth(t));
				}
			}
		  return new MeanStDev(population);
		}

	
	public double explorType(int type) {
		long countType = 0;
		long countAllType = 0;
		for (NodeEARS t : splitTrees) {

			if (t.getParent() == null) {
				countAllType++;
				if (type == 3) { // random init tree
					countType++;
				}
				continue;// skip count for root
			}
		}
		//System.out.println(countType + "/" + countAllType);
		return Util.divide(countType,countAllType);
	}
	
	private double countExploitType(int type, NodeEARS tree) {
		double countType = 0;
		return countType;
	}
    /*
     * Skips root
     */
    private double countExploitTypeNode(int type, NodeEARS tree) {
    	double sumType=0;
		for (NodeEARS n : tree.getChildrens()) {
    	  sumType +=countExploitType(type,n);
		}
    	return sumType;
    }
    /*
     * Average for NodeEARS
     */    
    public double exploitTypeNode(int type, NodeEARS n) {
    	double summAll=0, sumType=0;
    	summAll=countExploitTypeNode(0,n)+countExploitTypeNode(1,n)+countExploitTypeNode(2,n)+countExploitTypeNode(4,n);
    	sumType +=countExploitTypeNode(type,n);
    	return Util.divide(sumType,summAll);
    }

    /*
     * Average for NodeEARS
     */    
    public double exploitType(int type) {
    	double summAll=0, sumType=0;
    	for (NodeEARS n:splitTrees) {
    	  summAll+=countExploitTypeNode(0,n)+countExploitTypeNode(1,n)+countExploitTypeNode(2,n)+countExploitTypeNode(4,n);
    	  sumType +=countExploitTypeNode(type,n);
    	}
    	return Util.divide(sumType,summAll);
    }
    public double exploitSelectionPressure() {
    	int leafs=0;
    	for (NodeEARS n:splitTrees) {    		
        	for (NodeEARS nn:n.getChildrens()) {
        		leafs+=countLeafs(nn);
        	}
    	}
    	return Util.divide(leafs,count);
    }

	private int countLeafs(NodeEARS nn) {
		if (nn.getX()>=x) return 0;
		if (nn.getChildrens().size()==0) {
			return 1; //NodeEARS
		}
		int leafs=0;
		boolean l=true; //one chiled is not leaf then it is not leaf 
       	for (NodeEARS n:nn.getChildrens()) {
           	if (nn.getX()<x) {
           		l=false;
        		leafs+=countLeafs(n);
           	}
    	}
       	if (l) leafs++;
		return leafs;
	}
    
	private int firstExploreParent(NodeEARS n) {
		int i=1;
		while (n.getParent()!=null) {
			n=n.getParent();
			if (n.getX()>=x) { //explore condition
				break;
			}
			i++; //one level more
		}
		return i;
	}
	public MeanStDev exploitProgressiveness() {
		  ArrayList<Double> population = new ArrayList<Double>();
			for (NodeEARS t : allNodes) {
				if (t.getX()<x) { //explore condition
					population.add((double) firstExploreParent(t)); //TODO Very slow...
				}
			}
		  return new MeanStDev(population);

	}

	/*
	 * if NodeEARS nn has explore successor than it is not leaf 
	 */
	private boolean isExploreLeaf(NodeEARS nn) {
		for (NodeEARS n:nn.getChildrens()) {
           	if (n.getX()>=x) { //explore condition
           		return false; //has explore leaf
           	} else {
           		if (!isExploreLeaf(n)) return false;
           	}  		
    	}
		return true;
	}
	//Number of leafs in explore tree
	public double exploreSelectionPressure() {
    	int leafs=0;
    	for (NodeEARS n:splitTrees) { 
    		//
    		if (isExploreLeaf(n)) {
        		leafs++;
    		}
    	}
    	return Util.divide(leafs,splitTrees.size());
		//return 0;
	}
	
	public double bestFitness()
	{
		double bestFit = Double.MAX_VALUE;
		for (NodeEARS n:allNodes)
		{
			double fit = Math.abs(n.getFit());

			bestFit = (fit < bestFit) ? fit : bestFit;
		}
		//System.out.println("Best fitness: "+bestFit);
		return bestFit;
	}

}
