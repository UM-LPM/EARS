package org.um.feri.ears.problems.moo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.Solution;
import org.um.feri.ears.quality_indicator.IndicatorFactory;
import org.um.feri.ears.quality_indicator.QualityIndicator;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.util.Util;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.PostscriptTerminal;

public class ParetoSolution<N extends Number> extends Solution implements Iterable<NumberSolution<N>> {

    public List<NumberSolution<N>> solutions;
    private HashMap<String, Double> qiEval = new HashMap<String, Double>();

    /**
     * Maximum size of the solution set
     */
    private int capacity = 0;

    public ParetoSolution(ParetoSolution<N> ps) {

        this.capacity = ps.capacity;
        this.constraintsMet = ps.constraintsMet;
        if (ps.constraints != null) {
            constraints = new double[ps.constraints.length];
            System.arraycopy(ps.constraints, 0, constraints, 0, constraints.length);
        }
        overallConstraintViolation = ps.getOverallConstraintViolation();
        numberOfViolatedConstraints = ps.getNumberOfViolatedConstraint();

        solutions = new ArrayList<>();
        for (NumberSolution<N> sol : ps) {
            solutions.add(sol.copy());
        }
    }
    public ParetoSolution() {
        solutions = new ArrayList<>();
        capacity = 1000;
    }

    public ParetoSolution(int maximumSize) {

        solutions = new ArrayList<>();
        capacity = maximumSize;
    }

