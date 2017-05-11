package org.um.feri.analyse.EE.util;

import java.util.StringTokenizer;

public class LineParserECJ {
	public static final int BOF=0;
	public static final int EOF=-1;
	public static final int P=1;
	public static final int ID=3;
	public static final int IN=4;
	public static final int CROSS=5;
	public static final int MUTAT=6;
	public static final int REPAIR=7;
	public static final int FITNESS=8;
	
	private String value;
	private StringTokenizer tok;
	private StringTokenizer inTok;
	private StringTokenizer pTok;
	int state; 

	public int getState() {
		return state;
	}
	public String getValue() {
		return value;
	}
	public int getIntValue() {
		return Integer.parseInt(value);
	}
	public double getDoubleVaue()
	{
		return Double.parseDouble(value);
	}

	public LineParserECJ(String l, String delim) {
		state=BOF;
		tok = new StringTokenizer(l, delim);
		nextState();
	}
	//Example: id;fitness;in[,,,];parents[,,,]
	public void nextState() {
		switch (state) {
		case BOF:
			value = tok.nextToken();
			state = ID;
			break;
		case ID:
			state = FITNESS;
			value = tok.nextToken();
			break;
		 case FITNESS:
			 inTok = new StringTokenizer(tok.nextToken(), ",");
			 value = inTok.nextToken();
			 value = value.replace('[', ' ');
			 state = IN;
			 break;
		 case IN:
			 if(inTok.hasMoreTokens())
			 {
				 state = IN;
				 value = inTok.nextToken();
				 value = value.replace(']', ' ');
			 }
			 else
			 {
				 state = P;
				 if(tok.hasMoreTokens())
				 {
					 pTok = new StringTokenizer(tok.nextToken(), ",");
					 value = pTok.nextToken();
					 value = value.replace('[', ' ');
				 }
				 else
				 {
					 state = EOF;
					 value = "";
				 }
			 } 
			 break;
		 case P:
			 if(pTok.hasMoreTokens())
			 {
				 state = P;
				 value = pTok.nextToken();
				 value = value.replace(']', ' ');
			 }
			 else
			 {
				 state = EOF;
				 value = "";
			 }
			 break;
			 
		}
		
	}
}
