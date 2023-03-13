package com.management.employee.esmapp.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author Kavya Sreedharan
 * This is the model class for Employee Data
 *
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class EmployeeDataResponse extends Response{
	
	private long totalElements;
	
	private List<EmployeeData> results;
	
	private EmployeeData empDataRecord;

	
}
