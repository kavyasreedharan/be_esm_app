package com.management.employee.esmapp.service;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.management.employee.esmapp.model.EmployeeData;

/**
 * 
 * @author Kavya Sreedharan
 * This is the validator service implementation class for validations
 *
 */

@Service
public class ValidatorServiceImpl implements ValidatorService {

	Logger logger = LoggerFactory.getLogger(ValidatorServiceImpl.class);

	/**
	 * This method is to validate if the content type of the uploaded file is CVS
	 * @param contentType
	 * @return validation result
	 */
	@Override
	public boolean validateEmployeeFileType(String fileType) {
		logger.info("Content type of employee file upload => " + fileType);
		if(!StringUtils.isEmpty(fileType) && fileType.equals("text/csv")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is to validate all the request parameters data in get all employees details API
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
				logger.info("Get employees API request parameters validated successfully");
				return true;
			}
		}
		logger.error("Get employees API request parameters validation failed");
		return false;
	}

	/**
	 * This method is to validate all the request parameters data in get employee details API
	 * @param empId
	 * @param empData
	 * @return validation result
	 */
	@Override
	public boolean validateEmployeeDataRequest(String empId, EmployeeData empData) {
		if(!StringUtils.isEmpty(empId) && !StringUtils.isEmpty(empData.getId()) &&
				!StringUtils.isEmpty(empData.getLogin()) && !StringUtils.isEmpty(empData.getName()) &&
						(empData.getSalary() >= 0)) {
				logger.info("Employees API request parameters validated successfully");
				return true;
			}
		logger.error("Employees API request parameters validation failed");
		return false;
	}

	/**
	 * This method is to validate employee ID of API request
	 * @param empId
	 * @return validation result
	 */
	@Override
	public boolean validateEmployeeIdDataRequest(String empId) {
		if(!StringUtils.isEmpty(empId)) {
				logger.info("Employee ID for  API request parameters validated successfully");
				return true;
			}
		logger.error("Employee ID  for API request parameters validation failed");
		return false;
	}

}
