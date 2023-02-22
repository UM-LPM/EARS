package org.um.feri.ears.operators;

public interface Operator<Result, Source, Problem>  {
	  /**
	   * @param source The data to process
	   */
	  Result execute(Source source, Problem problem);
}