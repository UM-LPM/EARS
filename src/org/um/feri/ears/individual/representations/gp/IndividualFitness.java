package org.um.feri.ears.individual.representations.gp;

import java.io.Serializable;
import java.util.HashMap;

public class IndividualFitness implements Serializable
{
    public int IndividualID;
    public float Value;
    public HashMap<String, Double> IndividualValues;
    public HashMap<String, Double> AdditionalValues;

    public IndividualFitness(int individualID)
    {
        this(individualID, new HashMap<>(){{
            put("Rating", Double.MAX_VALUE);
            put("StdDeviation", Double.MAX_VALUE);
        }});
    }

    public IndividualFitness(int individualID, double value, double stdDeviation)
    {
        this(individualID, new HashMap<>(){{
            put("Rating", value);
            put("StdDeviation", stdDeviation);
        }});
    }

    public IndividualFitness(int individualID, HashMap<String, Double> additionalValues)
    {
        this.IndividualID = individualID;
        this.Value = 0;
        this.IndividualValues = new HashMap<>();
        this.AdditionalValues = additionalValues;
    }
}