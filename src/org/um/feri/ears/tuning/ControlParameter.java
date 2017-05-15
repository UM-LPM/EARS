package org.um.feri.ears.tuning;

import org.um.feri.ears.util.Util;

public class ControlParameter {

	public String name;
	public String type;
	public double suggested_value;
	public double lower_bound;
	public double upper_bound;
	public double epsilon_neighbourhood;
	public double factor;
	public double precision;
	
	public ControlParameter(String name, String type, double suggested_value, double lower_bound, double upper_bound, double epsilon_neighbourhood, double precision) {
		this.name = name;
		this.type = type;
		this.suggested_value = suggested_value;
		this.lower_bound = lower_bound;
		this.upper_bound = upper_bound;
		this.epsilon_neighbourhood = epsilon_neighbourhood;
		this.precision = precision;
		this.factor = (upper_bound-lower_bound)/(3.9+Util.rnd.nextDouble()); // for initialisation to the interval [1,5]
	}
	
	public ControlParameter(String name, String type, double lower_bound, double upper_bound, double precision) {
		this.name = name;
		this.type = type;
		this.suggested_value = 0;
		this.lower_bound = lower_bound;
		this.upper_bound = upper_bound;
		this.epsilon_neighbourhood = 0;
		this.precision = precision;
		this.factor = (upper_bound-lower_bound)/(3.9+Util.rnd.nextDouble()); // for initialisation to the interval [1,5]
	}
	
	public double correctValue(double aValue){
		double value = Math.round(aValue/precision) * precision;	
		if (value<lower_bound) value = lower_bound;
		if (value>upper_bound) value = upper_bound;
		return value;
	}
	public double randomNeighbour(double aValue){
		double neighbour = aValue;
		double start = aValue-epsilon_neighbourhood;
		double end = aValue+epsilon_neighbourhood;
		if (type.compareTo("int")==0){
			if (start<lower_bound){
				start = lower_bound;
				neighbour = (int)(start) + Util.rnd.nextInt((int)(end - start));
			}else if (end>upper_bound){
				end = upper_bound;
				neighbour = (int)(start) + Util.rnd.nextInt((int)(end - start));
			}else{
				neighbour = (int)(start) + Util.rnd.nextInt((int)(end - start));
			}
		}else{
			if (start<lower_bound){
				start = lower_bound;
				neighbour = (start) + (end - start)*Util.rnd.nextDouble();
			}else if (end>upper_bound){
				end = upper_bound;
				neighbour = (start) + (end - start)*Util.rnd.nextDouble();
			}else{
				neighbour = (start) + (end - start)*Util.rnd.nextDouble();
			}
		}
		return neighbour;		
	}
	
	public double randomValue(){
		double value = 0;
		double start = lower_bound;
		double end = upper_bound;
		if (type.compareTo("int")==0){
			value = (int)(start) + Util.rnd.nextInt((int)(end - start + 1));
			value = Math.round(value/precision) * (int)precision;
			if (value < lower_bound) value = lower_bound;
			if (value > upper_bound) value = upper_bound;
		}else{
			value = (start) + (end - start)*Util.rnd.nextDouble();
			value = Math.round(value/precision) * precision;
			if (value < lower_bound) value = lower_bound;
			if (value > upper_bound) value = upper_bound;
		}
		return value;		
	}
	public double randomValue(double a, double b){
		double value = 0;
		double start = a;
		double end = b;
		if (start == end) return start;
		if (type.compareTo("int")==0){
			value = (int)(start) + Util.rnd.nextInt((int)(end - start + 1));
			value = Math.round(value/precision) * (int)precision;
		}else{
			value = (start) + (end - start)*Util.rnd.nextDouble();
			value = Math.round(value/precision) * precision;
		}
		return value;		
	}
}
