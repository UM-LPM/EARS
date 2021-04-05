package org.um.feri.ears.problems;

public enum StopCriterion {
	EVALUATIONS("Number of evaluations","Stop execution when the maximum number of evaluations is reached"), 
	GLOBAL_OPTIMUM_OR_EVALUATIONS("Number of evaluations or global optimum","Stop execution when the maximum number of evaluations or the global optimum is reached"), 
	CPU_TIME("CPU time","Stop execution when the allowed CPU time is reached"), 
	ITERATIONS("Number of iterations","Stop execution when the maximum number of iterations is reached"), 
	STAGNATION("Stagnation","Stop execution if the solutions do not improve over time");
	
    private String description, name;
    private StopCriterion(String name, String description) {
        this.description = description;
        this.name = name;
        
    }   
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
