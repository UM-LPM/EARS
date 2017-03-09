package org.um.feri.analyse.correlationdependence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import org.um.feri.analyse.util.Util;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.*;

public class TestCD4Plot {
	private static final String out_dir = "D:/Doktorat/OutPutEars/CF/";
	private static ArrayList<String> names = new  ArrayList<String>();
	private static ArrayList<String> labels = new  ArrayList<String>();
	private static ArrayList<Problem> problems = new  ArrayList<Problem>();
	private static ArrayList<Double> cds = new  ArrayList<Double>();
	public static void createFile(Problem p, String fileName, String label) {
	/*	names.clear();
		labels.clear();
		problems.clear();
		cds.clear();*/
		
		names.add(fileName);
		labels.add(label);
		problems.add(p);
		CorrelationAndDependence cd;
		cd = new CorrelationAndDependence(p, 3000000);
		cds.add(cd.r_xy);
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
			out.write("plot \""+fileName+".dat\" using 1:2 title \""+label+"\"\n");
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		System.out.println("Problem "+label+" CD="+Util.df3.format(cd.r_xy));
	}
	public static void latexInclude() {
		try {
			FileWriter fstream = new FileWriter(out_dir + "CD.tex");
			BufferedWriter out = new BufferedWriter(fstream);
			for (int i = 0; i < names.size(); i++) {
				String a = names.get(i);
				// Create file
				out.write("\\begin{figure}[!htb]\n");
				out.write("\\includegraphics[width=\\textwidth]{" + a + ".pdf}\n");
				out.write("\\caption{Problem " + labels.get(i) + " CD=" + Util.df3.format(cds.get(i)) + " }\n");
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

		int dimm[] = {10,50,100};
		int numOfProblems = 5;
		Problem problems[] = new Problem[numOfProblems];
		
		for (int nDim = 0; nDim < dimm.length; ++nDim )
		{
			problems[0] = new ProblemSphere(dimm[nDim]);
			problems[1] = new ProblemGriewank(dimm[nDim]);
			problems[2] = new ProblemRastrigin(dimm[nDim]);
			problems[3] = new ProblemRosenbrock(dimm[nDim]);
			problems[4] = new ProblemSchwefel(dimm[nDim]);
			
			for(int nProb = 0; nProb < numOfProblems; ++nProb)
			{
				createFile(problems[nProb], problems[nProb].getName() + problems[nProb].getNumberOfDimensions(),
						                    problems[nProb].getName() + " (D=" + problems[nProb].getNumberOfDimensions() + ")");
	
				System.out.println("");
				
			}
		}
		latexInclude();
		
	}

}
