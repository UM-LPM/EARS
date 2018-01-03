package org.um.feri.ears.benchmark;


import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class MOAlgorithmEvalResult {
	private ParetoSolution best;
    private MOAlgorithm al;
    private MOTask task;
    private boolean evaluated = false;
    
    
    public MOAlgorithmEvalResult(ParetoSolution best, MOAlgorithm al, MOTask task) {
        super();
        this.best = best;
        this.al = al;
        this.task = task;
    }
    
    public MOAlgorithmEvalResult(MOAlgorithmEvalResult res) {
		this.evaluated = res.evaluated;
		this.best = new ParetoSolution(res.best);
		this.al = res.al;
	}
    
	public ParetoSolution getBest() {
        return best;
    }
    public void setBest(ParetoSolution best) {
        this.best = best;
    }
    public MOAlgorithm getAl() {
        return al;
    }
    public MOTask getTask() {
        return task;
    }
    public void setAl(MOAlgorithm al) {
        this.al = al;
    }
	public boolean isEvaluated() {
		return evaluated;
	}
	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}
    
    
}
