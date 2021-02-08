package org.um.feri.ears.algorithms;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class AlgorithmInfo {
    private String acronym; // used as algorithm ID
    private String fullName;
    private String paperBibTeX; // BibTeX string of the original published paper
    private EnumMap<EnumAlgorithmParameters, String> parameters; //add all specific parameters
    private HashMap<String, String> customInfo = new HashMap<String, String>();

    public AlgorithmInfo(String acronym, String fullName, String paperBibTeX) {
        this.acronym = acronym;
        this.fullName = fullName;
        this.paperBibTeX = paperBibTeX;
        //http://download.oracle.com/javase/1,5.0/docs/guide/language/enums.html
        parameters = new EnumMap<EnumAlgorithmParameters, String>(EnumAlgorithmParameters.class);
    }

    public void addCustomInfo(String key, String value) {
        customInfo.put(key, value);
    }

    public String getCustomInfoByKey(String key) {
        return customInfo.get(key);
    }

    public HashMap<String, String> getCustomInfo() {
        return customInfo;
    }

    public String paramsToString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (EnumAlgorithmParameters t : parameters.keySet()) {
            if (first) first = false;
            else sb.append(",");
            sb.append(t.getShortName()).append(" = ").append(parameters.get(t));
        }
        sb.append("]");
        return sb.toString();
    }

    //returns parameters
    public Map<EnumAlgorithmParameters, String> getParameters() {
        Map<EnumAlgorithmParameters, String> tmp = new EnumMap<EnumAlgorithmParameters, String>(parameters);
        tmp = Collections.unmodifiableMap(tmp);
        return tmp;
    }

    public void addParameter(EnumAlgorithmParameters id, String value) {
        parameters.put(id, value);
    }

    public void addParameters(EnumMap<EnumAlgorithmParameters, String> operatorParameters) {

        if (operatorParameters != null) {
            parameters.putAll(operatorParameters);
        }
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getPaperBibTeX() {
        return paperBibTeX;
    }

    public void setPaperBibTeX(String paperBibTeX) {
        this.paperBibTeX = paperBibTeX;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
