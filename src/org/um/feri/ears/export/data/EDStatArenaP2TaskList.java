package org.um.feri.ears.export.data;

import java.io.Serializable;
import java.util.ArrayList;

public class EDStatArenaP2TaskList  implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 8603879528226167731L;
    public String benchmarkRunArenaID;;
    public ArrayList<EDStatP2TaskList> list;
    public EDStatArenaP2TaskList() {
        this("");
    }
    
    public EDStatArenaP2TaskList(String id) {
        list = new ArrayList<EDStatP2TaskList>();
        benchmarkRunArenaID = id;
    }

    @Override
    public String toString() {
        return "EDStatArenaP2TaskList [benchmarkRunArenaID=" + benchmarkRunArenaID + ", list=" + list + "]";
    }
}
