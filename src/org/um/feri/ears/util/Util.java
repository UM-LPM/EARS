package org.um.feri.ears.util;
/**
 * Simple util methods.
 * 
 * <p>
 * 
 * @author Matej Crepinsek
 * @version 1
 * 
 *          <h3>License</h3>
 * 
 *          Copyright (c) 2011 by Matej Crepinsek. <br>
 *          All rights reserved. <br>
 * 
 *          <p>
 *          Redistribution and use in source and binary forms, with or without
 *          modification, are permitted provided that the following conditions
 *          are met:
 *          <ul>
 *          <li>Redistributions of source code must retain the above copyright
 *          notice, this list of conditions and the following disclaimer.
 *          <li>Redistributions in binary form must reproduce the above
 *          copyright notice, this list of conditions and the following
 *          disclaimer in the documentation and/or other materials provided with
 *          the distribution.
 *          <li>Neither the name of the copyright owners, their employers, nor
 *          the names of its contributors may be used to endorse or promote
 *          products derived from this software without specific prior written
 *          permission.
 *          </ul>
 *          <p>
 *          THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *          "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *          LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *          FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *          COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *          INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *          BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *          LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *          CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *          LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *          ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *          POSSIBILITY OF SUCH DAMAGE.
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Util {
    public static DecimalFormat df0 = new DecimalFormat("####.##");
    public static DecimalFormat df01 = new DecimalFormat("####.####");
	public static DecimalFormat df1 = new DecimalFormat("#,###.#");
	public static DecimalFormat df = new DecimalFormat("#,###.##");
	public static DecimalFormat df3 = new DecimalFormat("#,###.###");
	public static DecimalFormat df6 = new DecimalFormat("#,###.######");
	public static DecimalFormat dfc1 = new DecimalFormat("#,##0.#######E0");
	public static DecimalFormat dfc2 = new DecimalFormat("#,##0.##E0");
	public static DecimalFormat dfcshort = new DecimalFormat("0.##E0");
	public static DecimalFormat intf = new DecimalFormat("###,###,###");
	public static long randomseed = 316227711; //to be able too repeat experiment
	public static Random rnd = new MersenneTwister(randomseed);
	static final String JSON_FILE = "Cache\\Pareto_Cache.json";
	
	
    public static double roundDouble3(double r) {
        return roundDouble(r, 3); 
    }
	public static double roundDouble(double r, int dec) {
	    r = Math.round(r*Math.pow(10, dec));
	    return r/Math.pow(10, dec);
	}
	public static String arrayToString(List<Double> d) {
		String s = "";
		for (int i = 0; i < d.size(); i++) {
			s = s + df01.format(d.get(i));
			if (i < d.size() - 1)
				s = s + ", ";
		}
		return s;
	}
	
	public static String arrayToString(double[] d) {
		String s = "";
		for (int i = 0; i < d.length; i++) {
			s = s + df01.format(d[i]);
			if (i < d.length - 1)
				s = s + ", ";
		}
		return s;
	}
	
	public static void addParetoToJSON(String listID, ParetoSolution<Type> best)
	{
		ParetoSolutionCache cache = new ParetoSolutionCache();
		Gson gson = new Gson();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(JSON_FILE));
			cache = gson.fromJson(br, ParetoSolutionCache.class); 
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if(cache == null)
			cache = new ParetoSolutionCache();
		
		if(cache.data.containsKey(listID))
		{
			if(cache.data.get(listID).size() >= cache.limit)
			{
				return;
			}
			List<List<double[]>> solutions = cache.data.get(listID);
			
			List<double[]> paretoList = new ArrayList<>();
			
			for(MOSolutionBase<Type> s: best.solutions)
			{
				double[] objectives = s.getObjectives();
				paretoList.add(objectives);
			}
			solutions.add(paretoList);
			cache.data.put(listID, solutions);

		}
		else
		{
			List<List<double[]>> solutions = new ArrayList<List<double[]>>();
			
			List<double[]> paretoList = new ArrayList<double[]>();
			
			for(MOSolutionBase<Type> s: best.solutions)
			{
				double[] objectives = s.getObjectives();
				paretoList.add(objectives);
			}
			solutions.add(paretoList);
			cache.data.put(listID, solutions);
		}
		
		try (Writer writer = new FileWriter(JSON_FILE)) {
		    gson.toJson(cache, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<ParetoSolution<Double>> readParetoListFromJSON(String listID)
	{
		ParetoSolutionCache cache = new ParetoSolutionCache();
		Gson gson = new Gson();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(JSON_FILE));
			cache = gson.fromJson(br, ParetoSolutionCache.class); 
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if(cache == null)
			return null;
		if(!cache.data.containsKey(listID))
			return null;
		
		List<ParetoSolution<Double>> solutions = new ArrayList<ParetoSolution<Double>>();
		List<List<double[]>> ps = cache.data.get(listID);
		
		for(List<double[]> pareto : ps)
		{
			ParetoSolution<Double> solution = new ParetoSolution<Double>(pareto.size());
			
			for(double[] obj : pareto)
			{
				MOSolutionBase<Double> sol = new MOSolutionBase<>(obj.length);
				sol.setObjectives(obj);
				solution.add(sol);
			}
			
			solutions.add(solution);
		}
		
		return solutions;
	}
	
/*
	public static void printPopulationCompare(double d[][], double e[],
			Problem p, int gen) {
		System.out.println("Generation:" + gen);
		for (int i = 0; i < d.length; i++) {
			System.out.println(arrayToString(d[i]) + " eval:" + e[i]
					+ " real: " + p.eval(d[i]) + " constrain: "
					+ p.constrainsEvaluations(d[i]));
			if (e[i] != p.eval(d[i]))
				System.out.println("ERROR!!!");
		}
	}

	public static void printPopulation(double d[][], Problem p, int gen) {
		System.out.println("Generation:" + gen);
		for (int i = 0; i < d.length; i++) {
			System.out.println(arrayToString(d[i]) + " eval:" + p.eval(d[i])
					+ " constrain: " + p.constrainsEvaluations(d[i]));
		}
	}*/

	public static double divide(double a, double b) {
		if (b == 0)
			return 0;
		return a / b;
	}
	
	public static int nextInt(int n) {
		return rnd.nextInt(n);
	}
	
	
	/**
	 * Returns the next random, uniformly distributed {@code int} value between
	 * {@code 0} (inclusive) and {@code n} (exclusive).
	 * 
	 * @return the next random, uniformly distributed {@code int} value between
	 *         {@code 0} (inclusive) and {@code n} (exclusive).
	 */
	public static int nextInt(int lowerBound, int upperBound) {
		return lowerBound + rnd.nextInt((upperBound - lowerBound + 1)) ;
	}
	
	public static double nextDouble() {
		return rnd.nextDouble();
	}
	
	/**
	 * Returns the next random, uniformly distributed {@code double} value
	 * between {@code min} and {@code max}.
	 * 
	 * @return the next random, uniformly distributed {@code double} value
	 *         between {@code min} and {@code max}
	 */
	public static double nextDouble(double min, double max) {
		return min + rnd.nextDouble() * (max - min);
	}
	
	/**
	 * Shuffles the elements of the specified array using the same algorithm as
	 * {@link Collections#shuffle}.
	 * 
	 * @param <T> the type of element stored in the array
	 * @param array the array to be shuffled
	 */
	public static <T> void shuffle(T[] array) {
		for (int i = array.length - 1; i >= 1; i--) {
			int j = nextInt(i + 1);

			if (i != j) {
				T temp = array[i];
				array[i] = array[j];
				array[j] = temp;
			}
		}
	}
	
	public static int[] randomPermutation(int length) {
		int[] permutation = new int[length];
		
		for (int i = 0; i < length; i++) {
			permutation[i] = i;
		}
		
		Util.shuffle(permutation);
		
		return permutation;
	}
	
	/**
	 * Shuffles the elements of the specified array using the same algorithm as
	 * {@link Collections#shuffle}.
	 * 
	 * @param array the array to be shuffled
	 */
	public static void shuffle(int[] array) {
		for (int i = array.length - 1; i >= 1; i--) {
			int j = nextInt(i + 1);

			if (i != j) {
				int temp = array[i];
				array[i] = array[j];
				array[j] = temp;
			}
		}
	}
	
	/**
	 * Returns a randomly selected item from the specified list.
	 * 
	 * @param <T> the type of the elements stored in the list
	 * @param list the list from which the item is randomly selected
	 * @return a randomly selected item from the specified list
	 */
	public static <T> T nextItem(List<T> list) {
		return list.get(Util.nextInt(list.size()));
	}
	
	/**
	 * Returns the next random, uniformly distributed {@code boolean} value.
	 * 
	 * @return the next random, uniformly distributed {@code boolean} value.
	 */
	public static boolean nextBoolean() {
		return rnd.nextBoolean();
	}


}
