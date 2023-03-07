package com.management.employee.esmapp.service;

import org.springframework.web.multipart.MultipartFile;

import com.management.employee.esmapp.model.Response;

/**
 * 
 * @author Kavya Sreedharan
 * This is the service interface for Employee Data
 *
 */
public interface EmployeeDataService {
	
	public Response extractEmployeeData(MultipartFile file);

}
