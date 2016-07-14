package org.um.feri.ears.qualityIndicator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.text.StrTokenizer;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.RedirectStream;

public class NativeHV<T extends Number> extends QualityIndicator<T>{

	private static final double DELTA_ = 0.01;
	public NativeHV(int num_obj, String file_name) {
		super(num_obj, file_name, (ParetoSolution<T>) getReferenceSet(file_name));
		name="WFGHypervolume";
	}

	@Override
	public double evaluate(ParetoSolution<T> population) {
		
		double[][] normalizedApproximation;
		normalizedApproximation = MetricsUtil.getNormalizedFront(population.writeObjectivesToMatrix(), maximumValue, minimumValue);
		
		double[][] invertedFront = MetricsUtil.invertedFront(normalizedApproximation);

		return invokeNativeHypervolume(invertedFront, true);
	}
	

	/**
	 * Since hypervolume calculation is expensive, this method provides the
	 * ability to execute a native process to calculate hypervolume. If
	 * provided, the {@code org.moeaframework.core.indicator.native.hypervolume}
	 * system property defines the command for invoking the native hypervolume
	 * executable.
	 * 
	 * @param solutions
	 * @param isInverted {@code true} if the solutions are inverted;
	 *        {@code false} otherwise
	 * @return the hypervolume value
	 */
	private double invokeNativeHypervolume(double[][] solutions, boolean isInverted) {
		try {
			String command = "./wfg.exe {0}";
			
			//compute the nadir point for minimization or maximization scenario
			/*double nadirPoint;
			
			if (isInverted) {
				nadirPoint = 0.0 - DELTA_;
			} else {
				nadirPoint = 1.0 + DELTA_;
			}*/
			
			//generate approximation set file
			File approximationSetFile = File.createTempFile("approximationSet", null);
			approximationSetFile.deleteOnExit();
				
			writeObjectives(approximationSetFile, solutions);
			

			// construct the command for invoking the native process
			Object[] arguments = new Object[] {
					approximationSetFile.getCanonicalPath(),
					};

			// invoke the native process
			return invokeNativeProcess(MessageFormat.format(command, arguments));
		} catch (IOException e) {
			//throw new FrameworkException(e);
			System.out.println(e.getMessage());
		}
		return 0.0;
	}

	/**
	 * Invokes the native process whose last output token should be the
	 * indicator value.
	 * 
	 * @param command the command to execute
	 * @return the indicator value
	 * @throws IOException if an I/O error occurred
	 */
	private static double invokeNativeProcess(String command) throws IOException {
		
		Process process = new ProcessBuilder(parseCommand(command)).start();
		RedirectStream.redirect(process.getErrorStream(), System.err);
		BufferedReader reader = null;
		String lastLine = null;

		try {
			reader = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			String line = null;

			while ((line = reader.readLine()) != null) {
				lastLine = line;
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		String[] tokens = lastLine.split("\\s+");
		return Double.parseDouble(tokens[tokens.length - 1]);
	}
	
	/**
	 * Splits an executable command into its individual arguments.  This method
	 * allows quoted text ({@code "..."}) in properties to be treated as an
	 * individual argument as required by {@link ProcessBuilder}.
	 *  
	 * @param command the command represented in a single string
	 * @return the individual arguments comprising the command
	 */
	public static String[] parseCommand(String command) {
		return new StrTokenizer(command).setQuoteChar('\"').getTokenArray();
	}
	
	/**
	 * Writes the objective vectors of all solutions to the specified file. 
	 * Files created using this method should only be loaded using the 
	 * {@code loadObjectives} method.
	 * 
	 * @param file the file to which the objective vectors are written
	 * @param solutions the solutions whose objective vectors are written to
	 *        the specified file
	 * @throws IOException if an I/O exception occurred
	 */
	public static void writeObjectives(File file, double[][] solutions) throws IOException {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write("#");
			writer.newLine();
			for(int j=0; j < solutions.length;j++)
			{
				writer.write(Double.toString(solutions[j][0]));
				
				for (int i = 1; i < solutions[j].length; i++) {
					writer.write(" ");
					writer.write(Double.toString(solutions[j][i]));
				}
				writer.newLine();
			}
			writer.write("#");
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	@Override
	public IndicatorType getIndicatorType() {
		return IndicatorType.Unary;
	}

	@Override
	public boolean isMin() {
		return false;
	}

	@Override
	public boolean requiresReferenceSet() {
		return false;
	}

	@Override
	public int compare(ParetoSolution<T> front1, ParetoSolution<T> front2, Double epsilon) {
		return 0;
	}

}
