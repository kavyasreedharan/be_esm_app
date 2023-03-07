package com.management.employee.esmapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.management.employee.esmapp.controller.EmployeeDataController;

/**
 * 
 * @author Kavya Sreedharan
 * This is the validator service implementation class for validations
 *
 */

@Service
public class ValidatorServiceImpl implements ValidatorService {

	Logger logger = LoggerFactory.getLogger(EmployeeDataController.class);

	/**
	 * This method is to validate if the content type of the uploaded file is CVS
	 * @param contentType
	 * @return validation result
	 */
	@Override
	public boolean validateUserFileType(String fileType) {
		logger.info("Content type => " + fileType);
		if(fileType.equals("text/csv")) {
			return true;
		} else {
			return false;
		}
	}

}
