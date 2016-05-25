package org.um.feri.ears.problems;

public enum EnumProblemSolutionDataTypes {
    REAL_VECTOR("vector with real numbers (double)","vector"),
    REAL_VALUE("real number (double)","one value")
    ;  
    private String description, shortName;
    private EnumProblemSolutionDataTypes(String s, String sh) {
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
