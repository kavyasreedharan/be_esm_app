package com.management.employee.esmapp.service;

import com.management.employee.esmapp.model.EmployeeData;

/**
 * 
 * @author Kavya Sreedharan
 * This is the validator interface for validations
 *
 */

public interface ValidatorService {
	
	public boolean validateEmployeeFileType(String fileType);
	
	public boolean validateGetEmployeeDetailsRequest(int minSalary, int maxSalary, int offset, int limit, String columnHeader);
	
	public boolean validateEmployeeDataRequest(String empId, EmployeeData empData);
	
	public boolean validateEmployeeIdDataRequest(String empId);

}
