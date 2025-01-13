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
        this(individualID, null);
    }

    public IndividualFitness(int individualID, HashMap<String, Double> additionalValues)
    {
        this.IndividualID = individualID;
        this.Value = 0;
        this.IndividualValues = new HashMap<>();
        this.AdditionalValues = additionalValues;
    }
}