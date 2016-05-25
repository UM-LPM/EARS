package org.um.feri.ears.statistic.friedman;

public class PairStatistics {
	
	public String first;
	public String second;
	public double pNeme;
	public double pHolm;
	public double pShaf;
	
	public PairStatistics(String aFirst, String aSecond){
	   this.first = aFirst;
	   this.second = aSecond;
	}
	
	public void setNeme(double aPNeme){
		this.pNeme = aPNeme;
	}
	
	public void setHolm(double aPHolm){
		this.pHolm = aPHolm;
	}
	
	public void setShaf(double aPShafe){
		this.pShaf = aPShafe;
	}
	
	public int getNeme(double alpha){
		if(pNeme<alpha) return 1;
		else return 0;
	}
	
	public int getHolm(double alpha){
		if(pHolm<alpha) return 1;
		else return 0;
	}
	
	public int getShaf(double alpha){
		if(pShaf<alpha) return 1;
		else return 0;
	}

}
