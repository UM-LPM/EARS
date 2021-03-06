package org.um.feri.ears.engine;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.um.feri.ears.benchmark.DummyBenhcmark;
import org.um.feri.ears.benchmark.MOBenchmark;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.BenchmarkBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GenerateBenchmarksFile {

    static String earsPath;
    static String earsFolder;
    static String configDir;

    static final String BENCHMARKS_FILE = "benchmarks.json";
    static final String CONFIG_FOLDER = "config";
    static final String BENCHMARK_PACKAGE = "org.um.feri.ears.benchmark";
    private static final String DUMMY_BENCHMARK_CLASS_NAME = DummyBenhcmark.class.getSimpleName();
    private static final String MO_BENCHMARK_CLASS_NAME = MOBenchmark.class.getSimpleName();
    private static final String SO_BENCHMARK_CLASS_NAME = Benchmark.class.getSimpleName();

    public static void main(String[] args) {

        final File f = new File(ExecuteTournaments.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        earsPath = f.getPath();
        earsFolder = f.getParent();
        configDir = new File(earsFolder).getParent() + File.separator + CONFIG_FOLDER;

        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(BENCHMARK_PACKAGE))));


        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        List<BenchmarkJson> benchmarks = new ArrayList<BenchmarkJson>();
        for (Class<? extends Object> clazz : classes) {
            if (!clazz.getName().contains("$")) {

                if (Modifier.isAbstract(clazz.getModifiers()) || clazz.getSimpleName().equals(DUMMY_BENCHMARK_CLASS_NAME)) //check if class is abstract or DummyRating
                    continue;

                try {
                    if (clazz.getSuperclass() != null) {
                        if (clazz.getSuperclass().getSimpleName().equals(MO_BENCHMARK_CLASS_NAME)) {
                            System.out.println("Multi-Objective: " + clazz.getName());
                            Object benchmark = clazz.newInstance();
                            BenchmarkJson b = new BenchmarkJson();
                            b.name = ((BenchmarkBase) benchmark).getName();
                            b.fileName = clazz.getSimpleName();
                            b.numberOfRuns = ((BenchmarkBase) benchmark).getNumberOfRuns();
                            b.type = "Multi-Objective";
                            b.stopCriteria = ((BenchmarkBase) benchmark).getStopCriterion().toString();
                            b.stopCondition = ((BenchmarkBase) benchmark).getStopCondition();
                            b.problems = ((BenchmarkBase) benchmark).getProblems();
                            benchmarks.add(b);

                        } else if (clazz.getSuperclass().getSimpleName().equals(SO_BENCHMARK_CLASS_NAME)) {
                            System.out.println("Single-Objective: " + clazz.getName());
                            Object benchmark = clazz.newInstance();
                            BenchmarkJson b = new BenchmarkJson();
                            b.name = ((BenchmarkBase) benchmark).getName();
                            b.fileName = clazz.getSimpleName();
                            b.numberOfRuns = ((BenchmarkBase) benchmark).getNumberOfRuns();
                            b.type = "Single-Objective";
                            b.stopCriteria = ((BenchmarkBase) benchmark).getStopCriterion().toString();
                            b.stopCondition = ((BenchmarkBase) benchmark).getStopCondition();
                            b.problems = ((BenchmarkBase) benchmark).getProblems();
                            benchmarks.add(b);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try (Writer writer = new FileWriter(configDir + File.separator + BENCHMARKS_FILE)) {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            gson.toJson(benchmarks, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
