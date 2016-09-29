package org.um.feri.ears.experiments;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.algorithms.moo.ibea.D_IBEA;
import org.um.feri.ears.algorithms.moo.ibea.I_IBEA;
import org.um.feri.ears.algorithms.moo.moead.D_MOEAD;
import org.um.feri.ears.algorithms.moo.moead.I_MOEAD;
import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.algorithms.moo.nsga2.I_NSGAII;
import org.um.feri.ears.algorithms.moo.pesa2.D_PESAII;
import org.um.feri.ears.algorithms.moo.pesa2.I_PESAII;
import org.um.feri.ears.algorithms.moo.spea2.D_SPEA2;
import org.um.feri.ears.algorithms.moo.spea2.I_SPEA2;
import org.um.feri.ears.benchmark.MOAlgorithmEvalResult;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.IntegerMOTask;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.real_world.CITOProblem;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem1;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.rating.Player;

public class ASConeRun {

	public static void main(String[] args) {


		ArrayList<MOAlgorithm> players = new ArrayList<MOAlgorithm>();

		/*players.add(new D_MOEAD());
		players.add(new D_NSGAII());
		players.add(new D_SPEA2());
		players.add(new D_PESAII());
		players.add(new D_IBEA());*/
		
		players.add(new I_MOEAD());
		players.add(new I_NSGAII());
		players.add(new I_SPEA2());
		players.add(new I_PESAII());
		players.add(new I_IBEA());


		MOAlgorithm.setRunWithOptimalParameters(true);

		ArrayList<IndicatorName> indicators = new ArrayList<IndicatorName>();
		indicators.add(IndicatorName.NativeHV);
		indicators.add(IndicatorName.IGD);
		indicators.add(IndicatorName.IGDPlus);
		indicators.add(IndicatorName.Epsilon);
		indicators.add(IndicatorName.R2);
		indicators.add(IndicatorName.MaximumSpread);
		indicators.add(IndicatorName.GeneralizedSpread);
		indicators.add(IndicatorName.CovergeOfTwoSets);
		indicators.add(IndicatorName.GD);
		indicators.add(IndicatorName.MPFE);
		indicators.add(IndicatorName.Spacing);

		StringBuilder sb = new StringBuilder();
		sb.append("QI;");
		for (MOAlgorithm p : players)
		{
			sb.append(p.getAlgorithmInfo().getPublishedAcronym());
			sb.append(";");
		}
		sb.append("\n");
		
		//HashMap<IndicatorName, ArrayList<Player>> results = new HashMap<IndicatorName, ArrayList<Player>>();
		ArrayList<MOAlgorithmEvalResult> results = new ArrayList<>();
		
		CITOProblem problem = new CITOProblem("OO_MyBatis");
		//DoubleMOTask t = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001,  new UnconstrainedProblem1());
		IntegerMOTask t = new IntegerMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001,  problem);
		
		FitnessComparator fc;
		
		for(MOAlgorithm p: players)
		{
			System.out.println("Algorithm: "+ p.getAlgorithmInfo().getPublishedAcronym());

			try {
				// OA_AJHsqldb OO_BCEL OO_MyBatis
				//ParetoSolution best = p.execute(new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem1()));
				p.setDisplayData(true);
				ParetoSolution best = p.execute(t);
				results.add(new MOAlgorithmEvalResult(best,p));
				t.resetCounter();
            	best.printFeasibleFUN("D:\\Benchmark results\\AppliedSoftComputing\\"+p.getAlgorithmInfo().getPublishedAcronym()+"_CITO.dat");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for (IndicatorName indicatorName : indicators) {

			System.out.println("Indicator: "+indicatorName);
			sb.append(indicatorName+";");
			
			QualityIndicator qi = IndicatorFactory.createIndicator(indicatorName, problem.getNumberOfObjectives(), problem.getFileName());
			
			fc = new FitnessComparator(t, qi);
			
			Collections.sort(results, fc); //best first

			for(MOAlgorithm p: players)
			{
				for(int i=0;i<results.size();i++)
				{
					if(results.get(i).getAl().getAlgorithmInfo().getPublishedAcronym().equals(p.getAlgorithmInfo().getPublishedAcronym()))
					{
						sb.append((i+1)+";");
					}
				}
			}
			sb.append("\n");
		}
		
		try {
			FileOutputStream fos = new FileOutputStream("D:\\Benchmark results\\AppliedSoftComputing\\QIevalForSingleRunCITO_OO_MyBatis2.csv");
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(sb.toString());
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	static class FitnessComparator implements Comparator<MOAlgorithmEvalResult> {
        MOTask t;
        QualityIndicator qi;
        public FitnessComparator(MOTask t, QualityIndicator qi) {
            this.t = t;
            this.qi = qi;
        }
        @Override
        public int compare(MOAlgorithmEvalResult arg0, MOAlgorithmEvalResult arg1) {
            if (arg0.getBest()!=null) {
                if (arg1.getBest()!=null){
                   // if (resultEqual(arg0.getBest(), arg1.getBest())) return 0; Normal sor later!
                	if(qi.getIndicatorType() == IndicatorType.Unary)
                	{
                		try {
							arg0.getBest().evaluate(qi);
							arg1.getBest().evaluate(qi);
						} catch (Exception e) {
							e.printStackTrace();
						}
                	}
                    try {
						if (t.isFirstBetter(arg0.getBest(),arg1.getBest(), qi)) return -1;
						else return 1;
					} catch (Exception e) {
						e.printStackTrace();
					}
                } else return -1; //second is null
            } else
                if (arg1.getBest()!= null) return 1; //first null
            return 0; //both equal
        }
    }
}


