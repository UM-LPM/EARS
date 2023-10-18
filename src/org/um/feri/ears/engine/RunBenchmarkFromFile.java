package org.um.feri.ears.engine;

import org.um.feri.ears.algorithms.DummyAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.DummyBenchmark;
import org.um.feri.ears.statistic.rating_system.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create rating leaderboard from single file. Each line in the file contains the results of a single algorithm on a single problem (algorithmName_ProblemName;run1;run2;run3;....).
 */

public class RunBenchmarkFromFile {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("ERROR: Not enough arguments (source file, destination file) provided!");
            return;
        }

        String srcFile = args[0];
        String destFile = args[1];

        Benchmark.printInfo = true; //prints one on one results

        //from args
        //String srcFile = "D:\\Benchmark results\\soil_model_fitness_files\\merged.txt";
        //String destFile = "D:\\Benchmark results\\soil_model_fitness_files\\output.txt";

        List<String> problems = new ArrayList<String>();

        int numberOfsolutions = 0;

        ArrayList<DummyAlgorithm> players = new ArrayList<DummyAlgorithm>();
        DummyBenchmark dr = new DummyBenchmark(0.000001); //Create banchmark
        dr.setDisplayRatingCharts(false);
        //parse algorithm and problem names
        String algorithmName, problemName;
        try (BufferedReader br = new BufferedReader(new FileReader(srcFile))) {
            String line = br.readLine();
            while (line != null) {
                String[] singleproblemResults = line.split(";");
                if (singleproblemResults.length > 0) {

                    int index = singleproblemResults[0].indexOf("_");
                    if (index == 0) {
                        line = br.readLine();
                        continue;
                    }
                    algorithmName = (singleproblemResults[0].substring(0, index)).toLowerCase();
                    problemName = (singleproblemResults[0].substring(index + 1)).toLowerCase();

                    double[] resultArray = new double[10000];

                    DummyAlgorithm alg = getOrCreatePlayer(algorithmName, players);

                    for (int i = 0; i + 1 < singleproblemResults.length; i++) {
                        resultArray[i] = Double.parseDouble(singleproblemResults[i + 1]);
                    }

                    //numberOfsolutions = resultArray.length;

                    alg.addProblemResults(problemName, resultArray);
                    problems.add(problemName);
                }

                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //get distinct problem names
        List<String> distinctProblem = problems.stream().distinct().collect(Collectors.toList());
        //add problems to benchmark
        for (String name : distinctProblem) {
            dr.addDummyTask(name);
        }


        for (DummyAlgorithm al : players) {
            dr.addAlgorithm(al);
        }
        long initTime = System.currentTimeMillis();
        dr.run(100); //repeat competition 50X
        long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
        System.out.println("Benchmark execution time: " + estimatedTime + "s");
        ArrayList<Player> list = dr.getTournamentResults().getPlayers();
        StringBuilder sb = new StringBuilder();
        for (Player p : list) {
            System.out.println(p); //print ranks
            sb.append(p.toString());
            sb.append("\n");
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile)))) {
            bw.write(sb.toString());
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static DummyAlgorithm getOrCreatePlayer(String algorithmName, ArrayList<DummyAlgorithm> players) {

        for (DummyAlgorithm alg : players) {
            if (alg.getAlgorithmInfo().getAcronym().equals(algorithmName))
                return alg;
        }

        DummyAlgorithm newPlayer = new DummyAlgorithm(algorithmName);
        players.add(newPlayer);

        return newPlayer;
    }

}
