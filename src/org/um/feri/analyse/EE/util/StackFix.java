package org.um.feri.analyse.EE.util;

import java.util.Arrays;
/**
 * Vsak enkrat pride na vrsto
 * 
 * @author matej
 *
 */
public class StackFix {
	int all[];
	int len;
	public StackFix(int len) {
		all = new int[len];
		for (int i=0; i<len; i++) all[i]=i;
		this.len = len;
	}
	public int selectIndex(boolean pos[]) {
		int r=-1;
		for (int i=0; i<len; i++) {
			if ( pos[all[i]]) {
				r = all[i];
				//reindex;
				for (int j=i; j<len-1;j++) {
					all[j] =all[j+1];
				}
				all[len-1] = r;
				break;
			}
		}
		return r;
	}
	public String toString() {
		return Arrays.toString(all);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StackFix sf= new StackFix(3);
		System.out.println("Results:"+sf);
		boolean pos[]={false, false, true};
		System.out.println(sf.selectIndex(pos));
		System.out.println("Results:"+sf);
		boolean pos1[]={true, false, true};
		System.out.println(sf.selectIndex(pos1));
		System.out.println("Results:"+sf);
		boolean pos2[]={true, true, true};
		System.out.println(sf.selectIndex(pos2));
		System.out.println("Results:"+sf);
		System.out.println(sf.selectIndex(pos2));
		System.out.println("Results:"+sf);
		System.out.println(sf.selectIndex(pos2));
		System.out.println("Results:"+sf);

	}

}
