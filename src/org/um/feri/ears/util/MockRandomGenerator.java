package org.um.feri.ears.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class MockRandomGenerator {
	private int index;
	private List<Double> numbers;
	private final static String numbersFilePath = "D://TestNumbers2.txt";
	
	public MockRandomGenerator() {
		this(numbersFilePath);
	}
	
	public MockRandomGenerator(String filePath) {
		index = 0;
		try {
			numbers = Files.lines(Paths.get(filePath))
			        .map(Double::parseDouble)
			        .collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double nextDouble() {
		if (index >= numbers.size())
			index = 0;
		return numbers.get(index++);
	}
	
	public double nextDouble(double min, double max) {
		if (index >= numbers.size())
			index = 0;
		double random = numbers.get(index++);
		double range = max - min;
		return min + (random * range);
	}
	
	public int nextInt(int min, int max) {
		if (index >= numbers.size())
			index = 0;
		double random = numbers.get(index++);
		int range = max - min;
		return min + (int)(random * range); // Rounds DOWN! TODO: Round to nearest ? 
	}

}
