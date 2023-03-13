package com.management.employee.esmapp.model;

import lombok.Data;

/**
 * 
 * @author Kavya Sreedharan
 * This is the model class for Employee Data
 *
 */

@Data
public class EmployeeData {
	
	private String id;
	
	private String login;
	
	private String name;
 
	private float salary;
	
	public EmployeeData() {
	}

	public EmployeeData(String id, String login, String name, float salary) {
		super();
		this.id = id;
		this.login = login;
		this.name = name;
		this.salary = salary;
	}
	
	
}
