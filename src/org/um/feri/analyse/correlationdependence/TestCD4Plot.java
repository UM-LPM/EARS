package org.um.feri.analyse.correlationdependence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.um.feri.analyse.util.Util;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.ProblemSphere;
import org.um.feri.ears.problems.unconstrained.cec2014.*;

public class TestCD4Plot {
	private static final String out_dir = "D:/Doktorat/OutPutEars/CF/cec2014/";
	private static ArrayList<String> names = new  ArrayList<String>();
	private static ArrayList<String> labels = new  ArrayList<String>();
	private static ArrayList<Problem> problems = new  ArrayList<Problem>();
	private static ArrayList<HashMap<Double, Integer>> cds = new  ArrayList<HashMap<Double, Integer>>();
	
	public static void createFile(Problem p, String fileName, String label) {
	/*	names.clear();
		labels.clear();
		problems.clear();
		cds.clear();*/
		HashMap<Double, Integer> cdMap = new HashMap<>();
		names.add(fileName);
		labels.add(label);
		problems.add(p);
		CorrelationAndDependence cd;
		cd = new CorrelationAndDependence(p, 3000000);
		cdMap.put(cd.r_xy, cd.getGraphArea(100, 100));
		cds.add(cdMap);
		try {
			// Create file
			FileWriter fstream = new FileWriter(out_dir+fileName+".dat");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(cd.discriteGraph(100, 100));
			// Close the output stream
			out.close();
			
			fstream = new FileWriter(out_dir+"plot.plt",true);
			out = new BufferedWriter(fstream);
			out.write("set output \""  + fileName + ".eps\"\n" );
			out.write("plot \""+fileName+".dat\" using 1:2:5 with points palette title \""+label+"\"\n");
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		System.out.println("Problem "+label+" CD="+Util.df3.format(cd.r_xy) + " Area="+cdMap.get(cd.r_xy));
	}
	public static void latexInclude(int dim) {
		try {
			FileWriter fstream = new FileWriter(out_dir + "CD"+dim +".tex");
			BufferedWriter out = new BufferedWriter(fstream);
			for (int i = 0; i < names.size(); i++) {
				HashMap<Double, Integer> cdMap = cds.get(i);
				Map.Entry<Double, Integer> ent = (Map.Entry<Double, Integer>) cdMap.entrySet().toArray()[0];
 				String a = names.get(i);
				// Create file
				out.write("\\begin{figure}[!htb]\n");
				out.write("\\includegraphics[width=\\textwidth]{" + a + ".pdf}\n");
				out.write("\\caption{Problem " + labels.get(i) + " CD=" + Util.df3.format(ent.getKey()) + " Area="+ent.getValue()+ " }\n");
				out.write("\\label{fig:" + names.get(i) + "}");
				out.write("\\end{figure}\n");
				if (i % 9 == 8) {
					out.write("\\clearpage\n");
				}
				// Close the output stream
			}
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int dimm[] = {2,10,20,30};
		int numOfProblems = 16;
		Problem problems[] = new Problem[numOfProblems];
		
		for (int nDim = 0; nDim < dimm.length; ++nDim )
		{
			problems[0] = new F1(dimm[nDim]);
			problems[1] = new F2(dimm[nDim]);
			problems[2] = new F3(dimm[nDim]);
			problems[3] = new F4(dimm[nDim]);
			problems[4] = new F5(dimm[nDim]);
			problems[5] = new F6(dimm[nDim]);
			problems[6] = new F7(dimm[nDim]);
			problems[7] = new F8(dimm[nDim]);
			problems[8] = new F9(dimm[nDim]);
			problems[9] = new F10(dimm[nDim]);
			problems[10] = new F11(dimm[nDim]);
			problems[11] = new F12(dimm[nDim]);
			problems[12] = new F13(dimm[nDim]);
			problems[13] = new F14(dimm[nDim]);
			problems[14] = new F15(dimm[nDim]);
			problems[15] = new F16(dimm[nDim]);
			
			
			
			for(int nProb = 0; nProb < numOfProblems; ++nProb)
			{
				createFile(problems[nProb], problems[nProb].getName() + problems[nProb].getNumberOfDimensions(),
						                    problems[nProb].getName() + " (D=" + problems[nProb].getNumberOfDimensions() + ")");
	
				System.out.println("");
				
			}
			
			latexInclude(dimm[nDim]);
		}
		
			
			
		
	}

}
