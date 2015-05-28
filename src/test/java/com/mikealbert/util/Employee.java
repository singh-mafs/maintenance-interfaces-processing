package com.mikealbert.util;

import java.util.List;

public class Employee {
	private String firstName;
	private String lastName;
	private int age;
	private String employeeNo;
	
	private List<EmployeeLocation> locs;
	
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public List<EmployeeLocation> getLocs() {
		return locs;
	}
	public void setLocs(List<EmployeeLocation> locs) {
		this.locs = locs;
	}
	
}