    public ParetoSolution(int numberOfPoints, int numberOfObjectives) {
        capacity = numberOfPoints;
        solutions = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            NumberSolution<N> point = new NumberSolution<N>(numberOfObjectives);
            for (int j = 0; j < numberOfObjectives; j++) {
                point.setObjective(j, 0.0);
            }
            solutions.add(point);
        }
    }

    public ParetoSolution(List<NumberSolution<N>> pop) {
        solutions = pop;
        capacity = pop.size();
    }

    @Override
    public ParetoSolution<N> copy() {
        return new ParetoSolution<>(this);
    }

    /**
     * Returns evaluations for all unary quality indicators.
     *
     * @return all unary quality indicators evaluations.
     */
    public HashMap<String, Double> getAllQiEval() {
        return qiEval;
    }
    public double getQiEval(String name) {

        if (qiEval.containsKey(name)) {
            return qiEval.get(name);
        }
        return Double.MAX_VALUE;
    }

    public void setEvalForAllUnaryQIs(HashMap<String, Double> qiEval) {
        this.qiEval = qiEval;
    }

    public boolean add(NumberSolution<N> solution) {
        if (solutions.size() == capacity) {
            return false;
        }

        solutions.add(solution);
        return true;
    }

    /**
     * Replaces the solution at the given index.
     *
     * @param index    the index to replace
     * @param solution the new solution
     */
    public void replace(int index, NumberSolution<N> solution) {
        solutions.set(index, solution);
    }

    /**
     * Adds a collection of solutions to this population.
     *
     * @return {@code true} if the population was modified as a result of this
     * method; {@code false} otherwise
     */
    public boolean addAll(ParetoSolution<N> population) {
        boolean changed = false;

        for (NumberSolution<N> solution : population.solutions) {
            changed |= add(solution);
        }

        return changed;
    }

    public void addAll(List<NumberSolution<N>> pop) {

        for (NumberSolution<N> solution : pop) {
            add(solution);
        }
    }

    public NumberSolution<N> get(int i) {
        if (i >= solutions.size()) {
            throw new IndexOutOfBoundsException("Index out of Bound " + i);
        }
        return solutions.get(i);
    }

    public void set(int index, NumberSolution<N> solution) {
        solutions.set(index, solution);
    }

    /**
     * Returns {@code true} if this population contains no solutions;
     * {@code false} otherwise.
     *
     * @return {@code true} if this population contains no solutions;
     * {@code false} otherwise.
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

    public void evaluate(QualityIndicator<N> qi) throws Exception {
        this.evaluate(qi, false);
    }

    /**
     * Evaluates the Pareto approximation with the given quality indicator. If {@code useCache} is set to true
     * and the Pareto approximation is already evaluated with the given {@code qi}, that value is used.
     *
     * @param qi       the quality indicator used for evaluation
     * @param useCache set to true to read from cache
     * @throws Exception if {@code qi} is null or an incorrect type
     */
    public void evaluate(QualityIndicator<N> qi, boolean useCache) throws Exception {
        if (!useCache) {
            if (qi == null || qi.getIndicatorType() != IndicatorType.UNARY)
                throw new Exception("Indicator is null or incorrect indicator type!");
            double paretoEval = qi.evaluate(this);

            qiEval.put(qi.getName(), paretoEval);
        }
    }

    public void evaluateWithAllUnaryQI(int numObj, String fileName) throws Exception {
        for (IndicatorName name : IndicatorName.values()) {
            QualityIndicator<N> qi = IndicatorFactory.<N>createIndicator(name, numObj, fileName);
            if (qi.getIndicatorType() == IndicatorType.UNARY) {
                this.evaluate(qi, true);
            }
        }
    }

    public boolean isFirstBetter(ParetoSolution<N> second, QualityIndicator<N> qi) {
        if (qi == null) {
            System.err.println("Indicator is null!");
            return false;
        }

        if (qi.getIndicatorType() == QualityIndicator.IndicatorType.UNARY) {
            if (qi.isMin())
                return this.getEval() < second.getEval();
            return this.getEval() > second.getEval();

        } else if (qi.getIndicatorType() == QualityIndicator.IndicatorType.BINARY) {
            if (qi.compare(this, second, 0.0) == -1)
                return true;
        } else {
            return false;
        }
        return false;
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
     *                                   {@code (index < 0) || (index >= size())}
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
     * method; {@code false} otherwise
     */
    public boolean remove(NumberSolution<N> solution) {
        modCount++;
        return solutions.remove(solution);
    }

    /**
     * Returns {@code true} if this population contains the specified solution;
     * {@code false} otherwise.
     *
     * @param solution the solution whose presence is tested
     * @return {@code true} if this population contains the specified
     * solution; {@code false} otherwise
     */
    public boolean contains(NumberSolution<N> solution) {
        return solutions.contains(solution);
    }

    public void displayAllUnaryQualityIndicators(int num_obj, String file_name) {
        ArrayList<QualityIndicator<N>> indicators = new ArrayList<QualityIndicator<N>>();
        double value;
        // add all unary indicators to list
        for (IndicatorName name : IndicatorName.values()) {
            QualityIndicator<N> qi = IndicatorFactory.<N>createIndicator(name, num_obj, file_name);
            if (qi.getIndicatorType() == IndicatorType.UNARY)
                indicators.add(qi);
        }

        System.out.println("Quality indicators\n");
        for (QualityIndicator<N> qi : indicators) {
            value = qi.evaluate(this);
            System.out.println(qi.getName() + ": " + value);
        }
    }

    /**
     * Returns the index of the worst Solution using a <code>Comparator</code>.
     * If there are more than one occurrences, only the index of the first one is returned
     *
     * @param comparator <code>Comparator</code> used to compare solutions.
     * @return The index of the worst Solution attending to the comparator or
     * <code>-1<code> if the SolutionSet is empty
     */
    public int indexWorst(Comparator<NumberSolution<N>> comparator) {

        if ((solutions == null) || (this.solutions.isEmpty())) {
            return -1;
        }

        int index = 0;
        NumberSolution<N> worstKnown = solutions.get(0), candidateSolution;
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
     *
     * @param solutionSet to join with the current ParetoSolution.
     * @return The result of the union operation.
     */
    public ParetoSolution<N> union(ParetoSolution<N> solutionSet) {
        // Check the correct size. In development
        int newSize = this.size() + solutionSet.size();
        if (newSize < capacity)
            newSize = capacity;

        // Create a new population
        ParetoSolution<N> union = new ParetoSolution<>(newSize);
        for (int i = 0; i < this.size(); i++) {
            union.add(get(i));
        }

        for (int i = this.size(); i < (this.size() + solutionSet.size()); i++) {
            union.add(solutionSet.get(i - this.size()));
        }

        return union;
    }

    public void sort(Comparator<NumberSolution<N>> comparator) {
        if (comparator == null)
            return;

        solutions.sort(comparator);
    }

    /**
     * Copies the objectives of the solution set to a matrix
     *
     * @return A matrix containing the objectives
     */
    public double[][] writeObjectivesToMatrix() {
        if (this.size() == 0) {
            return null;
        }
        double[][] objectives;
        objectives = new double[size()][get(0).getNumberOfObjectives()];
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < get(0).getNumberOfObjectives(); j++) {
                objectives[i][j] = get(i).getObjective(j);
            }
        }
        return objectives;
    }

    public void loadObjectivesFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            solutions.clear();
            //ParetoSolution solutionSet = new NondominatedPopulation();

            String aux = br.readLine();
            while (aux != null) {
                StringTokenizer st = new StringTokenizer(aux);
                int i = 0;
                NumberSolution<N> solution = new NumberSolution<N>(st.countTokens());
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
     *
     * @param fileName The name of the file.
     */
    public void printObjectivesToCSVFile(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + ".csv")))) {
            for (NumberSolution<N> solution : solutions) {
                // if (this.vector[i].getFitness()<1.0) {
                bw.write(solution.toStringCSV());
                bw.newLine();
                // }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the variables to a file.
     *
     * @param fileName The name of the file.
     */
    public void printVariablesToFile(String fileName) {

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))) {
            if (solutions.size() > 0) {
                int numberOfVariables = solutions.get(0).getVariables().size();
                for (NumberSolution<N> solution : solutions) {
                    for (int j = 0; j < numberOfVariables; j++)
                        bw.write(solution.getValue(j) + " ");
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the objectives to a file.
     *
     * @param fileName The name of the file.
     */
    public void printFeasibleFUN(String fileName) {

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))) {
            for (NumberSolution<N> solution : solutions) {
                bw.write(solution.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (NumberSolution<N> i : solutions) {
            s.append("[").append(Util.arrayToString(i.getObjectives())).append("] \n");
        }
        return s.toString();
    }

    public void displayData(String algorithm, String problemN) {

        final String algorithmName = algorithm.replace("_", "\\_");
        final String problemName = problemN.replace("_", "\\_");

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
                p.setTitle(algorithmName + " - " + problemName, "Arial", 20);
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

    public void saveParetoImage(final String algorithmName, final String problemName) {

        final double[][] front = this.writeObjectivesToMatrix();
        Thread t = new Thread(new Runnable() {
            public void run() {
                PostscriptTerminal eps = new PostscriptTerminal(algorithmName + ".eps");

                PlotStyle myPlotStyle = new PlotStyle();
                myPlotStyle.setStyle(Style.POINTS);
                myPlotStyle.setPointType(7); // 7 - circle
                myPlotStyle.setLineType(3); // blue color

                JavaPlot p = new JavaPlot();
                p.setPersist(false);
                p.setTerminal(eps);
                p.setTitle(algorithmName + " - " + problemName, "Arial", 20);
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
    public Iterator<NumberSolution<N>> iterator() {
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
    private class PopulationIterator implements Iterator<NumberSolution<N>> {

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
        public NumberSolution<N> next() {
            checkModCount();

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            try {
                NumberSolution<N> value = get(nextIndex);
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
         *                                         count is not the value that was expected
         */
        private void checkModCount() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
