package org.um.feri.ears.benchmark;

import java.util.ArrayList;
import java.util.List;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.moo.IntegerMOProblem;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.real_world.CITOProblem;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.rating.Game;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.util.Util;

public class CITOBenchmark extends MORatingBenchmark<Integer, IntegerMOTask, IntegerMOProblem>{
    public static final String name="Rating Ensemble";
    protected int evaluationsOnDimension;
    protected int dimension=3;
    private double draw_limit=0.000000001;
    private boolean random;
    
	@Override
	public boolean resultEqual(ParetoSolution<Integer> a, ParetoSolution<Integer> b, QualityIndicator<Integer> qi) {
		if ((a==null) &&(b==null)) return true;
        if (a==null) return false;
        if (b==null) return false;
        if(qi.getIndicatorType() == IndicatorType.Unary)
        	return a.isEqual(b,draw_limit); 
        else if(qi.getIndicatorType() == IndicatorType.Binary)
        {
			if(qi.compare(a, b, draw_limit) == 0)
			{
				return true;
			}
        }
        return false;
	}
    
    public CITOBenchmark(List<IndicatorName> indicators, double draw_limit, boolean random) {
        super(indicators);
        this.random = random;
        this.draw_limit = draw_limit;
        evaluationsOnDimension=300000;
        initFullProblemList();
        addParameter(EnumBenchmarkInfoParameters.EVAL,String.valueOf(evaluationsOnDimension));
        addParameter(EnumBenchmarkInfoParameters.DRAW_PARAM,"abs(evaluation_diff) < "+draw_limit);

    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#registerTask(org.um.feri.ears.problems.Problem)
     */
    @Override
    protected void registerTask(EnumStopCriteria sc, int eval, double epsilon, IntegerMOProblem p) {
        listOfProblems.add(new IntegerMOTask(sc, eval, epsilon, p));
    }
    
    @Override
    protected void setWinLoseFromResultList(ResultArena arena, IntegerMOTask t) {
    	
    	if(random)
    	{
    		MOAlgorithmEvalResult first;
			MOAlgorithmEvalResult second;
			QualityIndicator<Integer> qi;
			IndicatorName indicatorName;
    		for (int i=0; i<results.size(); i++) {
    			first = results.get(i);
    			for (int j=i+1; j<results.size(); j++) {
    				second = results.get(j);
    				indicatorName = indicators.get(Util.nextInt(indicators.size()));
    				qi = IndicatorFactory.createIndicator(indicatorName, t.getProblem());
    				
    				try {
    					if(qi.getIndicatorType() == IndicatorType.Unary)
    					{
    						first.getBest().evaluate(qi);
    						second.getBest().evaluate(qi);
    					}
					} catch (Exception e) {
						e.printStackTrace();
					}
    				if (resultEqual(first.getBest(), second.getBest(), qi)) { 
						arena.addGameResult(Game.DRAW, first.getAl().getAlgorithmInfo().getVersionAcronym(), second.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemShortName(), indicatorName.toString());
					} 
    				else 
    				{
    					if (t.isFirstBetter(first.getBest(),second.getBest(), qi))
    					{
    						arena.addGameResult(Game.WIN, first.getAl().getAlgorithmInfo().getVersionAcronym(), second.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemShortName(), indicatorName.toString());
    					}
    					else
    					{
    						arena.addGameResult(Game.WIN, second.getAl().getAlgorithmInfo().getVersionAcronym(), first.getAl().getAlgorithmInfo().getVersionAcronym(), t.getProblemShortName(), indicatorName.toString());
    					}
    				}
        		}
    		}	
    	}
    	else
    		super.setWinLoseFromResultList(arena, t);
    	
    }

	/* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#initFullProblemList()
     */
    @Override
    protected void initFullProblemList() {
    	
    	ArrayList<IntegerMOProblem> problems = new ArrayList<IntegerMOProblem>();

    	problems.add(new CITOProblem("OA_AJHsqldb"));
    	problems.add(new CITOProblem("OO_BCEL"));
    	problems.add(new CITOProblem("OO_MyBatis"));
    	
    	
    	for (IntegerMOProblem moProblem : problems) {
    		registerTask(stopCriteria, evaluationsOnDimension, 0.001, moProblem);
		}
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
        return "RatingEnsemble";
    }
    /* (non-Javadoc)
     * @see org.um.feri.ears.benchmark.RatingBenchmark#getInfo()
     */
    @Override
    public String getInfo() {
        return "";
    }
}
