package org.um.feri.ears.algorithms;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.um.feri.ears.benchmark.AlgorithmRunResult;
import org.um.feri.ears.benchmark.EnumBenchmarkInfoParameters;
import org.um.feri.ears.problems.SolutionBase;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.TaskBase;
import org.um.feri.ears.util.Cache;

public abstract class AlgorithmBase<T extends TaskBase, S extends SolutionBase> {

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

    public Callable<AlgorithmRunResult> createRunnable(final AlgorithmBase<T, S> al, final T task) {

        return () -> {
            long duration = System.nanoTime();
            S res = execute(task);
            duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - duration);
            al.addRunDuration(duration, duration - task.getEvaluationTimeMs());
            AlgorithmRunResult future = new AlgorithmRunResult(res, al, task);
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

    public String getCustomInfoByKey(String key) {
        if (ai != null)
            return ai.getCustomInfoByKey(key);
        return "";
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
     * @param task
     * @return
     * @throws StopCriterionException
     */
    public abstract S execute(T task) throws StopCriterionException;

    /**
     * Returns a filename safe string which contains the algorithm acronym and version
     * @return filename safe string which contains the algorithm acronym and version
     */
    public String getFileNameString() {
        String fileName = ai.getAcronym().trim().replaceAll("[\\s_]", "-"); // replace all spaces and underscores with hyphen/dash
        fileName = fileName.replaceAll("[\\\\/:*?\"<>'%&@,.|{}+]", ""); // remove invalid characters
        fileName += "-"+version;
        return fileName;
    }

    public String getCacheKey(String taskString) {

        return "Algorithm = " + ai.getAcronym() + " version: " + version + ", " + shortAlgorithmInfo() + " " + taskString;
    }

    public String getVersion() {
        return version;
    }

    public String shortAlgorithmInfo() {

        String info = "";

        for (Entry<EnumAlgorithmParameters, String> entry : ai.getParameters().entrySet()) {
            info += entry.getKey().getShortName() + ":" + entry.getValue() + ",";
        }

        //remove last comma
        if (info.length() > 1)
            info = info.substring(0, info.length() - 2);

        return info;
    }

    public String longAlgorithmInfo() {

        String info = "";

        for (Entry<EnumAlgorithmParameters, String> entry : ai.getParameters().entrySet()) {
            info += entry.getKey().getDescription() + ": " + entry.getValue() + ", ";
        }

        if (info.length() > 1)
            info = info.substring(0, info.length() - 2);

        return info;
    }

    public String getAlgorithmInfoCSV() {

        HashMap<String, String> customInfo = getCustomInfo();
        String customInfoCSV = "";

        for (Map.Entry<String, String> entry : customInfo.entrySet()) {
            customInfoCSV += entry.getKey() + ":" + entry.getValue() + ",";
        }

        return "algorithm name:" + ai.getAcronym() + ",algorithm version:" + version + "," + shortAlgorithmInfo() + "," + customInfoCSV;
    }

    /**
     * It is called every time before every run!
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

    public String getID() {
        return ai.getAcronym();
    }

    public AlgorithmBase<T, S> setID(String id) {
        ai.setAcronym(id);
        return this;
    }

    /**
     * Returns algorithms with different settings for selecting the best one!
     * maxCombinations is usually set to 8!
     * If maxCombinations==1 than return combination that is expected to perform best!
     * <p>
     * NOTE not static because java doesn't support abstract static!
     *
     * @return
     */
    public List<AlgorithmBase> getAlgorithmParameterTest(EnumMap<EnumBenchmarkInfoParameters, String> parameters, int maxCombinations) {
        List<AlgorithmBase> noAlternative = new ArrayList<AlgorithmBase>();
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
}
