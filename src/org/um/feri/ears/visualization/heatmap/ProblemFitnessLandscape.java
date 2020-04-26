package org.um.feri.ears.visualization.heatmap;

import org.um.feri.ears.problems.unconstrained.*;
import org.um.feri.ears.problems.Problem;
import java.io.*;

public class ProblemFitnessLandscape {

    public static final String folderLocation = "problem_landscapes";

    public static void main(String[] args) {

        Problem problem = new Schwefel2_26(2);

        int numOfPartitions = 1000;
        double lowerBound = problem.lowerLimit.get(0);
        double upperBound = problem.upperLimit.get(0);


        double[][] data = new double[numOfPartitions][numOfPartitions];

        double[] partitions = GeneratePartitions(lowerBound, upperBound, numOfPartitions);

        for (int x = 0; x < numOfPartitions; x++) {
            for (int y = 0; y < numOfPartitions; y++) {
                data[x][y] = problem.eval(new double[]{partitions[x], partitions[y]});
            }
        }

        String text = ArrayToString(data);

        //System.getProperty("user.dir") + "/ProblemLandscapes";

        String fileName = problem.getName() + "_" + numOfPartitions + ".txt";

        File file = new File(folderLocation + "/" + fileName);

        try {
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
