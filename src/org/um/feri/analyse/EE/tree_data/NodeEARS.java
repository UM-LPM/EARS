package org.um.feri.analyse.EE.tree_data;

import java.util.ArrayList;

import org.um.feri.analyse.EE.util.LineParserECJ;

public class NodeEARS implements Comparable<NodeEARS> {
	private NodeEARS parent;
	private ArrayList<NodeEARS> childrens;
	private long revisits;
	private long idGen;
	private double finess;
	public String chromo; //just for fast compare!!!
	private boolean pareto;
	private boolean revisited;
	public int ones1, ones2, ones3;
	public boolean isRevisited() {
		return revisited;
	}
	public void setRevisited(boolean revisited) {
		this.revisited = revisited;
	}
	public boolean isTmp() {
		return tmp;
	}
	public void setTmp(boolean tmp) {
		this.tmp = tmp;
	}

	private boolean tmp;

	private double x;

	public NodeEARS() {
		super();
		childrens = new ArrayList<NodeEARS>();
		chromo = new String();
		revisits = 0;
		parent = null;
		pareto = false;
		tmp=false;
	}
	public void addRevisited()  {
		revisits++;
	}
	public boolean isPareto() {
		return pareto;
	}
	public void setPareto(boolean pareto) {
		this.pareto = pareto;
	}
	public NodeEARS(long idGen, String chromo, double x) {
		this();
		this.idGen = idGen;
		setChromo(chromo);
		this.x = x;
	}


	public NodeEARS getParent() {
		return parent;
	}
	public void addChild(NodeEARS c) {
		childrens.add(c);
	}
	public void setParent(NodeEARS parent) {
		this.parent = parent;
		if (parent!=null)
		parent.addChild(this);
	}

	public ArrayList<NodeEARS> getChildrens() {
		return childrens;
	}
	public void setChildrens(ArrayList<NodeEARS> childrens) {
		this.childrens = childrens;
	}
	public long getIdGen() {
		return idGen;
	}
	public void setIdGen(long idGen) {
		this.idGen = idGen;
	}

	public String getChromo() {
		return chromo;
	}
	public void setChromo(String chromo) {
		
		if(this.chromo.isEmpty())
		{
			this.chromo += chromo;
		}
		else
		{
			this.chromo += " " + chromo;
		}
	}

	public boolean isLeaf() {
		if (childrens.size()==0) return true;
		return false;
	}


	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public boolean isRnd() {
		return 	(parent==null);
	}
	public void setFit(double fitness)
	{
		this.finess = fitness;
	}
	public double getFit()
	{
		return this.finess;
	}
	@Override
	public int compareTo(NodeEARS o) {
        if (o.idGen==idGen) return 0;
        if (o.idGen>idGen) return -1;
        if (o.idGen==idGen) return -1;
        return 1;
	}
	public String getID() {
		return "("+getIdGen()+")";
	}
	public String getID(NodeEARS n) {
		if (n==null) return "(1)";
		return n.getID();
	}

	public String printChildrens() {
		StringBuffer sb= new StringBuffer();
		for (NodeEARS t:childrens) {
			sb.append(t.getID()).append(" ");
		}
		return sb.toString();
	}
	public String toString() {
		String t=getID()+" "+getID(parent)+" "+getChromo()+" "+getX()+" "+printChildrens()+" "+isPareto()+" "+isTmp();
		return t;
	}
	
	
	public static NodeEARS convert4String(String line, NodesEARS all, double epsilon) 
	{
		LineParserECJ lp = new LineParserECJ(line, ";");
		NodeEARS r = new NodeEARS();
		ArrayList <NodeEARS> p = new ArrayList<NodeEARS>();
		String id;
		String key;
		while (lp.getState()!=LineParserECJ.EOF) {
			switch (lp.getState()) {
			case LineParserECJ.ID:
				id = lp.getValue();
				r.setIdGen(Long.parseLong(id));
				break;
			case LineParserECJ.P:
				id = lp.getValue().trim();
				key= "("+id+")";
				if (all.containsKey(key)) p.add(all.get(key));
				break;
			case LineParserECJ.IN:
				r.setChromo(lp.getValue().trim());
				break;
			case LineParserECJ.FITNESS:
				r.setFit(lp.getDoubleVaue());
				break;
			}
			lp.nextState();
		}
		
		//find most similar parent
		NodeEARS parent = r;
		double parentX = Double.MAX_VALUE;
		for(NodeEARS n:p)
		{
			double x = calcX(r, n, epsilon);
			if(x < parentX)
			{
				parent = n;
				parentX = x;
			}
		}
		
		if(!p.isEmpty())
		{
			r.setParent(parent);
			r.setX(parentX);
		}
		
		return r;
	}
	
	//Example: id;fitness;chromo[,,,];parents[,,,]
	public static NodeEARS convert4String(String line, NodesEARS all, double epsilon[]) 
	{
		LineParserECJ lp = new LineParserECJ(line, ";");
		NodeEARS r = new NodeEARS();
		ArrayList <NodeEARS> p = new ArrayList<NodeEARS>();
		String id;
		String key;
		while (lp.getState()!=LineParserECJ.EOF) {
			switch (lp.getState()) {
			case LineParserECJ.ID:
				id = lp.getValue();
				r.setIdGen(Long.parseLong(id));
				break;
			case LineParserECJ.P:
				id = lp.getValue();
				key= "("+id+")";
				if (all.containsKey(key)) p.add(all.get(key));
				break;
			case LineParserECJ.IN:
				r.setChromo(lp.getValue().trim());
				break;
			case LineParserECJ.FITNESS:
				r.setFit(lp.getDoubleVaue());
				break;
			}
			lp.nextState();
		}

		//find most similar parent
		NodeEARS parent = r;
		double parentX = Double.MAX_VALUE;
		for(NodeEARS n:p)
		{
			double x = calcX(r, n, epsilon);
			if(x < parentX)
			{
				parent = n;
				parentX = x;
			}
		}
		
		r.setParent(parent);
		r.setX(parentX);
		
		return r;
	}
	

	
	private static double calcX(NodeEARS r, NodeEARS p, double[] epsilon) {
		if (p==null) return Integer.MAX_VALUE;
		String l[] = r.chromo.split(" ");
		double d1,d2;
		String lp[] = p.chromo.split(" ");
		double x=0;
		for (int i=0; i<l.length; i++) {
			d1 = Double.parseDouble(l[i]); 
			d2 = Double.parseDouble(lp[i]);
			if (Math.abs(d1-d2)>epsilon[i]) x++;
		}
		return x;
	}
	
	private static double calcX(NodeEARS r, NodeEARS p, double epsilon) {
		if (p==null) return Integer.MAX_VALUE;
		String l[] = r.chromo.split(" ");
		double d1,d2;
		double diff = 0;
		String lp[] = p.chromo.split(" ");
		double x=0;
		for (int i=0; i<l.length; i++) {
			d1 = Double.parseDouble(l[i]); 
			d2 = Double.parseDouble(lp[i]);
			diff += Math.abs(d1-d2);
			
		}
		if (diff>epsilon) x = diff;
		return x;
	}
	
}
