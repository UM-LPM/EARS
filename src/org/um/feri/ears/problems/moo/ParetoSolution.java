//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.

package org.um.feri.ears.problems.moo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.util.Util;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.PostscriptTerminal;

public class ParetoSolution<Type extends Number> extends SolutionBase<Type> implements Iterable<MOSolutionBase<Type>> {

	public ParetoSolution(ParetoSolution<Type> ps) {

		this.capacity = ps.capacity;
		this.pareto_eval = ps.pareto_eval;
		this.feasible = ps.feasible;
		if (ps.constraints!=null) {
			constraints = new double[ps.constraints.length];
			System.arraycopy(ps.constraints, 0, constraints, 0, constraints.length);
		}
		overallConstraintViolation_  = ps.getOverallConstraintViolation();
		numberOfViolatedConstraints_ = ps.getNumberOfViolatedConstraint();

		solutions = new ArrayList<MOSolutionBase<Type>>();
		for (MOSolutionBase<Type> sol : ps) {
			solutions.add(sol.copy());
		}
	}

	public List<MOSolutionBase<Type>> solutions;
	private double pareto_eval;

	private HashMap<String,Double> qiEval = new HashMap<String,Double>();

	/**
	 * Returns evaluations for all unary quality indicators.
	 * @return all unary quality indicators evaluations.
	 */
	public HashMap<String, Double> getAllQiEval() {
		return qiEval;
	}

	public void setEvalForAllUnaryQIs(HashMap<String, Double> qiEval) {
		this.qiEval = qiEval;
	}

	/**
	 * Maximum size of the solution set
	 */
	private int capacity = 0;

	public ParetoSolution() {
		solutions = new ArrayList<MOSolutionBase<Type>>();
		capacity = 1000;
	}

	public ParetoSolution(int maximumSize) {

		solutions = new ArrayList<MOSolutionBase<Type>>();
		capacity = maximumSize;
	}

	public ParetoSolution(int numberOfPoints, int numberOfObjectives) {
		capacity = numberOfPoints;
		solutions = new ArrayList<MOSolutionBase<Type>>();
	    for (int i = 0; i < numberOfPoints; i++) {
	      MOSolutionBase<Type> point = new MOSolutionBase<Type>(numberOfObjectives) ;
	      for (int j = 0; j < numberOfObjectives; j++) {
	        point.setObjective(j, 0.0);
	      }
	      solutions.add(point);
	    }
	}

	public ParetoSolution(List<MOSolutionBase<Type>> pop) {
		solutions = pop;
		capacity = pop.size();
	}

	public boolean add(MOSolutionBase<Type> solution) {
		if (solutions.size() == capacity) {
			return false;
		}

		solutions.add(solution);
		return true;
	}

	/**
	 * Replaces the solution at the given index.
	 *
	 * @param index the index to replace
	 * @param solution the new solution
	 */
	public void replace(int index, MOSolutionBase<Type> solution) {
		solutions.set(index, solution);
	}

	/**
	 * Adds a collection of solutions to this population.
	 *
	 * @param iterable the collection of solutions to be added
	 * @return {@code true} if the population was modified as a result of this
	 *         method; {@code false} otherwise
	 */
	public boolean addAll(ParetoSolution<Type> population) {
		boolean changed = false;

		for (MOSolutionBase<Type> solution : population.solutions) {
			changed |= add(solution);
		}

		return changed;
	}

	public void addAll(List<MOSolutionBase<Type>> pop) {

		for (MOSolutionBase<Type> solution : pop) {
			add(solution);
		}
	}

	public MOSolutionBase<Type> get(int i) {
		if (i >= solutions.size()) {
			throw new IndexOutOfBoundsException("Index out of Bound " + i);
		}
		return solutions.get(i);
	}

	public void set(int index, MOSolutionBase<Type> solution)
	{
		solutions.set(index, solution);
	}

