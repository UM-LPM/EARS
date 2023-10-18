package org.um.feri.ears.tuning;

import org.um.feri.ears.util.random.RNG;

public class ControlParameter {

	public String name;
	public String type;
	public double suggested_value;
	public double lower_bound;
	public double upper_bound;
	public double epsilon_neighbourhood;
	public double factor;
	public ControlParameter dependency; // control parameter on which the upper bound is dependent on
	
	public ControlParameter(String name, String type, double suggested_value, double lower_bound, double upper_bound, double epsilon_neighbourhood) {
		this.name = name;
		this.type = type;
		this.suggested_value = suggested_value;
		this.lower_bound = lower_bound;
		this.upper_bound = upper_bound;
		this.epsilon_neighbourhood = epsilon_neighbourhood;
		this.factor = (upper_bound-lower_bound)/(3.9+RNG.nextDouble()); // for initialisation to the interval [1,5]
	}

	
	
	public ControlParameter(String name, String type, double lower_bound, double upper_bound) {
		this.name = name;
		this.type = type;
		this.suggested_value = 0;
		this.lower_bound = lower_bound;
		this.upper_bound = upper_bound;
		this.epsilon_neighbourhood = 0;
		this.factor = (upper_bound-lower_bound)/(3.9+ RNG.nextDouble()); // for initialisation to the interval [1,5]
	}
	
	public ControlParameter(String name, String type, double lower_bound, ControlParameter dependency) {
		this.name = name;
		this.type = type;
		this.suggested_value = 0;
		this.lower_bound = lower_bound;
		this.dependency = dependency;
		this.epsilon_neighbourhood = 0;
		this.factor = (upper_bound-lower_bound)/(3.9+RNG.nextDouble());
	}


	public ControlParameter getDependency() {
		return this.dependency;
	}

	public double correctValue(double value){
		if(type.equalsIgnoreCase("int"))
			value = Math.round(value);
		value = Math.min(upper_bound, Math.max(value, lower_bound));
		return value;
	}
	public double randomNeighbour(double aValue){
		double neighbour = aValue;
		double start = aValue-epsilon_neighbourhood;
		double end = aValue+epsilon_neighbourhood;
		if (type.compareTo("int")==0){
			if (start<lower_bound){
				start = lower_bound;
				neighbour = (int)(start) + RNG.nextInt((int)(end - start));
			}else if (end>upper_bound){
				end = upper_bound;
				neighbour = (int)(start) + RNG.nextInt((int)(end - start));
			}else{
				neighbour = (int)(start) + RNG.nextInt((int)(end - start));
			}
		}else{
			if (start<lower_bound){
				start = lower_bound;
				neighbour = (start) + (end - start)*RNG.nextDouble();
			}else if (end>upper_bound){
				end = upper_bound;
				neighbour = (start) + (end - start)*RNG.nextDouble();
			}else{
				neighbour = (start) + (end - start)*RNG.nextDouble();
			}
		}
		return neighbour;		
	}
	
	public double randomValue(){
		double value = 0;
		double start = lower_bound;
		double end = upper_bound;
		if (type.toLowerCase().equals("int")){
			value = RNG.nextInt((int)start, (int)end);
		}else{
			value = RNG.nextDouble(start, end);
		}
		return value;		
	}
	
	public double randomValue(double start, double end){
		double value = 0;
		if (start == end) return start;
		if (type.toLowerCase().equals("int")){
			value = RNG.nextInt((int)start, (int)end);
			}else{
				value = RNG.nextDouble(start, end);
		}
		return value;		
	}
}
