package org.um.feri.ears.benchmark;

import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.cec2014.*;



public class RatingCEC2014 extends RatingBenchmark{
    public static final String name="Benchmark CEC 2014";
    protected int evaluationsOnDimension;
    protected int dimension;
    private double draw_limit=0.0000001;
    protected long timeLimit;
    private int maxIterations;
    
    public boolean resultEqual(DoubleSolution a, DoubleSolution b) {
        if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if (Math.abs(a.getEval()-b.getEval())<draw_limit) return true;
        return false;
    }
    public RatingCEC2014(){
    	this(0.0000001);
    }
    public RatingCEC2014(double draw_limit) {
        super();
        this.draw_limit = draw_limit;
        evaluationsOnDimension=3000;
        dimension=10;
        timeLimit = 0;
        maxIterations = 0;
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.DIMENSION,"10");
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(evaluationsOnDimension));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#registerTask(org.um.feri.ears.problems.Problem)
     */
    @Override
    protected void registerTask(Problem p, EnumStopCriteria sc, int eval, long time, int maxIterations, double epsilon) {
        listOfProblems.add(new Task(sc, eval, time, maxIterations, epsilon, p));
    }
    
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#initFullProblemList()
     */
    @Override
    protected void initFullProblemList() {
    	
    	registerTask(new F1(dimension),stopCriteria,evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F2(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F3(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F4(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F5(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F6(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F7(dimension),stopCriteria,  evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F8(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F9(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F10(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F11(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F12(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F13(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F14(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F15(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F16(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F17(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F18(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F19(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F20(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F21(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F22(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F23(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F24(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F25(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F26(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F27(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F28(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F29(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);
    	registerTask(new F30(dimension),stopCriteria, evaluationsOnDimension, timeLimit, maxIterations, 0.001);

    }
        
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getAcronym()
     */
    @Override
    public String getAcronym() {
        return "CEC2014";
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getInfo()
     */
    @Override
    public String getInfo() {
        return "";
    }
    
}