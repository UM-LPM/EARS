package org.um.feri.ears.export.data;

import java.io.Serializable;
import java.util.ArrayList;

public class EDStatP2PList implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -903602227831017256L;
    public String benchmarkRunArenaID;
    public ArrayList<EDStatPlayer2Player> list;
    public EDStatP2PList() {
        this("");
    }
    public EDStatP2PList(String id) {
        list = new ArrayList<EDStatPlayer2Player>();
        benchmarkRunArenaID = id;
    }
    @Override
    public String toString() {
        return "EDStatP2PList [benchmarkRunArenaID=" + benchmarkRunArenaID + ", list=" + list + "]";
    }
}
