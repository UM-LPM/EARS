package org.um.feri.ears.export.data;

import java.io.Serializable;

public class EDPlayer implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -4571489341636728590L;
    public String acronym; // acronym 
    public String id_version; // if you made some semantic changes compared
    public String info;
    public EDRating newRating;
    public EDRating oldRating;
    public EDWinnLossDraw stat;
    public long benchmarkRunDuration; 
    @Override
    public String toString() {
        return "EDPlayer [acronym=" + acronym + ", id_version=" + id_version + ", info=" + info + ", newRating=" + newRating + ", oldRating=" + oldRating
                + ", stat=" + stat + "]";
    }
}
