package org.um.feri.ears.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.um.feri.ears.benchmark.SOBenchmark;
import org.um.feri.ears.benchmark.DummyBenchmark;
import org.um.feri.ears.benchmark.MOBenchmark;
import org.um.feri.ears.benchmark.Benchmark;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class GenerateBenchmarksFileTest {

    static String earsPath;
    static String earsFolder;

    static String configDir = "D:\\VSCodeProjects\\platforma\\config";

    private static final String BENCHMARKS_FILE = "benchmarks.json";
    private static final String CONFIG_FOLDER = "config";
    private static final String BENCHMARK_PACKAGE = "org.um.feri.ears.benchmark";
    private static final String DUMMY_BENCHMARK_CLASS_NAME = DummyBenchmark.class.getSimpleName();
    private static final String MO_BENCHMARK_CLASS_NAME = MOBenchmark.class.getSimpleName();
    private static final String SO_BENCHMARK_CLASS_NAME = SOBenchmark.class.getSimpleName();

    public static void main(String[] args) {

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
                            b.name = ((Benchmark) benchmark).getName();
                            b.fileName = clazz.getSimpleName();
                            b.numberOfRuns = ((Benchmark) benchmark).getNumberOfRuns();
                            b.type = "Multi-Objective";
                            b.stopCriteria = ((Benchmark) benchmark).getStopCriterion().toString();
                            b.stopCondition = ((Benchmark) benchmark).getStoppingCriterion();
                            b.problems = ((Benchmark) benchmark).getProblems();
                            benchmarks.add(b);

                        } else if (clazz.getSuperclass().getSimpleName().equals(SO_BENCHMARK_CLASS_NAME)) {
                            System.out.println("Single-Objective: " + clazz.getName());
                            Object benchmark = clazz.newInstance();
                            BenchmarkJson b = new BenchmarkJson();
                            b.name = ((Benchmark) benchmark).getName();
                            b.fileName = clazz.getSimpleName();
                            b.numberOfRuns = ((Benchmark) benchmark).getNumberOfRuns();
                            b.type = "Single-Objective";
                            b.stopCriteria = ((Benchmark) benchmark).getStopCriterion().toString();
                            b.stopCondition = ((Benchmark) benchmark).getStoppingCriterion();
                            b.problems = ((Benchmark) benchmark).getProblems();
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
