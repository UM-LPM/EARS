package org.um.feri.ears.memory;

import java.util.HashMap;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;


public class MemoryBankDoubleSolution {
	double precisionPower;
	int precisionInDecimalPlaces;
	int duplicationHitSum;
	int duplicationBeforeGlobal;
	private HashMap<String, DoubleSolution> hashMapMemory;
	private HashMap<String, Integer> hashMapMemoryHits;
	DuplicationRemovalStrategyInterface updateStrategy;
	
	public MemoryBankDoubleSolution(int precisionInDecimalPlaces, DuplicationRemovalStrategyInterface updateStrategy) {
		this.precisionInDecimalPlaces = precisionInDecimalPlaces;
		this.updateStrategy = updateStrategy;
		precisionPower = (long) Math.pow(10, precisionInDecimalPlaces);
		hashMapMemory = new HashMap<>();
		hashMapMemoryHits = new HashMap<>();
		reset();
	}
	
	private double round(double v) {
		return Math.floor(precisionPower * v + 0.5) / precisionPower;
	}
	
	public void round(double x[]) {
		for (int i=0; i<x.length; i++) {
			x[i] = round(x[i]);
		}
	}
	
	public String encodeKey(double x[]) {
		StringBuffer sb = new StringBuffer();
		for(double d:x) {
			//sb.append(""+Double.toString(d)).append(".");
			sb.append(""+Double.doubleToLongBits(d)).append(".");
		}
		return sb.toString();
		
	}
/*	public String encodeKey(double x[]) {
		StringBuffer sb = new StringBuffer();
		for(double d:x) {
			sb.append(""+Double.doubleToLongBits(d)).append(".");
		}
		return sb.toString();
		
	}
*/
	
	public DoubleSolution eval(TaskWithMemory task, double x[]) throws StopCriteriaException {
		round(x);
		String key = encodeKey(x);
		if (hashMapMemory.containsKey(key)) {
			duplicationHitSum++;
			if (!task.isGlobal()) {
				duplicationBeforeGlobal++;
			}
			if (updateStrategy.criteria4Change(hashMapMemoryHits.get(key))) {
				updateStrategy.changeSolution(x);
				hashMapMemoryHits.put(key,1); //new one¸
				DoubleSolution ds = task.evalOrg(x);
				hashMapMemory.put(key, ds);
			} else {
			  hashMapMemoryHits.put(key, hashMapMemoryHits.get(key)+1);
			}
			return hashMapMemory.get(key);
		}
		hashMapMemoryHits.put(key,1); //new one¸
		DoubleSolution ds = task.evalOrg(x);
		hashMapMemory.put(key, ds);
		return ds;
	}
	
	public void reset() {
		duplicationHitSum = 0;
		hashMapMemory.clear();
		duplicationBeforeGlobal =0;
		hashMapMemoryHits.clear();
	}
	
	public String toString( ) {
		StringBuffer sb = new StringBuffer();
		sb.append("Precision decimal:").append(precisionInDecimalPlaces).append(" ");
		sb.append("Double hits:").append(duplicationHitSum).append(" ");
		sb.append("Double before global:").append(duplicationBeforeGlobal).append(" ");		
		sb.append("Max single hit:").append(updateStrategy).append(" ");
		return sb.toString();
		
		
	}

}
