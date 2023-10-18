package org.um.feri.ears.tuning;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.SOBenchmark;
import org.um.feri.ears.statistic.rating_system.GameInfo;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.util.comparator.RatingComparator;
import org.um.feri.ears.util.random.RNG;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class CRSTuning {

    // Control parameters
    private int max_execs;
    private double Cr_rate = 1.00;
    private double F_rate = 1.00;
    private int M = 25; //number of players
    private int E = M / 2;

    private ArrayList<Player> players;
    private boolean printDebug;
    private boolean printSingleRunDuration;
    private HashMap<String, NumberAlgorithm> algorithms;
    protected SOBenchmark benchmark; // suopm = new RatingRPUOed2();
    private long duration;
    private int noRepeats;

    public static void writeToFile(String pFilename, StringBuffer pData) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(pFilename));
        out.write(pData.toString());
        out.flush();
        out.close();
    }

    public StringBuffer outputRanking(int run) {
        StringBuffer sb = new StringBuffer();
        sb.append("CRS4EAs output " + run + "\n");
        sb.append("i \t Age \t Algorithm \t Rating \t RD \t RI \t RV" + "\n");
        for (int i = 0; i < players.size(); i++) {
            NumberAlgorithm algorithm = algorithms.get(players.get(i).getId());
            sb.append((i) + " \t " + (algorithm.getAge())
                    + " \t " + (algorithm.getId().replace("\n", "").replace("\r", ""))
                    + " \t " + Math.round(players.get(i).getGlicko2Rating().getRating())
                    + " \t " + Math.round(players.get(i).getGlicko2Rating().getRatingDeviation())
                    + " \t [" + Math.round(players.get(i).getGlicko2Rating().getRating() - 2 * players.get(i).getGlicko2Rating().getRatingDeviation())
                    + "," + Math.round(players.get(i).getGlicko2Rating().getRating() + 2 * players.get(i).getGlicko2Rating().getRatingDeviation())
                    + "] \t " + Math.round(players.get(i).getGlicko2Rating().getRatingVolatility()) + "\n");
        }
        sb.append("-------------------------------------------------------------");
        return sb;
    }

    public void restartRatings() {
        for (Player al : players) {
            al.setGlicko2Rating(GameInfo.getDefaultGlicko2Rating());
        }
    }

    public CRSTuning(boolean printDebug, boolean printSingleRunDuration, SOBenchmark benchmark, int max_execs) {
        algorithms = new HashMap<>();
        this.printDebug = printDebug;
        this.benchmark = benchmark;
        players = new ArrayList<>();
        this.printSingleRunDuration = printSingleRunDuration;
        this.max_execs = max_execs;
    }

    public void addAlgorithm(NumberAlgorithm algorithm) {
        if (!algorithms.containsKey(algorithm.getId())) {
            algorithms.put(algorithm.getId(), algorithm);
            if (algorithm.getAlgorithmInfo() == null)
                System.out.println("Add algorithm with null AlgorithmInfo " + algorithm.getClass().getName());
            if (algorithm.getImplementationAuthor() == null)
                System.out.println("Add algorithm with null Author " + algorithm.getClass().getName());
            players.add(new Player(algorithm.getId()));
            benchmark.addAlgorithm(algorithm);
        }
    }

    public void removeAlgorithm(Algorithm algorithm) {
        benchmark.removeAlgorithm(algorithm);
        algorithms.remove(algorithm.getId());
        benchmark.getTournamentResults().removePlayer(algorithm.getId());
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId().compareTo(algorithm.getId()) == 0) {
                players.remove(i);
                break;
            }
        }
    }

    public void tune(int repeat, ArrayList<ControlParameter> control_parameters, Class<? extends NumberAlgorithm> classAlg, String aName, int decimals) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        noRepeats = repeat;
        String file = "CRS tuning" + "_" + aName + "M" + M + "F" + F_rate + "Cr" + Cr_rate + "_" + System.currentTimeMillis() + ".txt";
        StringBuffer sb = new StringBuffer();

        sb.append("CR: " + Cr_rate + "\nF: " + F_rate + "\nMu: " + M + "\nEa: " + E + "\nMaximum number of executions: " + max_execs + "\n------------------\n\n");


        //Class<?> classAlg = null;
        Constructor<?> ctor = null;
        try {
            //classAlg = Class.forName(classAlg);
            ctor = classAlg.getConstructor(ArrayList.class, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }


        while (algorithms.size() <= M) {
            ArrayList<Double> configuration = new ArrayList<Double>();
            for (ControlParameter cp : control_parameters) {
                configuration.add(cp.randomValue());
            }

            NumberAlgorithm object = (NumberAlgorithm) ctor.newInstance(configuration, aName);
            this.addAlgorithm(object);
        }

        long stTime = System.currentTimeMillis();
        Benchmark.printInfo = printDebug; // prints one on one results

        sb.append(outputRanking(0));
        System.out.println(outputRanking(0));

        int i = algorithms.size();
        int gen = 0;
        while (i < max_execs) {
            benchmark.run(repeat);
            benchmark.allPlayed();
            players.sort(new RatingComparator());
            // Remove significantly worse algorithms
            for (int j = 1; j < players.size(); j++) {
                double difference = Math.abs(Math.round(players.get(0).getGlicko2Rating().getRating() - players.get(j).getGlicko2Rating().getRating()));
                if (difference > 200 || j > E) {
                    removeAlgorithm(algorithms.get(players.get(j).getId()));
                    j--;
                }
            }
            int n_players = algorithms.size();
            int cr1 = 0, cr2 = 0;
            NumberAlgorithm new_alg1 = null;
            NumberAlgorithm new_alg2 = null;
            i = i + (M - algorithms.size());
            // Create new player through crossover and mutation
            if (i < max_execs) {
                while (algorithms.size() < M) {
                    for (int j = 0; j < n_players; j++) {
                        // Find two individuals for crossover
                        if (RNG.nextDouble() < Cr_rate && algorithms.size() < M) {
                            if (cr1 != 0) cr2 = j;
                            else cr1 = j;
                        }
                        // Uniform Crossover
                        if (cr1 != 0 && cr2 != 0) {
                            ArrayList<Double> child1 = new ArrayList<Double>();
                            ArrayList<Double> child2 = new ArrayList<Double>();
                            for (int k = 0; k < control_parameters.size(); k++) {
                                if (RNG.nextDouble() < 0.5) {
                                    child1.add(algorithms.get(cr1).getControlParameters().get(k));
                                    child2.add(algorithms.get(cr2).getControlParameters().get(k));
                                } else {
                                    child1.add(algorithms.get(cr2).getControlParameters().get(k));
                                    child2.add(algorithms.get(cr1).getControlParameters().get(k));
                                }
                            }
                            new_alg1 = (NumberAlgorithm) ctor.newInstance(child1, aName);
                            new_alg2 = (NumberAlgorithm) ctor.newInstance(child2, aName);
                            if (algorithms.size() < M) this.addAlgorithm(new_alg1);
                            if (algorithms.size() < M) this.addAlgorithm(new_alg2);
                            cr1 = 0;
                            cr2 = 0;
                        }

                        ArrayList<Double> child = new ArrayList<Double>();
                        // Mutation
                        int mutated = 0;
                        for (int m = 0; m < control_parameters.size(); m++) {
                            if (RNG.nextDouble() < F_rate && algorithms.size() < M) {
                                child.add(control_parameters.get(m).randomValue());
                                mutated = 1;
                            } else {
                                child.add(algorithms.get(j).getControlParameters().get(m));
                            }
                        }
                        if (mutated == 1) {
                            new_alg1 = (NumberAlgorithm) ctor.newInstance(child, aName);
                            if (algorithms.size() < M) this.addAlgorithm(new_alg1);
                        }
                    }
                }
            } else {
                break;
            }
            // Output
            sb.append(outputRanking(gen));
            System.out.println(outputRanking(gen));
            System.out.println(i);
            gen++;
            //this.restartRatings();
        }

        long endTime = System.currentTimeMillis();
        duration = endTime - stTime;
        System.out.println("DURATION: " + duration / 1000 + "s");
        sb.append("\n\nDURATION: " + duration / 1000 + "s\n");
        try {
            writeToFile(file, sb);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Results for benchmark:").append(benchmark.getShortName()).append("Benchmark DURATION: (" + duration / 1000 + "s)").append("\n").append("\n");
        ;
        for (Player a : players) {
            sb.append(a.getId()).append(" ").append(a.getGlicko2Rating().toString()).append("\n");
        }
        return sb.toString();
    }
}
