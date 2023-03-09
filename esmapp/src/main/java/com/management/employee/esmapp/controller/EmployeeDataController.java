package com.management.employee.esmapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.management.employee.esmapp.model.Response;
import com.management.employee.esmapp.service.EmployeeDataService;
import com.management.employee.esmapp.service.ValidatorService;

/**
 * @author Kavya Sreedharan
 * 
 * This is the controller class for Employee Data CRUD Operations
 */


@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class EmployeeDataController {

	Logger logger = LoggerFactory.getLogger(EmployeeDataController.class);

	@Autowired
	private ValidatorService validatorService;

	@Autowired
	private EmployeeDataService employeeDataService;

	/**
	 * This method is used to upload user data into the database
	 * @param file
	 * @return responseCode and ResponseData
	 */
	@PostMapping(value="/users/upload", consumes={"multipart/form-data","text/csv"})
	public ResponseEntity<Response> userDataUpload(@RequestParam("file") MultipartFile file) {
		Response responseData= new Response();
		
		try {
			// Validate content type of file is CSV
			if(validatorService.validateUserFileType(file.getContentType())) {
				logger.info("userDataUpload request validation is successfull");
				
				responseData = employeeDataService.extractEmployeeData(file);
			} else {
				responseData.setResponseCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
				responseData.setMessage("Input file is not in CSV format");
			}
		} catch (Exception e) {
			logger.error("Error in userDataUpload => " + e.getMessage());
		}
		
		logger.info("Response of userDataUpload => " + responseData);
		
		return ResponseEntity.status(responseData.getResponseCode()).body(responseData);
	}

	/**
	 * This method is used to upload user data into the database
	 * @param file
	 * @return responseCode and ResponseData
	 */
	@GetMapping(value="/users")
	public ResponseEntity<Response> getUsersDetails(@RequestParam("minSalary") int minSalary,
			@RequestParam("maxSalary") int maxSalary,
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit,
			@RequestParam("sort") String columnHeader) {
		Response responseData= new Response();
		
		try {
			// Validate request parameters
			logger.info("getUsersDetails request parameters minSalary = " + minSalary + " maxSalary = " + 
					maxSalary + " offset = " + offset + " limit = " + limit + " columnHeader = " + columnHeader);
			
			if(validatorService.validateGetUsersDetailsRequest(minSalary, maxSalary, offset, limit, columnHeader)) {
				logger.info("getUsersDetails request validation is successfull");
				
				responseData = employeeDataService.getUserDetails(minSalary, maxSalary, offset, limit, columnHeader);
			} else {
				responseData.setResponseCode(HttpStatus.BAD_REQUEST.value());
				responseData.setMessage(HttpStatus.BAD_REQUEST.toString());
			}
		} catch (Exception e) {
			logger.error("Error in userDataUpload => " + e.getMessage());
		}
		
		logger.info("Response of userDataUpload => " + responseData);
		
		return ResponseEntity.status(responseData.getResponseCode()).body(responseData);
	}

}
