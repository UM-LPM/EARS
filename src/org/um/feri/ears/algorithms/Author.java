package org.um.feri.ears.algorithms;

import java.io.Serializable;

public class Author implements Serializable {
	private String firstName; 
	private String lastName;
	private String nickName; //mandatory
	private String email; //mandatory used as id
	private String info; //data of original code author (if from other)
	
	public Author(String firstName, String lastName, String nickName,
			String email, String info) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.email = email;
		this.info = info;
	}
	public Author(String nickName, String email) {
		super();
		this.nickName = nickName;
		this.email = email;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email.replaceAll(" at ", "@");
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
