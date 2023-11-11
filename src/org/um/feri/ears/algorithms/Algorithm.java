package org.um.feri.ears.algorithms;

import org.um.feri.ears.benchmark.AlgorithmRunResult;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.Solution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Cache;
import org.um.feri.ears.util.annotation.AnnotationUtil;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public abstract class Algorithm<R extends Solution, S extends Solution, P extends Problem<S>> {

    protected Task<S, P> task;

    protected boolean debug;
    protected boolean displayData = false;
    protected boolean saveData = false;
    protected static Cache caching = Cache.NONE;

    protected String version = "1.0";
    protected Author au;
    protected AlgorithmInfo ai;
    protected AlgorithmInfo tempAi;
    protected AlgorithmRunTime art;

    //Tuning
    protected int age;
    protected ArrayList<Double> controlParameters;
    protected boolean played = false;

    /**
     * Method for creating callable object to run algorithms in parallel
     *
     * @param algorithm to be run in parallel
     * @param task      to be executed
     * @return callable object
     */
    public Callable<AlgorithmRunResult> createRunnable(final Algorithm<R, S, P> algorithm, final Task<S, P> task) {

        return () -> {
            long duration = System.nanoTime();
            R res = execute(task);
            duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - duration);
            algorithm.addRunDuration(duration, duration - task.getEvaluationTimeMs());
            AlgorithmRunResult future = new AlgorithmRunResult(res, algorithm, task);
            return future;
        };
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public boolean getPlayed() {
        return played;
    }

    public ArrayList<Double> getControlParameters() {
        return this.controlParameters;
    }

    public void addCustomInfo(String key, String value) {
        if (ai != null)
            ai.addCustomInfo(key, value);
    }

    public HashMap<String, String> getCustomInfo() {
        return ai.getCustomInfo();
    }

    /**
     * Adds total run time of the algorithm and the execution time without function evaluations.
     *
     * @param totalRunTime     total run time of the algorithm
     * @param algorithmRunTime execution time of the algorithm without function evaluations
     */
    public void addRunDuration(long totalRunTime, long algorithmRunTime) {
        if (art == null) {
            art = new AlgorithmRunTime();
        }
        art.addRunDuration(totalRunTime, algorithmRunTime);
    }

    public String getLastRunDuration() {
        return art.getLastDuration() + " s";
    }

    public void setTempAlgorithmInfo(AlgorithmInfo tempAi) {
        this.tempAi = ai;
        ai = tempAi;
    }

    public void setAlgorithmInfoFromTmp() {
        ai = tempAi;
    }

    public void setAlgorithmInfo(AlgorithmInfo ai) {
        this.ai = ai;
    }

    /**
     * Main method which executes the algorithm
     *
     * @param task to be executed
     * @return best found solution
     * @throws StopCriterionException if the stopping criterion is violated
     */
    public abstract R execute(Task<S, P> task) throws StopCriterionException;

    /**
     * Returns a filename safe string which contains the algorithm acronym and version
     *
     * @return filename safe string which contains the algorithm acronym and version
     */
    public String getFileNameString() {
        String fileName = ai.getAcronym().trim().replaceAll("[\\s_]", "-"); // replace all spaces and underscores with hyphen/dash
        fileName = fileName.replaceAll("[\\\\/:*?\"<>'%&@,.|{}+]", ""); // remove invalid characters
        fileName += "-" + version;
        return fileName;
    }

    public String getCacheKey(String taskString) {

        return "Algorithm = " + ai.getAcronym() + " version: " + version + ", " + getParametersAsString() + " " + taskString;
    }

    public String getVersion() {
        return version;
    }

    public String getParametersAsString() {
        return AnnotationUtil.getParameterNamesAndValues(this);
    }

    public String getParameterValue(String parameterName) {
        return AnnotationUtil.getParameterValue(this, parameterName);
    }

    public String getAlgorithmInfoCSV() {

        HashMap<String, String> customInfo = getCustomInfo();
        String customInfoCSV = "";

        for (Map.Entry<String, String> entry : customInfo.entrySet()) {
            customInfoCSV += entry.getKey() + ":" + entry.getValue() + ",";
        }

        return "algorithm name:" + ai.getAcronym() + ",algorithm version:" + version + "," + getParametersAsString() + "," + customInfoCSV;
    }

    /**
     * Used to reset algorithm's parameters before every run.
     */
    public abstract void resetToDefaultsBeforeNewRun();

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Author getImplementationAuthor() {
        return au;
    }

    public AlgorithmInfo getAlgorithmInfo() {
        return ai;
    }

    public String getId() {
        return ai.getAcronym();
    }

    public Algorithm<R, S, P> setId(String id) {
        ai.setAcronym(id);
        return this;
    }

    /**
     * Returns algorithms with different settings for selecting the best one!
     * maxCombinations is usually set to 8!
     * If maxCombinations==1 than return combination that is expected to perform best!
     * <p>
     *
     * @return
     */
    public List<Algorithm> getAlgorithmParameterTest(int dimension, int maxCombinations) {
        List<Algorithm> noAlternative = new ArrayList<Algorithm>();
        noAlternative.add(this);
        return noAlternative;
    }

    public void resetDuration() {
        art = new AlgorithmRunTime();
    }

    public void setDisplayData(boolean b) {
        displayData = b;
    }

    public boolean getDisplayData() {
        return displayData;
    }

    public void setSaveData(boolean b) {
        saveData = b;
    }

    public boolean getSaveData() {
        return saveData;
    }

    public static void setCaching(Cache c) {
        caching = c;
    }

    public static Cache getCaching() {
        return caching;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int aAge) {
        this.age = aAge;
    }

    public void increaseAge() {
        this.age = this.age + 1;
    }

    public Task<S, P> getTask() {
        return task;
    }
}
