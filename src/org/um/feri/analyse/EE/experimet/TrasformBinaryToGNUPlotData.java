package org.um.feri.analyse.EE.experimet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.um.feri.analyse.EE.treeData.NodeEARS;
import org.um.feri.analyse.EE.util.GrayMetrics;


public class TrasformBinaryToGNUPlotData {
	public static String rootPath="/Users/matej/Documents/clanki2010/IEEETec/ResultData/2_18/";

	public static void allPossible() throws IOException{
		GrayMetrics gm = new GrayMetrics(18);
		BufferedReader br=new BufferedReader(new FileReader(rootPath+"/AllPossible.txt"));
		//preberem prvo vrstico
		String vrstica="";
	    vrstica=br.readLine();
	    NodeEARS n;
		while(vrstica != null)
    	{
			n=new NodeEARS(0,vrstica.trim(),0);
			vrstica=br.readLine();
			gm.add(n);
    	}
		PrintWriter fw = new PrintWriter(rootPath+"/AllPossibleData.txt");
		fw.append(gm.getOptimizedDataTableArea(0));
		fw.flush();
		fw.close();
		
	
	}
	
	public static void allPossiblePareto1() throws IOException{
		GrayMetrics gm = new GrayMetrics(18);
		BufferedReader br=new BufferedReader(new FileReader(rootPath+"/ParetoBitValues.txt"));
		//preberem prvo vrstico
		String vrstica="";
	    vrstica=br.readLine();
	    NodeEARS n;
		while(vrstica != null)
    	{
			n=new NodeEARS(0,vrstica.trim(),0);
			vrstica=br.readLine();
			gm.add(n);
    	}
		PrintWriter fw = new PrintWriter(rootPath+"/ParetoPlotData.txt");
		fw.append(gm.getExplore(0));
		fw.flush();
		fw.close();
		
	
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		allPossiblePareto1();
	}

}
