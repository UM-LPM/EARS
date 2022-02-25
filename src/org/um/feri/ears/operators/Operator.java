package org.um.feri.ears.operators;

public interface Operator<Result, Source, TaskBase>  {
	  /**
	   * @param source The data to process
	   */
	  Result execute(Source source, TaskBase tb);
}