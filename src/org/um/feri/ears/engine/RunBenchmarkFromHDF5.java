package org.um.feri.ears.engine;

import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5SimpleReader;
import ch.systemsx.cisd.hdf5.IHDF5SimpleWriter;
import org.um.feri.ears.algorithms.DummyAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.DummyBenchmark;
import org.um.feri.ears.statistic.rating_system.Player;

import java.util.ArrayList;

/**
 * Create rating leaderboard from HDF5 file. File structure: players [a1,a2,a3,a4....] problems[p1,p2,p3,p4....] data [[a1_p1],[a1_p2],[a1_p3]...[a2_p1],[a2_p2],[a2_p3]...]
 */

public class RunBenchmarkFromHDF5 {

    public static void main(String[] args) {

        Benchmark.printInfo = true; //prints one on one results

        if (args.length < 2) {
            System.out.println("ERROR: Not enough arguments (source file, destination file) provided!");
            return;
        }

        String srcFile = args[0];
        String destFile = args[1];

		/*String srcFile="D:\\Benchmark results\\test_rating_files2\\test.h5";
		String destFile="D:\\Benchmark results\\test_rating_files2\\results.h5";*/

        int[] numberOfAllSolutions;

        ArrayList<DummyAlgorithm> players = new ArrayList<DummyAlgorithm>();
        DummyBenchmark dr = new DummyBenchmark(0.000001); //Create banchmark
        dr.setDisplayRatingCharts(false);

        IHDF5SimpleReader reader = HDF5Factory.openForReading(srcFile);
        String[] playerList = reader.readStringArray("players");
        String[] problemList = reader.readStringArray("problems");
        double[][] data = reader.readDoubleMatrix("data");
        reader.close();

        numberOfAllSolutions = new int[playerList.length * problemList.length];

        int index = 0;
        for (String player : playerList) {
            DummyAlgorithm da = new DummyAlgorithm(player);
            for (String problem : problemList) {
                da.addProblemResults(problem, data[index]);
                numberOfAllSolutions[index] = data[index].length;
                index++;
            }
            players.add(da);
        }
        int numberOfSolutions = numberOfAllSolutions[0];
        //check solutions size
        for (int i = 1; i < numberOfAllSolutions.length; i++) {
            if (numberOfSolutions != numberOfAllSolutions[i]) {
                System.out.println("ERROR: file contains different number of solutions!");
                return;
            }
        }

        for (String name : problemList) {
            dr.addDummyTask(name);
        }

        for (DummyAlgorithm al : players) {
            dr.addAlgorithm(al);
        }

        long initTime = System.currentTimeMillis();
        dr.run(numberOfSolutions); //repeat competition 50X
        long estimatedTime = (System.currentTimeMillis() - initTime) / 1000;
        System.out.println("Benchmark execution time: " + estimatedTime + "s");
        ArrayList<Player> list = dr.getTournamentResults().getPlayers();

        String[] algorithms = new String[list.size()];
        double[] ratings = new double[list.size()];

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i)); //print ranks
            ratings[i] = list.get(i).getGlicko2Rating().getRating();
            algorithms[i] = list.get(i).getId();
        }

        IHDF5SimpleWriter writer = HDF5Factory.open(destFile);
        writer.writeStringArray("players", algorithms);
        writer.writeDoubleArray("ratings", ratings);

        writer.close();

    }

}
