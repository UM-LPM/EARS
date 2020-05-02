package org.um.feri.ears.visualization.heatmap;

import org.um.feri.ears.problems.unconstrained.*;
import org.um.feri.ears.problems.Problem;

import java.io.*;

public class ProblemFitnessLandscape {

    public static final String folderLocation = "problem_landscapes";

    public static void main(String[] args) {

        Problem problem = new WayburnSeader3();

        double fit = problem.eval(problem.getOptimalVector()[0]);
        double global = problem.getGlobalOptimum();
        double min = fit;
        System.out.println("Closeness to global: " + (global - fit));
        int numOfPartitions = 1000;

        double[][] data = new double[numOfPartitions][numOfPartitions];

        double[] partitionsX1 = GeneratePartitions(problem.lowerLimit.get(0), problem.upperLimit.get(0), numOfPartitions);
        double[] partitionsX2 = GeneratePartitions(problem.lowerLimit.get(1), problem.upperLimit.get(1), numOfPartitions);

        for (int x = 0; x < numOfPartitions; x++) {
            for (int y = 0; y < numOfPartitions; y++) {
                fit = problem.eval(new double[]{partitionsX1[x], partitionsX2[y]});
                if(Double.isNaN(fit))
                    System.out.println("Nan at "+partitionsX1[x]+" "+partitionsX2[y]);
                if(Double.isInfinite(fit))
                    System.out.println("Infinite at "+partitionsX1[x]+" "+partitionsX2[y]);
                if(min >= fit){
                    min = fit;
                    System.out.println(min);
                    System.out.println(partitionsX1[x]+" "+partitionsX2[y]);
                }
                /*if(Math.abs(fit - global) <= 0.001)
                    System.out.println("global");*/
                data[x][y] = fit;
            }
        }

        String text = ArrayToString(data);
        //System.getProperty("user.dir") + "/ProblemLandscapes";

        String fileName = problem.getName() + "_" + numOfPartitions + ".txt";

        File file = new File(folderLocation + "/" + fileName);

        try {
            File directory = new File(folderLocation);
            if (! directory.exists()){
                directory.mkdir();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);//save the string representation of the board
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private static String ArrayToString(double[][] data) {
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < data.length; x++) {
            for (int y = 0; y < data.length; y++) {
                sb.append(data[x][y] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static double[][] GetProblemHeatmapValues(String problemName, int numOfPartitions) {

        double[][] data = new double[numOfPartitions][numOfPartitions];

        String file = folderLocation + "/" + problemName + "_" + numOfPartitions + ".txt";

        //TODO check if file exists
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            int row = 0;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(" ");
                int col = 0;
                for (String c : cols) {
                    data[row][col] = Double.parseDouble(c);
                    col++;
                }
                row++;
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }

        return data;
    }

    private static double[] GeneratePartitions(double lowerBound, double upperBound, int numOfPartitions) {
        double partition = (Math.abs(lowerBound) + Math.abs(upperBound)) / numOfPartitions;

        double[] partitions = new double[numOfPartitions];
        partitions[0] = lowerBound;
        partitions[numOfPartitions - 1] = upperBound;

        for (int i = 1; i < numOfPartitions - 1; i++) {
            partitions[i] = partitions[i - 1] + partition;
        }
        return partitions;
    }
}
