package org.um.feri.ears.problems;

public enum EnumProblemTypes {
    SORPO("single objective real parameter optimization", "SORPO"),
    MORPO("multi objective real parameter optimization", "MORPO")
    ;  
    private String description, shortName;
    private EnumProblemTypes(String s, String sh) {
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
