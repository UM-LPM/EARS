package org.um.feri.ears.operators;

import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Solution;

import java.io.Serializable;
import java.util.List;

public abstract class Selection <Source extends Solution, P extends Problem> implements SelectionOperator<Source, List<Source>, P>, Serializable {
}
