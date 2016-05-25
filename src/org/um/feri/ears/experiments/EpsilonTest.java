package org.um.feri.ears.experiments;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.um.feri.ears.algorithms.moo.nsga2.D_NSGAII;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.dtlz.DTLZ1;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem1;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem10;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem2;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem3;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem4;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem5;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem6;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem7;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem8;
import org.um.feri.ears.problems.moo.unconstrained.cec2009.UnconstrainedProblem9;
import org.um.feri.ears.problems.moo.wfg.WFG1;
import org.um.feri.ears.problems.moo.wfg.WFG2;
import org.um.feri.ears.problems.moo.wfg.WFG3;
import org.um.feri.ears.problems.moo.wfg.WFG4;
import org.um.feri.ears.problems.moo.wfg.WFG5;
import org.um.feri.ears.problems.moo.wfg.WFG6;
import org.um.feri.ears.problems.moo.wfg.WFG7;
import org.um.feri.ears.problems.moo.wfg.WFG8;
import org.um.feri.ears.problems.moo.wfg.WFG9;
import org.um.feri.ears.problems.moo.zdt.ZDT1;
import org.um.feri.ears.problems.moo.zdt.ZDT2;
import org.um.feri.ears.problems.moo.zdt.ZDT3;
import org.um.feri.ears.problems.moo.zdt.ZDT4;
import org.um.feri.ears.problems.moo.zdt.ZDT6;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.util.Util;

public class EpsilonTest {
	
	public static void main(String[] args) {

        Util.rnd.setSeed(System.currentTimeMillis());
        
        //MOTask task = new MOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, new UnconstrainedProblem1());
        
	    List<IndicatorName> indi = new ArrayList<>();
	    //indi.add(IndicatorName.CovergeOfTwoSets);
	    indi.add(IndicatorName.Epsilon);
	    //indi.add(IndicatorName.EpsilonBin);
	    indi.add(IndicatorName.GD);
	    //indi.add(IndicatorName.GeneralizedSpread);
	    indi.add(IndicatorName.Hypervolume);
	    indi.add(IndicatorName.IGD);
	    indi.add(IndicatorName.IGDPlus);
	    //indi.add(IndicatorName.MaximumSpread);
	    indi.add(IndicatorName.MPFE);
	    //indi.add(IndicatorName.R2);
	    indi.add(IndicatorName.Spacing);
	    Collections.sort(indi, new Comparator<IndicatorName>() {
	    	@Override
	    	public int compare(IndicatorName in1, IndicatorName in2) {
	    		return in1.compareTo(in2);
	    	}
	    } );
	    
	    
	    
	    ArrayList<DoubleMOProblem> problems = new ArrayList<DoubleMOProblem>();
    	

	    problems.add(new UnconstrainedProblem1());
    	problems.add(new UnconstrainedProblem2());
    	problems.add(new UnconstrainedProblem3());
    	problems.add(new UnconstrainedProblem4());
    	problems.add(new UnconstrainedProblem5());
    	problems.add(new UnconstrainedProblem6());
    	problems.add(new UnconstrainedProblem7());
    	problems.add(new UnconstrainedProblem8());
    	problems.add(new UnconstrainedProblem9());
    	problems.add(new UnconstrainedProblem10());
	    problems.add(new ZDT1());
	    problems.add(new ZDT2());
	    problems.add(new ZDT3());
	    problems.add(new ZDT4());
	    problems.add(new ZDT6());
	    problems.add(new WFG1());
	    problems.add(new WFG2());
	    problems.add(new WFG3());
	    problems.add(new WFG4());
	    problems.add(new WFG5());
	    problems.add(new WFG6());
	    problems.add(new WFG7());
	    problems.add(new WFG8());
	    problems.add(new WFG9());
	    problems.add(new DTLZ1(2));
	    problems.add(new DTLZ1(3));
	    problems.add(new DTLZ1(4));
	    //problems.add(new DTLZ1(6));
	    //problems.add(new DTLZ1(8));*/
	    
	    double[] epsilon = {
	    	1e-11,
	    	1e-10,
	    	1e-9,
	    	1e-8,
	    	1e-7,
	    	1e-6,
	    	1e-5,
	    	1e-4,
	    	1e-3,
	    	1e-2,
	    	1e-1,
	    	1.0
	    };
	    
    	StringBuilder sb = new StringBuilder();
    	D_NSGAII alg = new D_NSGAII();
    	
    	sb.append("Algorithm: "+alg.getAlgorithmInfo().getVersionAcronym()+";");
    	
    	for(DoubleMOProblem mop : problems)
    	{
    		sb.append(mop.name+" ");
    	}
    	sb.append(";\n;");
    	
    	for (IndicatorName name : indi) {
    		sb.append(name+";");
    	}
    	sb.append("\n");
    	
    	double[][] data = new double[indi.size()][50];
    	int[][] count = new int[indi.size()][epsilon.length];
    	int indiCounter = 0;
    	double diff;
    	
		for (DoubleMOProblem mop : problems) {
			
			for (int i = 0; i < 50; i++) {
				DoubleMOTask task = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 300000, 0.0001, mop);
				

				try {
					ParetoSolution best = alg.run(task);
					indiCounter = 0;
					for (IndicatorName name : indi) {
						QualityIndicator qi = IndicatorFactory.createIndicator(name, task.getProblem());
						best.evaluate(qi);
						//sb.append(best.getEval()+";");
						data[indiCounter][i] = best.getEval();
						indiCounter++;
					}
					//sb.append("\n");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			for(int j = 0; j < indi.size(); j++)
			{
				Arrays.sort(data[j]);
				
				for (int x = 0; x < data[j].length - 1; x++) {
					diff = data[j][x+1] - data[j][x];
					if(diff < 0.0)
						break;
					
					for (int y = 0; y < epsilon.length; y++) {
						if(diff < epsilon[y])
							count[j][y]++;
					}
				}
			}
		}
		
		NumberFormat formatter = new DecimalFormat("0.0E0");
		
		for (int i = 0; i < epsilon.length; i++) {
			sb.append(formatter.format(epsilon[i])+";");
			for (int j = 0; j < indi.size(); j++) {
				sb.append(count[j][i]+";");
			}
			sb.append("\n");
		}
		
		try {
			FileOutputStream fos = new FileOutputStream("D:\\Benchmark results\\EpislonTest_NSGAII_Count.csv");
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(sb.toString());
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
