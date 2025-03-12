package org.um.feri.ears.algorithms;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class AlgorithmInfo implements Serializable {
    private String acronym; // used as algorithm ID
    private String fullName;
    private String paperBibTeX; // BibTeX string of the original published paper
    private HashMap<String, String> customInfo = new HashMap<String, String>();

    public AlgorithmInfo(String acronym, String fullName, String paperBibTeX) {
        this.acronym = acronym;
        this.fullName = fullName;
        this.paperBibTeX = paperBibTeX;
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
