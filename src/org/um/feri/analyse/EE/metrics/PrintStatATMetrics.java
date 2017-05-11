package org.um.feri.analyse.EE.metrics;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class PrintStatATMetrics {
	
	public StatATMetrics m;
	NumberFormat formatter, stdFormat;

	public PrintStatATMetrics(StatATMetrics m) {
		super();
		this.m = m;
		formatter = new DecimalFormat("#0.000");
		stdFormat = new DecimalFormat("#.00");
	}
	
	public static String toLatex(ArrayList<String> heads, ArrayList<ArrayList<String>> cols, String tableCaption) {
		StringBuffer sb = new StringBuffer();
		sb.append("\\begin{table}[!hpb]\n");
		sb.append("\\begin{center}\n");
		sb.append("\\begin{tabular}{@{}l@{\\hspace{1pt}}||@{\\hspace{1pt}}");
		for (int i=0;i<heads.size();i++) {
			sb.append("@{\\hspace{1pt}}r");
			if (i<(heads.size()-1))sb.append("@{\\hspace{1pt}} | ");
			else sb.append("} \n ");
		}
		sb.append("Metrics &");
		for (int i=0;i<heads.size();i++) {
			sb.append(heads.get(i));
			if (i<(heads.size()-1))sb.append(" & ");
			else sb.append("\\\\ \\hline\\hline\n");
		}
		for (int j=0;j<cols.get(0).size();j++) {
		  for (int i=0;i<cols.size();i++) {
				sb.append(cols.get(i).get(j));
				if (i<(cols.size()-1))sb.append(" & ");
				else sb.append("\\\\ \\hline\n");				
			}
		}
		
		sb.append("\\end{tabular}\n");
		sb.append("\\caption{"+tableCaption+"}\n");
		sb.append("\\label{smalldatatable}\n");
		sb.append("\\end{center}\n");
		sb.append("\\end{table}\n");
		return sb.toString();
	}
	
	public ArrayList<String> getColumn() {
		ArrayList<String> data = new ArrayList<String>();
		data.add(m.getExplorRatio().toString());
		data.add(m.getExplorGap_1().toString());
		data.add(m.getExplorGap_1_std().toString());
		data.add(m.getExplorProgressiveness().toString());
		data.add(m.getExplorProgressiveness_std().toString());
		data.add(m.getExploreSelectionPressure().toString());
		//
		data.add(m.getExploitRatio().toString());
		data.add(m.getExploitProgressiveness().toString());
		data.add(m.getExploitProgressiveness_std().toString());
		data.add(m.getExploitSelectionPressure().toString());
		data.add(m.getCountAllNodes().toString());
		data.add(m.getNonRevisitedRatio().toString());		
		return data;
		
	}
	public ArrayList<String> getDoubleColumn() {
		ArrayList<String> data = new ArrayList<String>();
		data.add(m.getBestFitness().toStringMean());
		data.add(m.getBestFitness().toStringStDev());
		data.add(m.getExplorRatio().toStringMean());
		data.add(m.getExplorRatio().toStringStDev());
		data.add(m.getExplorGap_1().toStringMean());
		data.add(m.getExplorGap_1().toStringStDev());
		//data.add(m.getExplorDynamic_1_std().toString());
		data.add(m.getExplorProgressiveness().toStringMean());
		data.add(m.getExplorProgressiveness().toStringStDev());
		data.add(m.getExploreSelectionPressure().toStringMean());
		data.add(m.getExploreSelectionPressure().toStringStDev());
		//data.add(m.getExplorDynamic_2_std().toString());
		data.add(m.getExploitRatio().toStringMean());
		data.add(m.getExploitRatio().toStringStDev());
		data.add(m.getExploitProgressiveness().toStringMean());
		data.add(m.getExploitProgressiveness().toStringStDev());
		data.add(m.getExploitSelectionPressure().toStringMean());
		data.add(m.getExploitSelectionPressure().toStringStDev());
		data.add(m.getNonRevisitedRatio().toStringMean());
		data.add(m.getNonRevisitedRatio().toStringStDev());
		//data.add(""+(int)m.getCountAllNodes().getMean());
		return data;
		
	}
	public static ArrayList<String> getInfoColumn() {
		ArrayList<String> data = new ArrayList<String>();
		data.add("{\\footnotesize$explorRatio$"+"}");
		data.add("{\\footnotesize$explorGap$"+"}");
		data.add("{\\footnotesize$\\hspace{15mm}stdev$"+"}");
		data.add("{\\footnotesize$explorProgressiveness$"+"}");
		data.add("{\\footnotesize$\\hspace{15mm}stdev$"+"}");
		data.add("{\\footnotesize$exploreSelectionPressure$"+"}");
		data.add("{\\footnotesize$exploitRatio$"+"}");
		data.add("{\\footnotesize$exploitProgressiveness$"+"}");
		data.add("{\\footnotesize$\\hspace{15mm}stdev$"+"}");
		data.add("{\\footnotesize$exploitSelectionPressure$"+"}");
		data.add("{\\footnotesize$countAllNodes$"+"}");
		data.add("{\\footnotesize$nonRevisitedRatio$"+"}");
		return data;		
	}
	public static ArrayList<String> getDoubleInfoColumn() {
		ArrayList<String> data = new ArrayList<String>();
		data.add("{\\footnotesize$Fitness$"+"}");
		data.add("");
		data.add("{\\footnotesize$explorRatio$"+"}");
		data.add("");
		data.add("{\\footnotesize$explorGap$"+"}");
		data.add("");
		//data.add("{\\footnotesize$\\hspace{15mm}stdev$"+"}");
		data.add("{\\footnotesize$explorProgressiveness$"+"}");
		data.add("");
		data.add("{\\footnotesize$explorSelectionPressure$"+"}");
		data.add("");
		//data.add("{\\footnotesize$\\hspace{15mm}stdev$"+"}");
		data.add("{\\footnotesize$exploitRatio$"+"}");
		data.add("");
		data.add("{\\footnotesize$exploitProgressiveness$"+"}");
		data.add("");
		data.add("{\\footnotesize$exploitSelectionPressure$"+"}");
		data.add("");
		data.add("{\\footnotesize$nonRevisitedRatio$"+"}");
		data.add("");
		//data.add("{\\footnotesize$countAllNodes$"+"}");
		return data;		
	}

	public void printToScreen() {
		System.out.println("Not implemented yet!");
	}

	

}
