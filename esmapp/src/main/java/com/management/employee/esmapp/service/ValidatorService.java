package com.management.employee.esmapp.service;

/**
 * 
 * @author Kavya Sreedharan
 * This is the validator interface for validations
 *
 */

public interface ValidatorService {
	
	public boolean validateEmployeeFileType(String fileType);
	
	public boolean validateGetEmployeeDetailsRequest(int minSalary, int maxSalary, int offset, int limit, String columnHeader);

}
