package org.um.feri.analyse.EE.util;

public class UtilTestMain {

	/**
	 * Just example test!
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		LineParserECJ lp = new LineParserECJ("1;1;(0,0,0);(1)", ";");
		while (lp.getState()!=LineParserECJ.EOF) {
			System.out.println(lp.getState() +" Value:"+lp.getValue());
			lp.nextState();
			
		}

	}

}
