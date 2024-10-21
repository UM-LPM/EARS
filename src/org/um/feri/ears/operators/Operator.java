package org.um.feri.ears.operators;

import java.io.Serializable;

public interface Operator<Result, Source, Problem> extends Serializable {

	  Result execute(Source source, Problem problem);
}