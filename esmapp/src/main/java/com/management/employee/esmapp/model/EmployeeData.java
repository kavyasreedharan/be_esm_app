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
	
	private String employeeId;
	
	private String employeeLogin;
	
	private String employeeName;
 
	private float employeeSalary;
}
