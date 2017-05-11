package org.um.feri.analyse.EE.util;

import java.util.Comparator;

public class Frequency implements Comparator<Frequency>{
	Integer id;
	int count;
	
	public Frequency(int id) {
		super();
		this.id = id;
		count = 1; //starts with 1
	}
	
	public int getCount() {
		return count;
	}

	public Integer getId() {
		return id;
	}
	
	public void inc() {
		count++;
	}
	
	public String toString() {
		return id+" ("+count+")";
		
	}

	public int describesElements() {
		return id.intValue()*count;
	}

	 public static class CompareCount implements Comparator {
		    public int compare(Object o1, Object o2) {
		      if (!(o1 instanceof Frequency) || !(o2 instanceof Frequency))
		        throw new ClassCastException();

		      Frequency e1 = (Frequency) o1;
		      Frequency e2 = (Frequency) o2;

		      return (int) (e1.getCount() - e2.getCount());
		    }
		  }
	 
	 public static class CompareID implements Comparator<Frequency> {
		    public int compare(Frequency o1, Frequency o2) {
		      return (int) (o1.getId().intValue() -o2.getId().intValue());
		    }
		  }

	@Override
	public int compare(Frequency o1, Frequency o2) {
		return o1.id.intValue() - o2.id.intValue();
	}
	

}
