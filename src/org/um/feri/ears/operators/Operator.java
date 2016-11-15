package org.um.feri.ears.operators;

import java.util.EnumMap;

import org.um.feri.ears.algorithms.EnumAlgorithmParameters;

public interface Operator<Result, Source, TaskBase>  {
	  /**
	   * @param source The data to process
	   */
	  public Result execute(Source source, TaskBase tb);
	  
	  public abstract EnumMap<EnumAlgorithmParameters,String> getOperatorParameters();

}