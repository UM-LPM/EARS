package org.um.feri.ears.operators;

public interface Operator<Result, Source, Problem>  {

	  Result execute(Source source, Problem problem);
}