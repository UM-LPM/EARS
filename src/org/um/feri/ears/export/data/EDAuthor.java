package org.um.feri.ears.export.data;

import java.io.Serializable;

public class EDAuthor implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 6286602913886179602L;
    public String firstName; 
	public String lastName;
	public String nickName; //mandatory
	public String email; //mandatory used as id
	public String info; //data of original code author (if from other)
    @Override
    public String toString() {
        return "EDAuthor [firstName=" + firstName + ", lastName=" + lastName + ", nickName=" + nickName + ", email=" + email + ", info=" + info + "]";
    }
}
