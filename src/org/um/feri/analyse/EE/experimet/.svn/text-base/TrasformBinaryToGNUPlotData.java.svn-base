package atree.experimet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import atree.treeData.Node;
import atree.util.GrayMetrics;


public class TrasformBinaryToGNUPlotData {
	public static String rootPath="/Users/matej/Documents/clanki2010/IEEETec/ResultData/2_18/";

	public static void allPossible() throws IOException{
		GrayMetrics gm = new GrayMetrics(18);
		BufferedReader br=new BufferedReader(new FileReader(rootPath+"/AllPossible.txt"));
		//preberem prvo vrstico
		String vrstica="";
	    vrstica=br.readLine();
	    Node n;
		while(vrstica != null)
    	{
			n=new Node(0,0,vrstica.trim(),false,false, false,0);
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
	    Node n;
		while(vrstica != null)
    	{
			n=new Node(0,0,vrstica.trim(),false,false, false,0);
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
