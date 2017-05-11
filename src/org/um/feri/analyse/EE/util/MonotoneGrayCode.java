package org.um.feri.analyse.EE.util;

public class MonotoneGrayCode {
	public static String[] all3 = {
		  "00000", "00001", "00011", "00010", "00110", "00100", "01100", "01000", 
		  "11000", "10000", "10001", "10101", "10100", "10110", "10010", "11010",
		  "01010", "01011", "01001", "01101", "00101", "00111", "01111", "01110",
		  "11110", "11100", "11101", "11001", "11011", "10011", "10111", "11111"};

 public static String[] all5 = {
		  "00000", "00001", "00011", "00010", "00110", "00100", "01100", "01000", 
		  "11000", "10000", "10001", "10101", "10100", "10110", "10010", "11010",
		  "01010", "01011", "01001", "01101", "00101", "00111", "01111", "01110",
		  "11110", "11100", "11101", "11001", "11011", "10011", "10111", "11111"};
 
 public static int pos(String s,String[] all5){
	 for  (int i=0;i<all5.length;i++){
		 if (all5[i].equals(s)) return i;
	 }
	 System.out.println("Cant find:"+s);
	 return -1;
 }
 
 public static MyPoint getPos(String s,String[] all5){
	 int x = pos(s.substring(0, s.length()/2),all5);
	 int y = pos(s.substring(s.length()/2),all5);
	 return new MyPoint(x, y);
	 
}
 //set xtics   ("00000" 1.00000 -1, "00001" 2.00000 -1, "00010" 3.00000 -1, "1921-1930" 4.00000 -1, "1931-1940" 5.00000 -1, "1941-1950" 6.00000 -1, "1951-1960" 7.00000 -1, "1961-1970" 8.00000 -1)
public static String getGNUPlotX(String[] all5 ) {
	StringBuffer sb = new StringBuffer();
	sb.append("set xtics   (");
	for (int i=0;i<all5.length;i++) {
		sb.append("\"").append(all5[i]).append("\" ").append(i+1).append(" ").append("-1");
		if (i<(all5.length-1)) sb.append(", ");
	}
	sb.append(")");
	return sb.toString();
}

public static void main(String a[]) {
	System.out.println(getGNUPlotX(all5));
}
 
}
