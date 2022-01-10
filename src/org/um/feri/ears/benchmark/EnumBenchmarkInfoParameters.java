package org.um.feri.ears.benchmark;
/**
 * This parameters can be used for dynamically tuning algorithms, based
 * on parameters
 */
public enum EnumBenchmarkInfoParameters {
    DIMENSION("number of real parameters/dimensions","D"),
    STOPPING_CRITERIA("stopping criteria used in the benchmark","STOPPING_CRITERIA"),
    ITERATIONS("number of iterations","IT"),
    CPU_TIME("CPU time allowed fore evaluation","CT"),
    EVAL("number of evaluations","E"),
    CONSTRAINED("constrained optimization","C_O"),
    STOP_IF_GLOBAL("stops when global optimum was founded","GLOBAL_STOP"),
    DRAW_PARAM("sets condition for draw","DRAW_PARAM"),
    NUMBER_OF_TEST_CONFIGURATIONS("maximum number of tested parameter configurations","BEST_OF_X_CONFIGURATIONS"),
    NUMBER_OF_TASKS("number of problems","NUMBER_OF_TASKS"), 
    NUMBER_OF_DUELS("number of duels / repetitions","NUMBER_OF_DEULS"),
	BENCHMARK_RUN_DURATION("run duration of the benchmark in milliseconds","DURATION");
    
    private String description, shortName;
    private EnumBenchmarkInfoParameters(String s, String sh) {
        description = s;
        shortName = sh;
        
    }   
    public String getDescription() {
        return description;
    }

    public String getShortName() {
        return shortName;
    }

    public String toString() {
        return shortName;
    }
}
