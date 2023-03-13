package com.management.employee.esmapp.service;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
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
	public boolean validateEmployeeFileType(String fileType) {
		logger.info("Content type => " + fileType);
		if(!StringUtils.isEmpty(fileType) && fileType.equals("text/csv")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is to validate all the request parameters data in get user details API
	 * @param minSalary
	 * @param maxSalary
	 * @param offset
	 * @param limit
	 * @param columnHeader
	 * @return validation result
	 */
	@Override
	public boolean validateGetEmployeeDetailsRequest(int minSalary, int maxSalary, int offset, int limit, String columnHeader) {
		if(minSalary >=0 && maxSalary > 0 && offset >= 0 && limit > 0) {
			if(!StringUtils.isEmpty(columnHeader) &&Stream.of("id","login", "name", "salary").anyMatch(columnHeader.substring(1, columnHeader.length())::equalsIgnoreCase)) {
				logger.info("Get users API request parameters validated successfully");
				return true;
			}
		}
		logger.error("Get users API request parameters validation failed");
		return false;
	}

}
