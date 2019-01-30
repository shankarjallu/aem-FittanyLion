package com.fittanylion.aem.core.bean;

public class CustomerItem {
	public int relativeChance;
    public String firstname;
    public String lasttname;
    public String email;
    public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLasttname() {
		return lasttname;
	}
	public void setLasttname(String lasttname) {
		this.lasttname = lasttname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int customerId;
	public int getRelativeChance() {
		return relativeChance;
	}
	public void setRelativeChance(int relativeChance) {
		this.relativeChance = relativeChance;
	}
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	

}
