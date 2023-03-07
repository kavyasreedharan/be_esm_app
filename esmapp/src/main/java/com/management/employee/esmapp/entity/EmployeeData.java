package com.management.employee.esmapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 
 * @author Kavya Sreedharan
 * This is the entity class for Employee Data
 *
 */
@Data
@Entity
@Table(name = "esm_employee_data")
public class EmployeeData {
	
	@Id
	@Column(name = "emp_id", unique=true, nullable = false)
	private String employeeId;
	
	@Column(name = "emp_login", unique=true, nullable = false)
	private String employeeLogin;
	
	@Column(name = "emp_name", unique=true, nullable = false)
	private String employeeName;
	
	@Column(name = "emp_salary", unique=true, nullable = false)
	private float employeeSalary;

}
