package org.um.feri.ears.individual.representations.gp;

import java.io.Serializable;
import java.util.HashMap;

public class IndividualMatchResult implements Serializable {
    public int[] opponentsIDs;
    public String matchName;
    public float value;
    public HashMap<String, Double> individualValues;

    public IndividualMatchResult(){
        this.opponentsIDs = new int[]{};
        this.matchName = "";
        this.value = 0;
        this.individualValues = new HashMap<>();
    }

    public IndividualMatchResult(IndividualMatchResult imr){
        this.opponentsIDs = imr.opponentsIDs.clone();
        this.matchName = imr.matchName;
        this.value = imr.value;
        this.individualValues = new HashMap<>();
        this.individualValues.putAll(imr.individualValues);
    }
}
