package org.um.feri.ears.operators;

import java.util.EnumMap;

import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.MOTask;


public interface Operator<Result, Source, Task>  {
	  /**
	   * @param source The data to process
	   */
	  public Result execute(Source source, Task tb);
	  
	  public abstract EnumMap<EnumAlgorithmParameters,String> getOperatorParameters();
}