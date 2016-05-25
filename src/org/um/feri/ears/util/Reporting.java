package org.um.feri.ears.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.rating.Player;

public class Reporting {
	
	public static void savePlayersToFile(ArrayList<Player> players, String file_name, int decimalPlaces)
	{
		DecimalFormat df = getFormat(decimalPlaces);
		df.setRoundingMode(RoundingMode.HALF_UP);
		try {
			FileOutputStream fos = new FileOutputStream(file_name);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			int rank = 1;
			double rating, RD, voaltility;
			String interval;
			for (Player p : players) {
				rating = p.getRatingData().getRating();
				RD = p.getRatingData().getRD();
				voaltility = p.getRatingData().getRatingVolatility();
				interval = "["+ df.format(rating - 2 * RD) + "," + df.format(rating + 2 * RD) + "]";
				
				bw.write(rank+". "+p.getPlayerId()+" "+df.format(rating)+" "+ RD + " " +interval);
				bw.newLine();
				rank++;
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveLeaderboard(ArrayList<Player> players, String file_name)
	{
		try {
			FileOutputStream fos = new FileOutputStream(file_name);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			for (Player p : players) {
				bw.write(p.toString());
				bw.newLine();
			}
			bw.close();
			osw.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static DecimalFormat getFormat(int decimalPlaces)
	{
		if(decimalPlaces < 1)
			return new DecimalFormat("#");
		else
		{
			String format = "#.";
			format+=StringUtils.repeat("#",decimalPlaces);

			return new DecimalFormat(format);
		}
	}
	
	public static void createLatexTable(ArrayList<Player> players, String file_name)
	{
		DecimalFormat df = getFormat(0);
		df.setRoundingMode(RoundingMode.HALF_UP);
		
		StringBuilder sb = new StringBuilder();
		sb.append("\\begin{table}");
		sb.append(System.getProperty("line.separator"));
		sb.append("\t\\begin{tabular}{lll}");
		sb.append(System.getProperty("line.separator"));
		
		sb.append("\t");
		for(int i = 0; i < players.size(); i++)
		{
			sb.append(players.get(i).getPlayerId());
			if(i+1 < players.size())
				sb.append(" & ");
			else
				sb.append("\\\\");
		}
		sb.append(System.getProperty("line.separator"));
		
		double rating, RD;
		sb.append("\t");
		for(int i = 0; i < players.size(); i++)
		{
			rating = players.get(i).getRatingData().getRating();
			RD = players.get(i).getRatingData().getRD();
			
			sb.append(df.format(rating));
			if(i+1 < players.size())
				sb.append(" & ");
			else
				sb.append("\\\\");
		}
		sb.append(System.getProperty("line.separator"));


		sb.append("\t\\end{tabular}");
		sb.append(System.getProperty("line.separator"));
		sb.append("\\end{table}");
		sb.append(System.getProperty("line.separator"));

		try {
			FileOutputStream fos = new FileOutputStream(file_name);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			bw.write(sb.toString());
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void createLatexTable(HashMap<IndicatorName, ArrayList<Player>> indikatorResults, String file_name) {
		
		DecimalFormat df = getFormat(0);
		df.setRoundingMode(RoundingMode.HALF_UP);
		double rating, RD;
		boolean isFirst = true;
		String RDline ="";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\\begin{table}[H]");
		sb.append(System.getProperty("line.separator"));

		
		for (Entry<IndicatorName, ArrayList<Player>> entry : indikatorResults.entrySet()) {
		    IndicatorName indicator = entry.getKey();
		    ArrayList<Player> row = entry.getValue();
		    
		    Collections.sort(row, new Comparator<Player>() {
		    	@Override
		    	public int compare(Player p1, Player p2) {
		    		return p1.getPlayerId().compareTo(p2.getPlayerId());
		    	}
		    } );

		    if(isFirst)
		    {
				sb.append("\t\\begin{tabular}{l"+StringUtils.repeat("l",row.size())+"}");
				sb.append(System.getProperty("line.separator"));
				sb.append("\t");
				sb.append("~ & "); //first cell is empty
				for(int i = 0; i < row.size(); i++)
				{
					sb.append(row.get(i).getPlayerId());
					if(i+1 < row.size())
						sb.append(" & ");
					else
						sb.append(" \\\\");
				}
				sb.append(System.getProperty("line.separator"));
				isFirst = false;
		    }
		    
		    RDline="";
			sb.append("\t");
			sb.append(indicator+ " & ");
			
			int[] ranks = getRanks(row);
			for(int i = 0; i < row.size(); i++)
			{
				rating = row.get(i).getRatingData().getRating();
				RD = row.get(i).getRatingData().getRD();
				RDline+="["+df.format(rating-2*RD)+","+df.format(rating+2*RD)+"]";
				
				sb.append(df.format(rating)+" ("+ranks[i]+")"); //df.format(RD)
				if(i+1 < row.size())
				{
					sb.append(" & ");
					RDline+=" & ";
				}
				else
				{
					sb.append(" \\\\");
					RDline+=" \\\\ ";
				}
			}
			sb.append(System.getProperty("line.separator"));
			
			sb.append("RD $\\pm$ 2RD (95\\%) & ");
			sb.append(RDline);
			sb.append(System.getProperty("line.separator"));
		    
		}

		sb.append("\t\\end{tabular}");
		sb.append(System.getProperty("line.separator"));
		sb.append("\\end{table}");
		sb.append(System.getProperty("line.separator"));

		try {
			FileOutputStream fos = new FileOutputStream(file_name);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			bw.write(sb.toString());
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int[] getRanks(ArrayList<Player> row) {
		
		int[] ranks = new int[row.size()];
		Double[] ratings = new Double[row.size()];
		for(int i = 0; i < row.size(); i++)
		{
			ratings[i] = row.get(i).getRatingData().getRating();
		}
		Arrays.sort(ratings,Collections.reverseOrder());
		
		for(int k = 0; k < ranks.length; k++)
		{
			for(int i = 0; i < row.size(); i++)
			{
				if(ratings[i] == row.get(k).getRatingData().getRating())
					ranks[k] = i+1;
			}
		}
		return ranks;
	}
	
}