	/**
	 * Returns {@code true} if this population contains no solutions;
	 * {@code false} otherwise.
	 *
	 * @return {@code true} if this population contains no solutions;
	 *         {@code false} otherwise.
	 */
	public boolean isEmpty() {
		return solutions.isEmpty();
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public double getEval() {

		return pareto_eval;
	}

	public void evaluate(QualityIndicator<Type> qi) throws Exception
	{
		this.evaluate(qi,false);
	}

	/**
	 * Evaluates the Pareto approximation with the given quality indicator. If {@code usecache} is set to true
	 * and the Pareto approximation is already evaluated with the given {@code qi}, that value is used.
	 * @param qi the quality indicator used for evaluation
	 * @param usecache set to true to read from cache
	 * @throws Exception if {@code qi} is null or an incorrect type
	 */
    public void evaluate(QualityIndicator<Type> qi, boolean usecache) throws Exception
    {
    	if(usecache)
    	{
    		if(qiEval.containsKey(qi.getName()))
    		{
    			pareto_eval = qiEval.get(qi.getName());
    			return;
    		}
    	}

    	// throw error if the indicator is null or not unary
    	if(qi == null || qi.getIndicatorType() != IndicatorType.Unary)
			throw new Exception("Indicator is null or incorrect indicator type!");
		pareto_eval = qi.evaluate(this);

		qiEval.put(qi.getName(), pareto_eval); //replace value
    }

    public void evaluteWithAllUnaryQI(int num_obj, String file_name) throws Exception
    {
    	for (IndicatorName name : IndicatorName.values()) {
    		QualityIndicator<Type> qi = IndicatorFactory.<Type>createIndicator(name, num_obj, file_name);
    		if(qi.getIndicatorType() == IndicatorType.Unary)
    		{
    			this.evaluate(qi, true);
    		}
    	}
    }


	public boolean isFirstBetter(ParetoSolution<Type> second, QualityIndicator<Type> qi)
	{
		if(qi == null)
		{
			System.err.println("Indicator is null!");
			return false;
		}

		if(qi.getIndicatorType() == QualityIndicator.IndicatorType.Unary)
		{
		    if (qi.isMin())
				return this.getEval() < second.getEval();
			return this.getEval() > second.getEval();

		}
		else if(qi.getIndicatorType() == QualityIndicator.IndicatorType.Binary)
		{
			if(qi.compare(this, second, 0.0) == -1)
				return true;
		}
		else
		{
			return false;
		}
		return false;
	}

	@Override
	public List<Type> getVariables() {
		List<Type> x = null; //TODO check
		return x;
	}

	public int getMaxSize() {
		return capacity;
	}

	public void clear() {
		modCount++;
		solutions.clear();
	}

	public int size() {
		return solutions.size();
	}

	/**
	 * Removes the solution at the specified index from this population.
	 *
	 * @param index the index of the solution to be removed
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *         {@code (index < 0) || (index >= size())}
	 */
	public void remove(int index) {
		modCount++;
		solutions.remove(index);
	}

	/**
	 * Removes the specified solution from this population, if present.
	 *
	 * @param solution the solution to be removed
	 * @return {@code true} if this population was modified as a result of this
	 *         method; {@code false} otherwise
	 */
	public boolean remove(MOSolutionBase<Type> solution) {
		modCount++;
		return solutions.remove(solution);
	}

	/**
	 * Returns {@code true} if this population contains the specified solution;
	 * {@code false} otherwise.
	 *
	 * @param solution the solution whose presence is tested
	 * @return {@code true} if this population contains the specified
	 *         solution; {@code false} otherwise
	 */
	public boolean contains(MOSolutionBase<Type> solution) {
		return solutions.contains(solution);
	}

	public void displayAllUnaryQulaityIndicators(int num_obj, String file_name)
	{
		 ArrayList<QualityIndicator<Type>> indicators = new ArrayList<QualityIndicator<Type>>();
		 double value;
		 // add all unary indicators to list
		 for (IndicatorName name : IndicatorName.values()) {
			  QualityIndicator<Type> qi = IndicatorFactory.<Type>createIndicator(name, num_obj, file_name);
			  if(qi.getIndicatorType() == IndicatorType.Unary)
				  indicators.add(qi);
		 }

		 System.out.println("Quality indicators\n");
		 for (QualityIndicator<Type> qi : indicators) {
			value = qi.evaluate(this);
			System.out.println(qi.getName()+": " + value);
		}
	}

	 /**
	  * Returns the index of the worst Solution using a <code>Comparator</code>.
	  * If there are more than one occurrences, only the index of the first one is returned
	  * @param comparator <code>Comparator</code> used to compare solutions.
	  * @return The index of the worst Solution attending to the comparator or
	  * <code>-1<code> if the SolutionSet is empty
	  */
	public int indexWorst(Comparator<MOSolutionBase<Type>> comparator) {

		if ((solutions == null) || (this.solutions.isEmpty())) {
			return -1;
		}

		int index = 0;
		MOSolutionBase<Type> worstKnown = solutions.get(0), candidateSolution;
		int flag;
		for (int i = 1; i < solutions.size(); i++) {
			candidateSolution = solutions.get(i);
			flag = comparator.compare(worstKnown, candidateSolution);
			if (flag == -1) {
				index = i;
				worstKnown = candidateSolution;
			}
		}

		return index;
	}

	/**
	 * Returns a new <code>MOParetoIndividual</code> which is the result of the union
	 * between the current solution set and the one passed as a parameter.
	 * @param ParetoSolution MOParetoIndividual to join with the current MOParetoIndividual.
	 * @return The result of the union operation.
	 */
	public ParetoSolution<Type> union(ParetoSolution<Type> solutionSet) {
		// Check the correct size. In development
		int newSize = this.size() + solutionSet.size();
		if (newSize < capacity)
			newSize = capacity;

		// Create a new population
		ParetoSolution<Type> union = new ParetoSolution<>(newSize);
		for (int i = 0; i < this.size(); i++) {
			union.add(get(i));
		}

		for (int i = this.size(); i < (this.size() + solutionSet.size()); i++) {
			union.add(solutionSet.get(i - this.size()));
		}

		return union;
	}

	public void sort(Comparator<MOSolutionBase<Type>> comparator) {
		if (comparator == null)
			return;

		Collections.sort(solutions, comparator);
	}

	/**
   * Copies the objectives of the solution set to a matrix
   * @return A matrix containing the objectives
   */
	public double[][] writeObjectivesToMatrix() {
		if (this.size() == 0) {
			return null;
		}
		double[][] objectives;
		objectives = new double[size()][get(0).numberOfObjectives()];
		for (int i = 0; i < size(); i++) {
			for (int j = 0; j < get(0).numberOfObjectives(); j++) {
				objectives[i][j] = get(i).getObjective(j);
			}
		}
		return objectives;
	}

	public void loadObjectivesFromFile(String fileName)
	{
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))){
			solutions.clear();
			//ParetoSolution solutionSet = new NonDominatedSolutionList();

			String aux = br.readLine();
			while (aux != null) {
				StringTokenizer st = new StringTokenizer(aux);
				int i = 0;
				MOSolutionBase<Type> solution = new MOSolutionBase<Type>(st.countTokens());
				while (st.hasMoreTokens()) {
					double value = Double.parseDouble(st.nextToken());
					solution.setObjective(i, value);
					i++;
				}
				solutions.add(solution);
				aux = br.readLine();
			}
		} catch (Exception e) {
			System.out.println("readNonDominatedSolutionSet: " + fileName);
			e.printStackTrace();
		}
	}

    /**
     * Prints the objectives to a file in CSV format.
     * @param file_name The name of the file.
     */
	public void printObjectivesToCSVFile(String file_name) {
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_name + ".csv")))){
			for (MOSolutionBase<Type> aSolutionsList_ : solutions) {
				// if (this.vector[i].getFitness()<1.0) {
				bw.write(aSolutionsList_.toStringCSV());
				bw.newLine();
				// }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	 /**
     * Prints the variables to a file.
     * @param file_name The name of the file.
     */
	public void printVariablesToFile(String file_name) {

		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_name)))){
			if (solutions.size() > 0) {
				int numberOfVariables = solutions.get(0).getVariables().size();
				for (MOSolutionBase<Type> aSolutionsList : solutions) {
					for (int j = 0; j < numberOfVariables; j++)
						bw.write(aSolutionsList.getValue(j) + " ");
					bw.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     * Prints the objectives to a file.
     * @param file_name The name of the file.
     */
	public void printFeasibleFUN(String file_name) {
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_name)))){
			for (MOSolutionBase<Type> aSolutionsList : solutions) {
				bw.write(aSolutionsList.toString());
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		String s = "";
		for (MOSolutionBase<Type> i : solutions) {
			s += "[" + Util.arrayToString(i.getObjectives()) + "] \n";
		}
		return s;
	}

	public void displayData(String algorithm, String problemN) {

		final String algorithm_name = algorithm.replace("_", "\\_");
		final String problem_name = problemN.replace("_", "\\_");

		final double[][] front = this.writeObjectivesToMatrix();
		Thread t = new Thread(new Runnable() {
			public void run() {
				PlotStyle myPlotStyle = new PlotStyle();
				myPlotStyle.setStyle(Style.POINTS);
				myPlotStyle.setPointType(7); // 7 - circle
				myPlotStyle.setLineType(3); // blue color

				// myPlotStyle.setPointSize(1);
				// myPlotStyle.setLineType(9);
				// myPlotStyle.setLineType(NamedPlotColor.BLUE);
				JavaPlot p = new JavaPlot(); // true if 3D plot
				p.setTitle(algorithm_name + " - " + problem_name, "Arial", 20);
				p.getAxis("x").setLabel("f1", "Arial", 15);
				p.getAxis("y").setLabel("f2", "Arial", 15);
				// p.getAxis("x").setBoundaries(-30, 20);
				p.setKey(JavaPlot.Key.TOP_RIGHT);

				DataSetPlot s = new DataSetPlot(front);
				s.setPlotStyle(myPlotStyle);
				s.setTitle("");
				p.addPlot(s);
				p.plot();
			}
		});
		t.start();

	}

	public void saveParetoImage(final String algorithm_name,final String problem_name) {

		final double[][] front = this.writeObjectivesToMatrix();
		Thread t = new Thread(new Runnable() {
	         public void run()
	         {
				PostscriptTerminal eps = new PostscriptTerminal(algorithm_name+".eps");

				PlotStyle myPlotStyle = new PlotStyle();
				myPlotStyle.setStyle(Style.POINTS);
				myPlotStyle.setPointType(7); // 7 - circle
				myPlotStyle.setLineType(3); // blue color

				JavaPlot p = new JavaPlot();
				p.setPersist(false);
				p.setTerminal(eps);
				p.setTitle(algorithm_name + " - " + problem_name, "Arial", 20);
				p.getAxis("x").setLabel("f1", "Arial", 15);
				p.getAxis("y").setLabel("f2", "Arial", 15);
				p.setKey(JavaPlot.Key.TOP_RIGHT);

				DataSetPlot s = new DataSetPlot(front);
				s.setPlotStyle(myPlotStyle);
				s.setTitle("");
				p.addPlot(s);
				p.plot();
	         }
		});
		t.start();
	}

	/**
	 * Returns an iterator for accessing the solutions in this population.
	 */
	@Override
	public Iterator<MOSolutionBase<Type>> iterator() {
		return new PopulationIterator();
	}

	/*
	 * The following code is based on the Apache Commons Collections library.
	 * This is to provide a similar iterator behavior to other collection
	 * classes without requiring the Population to implement all collection
	 * methods.  The license terms are provided below.
	 *
	 * Licensed to the Apache Software Foundation (ASF) under one or more
	 * contributor license agreements.  See the NOTICE file distributed with
	 * this work for additional information regarding copyright ownership.
	 * The ASF licenses this file to You under the Apache License, Version 2.0
	 * (the "License"); you may not use this file except in compliance with
	 * the License.  You may obtain a copy of the License at
	 *
	 *     http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 */

	/**
	 * The modification count.
	 */
	private int modCount;

	/**
	 * An iterator over the solutions in a population.
	 */
	private class PopulationIterator implements Iterator<MOSolutionBase<Type>> {

		/**
		 * The index of the next node to be returned.
		 */
		private int nextIndex;

		/**
		 * The index of the last node that was returned.  Set to {@code -1}
		 * if the iterator is not positioned at a valid node (i.e., at
		 * initialization or after an element is removed).
		 */
		private int currentIndex;

		/**
         * The modification count that the list is expected to have. If the list
         * doesn't have this count, then a
         * {@link java.util.ConcurrentModificationException} may be thrown by
         * the operations.
         */
		private int expectedModCount;

		/**
		 * Constructs a population iterator.
		 */
		public PopulationIterator() {
			super();

			nextIndex = 0;
			currentIndex = -1;
			expectedModCount = modCount;
		}

		@Override
		public boolean hasNext() {
			return nextIndex != size();
		}

		@Override
		public MOSolutionBase<Type> next() {
			checkModCount();

			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			try {
				MOSolutionBase<Type> value = get(nextIndex);
				currentIndex = nextIndex++;
				return value;
			} catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}

		@Override
		public void remove() {
			checkModCount();

			if (currentIndex == -1) {
				throw new IllegalStateException();
			}

			try {
				ParetoSolution.this.remove(currentIndex);
				nextIndex--;
				currentIndex = -1;
				expectedModCount++;
			} catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}

		/**
         * Checks the modification count of the list is the value that this
         * object expects.
         *
         * @throws ConcurrentModificationException if the list's modification
         *         count is not the value that was expected
         */
		private void checkModCount() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
		}
	}

}
