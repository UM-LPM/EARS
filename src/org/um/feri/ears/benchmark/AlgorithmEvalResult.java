package org.um.feri.ears.benchmark;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.problems.DoubleSolution;

public class AlgorithmEvalResult {
    DoubleSolution best;
    Algorithm al;
    int numEvaluations; //for global optimum stopping criteria
    public AlgorithmEvalResult(DoubleSolution best, Algorithm al, int evaluations) {
        super();
        this.best = best;
        this.al = al;
        numEvaluations = evaluations;
    }
    public DoubleSolution getBest() {
        return best;
    }
    public void setBest(DoubleSolution best) {
        this.best = best;
    }
    public Algorithm getAl() {
        return al;
    }
    public void setAl(Algorithm al) {
        this.al = al;
    }
}
