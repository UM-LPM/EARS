package org.um.feri.analyse.EE.metrics;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class PrintAMetrics {
	ATMetrics m;
	NumberFormat formatter, stdFormat;

	public PrintAMetrics(ATMetrics m) {
		super();
		this.m = m;
		formatter = new DecimalFormat("#0.000");
		stdFormat = new DecimalFormat("#.00");
	}
	
	public String toLatex(ArrayList<String> heads, ArrayList<ArrayList<String>> cols, String tableCaption) {
		StringBuffer sb = new StringBuffer();
		sb.append("\\begin{table}[h!]\n");
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
		data.add(formatter.format(m.explorRatio()));
		data.add(formatter.format(m.explorGap().mean));
		data.add("{\\footnotesize $\\pm$"+stdFormat.format(m.explorGap().stdev)+"}");
		data.add(formatter.format(m.explorProgressiveness().mean));
		data.add("{\\footnotesize $\\pm$"+stdFormat.format(m.explorProgressiveness().stdev)+"}");
		data.add(formatter.format(m.exploitRatio()));
		data.add(formatter.format(m.exploitSelectionPressure()));
		data.add(""+m.getCount());
		data.add(formatter.format(m.nonRevisitedRatio()));
		return data;
		
	}
	public static ArrayList<String> getInfoColumn() {
		ArrayList<String> data = new ArrayList<String>();
		data.add("{\\footnotesize$explorRatio$"+"}");
		data.add("{\\footnotesize$explorGap$"+"}");
		data.add("{\\footnotesize$\\hspace{15mm}stdev$"+"}");
		data.add("{\\footnotesize$explorProgressiveness$"+"}");
		data.add("{\\footnotesize$\\hspace{15mm}stdev$"+"}");
		data.add("{\\footnotesize$exploitRatio$"+"}");
		data.add("{\\footnotesize$exploitSelectionPressure$"+"}");
		data.add("{\\footnotesize$countAllNodes$"+"}");
		data.add("{\\footnotesize$nonRevisitedRatio$"+"}");
		return data;		
	}

	public void printToScreen() {
		System.out.println("explorRatio:"+m.explorRatio());
		System.out.println("explorDynamic1:"+m.explorGap());
		System.out.println("explorDynamic2:"+m.explorProgressiveness());
		System.out.println("exploitRatio:"+m.exploitRatio());
		System.out.println("exploitSelectionPressure:"+m.exploitSelectionPressure());
		System.out.println("countAll:"+m.getCount());
		System.out.println("nonRevisitedRatio:"+m.getCount());
		
	}
}
