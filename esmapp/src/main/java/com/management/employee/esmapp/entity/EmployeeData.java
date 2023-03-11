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
	@Column(name = "id", unique=true, nullable = false)
	private String id;
	
	@Column(name = "login", unique=true, nullable = false)
	private String login;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "salary", nullable = false)
	private float salary;

}
